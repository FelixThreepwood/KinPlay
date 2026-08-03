# KinPlay Batch A9A144CD release record

Status: BLOCKED_PENDING_GOOGLE_DRIVE_AUTH
Last updated: 2026-08-03T23:50:41Z
Batch: `KP-BATCH-A9A144CD-D6AE-47E6-8C6F-4F14214377E4`
Target release: `0.6.0-beta1`

## Release candidate

- Package: `com.kinplay.app`
- Version name: `0.6.0-beta1`
- Version code: `6`
- APK filename: `20260803_KinPlay_v0.6.0-beta1_MVP.apk`
- Local APK: `/mnt/cyberforgex-torrents/KinPlay/apk-drops/20260803_KinPlay_v0.6.0-beta1_MVP.apk`
- Size: `11,674,099` bytes
- SHA-256: `069dadf06a5d79d1a485694219cfd799169f26cab126cf359ae10bca72f13ae8`
- Previous local root APK: `20260727_KinPlay_v0.5.0-beta1_MVP.apk`
- Previous local root archive: `/mnt/cyberforgex-torrents/KinPlay/apk-drops/beta-testing/20260727_KinPlay_v0.5.0-beta1_MVP.apk`
- Google Drive target folder ID: `1JISfojmmxDuLKhx5XZ91at1lEi0TwWxk`
- Google Drive file ID/link: not available; publication is blocked by revoked OAuth credentials.

## Source publication checkpoint

- Implementation branch: `feat/kinplay-0.6.0-beta1-feedback`
- Validated source checkpoint: `6f35f071b49b2662764de57519e466f3f47aee5f`
- Feature branch remote verification: `origin/feat/kinplay-0.6.0-beta1-feedback` resolves to `c2a49116129c0088b136818d213ac46b0e5617d3`.
- Final branch: `main` is fast-forwarded to `c2a49116129c0088b136818d213ac46b0e5617d3` and pushed to `origin/main`; dual-destination publication remains blocked only by Google OAuth reauthentication.

## Validation evidence

Passed:

- `kinplay_offline_validate.py --mode quick`
- `kinplay_offline_validate.py --mode checkpoint`
- `kinplay_offline_validate.py --mode release`
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

The configured Google OAuth token at the Hermes profile expired/revoked and refresh returned `invalid_grant`. The Google client dependencies were supplied through `uv` for diagnosis. A second fresh consent flow was generated and opened in Firefox at 2026-08-03T23:30Z; Google still presents a signed-out account chooser, so the account holder must complete interactive sign-in and consent. A localhost:1 callback listener is running to capture the redirect without exposing it in chat. No Drive upload, download verification, or cloud trash operation has been claimed or performed. Source publication is complete and verified at `origin/main` commit `c2a49116129c0088b136818d213ac46b0e5617d3`. Resume B18 after reauthentication, upload to the exact folder, download the returned object, and compare its size and SHA-256 to the local APK before replacing this blocked record with the completed release record.
