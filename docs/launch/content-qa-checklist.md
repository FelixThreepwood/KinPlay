# KinPlay content and safety QA checklist

Status: ready for use on every content revision
Applies to: canonical JSON, runtime JSON, review exports, tests, and packaged APK assets

This checklist prevents schema-valid but incomplete or unsafe content from reaching normal app flows.

## A. Intake and provenance

- [ ] Content request has an authorized source or product decision.
- [ ] Stable content ID is unique and preserved across revisions.
- [ ] Source, authoring status, reviewer, and review date are recorded.
- [ ] No child name, exact birthdate, photo, audio, video, precise location, credential, or private family detail appears in the content, fixture, export, or filename.
- [ ] Original or licensed text provenance is recorded for any non-generated source.
- [ ] Draft or unresolved content is explicitly marked `draft` or `retired` and is excluded from normal app queries.

## B. Schema and representation parity

- [ ] Canonical JSON validates against `content/kinplay-content.schema.json`.
- [ ] Canonical and runtime JSON files are byte-identical.
- [ ] IDs are unique.
- [ ] Every schema-backed field is parsed by Kotlin.
- [ ] Every reviewed field is rendered where a parent can use it.
- [ ] Review exports include the same active items and metadata as the canonical seed.
- [ ] The APK-packaged asset matches the validated runtime asset byte-for-byte.
- [ ] No placeholder tokens remain outside the intended Mad Libs template fields.

Recommended checks:

```bash
python3 /home/phantomatic/.hermes/scripts/kinplay_offline_validate.py --mode checkpoint
python3 /home/phantomatic/.hermes/skills/software-development/mobile-app-beta-operations/scripts/validate-json-backed-android-beta.py \
  --schema content/kinplay-content.schema.json \
  --canonical content/seed/kinplay_seed_v1.json \
  --runtime app/src/main/assets/kinplay_seed_v1.json \
  --apk app/build/outputs/apk/debug/app-debug.apk \
  --asset assets/kinplay_seed_v1.json
```

## C. Ready-to-use semantics

For every active activity:

- [ ] Title is recognizable and parent-facing.
- [ ] Summary explains the activity in one concise sentence.
- [ ] Age range and duration are plausible.
- [ ] Energy level is accurate.
- [ ] Materials state `none` explicitly when no materials are needed.
- [ ] Setup steps explain the actual setup burden.
- [ ] Play steps contain the complete procedure.
- [ ] Any needed clues, patterns, destinations, scenarios, messages, recipes, folds, turns, or examples are supplied in the content.
- [ ] Parent notes explain supervision or adaptation without negative parent-state framing.
- [ ] Replay variations are actionable and do not require invention.
- [ ] Participant suitability and timed-session eligibility match the reviewed decision.

For every Mad Libs template:

- [ ] All required fields have labels and examples.
- [ ] Placeholder keys match the ordered field list.
- [ ] The resulting story is readable after substitution.
- [ ] The read-aloud note is present when the story needs facilitation guidance.
- [ ] The story contains no identifying or unsafe sample text.

For prompt libraries:

- [ ] Prompt IDs are unique.
- [ ] Category counts match the approved requirement.
- [ ] Duplicate and near-duplicate review is complete.
- [ ] Prompt order/randomization behavior is tested.
- [ ] Persisted progress cannot expose a retired or missing prompt.

## D. Safety review

- [ ] Parent supervision is stated where movement, small objects, food, outdoor play, or reading support requires it.
- [ ] Unsafe physical contact is absent.
- [ ] Scary, violent, sexual, political, medical, and public-sharing content is absent unless an explicit later product review authorizes a change.
- [ ] Safety warnings are retained or relocated according to the approved safety decision matrix.
- [ ] A safety warning is not removed only because a normal card should be compact.
- [ ] The activity remains appropriate for the stated age range.
- [ ] Materials, furniture, cords, stairs, pets, and fragile objects are addressed where relevant.
- [ ] The activity can be stopped safely when a family session becomes chaotic.

## E. UI and accessibility review

- [ ] The item appears only in intended categories and modes.
- [ ] Collapsed cards show title, concise summary, material burden, and reviewed descriptors before opening.
- [ ] Detail pages show setup and play steps without clipping at narrow widths or large font scales.
- [ ] Visible labels and accessibility labels agree.
- [ ] Interactive controls retain at least the project-tested touch-target size.
- [ ] Lock controls appear only for reviewed eligible content.
- [ ] Draft and retired content remain unreachable from Home, random selection, favorites, recents, direct detail lookup, and category lists.

## F. Regression and approval

- [ ] Focused content tests pass.
- [ ] Full JVM suite passes.
- [ ] Android test sources compile.
- [ ] Debug or release APK assembles as requested.
- [ ] Lint passes.
- [ ] `git diff --check` passes.
- [ ] A reviewer confirms the source diff, content diff, safety classification, and generated exports.
- [ ] The progress ledger records the exact evidence and remaining device limitation.

## Review record

| Field | Value |
|---|---|
| Content batch |  |
| Reviewer |  |
| Review date |  |
| Canonical seed commit |  |
| Schema/parity result |  |
| Safety result |  |
| Focused tests |  |
| Full tests |  |
| APK validation |  |
| Decision | Approve / revise / hold |
| Notes |  |
