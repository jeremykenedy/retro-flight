# Continuous integration

GitHub Actions validates the Android build, host settings tests, installer tests, exact 100% line and branch coverage gates, Java formatting, documentation links, and app runtime privacy. CI builds unsigned review APKs. Release keys stay outside public CI. A release is signed outside public CI and includes a SHA-256 checksum file.

GitGuardian and SonarCloud workflows run when their repository secrets are configured. Provider credentials stay in GitHub Actions secrets. Do not add token values to source, artifacts, or logs.
