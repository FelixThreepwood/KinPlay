# KinPlay closed-testing plan

Status: operational draft ready for Play Console coordination
Target: parent/caregiver testing after physical APK validation
Current beta: `0.6.0-beta1` (version code `6`)

## Purpose

Validate installation, offline launch, parent comprehension, repeat use, content safety, and trust before broader distribution. The test is parent-led and must not collect child-identifying information.

## Entry gates

- [ ] Physical smoke test passes on at least three distinct Android devices.
- [ ] No install, launch, offline-content, permission, safety, or trust blocker is open.
- [ ] Privacy-policy draft is reviewed and hosted at a stable URL.
- [ ] SDK/data inventory is reconciled with the final APK.
- [ ] Play Console app record, target-audience, content-rating, and Data safety decisions are ready.
- [ ] Release signing and upload-key ownership are documented.
- [ ] Tester invitation and feedback instructions are ready.

## Tester population

Recruit 5-10 parent or caregiver testers.

Preferred coverage:

- children in the broad age bands 2-3, 4-5, and 6-8;
- at least two Android phone models;
- at least one tablet when available;
- at least two testers who receive no product walkthrough before the first session;
- anonymous tester IDs such as `T01` and `T02`.

Do not record child names, exact birthdates, photos, videos, audio, precise locations, contacts, or device-wide logs.

## Test phases

### Phase 1: Installation and offline smoke test

1. Install the signed test build.
2. Launch KinPlay.
3. Confirm that no runtime permission prompt appears.
4. Enable airplane mode.
5. Relaunch the app.
6. Open Random game, All games and activities, Mad Libs, Calm Down, About the app, and Safety and privacy.
7. Record installation, launch, offline, and permission results.

Pass criteria:

- Installation succeeds on the target device.
- KinPlay launches.
- Local content appears while offline.
- No permission prompt appears.
- Safety and privacy content is reachable.

### Phase 2: Unfamiliar-parent readiness

Use a tester who has not received a walkthrough. Start timing at a fresh launch.

1. Ask the tester to choose any game or activity.
2. Confirm that materials, setup, age, duration, and participant fit are visible before play.
3. Confirm that the tester can begin the first spoken or physical step without inventing content or requesting explanation.
4. Record elapsed time, chosen item, hesitation, and pass/fail.

Target: a ready choice within 30 seconds without outside coaching.

### Phase 3: Family sessions

Ask each tester to complete at least two sessions when possible.

Record:

- first mode and activity;
- completion or abandonment;
- confusion category and severity;
- whether the instructions were sufficient;
- safety or trust concern;
- whether the family used KinPlay again without a walkthrough;
- requests for more content or a concrete repeat-use situation.

### Phase 4: Follow-up and triage

Collect the structured feedback form without child-identifying information. Classify each finding as:

- launch blocker;
- should fix before broader testing;
- post-launch improvement;
- content request;
- observation with no action yet.

Do not change the app from a passive report alone. An authorized revision pass must use the complete implementation, test, review, versioning, publication, and checksum workflow.

## Go/no-go criteria

Proceed to broader closed testing only when:

- the required device install and offline targets pass;
- at least five testers complete a session;
- at least three of five, or five of ten, testers score 2 or higher for repeat use;
- no high-severity confusion affects more than one tester;
- at least two testers request more content or describe a concrete repeat-use case;
- no tester reports inappropriate content, an unexpected permission, or unexpected data collection;
- at least 80% of testers would trust KinPlay on a family device after reading the MVP privacy posture.

Pause and triage when any install/launch failure, permission concern, safety/trust blocker, or repeated high-severity confusion appears.

## Tester message

> KinPlay is an offline Android beta for parent-led family play. Please use it naturally with your family over the test period. It should not require an account or request camera, microphone, contacts, location, or other sensitive permissions. Please do not send child names, photos, audio, video, exact birthdates, or precise locations. We need notes about installation, what was clear or confusing, whether the family used it again, whether any content felt unsafe, and what content would make you return.

## Device and session records

Use the existing [`docs/testing/family-device-test-plan.md`](../testing/family-device-test-plan.md) for the device matrix, session tracker, definitions, and data-hygiene rules. Keep raw tester material outside source control and store only sanitized summaries in the repository.

## Release handoff record

| Field | Value |
|---|---|
| Play track | Internal / closed |
| Artifact filename |  |
| Version name/code |  |
| SHA-256 |  |
| Tester cohort size |  |
| Device count |  |
| Install/offline result |  |
| Privacy/Data safety review |  |
| Feedback destination |  |
| Go/no-go decision |  |
| Decision date |  |
