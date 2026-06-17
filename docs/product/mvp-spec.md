# KinPlay MVP Product Spec

## Product goal

KinPlay is an Android app for guided family play with young children. The MVP should help a parent quickly start a short, safe, low-prep activity with kids ages 2-8.

## MVP principles

- Offline-first: ship seed content as local JSON assets.
- No accounts, child profiles, camera, microphone, location, contacts, public sharing, ads, or runtime AI.
- Parent-led: the adult chooses or starts a session; children participate verbally or physically.
- Short sessions: default to 5-15 minutes.
- Low setup: each activity lists materials, if any, before start.
- Safe by default: content includes age tags, safety tags, and supervision notes.

## MVP modes

1. Quick Play
   - Parent taps Start.
   - App selects a suitable card using session selection rules.
   - Best for immediate family use.

2. Pick a Game
   - Parent browses activity cards by category.
   - Filters: age range, duration, energy level, materials, mode.

3. Mad Libs
   - Parent or child fills prompted words.
   - App reveals a silly family-safe story.
   - Must support word prompts such as noun, verb, adjective, place, animal, name, sound, food, and number.

4. Calm Down
   - Short quiet activities for transitions, bedtime-adjacent moments, or post-conflict reset.
   - No scary, competitive, or high-energy prompts.

## MVP content types

### Activity card

A single guided play activity. Examples: animal charades, silly walk challenge, story dice without dice, scavenger hunt.

Required content:
- Title
- Summary
- Mode
- Age range
- Duration estimate
- Energy level
- Materials
- Setup steps
- Play steps
- Parent notes
- Safety tags
- Replay variations

### Mad Libs template

A fill-in-the-blank story with ordered fields.

Required content:
- Title
- Age range
- Prompt fields
- Template text with placeholders
- Optional read-aloud note
- Safety tags

### Prompt card

A lightweight conversation or imagination prompt.

Required content:
- Title
- Prompt text
- Optional follow-up prompts
- Age range
- Duration estimate
- Safety tags

## Age tags

Use broad overlapping ranges, not strict grade labels:

- `age_2_3`
- `age_4_5`
- `age_6_8`
- `all_ages`

Each card also has numeric `minAge` and `maxAge` for filtering.

## Safety tags

Allowed MVP safety tags:

- `parent_supervision`
- `movement`
- `quiet`
- `no_materials`
- `small_objects`
- `food_optional`
- `outdoor_optional`
- `reading_help`
- `sibling_friendly`
- `calming`

MVP must avoid content requiring:

- accounts or child identity capture
- images/video/audio recording
- online interaction
- location access
- purchases
- unsafe physical contact
- scary, violent, sexual, political, or medical content

## Session selection rules

Quick Play selects one eligible card using these rules:

1. Include only active seed content.
2. Match selected mode if the parent chose one; otherwise allow all MVP modes.
3. Prefer `all_ages` or cards that overlap the configured household age range.
4. Exclude cards with unavailable required materials.
5. Avoid repeating the same card twice in a row.
6. Prefer shorter duration for first-run sessions.
7. Prefer `no_materials` cards when no setup preference is known.
8. For Calm Down, include only `quiet` or `calming` content and exclude `movement`.

The MVP can implement these rules locally with deterministic fallback: if filtering produces no card, show a curated `all_ages`, `no_materials`, short-duration card.

## Screens required for MVP

1. Home
   - App name
   - Quick Play button
   - Pick a Game button
   - Mad Libs button
   - Calm Down button

2. Activity detail
   - Title, duration, age range, materials
   - Setup section
   - Steps section
   - Variation button or list

3. Mad Libs fill-in
   - Ordered fields
   - Simple validation: required fields cannot be blank

4. Mad Libs reveal
   - Story text with filled values
   - Start another button

5. About / Safety
   - Parent-led disclaimer
   - No data collection statement for MVP

## Acceptance criteria

The MVP spec is satisfied when:

- The app builds a debug APK locally.
- The APK can be copied to `/mnt/cyberforgex-torrents/KinPlay/apk-drops`.
- The app launches on a physical Android device after manual transfer.
- Home screen exposes Quick Play, Pick a Game, Mad Libs, and Calm Down.
- Content loads from local JSON assets.
- At least 12 seed items exist:
  - 5 activity cards
  - 3 Mad Libs templates
  - 2 calm-down cards
  - 2 prompt cards
- Quick Play chooses a card without network access.
- Mad Libs collects fields and renders a completed story.
- No account, analytics, ads, in-app purchases, camera, microphone, contacts, or location permission is requested.
