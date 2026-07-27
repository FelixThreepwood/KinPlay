# KinPlay Feedback Punchlist

Sanitized product-test records. Incoming comments are evidence, not authorization to change application code.

Last updated: 2026-07-26T12:23:18-07:00

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
- Occurrence count: 1
- Source note: `KP-NOTE-09B10E03-CB5C-4C41-B2F8-E024775D1628`
- Normalized finding: Keep KinPlay at the upper-left, place a concise one-line descriptor beside it, remove the “What fits now” instructional section, and move the six-category grid upward. Defer richer graphics and animated characters to later development.
- Expected behavior: The first screen presents identity, purpose, and category choices with minimal copy and no unnecessary vertical gap.
- Reproduction: Visual review required against the affected build.
- Implementation status: complete
- Retest build: 0.4.0-beta1 (4)
- Verification result: automated_checks_passed_pending_family_device_retest

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
- Occurrence count: 1
- Source note: `KP-NOTE-961D7088-D634-4A4A-89B2-6F57771E399C`
- Normalized finding: Mark the Quality Time category and each activity as intended for one-on-one play, group play, or both.
- Expected behavior: A parent can identify participant fit before opening or starting an activity.
- Reproduction: Content review required against the affected build.
- Implementation status: complete
- Retest build: 0.4.0-beta1 (4)
- Verification result: automated_checks_passed_pending_family_device_retest

## KPF-0009 — Include activities as well as games

- Status: fixed-awaiting-retest
- Type: Product scope / content request
- Priority: Should fix soon
- Affected build: 0.3.0-beta1 (3)
- Capture screen: `home`
- Occurrence count: 1
- Source note: `KP-NOTE-961D7088-D634-4A4A-89B2-6F57771E399C`
- Normalized finding: Treat suitable activities as first-class content alongside games, including drawing, coloring, and painting.
- Expected behavior: Discovery and category language accommodate both games and activities that support family engagement and children’s creativity.
- Reproduction: Product-copy and content review required.
- Implementation status: complete
- Retest build: 0.4.0-beta1 (4)
- Verification result: automated_checks_passed_pending_family_device_retest

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
- Occurrence count: 1
- Source note: `KP-NOTE-2254611E-4E35-4960-9E4D-FA832B55405F`
- Normalized finding: Provide a visible lock control that requires a three-second hold to lock or unlock and displays hold progress. While active, the mode should prevent accidental in-app navigation or game exit when the device is handed to a young participant.
- Expected behavior: Lock state is unmistakable, activation and release require a deliberate hold, progress is visible, and accidental app controls or back navigation are guarded within Android platform limits. System-level escape restrictions require a feasibility and safety review.
- Reproduction: Android platform and safety review required. Related: `KPF-0017`.
- Implementation evidence: Game and activity play surfaces now expose a visible three-second hold-to-lock/unlock control with progress, lock-state announcement, keyboard and accessibility activation countdown, and an overlay that blocks in-app controls and Back while preserving the recovery control; Android system controls intentionally remain available.
- Implementation status: complete
- Retest build: 0.5.0-beta1 (5)
- Verification result: automated_checks_passed_pending_family_device_retest


## KPF-0019 — Create an original KinPlay launcher icon through Gemini

- Status: fixed-awaiting-retest
- Type: Brand design / visual asset / trademark review
- Priority: Should fix soon
- Affected build: 0.4.0-beta1 (4)
- Capture screen: `launcher_and_home`
- Occurrence count: 1
- Source note: `KP-NOTE-1B5F8F8F-0D75-444C-821A-A2B435704E1E`
- Normalized finding: Create a clean, minimal KinPlay icon through the required Google Gemini Nano Banana visual process. Explore simple play and curved smile or directional cues, balanced proportions, and teal, emerald, or pale-yellow color directions while maintaining an original identity that does not imitate third-party logos.
- Expected behavior: The approved icon is original, recognizable at launcher sizes, technically valid for Android adaptive-icon use, and preserved as an untouched Gemini master with documented derivatives.
- Reproduction: Gemini visual brief and trademark review required. Related: `KPF-0020`.
- Implementation evidence: Two original Gemini-generated KinPlay icon masters passed visual QA and are preserved byte-for-byte with provider/model, prompt purpose, SHA-256 hashes, palette, derivative paths, and originality/trademark-review notes; Android adaptive-icon resources ship the approved abstract play/smile emblem.
- Implementation status: complete
- Retest build: 0.5.0-beta1 (5)
- Verification result: automated_checks_passed_pending_family_device_retest


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
