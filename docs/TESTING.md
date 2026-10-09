# Testing and coverage

Run all local checks from the repository root:

```bash
bash test.sh
bash scripts/test-python-coverage.sh
bash scripts/test-coverage.sh
bash build.sh
python3 scripts/check-docs.py
python3 scripts/check-privacy.py
bash scripts/check-style.sh
```

The Java coverage task measures only the pure settings resolver and provider-value validator; JaCoCo requires 100% line and branch coverage. The installer has a separate 100% line and branch coverage gate. Android framework code is excluded from the host percentage because it requires the Android runtime. It is checked through APK metadata validation, emulator installation, settings interaction, provider query/update, DreamService activation, and inspected screenshots.

The tests check supported and unknown setting values, individual and global randomization, checksum parsing and rejection, release URL restrictions, trusted redirect handling, ambiguous devices, cancellation, explicit serial selection, install/update, and protected non-interactive uninstall. They do not claim physical vendor-menu or 4K hardware coverage.
