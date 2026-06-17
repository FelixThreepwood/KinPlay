# KinPlay Systems and Access Review

## Current confirmed access

- GitHub: `FelixThreepwood/KinPlay`, Hermione has ADMIN via `gh`.
- Local repo: `/home/phantomatic/projects/app-pass-rev/KinPlay`.
- Android build foundation: user-local JDK 17 and Android SDK platform/build tools installed.
- Sudo: passwordless sudo available; use only with discretion.
- SMB/Cyberforgex: `/mnt/cyberforgex-torrents` is accessible from Hogwarts and writable.
- Physical Android devices: user has devices available for testing; USB/ADB connection still needs per-device verification.

## MVP-critical systems

1. GitHub repository and commits
   - Required now for source control, rollback, and handoff.
   - Review status: confirmed ready for MVP on 2026-06-17.
   - Current repo: `FelixThreepwood/KinPlay` on GitHub, default branch `main`, public visibility, Hermione has `ADMIN` access via `gh`.
   - PM decision: use GitHub as the canonical source of truth for code and durable checkpoints; keep Hermes Kanban as the working execution board for now. Do not create a GitHub Projects layer until MVP scope stabilizes.

2. Android SDK / JDK / Gradle wrapper
   - Required now for building APKs.
   - Keep SDK/JDK user-local unless system-wide need appears.
   - Review status: confirmed ready for MVP on 2026-06-17 after remediation.
   - JDK: Temurin OpenJDK 17.0.19 installed user-local at `~/.local/jdks/temurin-17`.
   - Android SDK: installed user-local at `~/Android/Sdk` with `platforms;android-35`, `build-tools;35.0.0`, and `platform-tools` / ADB 37.0.0.
   - Gradle wrapper: generated and verified with Gradle 8.13 via `./gradlew tasks --no-daemon`.
   - Project env helper: `scripts/android-env.sh` exports `JAVA_HOME`, `ANDROID_HOME`, `ANDROID_SDK_ROOT`, and Android SDK tools on `PATH`.

3. Physical Android device testing
   - Required before calling the demo usable.
   - Need to verify ADB, USB debugging, install permissions, and screen-size behavior.

4. Content schema and seed content workflow
   - Required now because KinPlay is content-led.
   - Use JSON assets first; avoid backend and runtime AI in MVP.

5. Kanban tracking
   - Recommended now because the project has multiple phases, dependencies, and human review gates.
   - Board created: `kinplay`.

## Beneficial soon, not MVP-blocking

1. Figma or design board
   - Useful for wireframes, visual system, icon/brand exploration, screenshots, and handoff.
   - Not required for the first Compose scaffold.

2. GitHub Issues/Projects
   - Useful if the user wants external project tracking visible in GitHub.
   - Hermes Kanban can remain the working board; GitHub Issues can mirror milestones later.

3. CI/CD through GitHub Actions
   - Useful after Gradle scaffold exists.
   - First workflow: build debug APK on push/PR.

4. Cyberforgex shared folder
   - Useful for APK drops, screenshots, exported docs, presentations, and test feedback files.
   - Do not store secrets there.

5. Google Play Console
   - Needed later for closed/internal testing and release.
   - Not needed for first local APK.

6. Privacy policy generator/legal review workflow
   - Needed before Play Store testing if any data collection or analytics are added.
   - MVP should avoid accounts, child profiles, ads, public UGC, camera, mic, location, and contacts.

7. Crash reporting/analytics
   - Consider after MVP flow is stable.
   - Prefer privacy-conscious minimal telemetry. Options: Firebase Crashlytics/Analytics or Sentry. Avoid until policy is clear.

8. Design/brand asset workflow
   - App icon, feature graphic, screenshots, mascot exploration.
   - Important before Play Store, not before scaffold.

9. Monetization systems
   - Google Play Billing for one-time Pro unlock/content packs later.
   - Do not add billing to MVP.

## Long-term optional systems

- Play Console internal/closed testing tracks.
- App Store/iOS path if product validates.
- Lightweight backend only if needed for sync, content updates, or subscriptions.
- Remote config/content delivery only after static content proves repeat use.
- Email/support inbox for tester feedback and eventual customer support.

## Recommended current critical path

1. Review this systems/access list.
2. Complete MVP spec and content schema.
3. Scaffold Android Compose app.
4. Add seed content including Mad Libs.
5. Build debug APK.
6. Test on physical Android devices.
7. Decide whether to add CI, Figma, Play Console, analytics, and monetization prep.
