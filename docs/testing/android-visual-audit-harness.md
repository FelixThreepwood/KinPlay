# KidPlay Android visual-interaction audit harness

This harness provides a repeatable, ADB-driven audit loop for the current KidPlay
APK. It is intended for a dedicated Android emulator or an explicitly attached
Android device. It complements physical Pixel and family-device checks; it does not
replace them.

## Scope and boundary

The harness can:

- report SDK, ADB, AVD, user-service, boot, API-level, display-size, and density health;
- install a selected APK, clear the app data, and launch the configured component in a
  deterministic order;
- send coordinate taps, swipes, Android Back events, and repeated taps;
- dump the UIAutomator accessibility tree and use a unique text, content-description,
  or resource-id selector for semantic input when a tree is available;
- capture PNG screenshots, short MP4 recordings, sanitized logcat, and hashes;
- exercise portrait/landscape, font-scale, window-size, and offline scenarios while
  restoring the prior device settings;
- record a repeated-tap stress case and a bounded Back-navigation sequence; and
- write a JSON result and appendable evidence manifest for every run.

The emulator is not evidence of physical Pixel performance, hardware rendering,
launcher-cache behavior, TalkBack/service behavior, keyboard behavior, audio output,
real-world touch feel, battery use, or family usability. Those checks remain an
owner/device gate. A harness result of `unavailable` or `not-boot-complete` is negative
runtime evidence, not a pass.

## Toolchain and target selection

Use the project wrapper so a non-interactive worker does not depend on an inherited
shell `PATH`:

```bash
scripts/android-audit.sh health
```

The Python entry point resolves `adb`, `emulator`, and `aapt` only from an explicit
path, `ANDROID_HOME`, `ANDROID_SDK_ROOT`, a project-local `.android-sdk`, or the
standard `$HOME/Android/Sdk` location. It does not use `PATH` for SDK tools. Override
an individual tool when needed:

```bash
scripts/android-audit.sh health \
  --adb /absolute/path/to/Android/Sdk/platform-tools/adb \
  --emulator /absolute/path/to/Android/Sdk/emulator/emulator
```

When more than one target is ready, pass `--serial`. An `offline` or `unauthorized`
target is never selected as ready:

```bash
scripts/android-audit.sh health --serial emulator-5554
```

The default app target is `com.kinplay.app/.LauncherTeal`, and the default APK is
`app/build/outputs/apk/debug/app-debug.apk`. Use `--package`, `--activity`, and
`--apk` for another explicitly reviewed target. The activity is started explicitly;
the harness does not rely on launcher discovery.

## Evidence bundles

The default evidence root is `build/android-audit/<run-id>`, which is ignored by Git.
Use one stable `--run-id` when several commands belong to one audit bundle. A new
command appends its action and replaces only same-path artifacts in the existing
manifest:

```bash
RUN_ID=kp-pro-038-$(date -u +%Y%m%dT%H%M%SZ)
scripts/android-audit.sh prepare --run-id "$RUN_ID"
scripts/android-audit.sh capture --run-id "$RUN_ID" --label home-after-prepare
```

A bundle contains:

- `manifest.json`: schema version, UTC timestamps, resolved tool paths, target,
  health, actions, and artifact metadata;
- `result.json`: sanitized result for the last command;
- `screens/*.png` and matching `.sha256` sidecars;
- `recordings/*.mp4` and matching `.sha256` sidecars;
- `logs/*.log` (sanitized logcat) and matching `.sha256` sidecars; and
- `accessibility/*.xml` when UIAutomator succeeds, or `*.fallback.txt` when the
  diagnostic fallback is required, with matching `.sha256` sidecars.

A sidecar uses the standard form `<sha256>  <filename>`. The manifest repeats the
hash, size, artifact kind, and sidecar path. Verify an artifact with:

```bash
sha256sum build/android-audit/<run-id>/screens/home-after-prepare.png
cat build/android-audit/<run-id>/screens/home-after-prepare.png.sha256
```

Screenshots and recordings are not transformed by the harness. Before sharing a
bundle, inspect them for accidental personal content and delete the bundle if it is
not suitable for sharing. Text artifacts are sanitized for home paths, common
credential assignments, bearer values, email addresses, phone numbers, and IPv4
addresses. Sanitization is a precaution, not a guarantee of anonymity; use a
synthetic emulator account and synthetic app data.

Do not place raw logcat or screenshots in tracked documentation. The generated
bundle path is intentionally under `build/android-audit/`.

## Deterministic preparation and basic actions

`prepare` performs install, force-stop/data clear, explicit launch, screenshot,
accessibility capture, and logcat capture. It fails if there is no ready target or if
any step does not report success:

```bash
scripts/android-audit.sh prepare \
  --run-id kp-pro-038-<timestamp> \
  --apk app/build/outputs/apk/debug/app-debug.apk
```

The individual operations are available for a longer bundle:

```bash
scripts/android-audit.sh install --run-id <id> --apk app/build/outputs/apk/debug/app-debug.apk
scripts/android-audit.sh reset --run-id <id>
scripts/android-audit.sh launch --run-id <id>
scripts/android-audit.sh capture --run-id <id> --label home
scripts/android-audit.sh accessibility --run-id <id> --label home
scripts/android-audit.sh logcat --run-id <id> --label after-home
scripts/android-audit.sh record --run-id <id> --label home-flow --seconds 5
```

`reset` uses `am force-stop` followed by `pm clear`; it does not uninstall the
package. `install` uses `adb install -r -d`, records the APK SHA-256, and does not
clear data. Use `prepare` when a clean app-data state is required.

Coordinate and semantic input are separate paths:

```bash
scripts/android-audit.sh tap --run-id <id> 540 1200 --label after-tap
scripts/android-audit.sh swipe --run-id <id> 540 1900 540 500 --duration-ms 400
scripts/android-audit.sh back --run-id <id> --label after-back
scripts/android-audit.sh semantic-tap --run-id <id> \
  --text "All games and activities" --label all-games
```

Semantic input requires exactly one unique matching UIAutomator node with valid
bounds. Ambiguous or missing matches fail closed. The recorded tap uses the center
of the node bounds. Coordinate input is available when semantic input is not.
Coordinate values are passed as separate ADB arguments; no shell expression is
constructed from user input.

## Scenarios

Each scenario captures the changed surface and an accessibility result when possible.
Configuration values are restored in a `finally` path, followed by a relaunch:

```bash
scripts/android-audit.sh orientation --run-id <id> landscape
scripts/android-audit.sh orientation --run-id <id> portrait
scripts/android-audit.sh font-scale --run-id <id> 1.5
scripts/android-audit.sh window-size --run-id <id> 800x1280
scripts/android-audit.sh offline --run-id <id> --duration 2
scripts/android-audit.sh stress-tap --run-id <id> 540 1200 --count 25 --interval 0.1
scripts/android-audit.sh back-navigation --run-id <id> --count 3
```

The equivalent `scenario` command is useful for scripts that dispatch by name:

```bash
scripts/android-audit.sh scenario --run-id <id> orientation --orientation landscape
scripts/android-audit.sh scenario --run-id <id> font-scale --scale 1.5
scripts/android-audit.sh scenario --run-id <id> window-size --size 800x1280
scripts/android-audit.sh scenario --run-id <id> offline --duration 2
scripts/android-audit.sh scenario --run-id <id> stress-tap --x 540 --y 1200 --count 25
```

Offline mode disables Wi-Fi and mobile data with `svc`, captures the app, and
restores the observed Wi-Fi/mobile-data state when it can be determined. It is
appropriate for the dedicated emulator or an explicitly approved test device. The
command does not claim that every OEM radio or captive-portal state was tested.

Window-size mode uses `wm size` and restores an existing override or calls
`wm size reset`. Orientation mode temporarily disables automatic rotation and sets
`user_rotation` to portrait or landscape, then restores both settings. Font-scale
mode changes `system/font_scale`, relaunches the app, captures, and restores it.

## UIAutomator null fallback

`accessibility` first runs:

```text
adb shell uiautomator dump --compressed /sdcard/<temporary-window-file>
adb exec-out cat /sdcard/<temporary-window-file>
```

When the dump is valid XML containing a hierarchy, it is written as
`accessibility/<label>.xml`. If UIAutomator returns `null`, invalid XML, or a failed
file read, the harness instead stores:

- `dumpsys accessibility`; and
- `dumpsys window windows`.

The fallback is explicitly marked `accessibility-diagnostic-fallback` and is not an
equivalent accessibility tree. Semantic input is refused in this state; use a
visually reviewed coordinate action only, and record the fallback as a validation
gap. The temporary XML file is removed from the target in both paths.

## Health and exit codes

`health` records `/dev/kvm` presence/accessibility, the ADB version, listed targets,
AVD names and emulator acceleration output when the emulator binary is available,
user-service properties when `systemctl` is available, and for one selected target:
`sys.boot_completed`, API level, package-installed state, foreground-app state,
`wm size`, and `wm density`.

Exit codes are:

- `0`: requested operation passed, or health reported a ready target;
- `1`: harness or operation failure; and
- `2`: no ready target or the target has not completed boot.

Example negative evidence on a host without KVM/ADB target:

```bash
scripts/android-audit.sh health --run-id kp-pro-038-health
# Inspect build/android-audit/kp-pro-038-health/result.json and manifest.json.
```

Do not convert an exit code `2` bundle into an instrumentation pass. If the
configured x86_64 AVD cannot boot because `/dev/kvm` is unavailable, probe an
ADB-exposed Windows-host emulator or explicitly attached device once, then retain
the exact negative result if neither target exists.

## Verification

Run the harness unit tests and syntax checks from the repository root:

```bash
python3 -m unittest scripts.test_android_audit -v
python3 -m py_compile scripts/android_audit.py scripts/test_android_audit.py
bash -n scripts/android-audit.sh
```

The tests cover ADB row parsing and target selection, strict semantic-node matching,
bounds parsing, credential/personal-value redaction, and hash-sidecar/manifest
creation. Device operations require a live target and must be reported separately
from source compilation or unit-test results.
