# KinPlay Batch A9A144CD release record

Status: RELEASED_TO_SHARED_AND_GOOGLE_DRIVE
Last updated: 2026-08-05T01:31:22Z
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
- Google Drive target folder ID: `12bINCtZHQwvh3-mIbQ2x-swPE6ACzjYp`
- Google Drive file ID/link: `1yj5w7xo-5okqRUiMDmHl-fL0MGERP_m3` / https://drive.google.com/file/d/1yj5w7xo-5okqRUiMDmHl-fL0MGERP_m3/view?usp=drivesdk

## Verified dual publication

- Google Workspace authentication: `AUTHENTICATED`; token refresh is operational.
- Google Drive object name: `20260804_KinPlay_v0.6.0-beta1_MVP.apk`
- Google Drive object size: `11,674,099` bytes
- Google Drive object parent: `12bINCtZHQwvh3-mIbQ2x-swPE6ACzjYp`
- Downloaded Drive object SHA-256: `069dadf06a5d79d1a485694219cfd799169f26cab126cf359ae10bca72f13ae8`
- Local APK SHA-256: `069dadf06a5d79d1a485694219cfd799169f26cab126cf359ae10bca72f13ae8`
- Final active APK count in the Drive folder: exactly one.
- Superseded cloud APK `20260727_KinPlay_v0.5.0-beta1_MVP.apk` was moved to Drive trash only after new-object verification.

## Source publication checkpoint

- Implementation branch: `feat/kinplay-0.6.0-beta1-feedback`
- Validated source checkpoint: `6f35f071b49b2662764de57519e466f3f47aee5f`
- Feature branch remote verification: `origin/feat/kinplay-0.6.0-beta1-feedback` resolves to `76d0b846e6d367de89b82c1bbbc8cabbaeba2252`.
- Final branch: `main` is pushed to `origin/main` at final documentation checkpoint `7612cb38f9d28b8410ecdb0e320060688da2af9b` and contains the verified source-publication checkpoint; dual-destination publication is now verified.

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

## Completion status

B18 dual publication is complete and independently verified. The only remaining project limitation is physical-device/family retesting, which is performed by the project owner. No Android device or emulator was available to this environment, so no device-test pass is claimed.
