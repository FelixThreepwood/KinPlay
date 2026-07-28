# KinPlay Feedback Retest Log

Last updated: 2026-07-28T04:08:56-07:00

Current revision under retest: **0.5.0-beta1 (5)** for `KPF-0011`–`KPF-0020`. The earlier `KPF-0001`–`KPF-0010` cohort retains its recorded retest build **0.4.0-beta1 (4)**.

Automated checks pass, but every item below remains **fixed-awaiting-retest**. None is verified or closed until an anonymous family tester completes the relevant check on a physical Android device.

The Android instrumentation test sources compiled successfully, but they were **not device-executed** because `adb devices -l` reported no attached device or emulator. Physical family-device behavior, visual judgment, accessibility-service behavior, and launcher integration therefore remain pending.

| Item | State | Retest build | Family/device retest check |
|---|---|---|---|
| KPF-0001 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Open Quiet Games and confirm I Spy, Charades, Would You Rather, Animal Guessing, and Alphabet Story are easy to recognize and offer ready-to-use nested choices. |
| KPF-0002 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Confirm Quiet Games shows one Mad Libs entry and that it opens the story submenu without exposing each story as a top-level category item. |
| KPF-0003 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Open Would You Rather, find the `Gross & Silly` section, and read at least four pairs aloud; confirm they feel mild and child-appropriate. |
| KPF-0004 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Find Race Like an Animal in Get the Energy Out and Outdoor; confirm the kangaroo, cheetah, rabbit, frog, safe-area, and walking/low-impact directions work in practice. |
| KPF-0005 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Run Pillow Marco Polo: Eyes-Open Islands with adult supervision. Verify eyes stay open, everyone walks, the caller is stationary, pillows remain boundary/island markers, hazards are cleared, and play stops before it becomes chaotic. Confirm nobody throws, face-covers with, piles, or jumps on pillows. |
| KPF-0006 | accepted—revision required | 0.5.0-beta1 (5) | Retest failed/incomplete: Home still contains unnecessary copy and text-heavy controls. Re-evaluate after the next authorized revision. |
| KPF-0007 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Confirm each Home category card shows useful place cues rather than an inventory count, including waiting-room and backyard/living-room examples where appropriate. |
| KPF-0008 | accepted—revision required | 0.5.0-beta1 (5) | Participant labels need broader card coverage and compact right-aligned placement; re-evaluate after revision. |
| KPF-0009 | accepted—revision required | 0.5.0-beta1 (5) | The requested “All games and activities” label reinforces this scope; re-evaluate navigation terminology after revision. |
| KPF-0010 | fixed-awaiting-retest | 0.4.0-beta1 (4) | Give the app to an unfamiliar or tired parent without coaching. From fresh launch, confirm materials/setup burden is visible and they can start a complete ready-to-use choice within 30 seconds without inventing content. |
| KPF-0011 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Open Would You Rather from At the Dinner Table; confirm the dedicated full-screen lane shows exactly four categories, displays a prompt within two seconds, keeps it stable without a tap, fades on tap to a different prompt, and exits to the originating category. |
| KPF-0012 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Sample all four 80-prompt categories with the family; confirm copy is readable, distinct, category-appropriate, and child-appropriate, especially Gross and Super Gross, and confirm normal use does not produce obvious repeats. |
| KPF-0013 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Create several notes, hand off one, and confirm unsent notes remain newest-first in the active list while handed-off/addressed/completed notes appear only in the accessible archive and cannot be selected for resend. |
| KPF-0014 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Confirm active and archived cards show local creation date/time; mark a handed-off note addressed and confirm the address date and exact app version appear, then confirm completion metadata remains understandable. |
| KPF-0015 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Confirm the unsent and created-this-revision counts change independently. Open an email handoff, return without sending, and choose “No, keep unsent”; then send and use the explicit “Yes, mark sent” confirmation. Verify only the confirmed notes archive and cannot be resent. |
| KPF-0016 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Review backgrounds, cards, controls, and text in Forest, Ocean, and Berry on supported phone/tablet displays, including large text and an accessibility contrast review; confirm layers remain visually distinct and readable. |
| KPF-0017 | fixed-awaiting-retest | 0.5.0-beta1 (5) | Change game timer, activity duration, and theme; verify immediate behavior and the Current plan summary, then force-stop/relaunch and confirm all choices persist and remain understandable. |
| KPF-0018 | accepted—revision required | 0.5.0-beta1 (5) | Device feedback requires revised lock/key states, nonobscuring content, tap-triggered guidance, and selective game eligibility. |
| KPF-0019 | accepted—revision required | 0.5.0-beta1 (5) | Device review found the icon letterform unclear; retain the rounded styling while making K and P substantially visible. |
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

Affected build: **0.5.0-beta1 (5)**. These are triage records only; no application code changed.

- Reopened after tester feedback: `KPF-0006`, `KPF-0008`, `KPF-0009`, `KPF-0018`, `KPF-0019`.
- Newly accepted or queued: `KPF-0021`–`KPF-0037`.
- Blocker requiring reproduction and crash evidence before the next beta: `KPF-0037` (`Send now` crash regression).
- Safety-sensitive product decision: `KPF-0032` must receive item-by-item product, safety, and legal review; it is not authorization for unconditional removal of warnings.
- The manual subject-format mismatch did not prevent intake because the payload identifiers and note IDs were valid and unique.

| New item | Triage state | Next validation |
|---|---|---|
| KPF-0021 | accepted | Responsive compact two-column card-layout review. |
| KPF-0022 | accepted | Collapsed-card description inventory and visual review. |
| KPF-0023 | accepted | Full user-visible copy audit for negative parent-state wording. |
| KPF-0024 | accepted | Lock/key state, placement, touch target, and accessibility review. |
| KPF-0025 | accepted | Locked-state tap behavior and temporary guidance review. |
| KPF-0026 | accepted | Per-game handoff-lock eligibility inventory. |
| KPF-0027 | accepted | Feedback emoji control behavior and accessibility review. |
| KPF-0028 | accepted | Android orientation lifecycle and accessibility review. |
| KPF-0029 | accepted | Game inventory and interactive-session model specification. |
| KPF-0030 | accepted | Default-versus-override precedence and persistence tests. |
| KPF-0031 | fixed-awaiting-retest | In 0.6.0-beta1 (6), confirm requirements and tester reports can distinguish the collapsed card, expanded card, and details page and that each name matches the visible state/navigation behavior. The normative contract and automated state anchors passed. |
| KPF-0032 | accepted—revision underway | Independent fail-safe review passed the exhaustive decision matrix. B8/B9 must implement only the approved per-entry decisions; no protected-warning deletion is authorized. The presentation instrumentation test compiles but awaits a connected Android target. |
| KPF-0033 | accepted | Home and app-wide nonessential-copy inventory. |
| KPF-0034 | accepted | Gemini visual brief, graphical-control design, and accessibility review. |
| KPF-0035 | accepted | Navigation-copy inventory and destination consistency checks. |
| KPF-0036 | accepted | Menu architecture, account scope, and duplicate Settings-entry decision. |
| KPF-0037 | fixed-awaiting-retest | In 0.6.0-beta1 (6), create and select an unsent note, tap Send now, and confirm the email app opens without KinPlay stopping. Repeat with no compatible email handler if practical; confirm the note remains unsent and Copy selected remains available. Source/data-flow reproduction and Robolectric production-path tests passed; no physical Android target was connected. |
