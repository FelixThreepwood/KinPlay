# Implementation ledger — KP-BATCH-F20A381B-0120-408C-A207-543DB1F1CD89

Implementation authorization: direct project-owner request on 2026-08-24.

Privacy boundary: this ledger contains canonical item IDs and product acceptance criteria only. Raw sender details and child-identifying information are not copied.

Release target: `com.kinplay.app` **0.7.3 (16)**.

| Item | Acceptance criterion | Affected paths / evidence |
|---|---|---|
| KPF-0032 | Remove repetitive normal-surface safety labels and unnecessary safety wording while retaining point-of-use, platform, privacy, and foreseeable-harm warnings. | `MainActivity.kt`; canonical/runtime seed; safety matrix; safety regression tests |
| KPF-0093 | No production game-detail or active-play surface exposes the three-second handoff-lock control. Legacy lock code remains isolated and unused for rollback safety. | `MainActivity.kt`; `WouldYouRatherScreen.kt`; release/safety copy; source contract and UI tests |
| KPF-0094 | Feedback control remains reachable on every production route, including Would You Rather, timed play, details, and settings-adjacent screens, while retaining route/content context. | `MainActivity.kt`; feedback capture tests; connected Compose coverage |
| KPF-0095 | About the app shows stable, newest-first version entries with version and release date. | `ReleaseChangelog.kt`; About UI; changelog tests |
| KPF-0096 | Every release item uses a confirmed 5–10 word maximum summary. | `ReleaseChangelog.kt`; changelog tests |
| KPF-0097 | All games and activities heading uses a legible serif small-caps treatment with stable semantics and font-scale-safe layout. | `MainActivity.kt`; heading source/UI contract |
| KPF-0098 | Level 1 retains title, ordering, and cards but removes explanatory taxonomy paragraphs and group descriptions. | `MainActivity.kt`; navigation/content tests |
| KPF-0099 | Entire Level 1 card opens the details destination; favorite control remains independent. | `MainActivity.kt`; card semantics/source and Compose tests |
| KPF-0100 | Every Would You Rather prompt displays scenario 1 plus `OR` on line one and scenario 2 on line two. | `WouldYouRatherModels.kt`; `WouldYouRatherScreen.kt`; formatter tests/UI tests |
| KPF-0101 | Detail surfaces use concise Players/Setup/Steps/Prompt/Clues/Variations headings and unnumbered instruction blocks. | `MainActivity.kt`; detail-section tests/UI tests |
| KPF-0102 | I Spy is the reviewed exemplar with neutral wording, concise player/step/clue/variation sections, and no unnecessary “safe” copy. | Both KinPlay seed copies; content invariants; detail UI tests |
| KPF-0103 | Shape Detective is retired from active discovery, search, random selection, favorites/recent restoration, and direct playable resolution. | Both KinPlay seed copies; content discovery tests |
| KPF-0104 | Backyard Micro Safari uses parent-selected observation targets, a three-minute find-and-explain flow, and reviewed no-touch guidance. | Both KinPlay seed copies; session/content tests |
| KPF-0105 | Backyard Micro Safari exposes an accessible vertical duration picker, defaults to 3 minutes, preserves per-game override precedence, and works at narrow/large-text layouts. | `AppSettings.kt`; `SessionLaunch.kt`; `MainActivity.kt`; session unit/Compose tests |

### Explicit implementation decisions

- KPF-0096 uses **5–10 words** per item, matching the existing release-quality convention.
- KPF-0099 opens the details page, not immediate play, because that preserves the existing Level 1 navigation contract and keeps the favorite control independent.
- KPF-0104 uses observation-only, parent-approved targets rather than inventing unsafe object examples.
- KPF-0105 adds a 3-minute duration option and uses a vertical, scrollable single-choice control for Backyard Micro Safari; per-game overrides remain one-shot and take precedence over the activity default.
- KPF-0032 removes generic safety-tag presentation from ordinary detail surfaces but does not delete protected warnings, parent notes, or the Safety and privacy surface.

### Verification evidence — 2026-08-24

- Focused command: `./gradlew :app:testDebugUnitTest --tests com.kinplay.app.BatchF20AcceptanceTest --no-daemon` — passed after the final runtime-exclusion regression was added.
- Full JVM command: `./gradlew :app:testDebugUnitTest --no-daemon` — **229 tests passed, 0 failures, 0 errors, 0 skipped**.
- Release-boundary command: `./gradlew :app:lintDebug :app:compileDebugAndroidTestKotlin :app:assembleDebug --rerun-tasks --no-daemon` — passed.
- Lint result: 30 warnings and 4 hints; no errors. Changed-path findings are existing `UseKtx`/`AutoboxingStateCreation` guidance only.
- Content validator: both seeds passed schema validation, canonical/runtime byte parity, unique-ID validation, and APK asset parity.
- APK: `app/build/outputs/apk/debug/app-debug.apk`; package `com.kinplay.app`; version `0.7.3`; version code `16`; size `25,535,866` bytes; SHA-256 `a26f5c549283eb24bd307d38fa419e0803b0f8878eb84d2fb703fedc307af664`.
- Signature: APK Signature Scheme v2 verified.
- Device boundary: Android-test sources compiled; no connected Android target was available for connected instrumentation or physical retest.

### Release evidence pending

- Explicit local staging, final staged-diff review, source commit/push, Google Drive upload, Drive metadata read-back, downloaded-byte SHA-256 comparison, and superseded-artifact archival remain open.
