package com.jeremykenedy.retroflight;

import java.util.Random;

public final class FlightOptionsAssertions {
  private FlightOptionsAssertions() {}

  public static void main(String[] args) {
    densityValuesAreBounded();
    speedValuesAreSupported();
    paletteValuesAreSupported();
    glowValuesAreSupported();
    selectionsCanBeRandomized();
    settingsProviderValuesAreValidated();
    System.out.println("Retro Flight settings tests passed.");
  }

  private static void densityValuesAreBounded() {
    check(FlightOptions.countFor("sparse") == 150, "sparse count");
    check(FlightOptions.countFor("balanced") == 300, "balanced count");
    check(FlightOptions.countFor("dense") == 480, "dense count");
    check(FlightOptions.countFor("packed") == 760, "packed count");
    check(FlightOptions.countFor("unknown") == 300, "unknown count fallback");
  }

  private static void speedValuesAreSupported() {
    check(FlightOptions.speedFor("slow") == 0.055f, "slow speed");
    check(FlightOptions.speedFor("normal") == 0.105f, "normal speed");
    check(FlightOptions.speedFor("fast") == 0.19f, "fast speed");
    check(FlightOptions.speedFor("unknown") == 0.105f, "unknown speed fallback");
  }

  private static void paletteValuesAreSupported() {
    check(FlightOptions.paletteFor("synthwave") == 0, "synthwave palette");
    check(FlightOptions.paletteFor("ultraviolet") == 1, "ultraviolet palette");
    check(FlightOptions.paletteFor("aqua") == 2, "aqua palette");
    check(FlightOptions.paletteFor("ember") == 3, "ember palette");
    check(FlightOptions.paletteFor("unknown") == 0, "unknown palette fallback");
  }

  private static void glowValuesAreSupported() {
    check(FlightOptions.glowFor("low") == 0.55f, "low glow");
    check(FlightOptions.glowFor("balanced") == 0.9f, "balanced glow");
    check(FlightOptions.glowFor("high") == 1.35f, "high glow");
    check(FlightOptions.glowFor("unknown") == 0.9f, "unknown glow fallback");
  }

  private static void selectionsCanBeRandomized() {
    Random random = new Random(1234L);
    FlightOptions explicit =
        FlightOptions.resolve("packed", "slow", "aqua", "open", "low", false, random);
    check(
        explicit.starCount == 760
            && explicit.speed == 0.055f
            && explicit.palette == 2
            && !explicit.grid
            && explicit.glow == 0.55f,
        "explicit settings");

    FlightOptions fallback =
        FlightOptions.resolve("invalid", "invalid", "invalid", "invalid", "invalid", false, random);
    check(
        fallback.starCount == 300
            && fallback.speed == 0.105f
            && fallback.palette == 0
            && fallback.grid
            && fallback.glow == 0.9f,
        "invalid values use safe defaults");

    FlightOptions allRandom =
        FlightOptions.resolve("balanced", "normal", "synthwave", "grid", "balanced", true, random);
    check(
        allRandom.starCount > 0
            && allRandom.speed > 0
            && allRandom.palette >= 0
            && allRandom.palette < 4,
        "global random choices stay in bounds");
    for (int i = 0; i < 200; i++) {
      FlightOptions individualRandom =
          FlightOptions.resolve("random", "random", "random", "random", "random", false, random);
      check(
          individualRandom.starCount > 0
              && individualRandom.speed > 0
              && individualRandom.palette >= 0
              && individualRandom.palette < 4,
          "individual random choices stay in bounds");
    }
    check(
        FlightOptions.choose("normal", false, random, "slow", "normal", "fast").equals("normal"),
        "supported selection is preserved");
  }

  private static void settingsProviderValuesAreValidated() {
    check(!SettingsValues.isSupported(null, "balanced"), "null key rejected");
    check(!SettingsValues.isSupported("stars", null), "null value rejected");
    check(SettingsValues.isSupported("stars", "packed"), "star density accepted");
    check(SettingsValues.isSupported("speed", "fast"), "speed accepted");
    check(SettingsValues.isSupported("palette", "ultraviolet"), "palette accepted");
    check(SettingsValues.isSupported("field", "open"), "field accepted");
    check(SettingsValues.isSupported("glow", "high"), "glow accepted");
    check(SettingsValues.isSupported("randomize_all", "true"), "global random accepted");
    check(!SettingsValues.isSupported("stars", "too-many"), "invalid density rejected");
    check(!SettingsValues.isSupported("unknown", "x"), "unknown key rejected");
  }

  private static void check(boolean condition, String message) {
    if (!condition) throw new AssertionError(message);
  }
}
