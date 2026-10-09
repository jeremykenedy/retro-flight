package com.jeremykenedy.retroflight;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import java.util.Random;

final class RetroFlightSceneView extends View {
  private static final int[] COLORS = {0xffff44c8, 0xff9a5dff, 0xff28e6ff, 0xffff9e4a};
  private static final int[] HORIZONS = {0xffff5bd8, 0xffc781ff, 0xff33dfff, 0xffffa04c};
  private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Random random = new Random(42017L);
  private final FlightOptions options;
  private final float[] starX;
  private final float[] starY;
  private final float[] starDepth;
  private LinearGradient background;
  private RadialGradient planet;
  private RadialGradient planetSurface;
  private final RectF orbit = new RectF();
  private float planetRadius;
  private long startedAt;
  private boolean running;
  private float horizon;
  private float unit;

  RetroFlightSceneView(Context context) {
    super(context);
    setLayerType(View.LAYER_TYPE_HARDWARE, null);
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    options =
        FlightOptions.resolve(
            preferences.getString("stars", "balanced"),
            preferences.getString("speed", "normal"),
            preferences.getString("palette", "synthwave"),
            preferences.getString("field", "grid"),
            preferences.getString("glow", "balanced"),
            preferences.getBoolean("randomize_all", false),
            new Random(System.currentTimeMillis()));
    starX = new float[options.starCount];
    starY = new float[options.starCount];
    starDepth = new float[options.starCount];
    for (int i = 0; i < starX.length; i++) {
      starX[i] = random.nextFloat();
      starY[i] = random.nextFloat();
      starDepth[i] = random.nextFloat();
    }
  }

  void start() {
    if (!running) {
      running = true;
      startedAt = SystemClock.uptimeMillis();
      postInvalidateOnAnimation();
    }
  }

  void stop() {
    running = false;
    removeCallbacks(invalidator);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (getWidth() == 0 || getHeight() == 0) return;
    float time = (SystemClock.uptimeMillis() - startedAt) / 1000f;
    drawSky(canvas);
    drawPlanet(canvas, time);
    drawStars(canvas, time);
    if (options.grid) drawGrid(canvas, time);
    drawHorizon(canvas, time);
    if (running) postDelayed(invalidator, 33L);
  }

  @Override
  protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
    super.onSizeChanged(width, height, oldWidth, oldHeight);
    if (width <= 0 || height <= 0) return;
    unit = Math.min(width, height) / 1080f;
    horizon = height * 0.57f;
    int top = options.palette == 3 ? 0xff16090b : 0xff08091a;
    int bottom = options.palette == 2 ? 0xff05131d : 0xff03040d;
    background = new LinearGradient(0, 0, 0, height, top, bottom, Shader.TileMode.CLAMP);
    int color = COLORS[options.palette];
    planetRadius = Math.min(width, height) * 0.115f;
    planetSurface =
        new RadialGradient(
            0f,
            0f,
            planetRadius,
            new int[] {withAlpha(color, 245), withAlpha(color, 80), 0x00101020},
            new float[] {0f, 0.83f, 1f},
            Shader.TileMode.CLAMP);
    planet =
        new RadialGradient(
            width * 0.72f,
            height * 0.34f,
            height * 0.48f,
            new int[] {withAlpha(color, 82), withAlpha(color, 18), withAlpha(color, 0)},
            new float[] {0f, 0.48f, 1f},
            Shader.TileMode.CLAMP);
  }

  private final Runnable invalidator =
      new Runnable() {
        @Override
        public void run() {
          if (running) invalidate();
        }
      };

  private void drawSky(Canvas canvas) {
    paint.setShader(background);
    canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    paint.setShader(planet);
    canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    paint.setShader(null);
  }

  private void drawPlanet(Canvas canvas, float time) {
    float width = getWidth();
    float height = getHeight();
    float x = width * (0.72f + 0.018f * (float) Math.sin(time * 0.045f));
    float y = height * 0.34f;
    float radius = planetRadius;
    canvas.save();
    canvas.translate(x, y);
    paint.setShader(planetSurface);
    canvas.drawCircle(0f, 0f, radius, paint);
    canvas.restore();
    paint.setShader(null);
    paint.setColor(HORIZONS[options.palette]);
    paint.setAlpha((int) (110 * options.glow));
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(Math.max(1f, unit * 2.2f));
    orbit.set(x - radius * 1.65f, y - radius * 0.48f, x + radius * 1.65f, y + radius * 0.48f);
    canvas.drawOval(orbit, paint);
    paint.setStyle(Paint.Style.FILL);
    paint.setAlpha(255);
  }

  private void drawStars(Canvas canvas, float time) {
    float centerX = getWidth() * 0.5f;
    float centerY = horizon;
    int color = options.palette == 2 ? 0xffa5f4ff : options.palette == 3 ? 0xffffd4a2 : 0xffd6d5ff;
    for (int i = 0; i < starX.length; i++) {
      float depth = (starDepth[i] + time * options.speed) % 1f;
      float x = centerX + (starX[i] - 0.5f) * getWidth() * depth * 1.65f;
      float y = centerY + (starY[i] - 0.5f) * getHeight() * depth * 1.75f;
      if (y < 0 || y > getHeight() || x < 0 || x > getWidth()) continue;
      float radius = (0.6f + depth * 2.2f) * unit;
      paint.setColor(color);
      paint.setAlpha((int) ((70 + depth * 180) * options.glow));
      paint.setStrokeWidth(Math.max(1f, radius));
      canvas.drawLine(
          centerX + (x - centerX) * Math.max(0f, depth - 0.018f),
          centerY + (y - centerY) * Math.max(0f, depth - 0.018f),
          x,
          y,
          paint);
      canvas.drawCircle(x, y, radius, paint);
    }
    paint.setAlpha(255);
  }

  private void drawGrid(Canvas canvas, float time) {
    float width = getWidth();
    float height = getHeight();
    float center = width * 0.5f;
    int color = HORIZONS[options.palette];
    paint.setColor(color);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(Math.max(1f, unit * 1.2f));
    for (int line = -7; line <= 7; line++) {
      float end = center + line * width * 0.14f;
      paint.setAlpha(line == 0 ? 92 : 54);
      canvas.drawLine(center, horizon, end, height, paint);
    }
    float phase = (time * options.speed * 0.68f) % 1f;
    for (int line = 0; line < 22; line++) {
      float depth = (line / 22f + phase) % 1f;
      float y = horizon + depth * depth * (height - horizon);
      float alpha = 35f + depth * 100f;
      paint.setAlpha((int) Math.min(140f, alpha * options.glow));
      canvas.drawLine(0, y, width, y, paint);
    }
    paint.setStyle(Paint.Style.FILL);
    paint.setAlpha(255);
  }

  private void drawHorizon(Canvas canvas, float time) {
    int color = HORIZONS[options.palette];
    float pulse = 0.75f + 0.25f * (float) Math.sin(time * 0.7f);
    paint.setColor(color);
    paint.setAlpha((int) (170f * options.glow * pulse));
    paint.setStrokeWidth(Math.max(1f, unit * 2f));
    canvas.drawLine(0, horizon, getWidth(), horizon, paint);
    paint.setAlpha(255);
  }

  private static int withAlpha(int color, int alpha) {
    return (color & 0x00ffffff) | (Math.min(255, Math.max(0, alpha)) << 24);
  }
}
