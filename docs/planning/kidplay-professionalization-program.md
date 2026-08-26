# KidPlay Professionalization Program

Status: Active  
Program owner / creator: LJ Busekrus  
Program manager: Hermione  
Canonical execution board: Hermes Kanban board `kinplay`  
Program window: 2026-08-24 through 2027-04-24, or public-release approval by the owner  
Primary repository: `/home/phantomatic/projects/app-pass-rev/KinPlay`

## 1. Mission

Raise KidPlay from a capable offline Android app into an original, cohesive, trustworthy, public-release-quality family product. Preserve the no-ads, no-accounts, offline-first, parent-led posture unless the owner explicitly approves a product change.

The board, not scattered chat lists, is the source of truth for task order, ownership, dependencies, blockers, and completion evidence.

## 2. Program outcomes

1. Evidence-based benchmark audit across leading children’s and family products.
2. Separate capability-gap and appearance-gap matrices.
3. An original KidPlay product and visual direction; no copying of branded artwork, exact layouts, copy, or distinctive trade dress.
4. A durable `DESIGN.md` with validated tokens, components, layouts, motion, accessibility, and asset rules.
5. A Dev Lab component gallery covering normal and adverse states.
6. One complete production vertical slice:
   `open → choose moment → select activity → understand → start → complete → next step`.
7. Full-app migration after vertical-slice approval.
8. Physical-device, accessibility, privacy, content, performance, and release verification.
9. A signed public-release candidate and owner go/no-go record.

## 3. Protected constraints

- Android-first, Kotlin, Jetpack Compose.
- Offline-first bundled content and local-only storage.
- No accounts, ads, child identity capture, camera, microphone, contacts, location, public sharing, or runtime AI without a separately approved product/privacy change.
- Parent-led experience for families with children roughly ages 2–8.
- User-supplied content must remain distinguishable and protected from automated deletion.
- Gemini Nano Banana is the exclusive visual-asset generation path.
- Customer-facing KidPlay and Dev Lab copy receives a Humanizer pass after factual/product review.
- Every feedback batch increments the affected app version.
- Published APK names contain app name, version, and optional date only; forbidden descriptors remain excluded.
- Only one current APK per app remains in Google Drive and the shared APK drop; superseded artifacts are removed after read-back verification.
- Physical-device feel, touch, orientation, TalkBack, and animation acceptance remains an owner gate when no suitable device is attached to Hogwarts.
- Duolingo-derived interaction discipline is a protected redesign requirement: one clear task at a time, one obvious primary action, immediate specific feedback, safe correction and recovery, controlled pacing, explicit completion/next-step behavior, and resilience to rapid or repeated input.
- The KidPlay fox is the functional guide character. Across appropriate flows, the fox introduces activities, demonstrates rules, acknowledges choices, celebrates completion, and gently redirects mistakes. Every appearance must serve a clear interaction or comprehension purpose rather than consume space decoratively.
- Fox guidance and audio remain offline, optional, parent-appropriate, accessible without sound, compatible with mute and reduced-motion settings, and free of manipulative engagement mechanics.

## 4. Operating model

### Roles

- **LJ / Mr. B — product owner:** taste decisions, priority exceptions, reference captures, physical-device observations, monetization, legal/store attestations, and final go/no-go.
- **Hermione — PM and integration owner:** board health, scope, dependency graph, sprint planning, risk register, decisions, evidence, daily reporting, acceptance gates, backlog integrity, and release coordination.
- **kidplayresearch — research/design worker:** benchmark evidence, product matrices, UX analysis, accessibility guidance, and design synthesis. Model: GPT-5.6-Luna, reasoning maximum.
- **kidplayengineer — engineering/QA worker:** Compose architecture, Dev Lab, TDD, integration, builds, test automation, performance, and release evidence. Model: GPT-5.6-Luna, reasoning maximum.

### Cadence

- Continuous Kanban flow with two-week planning horizons.
- Work-in-progress limit: two active worker cards, normally one research/design and one engineering/QA card.
- Daily PM check-in delivered at 18:00 America/Los_Angeles.
- Weekly backlog and risk review every Monday within the daily check-in.
- Sprint review and next-sprint selection every second Monday.
- Owner gates remain blocked until the requested evidence or decision arrives.

### Definition of Ready

A card is ready only when it states:

- purpose and user outcome;
- exact scope and exclusions;
- source files or evidence inputs;
- dependencies;
- acceptance criteria;
- verification command or owner test;
- required skill(s), where applicable.

### Definition of Done

A card is done only when:

- every acceptance criterion is satisfied;
- implementation is exercised, not merely drafted;
- tests and relevant quality checks pass;
- screenshots/builds/reports are stored at durable paths;
- result evidence is added to the card;
- product-facing copy has completed the required review path;
- code changes are reviewed, committed, and pushed unless intentionally held;
- owner-gated physical testing is explicitly recorded when required.

## 5. Stage gates

### G0 — Program control established

- Canonical board active.
- Two specialist profiles configured.
- Daily check-in scheduled.
- Program charter, backlog, risk register, and evidence locations established.

### G1 — Evidence baseline accepted

- Current product/code/design baseline audited.
- Benchmark evidence set includes official sources plus owner-provided captures.
- Capability and appearance matrices completed.
- Initial product risks and technical debt ranked.

### G2 — Direction selected

- Three materially distinct original directions produced.
- Recommended direction includes decision rationale.
- Owner selects or amends one direction.
- Core family moments and non-negotiable capabilities confirmed.

### G3 — Design system validated

- `DESIGN.md` defines color, typography, spacing, shape, iconography, illustration, motion, navigation, state, responsive, and accessibility rules.
- `DESIGN.md` defines the fox guide’s role, expression/state vocabulary, visual placement, animation, offline audio/caption behavior, interruption rules, repetition limits, accessibility alternatives, and parent controls.
- The interaction system specifies prompt, selection, correct, incorrect, retry, transition, completion, and next-step states, including rapid-tap and duplicate-navigation protection.
- Token contrast and semantic roles validated.
- Dev Lab gallery covers real content, long labels, large text, narrow/wide layouts, reduced motion, empty/loading/error/success/completion states, and fox-guided introduction/demonstration/acknowledgment/celebration/redirection states.
- Owner accepts the component language on a physical device.

### G4 — Vertical slice accepted

- Complete core loop is functional in production code.
- Automated tests, screenshot review, accessibility checks, and physical-device review pass.
- Completion/next-step behavior is clear.
- The fox guide is exercised across introduction, demonstration, acknowledgment, correction, completion, and next-step moments without obscuring content or blocking progress when audio or motion is disabled.
- No regression to privacy/offline behavior.

### G5 — Full product migration accepted

- Remaining screens use the approved system.
- Content library and guided steps meet quality targets.
- Navigation, favorites, recents, timers, filters, parent/settings, and safety surfaces are coherent.
- Performance and resilience targets pass.

### G6 — Release candidate accepted

- Family/closed-test blockers resolved.
- Privacy policy, SDK inventory, Data safety answers, permissions, store listing, and shipped behavior agree.
- Signed release artifact, rollback plan, and verification evidence exist.
- Owner records public-release approval.

## 6. Workstreams and backlog coverage

### A. Product research and competitive evidence

- Pok Pok: calm interaction, tactile feedback, visual density, and Apple design case study.
- PBS KIDS Games: safe child navigation and recognizable discovery.
- Sago Mini World: visual browsing and creative-play presentation.
- Khan Academy Kids: onboarding, parent/child separation, structured library, breadth, and progression.
- Duolingo: navigation economy, character system, motivation, and animation restraint relevant to KidPlay.
- Bible App for Kids: animated storytelling and child-readable interaction.
- My Very Hungry Caterpillar: tactile interaction mechanics.
- Hooked on Phonics: character and animation direction.
- Material 3, Android adaptive-layout guidance, Android core app quality, accessibility guidance, and Google Play family policy as foundations rather than product identity.
- At least one high-quality adult parent utility for rapid scanning and task completion.

For each product, capture first use, home, browse/discovery, detail, active session, completion/next step, parent/settings, empty/error states, navigation depth, density, copy burden, motion, sound, operator, safety cues, monetization, and offline behavior where observable.

### B. Capability gap matrix

Score KidPlay and references for:

- first-use comprehension;
- choosing by available time;
- energy/noise/mood;
- participant count and age mix;
- materials and preparation burden;
- search, filters, categories, favorites, and recents;
- guided setup and play steps;
- completion and next activity;
- parent controls and child handoff;
- offline behavior;
- progress/history/achievements only where beneficial;
- audio, animation, visual instructions, and reduced motion;
- content breadth, depth, replay value, and quality;
- safety, trust, privacy, and accessibility.

Every proposed feature receives a value/complexity/privacy/maintenance decision. Mainstream prevalence alone is not approval.

### C. Visual and interaction system

Produce three original directions:

1. Calm illustrated family system.
2. Structured family utility system.
3. Bold activity system.

The working hypothesis is a controlled combination of calm illustrated visuals and adult-speed utility; this is not final until G2.

Define:

- brand and semantic colors;
- typography family, weights, sizes, and line heights;
- spacing scale and layout grid;
- card, surface, border, and elevation rules;
- icon sizing, stroke, and semantic rules;
- illustration style, dimensions, crops, and asset QA;
- activity-card image treatment;
- button hierarchy;
- navigation and back behavior;
- empty, loading, error, success, completion, and next-step states;
- motion duration/easing, sound policy, haptics policy, and reduced motion;
- fox-guide behavior: introduction, demonstration, acknowledgment, celebration, gentle correction, placement, expression states, animation, offline audio, captions, replay, mute, repetition limits, interruption priority, and fallback behavior;
- interaction discipline: one clear task, one obvious primary action, immediate specific feedback, correction without lost progress, controlled transition pacing, explicit completion, and safe next-step choices;
- 48 dp minimum touch targets unless a stronger requirement applies;
- contrast, large-text, screen-reader, focus-order, narrow-screen, tablet, foldable, and landscape behavior.

### D. Dev Lab validation

Create and exercise:

- home variants;
- occasion/moment selection cards;
- category tiles and activity cards;
- detail pages and guided steps;
- timer/session controls;
- completion and next-step states;
- empty/loading/error/success states;
- fox-guide state gallery covering introduction, demonstration, acknowledgment, celebration, incorrect/incomplete redirection, muted audio, captions, reduced motion, replay, interruption, and repeated-tap stress;
- parent/settings surfaces;
- long-label and large-font cases;
- 320 dp, typical phone, and expanded-width layouts;
- dark/low-light treatment only if retained;
- reduced-motion behavior;
- semantics, focus order, TalkBack labels, and touch targets.

### E. Production vertical slice

Test the core loop through high-value entry moments:

- We have five minutes.
- We need something calm.
- We have multiple children.
- We want movement.
- We want almost no preparation.

The selected slice must expose setup/material burden before play, provide complete instructions, support completion, and offer a clear next action. It must also exercise the fox as a functional guide during introduction, rule demonstration, choice acknowledgment, completion celebration, and gentle mistake redirection, while preserving an equally understandable muted/reduced-motion path.

### F. Architecture and engineering quality

- Decompose oversized Compose surfaces and isolate design-system components.
- Preserve state, navigation, offline content loading, favorites, recents, timers, and feedback behavior.
- Add previews and tests around each reusable component.
- Maintain deterministic bundled-content validation.
- Test back navigation, process recreation where practical, rotation, narrow/wide windows, font scaling, dark/low-light mode if present, and reduced motion.
- Model interaction as explicit prompt, input-ready, selected, evaluating, correct, incorrect, retry, transitioning, completed, and next-step states. Reject duplicate submissions/navigation during transitions and verify recovery under rapid or repeated taps.
- Keep bundled guide audio lifecycle-safe across navigation, backgrounding, interruption, process recreation where practical, mute, and replay. Essential instructions and feedback must never depend on audio alone.
- Establish screenshot/golden strategy where stable and maintainable.
- Track startup, interaction responsiveness, memory, APK size, and regressions.
- Keep manifests and dependency/SDK inventory free of unapproved permissions or telemetry.

### G. Content professionalism

- Audit every active activity for title, summary, age fit, time, energy, materials, setup, full play steps, safety, parent notes, and replay variations.
- Separate user-supplied and assistant-created prompts.
- Preserve protected user content during trimming.
- Assess breadth by age, duration, energy, materials, participant count, indoor/outdoor, quiet/bedtime, movement, creative play, conversation, and replay value.
- Expand only after evidence identifies a real gap.
- Run safety and language QA before release.

### H. Verification and release

- Unit, Compose UI, integration, static analysis, dependency, content-schema, and build checks.
- Emulator screenshot matrix plus physical Pixel 8 Pro/other-device owner checks.
- TalkBack, large text, reduced motion, contrast, focus order, and touch-target checks.
- Family test and closed-test feedback triage.
- Privacy policy, SDK/data inventory, Data safety, family policy, signing, store assets, listing, support, rollout, rollback, and monitoring.
- Version increment and changelog after every feedback batch.
- APK and Drive read-back SHA-256 verification.

## 7. Eight-month roadmap

Roadmap dates are planning boundaries, not permission to skip gates.

- **2026-08-24 to 2026-09-13 — Mobilize and baseline:** G0; current-state audit; research intake; initial matrices; risk baseline.
- **2026-09-14 to 2026-10-11 — Benchmark and define:** benchmark corpus; capability decisions; three directions; owner selects direction; G1/G2.
- **2026-10-12 to 2026-11-22 — Design system and Dev Lab:** tokens, components, states, responsive behavior, motion, accessibility; G3.
- **2026-11-23 to 2027-01-03 — Vertical slice:** implement, test, iterate, physical-device review; G4.
- **2027-01-04 to 2027-02-14 — Full migration and content quality:** remaining flows, architecture cleanup, content breadth/quality; G5 candidate.
- **2027-02-15 to 2027-03-14 — Hardening:** accessibility, resilience, performance, security/privacy consistency, broad device matrix.
- **2027-03-15 to 2027-04-04 — Closed-test release candidate:** family/closed test, blocker fixes, store package.
- **2027-04-05 to 2027-04-24 — Public-release decision:** final evidence audit, staged rollout/rollback readiness, owner go/no-go; G6.

## 8. Current owner-input request

Please provide screenshots or short recordings, when convenient, for as many reference apps as you can access. Start with one to three clips rather than attempting the entire set.

Priority capture sequence:

1. Home screen and first-use navigation.
2. Browse/discovery and content detail.
3. Active activity/session interaction.
4. Completion or next-step screen.
5. Parent/settings screen.

Also provide matching current KidPlay captures for Home, browse, activity detail, active session, completion/next step, and settings/about. Recordings should show taps and transitions. No child names, faces, voices, account identifiers, email addresses, payment information, or other personal data should appear.

This request is a human gate, not a blocker for current code audit, official-source research, backlog creation, or technical preparation.

## 9. Risk register

| ID | Risk | Response | Gate |
|---|---|---|---|
| R1 | Visual polish masks capability gaps | Maintain separate matrices and product decisions | G1 |
| R2 | Reference work drifts into cloning | Extract principles; originality review before approval | G2/G3 |
| R3 | Large centralized Compose file slows iteration | Architecture audit and staged component extraction | G3/G4 |
| R4 | Owner captures arrive slowly | Continue official-source research and technical audit; send reminders | G1 |
| R5 | Automated tests miss physical feel | Require owner physical-device evidence | G3/G4/G6 |
| R6 | Scope expands before central loop works | Vertical slice before broad migration | G4 |
| R7 | Child/privacy exposure enters tooling or testing | Data minimization, no identifying captures, privacy checks | All |
| R8 | Generated assets become inconsistent | Nano Banana-only pipeline plus durable prompts and QA | G3/G5 |
| R9 | Content breadth grows without quality | Evidence-led expansion and content QA gates | G5 |
| R10 | Worker concurrency causes code conflicts | Maximum two active cards; serialize shared-file implementation | All |
| R11 | Release docs diverge from app behavior | Cross-check manifest, SDK inventory, policy, listing, and artifact | G6 |
| R12 | Eight-month schedule pressures premature approval | Gates control progression; dates are forecasts | All |

## 10. Evidence locations

- Program control: `docs/planning/`
- Product/design: `DESIGN.md`, `docs/product/`, and `docs/design/`
- Research: `docs/research/`
- Dev Lab and verification: `docs/testing/`
- Feedback: `docs/testing/feedback/`
- Launch: `docs/launch/`
- Build artifacts: `/mnt/cyberforgex-ai/KinPlay/apk-drops`
- Canonical status/dependencies: Hermes Kanban board `kinplay`
