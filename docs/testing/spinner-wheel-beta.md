# Spinner wheel beta candidate

Date: 2026-08-07

## Scope

KinPlay now has an offline-first circular spinner wheel for `race_like_an_animal`.
The wheel is a reusable Compose component in `:wheel-core`, so its deterministic
selection math is separate from rendering and can be reused by other activities.
The production surface provides:

- six reviewed animal choices with a short, family-safe movement instruction;
- equal colored sectors, a fixed top pointer, a central `GO` control, and easing-based multi-turn animation;
- tap-the-wheel and `Spin` interactions;
- an optional `Next` control for deterministic sequential testing;
- selected-result and spinning-state accessibility semantics;
- disabled behavior while KinPlay's child lock is active;
- state restoration through Compose saveable state.

The production wheel intentionally owns presentation state only. The current timed-session domain has no animal-selection field, so the result is not silently claimed as persisted session data.

The separate `:wheel-lab` application is a device-test sandbox. It contains three
switchable pages:

1. Animal moves — production-shaped data.
2. Color choices — eight-sector spacing and contrast check.
3. Long labels — wrapping and accessibility check.

Each page uses the same `:wheel-core` component and exposes wheel, Spin, and Next
test tags for physical-device testing.

Reference used: [YouTube Shorts spinner reference](https://www.youtube.com/shorts/GJAPULXg2GE?feature=shared).

## Build identity

- KidPlay: `0.7.0-beta2` / version code `12` / package `com.kinplay.app`; this follow-up also changes the visible application label and default launcher artwork to Fox Heart.
- Wheel Lab: `0.1.0` / version code `1` / package `com.kinplay.wheellab`.
- Minimum SDK: 26; target SDK: 35.
- No network, camera, microphone, contacts, location, storage, billing, or telemetry permission was added.

## Verification

All of the following completed successfully on 2026-08-07:

- `source scripts/android-env.sh && ./gradlew test lintDebug :app:assembleRelease :wheel-lab:assembleRelease`
- `source scripts/android-env.sh && ./gradlew :app:assembleDebugAndroidTest :wheel-lab:assembleDebugAndroidTest`
- Release-mode deterministic validator: `/home/phantomatic/.hermes/state/kinplay_offline_validation.json`
- `git diff --check`
- JSON schema/parity validation: canonical and runtime seed files remained byte-identical.
- APK badging, permission, and signature checks.

The debug JVM suite contains 216 KinPlay tests, 8 wheel-core tests, and 2 Wheel Lab
tests: 226 tests, all passed. Release JVM tests also passed for all three modules.
The instrumented-test sources compiled successfully, but no instrumented test was
executed because `adb devices` reported no connected device or emulator.

The deterministic validator reported `status: passed` for all of its steps,
including unit tests, Android test-source compilation, debug assembly, lint,
packaged-content validation, badging, permissions, and APK signature.

## Device-test artifacts

Installable, signed debug artifacts are staged outside the repository at
`/mnt/cyberforgex-torrents/KinPlay/apk-drops/working-builds/`:

| Artifact | Bytes | SHA-256 |
| --- | ---: | --- |
| `20260807_KinPlay_v0.7.0-beta1_SpinnerWheel_debug.apk` | 22,724,157 | `7628f77211cf690bdd9e5b5a13b005d360cadae45cbdf4e2a21204c7eabe70cc` |
| `20260807_KinPlay_WheelLab_v0.1.0_debug.apk` | 10,155,954 | `b2a7cb48ebcb7cd2418498598ad24292fb1af4e865dc55c186d770c25e82ee06` |

Matching `.sha256` sidecar files are next to both APKs. The release APK outputs
are unsigned (`app-release-unsigned.apk` and `wheel-lab-release-unsigned.apk`);
they are not presented as installable release artifacts.

## Latest branding follow-up

The current KidPlay branding candidate is staged separately from the original
spinner-wheel beta artifact:

| Artifact | Bytes | SHA-256 |
| --- | ---: | --- |
| `20260808_KidPlay_v0.7.0-beta2_FoxHeart_debug.apk` | 23,103,966 | `1b1e74759cb518e04f9099ad4ca151c69ac4e36a7be5d3abaf57e0d77c53976e` |

The active default launcher alias uses the exact Google Gemini Nano Banana
`08_fox_heart.jpg` master. The packaged JPEG is byte-identical to the source
master with SHA-256 `a8dd209cd588e0f1de4c9d58668b851ab477434ad5a5f93a5217dcb109bdbd5b`.
This is a private debug-signed beta artifact, not a production-store release.

## Physical-device checklist

On a Pixel 8 Pro / Android 16 or an emulator, test:

- `race_like_an_animal` from the normal activity detail screen;
- the same activity from the timed-session path;
- tap the wheel, press Spin, and press Next;
- rapid repeated input while the wheel is moving;
- child-lock disabled state;
- long-label and eight-sector pages in Wheel Lab;
- rotation, large font, TalkBack/content descriptions, and offline launch.

Do not mark the physical-device gate complete until those checks are observed on
the target device.
