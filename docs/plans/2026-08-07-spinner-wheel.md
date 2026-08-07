# KinPlay Spinner Wheel Implementation Plan

> **For Hermes:** Use the repository's Android Compose workflow and test-first implementation discipline.

**Goal:** Replace the existing horizontal animal selector with an accessible circular spinner wheel inspired by the referenced YouTube Short, and provide a separate offline Wheel Lab app for testing wheel layouts and interaction patterns on a device.

**Architecture:** Add a reusable `:wheel-core` Android library containing pure wheel-choice/rotation logic and a Compose circular Canvas wheel. The production `:app` will consume that library from `RaceAnimalWheel.kt`; a separate `:wheel-lab` Android application will showcase the same component with animal, color, and long-label datasets. The feature remains offline-first and uses no network, camera, microphone, or new dangerous permissions.

**Reference:** `https://www.youtube.com/shorts/GJAPULXg2GE?feature=shared` — visible reference cues are a circular wheel, equal colored wedges, a fixed pointer, an animated spin, and a central hub.

**Tech Stack:** Kotlin 2.1.21, Android Gradle Plugin 8.10.1, Jetpack Compose Material 3, Compose Canvas, Kotlin/JUnit, offline Gradle dependencies already used by KinPlay.

---

### Task 1: Add the reusable wheel module and failing logic tests

**Objective:** Establish the shared module and specify deterministic target-selection and pointer-alignment behavior before writing production logic.

**Files:**
- Modify: `settings.gradle.kts`
- Modify: `build.gradle.kts`
- Create: `wheel-core/build.gradle.kts`
- Create: `wheel-core/src/test/java/com/kinplay/wheel/SpinnerWheelLogicTest.kt`

**Tests to specify first:**
- A spin with two or more choices avoids the current choice.
- A one-choice wheel always returns index zero.
- Invalid empty-choice/count inputs fail clearly.
- A computed target rotation advances by the requested minimum turns and places the requested sector at the fixed top pointer.
- Pointer lookup returns the expected sector after a target rotation.

**Verification:**
```bash
./gradlew :wheel-core:test
```
Expected initially: compilation/test failure because the shared logic types do not yet exist; after Task 2: all new tests pass.

---

### Task 2: Implement pure wheel logic and the Compose circular wheel

**Objective:** Create the reusable data model, selection helpers, circular wedge renderer, fixed pointer, spin animation, accessibility semantics, and optional Next control.

**Files:**
- Create: `wheel-core/src/main/java/com/kinplay/wheel/SpinnerWheelLogic.kt`
- Create: `wheel-core/src/main/java/com/kinplay/wheel/SpinnerWheel.kt`

**Behavior:**
- Equal wedges with a stable palette and readable labels.
- Wheel rotates while the pointer remains fixed at the top.
- Spin chooses a different option when possible, animates several full rotations, then reports the landed choice.
- The wheel itself is tappable and there is a clearly labeled Spin button.
- Controls are disabled while spinning or when `enabled=false`.
- `rememberSaveable` preserves the selected choice and final rotation across recreation.
- Semantics expose the selected choice and spinning/stopped state.
- Optional Next advances one sector and preserves the existing production test-tag contracts.

**Verification:**
```bash
./gradlew :wheel-core:test
./gradlew :wheel-core:assembleDebug
```

---

### Task 3: Integrate the wheel into the production Race Like an Animal activity

**Objective:** Replace the horizontal LazyRow implementation without changing the reviewed content or activity route.

**Files:**
- Modify: `app/build.gradle.kts`
- Modify: `app/src/main/java/com/kinplay/app/RaceAnimalWheel.kt`
- Create/modify: `app/src/test/java/com/kinplay/app/RaceAnimalWheelTest.kt`

**Behavior:**
- Keep the reviewed six choices: Kangaroo, Cheetah, Rabbit, Frog, Turtle, Penguin.
- Keep `race-animal-wheel-card`, `race-animal-wheel`, `race-animal-selected`, `race-animal-spin-button`, and `race-animal-next-button` tags where practical.
- Show the selected animal and a concise, ready-to-use movement instruction.
- Respect the existing child-lock `enabled` value.
- Do not alter the reviewed safety instructions in the seed content.

**Verification:**
```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```

---

### Task 4: Add the offline Wheel Lab companion application

**Objective:** Give device testing a separate app that can exercise multiple wheel datasets and presentation patterns without touching production navigation/content.

**Files:**
- Modify: `settings.gradle.kts`
- Modify: `build.gradle.kts`
- Create: `wheel-lab/build.gradle.kts`
- Create: `wheel-lab/src/main/AndroidManifest.xml`
- Create: `wheel-lab/src/main/res/values/strings.xml`
- Create: `wheel-lab/src/main/res/values/themes.xml`
- Create: `wheel-lab/src/main/java/com/kinplay/wheellab/WheelLabActivity.kt`
- Create: `wheel-lab/src/test/java/com/kinplay/wheellab/WheelLabContractTest.kt`

**Demos:**
- Animal movement wheel matching KinPlay.
- Color wheel with eight choices.
- Long-label wheel for text-fitting/accessibility review.
- A compact page summary showing the current selection and interaction state.

**Verification:**
```bash
./gradlew :wheel-lab:testDebugUnitTest
./gradlew :wheel-lab:assembleDebug
```

---

### Task 5: Add Compose UI coverage and update version metadata

**Objective:** Verify the production and lab surfaces expose the intended controls and bump KinPlay to the next beta version.

**Files:**
- Create/modify: `app/src/androidTest/java/com/kinplay/app/RaceAnimalWheelScreenTest.kt`
- Create/modify: `wheel-lab/src/androidTest/java/com/kinplay/wheellab/WheelLabScreenTest.kt`
- Modify: `app/build.gradle.kts` (`0.7.0-beta1`, version code `11`)
- Modify: release/content notes only if validator requires them.

**Coverage:**
- Production wheel is displayed with selected-animal semantics.
- Spin and Next controls are available when enabled.
- Locked/disabled state prevents interaction.
- Lab demos expose each dataset and their controls.

**Verification:**
```bash
./gradlew :app:testDebugUnitTest :wheel-core:test :wheel-lab:testDebugUnitTest
./gradlew :app:lintDebug :wheel-lab:lintDebug
```
Connected-device tests remain a separate physical-device gate; no device result will be fabricated.

---

### Task 6: Build, inspect, and deliver verified artifacts

**Objective:** Run the full offline validation/build path and produce verifiable APK artifacts without claiming physical-device validation.

**Verification:**
```bash
./gradlew testDebugUnitTest lintDebug assembleDebug
./gradlew :wheel-core:test :wheel-lab:testDebugUnitTest :wheel-lab:assembleDebug
./gradlew :app:assembleRelease
sha256sum app/build/outputs/apk/release/*.apk wheel-lab/build/outputs/apk/debug/*.apk
```

Inspect package identity, version, permissions, and resource presence with `aapt dump badging`/`aapt dump permissions`; confirm Git diff, whitespace, tests, and exact output paths. Place user-facing APKs under `/mnt/cyberforgex-torrents` with clear versioned names after successful verification.
