package com.jeremykenedy.retroflight;

public final class SettingsValues {
  private SettingsValues() {}

  public static boolean isSupported(String key, String value) {
    if (key == null || value == null) return false;
    if ("stars".equals(key)) return oneOf(value, "sparse", "balanced", "dense", "packed", "random");
    if ("speed".equals(key)) return oneOf(value, "slow", "normal", "fast", "random");
    if ("palette".equals(key))
      return oneOf(value, "synthwave", "ultraviolet", "aqua", "ember", "random");
    if ("field".equals(key)) return oneOf(value, "grid", "open", "random");
    if ("glow".equals(key)) return oneOf(value, "low", "balanced", "high", "random");
    if ("randomize_all".equals(key)) return oneOf(value, "true", "false");
    return false;
  }

  private static boolean oneOf(String value, String... allowed) {
    for (String option : allowed) if (option.equals(value)) return true;
    return false;
  }
}
