# KinPlay Family Device Test Plan

## Goal

Validate the KinPlay MVP on physical Android devices with 5-10 family/parent testers before expanding scope. The test should answer four product questions:

1. Can a parent install and launch the APK on a real Android device?
2. Can a parent start a useful activity without explanation from the team?
3. Do families voluntarily use KinPlay more than once?
4. Do parents trust the app as safe, private, low-prep, and child-appropriate?

## Scope

### In scope

- Debug APK installed from `/mnt/cyberforgex-torrents/KinPlay/apk-drops/KinPlay-mvp-debug.apk`.
- Physical Android phones and tablets available to the user/testers.
- Parent-led sessions with children roughly ages 2-8.
- MVP flows:
  - Home
  - Quick Play
  - Pick a Game
  - Mad Libs
  - Calm Down
  - About / Safety
- Manual feedback collection only.

### Out of scope for this round

- Play Store testing tracks.
- Analytics, crash reporting, accounts, child profiles, cloud sync, remote config, purchases, ads, camera, microphone, contacts, location, or public sharing.
- Collecting child names, photos, audio, video, precise locations, or other child-identifying data.

## Test population

Recruit 5-10 parent/caregiver testers.

Preferred mix:

- 2-3 families with children ages 2-3.
- 2-3 families with children ages 4-5.
- 2-3 families with children ages 6-8.
- At least 2 Android phone models.
- At least 1 Android tablet if available.
- At least 2 testers who did not hear a product walkthrough before testing.

Tester IDs should be anonymous labels such as `T01`, `T02`, and `T03`. Do not store child names.

## Device matrix

Record one row per physical device.

| Tester ID | Device type | Manufacturer/model | Android version | Install source | Installed? | Launches? | Notes |
|---|---|---|---|---|---|---|---|
| T01 | Phone |  |  | APK drop |  |  |  |
| T02 | Tablet |  |  | APK drop |  |  |  |

Minimum device pass:

- APK installs on at least 3 distinct Android devices.
- App launches without network access on every installed device.
- No Android permission prompt appears during install or first launch.

## Tester setup script

Use this short script when sending the APK to testers.

> KinPlay is a small offline Android demo for parent-led family play. Please try it naturally with your child or children over the next 3-7 days. It does not need an account and should not ask for camera, microphone, contacts, location, or other permissions. Please do not send child names, photos, audio, or video in feedback. We only need notes about what worked, what confused you, whether you used it again, and whether the app felt safe and trustworthy.

## Install and smoke test

Run this once per device before asking for family use.

1. Transfer the APK from the shared APK drop to the Android device.
2. Install the APK.
3. Launch KinPlay.
4. Confirm no runtime permission prompts appear.
5. Turn on airplane mode.
6. Relaunch KinPlay.
7. Open each primary home option:
   - Quick Play
   - Pick a Game
   - Mad Libs
   - Calm Down
   - About / Safety
8. Confirm content appears while offline.
9. Record device result in the device matrix.

Smoke test pass criteria:

- Install succeeds.
- App launches.
- Home screen shows the four MVP modes.
- Local content loads while offline.
- No permission prompts appear.
- About / Safety states parent-led use and no MVP data collection.

## Family test protocol

Ask each tester to complete at least two sessions if possible.

### Session 1: First-run observation

Parent should start from a fresh launch and choose what they naturally want to try first.

Observe or ask afterward:

- Which button did they tap first?
- Did they understand what to do within 30 seconds?
- Did they need outside explanation?
- Did any wording, screen, or activity step confuse them?
- Did the child engage, ignore it, or resist it?
- Did the parent trust the activity instructions?
- Did anything feel unsafe, too chaotic, too babyish, too advanced, or too much work?

### Session 2+: Repeat-use check

Ask testers to use KinPlay again on another day or later the same day without being prompted by the team.

Record:

- Whether they opened it again.
- What prompted the repeat use.
- Which mode they used.
- Whether the content still felt fresh.
- Whether they requested more content, different content, filters, favorites, saved activities, timers, or printable/shareable ideas.

## Metrics and definitions

### 1. Repeat-use signal

This is the most important MVP validation signal.

Track per tester:

- `0 = no repeat use`: used once only, no stated intent to return.
- `1 = weak repeat signal`: says they might use again but did not actually reopen during the test window.
- `2 = real repeat use`: reopened KinPlay at least once without the team walking them through it.
- `3 = strong repeat use`: reopened multiple times or asked for more content/features because the family wanted more.

MVP target: at least 3 of 5 testers, or at least 5 of 10 testers, score `2` or higher.

### 2. Confusion signal

Track where parents hesitate, backtrack, ask for explanation, or misunderstand the app.

Categories:

- Install confusion.
- Home navigation confusion.
- Quick Play expectation mismatch.
- Pick a Game browsing confusion.
- Mad Libs input/reveal confusion.
- Calm Down purpose confusion.
- Activity instruction confusion.
- Age/material/duration mismatch.

Severity:

- `Low`: noticed but tester completed the session.
- `Medium`: slowed the session or required a hint.
- `High`: blocked the session or caused abandonment.

MVP target: no high-severity confusion affecting more than one tester.

### 3. Requests for more content

Track exact requests without overbuilding immediately.

Content request categories:

- More games.
- More calm-down activities.
- More Mad Libs.
- More age-specific activities.
- More no-materials activities.
- More outdoor/movement activities.
- More quiet/bedtime activities.
- Seasonal/holiday themes.
- Custom family prompts.

MVP target: at least 2 testers ask for more content or describe a concrete use case where more content would make them return.

### 4. Safety and trust feedback

Ask every tester directly:

- Did KinPlay feel parent-led rather than child-targeted?
- Did anything feel unsafe or inappropriate for your child?
- Did you notice any permission prompts?
- Did the no-account/no-data-collection posture make sense?
- Would you trust this app on your family device?
- What would make you trust it more?

MVP target:

- No tester reports a permission prompt.
- No tester reports child-identifying data collection.
- No tester reports scary, violent, sexual, political, medical, or otherwise inappropriate content.
- At least 80% of testers answer that they would trust the app on a family device after understanding the MVP privacy posture.

## Session feedback tracker

Use one row per tester session.

| Tester ID | Session # | Date/window | Device ID | Child age band | Mode used | Completed? | Repeat-use score | Confusion category | Confusion severity | More-content request | Safety/trust concern | Quote or note | Follow-up action |
|---|---:|---|---|---|---|---|---:|---|---|---|---|---|---|
| T01 | 1 |  |  | age_4_5 | Quick Play |  |  |  |  |  |  |  |  |
| T01 | 2 |  |  | age_4_5 | Calm Down |  |  |  |  |  |  |  |  |

Data hygiene:

- Use age bands, not exact birthdates.
- Do not store child names.
- Do not store photos, videos, or audio.
- Paraphrase sensitive family context.
- If a tester sends identifying information anyway, strip it before copying notes into project docs.

## Interview questions

Ask after each tester has used the app.

### Quick questions

1. What did you think KinPlay was for after seeing the home screen?
2. What did you tap first, and why?
3. Was there any point where you were unsure what to do?
4. Did your child engage with the activity?
5. Did any activity feel too hard, too easy, too long, too energetic, or too quiet?
6. Did you use it again? If yes, what made you come back?
7. What would you want more of?
8. Did the app feel safe and trustworthy for family use?
9. Did it ask for any permissions?
10. Would you recommend this to another parent in its current form? Why or why not?

### Optional deeper prompts

- If you had only 5 minutes before dinner/bedtime/leaving the house, would you open KinPlay?
- Which mode should be the default first button?
- Did Quick Play feel helpful or too random?
- Would filters by age, time, materials, energy, or mood matter?
- What content would make this useful every week?

## Triage rubric after feedback

Sort findings into four buckets.

### Must fix before broader testing

- Crash, install failure, or launch failure on common devices.
- Any permission prompt.
- Content unavailable offline.
- A safety/trust issue.
- A repeated high-severity confusion issue.

### Should fix soon

- Repeated wording/navigation confusion.
- Activities that parents understand but children do not engage with.
- Missing obvious content categories requested by multiple testers.
- Poor fit for a major age band.

### Backlog

- Nice-to-have features such as favorites, timers, custom packs, sharing, or visual polish.
- Requests from only one tester that do not block the MVP learning goal.

### Do not build yet

- Accounts, child profiles, analytics, cloud sync, remote content, public sharing, camera, microphone, location, contacts, ads, or purchases.

## Decision gates

### Proceed to content expansion if

- Device smoke test passes on at least 3 devices.
- At least 5 family/parent testers complete one session.
- Repeat-use target is met or nearly met.
- At least 2 testers ask for more content.
- No safety/trust blocker appears.

### Proceed to UX fixes before content expansion if

- Repeat-use is weak but parents show interest.
- Confusion is the main blocker.
- Multiple testers ask for clearer labels, shorter steps, or better browsing.

### Pause broader testing if

- Install/launch is unreliable.
- Any permission/data-collection concern appears.
- Parents do not trust the app after the About / Safety explanation.
- Children consistently disengage and parents do not ask for more content.

## Final test summary template

Use this at the end of the 5-10 tester round.

```md
# KinPlay Family Test Summary

## Test coverage

- Testers completed:
- Devices tested:
- Android versions:
- Sessions recorded:

## Result

- Device smoke test: PASS / FAIL
- Repeat-use target: PASS / FAIL
- Confusion target: PASS / FAIL
- More-content target: PASS / FAIL
- Safety/trust target: PASS / FAIL

## Top findings

1.
2.
3.

## Recommended next step

- [ ] Content expansion
- [ ] UX fixes
- [ ] Device/install remediation
- [ ] Safety/trust remediation
- [ ] Pause and reassess

## Evidence

- Repeat-use count:
- Strongest positive signal:
- Biggest blocker:
- Most requested content:
- Safety/trust notes:
```
