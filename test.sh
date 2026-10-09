#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
OUT="$ROOT/build/tests"
mkdir -p "$OUT"
javac -source 8 -target 8 -d "$OUT" \
  "$ROOT/src/com/jeremykenedy/retroflight/FlightOptions.java" \
  "$ROOT/src/com/jeremykenedy/retroflight/SettingsValues.java" \
  "$ROOT/tests/FlightOptionsAssertions.java"
java -ea -cp "$OUT" com.jeremykenedy.retroflight.FlightOptionsAssertions
python3 -m unittest -v tests.test_installer
