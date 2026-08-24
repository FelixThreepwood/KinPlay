# APK drop policy

## Scope

This policy applies to the canonical local drop and the live Google Drive `apk-drops` folder used for private Android validation:

- Local: `/mnt/cyberforgex-torrents/KinPlay/apk-drops`
- Drive: the live folder discovered by name before each publication; the current folder is `apk-drops`.
- Archive: `_archived-apk-drops` under each drop root.

The machine-readable source of truth is [`apk-drop-policy.json`](./apk-drop-policy.json). The validator is [`scripts/validate-apk-drop.py`](../../scripts/validate-apk-drop.py).

## Active-folder invariant

The active folder contains exactly one current APK for each registered application:

- `KidPlay` — package `com.kinplay.app`
- `DevLab` — package `com.devlab`

Checksum sidecars named `<active-apk>.sha256` are allowed and required locally. Archive, history, working-build, and pre-release directories are not active APK inventory.

An active APK must:

1. Match the exact filename in `apk-drop-policy.json`.
2. Match the registered package, version name, and version code in its APK manifest.
3. Use only the app name, semantic version, and optional date in its filename.
4. Exclude `debug`, `beta`, `MVP`, `feedback`, `verified`, `complete`, temporary, and status labels.
5. Be the only active APK for that application. Older versions are not alternate active copies.

## Publication gate

Before every local or Drive publication:

1. Build the final APK and update the registered expected filename/version in `apk-drop-policy.json` before validation.
2. Run the local validator. It must pass before copying or uploading:

   ```bash
   ANDROID_HOME=/home/phantomatic/Android/Sdk \
   ANDROID_SDK_ROOT=/home/phantomatic/Android/Sdk \
   python3 scripts/validate-apk-drop.py \
     --local /mnt/cyberforgex-torrents/KinPlay/apk-drops
   ```

   For a new release, validate the staged temporary copy with the same manifest/version checks before replacing the active name.

3. Discover the live Drive folder and query its direct APK children. Save the JSON response temporarily and run:

   ```bash
   python3 scripts/validate-apk-drop.py \
     --drive-inventory /tmp/kinplay-drive-active-apks.json
   ```

4. Upload or copy the new artifact first under a temporary name.
5. Read back metadata and downloaded bytes. Compare size and SHA-256 with the verified local artifact.
6. Rename the verified object to its clean active filename.
7. Move every superseded APK for the same registered application into `_archived-apk-drops` using a reversible parent move. Move matching checksum sidecars locally as well.
8. Re-list local and Drive active folders and rerun both validators. The final active inventory must pass exactly.

A failed validator is a release blocker. It is not permission to delete the unexpected APK. Identify it by object ID/path and move it to the reversible archive only after the replacement artifact has passed its independent byte check.

## Retention and rollback

- Routine cleanup moves superseded artifacts to `_archived-apk-drops`; it does not permanently delete them.
- Historical release folders remain intact.
- Archived object IDs and local paths belong in the release record.
- If a new release fails, restore the last verified archived object through the same metadata and checksum process.
- A temporary upload name must never remain in the active folder after publication.

## Policy maintenance

When a new version is published:

- update only the affected registered app's expected version, version code, and clean active filename;
- build and inspect the APK before changing the policy file's expected identity;
- rerun the local and Drive validators after cleanup;
- record the source commit, artifact SHA-256, Drive object ID, and archive result in the release evidence.

When adding an application, add its package and clean active filename to `registeredApps` before the first publication. Do not solve an inventory collision by adding a second copy of an existing application.
