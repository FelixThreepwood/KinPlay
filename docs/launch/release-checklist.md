# KidPlay release and rollback checklist

Status: operational draft
Previous verified work-in-progress release: `0.6.3` (version code `10`)
Current candidate under automated and physical-device validation: `0.7.0-beta2` (version code `12`); includes the reusable spinner wheel, separate `Wheel Lab` app, KidPlay application label, and Fox Heart launcher icon.
Package: `com.kinplay.app`

Use this checklist for a private beta, closed-test upload, or store candidate. A debug APK may be used for private device testing, but it is not a production Play artifact.

## 1. Scope and source

- [ ] Read the current progress ledger and release record.
- [ ] Confirm the intended branch and clean-worktree scope.
- [ ] Confirm no unrelated user work, credentials, OAuth files, raw tester notes, or local agent state is staged.
- [ ] Confirm the target KPF or launch task is authorized.
- [ ] Review the source and content diff before packaging.
- [ ] Run `git diff --check`.

## 2. Version and build identity

- [ ] Increment `versionCode` monotonically.
- [ ] Set the work-in-progress `versionName` before the final build.
- [ ] Use the dated artifact name:

```text
YYYYMMDD_KidPlay_vMAJOR.MINOR.PATCH[-PRERELEASE]_<purpose>.apk
```

- [ ] Confirm application ID `com.kinplay.app`.
- [ ] Confirm minimum SDK 26 and target SDK 35.

## 3. Automated validation

Run from the repository root:

```bash
source scripts/android-env.sh
python3 /home/phantomatic/.hermes/scripts/kinplay_offline_validate.py --mode checkpoint
./gradlew --no-daemon testDebugUnitTest --rerun-tasks
./gradlew --no-daemon lintDebug
```

For JSON-backed content, also run the packaged validator described in [`content-qa-checklist.md`](content-qa-checklist.md).

Record:

- JVM test count and result;
- Android test-source compilation result;
- lint result;
- schema, canonical/runtime, and APK asset parity result;
- `git diff --check` result.

## 4. APK inspection

For the actual final APK, run:

```bash
aapt dump badging app/build/outputs/apk/debug/app-debug.apk
aapt dump permissions app/build/outputs/apk/debug/app-debug.apk
apksigner verify --verbose app/build/outputs/apk/debug/app-debug.apk
sha256sum app/build/outputs/apk/debug/app-debug.apk
```

Confirm:

- package and version metadata are correct;
- no unexpected Internet, camera, microphone, contacts, location, storage, billing, or telemetry permission appears;
- signature verification passes;
- packaged content and approved visual assets match their validated source bytes;
- the artifact is clearly identified as debug/private-beta or release/store candidate.

## 5. Physical-device gate

- [ ] At least one physical device or emulator is available.
- [ ] Install and upgrade path tested.
- [ ] Offline launch tested.
- [ ] Home, category, detail, Mad Libs, Calm Down, settings, menu, feedback, lock, orientation, timer, completion, and back paths tested as applicable.
- [ ] Large font and accessibility checks performed.
- [ ] Launcher icon and alias/cache behavior tested.
- [ ] No permission prompt or unexpected data transfer observed.
- [ ] Family test plan results recorded separately.

Never claim device validation when no target is connected.

## 6. Source publication

- [ ] Stage only intended files.
- [ ] Run staged diff, staged whitespace, and relevant tests.
- [ ] Commit with a clear `feat:`, `fix:`, `docs:`, or `chore:` message.
- [ ] Push the intended branch.
- [ ] Verify local `HEAD` and remote branch SHA.
- [ ] Confirm the worktree is clean.

## 7. Active APK-drop inventory gate

- [ ] Update `docs/launch/apk-drop-policy.json` with the final expected filename, package, version name, and version code for every registered app affected by the release.
- [ ] Run `scripts/validate-apk-drop.py --local /mnt/cyberforgex-torrents/KinPlay/apk-drops` with the Android SDK environment set.
- [ ] Query the live Drive active folder by parent ID and run the validator against the saved JSON inventory.
- [ ] Require exactly one active APK per registered app; checksum sidecars may accompany the APKs, but stale APKs, temporary names, and status-labeled names are blockers.
- [ ] Preserve superseded APKs through a reversible move to `_archived-apk-drops` only after the new APK passes downloaded byte comparison.

## 8. Local shared-drive publication

- [ ] Confirm `/mnt/cyberforgex-torrents/KinPlay/apk-drops` is mounted and writable.
- [ ] Copy to a hidden temporary destination name.
- [ ] Verify size, checksum, metadata, and signature against the temporary destination.
- [ ] Rename the completed file to its final dated name.
- [ ] Move the superseded root APK for each affected registered app into `_archived-apk-drops` only after the new file passes verification.
- [ ] Confirm exactly one current APK per registered app remains in the root.

## 9. Google Drive publication

- [ ] Verify OAuth account and live access to the current shared `apk-drops` folder.
- [ ] Upload the versioned APK to the exact folder.
- [ ] Record returned file ID, name, size, parent folder, and link.
- [ ] Download the uploaded object to a temporary path.
- [ ] Compare downloaded SHA-256 and size with the local artifact.
- [ ] Move superseded cloud APKs for affected registered apps into `_archived-apk-drops` only after the new object passes verification; do not permanently delete routine release evidence.
- [ ] Re-list the folder and confirm exactly one active APK per registered app, with no temporary or status-labeled APK.

The current folder is recorded in the project beta-operations reference, but the live folder must be discovered before every upload; do not rely only on a copied ID.

## 10. Records and notification

- [ ] Update the progress ledger with tests, artifact, checksum, commit, remote SHA, and blockers.
- [ ] Update the release record with local and cloud handles.
- [ ] Preserve the family-device limitation when no target was available.
- [ ] Notify the project owner only after local and cloud copies pass independent checksum verification.

## 11. Rollback

If a release is defective:

1. Stop broader distribution.
2. Preserve the defective artifact and its checksum.
3. Record the affected version and reproduction evidence.
4. Restore the last verified local/cloud artifact through the same upload-before-delete and checksum process.
5. Do not delete evidence or raw test notes required for diagnosis.
6. Add a regression test before preparing a replacement build.

## Release record

| Field | Value |
|---|---|
| Version name/code | `0.6.3` / `10` |
| Artifact filename | `20260806_KinPlay_v0.6.3_MVP.apk` |
| SHA-256 | `f18d6e93a4c84f5876c4591254e0d31f2282ef0b1cbd50f7fb1325a829037205` |
| Local path | `/mnt/cyberforgex-torrents/KinPlay/apk-drops/20260806_KinPlay_v0.6.3_MVP.apk` |
| Google Drive file ID/link | `1MO4JEMKc4_yxe2D0s1r9t3DLgs36HdXj` — https://drive.google.com/file/d/1MO4JEMKc4_yxe2D0s1r9t3DLgs36HdXj/view?usp=drivesdk |
| Test count | 214 unit tests; 0 failures, 0 errors, 0 skipped; Android-test sources compiled |
| Lint result | passed |
| APK metadata/permissions | package `com.kinplay.app`, min SDK 26, target SDK 35; only dynamic receiver permission |
| Signature result | v2 verified |
| Device result | open — `adb` unavailable and no Android target connected |
| Commit/remote SHA | `f100058f430cef43ae5a4ecb2481e69dd5bac510` / verified equal |
| Release decision | published as iterative private/shared-drive WIP release; physical retest remains open |

## 0.6.3 completion evidence

- Local shared-drive root contains exactly one current APK; beta2 and beta3 were moved to `release-history` before final rename.
- Google Drive `apk-drops` contains exactly one active APK. The prior beta3 object was moved to reversible trash only after the new object read back byte-for-byte.
- Canonical/runtime content JSON, schema, unique IDs, APK content parity, and Tiny Monster visual-resource parity passed.
- This is a work-in-progress private test release, not an official production or store publication.

## 0.7.0-beta2 KidPlay branding completion evidence

- Visible application label is `KidPlay`; application ID remains `com.kinplay.app`.
- Active default launcher master is the exact Nano Banana `08_fox_heart.jpg` source; source and packaged master SHA-256: `a8dd209cd588e0f1de4c9d58668b851ab477434ad5a5f93a5217dcb109bdbd5b`.
- Debug APK: `20260808_KidPlay_v0.7.0-beta2_FoxHeart_debug.apk`; 23,103,966 bytes; SHA-256 `1b1e74759cb518e04f9099ad4ca151c69ac4e36a7be5d3abaf57e0d77c53976e`.
- Source commit: `7fcfa1056cf7258162b9591929ca5056c6e06a88`; remote `main` matched after push.
- Google Drive object: `1GrQRqy1drm-S0GouppZvwf9MBaoEZqd6`; https://drive.google.com/file/d/1GrQRqy1drm-S0GouppZvwf9MBaoEZqd6/view?usp=drivesdk
- Drive download matched the local APK byte-for-byte.
- Physical-device/emulator launcher-cache and install/upgrade testing remains open because no Android target was connected.
