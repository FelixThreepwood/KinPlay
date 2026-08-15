# Dev Lab 0.2.1 publication record

Published: 2026-08-15T18:00:30Z

## Source

- Repository: `https://github.com/FelixThreepwood/KinPlay`
- Source commit: `67ef372aca3edad75aa56a6a78b8ec5cb1f3e7d0`
- Remote `main`: verified to match the source commit
- Package: `com.devlab`
- Version: `0.2.1` (version code `2`)

## Artifact

- APK bytes: `10,261,603`
- SHA-256: `fa6891becda17f8189e7638859e9f12568ae7c089a61e88ca62020113dddc77e`
- Local canonical shared-drive path: `/mnt/cyberforgex-torrents/KinPlay/apk-drops/20260815_DevLab_v0.2.1.apk`
- Local compatibility path: `/mnt/cyberforgex-torrents/DevLab/DevLab_v0.2.1.apk`
- SHA-256 sidecars are present at both local paths.

## Google Drive

- Folder: `apk-drops` (`12bINCtZHQwvh3-mIbQ2x-swPE6ACzjYp`)
- Object: `1AQ-kWztxSiR7swEIuS8ozHkD7OWZRysw`
- Link: https://drive.google.com/file/d/1AQ-kWztxSiR7swEIuS8ozHkD7OWZRysw/view?usp=drivesdk
- Remote size: `10,261,603` bytes
- Remote read-back SHA-256: `fa6891becda17f8189e7638859e9f12568ae7c089a61e88ca62020113dddc77e`
- Source and downloaded Drive bytes compared exactly with `cmp`.
- The superseded Dev Lab object was moved to reversible Drive trash only after this verification.

## Included feedback functionality

The Dev Lab APK includes the standalone feedback flow under `com.devlab.feedback`, including:

- floating feedback access on each Dev Lab demo;
- active screen/content context capture;
- type, impact, comment, expected-result, and technical-context fields;
- offline-first local persistence;
- attachment policy and review;
- edit/archive/address/complete lifecycle handling;
- email handoff and clipboard fallback;
- feedback batch identifiers compatible with the existing intake process.

APK DEX inspection confirmed feedback markers including `Dev Lab feedback`, `devlab_feedback`, `Quick comment`, and `feedback-control`.

## Verification gates

Passed:

- `:wheel-lab:testDebugUnitTest` — 38 tests, 0 failures, 0 errors, 0 skipped
- `:wheel-lab:compileDebugAndroidTestKotlin`
- `:wheel-lab:lintDebug`
- `:wheel-lab:assembleDebug`
- `git diff --cached --check` before commit
- APK badging, package identity, version, permissions, and v2 signature inspection
- source/local/Drive byte and SHA-256 comparison
- final local inventory: exactly one current KidPlay APK and one current Dev Lab APK

## Interactive testing boundary

Android instrumentation sources compiled successfully, but no connected physical device or emulator was available on Hogwarts at publication time. `adb devices -l` returned no targets; the Android emulator executable and system images are not installed. Physical touch behavior, accessibility-service behavior, email/file-picker handoff, orientation/lifecycle behavior, animation fluidity, and launcher behavior remain open for interactive retest.

A persistent Android Virtual Device on Hogwarts would materially improve the development loop for repeatable Compose semantics, touch-target, state-restoration, rotation, animation, feedback persistence, and intent-handoff checks. It would complement—not replace—the Pixel 8 Pro/family-device retest for hardware, launcher, accessibility-service, and real-world usability evidence.
