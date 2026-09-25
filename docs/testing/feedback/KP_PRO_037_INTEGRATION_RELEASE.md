# KP-PRO-037 integration and release evidence

Date: 2026-09-12
Package: `com.kinplay.app`
Visible app name: `KidPlay`

## Scope

This release integrates the completed baseline-remediation lanes already present on `main`:

- `183165d` — connected-test regression contracts;
- `28861f8` — visible KidPlay branding reconciliation;
- `4da6d03` — lifecycle-safe bundled music controls;
- `6826cf9` — Android visual-interaction audit harness.

The integration release increments KidPlay exactly once from version `0.7.3` / code `16` to version `0.7.4` / code `17`. The in-app release summary is six words: `Integrated branding, music, and audit remediation`.

## Source and deterministic gates

- Integration commit: `005383060c131713977e8fe86fcb3c4f6191118e` (`fix(kinplay): integrate baseline remediation release`).
- `./gradlew --no-daemon :app:testDebugUnitTest :app:compileDebugAndroidTestKotlin :app:lintDebug :app:assembleDebug`: passed.
- JVM result: `235` tests, `0` failures, `0` errors, `0` skipped.
- Android-test source compilation: passed.
- Android lint: passed.
- Debug assembly: passed.
- `python3 -m unittest discover -s scripts -p 'test_*.py' -v`: `13` passed.
- Python compilation: passed for `scripts/android_audit.py` and `scripts/test_android_audit.py`.
- Shell syntax: passed for `scripts/android-audit.sh`.
- JSON schema, canonical/runtime byte parity, unique IDs, and packaged asset parity: passed.
- `git diff --check` and staged diff checks: passed.
- Added-line security scan: no secret, shell-injection, eval/exec, unsafe-deserialization, or SQL-injection hits.

## APK evidence

- Build output: `app/build/outputs/apk/debug/app-debug.apk`.
- Final size: `25,535,866` bytes.
- Final SHA-256: `a5171e637316e56ad3e8e9304764f899716a63bd9673396d9c753da674c7870c`.
- `aapt dump badging`: package `com.kinplay.app`, version `0.7.4`, code `17`, min SDK `26`, target SDK `35`, label `KidPlay`.
- APK permissions: only `com.kinplay.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION`.
- `apksigner verify --verbose`: v2 signature verified; one signer.
- A prior same-version copy produced before the final assemble had SHA-256 `93023d845a2faf54ba68eb2ab953bc8205e08b3a6120393ecc420e06715c51ba`; it was moved to the reversible archive before replacing the active copy. The final active copy is the exact final-build byte set recorded above.

## Local shared-drop evidence

Destination: `/mnt/cyberforgex-ai/App Dev/KinPlay/apk-drops`

- Active KidPlay artifact: `20260912_KidPlay_v0.7.4.apk`.
- Active KidPlay size and SHA-256 match the final build exactly.
- Active sidecar: `20260912_KidPlay_v0.7.4.apk.sha256`.
- Local validator: passed with no errors.
- Active APK inventory: exactly two registered APKs, `20260912_KidPlay_v0.7.4.apk` and `20260823_DevLab_v0.2.3.apk`.
- Superseded KidPlay copies are retained under `_archived-apk-drops`; no active temporary or status-labelled APK remains.

## Runtime boundary

Connected instrumentation was attempted with `:app:connectedDebugAndroidTest`. The configured AVD was not package-manager-ready: Gradle started `0` tests and failed installation with `cmd: Can't find service: package`. The audit health bundle is `build/android-audit/kp-pro-037-current/result.json`; it records `status: not-boot-complete`, no `/dev/kvm`, and `hogwarts-android-emulator.service` inactive, dead, and disabled. No physical-device validation was performed; those checks remain reserved for LJ.

## 2026-09-23 reconciliation

The source release and APK remain unchanged. This reconciliation updates only release/policy evidence; it does not edit app code, change either app version, rebuild an APK, or alter Drive objects.

### Policy and local artifact

- The pinned local root is `/mnt/cyberforgex-ai/App Dev/KinPlay/apk-drops`, confirmed present. `docs/launch/apk-drop-policy.json` and this policy's reproduction command now name the same path; the command quotes the path because it contains spaces.
- The active KidPlay artifact observed locally remains `20260912_KidPlay_v0.7.4.apk`, 25,535,866 bytes, SHA-256 `a5171e637316e56ad3e8e9304764f899716a63bd9673396d9c753da674c7870c`; its checksum sidecar matches. Local inventory also contains the registered `20260823_DevLab_v0.2.3.apk` and its sidecar.
- Reproducible local command: `ANDROID_HOME=/home/tigger/Android/Sdk ANDROID_SDK_ROOT=/home/tigger/Android/Sdk python3 scripts/validate-apk-drop.py --local "/mnt/cyberforgex-ai/App Dev/KinPlay/apk-drops"`. Result: passed, errors `[]`; the observed inventory contains exactly the two policy-registered APKs with matching package/version metadata and valid sidecars.
- `python3 -m unittest discover -s scripts -p 'test_*.py' -v`: 13 passed. `python3 -m json.tool docs/launch/apk-drop-policy.json` and `git diff --check`: passed.
- The APK is Android Debug-signed (APK Signature Scheme v2), not production-signed. No public-release claim is made.

### Humanizer review of changed customer copy

- Dedicated review session: `harry_potter`, model `gpt-5.6-sol`, provider `openai-codex`, medium reasoning; session `20260923_103016_9db612`. The session stores the complete itemized response. The CLI returned that response, then exited with status 134; the stored session was read back and confirmed.
- The review marked these values KEEP: `KidPlay`; `KidPlay Seed Pack v1`; `[KidPlay Beta][Feedback Batch][$versionName+$versionCode][$batchId]`; `KidPlay feedback`; `Return to KidPlay`; and `Child handoff lock active. KidPlay controls are blocked. Android system controls remain available.`
- It recommended these alternatives: `KidPlay helps adults guide short play sessions with children. Review the activity, clear the space, and supervise any movement or materials.`; `Confirm receipt in the KidPlay app-development Discord channel.`; and `Integrated branding, music, and audit fixes` (six words).
- No shipped wording was changed in this reconciliation. The current v0.7.4 APK remains byte-identical to the recorded artifact; applying copy revisions requires a separately versioned build and publication. The review recommendations are recorded, not represented as applied or approved for this APK.

### GitHub and Google Drive evidence

- At the start of this evidence follow-up on 2026-09-23, live `git ls-remote origin refs/heads/main` returned `e25a5e2e1372b0b5cae78d0c70802b700ce49dbe`, matching the clean evidence-worktree base. The follow-up commit and its post-push read-back are recorded in the execution handoff for Kanban task `t_139c8fd1`.
- The task handoff and authorized owner-supplied evidence report the private Drive `apk-drops` inventory as `KidPlay-0.7.4.apk` and `DevLab-0.3.0.apk`, under active parent folder ID `12bINCtZHQwvh3-mIbQ2x-swPE6ACzjYp`. This profile did not independently query Drive.
- KidPlay Drive object: ID `1A_YR6ywb7hUBZtUBZcvLT3Z4cnYEsCqN`, name `KidPlay-0.7.4.apk`, 25,535,866 bytes, SHA-256 `a5171e637316e56ad3e8e9304764f899716a63bd9673396d9c753da674c7870c`. The supplied handoff reports a downloaded byte-read-back match; the size and digest match the locally verified release artifact above.
- DevLab Drive object: ID `1qnFwCs1-FZRTClGyO-RokFYOa5TBmINd`, name `DevLab-0.3.0.apk`, 10,343,523 bytes, SHA-256 `e2d3b6e8e5efc14037a010bbcfe8973b56b85569dee730c2f3ef228701d4ffb1`. The authorized owner-supplied record states that it was uploaded under a temporary name, downloaded and compared byte-for-byte with `/mnt/cyberforgex-ai/App Dev/KinPlay/review-drops/2026-09-18-design-directions/20260918_DevLab_v0.3.0.apk`, renamed only after parity passed, then downloaded again after final rename with the same SHA-256. This worktree independently rehashed that local comparison source and confirmed the same 10,343,523-byte size and SHA-256; the Drive read-back claim itself remains attributed to the owner-supplied evidence.
- The owner-supplied record states the superseded DevLab 0.2.3 object was moved reversibly to `_archived-apk-drops`, folder ID `1bqeCNNXnSywOb5SQ-NsOhRFLe8K6nYXu`. The handoff reports prior current objects were archived; no Drive objects were modified in this reconciliation.
- The independent read-only review is recorded in Kanban task `t_5a362e1c`: it found no critical security or production-application logic defect, while retaining runtime/device and release-boundary holds.

## Historical external state at initial publication (2026-09-12)

- Independent reviewer: the first two read-only attempts timed out after 420 seconds without a JSON verdict. A later read-only review completed with a HOLD in Kanban task `t_5a362e1c`; no independent approval is claimed.
- GitHub: `origin/main` remains `45a5cadc6b69cc29bc5f1326ac760b75cf3e89e1`; the local integration commit is not pushed because owner-authenticated GitHub access is blocked by `t_b323a45f`.
- Google Drive: publication was not attempted because the active profile's Google Workspace check returned `NOT_AUTHENTICATED` for its profile-local token. No remote object was changed, and no Drive checksum or one-current inventory claim is made.

The statements in this historical section describe the initial publication state only. The 2026-09-23 reconciliation above supersedes the review, GitHub, and KidPlay Drive facts where stated. Connected instrumentation, physical-device validation, production signing, and public-release readiness remain unresolved.

## 2026-09-25 candidate verification and release hold

This checkpoint records the fresh candidate and the limits of the release tail. It does not claim a completed release.

- KidPlay remains version `0.7.4` / code `17`, with the existing six-word KP-PRO-037 summary `Integrated branding, music, and audit remediation`; no second version increment was made.
- The feedback-route context extraction and its detail, timed-session, and Would You Rather JVM coverage were committed as `16073b3e8fce69b8144b31127b59879f780a314a`. The candidate below was assembled from the same source contents before that commit; no app-source change followed the assembly.
- Fresh candidate: `app/build/outputs/apk/debug/20260925_KidPlay_v0.7.4.apk`, 25,535,866 bytes, SHA-256 `393caf9dca2c72a9a3005106c5952bc2b8261cba2c4d3daf5a73cedf58731333`. Its adjacent SHA-256 sidecar check returned `OK`.
- Deterministic gates reported for this build passed: 238/238 JVM tests, Android-test source compilation, lint, debug assembly, and the local APK-drop validator; script tests passed 13/13. These checks do not clear the connected-runtime gate.
- `:app:connectedDebugAndroidTest` failed with `DeviceException: No connected devices`; `adb devices -l` was empty and `/dev/kvm` was absent. The release status is therefore `candidate_only` / private WIP, not release-verified.

### Destination read-back and policy boundary

- Shared root inspected read-only: `/mnt/cyberforgex-ai/App Dev/KinPlay/apk-drops`. Its active APK inventory is exactly `20260912_KidPlay_v0.7.4.apk` and `20260823_DevLab_v0.2.3.apk`, with one APK and one checksum sidecar per registered app. The local policy validator passed with no errors.
- The active shared KidPlay APK independently read from that root is 25,535,866 bytes, SHA-256 `a5171e637316e56ad3e8e9304764f899716a63bd9673396d9c753da674c7870c`. It is not the fresh candidate hash above. The candidate was not copied into the shared root, and no active or archived file was changed; this is not a failed upload or a candidate read-back.
- The active profile's Google Workspace check returned `NOT_AUTHENTICATED` (no profile-local token). No live Drive listing, upload, download, rename, archive, or mutation was performed. Consequently, no Drive read-back or current one-APK inventory is claimed for this checkpoint; earlier owner-supplied Drive evidence remains historical, not a substitute for a fresh read.
- Physical-device checks were not performed and remain explicitly reserved for LJ. They are pending.

No APK destination was changed because connected instrumentation failed and Drive was inaccessible. Preserve the existing shared artifact and report the candidate as blocked from release verification until the connected gate and authorized Drive read-back can be completed.
