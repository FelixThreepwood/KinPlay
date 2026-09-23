---
version: alpha
name: KidPlay
description: A calm, readable family-play system with structured parent choice and focused active sessions.
colors:
  primary: "#2E6B57"
  primary-strong: "#1F5A45"
  secondary: "#375B63"
  tertiary: "#A64535"
  neutral: "#F8F5EE"
  background: "#F8F5EE"
  surface: "#FFFCF8"
  surface-raised: "#FFFFFF"
  ink: "#1C2B2A"
  ink-muted: "#45534F"
  on-primary: "#FFFFFF"
  on-secondary: "#FFFFFF"
  on-tertiary: "#FFFFFF"
  on-error: "#FFFFFF"
  primary-container: "#DCEFE3"
  on-primary-container: "#153C2C"
  sky: "#8FB8C2"
  sky-strong: "#375B63"
  coral: "#D96B55"
  coral-container: "#F7DDD6"
  sun: "#E7B94C"
  sun-ink: "#3D2B00"
  error: "#B3261E"
  warning: "#7A5000"
  warning-container: "#FFF0C2"
  info: "#285D75"
  info-container: "#DCECF2"
  on-info-container: "#163C4A"
  border: "#6B7B74"
typography:
  display:
    fontFamily: "Android system sans"
    fontSize: "2rem"
    fontWeight: 700
    lineHeight: 1.2
  headline:
    fontFamily: "Android system sans"
    fontSize: "1.5rem"
    fontWeight: 700
    lineHeight: 1.25
  title:
    fontFamily: "Android system sans"
    fontSize: "1.25rem"
    fontWeight: 700
    lineHeight: 1.3
  body:
    fontFamily: "Android system sans"
    fontSize: "1rem"
    fontWeight: 400
    lineHeight: 1.5
  body-emphasis:
    fontFamily: "Android system sans"
    fontSize: "1rem"
    fontWeight: 600
    lineHeight: 1.5
  label:
    fontFamily: "Android system sans"
    fontSize: "0.875rem"
    fontWeight: 600
    lineHeight: 1.43
  metadata:
    fontFamily: "Android system sans"
    fontSize: "0.875rem"
    fontWeight: 500
    lineHeight: 1.43
rounded:
  xs: "8px"
  sm: "12px"
  md: "16px"
  lg: "20px"
  xl: "28px"
  pill: "9999px"
spacing:
  xs: "4px"
  sm: "8px"
  md: "12px"
  lg: "16px"
  xl: "24px"
  2xl: "32px"
  3xl: "48px"
  4xl: "64px"
components:
  screen:
    backgroundColor: "{colors.neutral}"
    textColor: "{colors.ink}"
  screen-shell:
    backgroundColor: "{colors.background}"
    textColor: "{colors.ink}"
  surface-card:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.ink}"
    rounded: "{rounded.lg}"
    padding: "{spacing.xl}"
  surface-raised:
    backgroundColor: "{colors.surface-raised}"
    textColor: "{colors.ink}"
    rounded: "{rounded.md}"
    padding: "{spacing.lg}"
  text-secondary:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.ink-muted}"
  button-primary:
    backgroundColor: "{colors.primary-strong}"
    textColor: "{colors.on-primary}"
    rounded: "{rounded.md}"
    padding: "{spacing.lg}"
  button-primary-hover:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    rounded: "{rounded.md}"
    padding: "{spacing.lg}"
  button-primary-pressed:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    rounded: "{rounded.md}"
    padding: "{spacing.lg}"
  button-secondary:
    backgroundColor: "{colors.primary-container}"
    textColor: "{colors.on-primary-container}"
    rounded: "{rounded.md}"
    padding: "{spacing.lg}"
  button-secondary-pressed:
    backgroundColor: "{colors.primary-container}"
    textColor: "{colors.on-primary-container}"
    rounded: "{rounded.md}"
    padding: "{spacing.lg}"
  button-tertiary:
    backgroundColor: "{colors.secondary}"
    textColor: "{colors.on-secondary}"
    rounded: "{rounded.md}"
    padding: "{spacing.lg}"
  button-accent:
    backgroundColor: "{colors.tertiary}"
    textColor: "{colors.on-tertiary}"
    rounded: "{rounded.md}"
    padding: "{spacing.lg}"
  button-sun:
    backgroundColor: "{colors.sun}"
    textColor: "{colors.sun-ink}"
    rounded: "{rounded.md}"
    padding: "{spacing.lg}"
  button-danger:
    backgroundColor: "{colors.error}"
    textColor: "{colors.on-error}"
    rounded: "{rounded.md}"
    padding: "{spacing.lg}"
  selected-state:
    backgroundColor: "{colors.primary-container}"
    textColor: "{colors.on-primary-container}"
    rounded: "{rounded.sm}"
    padding: "{spacing.md}"
  status-success:
    backgroundColor: "{colors.primary-container}"
    textColor: "{colors.on-primary-container}"
    rounded: "{rounded.sm}"
    padding: "{spacing.md}"
  status-warning:
    backgroundColor: "{colors.warning-container}"
    textColor: "{colors.ink}"
    rounded: "{rounded.sm}"
    padding: "{spacing.md}"
  status-error:
    backgroundColor: "{colors.error}"
    textColor: "{colors.on-error}"
    rounded: "{rounded.sm}"
    padding: "{spacing.md}"
  status-info:
    backgroundColor: "{colors.info-container}"
    textColor: "{colors.sky-strong}"
    rounded: "{rounded.sm}"
    padding: "{spacing.md}"
  coral-message:
    backgroundColor: "{colors.coral-container}"
    textColor: "{colors.ink}"
    rounded: "{rounded.sm}"
    padding: "{spacing.md}"
  focus-ring:
    backgroundColor: "{colors.info}"
  decorative-coral:
    backgroundColor: "{colors.coral}"
  decorative-sky:
    backgroundColor: "{colors.sky}"
  decorative-sun:
    backgroundColor: "{colors.sun}"
  decorative-border:
    backgroundColor: "{colors.border}"
---

## Overview

KidPlay combines the D1 Calm Illustrated Family foundation, D2 Structured Family Utility for discovery and setup, and D3 focused active-session behavior. It is designed for parent-led family play, with bundled content and local storage; no account is required. KidPlay is the working product name. The owner will decide public naming separately under A-01.

The approved design direction focuses on three family moments:

1. No prep or no materials, with examples such as I Spy and Freeze Dance Statues.
2. Together, for shared play and mixed-age participation.
3. Move, for supervised movement with clear space and safety information.

The expected path is:

`open → choose moment → select activity → understand → start → active → complete → next step`

This file sets product and implementation requirements; it does not show that the current runtime meets them. Physical-device feel, TalkBack, Switch Access, audio lifecycle, and final owner acceptance still need validation. The owner may update this contract after Dev Lab review.

Show one task and one primary action at a time. Give each state immediate, specific feedback; preserve valid progress during correction; prevent accidental duplicate input; and state what happens next. The interface must remain understandable without sound. Accounts, child profiles, public sharing, ads, purchases, remote curriculum, runtime AI, and telemetry require a separate product and privacy decision.

## Colors

The palette is warm paper, deep ink, moss, accessible blue-green, coral, muted sky, and sun. Semantic roles control meaning. A raw accent is decorative unless its text pair passes the required contrast.

| Token | Value | Use |
|---|---|---|
| `colors.background` | `#F8F5EE` | App background and calm canvas. |
| `colors.surface` | `#FFFCF8` | Cards and grouped content. |
| `colors.surface-raised` | `#FFFFFF` | Dialogs and surfaces above cards. |
| `colors.ink` | `#1C2B2A` | Primary text, headings, and task instructions. |
| `colors.ink-muted` | `#45534F` | Supporting text and metadata. |
| `colors.primary` | `#2E6B57` | Moss selected state and hover/active emphasis. |
| `colors.primary-strong` | `#1F5A45` | Primary action background. |
| `colors.primary-container` | `#DCEFE3` | Selected, success, and quiet supporting surfaces. |
| `colors.secondary` | `#375B63` | Secondary action background and utility emphasis. |
| `colors.tertiary` | `#A64535` | Accessible coral action for a limited activity accent. |
| `colors.coral` | `#D96B55` | Decorative illustration accent only. |
| `colors.coral-container` | `#F7DDD6` | Soft correction or coral information surface. |
| `colors.sky` | `#8FB8C2` | Decorative sky accent only. |
| `colors.sun` | `#E7B94C` | Decorative or labeled sun accent. |
| `colors.error` | `#B3261E` | Error and destructive state. |
| `colors.warning-container` | `#FFF0C2` | Warning surface with deep ink text. |
| `colors.info-container` | `#DCECF2` | Informational surface. |
| `colors.border` | `#6B7B74` | Borders and focus support; never the only state signal. |

Contrast rules:

- Normal text uses `colors.ink` or `colors.ink-muted` on `background`, `surface`, or `surface-raised`.
- Filled primary actions use `primary-strong` with `on-primary`; selected and success surfaces use `primary-container` with `on-primary-container`.
- `tertiary` is the accessible coral action. Raw `coral` and `sky` remain decorative unless a later tested pair is approved.
- `sun` uses `sun-ink` for text. White text is not permitted on the sun accent.
- Error, warning, and info states use the explicit component pairs in the front matter. Do not invent a color-only state.
- Every new color must be added to the semantic palette, referenced by a component or documented as decorative, and checked against WCAG AA before use.

## Typography

Use the Android system sans family for all essential text. The system uses size, weight, line height, and spacing for hierarchy. Decorative display lettering must not carry instructions, safety content, or state meaning.

| Role | Size / weight | Line height | Use |
|---|---|---|---|
| Display | 32 sp / 700 | 1.2 | Product title or major moment heading. |
| Headline | 24 sp / 700 | 1.25 | Screen heading or completion announcement. |
| Title | 20 sp / 700 | 1.3 | Activity title, section heading, or dialog title. |
| Body | 16 sp / 400 | 1.5 | Instructions, summaries, setup, safety, and parent notes. |
| Body emphasis | 16 sp / 600 | 1.5 | Short action or state emphasis within body text. |
| Label | 14 sp / 600 | 1.43 | Buttons, moment labels, and control labels. |
| Metadata | 14 sp / 500 | 1.43 | Duration, participant fit, energy, and materials. |

Rules:

- Essential text is never smaller than 14 sp in the base configuration.
- Do not use all-caps for full instructions. Short labels may use weight and spacing, but remain readable when spoken by TalkBack.
- Keep activity instructions short. Put the action first, then the condition or safety note.
- Preserve the full label at large text. Reflow, scroll, or stack; do not hide or truncate essential meaning.
- Long titles and labels are test fixtures, not exceptional content. The component must remain usable without manual text editing.
- Use semantic headings and a stable reading order. Visual size alone does not establish heading semantics.

## Layout

Use a 4 dp base spacing scale. Map Compose dimensions to these tokens; add a screen-specific value only when a component needs a measured exception.

| Token | Value | Use |
|---|---:|---|
| `spacing.xs` | 4 dp | Icon-to-label or tight text spacing. |
| `spacing.sm` | 8 dp | Related metadata and compact control gaps. |
| `spacing.md` | 12 dp | Control internals and short list gaps. |
| `spacing.lg` | 16 dp | Standard component padding and row gaps. |
| `spacing.xl` | 24 dp | Card padding and section gaps. |
| `spacing.2xl` | 32 dp | Major group separation. |
| `spacing.3xl` | 48 dp | Entry or completion breathing room. |
| `spacing.4xl` | 64 dp | Reserved only for large structural separation. |

Layout rules:

- A screen has one dominant route and one obvious primary action. Secondary actions remain visually and semantically secondary.
- At 320 dp, use one column. Do not require a horizontally scrolling chip row to discover the priority moments.
- The three priority moments remain text-labeled. Icons and fox expressions can support them, but cannot replace the labels.
- Content order is title, context or summary, setup burden, safety, primary action, then secondary actions unless the activity requires a verified variation.
- Keep the active task visually dominant. A guide panel, illustration, or metadata block must not displace the task or hide the exit path.
- Expanded width may use a list-detail or two-region arrangement. The reading order must remain correct when the regions collapse to one column.
- Landscape is route-scoped for Move or another activity whose task benefits from width. Home and parent surfaces retain a safe portrait path.
- Foldable posture-specific behavior is deferred until a target posture and evidence exist.
- Preserve the selected moment and valid activity state through ordinary resize and safe Back behavior where the implementation can support it.

## Elevation & Depth

Depth separates grouped content from the background. It does not signal status by itself.

| Token | Value | Use |
|---|---:|---|
| `elevation.none` | 0 dp | Flat task field or quiet background. |
| `elevation.surface` | 1 dp | Supporting surface separation. |
| `elevation.card` | 2 dp | Ready card or activity card. |
| `elevation.raised` | 4 dp | Dialog or temporary parent surface. |

Rules:

- Prefer tonal separation and borders before increasing shadow.
- Do not use elevation to communicate correct, incorrect, disabled, or selected state. Pair state with text, semantics, and a visible border or fill.
- Borders use 1 dp for ordinary grouping and 2 dp for selected or focus support. A focus ring must remain visible against both the background and its surface.
- Shadows must remain restrained on calm surfaces. Avoid glossy treatments, gradients, flashing elevation, and stacked card shadows.
- A disabled action must have a semantic disabled state and readable supporting text where the reason is not obvious.

## Shapes

Use `rounded.md` for buttons and primary controls, `rounded.lg` for ready cards, and `rounded.sm` for selected or status surfaces. Reserve `rounded.pill` for compact chips or badges whose text remains fully visible.

- A rounded shape never replaces a label or a state announcement.
- Do not make every surface a pill. The task field, cards, controls, and status surfaces need distinct roles.
- Keep touchable padding inside the shape and preserve at least 48 dp by 48 dp for every interactive target. Movement targets may be larger.
- Illustrations use clear silhouettes and a restrained set of original forms. Do not copy a reference product’s characters, poses, status marks, or scene composition.

## Components

### Interaction discipline

For each activity state, define one task and one primary action. Give immediate feedback, allow safe correction, control pacing, state when the activity is complete, and offer a bounded next step.

- `prompt`: state the task in one readable sentence and expose the available action.
- `selection`: show the selected moment or option with text and a semantic selected state.
- `correct`: name what was accepted and move to the next task step without unnecessary delay.
- `incorrect`: state the problem neutrally and identify the next useful action. Do not erase valid work.
- `retry`: keep the retry action visible and secondary to the current task. Retrying must not reset completed steps unless the activity requires a deliberate restart.
- `transition`: settle one destination at a time. Disable or ignore duplicate submissions while navigation or completion is pending.
- `completion`: state what finished, stop or pause activity audio safely, and expose a clear next action.
- `next-step`: offer Repeat, another suitable activity, or return to the selected moment. Do not create an endless feed or pressure to continue.

Rapid taps and repeated taps must be safe. A single gesture produces at most one submission or navigation result. The final state must not depend on the tap count, and Back or a safety action must not wait for guide animation, sound, or a settling delay.

### Controls

There is one filled primary control per screen or activity state. Use the component hierarchy below:

| Role | Component | Examples |
|---|---|---|
| Primary | `button-primary` | Start, Check, Done, or the chosen next action. |
| Secondary | `button-secondary` | Choose another, View setup, or a parent route. |
| Tertiary | `button-tertiary` | Browse, filter, or a lower-emphasis utility action. |
| Accent | `button-accent` or `button-sun` | Activity-specific emphasis after contrast validation. |
| Destructive | `button-danger` | Stop, discard, or a clearly confirmed destructive action. |
| Icon-only | Labeled icon control | Back, search, or settings only when the accessible label is adjacent in semantics. |

Rules:

- A card can be selectable, but card selection must have a role, label, selected state, and a visible primary route. Do not make an entire screen an unlabeled collection of click targets.
- Icon-only controls require a content description, a stable focus target, and a hit area of at least 48 dp.
- Pressed, focused, selected, disabled, loading, and completed states must remain distinguishable without sound, motion, or color alone.
- Do not add a new per-activity mute button under the current owner decision. Do not use a hidden gesture as a mute path. Existing parent or system audio behavior must be verified without adding a second control surface.

### Navigation

On the first screen, show the working product identity, one ready route, the three priority moments, library access, and a clearly labeled parent/settings route. The app remains usable without network access or account setup.

Required route behavior:

- Home opens locally and does not require a sign-in, child profile, or remote load.
- Choosing a moment preserves the choice until the parent changes it or completes a safe route.
- Activity detail shows title, summary, duration, participant fit, energy, materials, setup, safety, and parent notes before Start.
- Start opens one focused activity. The Back action remains stable and returns to the originating route without discarding valid selection state.
- Parent/settings surfaces are adult-readable and separate from the child-facing active task. They do not request child identity.
- System Back, gesture Back, and visible Back use the same safe destination. A drawer or filter surface closes before the parent route changes.
- An invalid or missing local item states the cause and offers one meaningful recovery action, usually Back or Retry.
- A loading state is truthful and short. A failure state does not pretend that remote content exists.

### States

Every reusable surface must cover the following states. Test each state's name, trigger, visual treatment, semantic announcement, and recovery action in a component test or Dev Lab fixture.

| State | Required content | Primary behavior |
|---|---|---|
| Prompt | Short task statement, context, and first action. | Move to selection or start. |
| Selection | Selected label, selected semantics, and clear change path. | Confirm once; repeated selection is idempotent. |
| Correct | Specific acceptance text and the next task step. | Preserve progress and continue. |
| Incorrect | Neutral explanation and the next useful attempt. | Keep valid work and show Retry or correction. |
| Retry | Explicit retry label and what will be repeated. | Retry only the intended step. |
| Transition | Progress or destination context when useful. | Ignore duplicate navigation and settle once. |
| Completion | What finished, a calm acknowledgement, and next choices. | Repeat, another fit, or return. |
| Next step | One recommended safe action plus bounded alternatives. | Never force continuation. |
| Loading | What is loading and no false success state. | Wait, Retry, or Back according to the local source. |
| Empty | Why there is no result and one recovery action. | Clear filter, Browse, or Back. |
| Error | Cause in plain language, semantic error, and one recovery action. | Retry, Back, or return to a safe known state. |
| Offline | Local availability statement and any unavailable-content explanation. | Continue locally or Back; do not imply network recovery. |
| Disabled | Disabled semantics and an explanation when needed. | Keep the user oriented to the enabled action. |

State text is part of the product meaning. Do not communicate a result only with a fox expression, color, sound, animation, or icon.

### Fox guide

The fox is a functional guide. It appears only when it improves entry, correction, or completion.

| Fox state | Purpose | Placement | Limit |
|---|---|---|---|
| `entry` | Identify the activity and the first action. | Beside the activity title or primary Start action. | One short cue at activity entry. |
| `correction` | Give a neutral next attempt after a mistake or incomplete action. | Beside the relevant instruction, never over the task target. | Only when correction is needed; do not interrupt a successful attempt. |
| `completion` | Acknowledge that the activity finished. | Secondary to completion text and next-step actions. | One brief cue, then remain quiet. |
| `quiet` | Hold a neutral state when no guide action is needed. | Reserved guide slot or hidden when it would consume task space. | No idle animation or repeated speech. |

Fox expression vocabulary is limited to `neutral`, `welcoming`, `thinking`, `encouraging`, and `celebrating`. The expression must agree with the text state. No expression may blame, shame, imply a score, or suggest that audio is required.

Fox animation follows the activity energy and uses the motion tokens only where motion improves orientation or feedback. It never loops continuously during successful play. Entry, correction, and completion cues may be skipped by leaving the state; the guide cannot block Start, Retry, Done, Back, or a safety action.

Fox accessibility rules:

- The fox is decorative when its text is already exposed. Give it an empty semantic description in that case.
- If the fox exposes a unique action, expose that action as a labeled control and keep it available in text.
- Spoken guidance, when present, is bundled and offline. The same instruction and result remain visible as text.
- Captions or a stable text equivalent appear with meaningful spoken guidance. Audio failure falls back to text without blocking progress.
- A parent/settings route may document guide frequency, but this task does not authorize continuous guide playback or a new mute control.
- Guide cues stop or release priority when Back, a safety action, app backgrounding, or a destination change occurs.

### Illustration and images

Illustration supports comprehension, orientation, or a state. It does not fill every card.

- Use original flat illustrated forms with a restrained palette and clear silhouettes.
- Activity cards remain text-first. A small image or fox cue may accompany a title, but duration, participant fit, energy, materials, setup, and safety remain text.
- Instructional images have a visible text instruction and a Compose semantics equivalent.
- Do not copy reference-product artwork, characters, silhouettes, costumes, faces, scene layouts, logos, category art, or status marks.
- Generate new visual assets only through Google Gemini Nano Banana. Preserve the untouched generated master and its prompt/provenance record.
- Do not blur, pad, crop, resize, or rewrite DPI metadata on the untouched master. A derivative is permitted only for an explicit component requirement and must record the source master, transformation, dimensions, and QA result.
- Do not place essential text inside an image. Any asset text must be separately proofread and represented in accessible text.
- Respect the exact required subject count. Reject duplicate subjects, detached limbs, malformed hands, hidden controls, unreadable text, and artifacts that obscure the task.

### Motion, reduced motion, sound, and haptics

Match motion and sound to the activity's energy. Keep quiet or low-mobility activities restrained. Energetic, weird, wacky, or high-energy activities may use more energetic transitions, expressions, and sound. Per the owner decision, sound starts automatically for high-energy activities.

Motion tokens:

- `feedback`: 120 ms for direct press or selection response.
- `standard`: 180 ms for ordinary state change.
- `settle`: 240 ms for route or panel settling.
- `completion`: 280 ms for a completion state change.
- Do not use continuous ambient motion, flashing, or repeated celebratory loops.
- Motion must never delay safety, Back, Retry, Done, or completion acknowledgement.

Owner boundary for reduced motion:

- The current owner decision does not authorize an app-defined reduced-motion mode, toggle, or alternate motion implementation.
- Do not add reduced-motion behavior in this task, and do not claim that the current runtime supports it.
- Preserve readable text, semantics, safe navigation, and audio-independent instructions. A later owner amendment may reopen reduced-motion behavior as a separate design and validation decision.

Sound rules:

- Sound is bundled and offline. No remote narration, runtime generation, or network dependency is implied.
- Sound is activity-matched. High-energy activities may start their approved sound automatically; quiet activities remain low-key.
- The essential instruction, correctness result, safety information, and completion result are visible in text and semantics without sound.
- Meaningful spoken guidance has a caption or stable text equivalent. Music does not carry instructions.
- Sound and guide playback stop or release safely on Back, destination change, app backgrounding, interruption, and completion. No audio continues invisibly.
- Do not add another in-app mute control. Do not make a new activity control compete with the owner-approved automatic sound behavior.

Haptics are deferred. No activity, state, fox cue, or safety message depends on haptics. A future haptics decision requires owner approval, accessibility review, and a device test.

### Accessibility

Include accessibility requirements in each component contract from the start.

- Use semantic headings, roles, labels, selected/checked state, disabled state, progress, and error announcements.
- TalkBack order follows the task: screen title or rule, guide text or equivalent, task target, current state, primary action, Retry or Back.
- Home order is title, selected moment, ready card summary, Start, secondary choices, library, and parent/settings.
- Do not rely on color, sound, movement, expression, icon shape, or image recognition as the only state channel.
- Use at least 48 dp by 48 dp touch targets. Give movement targets more separation where motor control needs it.
- Preserve focus after selection, correction, completion, error recovery, and Back. Focus must not land on a removed guide cue.
- Switch Access and keyboard traversal follow the same semantic order. No filter, dialog, or guide panel may trap focus.
- Every icon-only control has a concise accessible label. Decorative art has no redundant label.
- Keep safety warnings, setup, materials, and next-step meaning in text.
- Validate contrast for text, controls, focus indicators, error states, and selected states.
- Validate on physical devices when available. Emulator semantics and screenshot evidence do not prove service-level behavior.

### Large text and responsive layouts

Reflow content to support large text; do not hide it.

- At 1.5x text scale, titles, labels, metadata, setup, safety, and action text remain present and readable.
- Cards stack when needed. Buttons may become full-width. Metadata may wrap to multiple lines.
- Do not reduce text below the base token to keep a row on one line.
- Do not clip the fox caption, task rule, error, completion, or next-step action.
- The 320 dp layout uses one column and deterministic focus order.
- The typical phone layout keeps the ready activity above secondary discovery.
- Expanded width may use list-detail or two-region composition, but both regions must have labels and a linear fallback.
- Move may use landscape when the active task benefits from width. Return to portrait without losing valid progress where the platform can support it.
- Device screenshots must record width, density, orientation, font scale, build revision, and fixture. A screenshot is evidence for that configuration only.

### Asset QA

Keep a record for every approved asset. See `docs/design/kidplay-asset-qa.md` for the companion checklist.

Required record fields:

- asset ID and intended surface/state;
- prompt and generation date;
- Google Gemini Nano Banana path and model label when available;
- untouched master path and SHA-256;
- derivative paths and transformations, if any;
- dimensions, format, color mode, and transparency result;
- concise alt text and Compose semantics text;
- subject count, silhouette, edge, text, and artifact review;
- state, contrast, large-text, responsive, and crop review;
- originality review against reference captures;
- reviewer, decision, and revision date.

Reject an asset if it changes identity cues, copies a trade-dress element, hides a required control, includes unreadable or malformed text, adds an unrequested subject, loses required transparency, or cannot be explained without the image.

## Do's and Don'ts

- Do keep the D1 visual foundation, D2 parent-speed utility, and D3 focused active session as one approved hybrid.
- Do prioritize No prep or no materials, Together, and Move.
- Do expose materials, setup, participant fit, energy, duration, and safety before Start.
- Do give every state a text and semantic equivalent.
- Do use the fox at entry, correction, and completion, with no continuous interruption during successful play.
- Do preserve valid progress during correction and ignore duplicate submissions.
- Do keep content, audio, and generated assets offline and local.
- Do run product review before the Humanizer pass on customer-facing copy.
- Do record Unknown behavior instead of presenting a design intention as runtime proof.
- Don't add a child profile, account, social exchange, ad, purchase, remote curriculum, runtime AI, or telemetry without a separate approval.
- Don't add a new per-activity mute control or an app-defined reduced-motion mode under this owner decision.
- Don't make the fox, sound, motion, color, or illustration the only carrier of meaning.
- Don't copy a reference product's artwork, exact composition, wording, taxonomy, control placement, or reward mechanic.
- Don't use raw decorative colors for normal text without a contrast check.
- Don't let a guide, transition, audio cue, or celebration block Back, safety, correction, completion, or the next safe action.
- Don't claim physical-device, TalkBack, Switch Access, offline breadth, or completion behavior until the corresponding evidence exists.
