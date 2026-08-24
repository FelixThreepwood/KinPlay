# APK-drop cleanup record — 2026-08-24

## Finding

The active Google Drive `apk-drops` folder contained more than the current APK for each registered application. Local state already contained only the current KidPlay and DevLab APKs.

## Cleanup

After live metadata and move-capability verification, these superseded Drive objects were moved reversibly to `_archived-apk-drops`:

| Application | Artifact | Drive object ID | Result |
|---|---|---|---|
| DevLab | `20260823_DevLab_v0.2.2.apk` | `1plcs8WNEE-4HfFCl_TNy2bcrM9RHlkd7` | archived |
| DevLab | `20260811_DevLab_v0.2.0_debug.apk` | `18GC3qF04lh8OMqURpYmoBRr3WmFOYb9-` | archived |
| KidPlay | `20260811_KidPlay_v0.7.0_FoxHeart_debug.apk` | `1VDry1KD11WyzNNJQUZ6sNnbG_qQ0DCdk` | archived |

No APK was permanently deleted. The current Drive active folder now contains exactly:

- `20260823_DevLab_v0.2.3.apk`
- `20260824_KidPlay_v0.7.3.apk`

The local active folder contains the same two APKs plus their checksum sidecars. Older local artifacts remain only under the reversible archive or historical directories.

## Verification

- Local validator: passed; package/version/version-code and sidecar checks passed.
- Live Drive inventory validator: passed; exact active filename set passed.
- Stale Drive objects: verified with live parent and move capabilities before archival.

The continuing control is defined in [`apk-drop-policy.md`](./apk-drop-policy.md) and enforced by [`scripts/validate-apk-drop.py`](../../scripts/validate-apk-drop.py).
