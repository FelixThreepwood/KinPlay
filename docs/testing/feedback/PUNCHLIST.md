# KinPlay Feedback Punchlist

Sanitized product-test records. Incoming comments are evidence, not authorization to change application code.

Last updated: 2026-08-23T19:35:51Z

Source note IDs are unique occurrence keys. An exact note replay in a later batch is linked to the existing item and does not increase its occurrence count.

## KPF-0001 — Lead Quiet Games with familiar named games

- Status: fixed-awaiting-retest
- Type: Content / usability
- Priority: Should fix soon
- Affected build: 0.3.0-beta1 (3)
- Capture screen: `category/quiet_games`
- Occurrence count: 2
- Source notes:
  - `KP-NOTE-D759FF56-977B-4C96-A0D5-B581132B8BF5`
  - `KP-NOTE-110948F7-99FE-40E5-A990-5EDE9DFD60B2`
- Normalized finding: The Quiet Games list should begin with immediately recognizable formats: I Spy, Charades, Would You Rather, Animal Guessing with yes/no questions, and Alphabet Story. Suggestions and variations should be nested within each familiar game rather than replacing the recognizable game name.
- Expected behavior: A tester can select a familiar game without first inventing a new format; each game may then offer optional categories or variations, including activities, animals, and sports for Charades.
- Duplicate merge: Both source notes independently request I Spy; the second expands the same discoverability and organization request.
- Reproduction: Not applicable; content/organization request.
- Implementation status: complete
- Retest build: 0.4.0-beta1 (4)
- Verification result: automated_checks_passed_pending_family_device_retest

## KPF-0002 — Group Mad Libs stories under a Mad Libs submenu

- Status: fixed-awaiting-retest
- Type: Information architecture
- Priority: Should fix soon
- Affected builds: 0.3.0-beta1 (3); 0.6.0-beta1 (6)
- Capture screen: `category/quiet_games`
- Occurrence count: 2
- Source note: `KP-NOTE-D759FF56-977B-4C96-A0D5-B581132B8BF5`
- Additional source note: `KP-NOTE-9F74C227-F346-4826-9487-FAAA377680C9`
- Normalized finding: Individual Mad Libs stories should be collected beneath one Mad Libs entry and submenu.
- Expected behavior: Quiet Games shows one recognizable Mad Libs entry; selecting it reveals the available stories.
- Reproduction: Confirm current Mad Libs stories appear as separate top-level entries during the revision pass.
- Implementation status: complete
- Retest build: 0.4.0-beta1 (4)
- Verification result: automated_checks_passed_pending_family_device_retest
- Latest 0.6.0-beta1 observation: The 0.6.0 feedback explicitly places Mad Libs stories at Level 2 beneath a Level 1 Mad Libs entry; the broader hierarchy is tracked in KPF-0040.

## KPF-0003 — Add a child-appropriate Gross Would You Rather set

- Status: fixed-awaiting-retest
- Type: Content request
- Priority: Backlog
- Affected builds: 0.3.0-beta1 (3); 0.4.0-beta1 (4)
- Capture screen: `category/quiet_games`; supporting expansion captured from `category/dinner_table`
- Occurrence count: 2
- Source notes:
  - `KP-NOTE-39377C47-9245-4B37-BB42-FDF889B5DA33`
  - `KP-NOTE-81C31412-FE6B-4984-9564-3EFF703F7AD4`
- Duplicate merge: Both unique notes independently request child-appropriate gross-humor prompts. The newer note's broader library and interaction requirements are tracked in `KPF-0011` and `KPF-0012`.
- Normalized finding: Add a Gross Would You Rather variation using absurd bodily or food concepts while excluding crass or inappropriate material.
- Content boundary: Gross and silly, but child-appropriate. Sample concepts include hair on teeth versus noodles for hair, and harmless combinations of sneezing, burping, or passing gas.
- Expected behavior: Would You Rather offers a clearly labeled gross-humor variation with reviewed prompts.
- Reproduction: Not applicable; content request.
- Implementation status: complete
- Retest build: 0.4.0-beta1 (4)
- Verification result: automated_checks_passed_pending_family_device_retest

## KPF-0004 — Add Race Like an Animal as an active game

- Status: fixed-awaiting-retest
- Type: Content request
- Priority: Backlog
- Affected builds: 0.3.0-beta1 (3); 0.6.0-beta3 (8)
- Capture screen: `category/quiet_games`
- Occurrence count: 2
- Source note: `KP-NOTE-3B05B013-BD41-408C-82C5-8C908F15D1A1`
- Additional source note: `KP-NOTE-3D279C88-CF60-40B7-B09A-EF407239A89A`
- Latest beta3 intake observation: The spin-wheel refinement is separately tracked in KPF-0064.
- Normalized finding: Add a race in which participants choose an animal and move in that animal's manner. Suggested animals include kangaroo, cheetah, rabbit, and frog.
- Target category: Active/high-energy. This is inferred from the requested activity despite capture occurring on Quiet Games (95% confidence).
- Expected behavior: The activity explains how to choose animals, define a safe race area, and imitate animal movement.
- Reproduction: Not applicable; content request.
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Retest build: 0.4.0-beta1 (4)
- Verification result: automated_checks_passed_pending_family_device_retest
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: Race Like an Animal remains active and now exposes a flickable snapping animal-selection wheel.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0005 — Add Indoor Marco Polo with pillows

- Status: fixed-awaiting-retest
- Type: Content request / safety review
- Priority: Backlog
- Affected build: 0.3.0-beta1 (3)
- Capture screen: `home`
- Occurrence count: 1
- Source note: `KP-NOTE-AA2C17B1-8FA2-44EA-80BB-D2DFD4379AEF`
- Normalized finding: Add an indoor Marco Polo activity that incorporates pillows.
- Expected behavior: The activity supplies ready-to-use setup and play instructions and is safety-reviewed before activation.
- Reproduction: Not applicable; content request.
- Safety status: Redesigned and implementation-complete; awaiting family/device retest.
- Safety redesign: Eyes remain open; players walk only; the adult supervises as a stationary caller. Pillows are flat boundary/island markers only—no throwing, face covering, piling, or jumping—and setup clears stairs, furniture edges, cords, pets, and fragile objects. Stop if play becomes chaotic.
- Implementation status: complete
- Retest build: 0.4.0-beta1 (4)
- Verification result: automated_checks_passed_pending_family_device_retest

## KPF-0006 — Minimize and compact the home screen

- Status: fixed-awaiting-retest
- Type: Usability / information architecture
- Priority: Should fix soon
- Affected builds: 0.3.0-beta1 (3); 0.5.0-beta1 (5); 0.6.0-beta2 (7)
- Capture screen: `home`
- Occurrence count: 5
- Source note: `KP-NOTE-09B10E03-CB5C-4C41-B2F8-E024775D1628`
- Additional source notes: `KP-NOTE-5C67464E-BA76-4E0A-AEF5-F39C2B5FBCE9`; `KP-NOTE-03AE0EF2-D2C7-4BD7-B860-81238110EA88`
- Normalized finding: Keep KinPlay at the upper-left, place a concise one-line descriptor beside it, remove the “What fits now” instructional section, and move the six-category grid upward. Defer richer graphics and animated characters to later development.
- Expected behavior: The first screen presents identity, purpose, and category choices with minimal copy and no unnecessary vertical gap.
- Reproduction: Visual review required against the affected build.
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Additional source note: `KP-NOTE-8BA66EEA-98BB-4FFA-B1C5-68973A6FDF04`
- Additional source note: `KP-NOTE-EC1CCA4A-5421-4C3C-9A16-8BDE4370EE80`
- Latest 0.5.0 feedback: 0.5.0 feedback says Home still contains unnecessary copy and text-heavy controls, so the compact-home acceptance criterion needs revision.
- Latest 0.6.0-beta2 observation: The 0.6.0-beta2 feedback requests moving primary Home browse actions above recents and renaming the Home descriptor to Kid Friendly Family Fun; the separate requirements are tracked in KPF-0058 and KPF-0061.
- Latest implementation build: 0.6.0-beta3 (8)
- Latest implementation evidence: Home descriptor is Kid Friendly Family Fun; browse actions render before favorites and recently played content; the instructional section remains disabled.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.

## KPF-0007 — Show suitable-place cues on category cards

- Status: fixed-awaiting-retest
- Type: Usability / content metadata
- Priority: Should fix soon
- Affected build: 0.3.0-beta1 (3)
- Capture screen: `home`
- Occurrence count: 1
- Source note: `KP-NOTE-961D7088-D634-4A4A-89B2-6F57771E399C`
- Normalized finding: Replace each category card’s game count with a concise list of suitable play locations, including waiting-room cues for quiet activities and backyard or living-room cues for high-energy activities.
- Expected behavior: A parent can choose a category from the current setting without interpreting an inventory count.
- Reproduction: Visual and content review required against the affected build.
- Implementation status: complete
- Retest build: 0.4.0-beta1 (4)
- Verification result: automated_checks_passed_pending_family_device_retest

## KPF-0008 — Label Quality Time activities by group suitability

- Status: fixed-awaiting-retest
- Type: Content metadata / usability
- Priority: Should fix soon
- Affected build: 0.3.0-beta1 (3)
- Capture screen: `home`
- Occurrence count: 2
- Source note: `KP-NOTE-961D7088-D634-4A4A-89B2-6F57771E399C`
- Normalized finding: Mark the Quality Time category and each activity as intended for one-on-one play, group play, or both.
- Expected behavior: A parent can identify participant fit before opening or starting an activity.
- Reproduction: Content review required against the affected build.
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: All active cards now expose reviewed participant-suitability metadata, and the collapsed and expanded card hierarchy renders the compact participant descriptor before opening.
- Additional source note: `KP-NOTE-6B822319-7029-40D5-B19C-022257842493`
- Latest 0.5.0 feedback: 0.5.0 feedback broadens participant-suitability labels to game cards generally and requests right-aligned compact descriptors.

## KPF-0009 — Include activities as well as games

- Status: fixed-awaiting-retest
- Type: Product scope / content request
- Priority: Should fix soon
- Affected build: 0.3.0-beta1 (3)
- Capture screen: `home`
- Occurrence count: 2
- Source note: `KP-NOTE-961D7088-D634-4A4A-89B2-6F57771E399C`
- Normalized finding: Treat suitable activities as first-class content alongside games, including drawing, coloring, and painting.
- Expected behavior: Discovery and category language accommodate both games and activities that support family engagement and children’s creativity.
- Reproduction: Product-copy and content review required.
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Home, destinations, documentation, and accessibility labels use the canonical games-and-activities vocabulary; active activity content remains available alongside games.
- Additional source note: `KP-NOTE-EC1CCA4A-5421-4C3C-9A16-8BDE4370EE80`
- Latest 0.5.0 feedback: 0.5.0 feedback reinforces games-and-activities terminology through the requested “All games and activities” navigation label.

## KPF-0010 — Make low-cognitive-load parent support a product requirement

- Status: fixed-awaiting-retest
- Type: Product principle / usability requirement
- Priority: Should fix soon
- Affected builds: 0.3.0-beta1 (3); 0.6.0-beta3 (8)
- Capture screen: `home`
- Occurrence count: 2
- Source note: `KP-NOTE-B3C04782-B438-4B0E-876D-3A87A15C8D37`
- Additional source note: `KP-NOTE-C381DF5B-D267-4A5A-9E4C-EA261969A3E9`
- Latest beta3 intake observation: The visual-first cross-library requirement is separately tracked in KPF-0075.
- Normalized finding: KinPlay should supply ready-to-use inspiration, choices, and facilitation so an exhausted or overwhelmed parent does not have to invent the activity. The intended outcome is family interaction, closeness, and children’s creativity and imagination.
- Expected behavior: Core flows provide immediately usable choices and guidance while minimizing decisions, setup work, and creative effort required from the parent.
- Related items: `KPF-0001`, `KPF-0006`, `KPF-0007`, `KPF-0008`, `KPF-0009`
- Reproduction: Product acceptance review required.
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Retest build: 0.4.0-beta1 (4)
- Verification result: automated_checks_passed_pending_family_device_retest
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: The 0.6.3 surfaces reduce parent invention through concrete previews, random reroll, single-column discovery cards, and visual instruction cues.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0011 — Add a full-screen Would You Rather play mode

- Status: fixed-awaiting-retest
- Type: Interaction design / usability
- Priority: Backlog
- Affected build: 0.4.0-beta1 (4)
- Capture screen: `category/dinner_table`
- Occurrence count: 1
- Source note: `KP-NOTE-81C31412-FE6B-4984-9564-3EFF703F7AD4`
- Normalized finding: Start Would You Rather in a distraction-minimized full-screen play space with four category choices. After selection, animate one prompt into view within about two seconds, keep it visible until a tap advances, fade between prompts, and provide a persistent exit control to the category screen.
- Expected behavior: A family can start, advance, and exit a readable prompt session with one-tap progression and no unnecessary controls.
- Reproduction: Interaction specification required. Related: `KPF-0003`, `KPF-0012`.
- Implementation evidence: Would You Rather now opens a dedicated distraction-minimized full-screen route with four ordered category choices, a 450 ms prompt fade, tap-only advancement, a persistent Exit control back to the originating category, and child-handoff lock integration.
- Implementation status: complete
- Retest build: 0.5.0-beta1 (5)
- Verification result: automated_checks_passed_pending_family_device_retest


## KPF-0012 — Build a reviewed four-category Would You Rather library

- Status: fixed-awaiting-retest
- Type: Content request / content safety / sourcing
- Priority: Backlog
- Affected build: 0.4.0-beta1 (4)
- Capture screen: `category/dinner_table`
- Occurrence count: 1
- Source note: `KP-NOTE-81C31412-FE6B-4984-9564-3EFF703F7AD4`
- Normalized finding: Provide 80 original or properly licensed, refined prompts in each of four categories: Cute & Silly, Animals, Gross, and Super Gross. Present prompts in randomized order while remembering recent presentations to reduce repetition. Keep all content child-appropriate; Gross may be odd or awkward, while Super Gross may use mild bodily humor without crass or crude material.
- Expected behavior: Each category contains 80 distinct reviewed prompts, randomization avoids obvious repeats, and sourcing records confirm that shipped text is original or licensed rather than copied from curated collections.
- Reproduction: Content and safety review required. Related: `KPF-0003`, `KPF-0011`.
- Implementation evidence: The shipped local library contains exactly 80 approved original prompts in each of Cute & Silly, Animals, Gross, and Super Gross; schema/parser, provenance hashes, child-safety review, duplicate checks, and persisted per-category shuffled bags prevent repeats within a cycle and across restoration boundaries.
- Implementation status: complete
- Retest build: 0.5.0-beta1 (5)
- Verification result: automated_checks_passed_pending_family_device_retest


## KPF-0013 — Separate active and archived feedback notes

- Status: fixed-awaiting-retest
- Type: Feedback workflow / information architecture
- Priority: Should fix soon
- Affected build: 0.4.0-beta1 (4)
- Capture screen: `feedback`
- Occurrence count: 1
- Source note: `KP-NOTE-C480FEC6-4DB8-4E5E-8A42-20D8D7283A42`
- Normalized finding: Sort active feedback newest-first and move notes that were submitted, transmitted, addressed, or otherwise completed into an archive available from the feedback interface.
- Expected behavior: The main feedback list contains current actionable notes in reverse chronological order; archived notes remain accessible without cluttering the active list or being resent unintentionally.
- Reproduction: Workflow review required. Related: `KPF-0014`, `KPF-0015`.
- Implementation evidence: Feedback now separates newest-first unsent notes from a visible archive of handed-off, addressed, and completed notes; archived notes are excluded from editing, selection, email batches, and bulk deletion of unsent notes.
- Implementation status: complete
- Retest build: 0.5.0-beta1 (5)
- Verification result: automated_checks_passed_pending_family_device_retest


## KPF-0014 — Show feedback creation and resolution metadata

- Status: fixed-awaiting-retest
- Type: Feedback workflow / metadata / usability
- Priority: Should fix soon
- Affected build: 0.4.0-beta1 (4)
- Capture screen: `feedback`
- Occurrence count: 1
- Source note: `KP-NOTE-C480FEC6-4DB8-4E5E-8A42-20D8D7283A42`
- Normalized finding: Display each note's creation date and time. For addressed notes, also display the app version in which it was addressed and the address date.
- Expected behavior: Active and archived notes expose enough timestamp and version context to determine when they were created and, when applicable, resolved.
- Reproduction: Data model and ui review required. Related: `KPF-0013`, `KPF-0015`.
- Implementation evidence: Persisted feedback records now retain creation timestamp, timezone, and creation build; active and archived cards display creation date/time, while addressed records also display the addressing version and date and completed or handed-off records display their lifecycle timestamp.
- Implementation status: complete
- Retest build: 0.5.0-beta1 (5)
- Verification result: automated_checks_passed_pending_family_device_retest


## KPF-0015 — Distinguish unsent and since-revision feedback counts

- Status: fixed-awaiting-retest
- Type: Feedback workflow / state management / usability
- Priority: Should fix soon
- Affected build: 0.4.0-beta1 (4)
- Capture screen: `feedback`
- Occurrence count: 1
- Source note: `KP-NOTE-C480FEC6-4DB8-4E5E-8A42-20D8D7283A42`
- Normalized finding: Show separate counts for notes created since the last email handoff and all notes created since the last app revision. Counters and state transitions must prevent previously submitted notes from being included accidentally in a later batch.
- Expected behavior: The tester can distinguish unsent notes from the broader since-revision total, and repeated submissions are avoided through explicit persisted state.
- Reproduction: State transition specification required. Related: `KPF-0013`, `KPF-0014`.
- Implementation evidence: The feedback UI independently reports unsent notes and notes created in the current version; only unsent selected notes are sendable, and opening the email app does not archive them until the tester explicitly confirms that Send was completed, preventing accidental resend of archived notes.
- Implementation status: complete
- Retest build: 0.5.0-beta1 (5)
- Verification result: automated_checks_passed_pending_family_device_retest


## KPF-0016 — Increase color differentiation among interface layers

- Status: fixed-awaiting-retest
- Type: Visual design / accessibility
- Priority: Backlog
- Affected builds: 0.4.0-beta1 (4); 0.6.0-beta2 (7)
- Capture screen: `category/dinner_table`
- Occurrence count: 2
- Source note: `KP-NOTE-0F552AA3-6508-4C02-9488-4C63A6571477`
- Additional source note: `KP-NOTE-7F9A5988-DE6A-46FF-8A2A-EE401EB5950B`
- Normalized finding: Use more distinct colors for the app background, cards, controls, and other components while preserving a coherent palette.
- Expected behavior: Major interface layers are easy to distinguish through color and still satisfy text and control contrast requirements across supported themes.
- Reproduction: Visual and accessibility review required.
- Implementation evidence: Forest, Ocean, and Berry themes now use distinct background, surface, surface-variant, primary, secondary-container, and tertiary-container layers; automated palette tests assert readable text/control contrast and measurable background-to-card separation across every supported theme.
- Implementation status: complete
- Retest build: 0.5.0-beta1 (5)
- Verification result: automated_checks_passed_pending_family_device_retest
- Latest 0.6.0-beta2 observation: The 0.6.0-beta2 feedback requests two additional bright, vibrant, non-pastel themes; the specific theme request is tracked in KPF-0060.
- Latest implementation build: 0.6.0-beta3 (8)
- Latest implementation evidence: Forest, Ocean, Berry, Sunshine, and Tropical palettes provide separated accessible interface layers.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.

## KPF-0017 — Add settings for timers, durations, and color themes

- Status: fixed-awaiting-retest
- Type: Settings / feature request / visual customization
- Priority: Should fix soon
- Affected builds: 0.4.0-beta1 (4); 0.6.0-beta2 (7); 0.6.0-beta3 (8)
- Capture screen: `settings`
- Occurrence count: 4
- Source note: `KP-NOTE-2254611E-4E35-4960-9E4D-FA832B55405F`
- Additional source notes: `KP-NOTE-A6DA9B34-B53F-4C28-84C8-A52BC35BF97E`; `KP-NOTE-7F9A5988-DE6A-46FF-8A2A-EE401EB5950B`
- Additional source note: `KP-NOTE-1E8C3B39-4BBF-44F8-B0F7-143993ADE636`
- Latest beta3 intake observation: Theme-selector copy and list-layout refinements are separately tracked in KPF-0065 and KPF-0066.
- Normalized finding: Provide a settings area for game timers, activity durations, and app color themes.
- Expected behavior: A tester can review and change supported timing and theme preferences from one stable settings destination, with choices persisted across launches.
- Reproduction: Product and data model specification required. Related: `KPF-0016`, `KPF-0020`.
- Implementation evidence: A stable Settings destination now offers finite game-timer, activity-duration, and Forest/Ocean/Berry theme choices; each choice is stored under a versioned key, survives recreation and relaunch, and the timer/theme selections are applied to the play and app surfaces.
- Implementation status: complete; awaiting physical retest
- Retest build: 0.5.0-beta1 (5)
- Verification result: automated_checks_passed_pending_family_device_retest
- Latest 0.6.0-beta2 observation: The 0.6.0-beta2 feedback requests a compact horizontal Settings timing layout and two additional themes; the refinements are tracked in KPF-0059 and KPF-0060.
- Latest implementation build: 0.6.3 (10)
- Latest implementation evidence: Settings persists timer, activity duration, round, theme, launcher, and session preferences through the existing versioned repository.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: Theme choices are name-only and vertical; persisted timing and theme preferences remain intact.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0018 — Add a deliberate child handoff lock mode

- Status: fixed-awaiting-retest
- Type: feature request / interaction safety / platform feasibility
- Priority: Should fix soon
- Affected builds: 0.4.0-beta1 (4); 0.5.0-beta1 (5); 0.6.0-beta1 (6)
- Capture screen: `game_play`
- Occurrence count: 3
- Source note: `KP-NOTE-2254611E-4E35-4960-9E4D-FA832B55405F`
- Additional source note: `KP-NOTE-9FE89973-6551-44D6-B8C9-F7B7F84C9277`
- Normalized finding: Provide a visible lock control that requires a three-second hold to lock or unlock and displays hold progress. While active, the mode should prevent accidental in-app navigation or game exit when the device is handed to a young participant.
- Expected behavior: Lock state is unmistakable, activation and release require a deliberate hold, progress is visible, and accidental app controls or back navigation are guarded within Android platform limits. System-level escape restrictions require a feasibility and safety review.
- Reproduction: Android platform and safety review required. Related: `KPF-0017`.
- Implementation evidence: Game and activity play surfaces now expose a visible three-second hold-to-lock/unlock control with progress, lock-state announcement, keyboard and accessibility activation countdown, and an overlay that blocks in-app controls and Back while preserving the recovery control; Android system controls intentionally remain available.
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Additional source note: `KP-NOTE-3E894705-6D41-4ACA-9299-CCB8D96658F9`
- Latest 0.5.0 feedback: Physical-device feedback requires revised lock/key icon states, nonobscuring locked content, tap-triggered guidance, and per-game eligibility.
- B11 implementation evidence: Eligible play surfaces retain clear content while the lock blocks in-app controls and Back; the lock control remains available for recovery; guidance appears only after a blocked tap and expires after three seconds. Android behavior-test source compilation passed; no connected device was available for execution.
- Latest 0.6.0-beta1 observation: The 0.6.0 feedback requests that the three-second lock countdown be centered on the play surface; the placement refinement is tracked in KPF-0047.

## KPF-0019 — Create an original KinPlay launcher icon through Gemini

- Status: fixed-awaiting-retest
- Type: Brand design / visual asset / trademark review
- Priority: Should fix soon
- Affected build: 0.4.0-beta1 (4)
- Capture screen: `launcher_and_home`
- Occurrence count: 2
- Source note: `KP-NOTE-1B5F8F8F-0D75-444C-821A-A2B435704E1E`
- Normalized finding: Create a clean, minimal KinPlay icon through the required Google Gemini Nano Banana visual process. Explore simple play and curved smile or directional cues, balanced proportions, and teal, emerald, or pale-yellow color directions while maintaining an original identity that does not imitate third-party logos.
- Expected behavior: The approved icon is original, recognizable at launcher sizes, technically valid for Android adaptive-icon use, and preserved as an untouched Gemini master with documented derivatives.
- Reproduction: Gemini visual brief and trademark review required. Related: `KPF-0020`.
- Implementation evidence: Candidate 1A (Teal) and Candidate 1C (Sunshine) were generated through the required Google Gemini Nano Banana path, preserved byte-for-byte with prompt provenance and SHA-256 hashes, independently visually approved for legible rounded K/P forms, safe adaptive margins, flat two-color treatment, launcher-scale readability, and no visible third-party imitation, then integrated into the Android launcher masters and existing adaptive-icon resources. Candidate 1B remains preserved as rejected after weaker separation at launcher scale.
- Implementation status: complete
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Additional source note: `KP-NOTE-707C0296-5231-4583-9EDB-D541887E1E11`
- Latest 0.5.0 feedback: Physical-device icon review found the abstract/cropped letterform unclear. The 0.6.0-beta1 masters preserve rounded curves while making at least 80% of K and most of P visibly recognizable; physical launcher confirmation remains required.

## KPF-0020 — Allow selection among supported launcher-icon colors

- Status: fixed-awaiting-retest
- Type: Settings / launcher customization / platform feasibility
- Priority: Should fix soon
- Affected build: 0.4.0-beta1 (4)
- Capture screen: `settings`
- Occurrence count: 1
- Source note: `KP-NOTE-1B5F8F8F-0D75-444C-821A-A2B435704E1E`
- Normalized finding: Offer a palette of supported colors in settings and apply the selected color to the Android home-screen launcher icon where the platform and launcher permit it.
- Expected behavior: The tester can select from a documented finite palette, the chosen predeclared launcher variant is applied reliably, and unsupported launcher behavior is handled without breaking app launch or upgrades.
- Reproduction: Android launcher feasibility review required. Related: `KPF-0017`, `KPF-0019`.
- Implementation evidence: Settings offers the finite Teal and Sunshine launcher variants backed by predeclared activity aliases; switching enables the target before disabling the previous alias, persists only successful selections, and restores a launchable fallback on failure, while documenting device-launcher cache and refresh variability.
- Implementation status: complete
- Retest build: 0.5.0-beta1 (5)
- Verification result: automated_checks_passed_pending_family_device_retest

## Intake: KP-BATCH-A9A144CD-D6AE-47E6-8C6F-4F14214377E4

- Received: 2026-07-27T17:31:51-07:00
- Affected build: 0.5.0-beta1 (5)
- Source notes: 12 new unique note IDs
- Existing items reopened: KPF-0006, KPF-0008, KPF-0009, KPF-0018, KPF-0019
- New items: KPF-0021–KPF-0037
- Privacy: No child-identifying information was present; generic family-role references were normalized.
- Code authorization: Intake and triage only; no application code change is authorized by this batch.

## KPF-0021 — Use compact two-column hierarchy on game cards

- Status: fixed-awaiting-retest
- Type: Usability / Layout / Information architecture
- Priority: Backlog
- Affected builds: 0.5.0-beta1 (5); 0.6.0-beta2 (7)
- Capture screen: `pick_game`
- Occurrence count: 2
- Source note: `KP-NOTE-6B822319-7029-40D5-B19C-022257842493`
- Additional source note: `KP-NOTE-07295843-9697-48E5-8B8E-02813D34554C`
- Normalized finding: Use available width on both sides of the screen. Keep the primary title and content left-aligned while placing compact descriptors in a right-aligned second column or trailing area on the same row.
- Expected behavior: Collapsed and expanded game cards use horizontal space efficiently without crowding, clipping, or obscuring primary labels.
- Reproduction/triage: Visual and responsive layout review required; automated narrow-width and large-font checks passed.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Collapsed and expanded cards share a compact hierarchy with left-aligned primary content and right-aligned participant, duration, and age descriptors, with a stacked fallback for constrained widths.
- Related items: `KPF-0008`, `KPF-0022`
- Latest 0.6.0-beta2 observation: The 0.6.0-beta2 feedback requests lower-bound-only age labels on cards; the specific copy rule is tracked in KPF-0057.
- Latest implementation build: 0.6.0-beta3 (8)
- Latest implementation evidence: Cards retain a compact responsive two-column expanded hierarchy with a stacked narrow/large-text fallback.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.

## KPF-0022 — Show a concise description on every collapsed game card

- Status: fixed-awaiting-retest
- Type: Usability / Content copy / Discovery
- Priority: Should fix soon
- Affected builds: 0.5.0-beta1 (5); 0.6.0-beta1 (6); 0.6.0-beta2 (7)
- Capture screen: `pick_game`
- Occurrence count: 5
- Additional source note: KP-NOTE-78EE3B8B-81D2-4FA3-B66E-72C7308ACE51
- Latest intake observation: The Level 1 request retains the one-line description requirement while removing expansion; no runtime copy or card behavior changed during intake.
- Latest intake disposition: Supporting evidence added; existing implementation status preserved.
- Additional source note: `KP-NOTE-5EA27820-0F32-4CDB-B88E-9722D7A6852C`
- Source note: `KP-NOTE-6B822319-7029-40D5-B19C-022257842493`
- Additional source note: `KP-NOTE-4BCC8385-0804-44CA-AB1C-684B812BA71E`
- Additional source note: `KP-NOTE-2ED08796-D644-4446-93FC-4BBC50DE1EF0`
- Normalized finding: Every collapsed game card should show the recognizable game name plus one concise description of the activity. Examples include “Guessing game for objects in the room” for I Spy and “Create a wacky story starting each new word with the next letter of the alphabet” for Alphabet Story.
- Expected behavior: A tester can understand the core activity without expanding the card, while the collapsed card remains compact.
- Reproduction/triage: Content inventory and visual review required; all active summaries and card rendering tests passed.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Every collapsed game card exposes a concise reviewed activity summary before expansion while retaining material/setup previews and existing actions.
- Related items: `KPF-0001`, `KPF-0021`
- Latest 0.6.0-beta1 observation: The 0.6.0 feedback restates minimal default-collapsed card content and adds explicit bold-emphasis requirements tracked in KPF-0042.
- Latest 0.6.0-beta2 observation: The 0.6.0-beta2 feedback broadens collapsed-card minimalism to every level and removes metadata, materials, and instructions from the collapsed state; the cross-level rule is tracked in KPF-0056.
- Latest implementation build: 0.6.0-beta3 (8)
- Latest implementation evidence: Collapsed cards expose the reviewed title and one-sentence description only.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.
- Latest intake batch: `KP-BATCH-7423FBCE-A279-49D5-B9E5-B0DF21D74F0E` (0.7.0-beta2 (12))
- Intake disposition: accepted_for_future_revision
- New intake observation: The 0.7.0-beta2 intake requests the exact I Spy description `guessing game of objects hidden in plain sight.`; no runtime copy changed during intake.
## KPF-0023 — Remove negative parent-state framing from user-visible copy

- Status: fixed-awaiting-retest
- Type: Content copy / Product tone / Usability
- Priority: Should fix soon
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `detail/quiet_color_hunt`
- Occurrence count: 1
- Source note: `KP-NOTE-8F46F2BF-A0E4-4ED4-A0CD-F3B3F4E26771`
- Normalized finding: Do not tell or imply to the user that they are tired or not creative. In I Spy, replace the instruction that references being tired with a neutral “Clues and suggestions” heading followed by the supplied clues.
- Expected behavior: All user-visible content offers ready-made help in neutral, supportive language without characterizing the parent negatively.
- Reproduction/triage: Copy inventory review required.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Replaced the I Spy parent-state wording in both seed assets and the review export; the rendered setup preview and detail section use the neutral `Clues and suggestions` heading.
- Related items: `KPF-0010`

## KPF-0024 — Use compact lock and key emoji controls for handoff lock state

- Status: fixed-awaiting-retest
- Type: Interaction design / Visual design / Accessibility
- Priority: Should fix soon
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `game_play`
- Occurrence count: 1
- Source note: `KP-NOTE-3E894705-6D41-4ACA-9299-CCB8D96658F9`
- Normalized finding: Place a compact circular control at the right side of eligible play screens. Show 🔒 when unlocked and available to lock; after locking, show 🔑 to indicate the three-second unlock action.
- Expected behavior: The compact control communicates both current state and the available three-second action, with an accessible text label independent of the emoji.
- Reproduction/triage: Visual state and accessibility review required.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: The handoff control uses compact circular 🔒/🔑 states with independent accessible action and progress semantics.
- Related items: `KPF-0018`, `KPF-0025`, `KPF-0026`

## KPF-0025 — Keep locked content clear and show unlock guidance only after a tap

- Status: fixed-awaiting-retest
- Type: Interaction design / Usability / State management
- Priority: Should fix soon
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `game_play`
- Occurrence count: 1
- Source note: `KP-NOTE-3E894705-6D41-4ACA-9299-CCB8D96658F9`
- Normalized finding: While handoff lock is active, keep game content clear and visible and remove the persistent locked notification. If the locked screen is tapped, temporarily show “Hold key for 3 seconds to unlock.”
- Expected behavior: Lock blocks unintended interaction without obscuring content; guidance appears only in response to an attempted tap and does not become permanent clutter.
- Reproduction/triage: Interaction and accessibility specification required.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Locked content remains visible and input-blocked; a blocked tap alone reveals temporary `Hold key for 3 seconds to unlock` guidance.
- Related items: `KPF-0018`, `KPF-0024`

## KPF-0026 — Limit handoff lock to selected child-interaction games

- Status: fixed-awaiting-retest
- Type: Feature scope / Content metadata / Usability
- Priority: Should fix soon
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `game_play`
- Occurrence count: 1
- Source note: `KP-NOTE-3E894705-6D41-4ACA-9299-CCB8D96658F9`
- Normalized finding: Show the handoff lock only for activities in which a child is expected to interact with or read from the phone directly, such as Charades and Would You Rather. Most games should not show it.
- Expected behavior: Each game has reviewed lock eligibility, and noneligible detail or play screens do not display the lock control.
- Reproduction/triage: Content inventory and eligibility review required.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Added an explicit reviewed eligibility decision for all 53 active seed items. Only Charades and Would You Rather show the lock; all other active content remains lock-free. Detail and Would You Rather routes consume the parsed decision.
- Related items: `KPF-0018`

## KPF-0027 — Replace the text feedback control with a compact note emoji button

- Status: fixed-awaiting-retest
- Type: Interaction design / Visual design / Accessibility
- Priority: Should fix soon
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `detail/quiet_color_hunt`
- Occurrence count: 1
- Source note: `KP-NOTE-F1847D47-12F5-4B28-ACD9-4E7F48B372A8`
- Normalized finding: Replace the text feedback button on game detail surfaces with a tight circular 📝 control.
- Expected behavior: The compact control opens the same feedback flow, has an accessible label, and remains easy to identify and tap.
- Reproduction/triage: Visual and accessibility review required.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Replaced the expanded text feedback launcher with a compact 📝 floating control while retaining the existing capture, send, and copy flow and an accessible unsent-count label.

## KPF-0028 — Default Would You Rather play to landscape orientation

- Status: fixed-awaiting-retest
- Type: Orientation / Interaction design / Android platform
- Priority: Backlog
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `would_you_rather_play`
- Occurrence count: 1
- Source note: `KP-NOTE-A732BE90-95FD-4C5A-9297-1A289CE99388`
- Normalized finding: Would You Rather should enter its play experience in landscape orientation by default.
- Expected behavior: Starting Would You Rather presents a stable landscape play surface and returns cleanly to the prior app orientation on exit, subject to Android accessibility and device-rotation review.
- Reproduction/triage: Android orientation and accessibility review required.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: A route-scoped orientation controller enters landscape for Would You Rather play, preserves the prior orientation, skips forcing in multi-window, and defers restoration safely across configuration recreation.
- Related items: `KPF-0011`

## KPF-0029 — Add a Start action and timed session flow to most game details

- Status: fixed-awaiting-retest
- Type: Interaction design / Session state / Settings
- Priority: Should fix soon
- Affected builds: 0.5.0-beta1 (5); 0.6.0-beta1 (6)
- Capture screen: `game_detail`
- Occurrence count: 2
- Source note: `KP-NOTE-49411202-49E2-4557-A918-BB28E68BAB52`
- Additional source note: `KP-NOTE-32F69299-ACB6-4D16-BFB6-79997D05280B`
- Normalized finding: For most games, place a Start action near the top of the detail page and begin an interactive session using the configured default duration.
- Expected behavior: Eligible game detail pages clearly separate reading from starting, and Start launches a consistent session using current defaults.
- Reproduction/triage: Game inventory and session model specification complete; Android interactive-session retest remains required.
- Decision: accepted for future revision
- Implementation status: complete; B14 established the eligible-details Start foundation and B15 adds the interactive timer, round progress, completion, and exit surface.
- Retest build: 0.6.0-beta1 (6)
- Verification result: b15_automated_checks_passed_pending_family_device_retest
- Implementation evidence: Active ordinary `pick_a_game` activities expose a visible `Start session` control near the top of the details page, show the applied duration/round values, and navigate with an immutable one-shot `TimedSession`. The timed-session surface persists round, timer, and completion state through recreation, advances automatically when a round timer expires, supports explicit round completion and exit, and uses the reviewed child-handoff lock for eligible games. Prompt, story, draft, and calm-only content remains reading-oriented until its session eligibility is reviewed.
- Related items: `KPF-0017`, `KPF-0030`
- Latest 0.6.0-beta1 observation: The 0.6.0 feedback requests that the active session surface show only play steps; the display-policy refinement is tracked in KPF-0052.

## KPF-0030 — Add default rounds and per-game duration and round overrides

- Status: fixed-awaiting-retest
- Type: Settings / Session configuration / Feature request
- Priority: Should fix soon
- Affected builds: 0.5.0-beta1 (5); 0.6.0-beta1 (6)
- Capture screen: `settings_and_game_detail`
- Occurrence count: 2
- Source note: `KP-NOTE-49411202-49E2-4557-A918-BB28E68BAB52`
- Additional source note: `KP-NOTE-DC0BC23F-1578-475F-91AD-CA14C0853CFB`
- Normalized finding: Add a default number of rounds to Settings. On each eligible game detail page, allow the tester to choose session duration and number of rounds without silently changing the global defaults.
- Expected behavior: Defaults persist across launches, each eligible game can override duration and rounds for the next session, and the applied values are visible before Start.
- Reproduction/triage: Settings precedence and session model specification required.
- Decision: accepted for future revision
- Implementation status: complete; B14 adds eligible details-page duration and round controls with independent per-game next-session overrides, reset-to-defaults behavior, and visible applied values.
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Settings defaults remain persisted globally; details-page selectors persist only the selected game's override, and the Start foundation consumes that override once without mutating global duration or round defaults. JVM session tests, the details-page contract test, Android-test source compilation, debug assembly, lint, and the checkpoint validator passed. No Android target was connected.
- Related items: `KPF-0017`, `KPF-0029`
- Latest 0.6.0-beta1 observation: The 0.6.0 feedback requests scrollable incremental session controls and explicit duration/round bounds; those refinements are tracked in KPF-0050 and KPF-0051.

## KPF-0031 — Adopt collapsed card, expanded card, and details page as canonical game-view terms

- Status: fixed-awaiting-retest
- Type: Product vocabulary / Information architecture
- Priority: Backlog
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `game_discovery`
- Occurrence count: 1
- Source note: `KP-NOTE-49411202-49E2-4557-A918-BB28E68BAB52`
- Normalized finding: Use three canonical view names in product records and specifications: collapsed card view, expanded card view, and details page.
- Expected behavior: Requirements, tests, and future feedback can refer unambiguously to one of the three game representations.
- Reproduction/triage: Complete. The normative vocabulary contract maps all three terms to their Kotlin, state, navigation, and visible-content boundaries.
- Decision: accepted product vocabulary
- Implementation status: complete
- Retest build: 0.6.0-beta1 (6)
- Verification: Vocabulary contract, state anchors, focused logic tests, full JVM unit suite, and independent safety/spec review passed; physical-device terminology/UI confirmation remains pending.

## KPF-0032 — Remove safety labels and instructional safety copy from the normal interactive interface

- Status: fixed-awaiting-retest
- Type: Content copy / Information architecture / Safety review
- Priority: Should fix soon
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `all_content_surfaces`
- Occurrence count: 1
- Source note: `KP-NOTE-63E99743-64A8-4EC7-A8A7-426A9722690E`
- Normalized finding: The tester requests that safety tags, safety references, and safety instructional content not be shown throughout normal end-user play surfaces, while safety remains intrinsic to content design and implementation.
- Expected behavior: The interface avoids repetitive safety labeling, but any warning required to prevent foreseeable harm or satisfy legal, platform, or product obligations remains available through a reviewed nonintrusive design.
- Reproduction/triage: Item-by-item fail-safe safety/spec review complete; implementation remains allocated across B8 and B9.
- Decision: implement only matrix-approved retain/relocate/collapse/remove decisions; no protected-warning deletion authorized
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Removed normal detail safety-tag rendering, retained reviewed warning-bearing content and privacy/lock boundaries, exposed reviewed safety summaries through Safety and privacy, and synchronized the neutral I Spy copy with the fail-safe matrix. No protected warning was deleted.
- Related items: `KPF-0005`, `KPF-0036`
- Triage note: Independent review passed the 587-entry decision matrix (540 content, 37 Kotlin, 10 safety-tag entries). All 154 protected entries are retain/relocate only, `uiDeletionAuthorized` is false, and connected-device execution of the compiled warning-presentation instrumentation test remains pending.

## KPF-0033 — Remove nonessential descriptive copy from Home and content surfaces

- Status: fixed-awaiting-retest
- Type: Content copy / Usability / Minimalism
- Priority: Should fix soon
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `home`
- Occurrence count: 1
- Source note: `KP-NOTE-8BA66EEA-98BB-4FFA-B1C5-68973A6FDF04`
- Normalized finding: Remove nonessential statements, including the Home subtitle “Offline parent-led choices for ages 2–8,” and audit the rest of the app for similarly unnecessary explanatory copy.
- Expected behavior: Home prioritizes actions and recognizable labels; retained copy has a clear decision, accessibility, legal, or instructional purpose.
- Reproduction/triage: Copy inventory and product review required.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Removed the reported Home subtitle and repeated list/detail headings while retaining action labels, accessibility text, safety/privacy notices, and activity instructions. Added source-level copy-absence and responsive Home coverage.
- Related items: `KPF-0006`

## KPF-0034 — Replace text-heavy Home shortcuts with compact graphical controls

- Status: fixed-awaiting-retest
- Type: Visual design / Interaction design / Minimalism
- Priority: Backlog
- Affected builds: 0.5.0-beta1 (5); 0.6.0-beta1 (6)
- Capture screen: `home`
- Occurrence count: 2
- Source note: `KP-NOTE-EC1CCA4A-5421-4C3C-9A16-8BDE4370EE80`
- Additional source note: `KP-NOTE-68C2E46E-D684-4496-B5B5-278134169017`
- Normalized finding: Use more graphical controls on Home instead of plain text boxes. Remove subtext from every “More ways to start” control and represent Settings with a compact gear icon.
- Expected behavior: Home shortcuts are compact, recognizable, accessible, and do not depend on decorative subtext.
- Reproduction/triage: Gemini visual brief and accessibility review required.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Home shortcuts now use compact graphical cues, readable labels, semantic click labels, minimum touch targets, and no rendered shortcut subtext; native symbols were used instead of a custom raster asset.
- Related items: `KPF-0006`, `KPF-0035`, `KPF-0036`
- Latest 0.6.0-beta1 observation: The 0.6.0 feedback broadens the graphical-cue request from Home shortcuts to standard icons in the overflow menu; the specific menu icon set is tracked in KPF-0039.

## KPF-0035 — Shorten Home shortcut labels

- Status: fixed-awaiting-retest
- Type: Content copy / Navigation / Usability
- Priority: Should fix soon
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `home`
- Occurrence count: 1
- Source note: `KP-NOTE-EC1CCA4A-5421-4C3C-9A16-8BDE4370EE80`
- Normalized finding: Rename “Pick for me” to “Random game” and rename the browse shortcut to “All games and activities.”
- Expected behavior: The two shortcut labels are concise, accurately describe their destinations, and remain consistent across Home, navigation, tests, and accessibility labels.
- Reproduction/triage: Navigation copy inventory required.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Home and its destinations use the exact `Random game` and `All games and activities` labels with matching navigation and accessibility labels.
- Related items: `KPF-0009`, `KPF-0034`

## KPF-0036 — Add a top-right application menu on Home

- Status: fixed-awaiting-retest
- Type: Navigation / Information architecture / Feature request
- Priority: Should fix soon
- Affected builds: 0.5.0-beta1 (5); 0.6.0-beta1 (6)
- Capture screen: `home`
- Occurrence count: 3
- Source note: `KP-NOTE-EE140225-4007-4170-8BC4-4BE68C4DAFB3`
- Additional source notes: `KP-NOTE-FB8A29AA-6D0F-40A0-98C4-0E8096282253`; `KP-NOTE-68C2E46E-D684-4496-B5B5-278134169017`
- Normalized finding: Place a conventional three-line menu control at the upper-right of Home. Its menu should provide Settings, Account, About the app, and Safety and Privacy destinations.
- Expected behavior: The menu is accessible, compact, and each listed destination either opens a complete screen or is explicitly staged behind an approved product decision; duplicate Settings entry points are resolved intentionally.
- Reproduction/triage: Navigation architecture and account scope review required.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: The upper-right three-line menu routes to Settings, staged Account, About the app, and Safety and privacy; duplicate Settings entry points remain intentionally available and the Account screen states that no account system exists in this MVP.
- Related items: `KPF-0032`, `KPF-0034`
- Triage note: The Settings gear request and Settings menu entry may coexist or be consolidated; product navigation review must choose deliberately.
- Latest 0.6.0-beta1 observation: The 0.6.0 feedback requests removing duplicate Home Settings/Safety and privacy shortcuts and adding standard icons to the overflow destinations; the refinements are tracked in KPF-0038 and KPF-0039.

## KPF-0037 — Fix the Send now feedback crash regression

- Status: fixed-awaiting-retest
- Type: Bug / Feedback workflow / Regression
- Priority: Must fix
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `feedback_send`
- Occurrence count: 1
- Source note: `KP-NOTE-CB7B8AAA-69FC-4DD7-B2FA-2A800E70645D`
- Normalized finding: On 0.5.0-beta1 (5), tapping “Send now” in Feedback causes Android to report that KinPlay keeps stopping. The tester reports that the same handoff worked in 0.4.0-beta1 (4).
- Expected behavior: Tapping “Send now” with a valid feedback selection opens the intended email handoff without crashing, preserves unsent data if handoff cannot start, and handles missing or incompatible email applications safely.
- Reproduction/triage: Source/data-flow reproduction confirmed that `KinPlayApp` passes an application context to the feedback overlay and the handoff called `startActivity` without `FLAG_ACTIVITY_NEW_TASK`; no physical crash trace was available because no Android target was connected.
- Decision: must fix before next beta
- Implementation evidence: The email handoff now follows wrapped contexts to detect an Activity, adds `FLAG_ACTIVITY_NEW_TASK` only when no Activity exists, safely returns failure for runtime launcher errors, keeps unsent notes unchanged, and preserves the Copy fallback. Robolectric production-path regressions cover wrapped Activity, application context, missing/incompatible-launch failure, cycle safety, mailto intent identity, and unsent-only payload behavior.
- Implementation status: complete
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Severity: Blocker
- Related items: `KPF-0013`, `KPF-0015`

## Intake: KP-BATCH-432C6744-035C-4F62-94C1-A1FE4B609C5B

- Received: 2026-08-05T16:40:45-07:00
- Affected build: **0.6.0-beta1 (6)**
- Source notes: 13 new unique note IDs
- Existing canonical items touched: `KPF-0002`, `KPF-0018`, `KPF-0022`, `KPF-0029`, `KPF-0030`, `KPF-0034`, `KPF-0036`
- New canonical items: `KPF-0038`–`KPF-0052` (15 items)
- Replay handling: no replays; occurrence counts use unique supporting source-note IDs.
- Privacy: No child-identifying information was written to project records; generic product context was retained only where needed for triage.
- Subject handling: The `0.6.0-beta1+6` subject form was accepted as a recorded format exception because the body identifiers were valid.
- Code authorization: Intake and triage only; no application code changed.

## KPF-0038 — Remove duplicate Home Settings and Safety and privacy shortcut cards

- Status: new
- Type: navigation / information_architecture / usability
- Priority: should_fix_soon
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `home`
- Occurrence count: 1
- Source note(s): `KP-NOTE-FB8A29AA-6D0F-40A0-98C4-0E8096282253`
- Normalized finding: Remove the rectangular Home shortcuts for Settings and Safety and privacy when those destinations are already available from the top-app-bar overflow menu.
- Expected behavior: Home has one intentional access path per destination in the primary surface; the overflow menu continues to expose both destinations and no required access is lost.
- Reproduction/triage: navigation_review_required
- Triage: This refines the prior decision to retain duplicate Settings entry points. Intake records the change request but does not implement it.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: not_started
- Code authorization: none from this intake
- Related items: `KPF-0034`, `KPF-0036`

## KPF-0039 — Add standard icons to overflow-menu destinations

- Status: new
- Type: visual_design / navigation / accessibility
- Priority: backlog
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `home`
- Occurrence count: 1
- Source note(s): `KP-NOTE-68C2E46E-D684-4496-B5B5-278134169017`
- Normalized finding: Use standard semantic icons in the Home overflow menu: a gear for Settings, a generic person silhouette for Account, an information-in-circle icon for About the app, and a lock or comparable standard privacy/safety icon for Safety and privacy.
- Expected behavior: Each menu item has a recognizable icon plus a readable label and accessible semantics; iconography does not replace text.
- Reproduction/triage: visual_and_accessibility_review_required
- Triage: The broader request for more visual cues reinforces KPF-0034. No custom asset generation was performed during intake.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: not_started
- Code authorization: none from this intake
- Related items: `KPF-0034`, `KPF-0036`

## KPF-0040 — Define the canonical Level 0/1/2 game navigation hierarchy

- Status: new
- Type: information_architecture / navigation / product_vocabulary
- Priority: should_fix_soon
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `game_discovery`
- Occurrence count: 1
- Source note(s): `KP-NOTE-9F74C227-F346-4826-9487-FAAA377680C9`
- Normalized finding: Adopt Level 0 as Home, Level 1 as the next navigation layer, and Level 2 as the selected game or game-type child content; nested Mad Lib stories and Would You Rather statements should not appear in the Level 1 list.
- Expected behavior: A Level 1 card opens the appropriate Level 2 list or content surface, so stories, prompts, and other variants appear only after the parent game or type is selected.
- Reproduction/triage: product_hierarchy_decision_required
- Triage: The note defines the level vocabulary and examples. Reconcile its named-game Level 1 examples with the Level 1 game-type grouping request in KPF-0041 before implementation.
- Decision: accepted_for_triage_product_decision_required
- Implementation status: not_started
- Code authorization: none from this intake
- Related items: `KPF-0002`, `KPF-0041`

## KPF-0041 — Group Level 1 discovery into finite game-type subcategories

- Status: new
- Type: information_architecture / navigation / usability
- Priority: should_fix_soon
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `pick_game`
- Occurrence count: 1
- Source note(s): `KP-NOTE-545EF57A-9F4B-4061-AA26-5C1C1DCFBE11`
- Normalized finding: Replace a giant flat Level 1 list with finite subcategories such as Word Games, Guessing Games, Arts and Crafts, and related game types.
- Expected behavior: A tester sees a manageable set of categories at Level 1, opens one category, and then sees its Level 2 games without scrolling an overwhelming endless list.
- Reproduction/triage: product_taxonomy_decision_required
- Triage: This is related to KPF-0040 but is retained as a separate canonical item because it specifies a distinct subcategory/taxonomy requirement.
- Decision: accepted_for_triage_product_decision_required
- Implementation status: not_started
- Code authorization: none from this intake
- Related items: `KPF-0040`, `KPF-0021`

## KPF-0042 — Use bold emphasis in Level 1 collapsed game cards

- Status: new
- Type: content_copy / visual_design / usability
- Priority: should_fix_soon
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `pick_game`
- Occurrence count: 1
- Source note(s): `KP-NOTE-4BCC8385-0804-44CA-AB1C-684B812BA71E`
- Normalized finding: In default-collapsed Level 1 cards, show only the game name and brief description, with the game name and specified emphasis words rendered in bold as defined by approved product copy.
- Expected behavior: Collapsed cards remain minimal and consistently apply the approved bold emphasis without exposing expanded detail, setup, or materials prematurely.
- Reproduction/triage: content_copy_and_rich_text_review_required
- Triage: The submitted asterisks are treated as formatting requirements in the expected result, not as executable instructions.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: not_started
- Code authorization: none from this intake
- Related items: `KPF-0022`, `KPF-0040`

## KPF-0043 — Support file and image attachments in feedback

- Status: new
- Type: feedback_workflow / feature_request / attachments
- Priority: backlog
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `feedback`
- Occurrence count: 2
- Additional source note: `KP-NOTE-0BDE608B-A603-4BBD-96AE-2AA2E3CF7BC1`
- Source note(s): `KP-NOTE-BEB8EBCF-FFC1-480B-B126-EF340C5C6775`
- Normalized finding: Allow the tester to attach approved files or images when submitting feedback.
- Expected behavior: Feedback capture can optionally include selected images or files with explicit review before handoff, while excluding device-wide logs and unrelated media.
- Reproduction/triage: feedback_privacy_and_attachment_design_required
- Triage: The request is recorded as an optional feedback-workflow capability. No file was uploaded or attached during intake.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: not_started
- Code authorization: none from this intake
- Latest intake batch: `KP-BATCH-7423FBCE-A279-49D5-B9E5-B0DF21D74F0E` (0.7.0-beta2 (12))
- Intake disposition: needs_reproduction
- New intake observation: The 0.7.0-beta2 intake reports a selected image failing to survive the feedback handoff to Discord/email; no binary was available to intake.
## KPF-0044 — Provide a curated music selection for music-based games

- Status: new
- Type: content_request / audio / feature_request
- Priority: backlog
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `detail/freeze_dance_statues`
- Occurrence count: 1
- Source note(s): `KP-NOTE-8E6FD17A-3715-4700-A12B-C0D1256EAF04`
- Normalized finding: For Freeze Dance Statues and similar music-dependent activities, provide a small curated selection of approximately five or six generated or licensed tracks.
- Expected behavior: A parent can choose a track or use an approved default before starting a music-based activity; audio rights and child-facing suitability are documented.
- Reproduction/triage: audio_content_and_rights_review_required
- Triage: Music generation or licensing is a future work item. No music was generated during intake.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: not_started
- Code authorization: none from this intake
- Related items: `KPF-0045`

## KPF-0045 — Add parent-controlled and automatic music playback

- Status: new
- Type: interaction_design / audio / session_state
- Priority: backlog
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `detail/freeze_dance_statues`
- Occurrence count: 1
- Source note(s): `KP-NOTE-8E6FD17A-3715-4700-A12B-C0D1256EAF04`
- Normalized finding: Let a parent start or pause music manually or configure approved automatic playback behavior for music-based games.
- Expected behavior: Play/pause and any automatic start/stop behavior are clear, parent-controlled, and do not interrupt unrelated device audio unexpectedly.
- Reproduction/triage: audio_session_and_android_policy_review_required
- Triage: The single source note contains separate content and playback requirements; they are tracked independently.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: not_started
- Code authorization: none from this intake
- Related items: `KPF-0044`

## KPF-0046 — Create a reviewed 120-card Charades library by category

- Status: new
- Type: content_request / content_sourcing / feature_scope
- Priority: backlog
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `timed_session/{gameId}/{duration}/{rounds}`
- Occurrence count: 1
- Source note(s): `KP-NOTE-D7BE1AAA-1FEB-4260-90E4-1EA7ACABDB62`
- Normalized finding: Provide about 120 distinct Charades cards organized into categories such as animals, activities, and objects.
- Expected behavior: A Charades session draws from a reviewed, nonrepeating, category-aware card set with a documented count and safe original or licensed content.
- Reproduction/triage: content_inventory_sourcing_and_safety_review_required
- Triage: The request to search online for the actual game's count is recorded as a future research/sourcing task only; no external content was copied or searched during intake.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: not_started
- Code authorization: none from this intake

## KPF-0047 — Center the three-second lock countdown on the play surface

- Status: new
- Type: interaction_design / accessibility / visual_design
- Priority: should_fix_soon
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `timed_session/{gameId}/{duration}/{rounds}`
- Occurrence count: 1
- Source note(s): `KP-NOTE-9FE89973-6551-44D6-B8C9-F7B7F84C9277`
- Normalized finding: Show the three-second hold countdown centrally on the screen while lock or unlock is in progress.
- Expected behavior: The parent can see hold progress at the center of the play surface, with accessible announcements and no loss of the recovery control.
- Reproduction/triage: visual_touch_and_accessibility_review_required
- Triage: This is a placement refinement to the existing lock behavior, not authorization to modify the lock implementation.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: not_started
- Code authorization: none from this intake
- Related items: `KPF-0018`, `KPF-0024`

## KPF-0048 — Evaluate a brain and movement activity section with picture diagrams

- Status: new
- Type: product_scope / content_request / visual_asset
- Priority: backlog
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `pick_game`
- Occurrence count: 1
- Source note(s): `KP-NOTE-3F95E653-8262-4CB6-8033-C09F6C9EBD8D`
- Normalized finding: Consider a dedicated section of bilateral brain-gym and physical activities accompanied by picture diagrams; any claim that activities improve brain health requires evidence review and careful wording.
- Expected behavior: If accepted, the section offers reviewed, age-appropriate activity instructions and clear diagrams without unsupported health claims.
- Reproduction/triage: product_scope_health_claim_and_visual_review_required
- Triage: No health-claim research or image generation was performed during intake.
- Decision: accepted_for_triage_product_decision_required
- Implementation status: not_started
- Code authorization: none from this intake

## KPF-0049 — Rename Three Word Story to Five Word Story

- Status: new
- Type: content_copy / content_request
- Priority: backlog
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `detail/three_word_story`
- Occurrence count: 1
- Source note(s): `KP-NOTE-48AF1DD9-570B-4391-81DB-E35ACFD8DBFD`
- Normalized finding: Change the activity title and corresponding visible references from Three Word Story to Five Word Story.
- Expected behavior: The title, instructions, accessibility labels, navigation, and content references consistently reflect the five-word format, subject to product and content review.
- Reproduction/triage: content_inventory_and_copy_review_required
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: not_started
- Code authorization: none from this intake

## KPF-0050 — Make session-selection controls scrollable and incremental by default

- Status: new
- Type: interaction_design / settings / usability
- Priority: should_fix_soon
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `detail/washable_coloring_together`
- Occurrence count: 1
- Source note(s): `KP-NOTE-DC0BC23F-1578-475F-91AD-CA14C0853CFB`
- Normalized finding: Present the per-game session selection controls in a scrollable incremental control by default rather than three static radio options.
- Expected behavior: The parent can adjust duration and round values through a compact scrollable control with clear applied values; the control remains usable at narrow widths and large text scales.
- Reproduction/triage: interaction_and_responsive_layout_review_required
- Triage: The note's selection-method wording is interpreted as the details-page duration/round controls; exact native control behavior requires design review.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: not_started
- Code authorization: none from this intake
- Related items: `KPF-0030`

## KPF-0051 — Cap session duration at 20 minutes and rounds at 15

- Status: new
- Type: settings / session_configuration / validation
- Priority: should_fix_soon
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `detail/washable_coloring_together`
- Occurrence count: 1
- Source note(s): `KP-NOTE-DC0BC23F-1578-475F-91AD-CA14C0853CFB`
- Normalized finding: Set a maximum of 20 minutes per round and a maximum of 15 rounds for per-game sessions.
- Expected behavior: No settings or details-page control can create a session above either bound; invalid stored or incoming values are rejected or safely clamped without changing unrelated defaults.
- Reproduction/triage: configuration_bounds_and_persistence_review_required
- Triage: Product review must confirm whether the caps apply to global defaults, per-game overrides, and already stored values.
- Decision: accepted_for_triage_product_decision_required
- Implementation status: not_started
- Code authorization: none from this intake
- Related items: `KPF-0030`

## KPF-0052 — Show only play steps during an active session

- Status: new
- Type: interaction_design / usability / session_state
- Priority: should_fix_soon
- Affected build: 0.6.0-beta1 (6)
- Capture screen: `timed_session/{gameId}/{duration}/{rounds}`
- Occurrence count: 1
- Source note(s): `KP-NOTE-32F69299-ACB6-4D16-BFB6-79997D05280B`
- Normalized finding: Once an activity session starts, reduce the active session surface to the steps needed to play and omit materials and setup instructions from that active view.
- Expected behavior: The timed session provides the playable steps and necessary controls while materials and setup remain available on the details page or prior setup surface.
- Reproduction/triage: session_content_and_safety_review_required
- Triage: Necessary safety or play-critical instructions must not be removed without item-by-item review; this record captures the requested active-session presentation policy only.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: not_started
- Code authorization: none from this intake
- Related items: `KPF-0010`, `KPF-0029`


## Beta2 implementation verification — KP-BATCH-432C6744-035C-4F62-94C1-A1FE4B609C5B

- Updated: **2026-08-06T03:06:47Z**
- Implementation build: **0.6.0-beta2 (7)**
- Scope: all 22 canonical items in the batch (`KPF-0002`, `KPF-0018`, `KPF-0022`, `KPF-0029`, `KPF-0030`, `KPF-0034`, `KPF-0036`, and `KPF-0038`–`KPF-0052`).
- Automated status: complete. Unit tests, Android lint, debug assembly, content schemas, canonical/runtime parity, packaged-resource checks, APK metadata, and APK signature verification passed.
- Device status: **fixed-awaiting-retest**. No Android device or emulator is attached to Hogwarts, so physical interaction, visual, launcher, accessibility-service, and audio behavior remain unverified.
- The separately staged Paper Airplanes note `KP-NOTE-27882454-9C8A-40DA-8479-F4A7851CC885` is implemented and recorded in `PAPER_AIRPLANES_IMPLEMENTATION.md`; it remains staged as an intake source for the next email batch.

| Item | State | Retest build | Verification state |
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


## Beta2 publication evidence — KP-BATCH-432C6744-035C-4F62-94C1-A1FE4B609C5B

- Published: **2026-08-06T03:22:38Z**
- Source commit: `204eabacb1b57e6901456b4b2aac7c4ed16e018c`
- Local root artifact: `/mnt/cyberforgex-torrents/KinPlay/apk-drops/20260806_KinPlay_v0.6.0-beta2_MVP.apk`
- Version: `0.6.0-beta2` / code `7` / package `com.kinplay.app`
- SHA-256: `6db0b08efc5ade3b7183c6ec0de4028287574b7f230374edbf9a5f3873c5a2bb`
- Drive folder: `12bINCtZHQwvh3-mIbQ2x-swPE6ACzjYp`
- Drive object: `1tm7P1Wlo4SiqHesMubR67ciRV29a5efP`
- Drive link: https://drive.google.com/file/d/1tm7P1Wlo4SiqHesMubR67ciRV29a5efP/view?usp=drivesdk
- Remote download comparison: passed byte-exact size and SHA-256 verification. The prior beta1 root object was moved to reversible Drive trash only after this comparison; the destination now contains exactly one active APK.


## Intake: KP-BATCH-26606A7F-956F-4699-AE68-328FFA369FBC

- Received: 2026-08-06T00:35:34-07:00
- Affected build: **0.6.0-beta2 (7)**
- Source notes: 11 new unique note IDs
- Existing canonical items touched: `KPF-0006`, `KPF-0016`, `KPF-0017`, `KPF-0021`, `KPF-0022`
- New canonical items: `KPF-0053`–`KPF-0063` (11 items)
- Replay handling: no replays; occurrence counts use unique supporting source-note IDs.
- Privacy: No child-identifying information was written to project records; generic product context was retained only where needed for triage.
- Subject handling: The `0.6.0-beta2+7` subject form was accepted as a recorded format exception because the body identifiers were valid.
- Code authorization: Intake and triage only; no application code changed.

## KPF-0053 — Standardize top-right navigation controls across pages

- Status: fixed-awaiting-retest
- Type: navigation / interaction_design / accessibility
- Priority: should_fix_soon
- Affected build: 0.6.0-beta2 (7)
- Capture screen: `all_pages`
- Occurrence count: 1
- Source note(s): `KP-NOTE-08D3DB8C-F55E-438A-877F-7683717F1D38`
- Normalized finding: Keep or move page navigation controls, including Back, into a consistent top navigation area and align them to the right on every page.
- Expected behavior: Every page exposes a consistent accessible top navigation control; right alignment is applied consistently where approved, system Back remains available, and controls do not collide with titles or other actions.
- Reproduction/triage: navigation_layout_and_platform_convention_review_required
- Triage: The keep/move wording is interpreted as a placement request, not a removal request. Confirm treatment of Android system Back and whether right alignment applies to all navigation controls or only app-bar actions.
- Decision: accepted_for_triage_product_decision_required
- Implementation status: complete
- Code authorization: none from this intake
- Related items: `KPF-0018`, `KPF-0036`
- Latest implementation build: 0.6.0-beta3 (8)
- Latest implementation evidence: All app-bar pages expose an accessible right-aligned top Back action; Android system Back remains available.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.

## KPF-0054 — Hide empty Materials sections on details pages

- Status: fixed-awaiting-retest
- Type: content_visibility / conditional_rendering / usability
- Priority: should_fix_soon
- Affected build: 0.6.0-beta2 (7)
- Capture screen: `detail/*`
- Occurrence count: 1
- Source note(s): `KP-NOTE-851DE16F-2E0D-4B6F-8FBA-91DBA131C649`
- Normalized finding: When an activity has no materials, omit the Materials label, section, placeholder text, and reserved layout space.
- Expected behavior: Details pages show a Materials section only when at least one reviewed material exists; nonempty lists remain clear and accessible.
- Reproduction/triage: content_inventory_and_conditional_rendering_review_required
- Triage: This is a conditional details-page visibility rule; it is separate from collapsed-card minimalism and active-session content reduction.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: complete
- Code authorization: none from this intake
- Latest implementation build: 0.6.0-beta3 (8)
- Latest implementation evidence: Materials sections render only when the reviewed materials list is nonempty.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.

## KPF-0055 — Remove nonessential metadata from game instruction pages

- Status: fixed-awaiting-retest
- Type: content_visibility / content_copy / information_architecture
- Priority: backlog
- Affected builds: 0.6.0-beta2 (7); 0.6.0-beta3 (8)
- Capture screen: `detail/instructions/*`
- Occurrence count: 2
- Source note(s): `KP-NOTE-3D0F19F5-849D-40AB-88A8-5C5A7B63E4FB`
- Additional source note: `KP-NOTE-3578A01C-6F57-44E0-A021-4093A3128150`
- Latest beta3 intake observation: Timer, round, and session-control visibility is separately tracked in KPF-0077.
- Normalized finding: Instruction pages should not show age range, typical duration, activity type, or one-on-one/group suitability metadata.
- Expected behavior: Instruction pages focus on play instructions and required safety or accessibility content; nonessential metadata is omitted consistently across games.
- Reproduction/triage: content_inventory_and_safety_review_required
- Triage: Required safety, legal, accessibility, and play-critical information remains subject to item-by-item review; this record covers nonessential metadata only.
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0032`
- Latest implementation build: 0.6.3 (10)
- Latest implementation evidence: Instruction surfaces no longer render age, typical duration, energy/activity type, or participant-suitability metadata.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: Instruction details retain play-critical content while omitting nonessential metadata and session controls.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0056 — Restrict collapsed cards to name and one-sentence description at every level

- Status: fixed-awaiting-retest
- Type: content_visibility / usability / information_architecture
- Priority: should_fix_soon
- Affected build: 0.6.0-beta2 (7)
- Capture screen: `game_type/{groupId}`
- Occurrence count: 1
- Source note(s): `KP-NOTE-2ED08796-D644-4446-93FC-4BBC50DE1EF0`
- Normalized finding: At Level 0, Level 1, Level 2, and any future card level, collapsed cards show only the game name and one-sentence description.
- Expected behavior: Duration, age, participant suitability, materials, and instructions appear only after expansion or opening; no secondary metadata or reserved sections appear in the collapsed state.
- Reproduction/triage: cross_level_card_content_inventory_and_visual_review_required
- Triage: This is broader and stricter than the prior Level 1 minimal-card note, so it remains a separate canonical item while strengthening KPF-0022.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: complete
- Code authorization: none from this intake
- Related items: `KPF-0021`, `KPF-0022`, `KPF-0040`, `KPF-0042`
- Latest implementation build: 0.6.0-beta3 (8)
- Latest implementation evidence: Collapsed cards at all discovery levels show only title, one-sentence description, and required favorite/expand controls; descriptors appear after expansion.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.

## KPF-0057 — Use minimum-age-only labels on game cards

- Status: fixed-awaiting-retest
- Type: content_copy / accessibility / visual_design
- Priority: should_fix_soon
- Affected build: 0.6.0-beta2 (7)
- Capture screen: `game_type/{groupId}`
- Occurrence count: 1
- Source note(s): `KP-NOTE-07295843-9697-48E5-8B8E-02813D34554C`
- Normalized finding: Replace age ranges with lower-bound-only labels such as Ages 2+ or Ages 6+; do not display a maximum age.
- Expected behavior: Every card age label exposes only the reviewed minimum recommended age in consistent accessible copy; absent or unreviewed values are not fabricated.
- Reproduction/triage: content_inventory_copy_and_accessibility_review_required
- Triage: This changes age-label semantics while leaving the underlying reviewed minimum-age data subject to content review.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: complete
- Code authorization: none from this intake
- Related items: `KPF-0021`
- Latest implementation build: 0.6.0-beta3 (8)
- Latest implementation evidence: Card age labels use the reviewed minimum only, such as Ages 2+ and Ages 6+.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.

## KPF-0058 — Place Home browse actions above recently played games

- Status: fixed-awaiting-retest
- Type: home_layout / navigation / information_architecture
- Priority: backlog
- Affected builds: 0.6.0-beta2 (7); 0.6.0-beta3 (8)
- Capture screen: `home`
- Occurrence count: 2
- Source note(s): `KP-NOTE-5C67464E-BA76-4E0A-AEF5-F39C2B5FBCE9`
- Additional source note: `KP-NOTE-31524398-ADF1-4FED-A7FB-D62C3E5F91EF`
- Latest beta3 intake observation: The same-row left/right arrangement is separately tracked in KPF-0073.
- Normalized finding: On Home Level 0, place Random game and All games and activities before the recently played games list.
- Expected behavior: Primary browse and start actions appear before recents in reading and focus order, while recently played games remain available below.
- Reproduction/triage: home_layout_and_focus_order_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0006`, `KPF-0035`
- Latest implementation build: 0.6.3 (10)
- Latest implementation evidence: Home renders Random game and All games and activities before favorites and recently played content.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: Random game and All games and activities remain above recently played content on Home.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0059 — Use a compact horizontal layout for Settings timing sections

- Status: fixed-awaiting-retest
- Type: settings / visual_design / responsive_layout
- Priority: backlog
- Affected build: 0.6.0-beta2 (7)
- Capture screen: `settings`
- Occurrence count: 1
- Source note(s): `KP-NOTE-A6DA9B34-B53F-4C28-84C8-A52BC35BF97E`
- Normalized finding: Reorganize game timer, activity duration, and related Settings sections for compact horizontal presentation; list the three default duration options in one row when space permits.
- Expected behavior: Settings uses a readable horizontal row for finite duration options at supported widths, with an accessible wrap or stack fallback for narrow screens and large text.
- Reproduction/triage: settings_layout_and_responsive_accessibility_review_required
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: complete
- Code authorization: none from this intake
- Related items: `KPF-0017`, `KPF-0030`
- Latest implementation build: 0.6.0-beta3 (8)
- Latest implementation evidence: Settings timing options use horizontal rows at supported widths with narrow and large-text stacked fallback.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.

## KPF-0060 — Add two vibrant bright color themes

- Status: fixed-awaiting-retest
- Type: visual_design / settings / theme
- Priority: backlog
- Affected build: 0.6.0-beta2 (7)
- Capture screen: `settings`
- Occurrence count: 2
- Additional source note: `KP-NOTE-72DE95BF-2A63-4A14-98C2-99A2D84B58FD`
- Source note(s): `KP-NOTE-7F9A5988-DE6A-46FF-8A2A-EE401EB5950B`
- Normalized finding: Add two additional bright, vibrant themes that are deliberately distinct from pastel or soft palettes.
- Expected behavior: The theme picker offers two reviewed high-saturation options with readable contrast and complete persistence and application behavior.
- Reproduction/triage: theme_palette_contrast_and_persistence_review_required
- Triage: No themes or visual assets were generated during intake.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: complete
- Code authorization: none from this intake
- Related items: `KPF-0016`, `KPF-0017`
- Latest implementation build: 0.6.0-beta3 (8)
- Latest implementation evidence: Sunshine and Tropical are persisted, selectable, bright themes with automated contrast/separation coverage.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.
- Latest intake batch: `KP-BATCH-7423FBCE-A279-49D5-B9E5-B0DF21D74F0E` (0.7.0-beta2 (12))
- Intake disposition: accepted_for_future_revision
- New intake observation: The 0.7.0-beta2 intake requests one additional theme; no theme or runtime content changed during intake.
## KPF-0061 — Rename the Home descriptor to Kid Friendly Family Fun

- Status: fixed-awaiting-retest
- Type: content_copy / home_layout
- Priority: backlog
- Affected build: 0.6.0-beta2 (7)
- Capture screen: `home`
- Occurrence count: 1
- Source note(s): `KP-NOTE-03AE0EF2-D2C7-4BD7-B860-81238110EA88`
- Normalized finding: Replace the Home or Level 0 title descriptor Family play with Kid Friendly Family Fun.
- Expected behavior: The approved phrase appears consistently on the Home surface and in applicable accessibility labels, navigation metadata, and review exports.
- Reproduction/triage: home_copy_inventory_and_accessibility_review_required
- Triage: This is a copy request only; no source or content file was changed.
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: complete
- Code authorization: none from this intake
- Related items: `KPF-0006`, `KPF-0033`
- Latest implementation build: 0.6.0-beta3 (8)
- Latest implementation evidence: Home descriptor is Kid Friendly Family Fun in the app surface and the batch release records.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.

## KPF-0062 — Provide hierarchical search for games and activities

- Status: fixed-awaiting-retest
- Type: feature_request / search / navigation
- Priority: backlog
- Affected build: 0.6.0-beta2 (7)
- Capture screen: `pick_game / game_discovery`
- Occurrence count: 1
- Source note(s): `KP-NOTE-8428B5CB-0052-480A-809C-2A3BD4D395C1`
- Normalized finding: Make search accessible from most hierarchical discovery pages and search game and activity titles, descriptions, and instructions.
- Expected behavior: A tester can invoke search from supported levels, receive relevant results from the indexed fields, and open results without losing hierarchy or back-stack context.
- Reproduction/triage: search_scope_index_and_navigation_design_required
- Triage: Search scope, instruction indexing, and result navigation need product and content-model review; no search was performed during intake.
- Decision: accepted_for_triage_product_decision_required
- Implementation status: complete
- Code authorization: none from this intake
- Related items: `KPF-0040`, `KPF-0041`
- Latest implementation build: 0.6.0-beta3 (8)
- Latest implementation evidence: Search is available from Home and discovery hierarchy pages and indexes titles, descriptions, and instructions.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.

## KPF-0063 — Add favorite toggles and a favorites list to game cards

- Status: fixed-awaiting-retest
- Type: feature_request / state_management / navigation
- Priority: should_fix_soon
- Affected build: 0.6.0-beta2 (7)
- Capture screen: `game_type/{groupId}`
- Occurrence count: 2
- Additional source note: KP-NOTE-78EE3B8B-81D2-4FA3-B66E-72C7308ACE51
- Latest intake observation: The Level 1 redesign must preserve the star favorite behavior and persisted state; no runtime favorite state changed during intake.
- Latest intake disposition: Supporting evidence added; existing implementation status preserved.
- Source note(s): `KP-NOTE-A2A74C8D-4C2D-497D-8692-CD52BDC961A5`
- Normalized finding: Show an initially empty star on every game card; tapping it toggles the favorite state and adds or removes the game from a favorites list.
- Expected behavior: Favorite state is persisted, accessible, reversible, visible on each card, and reflected by the favorites destination without duplicating or losing games.
- Reproduction/triage: favorite_state_persistence_and_navigation_design_required
- Triage: This is a new persistent collection capability; no favorite state was created or changed during intake.
- Decision: accepted_for_triage_product_decision_required
- Implementation status: complete
- Code authorization: none from this intake
- Latest implementation build: 0.6.0-beta3 (8)
- Latest implementation evidence: Cards expose accessible Star/StarBorder toggles; favorite state persists and the Favorites destination reflects additions/removals.
- Direct implementation authorization: User request in the active Discord session.
- Automated verification: beta3 unit tests, lint, assemble, APK metadata/permission/signature checks, and byte parity passed; Pixel 8 Pro / Android 16 retest remains open.
- Distribution: beta3 published to the verified private Drive folder; object `16fa3s_Zo7R7e9kV7nmjQ_hdUlc95DRTN`, byte-exact remote read-back passed.

## Intake: KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C

- Received: 2026-08-06T15:40:54-07:00
- Affected build: **0.6.0-beta3 (8)**
- Source notes: 11 new unique note IDs; no replayed IDs.
- Existing canonical items touched: `KPF-0004`, `KPF-0010`, `KPF-0017`, `KPF-0055`, `KPF-0058`.
- New canonical items: `KPF-0064`–`KPF-0077` (14 items).
- Canonical items touched: 19.
- Privacy: No child-identifying information was written to project records. The project records contain no names, contact details, images, birthdates, or precise locations.
- Attachments: One note lists three tester-confirmed PNG references; only generic count/type and unprocessed status were recorded in project records. No binary attachment was opened, copied, uploaded, or used for generation during intake.
- Subject handling: The `0.6.0-beta3+8` subject form was accepted as a recorded format exception because the body identifiers were valid.
- Code authorization: **None — intake and triage only.** No application code, content, visual asset, attachment, or build was changed.
- Duplicate handling: Notes 5 and 6 share the category-surface item `KPF-0069`; their conflicting drawer variants remain separate in `KPF-0071` and `KPF-0072`. The single-column card request remains separate from prior two-column item `KPF-0021`.
- Discord acknowledgment: delivered successfully to `#app-development` (channel `1501041077031800932`), message `1535057498514653237`.

| Source note | Canonical mapping |
|---|---|
| `KP-NOTE-3D279C88-CF60-40B7-B09A-EF407239A89A` | `KPF-0004`, `KPF-0064` |
| `KP-NOTE-1E8C3B39-4BBF-44F8-B0F7-143993ADE636` | `KPF-0017`, `KPF-0065`, `KPF-0066` |
| `KP-NOTE-41275DAB-D14F-4523-9149-A40922E95FD3` | `KPF-0067` |
| `KP-NOTE-512BCF4D-3404-4D15-9D7F-82DC650238DB` | `KPF-0068` |
| `KP-NOTE-7A845776-A132-4FBB-96D4-461CA64C3FA8` | `KPF-0069`, `KPF-0070`, `KPF-0071` |
| `KP-NOTE-1471739F-9A9F-40CC-93B2-686326B8741E` | `KPF-0069`, `KPF-0072` |
| `KP-NOTE-31524398-ADF1-4FED-A7FB-D62C3E5F91EF` | `KPF-0058`, `KPF-0073` |
| `KP-NOTE-766272D5-4E46-488C-93D1-B749DE2723CB` | `KPF-0074` |
| `KP-NOTE-C381DF5B-D267-4A5A-9E4C-EA261969A3E9` | `KPF-0010`, `KPF-0075` |
| `KP-NOTE-8041A19A-A852-401D-AA51-B562401DC1B3` | `KPF-0076` |
| `KP-NOTE-3578A01C-6F57-44E0-A021-4093A3128150` | `KPF-0055`, `KPF-0077` |

## KPF-0064 — Add a flickable animal-selection wheel to Race Like an Animal

- Status: fixed-awaiting-retest
- Type: interaction_design / feature_request / game_mechanic
- Priority: Backlog
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `detail/race_like_an_animal`
- Content ID: `race_like_an_animal`
- Occurrence count: 1
- Source note(s): `KP-NOTE-3D279C88-CF60-40B7-B09A-EF407239A89A`
- Normalized finding: Add a touch-driven spin wheel to Race Like an Animal so a tester can flick the wheel and receive an animal selection.
- Expected behavior: The wheel responds to a finger flick, spins with understandable motion, and settles on one animal that becomes the selected movement target.
- Reproduction/triage: interaction_design_and_touch_behavior_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0004`
- Triage notes: This is a separately testable interaction refinement to the existing Race Like an Animal content item. No animation, interaction, or asset work was performed during intake.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: RaceAnimalWheel.kt provides a snapping LazyRow, drag/flick selection, accessible selected state, and six reviewed animal choices.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0065 — Show only theme names in the Settings color-theme selector

- Status: fixed-awaiting-retest
- Type: settings / content_copy / visual_design
- Priority: Backlog
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `settings`
- Occurrence count: 1
- Source note(s): `KP-NOTE-1E8C3B39-4BBF-44F8-B0F7-143993ADE636`
- Normalized finding: In the app color-theme selector, display each theme name without a descriptive color sentence or palette explanation.
- Expected behavior: Each theme choice presents its name as the primary visible label; necessary accessible semantics remain available without restoring redundant descriptions.
- Reproduction/triage: settings_copy_and_accessibility_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0017`, `KPF-0066`
- Triage notes: This is separate from the theme-list layout change because visible copy and responsive list behavior require different acceptance checks.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: SettingsScreen.kt renders only AppColorTheme names in the theme selector.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0066 — Use a vertical one-row layout for the Settings theme list

- Status: fixed-awaiting-retest
- Type: settings / responsive_layout / interaction_design
- Priority: Backlog
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `settings`
- Occurrence count: 1
- Source note(s): `KP-NOTE-1E8C3B39-4BBF-44F8-B0F7-143993ADE636`
- Normalized finding: Change the Settings color-theme choices from a horizontally scrolling list to a vertical scrolling list with one theme item per row.
- Expected behavior: Theme choices scroll vertically, each item occupies one row, and narrow-width or large-text states remain readable and accessible.
- Reproduction/triage: settings_layout_and_responsive_accessibility_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0017`, `KPF-0059`
- Triage notes: The requested vertical one-row-per-item behavior is distinct from the previously tracked horizontal timing-section layout.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: Theme choices use the vertical one-row layout; timing options keep responsive narrow/large-text fallbacks.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0067 — Add a reroll action to the Random game flow

- Status: fixed-awaiting-retest
- Type: interaction_design / navigation / randomization
- Priority: Should fix soon
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `home/random_game`
- Occurrence count: 1
- Source note(s): `KP-NOTE-41275DAB-D14F-4523-9149-A40922E95FD3`
- Normalized finding: After Random game opens the selected activity, provide a clear action that picks and opens another random activity without requiring a return to Home.
- Expected behavior: A tester can request another random pick from the shown random-game result, with the new selection replacing or reopening the result in a predictable route.
- Reproduction/triage: random-selection_flow_and_back-stack_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0035`, `KPF-0058`
- Triage notes: This is a new action inside the Random game flow, not a duplicate of the existing Home label or browse-order records.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: QuickPlayScreen includes a Pick another action driven by rerollNonce without leaving the random flow.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0068 — Order the All games and activities Level 1 list by familiarity

- Status: fixed-awaiting-retest
- Type: information_architecture / discovery / content_ordering
- Priority: Should fix soon
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `pick_game`
- Occurrence count: 1
- Source note(s): `KP-NOTE-512BCF4D-3404-4D15-9D7F-82DC650238DB`
- Normalized finding: The All games and activities action should open a Level 1 list ordered from commonly recognized games to the least familiar or most unique; parent entries such as Mad Libs remain intact rather than exposing individual stories at Level 1.
- Expected behavior: The destination has a reviewed, stable familiarity ordering with recognizable formats first and less familiar choices later, without flattening nested story or prompt content.
- Reproduction/triage: product_taxonomy_and_familiarity_ordering_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0040`, `KPF-0041`
- Triage notes: The requested flat familiarity-sorted route may conflict with the existing finite game-type hierarchy. Decide whether All games and activities is an exception route or a category list before implementation.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: All Games uses a stable familiarity order and keeps Mad Libs as one Level 1 parent collection.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0069 — Surface the six activity categories on Home

- Status: fixed-awaiting-retest
- Type: home_layout / information_architecture / navigation
- Priority: Should fix soon
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `home`
- Occurrence count: 2
- Source note(s): `KP-NOTE-7A845776-A132-4FBB-96D4-461CA64C3FA8`; `KP-NOTE-1471739F-9A9F-40CC-93B2-686326B8741E`
- Normalized finding: Expose the six existing activity-theme categories on Home as a primary discoverable group rather than only after opening the All games and activities route.
- Expected behavior: Home provides access to all six reviewed categories without requiring a separate detour, while category identity and existing navigation remain unambiguous.
- Reproduction/triage: home_information_architecture_and_navigation_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0006`, `KPF-0041`, `KPF-0071`, `KPF-0072`
- Triage notes: This shared item records the duplicate category-surface requirement; drawer type and button wording are intentionally not merged.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: Home exposes all six categories through the collapsed shared drawer.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0070 — Arrange Home activity categories in a two-column, three-row grid

- Status: fixed-awaiting-retest
- Type: home_layout / responsive_layout / visual_design
- Priority: Backlog
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `home`
- Occurrence count: 1
- Source note(s): `KP-NOTE-7A845776-A132-4FBB-96D4-461CA64C3FA8`
- Normalized finding: Arrange the six Home activity categories in two columns and three rows when the supported width permits.
- Expected behavior: The six category controls form a readable 2-by-3 arrangement with a responsive fallback that does not clip, overlap, or make categories inaccessible.
- Reproduction/triage: responsive_home_layout_and_accessibility_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0069`
- Triage notes: The requested grid geometry is tracked separately from the category-drawer state and label.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: Category cards use two columns and three rows when width/font scale permit, with a one-column fallback.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0071 — Add an expandable Game categories drawer on Home

- Status: fixed-awaiting-retest
- Type: home_layout / interaction_design / information_architecture
- Priority: Should fix soon
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `home`
- Occurrence count: 1
- Source note(s): `KP-NOTE-7A845776-A132-4FBB-96D4-461CA64C3FA8`
- Normalized finding: Place the six Home categories behind a control labeled Game categories; tapping the control expands or collapses the category drawer.
- Expected behavior: The Game categories control has clear expanded and collapsed states, preserves access to all six categories, and exposes accessible state announcements.
- Reproduction/triage: drawer_state_label_and_accessibility_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0069`, `KPF-0072`
- Triage notes: This preserves Note 5’s requested label and expanding-drawer variant. Note 6 requests a different label and slide-out interaction, so the variants are not silently merged.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: The shared control visibly includes Game categories and announces expanded/collapsed state.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0072 — Add an Activity themes slide-out drawer on Home

- Status: fixed-awaiting-retest
- Type: home_layout / interaction_design / information_architecture
- Priority: Should fix soon
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `home`
- Occurrence count: 1
- Source note(s): `KP-NOTE-1471739F-9A9F-40CC-93B2-686326B8741E`
- Normalized finding: Gather the six Home categories into a collapsible slide-out drawer opened by a control labeled Activity themes.
- Expected behavior: The Activity themes control opens and closes a discoverable drawer containing all six categories, with predictable focus order and accessible expanded-state semantics.
- Reproduction/triage: slide_out_drawer_label_and_accessibility_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0069`, `KPF-0071`
- Triage notes: This preserves Note 6’s requested label and slide-out variant. Product review must select or reconcile it with the Game categories expanding-drawer variant.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: The shared Activity themes control animates a horizontal drawer containing all six categories.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0073 — Place Random game and All games and activities side-by-side on Home

- Status: fixed-awaiting-retest
- Type: home_layout / navigation / responsive_layout
- Priority: Backlog
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `home`
- Occurrence count: 1
- Source note(s): `KP-NOTE-31524398-ADF1-4FED-A7FB-D62C3E5F91EF`
- Normalized finding: Place Random game as the top-left Home action and All games and activities on the same row to its right.
- Expected behavior: The two primary actions share the top action row in the requested left/right order, with a readable responsive fallback at narrow widths and large text.
- Reproduction/triage: home_layout_focus_order_and_responsive_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0058`
- Triage notes: The above-recents portion of the same note reinforces KPF-0058; this item tracks the separately testable same-row left/right arrangement.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: Random game and All games and activities share the first Home row when supported and stack safely otherwise.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0074 — Use single-column full-width game cards

- Status: fixed-awaiting-retest
- Type: layout / responsive_layout / information_architecture
- Priority: Should fix soon
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `game_type/{groupId}`
- Occurrence count: 1
- Source note(s): `KP-NOTE-766272D5-4E46-488C-93D1-B749DE2723CB`
- Normalized finding: Replace the individual two-column game-card layout with one card per row using the widest practical horizontal width to reduce vertical scanning space.
- Expected behavior: Each card spans the available width without clipping or crowding, and the single-column layout remains readable at supported widths and text scales.
- Reproduction/triage: responsive_card_layout_and_product_decision_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0021`, `KPF-0056`
- Triage notes: This explicitly conflicts with the previously tracked compact two-column KPF-0021 layout; it is recorded as a new decision rather than merged evidence.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: CompactCardDetails is a full-width single-column card with a stable semantic/test tag.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0075 — Make game setup and instructions visual-first

- Status: fixed-awaiting-retest
- Type: product_principle / visual_design / content_request / accessibility
- Priority: Should fix soon
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `all_game_details_and_instructions`
- Occurrence count: 1
- Source note(s): `KP-NOTE-C381DF5B-D267-4A5A-9E4C-EA261969A3E9`
- Normalized finding: Represent game instructions, setup, steps, and materials with graphics or pictures wherever practical, using as few words as possible across the content library.
- Expected behavior: A parent can understand and perform each activity through clear visual cues plus only essential text, with accessible text alternatives retained where needed.
- Reproduction/triage: visual_content_system_accessibility_and_localization_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0010`, `KPF-0055`, `KPF-0076`
- Triage notes: This keep-this note establishes a cross-library visual-communication principle. No visual assets were generated and no content was changed during intake.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: VisualInstructionGuide and section cues provide a visual-first shell while retaining accessible text alternatives.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0076 — Use tester-supplied Tiny Monster drawings as visual references

- Status: fixed-awaiting-retest
- Type: visual_asset / content_request / reference_review
- Priority: Backlog
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `detail/timed_drawing_tiny_monster`
- Content ID: `timed_drawing_tiny_monster`
- Occurrence count: 1
- Source note(s): `KP-NOTE-8041A19A-A852-401D-AA51-B562401DC1B3`
- Normalized finding: Review the three tester-supplied PNG references as inspiration for an original Tiny Monster instructional and art direction, while preserving the source references unchanged.
- Expected behavior: A future visual-design pass produces an original, coherent Tiny Monster visual system informed by the approved references without silently copying or mutating the supplied source files.
- Reproduction/triage: visual_reference_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Attachments: 3 confirmed `image/png` reference file(s); binaries not ingested during intake.
- Related items: `KPF-0075`
- Triage notes: The private raw archive records three tester-confirmed PNG references. The binaries were not opened, copied into the repository, uploaded, or used for generation during passive intake.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: An original Gemini-generated Tiny Monster portrait guide is integrated; the three supplied references remain unchanged and outside the app binary.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## KPF-0077 — Hide nonessential timer, round, and session controls on instruction details pages

- Status: fixed-awaiting-retest
- Type: content_visibility / session_configuration / usability
- Priority: Should fix soon
- Affected build: 0.6.0-beta3 (8)
- Capture screen: `detail/instructions/*`
- Example content ID: `paper_airplane_weather`
- Occurrence count: 1
- Source note(s): `KP-NOTE-3578A01C-6F57-44E0-A021-4093A3128150`
- Normalized finding: On individual game instruction details pages, omit timer, round-count, and session-count sections unless essential; when essential, keep the section collapsed by default.
- Expected behavior: Most instruction pages contain no unnecessary session controls, while an activity that truly requires them presents the required section collapsed and still discoverable.
- Reproduction/triage: content_inventory_and_play_critical_exception_review_required
- Decision: accepted_and_implemented_for_wip_0.6.3
- Implementation status: complete; awaiting physical retest
- Latest implementation build: 0.6.3 (10)
- Code authorization: Project-owner standing completion goal for the named batch
- Related items: `KPF-0030`, `KPF-0055`
- Triage notes: This is separate from age/duration/activity metadata removal: it governs session controls and requires an item-by-item decision about what is essential to play correctly.
- Batch KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C implementation evidence: Detail session controls are limited to activities whose play definition makes timing central; Paper Airplanes is not given unnecessary controls.
- Automated verification: unit tests, Android-test source compilation, lint, assemble, JSON/schema/parity checks, APK metadata/signature/permission checks, and git diff --check passed; physical Android retest remains open.

## Implementation release record: KP-BATCH-57A91D51-5A25-4813-BA3A-7B23A915135C

- Implementation build: **0.6.3 (10)**.
- Coverage: all 19 canonical items are marked fixed-awaiting-retest with 0.6.3 implementation evidence above.
- Product decisions: one shared collapsed `Activity themes` / `Game categories` drawer; familiarity-ordered Level 1 All Games with Mad Libs preserved as one collection; single-column full-width cards; session controls retained only when timing is central to play.
- APK: `app/build/outputs/apk/debug/app-debug.apk`, 22,410,024 bytes, SHA-256 `f18d6e93a4c84f5876c4591254e0d31f2282ef0b1cbd50f7fb1325a829037205`.
- APK verification: package `com.kinplay.app`, version `0.6.3`, code `10`, target SDK 35, APK Signature Scheme v2 verified, only AndroidX dynamic receiver permission present.
- Content verification: canonical/runtime JSON parity, schema, unique IDs, APK JSON parity, and generated Tiny Monster resource parity passed.
- Physical retest: open because `adb` is unavailable and no Android target is connected; no device result is claimed.
- Source publication: commit `f100058f430cef43ae5a4ecb2481e69dd5bac510` is pushed to `origin/main` and remote SHA matches.
- Local distribution: `/mnt/cyberforgex-torrents/KinPlay/apk-drops/20260806_KinPlay_v0.6.3_MVP.apk` is the sole current root APK; older beta2/beta3 APKs are archived.
- Google Drive distribution: folder `12bINCtZHQwvh3-mIbQ2x-swPE6ACzjYp` contains exactly one active APK, object `1MO4JEMKc4_yxe2D0s1r9t3DLgs36HdXj`; download read-back checksum and size match.

## Intake: KP-BATCH-EC3C89DA-8759-4636-9BB8-46E904E18D9A

- Received: 2026-08-14T22:35:48Z
- Transport: direct project-owner Discord message; no email subject was involved.
- Affected build: **0.7.0 (13)**.
- Source note: `KP-NOTE-36ABF7C6-8D34-4D79-89C8-98CC7AFE9147`
- New canonical item: `KPF-0078`
- Privacy: No child-identifying information, contact details, images, birthdates, or precise locations were written to project records.
- Code authorization: Intake and triage only; no application code or runtime content change was authorized.
- Raw archive: `~/.hermes/kinplay-feedback/raw/20260814_KP-BATCH-EC3C89DA-8759-4636-9BB8-46E904E18D9A.json`
- Prompt provenance manifest: `docs/testing/feedback/WOULD_YOU_RATHER_OWNER_REPLACEMENTS.json`

## KPF-0078 — Stage five owner-supplied Would You Rather prompts for protected replacement

- Status: new
- Type: Content request / content provenance / content safety
- Priority: Backlog
- Affected build: 0.7.0 (13)
- Capture screen: `would_you_rather_play`
- Occurrence count: 2
- Additional source note: `KP-NOTE-AE3674BD-E47E-4DC4-8F14-60D4B51602EE`
- Source note: `KP-NOTE-36ABF7C6-8D34-4D79-89C8-98CC7AFE9147`
- Related item: `KPF-0012`
- Normalized finding: For the next authorized Would You Rather content revision, stage the five exact prompts supplied by the project owner and use them as replacement candidates for assistant-created prompts only. Keep owner-supplied prompts separately identified from prompts authored by the assistant.
- Expected behavior: The next authorized revision preserves all five owner-supplied prompts in the provenance record, performs the required child-safety review before any runtime release, and removes or replaces only prompts classified as assistant-created if the library count must be reduced.
- Owner-supplied prompts, preserved verbatim:
  1. `would you rather sweat all the time or slobber all the time?`
  2. `would you rather eat mac and cheese with beetles or soup with worms?`
  3. `would you rather have ants crawling on you or tiny lizards?`
  4. `would you rather eat a cockroach or a worm?`
  5. `would you rather your bedroom smell like fish or dog poop?`
- Provenance: Owner-supplied prompts use `project_owner_supplied_message_2026-08-14`; existing assistant-created prompts use `original_kinplay_editorial_work` in the Would You Rather review registry.
- Replacement/deletion rule: Only assistant-created prompts may be selected for replacement or removal. Owner-supplied prompts are protected from deletion or count reduction.
- Category assignment: Pending content and safety review; no category was inferred during intake.
- Safety triage: The ingestion prompts require explicit child-safety review against the existing hazardous-ingestion boundary before any runtime release.
- Prompt provenance manifest: `docs/testing/feedback/WOULD_YOU_RATHER_OWNER_REPLACEMENTS.json`
- Reproduction/triage: Content provenance and safety review required.
- Decision: Accepted for the next feedback batch; no code or runtime content change authorized.
- Implementation status: not started
- Latest intake batch: `KP-BATCH-7423FBCE-A279-49D5-B9E5-B0DF21D74F0E` (0.7.0-beta2 (12))
- Intake disposition: accepted_for_next_authorized_content_revision
- New intake observation: The 0.7.0-beta2 intake adds a full-library rewrite/reduction request and exact reference examples; the provenance manifest preserves them separately and runtime content remains unchanged.


## Intake: KP-BATCH-7423FBCE-A279-49D5-B9E5-B0DF21D74F0E

- Received: 2026-08-17T20:13:08Z
- Transport: email; subject and body identifiers matched.
- Affected build: **0.7.0-beta2 (12)**.
- Source notes: 9 new unique note IDs; no replayed IDs.
- Existing canonical items touched: `KPF-0022`, `KPF-0043`, `KPF-0060`, `KPF-0078`.
- New canonical items: `KPF-0079`–`KPF-0088`.
- Privacy: No child-identifying information or contact details were written to project records. One listed JPEG remains unprocessed attachment metadata only.
- Attachments: One `image/jpeg` reference was listed but its binary was not available to intake; no attachment, DM, upload, or asset-generation action occurred.
- Code authorization: Intake and triage only; no application code, runtime content, visual asset, attachment, or build changed.
- Discord acknowledgment: delivered successfully to `#app-development` (channel `1501041077031800932`), message `1539006561010188392`.
- Provenance: The Would You Rather manifest preserves the exact owner-supplied reference examples separately from assistant-created runtime content.
- Raw archive: `/home/phantomatic/.hermes/kinplay-feedback/raw/20260817_KP-BATCH-7423FBCE-A279-49D5-B9E5-B0DF21D74F0E.json`

| Item | Triage state | Next validation |
|---|---|---|
| KPF-0022 | existing; future copy revision requested | Confirm the exact I Spy description on every relevant card/state without changing other concise-card rules. |
| KPF-0043 | existing; needs reproduction | Reproduce selected-image handoff through the feedback interface and verify both Discord and email attachment paths. |
| KPF-0060 | existing; future theme revision requested | Decide the sixth theme and verify palette contrast, persistence, and application behavior. |
| KPF-0078 | existing; provenance extended | Verify prior and newly supplied owner examples remain protected and runtime content remains unchanged until authorization. |
| KPF-0079 | new | Decide retirement scope and verify discovery, search, random, favorites, recents, and direct-route filtering. |
| KPF-0080 | new | Define consolidation rules, parent formats, variations/modes, and migration behavior. |
| KPF-0081 | new | Resolve conflict with KPF-0020 and verify a launchable default/legacy-preference path. |
| KPF-0082 | new | Verify the three-column/two-row theme layout with narrow-width, large-text, and accessibility fallbacks. |
| KPF-0083 | new | Define the Charades card-to-picture mapping, visual QA, packaging, and accessible text alternative. |
| KPF-0084 | new | Obtain the reference binary through the approved attachment path, then create a Gemini visual brief and review gate. |
| KPF-0085 | new | Inventory every game-card state and confirm the Prepare Play Share graphic is absent without removing needed semantics. |
| KPF-0086 | new | Review the complete Would You Rather library with the Humanizer gate; do not alter runtime content during triage. |
| KPF-0087 | new | Reconcile 40-per-category with KPF-0012 and validate provenance-aware removal eligibility. |
| KPF-0088 | new | Record an item-by-item Gross/Super Gross safety decision before any age or tone change. |
## KPF-0079 — Retire Washable Coloring Together from the activity library

- Status: new
- Type: content_request / content_retirement
- Priority: Should Fix Soon
- Impact: important
- Affected build: 0.7.0-beta2 (12)
- Capture screen: `detail/washable_coloring_together`
- Content ID: `washable_coloring_together`
- Occurrence count: 1
- Source note: `KP-NOTE-CBBDD903-4856-46A2-9577-A355B36B45CC`
- Normalized finding: Remove the Washable Coloring Together activity from normal KinPlay discovery and playable content.
- Expected behavior: The activity no longer appears in Home, category lists, search, random selection, favorites/recent restoration, or direct playable routes; retired references fail safely.
- Reproduction/triage: content_inventory_and_retirement_regression_review_required
- Decision: accepted_for_triage_product_decision_required
- Implementation status: not_started
- Code authorization: none; passive intake and triage only
- Related items: `KPF-0010`, `KPF-0040`
- Triage notes: The content ID is present in the current runtime asset. No content was removed during intake.

## KPF-0080 — Consolidate similar games under generic variation-based formats

- Status: new
- Type: information_architecture / content_model / product_scope
- Priority: Should Fix Soon
- Impact: important
- Affected build: 0.7.0-beta2 (12)
- Capture screen: `pick_game`
- Occurrence count: 1
- Source note: `KP-NOTE-CE62831B-A798-43B3-9F83-64534175B8DC`
- Normalized finding: Review similar games for consolidation into generic parent formats with variations, modes, or styles instead of presenting many near-duplicate entries.
- Expected behavior: The product taxonomy defines which activities share a parent format, preserves recognizable names where needed, and avoids duplicate or confusing discovery entries.
- Reproduction/triage: product_taxonomy_and_content_model_review_required
- Decision: accepted_for_triage_product_decision_required
- Implementation status: not_started
- Code authorization: none; passive intake and triage only
- Related items: `KPF-0040`, `KPF-0041`, `KPF-0068`
- Triage notes: This is related to the existing hierarchy and finite-category work, but it requires a separate consolidation decision rather than an automatic merge.

## KPF-0081 — Remove launcher icon selection from Settings

- Status: new
- Type: settings / launcher_customization / product_decision
- Priority: Backlog
- Impact: minor
- Affected build: 0.7.0-beta2 (12)
- Capture screen: `settings`
- Occurrence count: 1
- Source note: `KP-NOTE-E1625FC7-E151-460C-94AA-9CAB948BD34B`
- Normalized finding: Remove the user-facing launcher icon selection option from Settings.
- Expected behavior: Settings no longer exposes launcher selection while the app retains one launchable default, stable package identity, upgrade continuity, and safe handling of any persisted legacy preference.
- Reproduction/triage: settings_and_launcher_product_decision_review_required
- Decision: accepted_for_triage_conflicts_with_existing_launcher_choice
- Implementation status: not_started
- Code authorization: none; passive intake and triage only
- Related items: `KPF-0017`, `KPF-0019`, `KPF-0020`
- Triage notes: This conflicts with KPF-0020, which requested and implemented finite launcher-color selection. It remains separate until the product owner chooses the direction.

## KPF-0082 — Arrange Settings theme choices in a three-column grid

- Status: new
- Type: settings / responsive_layout / visual_design
- Priority: Backlog
- Impact: minor
- Affected build: 0.7.0-beta2 (12)
- Capture screen: `settings`
- Occurrence count: 1
- Source note: `KP-NOTE-72DE95BF-2A63-4A14-98C2-99A2D84B58FD`
- Normalized finding: Replace the current theme-choice presentation with a section arranged as three columns and two color choices per column, while adding the requested sixth theme.
- Expected behavior: The six theme choices form a readable three-column by two-row layout where supported, with a safe responsive fallback for narrow widths and large text; names, selection state, persistence, and accessibility remain clear.
- Reproduction/triage: settings_theme_grid_and_accessibility_review_required
- Decision: accepted_for_triage_layout_product_decision_required
- Implementation status: not_started
- Code authorization: none; passive intake and triage only
- Related items: `KPF-0017`, `KPF-0060`, `KPF-0066`
- Triage notes: The additional theme is tracked as supporting evidence on KPF-0060; this item tracks the separately testable grid geometry and responsive behavior.

## KPF-0083 — Pair every Charades word with a generated picture

- Status: new
- Type: content_request / visual_asset / accessibility
- Priority: Backlog
- Impact: minor
- Affected build: 0.7.0-beta2 (12)
- Capture screen: `detail/family_charades_animals`
- Content ID: `family_charades_animals`
- Occurrence count: 1
- Source note: `KP-NOTE-5B0F24C7-C38C-4FB0-BC41-A6991D5DA5A6`
- Normalized finding: Pair each Charades card across the animals, activities, and objects groups with an original generated picture.
- Expected behavior: Every active Charades word has one deterministic reviewed picture mapping, consistent offline packaging, a usable accessible text alternative, and no unreviewed or identifying source material.
- Reproduction/triage: content_inventory_visual_generation_and_accessibility_review_required
- Decision: accepted_for_triage_visual_asset_review_required
- Implementation status: not_started
- Code authorization: none; passive intake and triage only
- Related items: `KPF-0046`, `KPF-0075`
- Attachments: 1 confirmed `image/jpeg` reference listed; binary not ingested.
- Triage notes: No visual asset was generated or integrated during intake. Related card-count work remains in KPF-0046.

## KPF-0084 — Use the tester drawing as Charades card-art direction

- Status: new
- Type: visual_asset / reference_review / content_request
- Priority: Backlog
- Impact: minor
- Affected build: 0.7.0-beta2 (12)
- Capture screen: `detail/family_charades_animals`
- Content ID: `family_charades_animals`
- Occurrence count: 1
- Source note: `KP-NOTE-5B0F24C7-C38C-4FB0-BC41-A6991D5DA5A6`
- Normalized finding: Use the tester-supplied hand-drawn card as a reference for a more attractive, cartoony Charades picture style without copying the source image.
- Expected behavior: A visual brief and approved Gemini Nano Banana masters define a coherent Charades card style; the supplied reference remains unchanged and is not silently copied, uploaded, or mutated.
- Reproduction/triage: attachment_availability_visual_brief_and_gemini_review_required
- Decision: accepted_for_triage_visual_asset_review_required
- Implementation status: not_started
- Code authorization: none; passive intake and triage only
- Related items: `KPF-0075`, `KPF-0076`
- Attachments: 1 confirmed `image/jpeg` reference listed; binary not ingested.
- Triage notes: The listed JPEG was not present in the received email context. No DM was sent and no image-generation or upload action occurred during passive intake.

## KPF-0085 — Remove the Prepare Play Share graphic from game cards

- Status: new
- Type: visual_design / card_layout / content_visibility
- Priority: Backlog
- Impact: minor
- Affected build: 0.7.0-beta2 (12)
- Capture screen: `detail/quiet_color_hunt`
- Content ID: `quiet_color_hunt`
- Occurrence count: 1
- Source note: `KP-NOTE-40B0AE3C-50C3-473B-9A41-70A60F2C9131`
- Normalized finding: Remove the Prepare Play Share information graphic from every game card.
- Expected behavior: No game card renders the graphic in collapsed, expanded, or related card states; titles, descriptions, actions, accessibility semantics, and necessary safety/instruction content remain intact.
- Reproduction/triage: cross_surface_card_inventory_and_visual_regression_review_required
- Decision: accepted_for_triage_no_code_authorized
- Implementation status: not_started
- Code authorization: none; passive intake and triage only
- Related items: `KPF-0055`, `KPF-0056`, `KPF-0075`
- Triage notes: This is recorded separately from broader collapsed-card minimalism because it targets a specific visual asset across every card state.

## KPF-0086 — Rewrite Would You Rather prompts in a concise humanized style

- Status: new
- Type: content_copy / content_safety / humanizer_review
- Priority: Should Fix Soon
- Impact: minor
- Affected build: 0.7.0-beta2 (12)
- Capture screen: `pick_game`
- Occurrence count: 1
- Source note: `KP-NOTE-AE3674BD-E47E-4DC4-8F14-60D4B51602EE`
- Normalized finding: Rewrite all Would You Rather prompts with shorter, more natural wording, removing unnecessary qualifiers and filler while preserving the intended choice structure.
- Expected behavior: Each prompt remains readable aloud, grammatically clear, distinct, and appropriate to its category; exact owner-supplied examples remain provenance-protected and runtime content stays unchanged until separately authorized.
- Reproduction/triage: full_library_copy_review_and_humanizer_gate_required
- Decision: accepted_for_next_authorized_content_revision
- Implementation status: not_started
- Code authorization: none; passive intake and triage only
- Related items: `KPF-0012`, `KPF-0078`
- Triage notes: The request to use a writing skill is recorded as a future review gate, not executed during intake. Exact examples are preserved in the owner-supplied provenance manifest as reference-only entries.

## KPF-0087 — Reduce Would You Rather to 40 prompts per category

- Status: new
- Type: content_request / content_provenance / content_library
- Priority: Should Fix Soon
- Impact: minor
- Affected build: 0.7.0-beta2 (12)
- Capture screen: `pick_game`
- Occurrence count: 1
- Source note: `KP-NOTE-AE3674BD-E47E-4DC4-8F14-60D4B51602EE`
- Normalized finding: Reduce each Would You Rather category to exactly 40 prompts.
- Expected behavior: The four category libraries contain 40 reviewed prompts each after reduction; only assistant-created entries may be removed, while project-owner-supplied entries remain present and separately identified.
- Reproduction/triage: provenance_aware_library_reduction_and_count_validation_required
- Decision: accepted_for_triage_provenance_and_safety_review_required
- Implementation status: not_started
- Code authorization: none; passive intake and triage only
- Related items: `KPF-0012`, `KPF-0078`
- Triage notes: This conflicts with the existing 80-per-category target in KPF-0012 and therefore remains a new product decision rather than a silent merge.

## KPF-0088 — Review the Gross and Super Gross age and safety boundary

- Status: new
- Type: content_safety / content_policy / product_decision
- Priority: Should Fix Soon
- Impact: minor
- Affected build: 0.7.0-beta2 (12)
- Capture screen: `pick_game`
- Occurrence count: 1
- Source note: `KP-NOTE-AE3674BD-E47E-4DC4-8F14-60D4B51602EE`
- Normalized finding: Evaluate whether Gross and Super Gross Would You Rather categories may target age 10+ with edgier but noncrass humor, rather than the current very-tame baseline.
- Expected behavior: A recorded item-by-item safety decision defines age targeting, prohibited bodily content, ingestion references, and acceptable gross-humor limits before any runtime content is rewritten or released.
- Reproduction/triage: content_safety_policy_and_library_review_required
- Decision: accepted_for_triage_safety_review_required
- Implementation status: not_started
- Code authorization: none; passive intake and triage only
- Related items: `KPF-0003`, `KPF-0012`, `KPF-0078`
- Triage notes: The note contains a boundary tension between excluding crass material and using more provocative examples. Preserve the request and require explicit safety review; do not infer approval from the examples.

## Intake batch KP-BATCH-DDE149EC-5EFC-4DE2-89B0-B0592C73E6F4

- Build: 0.7.0-beta2 (12)
- Source notes: 1 unique; 0 replayed.
- Canonical items touched: 3 total; 2 existing; 1 new.
- Privacy: no child-identifying information written; sender sign-off and contact details omitted.
- Attachment handling: no attachments listed or ingested.
- Code authorization: intake and triage only; no application code, runtime content, UI behavior, asset, or build change.
- Discord acknowledgment: delivered successfully to `#app-development` (channel `1501041077031800932`), message `1539009090318110901`.
- Mapping: KP-NOTE-78EE3B8B-81D2-4FA3-B66E-72C7308ACE51 → KPF-0022, KPF-0063, KPF-0089.
- Triage split: concise description and favorite preservation were linked to existing records; the Level 1 non-expandable/title-tap interaction was recorded as a new item because it changes the earlier card interaction scope.

## KPF-0089 — Make Level 1 cards non-expandable with title-tap details navigation

- Type: usability, navigation, content copy
- Priority: should_fix_soon
- Impact: minor
- Screen: pick_game
- Source note: KP-NOTE-78EE3B8B-81D2-4FA3-B66E-72C7308ACE51
- Occurrence count: 1
- Finding: At Level 1, remove card expand/collapse behavior and the Open button. Show only the game name/title and one-line description; tapping the game name opens the game details card; preserve star favorite behavior.
- Expected behavior: Level 1 cards expose no age, duration, or participant-suitability metadata and have no expand/collapse affordance. The title/name is the details navigation target, while the favorite star remains an independent accessible control with persisted state.
- Status: New; awaiting triage and implementation authorization.
- Implementation status: Not started.
- Scope note: Level 1 only; do not infer the same interaction for Level 0, Level 2, or details pages.

## Implementation closure: KP-BATCH-DDE149EC-5EFC-4DE2-89B0-B0592C73E6F4 + KP-BATCH-7423FBCE-A279-49D5-B9E5-B0DF21D74F0E

- Closure record: `docs/testing/feedback/20260817_IMPLEMENTATION_RELEASE.json`
- Implementation build: **0.7.0 (13)**; package `com.kinplay.app`.
- The intake-only entries above are historical. This closure record supersedes their earlier `new`/`not_started` triage state for the authorized implementation pass.
- Covered canonical items: `KPF-0022`, `KPF-0043`, `KPF-0060`, `KPF-0063`, `KPF-0078`–`KPF-0089`.
- Automated gates: JVM unit tests, lint, release assembly, offline checkpoint, JSON/schema/parity checks, APK metadata/permission inspection, APK Signature Scheme v2 verification, and `git diff --check` passed.
- APK: `/mnt/cyberforgex-torrents/KinPlay/apk-drops/20260817_KinPlay_v0.7.0_feedback-complete.apk`; size `25,949,602` bytes; SHA-256 `e9f32ac03eb8137140c57a4a6c6dcfc09166f4a9c891a0004762c0662fc499b2`.
- Drive: object `19Z7vDHR64_dbvs8pHDQJqMT_GypKV6yr` in folder `12bINCtZHQwvh3-mIbQ2x-swPE6ACzjYp`; downloaded read-back is byte-identical.
- Charades: all 120 cards have reviewed resource metadata and prompt-specific accessible alt text; three Gemini-generated category masters are packaged and parity-checked. Per-card presentation still requires connected runtime visual retest.
- Would You Rather: 40 prompts per category; the 20 protected runtime owner prompts remain present and separately identified; the five newer staged examples remain in the provenance manifest and were not silently promoted.
- Release boundary: this is a private validation APK. No production release keystore was found, and no connected Android device/emulator retest is claimed.
- Source publication: implementation commit `ef6ed79b159710241098a280c7fe596b5ba9123f` is pushed to `origin/main`; this closure appendix is finalized in the following publication-evidence commit.

## Implementation cycle — KP-BATCH-8657ED79-DC52-4E8C-8166-4A94FA7AC4C6 + KP-BATCH-AB828E53-A85C-42B7-AD12-CC787FC292E1

- Implementation build: **0.2.3 (4)**; package `com.devlab`.
- Named source notes implemented: `KP-NOTE-93D57E58-0BCF-4228-A9B5-54082BA74718` and `KP-NOTE-42204546-D5E4-4F2C-A9E0-C6591D5DDC11`.
- Separate source note `KP-NOTE-451376C4-0F0B-410F-A6AD-1424990FE6E8` is preserved as `KPF-0092` and remains deferred; it was not executed.
- Canonical items implemented: `KPF-0090` and `KPF-0091`.
- Automated evidence: full JVM tests, Android-test source compilation, connected Android tests (3/3), lint, and debug APK assembly passed.
- APK: `/mnt/cyberforgex-torrents/KinPlay/apk-drops/20260823_DevLab_v0.2.3.apk`; compatibility copy `/mnt/cyberforgex-torrents/DevLab/DevLab_v0.2.3.apk`; size `10,261,603` bytes; SHA-256 `fdcd1ad2ce047b9c5bdafe32f16330f038bf4d4c06c7301cd62d6b0e9f52b537`; read-back comparison passed.
- Source commit: `6fff3ae7910e7d05529fb09a675aa4ae51d95728` pushed to `origin/main`; remote SHA matched.
- Package evidence: `com.devlab`, version `0.2.3`, version code `4`; APK Signature Scheme v2 verified.
- Runtime boundary: connected API 35 Pixel 7 AVD tests passed in gestural navigation mode. Pixel 8 Pro / Android 16 three-button navigation retest remains open; the app cannot select the device's navigation mode.
- Current implementation acknowledgment is delivered in this `#app-development` response; no separate Discord API action was performed.

## KPF-0090 — Keep Android system navigation buttons visible in Dev Lab

- Status: Fixed; awaiting Pixel 8 Pro retest.
- Type: platform_behavior / navigation / accessibility
- Priority: should_fix_soon
- Impact: minor
- Affected builds: 0.2.2 (3), 0.2.3 (4)
- Capture screen: `dev_lab/animals`
- Content ID: `animals`
- Occurrence count: 1
- Source note: `KP-NOTE-42204546-D5E4-4F2C-A9E0-C6591D5DDC11`
- Normalized finding: Keep Android's system navigation bar visible when Dev Lab launches so the triangle, circle, and square navigation controls remain available.
- Expected behavior: Dev Lab clears fullscreen state, restores default system-bar behavior, explicitly shows system bars, and keeps content clear of the navigation-bar area. The exact triangle/circle/square presentation remains controlled by the device's navigation mode.
- Implementation status: Complete pending Pixel 8 Pro / Android 16 three-button retest.
- Evidence: `DevLabWindowContractTest`, `DevLabSystemBarsTest`, `DevLabScreenTest`, full JVM suite, connected Android suite, lint, and APK inspection passed.
- Related item: `KPF-0053`.

## KPF-0091 — Keep only Animal moves in Dev Lab

- Status: Fixed; awaiting physical visual retest.
- Type: dev_lab / content_visibility / product_test
- Priority: should_fix_soon
- Impact: minor
- Affected builds: 0.2.2 (3), 0.2.3 (4)
- Capture screen: `dev_lab/long_labels`
- Content ID: `long_labels`
- Occurrence count: 1
- Source note: `KP-NOTE-93D57E58-0BCF-4228-A9B5-54082BA74718`
- Normalized finding: Remove the Color choices and Long labels demos from Dev Lab while retaining Animal moves.
- Expected behavior: Dev Lab exposes only Animal moves, including its reel, Spin control, current choice, and detail; removed demo IDs are neither rendered nor selectable.
- Implementation status: Complete pending physical visual retest.
- Evidence: `DevLabContractTest`, `DevLabScreenTest`, `DevLabAboutTest`, full JVM suite, connected Android suite, lint, and APK inspection passed.

## KPF-0092 — Add directional gestures to the Animal moves tumbler

- Status: New; deferred.
- Type: dev_lab / interaction / gesture
- Priority: backlog
- Impact: minor
- Affected build: 0.2.2 (3)
- Capture screen: `dev_lab/animals`
- Content ID: `animals`
- Occurrence count: 1
- Source note: `KP-NOTE-451376C4-0F0B-410F-A6AD-1424990FE6E8`
- Normalized finding: Add swipe-up, swipe-down, and tap interactions to drive the Animal moves tumbler in the matching direction.
- Expected behavior: Swipes within the animal-name graphic move the tumbler in the matching direction, and a single tap spins it.
- Implementation status: Not started; this note was not named in the current implementation request.
- Code authorization: None for this cycle.
