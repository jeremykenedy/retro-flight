package com.jeremykenedy.retroflight;

import java.util.Random;

public final class FlightOptions {
  public final int starCount;
  public final float speed;
  public final int palette;
  public final boolean grid;
  public final float glow;

  private FlightOptions(int starCount, float speed, int palette, boolean grid, float glow) {
    this.starCount = starCount;
    this.speed = speed;
    this.palette = palette;
    this.grid = grid;
    this.glow = glow;
  }

  public static FlightOptions resolve(
      String stars,
      String speed,
      String palette,
      String field,
      String glow,
      boolean randomizeAll,
      Random random) {
    String selectedStars =
        choose(stars, randomizeAll, random, "balanced", "sparse", "dense", "packed");
    String selectedSpeed = choose(speed, randomizeAll, random, "normal", "slow", "fast");
    String selectedPalette =
        choose(palette, randomizeAll, random, "synthwave", "ultraviolet", "aqua", "ember");
    String selectedField = choose(field, randomizeAll, random, "grid", "open");
    String selectedGlow = choose(glow, randomizeAll, random, "balanced", "low", "high");
    return new FlightOptions(
        countFor(selectedStars),
        speedFor(selectedSpeed),
        paletteFor(selectedPalette),
        "grid".equals(selectedField),
        glowFor(selectedGlow));
  }

  static String choose(String selected, boolean randomizeAll, Random random, String... values) {
    if (randomizeAll || "random".equals(selected)) return values[random.nextInt(values.length)];
    for (String value : values) if (value.equals(selected)) return selected;
    return values[0];
  }

  static int countFor(String density) {
    if ("sparse".equals(density)) return 150;
    if ("dense".equals(density)) return 480;
    if ("packed".equals(density)) return 760;
    return 300;
  }

  static float speedFor(String speed) {
    if ("slow".equals(speed)) return 0.055f;
    if ("fast".equals(speed)) return 0.19f;
    return 0.105f;
  }

  static int paletteFor(String palette) {
    if ("ultraviolet".equals(palette)) return 1;
    if ("aqua".equals(palette)) return 2;
    if ("ember".equals(palette)) return 3;
    return 0;
  }

  static float glowFor(String glow) {
    if ("low".equals(glow)) return 0.55f;
    if ("high".equals(glow)) return 1.35f;
    return 0.9f;
  }
}
