# KidPlay design system companion

Status: G3 design-system contract, owner-approved hybrid
Canonical tokens and rules: `/DESIGN.md`
Owner decision source: Kanban task `t_f5167e45`, completed 2026-09-23
Companion scope: implementation handoff, state coverage, accessibility, responsive behavior, and verification

## 1. Approved product direction

KidPlay uses one hybrid system:

- D1 Calm Illustrated Family supplies the app-wide foundation, warm paper surfaces, restrained original illustration, and readable family entry.
- D2 Structured Family Utility supplies moment selection, parent discovery, setup visibility, local filtering, and adult-speed scanning.
- D3 Bold Activity supplies the focused active-session contract, macro task targets, explicit correction, completion, and bounded next steps.

The owner-approved priority moments are exactly:

1. No prep or no materials, including examples such as I Spy and Freeze Dance Statues.
2. Together, for group play and mixed-age participation.
3. Move, for supervised movement with space and safety information.

The owner approved this hybrid only. Dev Lab feedback can lead to a later amendment; it does not add scope on its own.

The public product-name decision remains separate from this design approval. Use KidPlay as the working design-system name and do not resolve the existing naming decision through visual work.

## 2. Implementation boundary

Use `DESIGN.md` as the source for new design-system work. This companion covers how to apply it in Compose and test the behavior. It does not claim that the current production app already follows every rule.

These items still need validation:

- Physical-device touch feel is open until an owner device review records it.
- TalkBack and Switch Access service behavior requires manual device validation.
- Audio focus, interruption, and release require runtime tests on the selected build.
- Offline behavior requires a physical or controlled runtime check; bundled assets alone are not proof.
- Completion-to-next-step behavior is a design requirement, not a claim about every existing route.
- The owner explicitly deferred an app-defined reduced-motion behavior. Do not implement or claim one under this card.

If implementation conflicts with this document, keep the protected product posture and record an amendment. Do not choose a new policy without documenting it.

## 3. Screen contract

Show each screen's purpose before its decoration. The following table sets the minimum content contract for the main routes.

| Surface | Required content | Primary action | Secondary behavior |
|---|---|---|---|
| Home | Working identity, ready activity, No prep/Together/Move moments, library, parent/settings | Start the ready activity or choose a moment | Browse library or open parent/settings |
| Moment results | Selected moment, result title, duration, participant fit, energy, materials, setup, safety cue | Open one result or Start when detail is unnecessary | Change moment, apply or clear local filters |
| Activity detail | Title, summary, time, participants, energy, materials, setup, safety, parent notes, first rule | Start | Back or choose another |
| Understand | One concise rule, one visual/text example, guide cue if useful | Start | Skip guide or Back |
| Active session | Task, current state, stable exit, task-specific control | Check, Done, or the activity action | Retry, Pause, or Back when supported |
| Completion | What finished, one brief acknowledgement, Repeat/Another fit/Back | One bounded next step | Other safe ending |
| Parent/settings | Adult-readable local controls and product/privacy explanation | Save or return | No child identity or account route |
| Empty/error/offline | Cause, current route context, one recovery action | Retry, Clear, or Back | No false success or network promise |

Make the primary action visually distinct and first in semantic order. When a screen has several choices but one next step is expected, give that action clear priority.

## 4. State machine

The shared activity state sequence is:

`prompt → selection → understand → start → active → correct/incorrect → retry or continue → complete → next step`

The state model is a behavioral contract. Implementations may use different Kotlin types, but they must preserve these invariants.

| State | Entry condition | Visible information | Input rule | Exit |
|---|---|---|---|---|
| Prompt | The route is ready for a new task. | Task wording, context, and first action. | One primary action is available. | Selection or Understand. |
| Selection | A moment, result, or answer is chosen. | The selected label and its selected semantics. | Repeated selection has no additional effect. | Detail, Understand, or Start. |
| Understand | The activity needs a short rule or setup cue. | Rule, example, setup, and safety text. | Guide can be skipped; text remains. | Start or Back. |
| Start | The user commits to the task. | Selected activity and settled transition. | Duplicate taps are ignored. | Active. |
| Active | The task is underway. | Current instruction, target, state, and stable exit. | Preserve valid work during input. | Correct, Incorrect, Complete, or Back. |
| Correct | An input or step is accepted. | Specific acceptance and the next step. | Do not require a second confirmation for the same input. | Active or Complete. |
| Incorrect | The input needs another attempt. | Neutral explanation and the next useful action. | Keep valid prior work. | Retry or Active. |
| Retry | The user requests another attempt. | What will repeat and what remains. | Repeat only the intended step. | Active. |
| Transition | A route or state change is in progress. | Destination context when useful. | Ignore duplicate navigation and settle once. | Destination state. |
| Complete | The activity has reached its defined endpoint. | Completion text, safe ending, and bounded choices. | Do not auto-replay. | Repeat, Another fit, or Back. |
| Next step | A completion choice is available. | One recommended next action and alternatives. | No infinite feed or forced continuation. | New task, moment, or Home. |

Error, empty, loading, disabled, and offline states are not separate destinations by default. They are explicit states of the owning surface and must retain route context and a recovery action.

## 5. Fox guide contract

The fox is a functional guide, not a permanent mascot layer. Its permitted state vocabulary is:

- `neutral`: no guidance is needed;
- `welcoming`: activity entry cue;
- `thinking`: a brief waiting or choice cue when the activity needs it;
- `encouraging`: gentle correction or retry cue;
- `celebrating`: one completion acknowledgement.

The owner-approved guide cadence is:

1. Activity entry.
2. Mistake or incomplete action requiring gentle correction.
3. Completion.

The fox does not repeatedly interrupt successful play. It does not provide a score, a streak, a reward economy, or a hidden progress claim.

### Placement

| Context | Placement | Prohibited behavior |
|---|---|---|
| Activity entry | Beside the title, rule, or Start action | Covering the action or appearing before the task is readable |
| Correction | Beside the relevant instruction or state text | Covering the target, erasing valid work, or blaming the child |
| Completion | Secondary to completion text and next actions | Taking focus from the next safe action or looping celebration |
| Active task | Reserved edge or hidden when unnecessary | Occupying task space without a guide purpose |

### Animation and audio

- Expressions and transitions use the activity energy contract.
- Quiet and low-mobility activities remain restrained.
- Energetic, weird, wacky, or high-energy activities may use more energetic transition, expression, and sound treatment.
- High-energy activity sound is automatically on under the owner decision.
- The fox has no continuous idle animation during successful play.
- Guide animation and audio never block Start, Retry, Done, Back, safety, or completion.
- All guide audio is bundled and offline. Visible text carries the same instruction or result.
- Captions or stable text equivalents accompany meaningful spoken guidance.
- Back, backgrounding, destination change, safety actions, and completion take priority over guide playback.

### Accessibility and parent boundary

- When the fox message is already visible, the fox image is decorative and has no redundant spoken label.
- When the fox exposes an action, the action is a normal labeled control and has a text equivalent.
- The current design does not add a per-activity mute control.
- A parent/settings surface may document existing audio behavior, but this task does not authorize a second mute surface or continuous guide setting.
- Audio failure, system silence, or unavailable media never removes the rule, safety cue, correctness state, or completion meaning.

## 6. Motion, sound, and haptics

### Motion

The normative durations are in `DESIGN.md`:

- feedback: 120 ms;
- standard: 180 ms;
- settle: 240 ms;
- completion: 280 ms.

Use motion only for orientation, direct feedback, or a meaningful state change. Do not use flashing, continuous ambient motion, or repeated celebration loops. Back, safety, Retry, Done, and completion must not wait for motion to finish.

### Reduced motion

The owner decision explicitly says not to define or implement reduced-motion behavior in this work. This is a deliberate scope boundary, not evidence that the runtime already supports it.

- Do not add a reduced-motion toggle, system-setting branch, or alternate animation contract under this card.
- Do not describe the product as reduced-motion compatible in release evidence.
- Keep essential instructions, state text, semantics, safe Back behavior, and audio-independent meaning intact.
- Reopen this decision only through an owner amendment with design, implementation, and device-validation scope.

### Sound

- Sound is local and bundled.
- Sound treatment follows activity energy.
- Energetic activities may start approved sound automatically.
- Quiet activities remain low-key.
- The screen provides text for every meaningful instruction, result, safety message, and completion announcement.
- Audio releases safely on navigation, backgrounding, interruption, and completion.
- No new in-app mute control is added.

### Haptics

Haptics are deferred. No task, correction, fox cue, completion, or safety instruction depends on haptics. A later decision needs owner approval, accessibility review, and physical-device evidence.

## 7. Parent and local-only boundaries

The design remains parent-led and offline-first.

Parent-facing surfaces may explain:

- activity duration, materials, setup, participant fit, energy, and safety;
- local favorites or finite recents where already supported;
- existing sound behavior and local storage;
- product privacy, offline content, and safe stopping.

They must not request:

- account creation;
- child name, age identity, photo, voice, or profile;
- network curriculum or remote recommendation;
- public sharing, ads, purchases, or social exchange;
- runtime AI or telemetry.

The parent lane does not become a learner-progress dashboard. Favorites and small local recents do not establish attainment, a child record, or a household profile.

## 8. Component and Compose mapping

The mapping is a target for the next implementation work. It does not authorize a broad production refactor in this documentation task.

| Design rule | Compose implementation direction | Required check |
|---|---|---|
| Semantic colors | Map `colors.*` to `ColorScheme` roles and explicit state colors. | Token contrast and rendered state review. |
| Typography | Map `typography.*` to `MaterialTheme.typography` or named local styles. | Large text, wrapping, and semantics. |
| Spacing | Use the 4 dp scale for padding and gaps. | 320 dp and expanded-width layout. |
| Shapes | Map rounded tokens to `Shape` values by component role. | Touch bounds and selected/focus states. |
| Elevation | Use restrained tonal/elevation values. | State meaning remains independent of shadow. |
| Primary control | Use one `button-primary` action for the screen state. | Rapid-tap and duplicate-navigation test. |
| Activity card | Keep content metadata visible before Start. | Long labels and no-materials fixture. |
| Active session | Keep task target, rule, state, and safe exit visible. | TalkBack order and correction without lost progress. |
| Fox guide | Keep the guide in a bounded slot with text equivalent. | Entry/correction/completion cadence and interruption. |
| Local content | Load bundled content without network. | Airplane-mode or offline runtime check. |

Do not introduce a new dependency or a new route solely to consume the tokens. Preserve existing package and release constraints until the implementation card scopes a change.

## 9. Responsive and accessibility matrix

| Case | Required result | Evidence |
|---|---|---|
| 320 dp, base text | One-column route; priority moment labels and primary action visible. | Compose preview and runtime semantics. |
| 320 dp, 1.5x text | Labels reflow or stack; no essential clipping or hidden recovery. | Preview plus runtime large-text check. |
| Typical phone | Ready activity remains above secondary discovery; active task remains dominant. | Screenshot and interaction review. |
| Expanded width | List-detail or two-region layout has a linear fallback and no duplicate primary action. | Preview or target-device capture. |
| Move landscape | Activity may use width; safe portrait Back remains available. | Orientation and state-restoration test. |
| TalkBack | Order follows title/rule, guide text, target, current state, primary action, Retry/Back. | Manual service review. |
| Switch Access/keyboard | Same semantic order; no focus trap in filters or dialogs. | Manual input review. |
| Offline | Bundled content and text equivalents remain available. | Controlled offline launch. |
| Audio unavailable | Rule, state, safety, and completion meaning remain visible. | Audio interruption test. |
| Reduced motion request | No behavior is specified or implemented under the current owner decision. | Record as deferred, not passed. |

## 10. Asset and copy handoff

Visual assets follow `docs/design/kidplay-asset-qa.md`. Customer-facing copy follows the product review first, then the required Humanizer pass. The Humanizer pass must not change locked product facts, token names, state names, safety wording, owner decisions, or accessibility requirements.

For every new customer-facing string, review:

- factual accuracy and product scope;
- age-appropriate plain language;
- whether the primary action is explicit;
- whether a text equivalent exists for sound, motion, fox, and image meaning;
- whether the string survives 320 dp and 1.5x text scale;
- whether it creates an unsupported promise about offline behavior, accessibility, personalization, or completion;
- Humanizer handoff after product review.

## 11. G3 validation checklist

### Structural and token checks

- [ ] `DESIGN.md` parses as YAML front matter plus Markdown body.
- [ ] The design CLI reports no broken token references.
- [ ] Component text/background pairs meet WCAG AA for normal text.
- [ ] Each semantic color is referenced or explicitly marked decorative.
- [ ] Canonical Markdown sections appear once and in order.
- [ ] No component variant is nested under another component.

### Interaction checks

- [ ] One primary action is visible for each tested state.
- [ ] Repeated Start, Check, Done, and navigation taps are idempotent.
- [ ] Correction preserves valid progress.
- [ ] Completion states explain what finished and provide a bounded next step.
- [ ] Back and safety actions do not wait for fox motion or audio.

### Fox and media checks

- [ ] Entry cue appears once for the activity.
- [ ] Correction cue appears only when correction is needed.
- [ ] Completion cue is brief and secondary to text.
- [ ] No continuous successful-play interruption occurs.
- [ ] Bundled audio survives local loading and releases on interruption.
- [ ] Text and semantics remain sufficient without sound.
- [ ] No new per-activity mute control is present.
- [ ] Reduced-motion behavior is recorded as owner-deferred, not falsely marked passed.

### Layout and accessibility checks

- [ ] 320 dp one-column layout is readable.
- [ ] 1.5x text scale preserves essential labels and recovery actions.
- [ ] Expanded layout collapses to a valid linear order.
- [ ] Touch targets are at least 48 dp.
- [ ] TalkBack order and announcements are tested on a device.
- [ ] Switch Access or keyboard traversal is tested where available.
- [ ] State meaning survives removal of color, sound, motion, or illustration.

### Product and release checks

- [ ] Offline/local-only/no-account/no-ads/no-purchases/no-social posture remains unchanged.
- [ ] No child identity or profile data is introduced.
- [ ] New assets have provenance, SHA-256, alt text, and QA records.
- [ ] Product review precedes the Humanizer pass.
- [ ] The final implementation and any physical-device limitation are recorded on the appropriate Kanban card.
