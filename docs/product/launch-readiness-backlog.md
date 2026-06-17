# KinPlay Launch Readiness Backlog

## Purpose

This backlog turns the playable MVP and family device test plan into a post-MVP launch path. It is intentionally ordered so KinPlay does not add store, analytics, monetization, or growth surface area before the parent-led privacy and safety posture is ready.

## Launch principles

- Keep KinPlay parent-led and family-safe.
- Preserve the MVP default: no accounts, child profiles, camera, microphone, contacts, location, public sharing, ads, or purchases unless a later release explicitly changes that posture.
- Do not collect child-identifying data.
- Use tester IDs and age bands, not child names, exact birthdates, photos, audio, or video.
- Treat privacy policy, Play Console Data safety, and any analytics/crash reporting as one dependency chain.
- Prefer closed testing with trusted parent/caregiver testers before any public listing.

## Readiness phases

### Phase 0: Family APK validation

Goal: prove the app is installable, understandable, useful, and trusted before store work.

Exit criteria:

- Family device test plan is executed with 5-10 parent/caregiver testers.
- APK installs on at least 3 distinct Android devices.
- App launches offline on every installed device.
- No runtime permission prompts appear.
- At least 3 of 5 testers, or 5 of 10 testers, score `2` or higher on repeat-use.
- No high-severity confusion affects more than one tester.
- At least 2 testers request more content or describe a concrete repeat-use case.
- No tester reports inappropriate content or unexpected data collection.
- At least 80% of testers would trust KinPlay on a family device after reading the MVP privacy posture.

### Phase 1: Store-readiness foundation

Goal: prepare Play Console and public-facing materials without changing the app privacy posture.

Exit criteria:

- App identity, package name, release signing, versioning, and build process are documented.
- Privacy policy is drafted and hosted at a stable URL.
- Play Console app record exists in draft or internal-only state.
- Data safety answers match the shipped app behavior.
- Store screenshots, icon, feature graphic, short description, and full description are prepared.
- Closed testing plan is ready before inviting non-family testers.

### Phase 2: Closed testing

Goal: run a controlled Play Console closed test while preserving tester privacy.

Exit criteria:

- Closed testing track is configured.
- Tester recruitment, install instructions, feedback form, and data hygiene rules are ready.
- First signed test build is uploaded.
- Crash reporting decision is made before integrating any SDK.
- No analytics or monetization SDK ships unless privacy policy and Data safety disclosures are updated first.
- Closed-test findings are triaged into launch blockers, post-launch improvements, and content backlog.

### Phase 3: Public launch candidate

Goal: publish only after trust, content depth, and store compliance are strong enough.

Exit criteria:

- All launch blockers from closed testing are resolved.
- Screenshots and listing copy reflect the actual shipped app.
- Privacy policy, Data safety form, permissions, and SDK inventory are consistent.
- Content library is broad enough for repeat use.
- Monetization decision is explicit: none, paid app, subscription, in-app purchase, sponsorship, or later deferral.
- Release checklist is complete and reviewed.

## Backlog

### P0: Launch gates from family testing

#### LCH-001: Summarize family device test results

Owner: product/research

Depends on: `docs/testing/family-device-test-plan.md`

Tasks:

- Create a test results summary after 5-10 parent/caregiver tests.
- Record device install outcomes, offline launch results, confusion signals, repeat-use scores, content requests, and safety/trust feedback.
- Strip child names, photos, audio, video, exact birthdates, precise locations, and other child-identifying information before saving notes.
- Separate issues into launch blockers, usability improvements, and content requests.

Acceptance criteria:

- Summary includes tester count, device count, repeat-use target result, confusion target result, safety/trust target result, and recommended next phase.
- No raw child-identifying data is stored.

#### LCH-002: Create launch blocker triage list

Owner: product/engineering

Depends on: LCH-001

Tasks:

- Convert family test issues into a prioritized blocker list.
- Mark each item as `blocker`, `should-fix-before-closed-test`, or `post-launch`.
- Include exact affected screen, reproduction notes, severity, and proposed fix.

Acceptance criteria:

- No closed testing work begins with unresolved install, offline launch, trust, or high-severity confusion blockers.

### P1: Play Console setup

#### LCH-010: Establish Play Console app record

Owner: release/ops

Tasks:

- Create the Google Play Console app record for KinPlay.
- Confirm app name, default language, app/game classification, free/paid status, and package name.
- Keep app in draft/internal state until privacy policy and Data safety answers are ready.

Acceptance criteria:

- Play Console app exists but is not publicly published.
- Package name matches the Android app build configuration.
- Release owner can upload an internal or closed test build.

#### LCH-011: Document release signing and versioning

Owner: engineering/release

Tasks:

- Decide whether to use Play App Signing.
- Document upload key storage, key rotation owner, and recovery process.
- Add version code/name rules.
- Add commands for producing a signed release or closed-test build.

Acceptance criteria:

- A fresh operator can build the same release artifact from documented commands.
- Signing material is not committed to the repository.

#### LCH-012: Complete Play Console policy checklist

Owner: release/privacy

Tasks:

- Complete Content rating questionnaire.
- Complete Target audience and content declarations for a family/parent-led app.
- Complete App content declarations required by Play Console.
- Confirm whether KinPlay is enrolled in Designed for Families or intentionally not enrolled for the first release.

Acceptance criteria:

- Policy answers are saved in Play Console draft.
- Any family-policy risk is documented before closed testing expands.

### P1: Privacy policy and data safety

#### LCH-020: Draft and host privacy policy

Owner: privacy/product

Tasks:

- Draft a plain-language privacy policy for the current app behavior.
- State that the MVP/initial release does not require accounts and does not request camera, microphone, contacts, location, or similar permissions.
- State whether crash logs, analytics, purchase data, or support emails are collected. If none are collected, say that clearly.
- Include contact email for privacy questions.
- Host the policy at a stable public URL suitable for Play Console.

Acceptance criteria:

- Privacy policy URL is accessible without login.
- Policy matches the actual shipped app and SDK inventory.
- Policy avoids promising future behavior that the team may later change.

#### LCH-021: Create SDK and data inventory

Owner: engineering/privacy

Tasks:

- Inventory every dependency and SDK in the app.
- Record whether each component collects, transmits, stores, or shares user data.
- Confirm no accidental analytics, advertising, or permission-requesting SDK is present.

Acceptance criteria:

- Inventory is stored in project docs.
- Play Console Data safety answers can be traced to the inventory.

#### LCH-022: Complete Play Console Data safety form

Owner: privacy/release

Depends on: LCH-020, LCH-021

Tasks:

- Fill Data safety answers based on actual app behavior.
- Re-check answers after adding crash reporting, analytics, ads, purchases, or any network feature.

Acceptance criteria:

- Data safety form matches the privacy policy, Android manifest, and SDK inventory.
- Any data collection is explicitly justified and disclosed before release.

### P1: Screenshots, icon, and brand

#### LCH-030: Define KinPlay brand basics

Owner: design/product

Tasks:

- Choose final app name usage, tagline, tone, color palette, typography direction, and icon concept.
- Keep tone parent-facing, warm, safe, and low-pressure.
- Avoid visuals that imply unsupervised child use, public sharing, recording, or targeted advertising.

Acceptance criteria:

- Brand brief is documented.
- Listing copy and screenshots use consistent wording and visual style.

#### LCH-031: Produce launcher icon and adaptive icon assets

Owner: design/engineering

Tasks:

- Create foreground/background adaptive icon assets.
- Export required densities or vector sources.
- Test icon appearance on light/dark launchers and common Android shapes.

Acceptance criteria:

- App uses final icon assets instead of placeholders.
- Icon remains legible at launcher size.

#### LCH-032: Produce Play Store graphics

Owner: design/release

Tasks:

- Create phone screenshots for Home, Quick Play, Pick a Game, Mad Libs, Calm Down, and About/Safety.
- Create a feature graphic if Play Console requires it for the chosen release path.
- Ensure screenshots show real app UI and do not imply features not present.

Acceptance criteria:

- Store graphics are export-ready and stored under a documented asset path.
- Screenshots match the current release build.

#### LCH-033: Write Play Store listing copy

Owner: product/release

Tasks:

- Draft short description.
- Draft full description.
- Draft release notes for closed test and public launch candidate.
- Mention parent-led offline family play and avoid unsupported claims.

Acceptance criteria:

- Listing copy is ready for Play Console.
- Copy aligns with privacy policy and screenshots.

### P1: Closed testing

#### LCH-040: Design Play Console closed testing plan

Owner: product/release

Tasks:

- Define tester count, tester profile, inclusion/exclusion criteria, test duration, and feedback cadence.
- Decide whether testers are invited by email list, Google Group, or managed tester group.
- Reuse data hygiene rules from the family device test plan.
- Define go/no-go criteria for public launch candidate.

Acceptance criteria:

- Closed test plan can be sent to testers without additional explanation.
- Feedback asks for parent observations, not child-identifying data.

#### LCH-041: Prepare closed testing feedback form

Owner: product/research

Tasks:

- Create a feedback form with anonymous tester IDs.
- Capture install success, device model, Android version, first mode used, repeat-use, confusion, content requests, safety/trust, and crash reports.
- Add warning not to upload child names, photos, videos, audio, exact birthdates, or precise locations.

Acceptance criteria:

- Feedback form supports triage without collecting child-identifying data.

#### LCH-042: Upload first closed-test build

Owner: engineering/release

Depends on: LCH-010, LCH-011, LCH-020, LCH-022, LCH-040

Tasks:

- Build signed artifact.
- Upload to the closed testing track.
- Add release notes.
- Confirm testers can install through Google Play.

Acceptance criteria:

- At least one tester installs the closed-test build through Play Console.
- Build behavior matches privacy policy and Data safety answers.

### P2: Analytics and crash reporting

#### LCH-050: Decide crash reporting posture

Owner: engineering/privacy

Tasks:

- Compare no crash SDK, Firebase Crashlytics, and Sentry.
- Prefer the smallest useful telemetry surface.
- Document collected data, retention, opt-out behavior if any, and privacy policy impact.

Acceptance criteria:

- Decision is recorded before any SDK is added.
- If crash reporting is added, privacy policy and Data safety answers are updated first.

#### LCH-051: Add minimal crash reporting only if approved

Owner: engineering

Depends on: LCH-050

Tasks:

- Integrate chosen crash reporting SDK only after policy and Data safety updates are ready.
- Disable collection of unnecessary identifiers where supported.
- Verify no extra permissions are introduced.
- Test a non-production crash path if the provider supports it.

Acceptance criteria:

- Crash reports appear in provider dashboard for test builds.
- Android manifest has no unexpected permissions.
- Privacy policy, Data safety, and SDK inventory are updated.

#### LCH-052: Decide analytics posture

Owner: product/privacy

Tasks:

- Decide whether analytics are needed before public launch.
- If needed, define a minimal event set that avoids child-identifying data.
- Candidate events: app_open, mode_selected, activity_started, mad_lib_completed, calm_down_started, content_request_clicked.
- Do not collect child names, exact ages, free-text family details, location, contacts, or media.

Acceptance criteria:

- Decision is documented as `none for launch`, `crash-only`, or `minimal analytics`.
- Any analytics plan includes event names, properties, retention, and privacy disclosures.

### P2: Monetization

#### LCH-060: Choose monetization strategy

Owner: product/business

Tasks:

- Evaluate options:
  - no monetization for initial launch;
  - paid app;
  - one-time content pack purchase;
  - subscription for expanded content;
  - sponsorship/partnership later;
  - ads avoided by default for family trust.
- Consider family trust, Play policies, implementation complexity, support burden, and privacy impact.

Acceptance criteria:

- Monetization decision is recorded before public launch.
- If monetization is deferred, the release plan says so clearly.

#### LCH-061: Implement purchases only after trust validation

Owner: engineering/product

Depends on: LCH-060

Tasks:

- If in-app purchases or subscriptions are chosen, design entitlement model.
- Add purchase flow, restore purchases, error handling, and parental clarity.
- Update privacy policy, Data safety, Play Console declarations, screenshots, and listing copy.

Acceptance criteria:

- Purchases are not added to the first public launch unless explicitly approved.
- Purchase flow is tested before release.

### P2: Content expansion

#### LCH-070: Expand seed content pack for repeat use

Owner: content/product

Tasks:

- Use family and closed-test requests to prioritize new content.
- Add more no-materials activities, calm-down activities, age-specific activities, outdoor/movement activities, quiet/bedtime ideas, and Mad Libs templates.
- Keep content offline and schema-valid.
- Retire content with `status: retired` rather than deleting IDs after release.

Acceptance criteria:

- Expanded content pack has enough variety for multiple family sessions.
- Content validates against `content/kinplay-content.schema.json`.
- Safety tags and parent notes are complete.

#### LCH-071: Add content QA checklist

Owner: content/safety

Tasks:

- Create review checklist for age fit, materials, safety, parent readability, replay value, and policy-sensitive topics.
- Add reviewer initials/date fields if a separate review document is used.
- Reject scary, violent, sexual, political, medical, public-sharing, or unsafe-contact prompts.

Acceptance criteria:

- Every new content item can be reviewed consistently before shipping.

#### LCH-072: Define post-launch content cadence

Owner: content/product

Tasks:

- Choose a realistic content expansion cadence.
- Group content into small themed packs.
- Decide whether content remains local-only or whether a future remote content system is worth planning.

Acceptance criteria:

- Content roadmap is documented for the first 1-3 post-launch updates.
- Remote content is not assumed before privacy/security design exists.

### P3: Launch operations

#### LCH-080: Create release checklist

Owner: release/ops

Tasks:

- Add checklist for branch, version, signed build, smoke test, Play Console upload, listing review, privacy/Data safety review, screenshot review, staged rollout, rollback, and post-release monitoring.

Acceptance criteria:

- Release operator can follow the checklist without guessing.

#### LCH-081: Define support and contact flow

Owner: product/support

Tasks:

- Choose support email.
- Draft support response templates for install help, privacy questions, content concerns, and bug reports.
- Tell users not to send child names, photos, audio, or video.

Acceptance criteria:

- Play Store support contact and privacy policy contact are consistent.

#### LCH-082: Decide launch scope

Owner: product/business

Tasks:

- Choose first public geography/language.
- Decide whether launch is public, unlisted/limited, or staged rollout.
- Define rollback criteria.

Acceptance criteria:

- Launch scope is explicit before production rollout.

## Dependency map

- Closed testing requires: Play Console setup, release signing/versioning, privacy policy, Data safety answers, closed test plan, and signed build.
- Crash reporting requires: SDK/data inventory, privacy policy update, Data safety update, and explicit approval.
- Analytics requires: event plan, privacy policy update, Data safety update, and explicit approval.
- Monetization requires: product decision, policy review, Play Billing implementation if applicable, privacy/Data safety update, and listing copy update.
- Public launch requires: resolved launch blockers, final screenshots/listing, policy consistency, support contact, and release checklist.

## Suggested immediate next tasks

1. Run the family device test plan and produce LCH-001.
2. Create a launch blocker triage list from test results.
3. Draft privacy policy for the current no-account/no-permission/no-analytics build.
4. Create SDK/data inventory before adding crash reporting or analytics.
5. Define brand basics and replace placeholder icon assets.
6. Prepare closed testing plan and feedback form.
