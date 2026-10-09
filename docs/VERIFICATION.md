# Device verification

## Emulator checks

| Device | OS/API | Resolution | Verified |
|---|---|---:|---|
| Android TV emulator (`sdk_google_atv64_arm64`) | Android 12, API 31 | 1920x1080 | Install and launch, settings with D-pad, preview rendering, DreamService test activation, content-provider schema/read/update, APK identity, and scene screenshot. |

The emulator was model `sdk_google_atv64_arm64` with Android 12/API 31. The screensaver component was activated through Android Dream Manager test mode after setting it as the active component, then left running for 45 seconds before the final scene capture. The prior device screensaver component, enabled state, activate-on-sleep state, and timeout were recorded and restored after testing.

The scene capture used Balanced star density, Cruising speed, Aqua palette, Neon grid, Balanced glow, and global randomization off. The settings screenshot shows the default values. Both images are original captures from the running app, with no retouching. The source scene PNG is 1920x1080 with SHA-256 `aa21a5f3cc4bb2f894165561d17d9629ce67d6a172129d973b2ae5ad9c5feb15`. The in-app preview is a 384x216 JPEG derived from that capture with SHA-256 `24e4341d446d78eb04cf7c88f65775bdc180c2f1b94bcc5f18bebc47e8702892`.

## Physical device matrix

| Platform | Status | Test request |
|---|---|---|
| Fire TV | Untested on physical hardware | We are looking for a Fire TV owner to test install, screensaver selection and idle activation, remote settings, update, uninstall, and sleep/wake behavior. Please report model, Fire OS/API, display resolution, steps, and results through the [issue tracker](https://github.com/jeremykenedy/retro-flight/issues). |
| Android TV | Untested on physical hardware | We are looking for an Android TV owner to test install, screensaver selection and idle activation, remote settings, update, uninstall, and sleep/wake behavior. Please report model, Android version/API, display resolution, steps, and results through the [issue tracker](https://github.com/jeremykenedy/retro-flight/issues). |
| Google TV | Untested on physical hardware | We are looking for a Google TV owner to test install, ambient display selection and idle activation, remote settings, update, uninstall, and sleep/wake behavior. Please report model, Android version/API, display resolution, steps, and results through the [issue tracker](https://github.com/jeremykenedy/retro-flight/issues). |

The emulator verifies that the DreamService renders on an Android TV system image. It does not verify Fire OS behavior, physical TV hardware decoding/composition, native 4K output, thermal behavior, or activation menus on vendor devices. No physical TV was used for this release.
