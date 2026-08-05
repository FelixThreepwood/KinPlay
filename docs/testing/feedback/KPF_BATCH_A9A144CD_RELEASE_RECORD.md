# KinPlay Batch A9A144CD release record

Status: BLOCKED_PENDING_GOOGLE_DRIVE_AUTH
Last updated: 2026-08-05T00:20:04Z
Batch: `KP-BATCH-A9A144CD-D6AE-47E6-8C6F-4F14214377E4`
Target release: `0.6.0-beta1`

## Release candidate

- Package: `com.kinplay.app`
- Version name: `0.6.0-beta1`
- Version code: `6`
- APK filename: `20260804_KinPlay_v0.6.0-beta1_MVP.apk`
- Local APK: `/mnt/cyberforgex-torrents/KinPlay/apk-drops/20260804_KinPlay_v0.6.0-beta1_MVP.apk`
- Size: `11,674,099` bytes
- SHA-256: `069dadf06a5d79d1a485694219cfd799169f26cab126cf359ae10bca72f13ae8`
- Previous local root APK: `20260727_KinPlay_v0.5.0-beta1_MVP.apk`
- Previous local root archive: `/mnt/cyberforgex-torrents/KinPlay/apk-drops/beta-testing/20260727_KinPlay_v0.5.0-beta1_MVP.apk`
- Google Drive target folder ID: `1JISfojmmxDuLKhx5XZ91at1lEi0TwWxk`
- Google Drive file ID/link: not available; publication is blocked at the account password step of OAuth reauthentication.

## Source publication checkpoint

- Implementation branch: `feat/kinplay-0.6.0-beta1-feedback`
- Validated source checkpoint: `6f35f071b49b2662764de57519e466f3f47aee5f`
- Feature branch remote verification: `origin/feat/kinplay-0.6.0-beta1-feedback` resolves to `76d0b846e6d367de89b82c1bbbc8cabbaeba2252`.
- Final branch: `main` is pushed to `origin/main` at current documentation checkpoint `b5d50d01a75ae6b2fc0f606bc6e7af7e07c1972f` and contains the verified source-publication checkpoint plus later durable B18 documentation checkpoints; dual-destination publication remains blocked only by Google OAuth reauthentication.

## Validation evidence

Passed:

- `kinplay_offline_validate.py --mode quick`
- `kinplay_offline_validate.py --mode checkpoint`
- `kinplay_offline_validate.py --mode release`
- Fresh 2026-08-04 release validator passed at 20:21:19Z; log: `/home/phantomatic/.hermes/logs/kinplay-validation/20260804-131802-release.log`
- Explicit `./gradlew --no-daemon testDebugUnitTest --rerun-tasks`
- Full JVM suite: 186/186
- Android instrumentation test-source compilation
- Debug APK assembly
- Android lint
- JSON Schema, canonical/runtime byte parity, and packaged APK content parity
- `git diff --check`
- APK metadata: package `com.kinplay.app`, versionCode 6, versionName `0.6.0-beta1`, minSdk 26, targetSdk 35
- APK permissions: only the AndroidX dynamic receiver not-exported permission; no Internet, camera, microphone, contacts, location, or storage permission
- APK Signature Scheme v2 verification
- Source APK, temporary local upload, and final local destination SHA-256 equality
- Exactly one current APK in the local `apk-drops` root; superseded root APK archived
- Independent read-only review: PASS; no security concerns or logic errors

Not executed:

- Android instrumentation/device tests: `adb devices -l` reported no attached device or emulator.
- Physical timer, lifecycle, lock, accessibility, orientation, visual, and launcher-cache retests remain pending.

## KPF status

All 22 canonical items linked to this batch are implementation-complete with automated evidence and remain `fixed-awaiting-retest` pending physical-device/family confirmation:

`KPF-0006`, `KPF-0008`, `KPF-0009`, `KPF-0018`, `KPF-0019`, `KPF-0021`, `KPF-0022`, `KPF-0023`, `KPF-0024`, `KPF-0025`, `KPF-0026`, `KPF-0027`, `KPF-0028`, `KPF-0029`, `KPF-0030`, `KPF-0031`, `KPF-0032`, `KPF-0033`, `KPF-0034`, `KPF-0035`, `KPF-0036`, `KPF-0037`.

`KPF-0020` is excluded from this batch.

## Blocking condition

The configured Google OAuth token at the Hermes profile expired/revoked; a fresh `uv`-backed setup check returned `TOKEN_REVOKED` with `invalid_grant`. A fresh PKCE consent session remains open in Firefox. The state-checked local callback bridge is listening on `127.0.0.1:1` with status `waiting_for_callback`, and the verification-first publisher is running. Google still requires the account holder to enter authentication and complete consent; the agent entered no credentials. No Drive upload, download verification, or cloud trash operation has been claimed or performed. Source publication is complete and verified at `origin/main`; after the matching callback arrives, the publisher will upload to the exact folder, download the returned object, compare its size and SHA-256 to the local APK, then trash superseded cloud APKs only after verification.
