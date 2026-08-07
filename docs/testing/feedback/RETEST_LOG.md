# KinPlay Feedback Retest Log

Last updated: 2026-08-06T22:40:54Z

Current revision under retest: **0.6.0-beta3 (8)** for the implemented items in batch `KP-BATCH-26606A7F-956F-4699-AE68-328FFA369FBC`; prior cohorts retain their historical retest builds. Earlier cohorts retain their recorded retest builds unless a row below names 0.6.0-beta1 (6); KPF-0020 remains on 0.5.0-beta1 (5) because it is excluded from this batch.

Automated checks pass, but every item below remains **fixed-awaiting-retest**. None is verified or closed until an anonymous family tester completes the relevant check on a physical Android device.

The Android instrumentation test sources compiled successfully, but they were **not device-executed** because `adb devices -l` reported no attached device or emulator. Physical family-device behavior, visual judgment, accessibility-service behavior, and launcher integration therefore remain pending.

| Item | State | Retest build | Family/device retest check |
|---|---|---|---|
| KPF-0001 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Open Quiet Games and confirm I Spy, Charades, Would You Rather, Animal Guessing, and Alphabet Story are easy to recognize and offer ready-to-use nested choices. |
| KPF-0002 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Confirm Quiet Games shows one Mad Libs entry and that it opens the story submenu without exposing each story as a top-level category item. |
| KPF-0003 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Open Would You Rather, find the `Gross & Silly` section, and read at least four pairs aloud; confirm they feel mild and child-appropriate. |
| KPF-0004 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Find Race Like an Animal in Get the Energy Out and Outdoor; confirm the kangaroo, cheetah, rabbit, frog, safe-area, and walking/low-impact directions work in practice. |
| KPF-0005 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Run Pillow Marco Polo: Eyes-Open Islands with adult supervision. Verify eyes stay open, everyone walks, the caller is stationary, pillows remain boundary/island markers, hazards are cleared, and play stops before it becomes chaotic. Confirm nobody throws, face-covers with, piles, or jumps on pillows. |
| KPF-0006 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Home now has a concise one-line descriptor, no instructional gap, and compact responsive category controls; perform physical visual retest at phone, wide, and large-text sizes. |
| KPF-0007 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Confirm each Home category card shows useful place cues rather than an inventory count, including waiting-room and backyard/living-room examples where appropriate. |
| KPF-0008 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm every active card exposes participant suitability before opening and that the compact descriptor remains readable in the right/trailing hierarchy. |
| KPF-0009 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm games-and-activities terminology and the exact `All games and activities` destination label on a physical device. |
| KPF-0010 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Give the app to an unfamiliar or tired parent without coaching. From fresh launch, confirm materials/setup burden is visible and they can start a complete ready-to-use choice within 30 seconds without inventing content. |
| KPF-0011 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Open Would You Rather from At the Dinner Table; confirm the dedicated full-screen lane shows exactly four categories, displays a prompt within two seconds, keeps it stable without a tap, fades on tap to a different prompt, and exits to the originating category. |
| KPF-0012 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Sample all four 80-prompt categories with the family; confirm copy is readable, distinct, category-appropriate, and child-appropriate, especially Gross and Super Gross, and confirm normal use does not produce obvious repeats. |
| KPF-0013 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Create several notes, hand off one, and confirm unsent notes remain newest-first in the active list while handed-off/addressed/completed notes appear only in the accessible archive and cannot be selected for resend. |
| KPF-0014 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Confirm active and archived cards show local creation date/time; mark a handed-off note addressed and confirm the address date and exact app version appear, then confirm completion metadata remains understandable. |
| KPF-0015 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Confirm the unsent and created-this-revision counts change independently. Open an email handoff, return without sending, and choose “No, keep unsent”; then send and use the explicit “Yes, mark sent” confirmation. Verify only the confirmed notes archive and cannot be resent. |
| KPF-0016 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Review backgrounds, cards, controls, and text in Forest, Ocean, and Berry on supported phone/tablet displays, including large text and an accessibility contrast review; confirm layers remain visually distinct and readable. |
| KPF-0017 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Change game timer, activity duration, and theme; verify immediate behavior and the Current plan summary, then force-stop/relaunch and confirm all choices persist and remain understandable. |
| KPF-0018 | fixed-awaiting-retest | 0.6.0-beta1 (6) | B10/B11 implement reviewed per-game eligibility, revised lock/key states, clear locked content, tap-triggered temporary unlock guidance, and in-app Back/control guarding. Physical touch, accessibility, and recovery retest remains required. |
| KPF-0019 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Candidate 1A Teal and Candidate 1C Sunshine were independently visually approved through the required Google Gemini Nano Banana path, integrated byte-for-byte as the two launcher masters, and passed exact hash/resource checks. Confirm K/P legibility, adaptive safe-zone margins, launcher rendering, and launcher-cache refresh behavior on a physical device. |
| KPF-0020 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Switch repeatedly between Teal and Sunshine and relaunch the app after each selection. Confirm one launchable icon remains and the preference persists. Record launcher refresh variability: the icon may update immediately, after returning Home, or only after launcher cache refresh; delayed refresh alone is not an app failure. |

## Implemented behavior awaiting family/device confirmation

- `KPF-0011`–`KPF-0012`: dedicated full-screen Would You Rather play, four reviewed 80-prompt categories, fade/tap/exit behavior, and persisted shuffled-bag progression.
- `KPF-0013`–`KPF-0015`: newest-first unsent/archive lifecycle, creation and resolution metadata, separate counts, resend prevention, and explicit tester confirmation after the external email handoff.
- `KPF-0016`–`KPF-0018`: differentiated accessible theme layers, persisted timers/durations/themes, and the deliberate child-handoff lock, including its keyboard/accessibility activation and recovery path.
- `KPF-0019`–`KPF-0020`: documented Gemini icon masters, Android adaptive-icon derivatives, and safe finite launcher-alias switching with launcher-controlled refresh timing.

## Retest evidence to record

Use anonymous tester IDs only (for example, `T01`). Record device type/model, launcher name where icon behavior is tested, Android version, accessibility service where the lock path is tested, item ID, elapsed time where relevant, pass/fail, and a sanitized note. Do not record family names, contact details, child names, photos, audio, video, birthdates, or precise locations.

When a retest fails, leave the item awaiting retest and add a new sanitized observation. When it passes, update status only through the project’s feedback-review process; this log does not pre-mark any item verified or closed.


## New intake awaiting revision — KP-BATCH-A9A144CD-D6AE-47E6-8C6F-4F14214377E4

- Affected build: **0.5.0-beta1 (5)**. B5–B14 implementation is now recorded against **0.6.0-beta1 (6)**; remaining intake rows are triage records until their batches complete.

- Reopened after tester feedback: `KPF-0006`, `KPF-0008`, `KPF-0009`, `KPF-0018`, `KPF-0019`.
- Newly accepted or queued: `KPF-0021`–`KPF-0037`.
- Blocker requiring reproduction and crash evidence before the next beta: `KPF-0037` (`Send now` crash regression).
- Safety-sensitive product decision: `KPF-0032` must receive item-by-item product, safety, and legal review; it is not authorization for unconditional removal of warnings.
- The manual subject-format mismatch did not prevent intake because the payload identifiers and note IDs were valid and unique.

| New item | Triage state | Retest build | Next validation |
|---|---|---|---|
| KPF-0021 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm responsive compact two-column card layout on phone, wide, and large-font displays. |
| KPF-0022 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm every collapsed card shows a concise description while retaining material/setup preview and actions. |
| KPF-0023 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm I Spy uses neutral `Clues and suggestions` wording and no parent-state characterization. |
| KPF-0024 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm the eligible lock control shows 🔒 while unlocked and 🔑 while locked, with an understandable screen-reader label and three-second progress. |
| KPF-0025 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Lock a play surface, confirm content remains clear and controls are blocked, then tap the surface and confirm temporary `Hold key for 3 seconds to unlock` guidance. |
| KPF-0026 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm only Charades and Would You Rather show the handoff-lock control; inspect representative noneligible detail screens and confirm the control is absent. The 55-item eligibility matrix and route/helper tests passed. |
| KPF-0027 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm the compact 📝 control opens the existing feedback flow, remains easy to tap, and announces its unsent-note count. |
| KPF-0028 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm Would You Rather enters landscape, restores prior orientation on exit, and remains usable with accessibility and rotation behavior. |
| KPF-0029 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Start an eligible game, confirm the applied duration and rounds, then verify the timed-session surface shows round progress and a live countdown, advances on timer expiry or Finish round, reaches Session complete after the configured rounds, exits back to details, and remains guarded by the child-handoff lock where eligible. |
| KPF-0030 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Change the global duration and default rounds, then choose different duration/round values on an eligible details page. Confirm the applied values appear before Start, the global Settings values remain unchanged, and the one-shot override is consumed on Start. |
| KPF-0031 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm requirements and tester reports can distinguish the collapsed card, expanded card, and details page and that each name matches the visible state/navigation behavior. The normative contract and automated state anchors passed. |
| KPF-0032 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm normal detail surfaces omit repetitive safety labels while protected activity warnings, privacy boundaries, and Safety and privacy content remain available. The fail-safe matrix and automated warning tests passed; no connected Android target was available. |
| KPF-0033 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm the removed Home/list/detail repetition is absent while action, accessibility, privacy, safety, and instructional copy remains understandable. |
| KPF-0034 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm Home shortcuts use compact graphical cues and readable labels without shortcut subtext, including the Settings gear cue; native symbols were used for this beta. |
| KPF-0035 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Confirm Home and destination labels read exactly `Random game` and `All games and activities`, including accessibility announcements. |
| KPF-0036 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Open the upper-right three-line menu and verify Settings, Account, About the app, and Safety and privacy destinations; confirm the staged Account message and intentional duplicate Settings entry. |
| KPF-0037 | fixed-awaiting-retest | 0.6.0-beta1 (6) | Create and select an unsent note, tap Send now, and confirm the email app opens without KinPlay stopping. Repeat with no compatible email handler if practical; confirm the note remains unsent and Copy selected remains available. Source/data-flow reproduction and Robolectric production-path tests passed; no physical Android target was connected. |


## New intake awaiting triage — KP-BATCH-432C6744-035C-4F62-94C1-A1FE4B609C5B

- Intake build: **0.6.0-beta1 (6)**. Post-intake implementation and release build: **0.6.0-beta2 (7)**.
- Source notes: 13 new unique note IDs; no replayed IDs.
- Existing canonical items touched: `KPF-0002`, `KPF-0018`, `KPF-0022`, `KPF-0029`, `KPF-0030`, `KPF-0034`, `KPF-0036`.
- New canonical items: `KPF-0038`–`KPF-0052`.
- Privacy: Project records contain no child names, contact details, images, birthdates, or precise locations.
- Code authorization: No application code change was authorized or made.

| New item | Triage state | Next validation |
|---|---|---|
| KPF-0038 | new | Confirm duplicate Home shortcuts are removed while overflow destinations remain reachable. |
| KPF-0039 | new | Review standard menu icons, labels, contrast, and screen-reader semantics. |
| KPF-0040 | new | Resolve the Level 0/1/2 product hierarchy and verify Mad Lib and prompt nesting. |
| KPF-0041 | new | Approve the Level 1 taxonomy and confirm the flat-list overload is removed. |
| KPF-0042 | new | Confirm collapsed cards show only the approved minimal content and bold emphasis. |
| KPF-0043 | new | Define attachment types, privacy review, size limits, and handoff behavior. |
| KPF-0044 | new | Approve five or six music tracks, suitability review, rights, and selection behavior. |
| KPF-0045 | new | Verify parent play/pause and automatic playback behavior on music-based sessions. |
| KPF-0046 | new | Approve the 120-card target, categories, originality/licensing, and nonrepetition rules. |
| KPF-0047 | new | Verify centered three-second lock countdown with touch and accessibility behavior. |
| KPF-0048 | new | Review scope and evidence for brain-health wording before content or diagrams are made. |
| KPF-0049 | new | Check the title, instructions, navigation, labels, and five-word mechanics for consistency. |
| KPF-0050 | new | Approve the scrollable incremental control and verify narrow-width/large-text behavior. |
| KPF-0051 | new | Confirm 20-minute and 15-round bounds across defaults, overrides, and stored values. |
| KPF-0052 | new | Define which play-critical instructions remain visible during active sessions and verify safety. |


## Beta2 automated verification — KP-BATCH-432C6744-035C-4F62-94C1-A1FE4B609C5B

- Build: **0.6.0-beta2 (7)**
- Automated result: **passed** — full JVM unit suite, Android test-source compilation, lint, debug assembly, content/schema/parity validation, packaged-resource validation, APK metadata, and APK signature checks.
- Physical-device result: **open** — no attached Android device or emulator; do not mark items verified or closed until family/device retest.

| Item | State | Retest build | Family/device retest check |
|---|---|---|---|
| KPF-0002 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0018 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0022 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0029 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0030 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0034 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0036 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0038 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0039 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0040 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0041 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0042 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0043 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0044 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0045 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0046 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0047 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0048 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0049 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0050 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0051 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |
| KPF-0052 | fixed-awaiting-retest | 0.6.0-beta2 (7) | Automated gates passed; physical Pixel 8 Pro / Android 16 retest remains open. |

Paper Airplanes note `KP-NOTE-27882454-9C8A-40DA-8479-F4A7851CC885` is implemented with two Gemini-generated instructional diagrams and remains a staged source note for the next email feedback batch.


## Beta2 publication verification — KP-BATCH-432C6744-035C-4F62-94C1-A1FE4B609C5B

- Published: **2026-08-06T03:22:38Z**
- Source commit: `204eabacb1b57e6901456b4b2aac7c4ed16e018c`
- APK: `20260806_KinPlay_v0.6.0-beta2_MVP.apk`
- SHA-256: `6db0b08efc5ade3b7183c6ec0de4028287574b7f230374edbf9a5f3873c5a2bb`
- Drive object: `1tm7P1Wlo4SiqHesMubR67ciRV29a5efP`
- Remote read-back: passed exact size and SHA-256 comparison; final folder inventory contains one active APK.
- Device status remains open: no Pixel 8 Pro or emulator was connected for physical interaction, visual, accessibility, or audio retest.

## New intake awaiting triage — KP-BATCH-26606A7F-956F-4699-AE68-328FFA369FBC

- Affected build: **0.6.0-beta2 (7)**. This batch is intake-only and has no retest build.
- Source notes: 11 new unique note IDs; no replayed IDs.
- Existing canonical items touched: `KPF-0006`, `KPF-0016`, `KPF-0017`, `KPF-0021`, `KPF-0022`.
- New canonical items: `KPF-0053`–`KPF-0063`.
- Privacy: Project records contain no child names, contact details, images, birthdates, or precise locations.
- Code authorization: No application code change was authorized or made.

| New item | Triage state | Next validation |
|---|---|---|
| KPF-0053 | new | Resolve app-bar/system Back placement and verify consistent right-aligned navigation with accessibility. |
| KPF-0054 | new | Verify empty-material activities omit the label, section, placeholder, and reserved space. |
| KPF-0055 | new | Review instruction pages for metadata removal while retaining necessary safety and play-critical content. |
| KPF-0056 | new | Verify every card level shows only name and one sentence until expansion/opening. |
| KPF-0057 | new | Verify every age label uses only the reviewed minimum age and no maximum. |
| KPF-0058 | new | Verify Home reading/focus order places Random game and All games and activities above recents. |
| KPF-0059 | new | Verify horizontal Settings duration options plus narrow-width and large-text fallbacks. |
| KPF-0060 | new | Approve two bright themes and verify contrast, persistence, and application behavior. |
| KPF-0061 | new | Check the approved Home descriptor across visible copy, labels, and review exports. |
| KPF-0062 | new | Define search scope/indexing and verify results preserve hierarchy and back-stack context. |
| KPF-0063 | new | Define persistence and verify favorite toggle, unfavorite, list, accessibility, and migration behavior. |

## 2026-08-06 — KP-BATCH-26606A7F beta3 implementation

- Batch: `KP-BATCH-26606A7F-956F-4699-AE68-328FFA369FBC`
- Implementation build: `0.6.0-beta3` (version code `8`)
- Scope: 16 canonical items, including existing items KPF-0006, KPF-0016, KPF-0017, KPF-0021, and KPF-0022 plus KPF-0053 through KPF-0063.
- Direct authorization: The active user request authorized implementation; the intake payload remains preserved as sanitized product-test data.
- Implemented behavior: consistent right-aligned top Back actions, empty-material suppression, instruction metadata removal, title/description-only collapsed cards, minimum-age labels, Home browse ordering and copy, compact Settings rows, Sunshine/Tropical themes, hierarchical local search, and persistent Favorites.
- Canonical/runtime content: no JSON content mutation was required; byte parity remains a release gate. Review exports were updated to minimum-age-only wording.
- Automated status at record update: unit suite passed; release validator and APK verification remain required before final release closure.
- Physical retest: Pixel 8 Pro on Android 16 / SDK 36 remains open until a device or emulator is available.

## 2026-08-06 — KP-BATCH-26606A7F beta3 automated release verification

- Build: `0.6.0-beta3` (8)
- Artifact: `/mnt/cyberforgex-torrents/KinPlay/apk-drops/20260806_KinPlay_v0.6.0-beta3_MVP.apk`
- Size: `21613823` bytes
- SHA-256: `6c99a77a315773304af353272577fd5b560681d942e3e7f89b61b3f6f3b36f22`
- Independent validator: passed; log `/home/phantomatic/.hermes/logs/kinplay-validation/20260806-011805-release.log`
- Fresh Gradle gate: `testDebugUnitTest lintDebug assembleDebug` passed after the final detail-screen cleanup.
- APK inspection: package `com.kinplay.app`, min SDK 26, target SDK 35, only AndroidX dynamic receiver permission, v2 signature valid.
- Canonical/runtime content parity: passed.
- Closure state: all 16 items are `fixed-awaiting-retest`; physical Pixel 8 Pro / Android 16 validation remains open.

## 2026-08-06 — KP-BATCH-26606A7F beta3 publication verification

- Source commit: `3aa866b7525632681c728be98279fb9521cd21f7`; `origin/main` matched this SHA.
- Drive folder: `12bINCtZHQwvh3-mIbQ2x-swPE6ACzjYp`
- Published object: `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`
- Link: https://drive.google.com/file/d/16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN/view?usp=drivesdk
- Remote name: `20260806_KinPlay_v0.6.0-beta3_MVP.apk`
- Remote size: `21613823` bytes
- Remote SHA-256: `6c99a77a315773304af353272577fd5b560681d942e3e7f89b61b3f6f3b36f22`
- Remote download comparison: byte-exact pass.
- Final folder inventory: exactly one active APK; beta2 was moved to reversible trash after verification.
- Physical retest remains open on Google Pixel 8 Pro / Android 16 / SDK 36.

## New intake awaiting triage — KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C

- Intake build: **0.6.0-beta3 (8)**. This batch is intake-only and has no retest build.
- Source notes: 11 new unique note IDs; no replayed IDs.
- Existing canonical items touched: `KPF-0004`, `KPF-0010`, `KPF-0017`, `KPF-0055`, `KPF-0058`.
- New canonical items: `KPF-0064`–`KPF-0077`.
- Privacy: No child-identifying information was written to project records.
- Attachments: Three confirmed PNG references are recorded as unprocessed metadata only; no binary attachment was opened, copied, uploaded, or used for generation.
- Code authorization: None — no application code, content, visual asset, attachment, or build was changed.
- Product decisions held: reconcile the two Home drawer variants (`Game categories` expanding drawer versus `Activity themes` slide-out drawer), the All games familiarity-sorted route versus the finite category hierarchy, and the requested single-column cards versus prior two-column layout before implementation.
- Discord acknowledgment: delivered successfully to `#app-development` (channel `1501041077031800932`), message `1535057498514653237`.

| Item | Triage state | Retest build | Next validation |
|---|---|---|---|
| KPF-0064 | new | — | Specify flick physics, settled selection, touch/accessibility behavior, and animal-selection state. |
| KPF-0065 | new | — | Review theme-name-only copy and accessible semantics. |
| KPF-0066 | new | — | Review vertical one-row theme layout with narrow-width and large-text fallbacks. |
| KPF-0067 | new | — | Specify reroll action, randomization, route/back-stack behavior, and repeat policy. |
| KPF-0068 | new | — | Decide familiarity ordering and reconcile the All games route with Level 1 hierarchy. |
| KPF-0069 | new | — | Approve surfacing all six categories on Home and its navigation relationship to All games and activities. |
| KPF-0070 | new | — | Verify the 2-by-3 category arrangement and responsive fallback. |
| KPF-0071 | new | — | Decide the Game categories expanding-drawer variant and verify state/accessibility behavior. |
| KPF-0072 | new | — | Decide the Activity themes slide-out-drawer variant and verify state/accessibility behavior. |
| KPF-0073 | new | — | Verify same-row left/right placement and narrow-width/large-text fallback. |
| KPF-0074 | new | — | Resolve the conflict with KPF-0021 before selecting single-column or two-column card layout. |
| KPF-0075 | new | — | Define the visual-first instruction system, accessibility text alternatives, and content-production scope. |
| KPF-0076 | new | — | Review the three confirmed PNG references and approve an original Tiny Monster visual direction; no assets were generated in intake. |
| KPF-0077 | new | — | Classify play-critical timer/round/session controls per activity and define collapsed-default behavior where essential. |

## Implementation retest — KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C

- Implementation build: **0.6.3 (10)**.
- Authorization: Project-owner standing completion goal explicitly authorized implementation of all feedback in this named batch through the next work-in-progress release.
- Coverage: 19 canonical items — existing `KPF-0004`, `KPF-0010`, `KPF-0017`, `KPF-0055`, `KPF-0058`, plus `KPF-0064`–`KPF-0077` — implemented and recorded below as fixed-awaiting-physical-retest.
- Product decisions: one shared collapsed `Activity themes` / `Game categories` drawer; familiarity-ordered Level 1 All Games with Mad Libs preserved as one collection; single-column full-width cards; timing controls retained only where central to play.
- Automated evidence: `testDebugUnitTest`, `compileDebugAndroidTestKotlin`, `lintDebug`, `assembleDebug`, JSON schema/parity/unique-ID validation, APK metadata/permission/signature checks, Tiny Monster APK resource parity, and `git diff --check` passed.
- APK: `com.kinplay.app`, `0.6.0-beta4` / version code `9`, `22,410,024` bytes, SHA-256 `d1914505d92c9b52a1ad63ae1f9e91da41d0ebd96ee1b1010522f64be4c3298f`; APK Signature Scheme v2 verified.
- Physical retest: **open**. `adb` is not installed and no Android target is connected in Hogwarts; no device result is claimed.
- Attachments: the three supplied worksheet references remain unchanged in the private image cache; the shipped Tiny Monster asset is an original Gemini-generated JPEG, not a copied attachment.

| Item | State | 0.6.3 implementation evidence |
|---|---|---|
| KPF-0004 | fixed-awaiting-physical-retest | Race Like an Animal remains active with the snapping animal wheel. |
| KPF-0010 | fixed-awaiting-physical-retest | Concrete previews, reroll, single-column cards, and visual instruction cues reduce parent invention. |
| KPF-0017 | fixed-awaiting-physical-retest | Theme names are concise and vertical; persisted settings remain. |
| KPF-0055 | fixed-awaiting-physical-retest | Nonessential instruction metadata/session controls are omitted. |
| KPF-0058 | fixed-awaiting-physical-retest | Primary Home actions remain above recents. |
| KPF-0064 | fixed-awaiting-physical-retest | Flickable snapping animal wheel with selected state. |
| KPF-0065 | fixed-awaiting-physical-retest | Theme selector uses names only. |
| KPF-0066 | fixed-awaiting-physical-retest | Theme selector uses one vertical item per row. |
| KPF-0067 | fixed-awaiting-physical-retest | Random flow includes Pick another. |
| KPF-0068 | fixed-awaiting-physical-retest | All Games uses stable familiarity ordering; Mad Libs is not flattened. |
| KPF-0069 | fixed-awaiting-physical-retest | Home exposes all six categories through the shared drawer. |
| KPF-0070 | fixed-awaiting-physical-retest | 2-by-3 wide layout with responsive fallback. |
| KPF-0071 | fixed-awaiting-physical-retest | Game categories terminology and state announcement are present. |
| KPF-0072 | fixed-awaiting-physical-retest | Activity themes terminology and animated drawer are present. |
| KPF-0073 | fixed-awaiting-physical-retest | Primary Home actions share a row where supported. |
| KPF-0074 | fixed-awaiting-physical-retest | Cards use full-width single-column layout. |
| KPF-0075 | fixed-awaiting-physical-retest | Visual instruction shell and per-section cues retain text alternatives. |
| KPF-0076 | fixed-awaiting-physical-retest | Original Gemini Tiny Monster guide is integrated byte-for-byte. |
| KPF-0077 | fixed-awaiting-physical-retest | Session controls are restricted to essential timed activities. |
