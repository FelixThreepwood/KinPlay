# KinPlay SDK and data inventory

Status: source-grounded draft
Inventory date: 2026-08-05
App version: `0.6.0-beta1` (version code `6`)
Package: `com.kinplay.app`

This inventory is based on the checked-in Gradle files, Android manifest, current application source, and the resolved `releaseRuntimeClasspath`. It is an engineering inventory, not a completed Google Play Data safety submission.

## Android package and permissions

| Field | Current value |
|---|---|
| Application ID | `com.kinplay.app` |
| Minimum SDK | 26 |
| Target SDK | 35 |
| Compile SDK | 35 |
| Manifest `uses-permission` entries | None |
| Main activity exported | `false` |
| Launcher aliases exported | `true`, with the standard `MAIN`/`LAUNCHER` intent filter |
| Android backup | Disabled with `allowBackup=false` and `fullBackupContent=false` |
| Network permission | Not declared |
| Camera/microphone/location/contacts/storage permissions | Not declared |

The exported launcher aliases are required for Android launch behavior and target the non-exported `MainActivity`. They are not data-collection components.

## Release runtime direct dependencies

These are the direct application/runtime declarations in `app/build.gradle.kts` and their resolved versions observed on 2026-08-05:

| Dependency | Resolved version | Runtime purpose | Data practice |
|---|---:|---|---|
| `org.jetbrains.kotlin:kotlin-stdlib` | 2.1.21 | Kotlin runtime | No KinPlay data service |
| `androidx.compose:compose-bom` | 2025.06.01 | Aligns Compose modules | UI framework |
| `androidx.activity:activity-compose` | 1.10.1 | Activity/Compose integration | Android lifecycle/UI |
| `androidx.compose.material3:material3` | 1.3.2 | Native Material 3 UI components | UI framework |
| `androidx.compose.ui:ui` | 1.8.3 | Compose UI runtime | UI framework |
| `androidx.compose.ui:ui-tooling-preview` | 1.8.3 | Preview annotations/runtime support | UI development support |
| `androidx.core:core-ktx` | 1.16.0 | Android compatibility helpers | Platform support |
| `androidx.navigation:navigation-compose` | 2.9.0 | Local in-app navigation | No remote navigation service |

The resolved graph also contains transitive AndroidX, Compose, Kotlin, lifecycle, saved-state, coroutine, and annotation modules. Regenerate the release dependency report before each store submission:

```bash
source scripts/android-env.sh
./gradlew --no-daemon :app:dependencies --configuration releaseRuntimeClasspath
```

## Test and debug-only dependencies

The following are not part of the release runtime dependency graph:

- JUnit;
- Gson;
- `org.json` test dependency used by JVM tests; Android supplies the platform class used by production code;
- Robolectric;
- NetworkNT JSON Schema Validator;
- AndroidX test and Compose UI test libraries;
- Compose tooling and test manifest debug dependencies.

A release APK inspection remains authoritative over this source inventory.

## Local data stores

| Store or state | Data | Network transfer |
|---|---|---|
| SharedPreferences `kinplay` | Settings, favorite IDs, recent IDs | None in current app |
| SharedPreferences `kinplay_feedback` | Encoded local feedback records and lifecycle state | Only included in an email handoff after tester action |
| SharedPreferences `would_you_rather_progress` | Shuffled-deck state and installation seed | None in current app |
| Compose `rememberSaveable` / saved UI state | Current route, form values, timer/session restoration state | None in current app |
| Bundled JSON assets | Reviewed content and Would You Rather prompt library | Read locally from the APK |

The current source does not add a database, content provider, background service, broadcast receiver, web client, analytics client, crash client, billing client, or cloud SDK.

## Outbound surfaces

1. **Email:** `ACTION_SENDTO` with a `mailto:` URI for an explicit tester-reviewed feedback handoff.
2. **Clipboard:** optional Copy batch action using Android's clipboard service.
3. **Launcher aliases:** enable/disable the predeclared Teal or Sunshine launcher alias; this changes local component state only.

No silent network upload is present in the current MVP path.

## Play Console Data safety preparation

The current engineering evidence supports the following preliminary posture:

- No account creation or sign-in.
- No advertising or analytics SDK.
- No crash-reporting SDK.
- No location, contacts, camera, microphone, or storage permission.
- No cloud synchronization or remote content service.
- Local application state remains on the device unless a tester explicitly hands off selected feedback to an email application.

The optional feedback email must be reviewed against the current Play Console Data safety questionnaire. Do not submit a definitive answer from this engineering document alone.

## Reconciliation gates before store upload

- [ ] Re-run `releaseRuntimeClasspath` inventory.
- [ ] Inspect final APK manifest and permissions with `aapt`.
- [ ] Verify the packaged APK contains no unexpected network or telemetry component.
- [ ] Reconcile privacy policy, Data safety answers, store listing, and feedback-recipient disclosure.
- [ ] Re-check every new dependency before it enters the release graph.
- [ ] Record any future crash, analytics, billing, account, cloud, or remote-content addition as a new privacy review gate.
