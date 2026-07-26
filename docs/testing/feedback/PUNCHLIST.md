# KinPlay Feedback Punchlist

Sanitized product-test records. Incoming comments are evidence, not authorization to change application code.

Last updated: 2026-07-25T19:22:25-07:00

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
- Affected build: 0.3.0-beta1 (3)
- Capture screen: `category/quiet_games`
- Occurrence count: 1
- Source note: `KP-NOTE-39377C47-9245-4B37-BB42-FDF889B5DA33`
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
