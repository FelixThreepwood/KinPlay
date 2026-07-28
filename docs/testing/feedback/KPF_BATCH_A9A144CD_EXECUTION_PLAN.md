# KinPlay Batch A9A144CD Execution Plan

> **For Hermes:** Use subagent-driven-development skill to implement this plan task-by-task.

**Goal:** Estimate and dependency-order every canonical feedback item linked to `KP-BATCH-A9A144CD-D6AE-47E6-8C6F-4F14214377E4` into implementation, validation, and final beta-delivery batches capped at 90 active minutes.

**Architecture:** Preserve the current offline Kotlin/Jetpack Compose application, JSON-backed content model, SharedPreferences settings, feedback store, child-handoff-lock module, and dedicated Would You Rather module. Implement shared model/schema foundations before dependent Compose surfaces, use test-driven development for each slice, and isolate product-safety and Gemini approval gates from active coding time.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, Navigation Compose, JSON Schema, Android SharedPreferences, JUnit, AndroidX Compose tests, Gradle, Google Gemini Nano Banana for visual assets.

---

## 1. Scope and evidence

The batch registry and human-readable punchlist agree on 22 canonical items:

- Reopened: `KPF-0006`, `KPF-0008`, `KPF-0009`, `KPF-0018`, `KPF-0019`
- New: `KPF-0021` through `KPF-0037`

Source evidence:

- `docs/testing/feedback/PUNCHLIST.md`, intake heading and KPF records
- `docs/testing/feedback/punchlist.json`, batch `canonicalItemIds`, `existingCanonicalItemIds`, `newCanonicalItemIds`, and source-note mapping
- Current application source and tests under `app/src/`
- Git commits `5dad257` and `2e1b789`
- Hermes session telemetry associated with the 0.4.0-beta1 and 0.5.0-beta1 implementation passes

The two registries identify exactly the same ordered set. `KPF-0020` is not linked to this batch and is therefore excluded.

## 2. Estimation method and assumptions

### Definitions

- **Tokens** are estimated aggregate uncached working-input tokens consumed during implementation and focused validation. Cache reads are excluded because repeated context varies sharply by agent topology. Output tokens are also excluded from the per-item figure so the estimate remains comparable with recorded input telemetry.
- **Control turns** are assistant/tool cycles: one reasoning/action response followed by one or more file, terminal, visual, review, or test calls. They are not raw delegated model invocations.
- **Active minutes** include repository inspection specific to the item, failing-test creation, implementation, focused unit or Compose validation, and immediate correction. They exclude final release packaging, the complete regression matrix, APK compilation, Git publication, distribution, family-device retesting, and waiting for external approval.
- Item estimates are standalone planning allocations. Shared context reduces repeated discovery, but the batch schedule keeps the full 1,395-minute point total as contingency rather than subtracting speculative savings.

### Historical calibration

| Prior pass | Git evidence | Active implementation window | Recorded execution telemetry |
|---|---|---:|---|
| `5dad257` | KPF-0001–KPF-0010; 17 files; +2,255/-337 | 96.90 min | 240 model/API calls, 591 tool calls, 1,554,433 uncached input, 146,881 output |
| `2e1b789` | KPF-0011–KPF-0020; 52 files; +10,994/-195 | 336.83 active min across three interrupted windows | 694 model/API calls, 1,305 tool calls, 4,350,819 uncached input, 415,245 output |

Observed rates were approximately 9.69 active minutes per KPF for the compact 0.4 pass and 33.68 active minutes per KPF for the broader 0.5 pass. The new batch contains several cross-cutting interaction, navigation, session-state, safety, orientation, and visual-approval requirements. Estimates therefore use the slower 0.5 pass as the stronger baseline, then add uncertainty for items requiring new architecture or external decisions.

Raw historical tool activity was approximately 3.9–6.1 calls per active minute because the prior runs used delegated-agent fan-out. The control-turn estimates below intentionally do not relabel all raw calls as conversational turns.

### Assumptions

1. Existing source behavior at `2e1b789` remains the implementation baseline. Confidence: 98%.
2. Focused JVM and Compose tests can run without a new dependency migration. Confidence: 90%.
3. A physical crash trace for KPF-0037 is not yet available. The strongest current suspect is `applicationContext.startActivity(...)` without `FLAG_ACTIVITY_NEW_TASK`, with only `ActivityNotFoundException` caught. Confidence in that diagnosis: 85%; the estimate retains diagnostic contingency.
4. KPF-0032 will receive a reviewed retain/relocate/remove decision matrix before user-visible safety text is changed. Confidence that this gate is required: 98%.
5. KPF-0019 uses only the Google Gemini Nano Banana visual path, preserves untouched masters, and passes an independent visual approval gate between generation and Android integration. Confidence: 99%.
6. Product decisions for KPF-0036 will stage or clearly label Account rather than invent an account system, because the current product specification excludes accounts. Confidence: 85%.
7. Independent spec, safety, and visual reviewers can resolve normal revision gates; only a genuinely unresolved product conflict pauses for user direction. External waiting time is not counted against a 90-minute implementation batch. Confidence: 90%.

Overall estimate confidence: 68%. Estimates are strongest for copy and existing-component revisions and weakest for safety treatment, session architecture, menu scope, crash reproduction, and icon approval.

## 3. Per-KPF estimates

Token ranges and control-turn ranges are deliberately broad. The active-minute column is the point allocation used by the batch arithmetic.

| KPF | Requirement | Tokens | Control turns | Active min | Affected areas | Dependencies | Uncertainty / confidence |
|---|---|---:|---:|---:|---|---|---|
| KPF-0006 | Minimize and compact Home | 100K–160K | 7–10 | 50 | `MainActivity.kt` Home layout; `KinPlayLogicTest`; Home Compose tests | 0033, 0034, 0035, 0036 | Medium; responsive visual acceptance. 70% |
| KPF-0008 | Participant-suitability labels | 120K–180K | 8–12 | 55 | Content schema and both seed copies; `KinPlayItem`; card/detail rendering; content tests | 0021, 0022; shares metadata foundation with 0026 | Medium-low; values need review beyond Quality Time. 80% |
| KPF-0009 | Games-and-activities scope and terminology | 50K–90K | 4–6 | 25 | Home/navigation copy; library helpers; product docs; terminology tests | 0031, 0035 | Low; content types already ship. 90% |
| KPF-0018 | Revise child handoff lock mode | 140K–220K | 10–14 | 70 | `lock/ChildHandoffLock*.kt`; detail/Would You Rather integration; lock tests | 0024, 0025, 0026 | Medium; existing lock reduces scope, device semantics remain. 75% |
| KPF-0019 | Revise original Gemini launcher icon | 240K–400K | 12–18 | 110 | Gemini masters; `res/drawable-nodpi`; adaptive-icon resources; asset manifest; originality QA | Gemini brief, approval gate, 0020 compatibility | High; generation quality and approval are nondeterministic. 45% |
| KPF-0021 | Compact two-column card hierarchy | 140K–220K | 9–13 | 65 | `ContentCard`; `CompactCardDetails`; responsive Compose tests | 0008 and 0022 data/copy first | Medium; narrow screens and large text. 75% |
| KPF-0022 | Description on every collapsed card | 100K–160K | 7–10 | 50 | Summary inventory in both seeds; collapsed-card rendering; content/card tests | 0021 layout; retain setup/material preview | Low-medium; concise-copy threshold needs judgment. 85% |
| KPF-0023 | Remove negative parent-state copy | 70K–110K | 5–8 | 35 | Both seed copies; neutral-copy regression test | Preserve KPF-0010 ready-made help | Low; current scan found one direct offender. 95% |
| KPF-0024 | Compact lock/key emoji controls | 90K–140K | 6–9 | 40 | Lock control composable, semantics labels, progress state, Compose tests | 0018 lock state contract | Low-medium; exact state wording and emoji rendering. 85% |
| KPF-0025 | Clear locked content and tap-only guidance | 110K–170K | 8–11 | 50 | Lock overlay/input interception; transient guidance state; lock tests | 0018 and 0024 | Medium; event interception must preserve recovery. 75% |
| KPF-0026 | Per-game lock eligibility | 130K–210K | 9–13 | 60 | Content schema/seeds/parser; route eligibility; detail/Would You Rather tests | Metadata foundation before 0018 integration | Medium-high; every activity needs review. 65% |
| KPF-0027 | Compact note-emoji feedback control | 70K–110K | 5–8 | 35 | `feedback/FeedbackUi.kt`; detail feedback Compose test | Preserve existing capture context and lock suppression | Low-medium; scope across eligible surfaces. 85% |
| KPF-0028 | Default Would You Rather to landscape | 100K–160K | 7–10 | 50 | Route/activity orientation controller; lifecycle restoration; WYR tests | Existing WYR route; accessibility/device-rotation review | Medium-high; Android multi-window/rotation behavior. 65% |
| KPF-0029 | Start action and timed session flow | 220K–360K | 12–18 | 130 | Session domain/state; detail UI; routes; eligible-game rollout; tests | 0030 model and precedence first | High; broad inventory and completion semantics. 50% |
| KPF-0030 | Default rounds and per-game overrides | 220K–340K | 12–18 | 120 | `AppSettings`; repository/codec; Settings UI; detail overrides; session tests | Foundation for 0029 | High; precedence and one-shot override behavior need specification. 55% |
| KPF-0031 | Canonical game-view terminology | 40K–70K | 3–5 | 25 | Product spec; test plan; test names/comments; code-to-product term map | Precedes card/session naming | Low-medium; code symbol renames are intentionally optional. 85% |
| KPF-0032 | Remove normal-surface safety labels/copy safely | 220K–360K | 12–18 | 110 | Safety inventory; `MainActivity.kt`; seeds/schema; content tests; Safety/Privacy docs | Product/safety decision gate; 0036 destination | Very high; unconditional deletion is prohibited. 35% |
| KPF-0033 | Remove nonessential descriptive copy | 110K–180K | 8–12 | 60 | Home/list/detail copy; possibly feedback/settings; copy-absence tests | 0006 and 0034; safety boundary from 0032 | Medium-high; “nonessential” needs classification. 65% |
| KPF-0034 | Compact graphical Home shortcuts | 150K–240K | 10–14 | 70 | Home shortcut component; platform/custom icons; accessibility tests | Gemini visual workflow before design; 0035 labels; 0036 nav policy | Medium-high; visual brief and duplicate entry points. 65% |
| KPF-0035 | Shorten Home shortcut labels | 50K–80K | 4–6 | 25 | Home constants/copy; navigation and accessibility tests | 0009 terminology; final 0034 component | Low. 95% |
| KPF-0036 | Top-right application menu | 180K–280K | 10–16 | 85 | Navigation routes; Home app bar/menu; Settings/Account/About/Safety screens; UI tests/docs | Account-scope decision; 0032 safety destination; 0034 duplicate controls | High; route scope changes with product decisions. 50% |
| KPF-0037 | Fix Send now crash | 180K–280K | 10–16 | 75 | `FeedbackStore.kt`; `FeedbackUi.kt`; launcher boundary; feedback unit/Compose tests | Must precede broader feedback UI regression | High until trace/reproduction confirms cause. 65% |

### Per-item arithmetic

- Item count: **22**
- Estimated uncached input: **2.830M–4.520M tokens**
- Estimated control turns: **178–265**
- Point active implementation-and-focused-validation time: **1,395 minutes = 23 hours 15 minutes**

These sums were computed directly from all 22 rows. No KPF is counted twice in the point total even when its work is split across batches.

## 4. Dependency order

1. Fix KPF-0037 first because it blocks reliable tester feedback handoff.
2. Establish KPF-0031 terminology and the KPF-0032 safety decision matrix before creating new metadata and navigation labels.
3. Build the shared metadata/content foundation for KPF-0008, KPF-0022, and KPF-0026 before card and lock UI work.
4. Complete the KPF-0021 card layout only after the descriptors and descriptions it must render are stable.
5. Settle Home labels/copy and menu architecture before finalizing graphical controls and compactness acceptance.
6. Add the KPF-0036 Safety and Privacy destination before relocating any safety information under KPF-0032.
7. Implement KPF-0026 eligibility before final lock behavior so the revised control appears only on reviewed games.
8. Implement KPF-0030 settings/session precedence before KPF-0029 Start and timed-session rollout.
9. Split KPF-0019 at the human approval gate: generate and review first, then integrate only the approved Gemini master.
10. Run the complete regression, packaging, GitHub, checksum, shared-drive, and Google Drive workflow only after every KPF slice passes its focused gate.

## 5. Execution batches

Every batch includes a test-driven cycle: add or revise a focused failing test, verify the expected failure, implement the minimum coherent slice, run focused tests, run affected-module unit tests, and inspect the diff. Each batch has at least five minutes reserved inside its allocation for correction and revalidation.

### B0 — Feedback Send now crash blocker — 75 minutes

**KPF allocation:** KPF-0037 (75)

**Work:** Reproduce with a testable context/intent launcher boundary; capture or simulate the non-Activity-context path; add flags and exception handling required by Android; preserve unsent notes and Copy batch fallback; add unit and Compose regression tests.

**Validation:** `FeedbackLogicTest`, focused feedback Compose test, then `testDebugUnitTest` for the feedback package.

**Gate:** Do not infer final cause solely from the current source suspicion if a crash trace contradicts it.

### B1 — Vocabulary contract and safety decision matrix — 70 minutes

**KPF allocation:** KPF-0031 (25), KPF-0032 (45)

**Work:** Record collapsed card, expanded card, and details page terminology; map current Kotlin components to those terms; inventory every user-visible safety tag/instruction; classify each as retain, relocate, collapse, or remove; encode regression expectations before UI deletion.

**Validation:** Documentation consistency checks plus focused content-test fixtures for protected warnings.

**Gate:** KPF-0032 code removal cannot proceed until an independent safety/spec reviewer approves the classification. Uncertain warnings default to retained or relocated, never silently deleted.

### B2 — Card metadata and concise-description foundation — 85 minutes

**KPF allocation:** KPF-0008 (35), KPF-0022 (50)

**Work:** Review missing/general participant suitability; update schema, model, parser, both byte-identical seeds, and summary copy where needed; add real-content regression tests for every active card.

**Validation:** JSON Schema, canonical/packaged byte parity, content invariant tests, parser tests.

### B3 — Responsive two-column card implementation — 85 minutes

**KPF allocation:** KPF-0021 (65), KPF-0008 (20)

**Work:** Refactor collapsed and expanded card rows once; keep title/summary left, compact participant/duration/age descriptors trailing; preserve expansion, favorite, Open, materials, and setup behavior; add narrow-width and large-font checks.

**Validation:** Card unit helpers and Compose instrumentation tests for collapsed/expanded states.

### B4 — Home vocabulary and shortcut labels — 70 minutes

**KPF allocation:** KPF-0006 (20), KPF-0009 (25), KPF-0035 (25)

**Work:** Establish final Home information hierarchy; normalize games-and-activities language; set `Random game` and `All games and activities`; update accessibility labels and tests.

**Validation:** `KinPlayLogicTest` and focused Home Compose assertions.

### B5 — Home copy reduction and compactness closure — 90 minutes

**KPF allocation:** KPF-0033 (60), KPF-0006 (30)

**Work:** Classify Home/list/detail explanatory copy; remove the reported subtitle and other nonessential text while retaining functional, accessibility, privacy, legal, and safety content; close compact-Home acceptance at representative widths.

**Validation:** Copy-absence tests, visible-action tests, 48-dp target checks, representative Compose previews/instrumentation.

### B6 — Compact graphical Home shortcuts — 70 minutes

**KPF allocation:** KPF-0034 (70)

**Work:** Load the required Gemini visual workflow before visual design; select platform symbols or approved Gemini assets; remove shortcut subtext; implement compact accessible controls without flattening the interface into an image.

**Validation:** Accessible labels, touch targets, contrast, navigation, narrow/large-font layout, packaged-resource presence when custom assets are used.

### B7 — Home application menu and destinations — 85 minutes

**KPF allocation:** KPF-0036 (85)

**Work:** Add the top-right three-line menu; route Settings, staged Account, About the app, and Safety and Privacy; resolve duplicate Settings entry points intentionally; document that no account system exists unless product scope changes.

**Validation:** Menu semantics and destination navigation Compose tests; back-stack labels; no dead destinations.

### B8 — Neutral support copy and approved safety treatment — 90 minutes

**KPF allocation:** KPF-0023 (35), KPF-0032 (55)

**Work:** Replace negative parent-state wording with neutral `Clues and suggestions`; implement the approved safety classification across normal play surfaces without deleting required warnings; relocate reviewed information to Safety and Privacy where appropriate.

**Validation:** Neutral-copy scan, protected-warning content tests, screen-level assertions for removed labels and retained required notices.

### B9 — Safety closure, compact feedback control, and lock icon states — 85 minutes

**KPF allocation:** KPF-0032 (10), KPF-0027 (35), KPF-0024 (40)

**Work:** Finish KPF-0032 cross-surface regression; replace the text feedback control with an accessible compact note control; implement lock/key state icons, three-second action labels, and progress semantics.

**Validation:** Feedback context tests, lock-state semantics tests, screen-reader labels, touch-target checks.

### B10 — Lock eligibility metadata and integration foundation — 90 minutes

**KPF allocation:** KPF-0026 (60), KPF-0018 (30)

**Work:** Define and review lock eligibility for every active game; update schema/seeds/parser; expose one pure eligibility helper; wire eligible detail and Would You Rather routes while keeping noneligible screens free of lock controls.

**Validation:** Schema/parity checks, full active-content eligibility inventory, route/helper unit tests.

### B11 — Nonobscuring lock behavior and recovery — 90 minutes

**KPF allocation:** KPF-0018 (40), KPF-0025 (50)

**Work:** Preserve clear content while blocking unintended input; remove persistent locked clutter; show `Hold key for 3 seconds to unlock` only after a blocked tap; preserve Back guarding, accessibility activation, and reliable recovery.

**Validation:** State-machine unit tests and Compose tests for clear content, blocked controls, transient guidance, back handling, and unlock recovery.

### B12 — Would You Rather landscape lifecycle — 50 minutes

**KPF allocation:** KPF-0028 (50)

**Work:** Introduce a route-scoped orientation controller; enter landscape only for play, restore prior orientation on exit/disposal, and handle recreation/multi-window safeguards.

**Validation:** Controller unit tests, route Compose test, manual emulator orientation check when available.

### B13 — Session defaults, rounds, and precedence model — 70 minutes

**KPF allocation:** KPF-0030 (70)

**Work:** Specify default rounds, global duration, per-game next-session overrides, precedence, validation bounds, persistence keys, and immutable session configuration; implement codec/repository tests before UI.

**Validation:** `AppSettingsTest` plus new session-configuration tests covering defaults, persistence, invalid values, override isolation, and relaunch.

### B14 — Detail controls and Start foundation — 90 minutes

**KPF allocation:** KPF-0030 (50), KPF-0029 (40)

**Work:** Add eligible details-page duration/round controls and visible applied values; add Start near the top; construct a session from resolved defaults/overrides without mutating globals.

**Validation:** Details-page state/helper tests and Compose tests for selectors, Start, and precedence.

### B15 — Timed-session rollout and completion behavior — 90 minutes

**KPF allocation:** KPF-0029 (90)

**Work:** Define eligible-game inventory; route Start into a consistent session surface; implement timer/round progress, completion/exit, recreation state, and lock compatibility; leave ineligible reading-only content unchanged.

**Validation:** Session state-machine tests, eligible/ineligible route tests, focused Compose navigation and completion regression.

### B16 — Gemini icon revision and approval candidate — 55 minutes active

**KPF allocation:** KPF-0019 (55)

**Work:** Load `gemini-image-compositing` then `visual-asset-production`; inspect approved current masters; create a constrained Nano Banana revision preserving rounded curves while showing at least 80% of K and most of P; preserve untouched candidates and provenance; run visual and originality review.

**Validation:** Candidate count/identity, legibility at launcher scale, no third-party imitation, exact dimensions/format, SHA-256 and asset-manifest draft.

**Gate:** An independent visual reviewer selects only a candidate that passes the brief, originality, launcher-scale, and adaptive-safe-zone checks. If no candidate passes, revise within a bounded loop; escalate only after the review cap is exhausted.

### B17 — Approved icon integration and Android verification — 55 minutes

**KPF allocation:** KPF-0019 (55)

**Work:** Integrate only the approved Gemini master; produce explicit Android adaptive-icon derivatives while preserving the master; update Teal/Sunshine variants and manifest documentation; retain KPF-0020 switching compatibility.

**Validation:** Resource compilation, packaged-resource inspection, launcher-variant unit tests, icon-manifest hashes, emulator/device launcher review when available.

### B18 — Full regression, beta packaging, GitHub, and dual-drive publication — 90 minutes

**KPF allocation:** No additional KPF estimate; this is mandatory release overhead after all 22 items are implemented.

**Estimated usage:** 180K–280K uncached input tokens; 10–16 control turns.

**Work:** Run the complete unit, lint, content/schema/parity, and available instrumentation matrix; perform independent final review; version the next beta; compile the deployable APK; inspect package/version/signature/permissions; rename with the dated version convention; compute and verify SHA-256; commit and push GitHub; archive the previous local root build; publish the new sole-current APK to the shared-drive root and Google Drive; download or hash-verify the cloud copy before trashing the old cloud build; update release records.

**Validation:** No new test/lint failures, valid APK metadata and signature, matching local/cloud SHA-256, pushed commit visible on GitHub, exactly one current APK in each delivery root, and old local history preserved under the established archive paths.

## 6. Batch arithmetic

| Batch | Minutes | Running total |
|---|---:|---:|
| B0 | 75 | 75 |
| B1 | 70 | 145 |
| B2 | 85 | 230 |
| B3 | 85 | 315 |
| B4 | 70 | 385 |
| B5 | 90 | 475 |
| B6 | 70 | 545 |
| B7 | 85 | 630 |
| B8 | 90 | 720 |
| B9 | 85 | 805 |
| B10 | 90 | 895 |
| B11 | 90 | 985 |
| B12 | 50 | 1,035 |
| B13 | 70 | 1,105 |
| B14 | 90 | 1,195 |
| B15 | 90 | 1,285 |
| B16 | 55 | 1,340 |
| B17 | 55 | 1,395 |
| B18 | 90 | 1,485 |

Checks:

- KPF implementation batch count: **18**
- Mandatory final release batch count: **1**
- Total work batches: **19**
- KPF implementation and focused validation: **1,395 minutes = 23 hours 15 minutes**
- Full workflow including release: **1,485 minutes = 24 hours 45 minutes**
- Maximum batch: **90 minutes**
- Batches over 90 minutes: **0**

## 7. KPF coverage proof

| KPF | Batch allocation | Total min |
|---|---|---:|
| KPF-0006 | B4: 20 + B5: 30 | 50 |
| KPF-0008 | B2: 35 + B3: 20 | 55 |
| KPF-0009 | B4: 25 | 25 |
| KPF-0018 | B10: 30 + B11: 40 | 70 |
| KPF-0019 | B16: 55 + B17: 55 | 110 |
| KPF-0021 | B3: 65 | 65 |
| KPF-0022 | B2: 50 | 50 |
| KPF-0023 | B8: 35 | 35 |
| KPF-0024 | B9: 40 | 40 |
| KPF-0025 | B11: 50 | 50 |
| KPF-0026 | B10: 60 | 60 |
| KPF-0027 | B9: 35 | 35 |
| KPF-0028 | B12: 50 | 50 |
| KPF-0029 | B14: 40 + B15: 90 | 130 |
| KPF-0030 | B13: 70 + B14: 50 | 120 |
| KPF-0031 | B1: 25 | 25 |
| KPF-0032 | B1: 45 + B8: 55 + B9: 10 | 110 |
| KPF-0033 | B5: 60 | 60 |
| KPF-0034 | B6: 70 | 70 |
| KPF-0035 | B4: 25 | 25 |
| KPF-0036 | B7: 85 | 85 |
| KPF-0037 | B0: 75 | 75 |

Coverage checks:

- Expected KPFs: **22**
- Covered KPFs: **22**
- Missing KPFs: **none**
- Extra KPFs: **none**
- Duplicate minute allocation: **none**; split KPFs sum to their per-item point estimates
- Coverage-table total: **1,395 minutes**, equal to the per-item total and B0–B17 KPF allocations; B18 adds 90 minutes of release overhead without double-counting a KPF

## 8. Validation commands for execution

Focused commands will vary by batch. The final unit-validation boundary for each source-changing batch should include:

```bash
cd /home/phantomatic/projects/app-pass-rev/KinPlay
source scripts/android-env.sh
./gradlew --no-daemon testDebugUnitTest --rerun-tasks
```

After a debug APK exists, content-changing batches must also run the packaged-content validator with its required explicit paths:

```bash
python3 /home/phantomatic/.hermes/skills/software-development/mobile-app-beta-operations/scripts/validate-json-backed-android-beta.py \
  --schema content/kinplay-content.schema.json \
  --canonical content/seed/kinplay_seed_v1.json \
  --runtime app/src/main/assets/kinplay_seed_v1.json \
  --apk app/build/outputs/apk/debug/app-debug.apk \
  --asset assets/kinplay_seed_v1.json
```

Compose batches should run the focused instrumentation class when an emulator/device is available, followed by:

```bash
./gradlew --no-daemon connectedDebugAndroidTest
```

If no Android target is connected, that limitation must be recorded; it must not be presented as a passed device validation.

## 9. Execution boundary for this planning run

This report does not itself perform application implementation or publication. The next explicitly authorized all-KPF execution goal must, however, treat B18 as part of completion: implementation is not finished until the next beta has passed validation, been versioned and compiled, committed and pushed, checksum-verified, and published to both established delivery locations. The existing uncommitted edits in `PUNCHLIST.md`, `RETEST_LOG.md`, and `punchlist.json` are preserved.
