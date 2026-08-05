# KinPlay launch-readiness packet

Status: offline preparation in progress
As of: 2026-08-05
Current beta: `0.6.0-beta1` (version code `6`)
Package: `com.kinplay.app`

This directory contains release and testing preparation that can be completed without a physical Android device or Play Console access. Draft documents are not public policy, legal advice, or evidence that a Play Console form has been submitted.

## Current verified baseline

- Local repository: `/home/phantomatic/projects/app-pass-rev/KinPlay`
- Current local beta APK: `/mnt/cyberforgex-torrents/KinPlay/apk-drops/20260804_KinPlay_v0.6.0-beta1_MVP.apk`
- Current APK SHA-256: `069dadf06a5d79d1a485694219cfd799169f26cab126cf359ae10bca72f13ae8`
- Current APK size: `11,674,099` bytes
- Current source head: `2678b328f88ceec2ce75447d9da8c5ffd5185966`
- Current Drive publication: recorded in `docs/testing/feedback/KPF_BATCH_A9A144CD_RELEASE_RECORD.md`
- Device status: no `adb` executable or connected Android target is available in this environment

## Offline work completed in this packet

| Backlog area | Offline result | Status |
|---|---|---|
| LCH-020 | Current-MVP privacy policy draft with explicit email handoff and local-storage boundaries | Draft ready; hosting and legal review pending |
| LCH-021 | Source/manifest/Gradle SDK and data inventory | Draft ready; final resolved dependency export remains a release check |
| LCH-050 | Decision record: no crash-reporting SDK for the current MVP | Decision ready; product/privacy owner confirmation pending |
| LCH-052 | Decision record: no analytics for the current MVP launch path | Decision ready; product/privacy owner confirmation pending |
| LCH-071 | Repeatable content and safety QA checklist | Ready for each content revision |
| LCH-040/LCH-041 | Closed-testing protocol and privacy-safe feedback form | Ready for Play Console/tester coordination |
| LCH-080 | Release, artifact, publication, and rollback checklist | Ready for release operations |

## External gates that remain

1. Project owner executes the physical-device and family test plan on at least the required device set.
2. Any install, launch, offline, safety, trust, or high-severity confusion issue is triaged before broader testing.
3. A public privacy-policy URL is selected and the draft is reviewed.
4. Play Console app, policy, target-audience, content-rating, and Data safety decisions are completed.
5. Release signing and Play App Signing ownership are documented before a store upload.
6. Store screenshots and any required feature graphic are captured from the tested build.
7. A product decision is recorded for first-launch monetization; the current MVP adds no ads or purchases.

## Documents

- [`privacy-policy-draft.md`](privacy-policy-draft.md)
- [`sdk-data-inventory.md`](sdk-data-inventory.md)
- [`content-qa-checklist.md`](content-qa-checklist.md)
- [`closed-testing-plan.md`](closed-testing-plan.md)
- [`feedback-form.md`](feedback-form.md)
- [`release-checklist.md`](release-checklist.md)

## Operating decisions recorded here

- The current MVP remains offline-first, parent-led, and account-free.
- The current app requests no dangerous Android runtime permission and declares no `uses-permission` entry in its manifest.
- The current app contains no analytics, advertising, crash-reporting, billing, account, cloud-sync, or remote-content SDK.
- The current app stores settings, favorites, recent IDs, feedback records, and Would You Rather progress locally.
- Feedback handoff uses an explicit Android email intent or clipboard copy. The app does not silently send email.
- Adding analytics, crash reporting, monetization, cloud sync, or remote content requires a new privacy/Data safety review before implementation.

## Update rule

At every release candidate, re-check the Gradle dependency graph, manifest, packaged APK permissions, local-storage code, privacy policy, Data safety answers, store listing, and this packet together. A draft must not be described as published until its external URL or Play Console record has been verified.
