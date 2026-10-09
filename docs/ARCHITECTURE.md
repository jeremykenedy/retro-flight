# Architecture

Retro Flight is a native Android application with an Android `DreamService`, a remote-friendly settings activity, and a small settings content provider for host applications. The scene is drawn procedurally with Android Canvas. It animates star particles moving outward from a vanishing point, a softly drifting ringed planet, a horizon glow, and an optional perspective grid.

The renderer allocates its star buffers once per view and reuses a single paint. It requests a frame only while the preview or screensaver is active and stops scheduling frames when the Activity or dream pauses. No video decoding, third-party runtime libraries, network access, wake locks, or background services are used.

The pure `FlightOptions` class resolves choices into bounded rendering values. `SettingsValues` validates updates exposed by the provider. The host test suite covers both with full line and branch coverage. Android framework callbacks and pixel output are verified through APK inspection and the Android TV emulator.

The standalone Python installer is separate from the Android app. It downloads only user-requested release assets from this repository, follows allowlisted GitHub asset redirects, validates the APK checksum and file size, and invokes ADB. It does not change screensaver selection or system power/update settings.
