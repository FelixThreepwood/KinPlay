# KinPlay release and rollback checklist

Status: operational draft
Current verified beta: `0.6.0-beta1` (version code `6`)
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
- [ ] Set the beta `versionName` before the final build.
- [ ] Use the dated artifact name:

```text
YYYYMMDD_KinPlay_vMAJOR.MINOR.PATCH[-PRERELEASE]_<purpose>.apk
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

## 7. Local shared-drive publication

- [ ] Confirm `/mnt/cyberforgex-torrents/KinPlay/apk-drops` is mounted and writable.
- [ ] Copy to a hidden temporary destination name.
- [ ] Verify size, checksum, metadata, and signature against the temporary destination.
- [ ] Rename the completed file to its final dated name.
- [ ] Move the superseded root MVP APK into the documented historical folder only after the new file passes verification.
- [ ] Confirm exactly one current MVP APK remains in the root.

## 8. Google Drive publication

- [ ] Verify OAuth account and live access to the current KinPlay `apk-drops` folder.
- [ ] Upload the versioned APK to the exact folder.
- [ ] Record returned file ID, name, size, parent folder, and link.
- [ ] Download the uploaded object to a temporary path.
- [ ] Compare downloaded SHA-256 and size with the local artifact.
- [ ] Trash superseded cloud APKs only after the new object passes verification.
- [ ] Re-list the folder and confirm exactly one active current APK.

The current folder is recorded in the project beta-operations reference. Verify it live before every upload; do not rely only on a copied ID.

## 9. Records and notification

- [ ] Update the progress ledger with tests, artifact, checksum, commit, remote SHA, and blockers.
- [ ] Update the release record with local and cloud handles.
- [ ] Preserve the family-device limitation when no target was available.
- [ ] Notify the project owner only after local and cloud copies pass independent checksum verification.

## 10. Rollback

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
| Version name/code |  |
| Artifact filename |  |
| SHA-256 |  |
| Local path |  |
| Google Drive file ID/link |  |
| Test count |  |
| Lint result |  |
| APK metadata/permissions |  |
| Signature result |  |
| Device result |  |
| Commit/remote SHA |  |
| Release decision |  |
