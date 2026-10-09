# Configuration

Open Retro Flight from the TV launcher or device screensaver settings. Settings use the remote and take effect when the next screensaver session begins. Each choice supports `Random`; the global toggle selects all options again each time the screensaver starts.

| Setting | Default | Choices |
|---|---|---|
| Star density | Balanced | Sparse, Balanced, Dense, Packed, Random |
| Flight speed | Cruising | Slow, Cruising, Fast, Random |
| Color palette | Synthwave | Synthwave, Ultraviolet, Aqua, Ember, Random |
| Perspective field | Neon grid | Neon grid, Open space, Random |
| Glow intensity | Balanced | Low, Balanced, High, Random |
| Randomize all settings each start | Off | Off, On |

The provider is read-only for queries and accepts a validated setting update. Consumers can discover the setting schema and values at:

- `content://com.jeremykenedy.retroflight.settings/schema`
- `content://com.jeremykenedy.retroflight.settings/settings`

Update one option by calling `ContentResolver.update()` on `/settings` with `ContentValues` containing `key` and `value`. Values not listed in the schema are rejected. The schema exposes `key`, `title`, `type`, `default`, `choices`, and `randomAllowed`. The settings cursor exposes `key` and `value`. The `randomize_all` field is a boolean string and all other values are strings.
