# KP-PRO-037 integration and release evidence

Date: 2026-09-12
Package: `com.kinplay.app`
Visible app name: `KidPlay`

## Scope

This release integrates the completed baseline-remediation lanes already present on `main`:

- `183165d` — connected-test regression contracts;
- `28861f8` — visible KidPlay branding reconciliation;
- `4da6d03` — lifecycle-safe bundled music controls;
- `6826cf9` — Android visual-interaction audit harness.

The integration release increments KidPlay exactly once from version `0.7.3` / code `16` to version `0.7.4` / code `17`. The in-app release summary is six words: `Integrated branding, music, and audit remediation`.

## Source and deterministic gates

- Integration commit: `005383060c131713977e8fe86fcb3c4f6191118e` (`fix(kinplay): integrate baseline remediation release`).
- `./gradlew --no-daemon :app:testDebugUnitTest :app:compileDebugAndroidTestKotlin :app:lintDebug :app:assembleDebug`: passed.
- JVM result: `235` tests, `0` failures, `0` errors, `0` skipped.
- Android-test source compilation: passed.
- Android lint: passed.
- Debug assembly: passed.
- `python3 -m unittest discover -s scripts -p 'test_*.py' -v`: `13` passed.
- Python compilation: passed for `scripts/android_audit.py` and `scripts/test_android_audit.py`.
- Shell syntax: passed for `scripts/android-audit.sh`.
- JSON schema, canonical/runtime byte parity, unique IDs, and packaged asset parity: passed.
- `git diff --check` and staged diff checks: passed.
- Added-line security scan: no secret, shell-injection, eval/exec, unsafe-deserialization, or SQL-injection hits.

## APK evidence

- Build output: `app/build/outputs/apk/debug/app-debug.apk`.
- Final size: `25,535,866` bytes.
- Final SHA-256: `a5171e637316e56ad3e8e9304764f899716a63bd9673396d9c753da674c7870c`.
- `aapt dump badging`: package `com.kinplay.app`, version `0.7.4`, code `17`, min SDK `26`, target SDK `35`, label `KidPlay`.
- APK permissions: only `com.kinplay.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION`.
- `apksigner verify --verbose`: v2 signature verified; one signer.
- A prior same-version copy produced before the final assemble had SHA-256 `93023d845a2faf54ba68eb2ab953bc8205e08b3a6120393ecc420e06715c51ba`; it was moved to the reversible archive before replacing the active copy. The final active copy is the exact final-build byte set recorded above.

## Local shared-drop evidence

Destination: `/mnt/cyberforgex-ai/App Dev/KinPlay/apk-drops`

- Active KidPlay artifact: `20260912_KidPlay_v0.7.4.apk`.
- Active KidPlay size and SHA-256 match the final build exactly.
- Active sidecar: `20260912_KidPlay_v0.7.4.apk.sha256`.
- Local validator: passed with no errors.
- Active APK inventory: exactly two registered APKs, `20260912_KidPlay_v0.7.4.apk` and `20260823_DevLab_v0.2.3.apk`.
- Superseded KidPlay copies are retained under `_archived-apk-drops`; no active temporary or status-labelled APK remains.

## Runtime boundary

Connected instrumentation was attempted with `:app:connectedDebugAndroidTest`. The configured AVD was not package-manager-ready: Gradle started `0` tests and failed installation with `cmd: Can't find service: package`. The audit health bundle is `build/android-audit/kp-pro-037-current/result.json`; it records `status: not-boot-complete`, no `/dev/kvm`, and `hogwarts-android-emulator.service` inactive, dead, and disabled. No physical-device validation was performed; those checks remain reserved for LJ.

## External holds

- Independent reviewer: two read-only reviewer attempts timed out after 420 seconds without a JSON verdict. No independent approval is claimed; this remains a review hold.
- GitHub: `origin/main` remains `45a5cadc6b69cc29bc5f1326ac760b75cf3e89e1`; the local integration commit is not pushed because owner-authenticated GitHub access is blocked by `t_b323a45f`.
- Google Drive: publication was not attempted because the active profile's Google Workspace check returned `NOT_AUTHENTICATED` for its profile-local token. No remote object was changed, and no Drive checksum or one-current inventory claim is made.

This record intentionally separates the verified local build/drop from the unavailable independent review, GitHub push, Drive publication, connected runtime, and physical-device gates.
