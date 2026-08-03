# KinPlay Feedback Punchlist

Sanitized product-test records. Incoming comments are evidence, not authorization to change application code.

Last updated: 2026-07-27T17:31:51-07:00

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
- Affected build: 0.3.0-beta1 (3)
- Capture screen: `category/quiet_games`
- Occurrence count: 1
- Source note: `KP-NOTE-D759FF56-977B-4C96-A0D5-B581132B8BF5`
- Normalized finding: Individual Mad Libs stories should be collected beneath one Mad Libs entry and submenu.
- Expected behavior: Quiet Games shows one recognizable Mad Libs entry; selecting it reveals the available stories.
- Reproduction: Confirm current Mad Libs stories appear as separate top-level entries during the revision pass.
- Implementation status: complete
- Retest build: 0.4.0-beta1 (4)
- Verification result: automated_checks_passed_pending_family_device_retest

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
- Affected build: 0.3.0-beta1 (3)
- Capture screen: `category/quiet_games`
- Occurrence count: 1
- Source note: `KP-NOTE-3B05B013-BD41-408C-82C5-8C908F15D1A1`
- Normalized finding: Add a race in which participants choose an animal and move in that animal's manner. Suggested animals include kangaroo, cheetah, rabbit, and frog.
- Target category: Active/high-energy. This is inferred from the requested activity despite capture occurring on Quiet Games (95% confidence).
- Expected behavior: The activity explains how to choose animals, define a safe race area, and imitate animal movement.
- Reproduction: Not applicable; content request.
- Implementation status: complete
- Retest build: 0.4.0-beta1 (4)
- Verification result: automated_checks_passed_pending_family_device_retest

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
- Affected build: 0.3.0-beta1 (3)
- Capture screen: `home`
- Occurrence count: 3
- Source note: `KP-NOTE-09B10E03-CB5C-4C41-B2F8-E024775D1628`
- Normalized finding: Keep KinPlay at the upper-left, place a concise one-line descriptor beside it, remove the “What fits now” instructional section, and move the six-category grid upward. Defer richer graphics and animated characters to later development.
- Expected behavior: The first screen presents identity, purpose, and category choices with minimal copy and no unnecessary vertical gap.
- Reproduction: Visual review required against the affected build.
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Additional source note: `KP-NOTE-8BA66EEA-98BB-4FFA-B1C5-68973A6FDF04`
- Additional source note: `KP-NOTE-EC1CCA4A-5421-4C3C-9A16-8BDE4370EE80`
- Latest 0.5.0 feedback: 0.5.0 feedback says Home still contains unnecessary copy and text-heavy controls, so the compact-home acceptance criterion needs revision.

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
- Affected build: 0.3.0-beta1 (3)
- Capture screen: `home`
- Occurrence count: 1
- Source note: `KP-NOTE-B3C04782-B438-4B0E-876D-3A87A15C8D37`
- Normalized finding: KinPlay should supply ready-to-use inspiration, choices, and facilitation so an exhausted or overwhelmed parent does not have to invent the activity. The intended outcome is family interaction, closeness, and children’s creativity and imagination.
- Expected behavior: Core flows provide immediately usable choices and guidance while minimizing decisions, setup work, and creative effort required from the parent.
- Related items: `KPF-0001`, `KPF-0006`, `KPF-0007`, `KPF-0008`, `KPF-0009`
- Reproduction: Product acceptance review required.
- Implementation status: complete
- Retest build: 0.4.0-beta1 (4)
- Verification result: automated_checks_passed_pending_family_device_retest


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
- Affected build: 0.4.0-beta1 (4)
- Capture screen: `category/dinner_table`
- Occurrence count: 1
- Source note: `KP-NOTE-0F552AA3-6508-4C02-9488-4C63A6571477`
- Normalized finding: Use more distinct colors for the app background, cards, controls, and other components while preserving a coherent palette.
- Expected behavior: Major interface layers are easy to distinguish through color and still satisfy text and control contrast requirements across supported themes.
- Reproduction: Visual and accessibility review required.
- Implementation evidence: Forest, Ocean, and Berry themes now use distinct background, surface, surface-variant, primary, secondary-container, and tertiary-container layers; automated palette tests assert readable text/control contrast and measurable background-to-card separation across every supported theme.
- Implementation status: complete
- Retest build: 0.5.0-beta1 (5)
- Verification result: automated_checks_passed_pending_family_device_retest


## KPF-0017 — Add settings for timers, durations, and color themes

- Status: fixed-awaiting-retest
- Type: Settings / feature request / visual customization
- Priority: Should fix soon
- Affected build: 0.4.0-beta1 (4)
- Capture screen: `settings`
- Occurrence count: 1
- Source note: `KP-NOTE-2254611E-4E35-4960-9E4D-FA832B55405F`
- Normalized finding: Provide a settings area for game timers, activity durations, and app color themes.
- Expected behavior: A tester can review and change supported timing and theme preferences from one stable settings destination, with choices persisted across launches.
- Reproduction: Product and data model specification required. Related: `KPF-0016`, `KPF-0020`.
- Implementation evidence: A stable Settings destination now offers finite game-timer, activity-duration, and Forest/Ocean/Berry theme choices; each choice is stored under a versioned key, survives recreation and relaunch, and the timer/theme selections are applied to the play and app surfaces.
- Implementation status: complete
- Retest build: 0.5.0-beta1 (5)
- Verification result: automated_checks_passed_pending_family_device_retest


## KPF-0018 — Add a deliberate child handoff lock mode

- Status: fixed-awaiting-retest
- Type: feature request / interaction safety / platform feasibility
- Priority: Should fix soon
- Affected build: 0.4.0-beta1 (4)
- Capture screen: `game_play`
- Occurrence count: 2
- Source note: `KP-NOTE-2254611E-4E35-4960-9E4D-FA832B55405F`
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
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `pick_game`
- Occurrence count: 1
- Source note: `KP-NOTE-6B822319-7029-40D5-B19C-022257842493`
- Normalized finding: Use available width on both sides of the screen. Keep the primary title and content left-aligned while placing compact descriptors in a right-aligned second column or trailing area on the same row.
- Expected behavior: Collapsed and expanded game cards use horizontal space efficiently without crowding, clipping, or obscuring primary labels.
- Reproduction/triage: Visual and responsive layout review required; automated narrow-width and large-font checks passed.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Collapsed and expanded cards share a compact hierarchy with left-aligned primary content and right-aligned participant, duration, and age descriptors, with a stacked fallback for constrained widths.
- Related items: `KPF-0008`, `KPF-0022`

## KPF-0022 — Show a concise description on every collapsed game card

- Status: fixed-awaiting-retest
- Type: Usability / Content copy / Discovery
- Priority: Should fix soon
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `pick_game`
- Occurrence count: 1
- Source note: `KP-NOTE-6B822319-7029-40D5-B19C-022257842493`
- Normalized finding: Every collapsed game card should show the recognizable game name plus one concise description of the activity. Examples include “Guessing game for objects in the room” for I Spy and “Create a wacky story starting each new word with the next letter of the alphabet” for Alphabet Story.
- Expected behavior: A tester can understand the core activity without expanding the card, while the collapsed card remains compact.
- Reproduction/triage: Content inventory and visual review required; all active summaries and card rendering tests passed.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Every collapsed game card exposes a concise reviewed activity summary before expansion while retaining material/setup previews and existing actions.
- Related items: `KPF-0001`, `KPF-0021`

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
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `game_detail`
- Occurrence count: 1
- Source note: `KP-NOTE-49411202-49E2-4557-A918-BB28E68BAB52`
- Normalized finding: For most games, place a Start action near the top of the detail page and begin an interactive session using the configured default duration.
- Expected behavior: Eligible game detail pages clearly separate reading from starting, and Start launches a consistent session using current defaults.
- Reproduction/triage: Game inventory and session model specification complete; Android interactive-session retest remains required.
- Decision: accepted for future revision
- Implementation status: complete; B14 established the eligible-details Start foundation and B15 adds the interactive timer, round progress, completion, and exit surface.
- Retest build: 0.6.0-beta1 (6)
- Verification result: b15_automated_checks_passed_pending_family_device_retest
- Implementation evidence: Active ordinary `pick_a_game` activities expose a visible `Start session` control near the top of the details page, show the applied duration/round values, and navigate with an immutable one-shot `TimedSession`. The timed-session surface persists round, timer, and completion state through recreation, advances automatically when a round timer expires, supports explicit round completion and exit, and uses the reviewed child-handoff lock for eligible games. Prompt, story, draft, and calm-only content remains reading-oriented until its session eligibility is reviewed.
- Related items: `KPF-0017`, `KPF-0030`

## KPF-0030 — Add default rounds and per-game duration and round overrides

- Status: fixed-awaiting-retest
- Type: Settings / Session configuration / Feature request
- Priority: Should fix soon
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `settings_and_game_detail`
- Occurrence count: 1
- Source note: `KP-NOTE-49411202-49E2-4557-A918-BB28E68BAB52`
- Normalized finding: Add a default number of rounds to Settings. On each eligible game detail page, allow the tester to choose session duration and number of rounds without silently changing the global defaults.
- Expected behavior: Defaults persist across launches, each eligible game can override duration and rounds for the next session, and the applied values are visible before Start.
- Reproduction/triage: Settings precedence and session model specification required.
- Decision: accepted for future revision
- Implementation status: complete; B14 adds eligible details-page duration and round controls with independent per-game next-session overrides, reset-to-defaults behavior, and visible applied values.
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Settings defaults remain persisted globally; details-page selectors persist only the selected game's override, and the Start foundation consumes that override once without mutating global duration or round defaults. JVM session tests, the details-page contract test, Android-test source compilation, debug assembly, lint, and the checkpoint validator passed. No Android target was connected.
- Related items: `KPF-0017`, `KPF-0029`

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
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `home`
- Occurrence count: 1
- Source note: `KP-NOTE-EC1CCA4A-5421-4C3C-9A16-8BDE4370EE80`
- Normalized finding: Use more graphical controls on Home instead of plain text boxes. Remove subtext from every “More ways to start” control and represent Settings with a compact gear icon.
- Expected behavior: Home shortcuts are compact, recognizable, accessible, and do not depend on decorative subtext.
- Reproduction/triage: Gemini visual brief and accessibility review required.
- Decision: accepted for future revision
- Implementation status: complete; awaiting physical retest
- Retest build: 0.6.0-beta1 (6)
- Verification result: automated_checks_passed_pending_family_device_retest
- Implementation evidence: Home shortcuts now use compact graphical cues, readable labels, semantic click labels, minimum touch targets, and no rendered shortcut subtext; native symbols were used instead of a custom raster asset.
- Related items: `KPF-0006`, `KPF-0035`, `KPF-0036`

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
- Affected build: 0.5.0-beta1 (5)
- Capture screen: `home`
- Occurrence count: 1
- Source note: `KP-NOTE-EE140225-4007-4170-8BC4-4BE68C4DAFB3`
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
