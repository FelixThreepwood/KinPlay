# KinPlay privacy policy draft

Status: draft for review and hosting; not a public policy
Draft date: 2026-08-05
App version described: `0.6.0-beta1`
Package: `com.kinplay.app`
Privacy contact for review: `[approved public address to be set before publication]`

This draft describes the current offline Android MVP. It must be reviewed for legal sufficiency, assigned a public effective date, and hosted at a stable URL before a Play Console submission. It must be revised before any analytics, crash reporting, advertising, purchases, accounts, cloud sync, remote content, or new data transfer is added.

## 1. What KinPlay does

KinPlay provides parent-led games and activities for family play. The current MVP ships its content inside the application and is designed to operate without an account or network connection.

## 2. Information KinPlay does not collect for its own operation

The current MVP does not operate an account system and does not include analytics, advertising, crash reporting, billing, cloud synchronization, remote content delivery, public sharing, or runtime artificial-intelligence services.

The application manifest declares no `uses-permission` entry. The current build does not request camera, microphone, contacts, location, storage, notification, or other dangerous Android runtime permissions.

KinPlay does not ask a family to provide a child name, exact birthdate, photograph, video, audio recording, or precise location for the MVP flow.

## 3. Information stored locally on the device

KinPlay stores application state in local Android preferences and in-memory or saved UI state. Current local state includes:

- selected settings such as timer, duration, rounds, color theme, and launcher-icon variant;
- favorite activity IDs and recently played activity IDs;
- Would You Rather shuffled-deck progress and an installation seed used to avoid immediate repetition;
- locally captured beta-feedback notes, when the tester chooses to save them;
- temporary form answers and current session state needed to render the active screen.

Feedback notes can contain the tester's selected category, impact, comment, expected result, screen, activity ID/title, creation time, app version, and optional device/Android context when the tester enables technical context. Testers must not enter child-identifying information into feedback comments.

The app does not transmit this local state during normal offline use. Android's application-data controls can clear local app data, and uninstalling the app removes app-private storage subject to Android platform behavior. Unsent feedback can also be edited or deleted through the in-app feedback workflow where those controls are available.

## 4. Optional feedback email handoff

The beta feedback flow can prepare a message for the device's email application. The current beta recipient is `FelixThreepwood@gmail.com`.

KinPlay uses Android's explicit `ACTION_SENDTO` email handoff. The tester reviews the prepared message and performs the final Send action in the email application. KinPlay does not silently send the message. If the tester uses the Copy batch option instead, the prepared text is placed on the device clipboard and remains subject to the device and clipboard application's behavior.

After the email application receives the message, the email provider and recipient control their own processing, storage, security, and deletion practices. This policy does not replace the email provider's policy.

## 5. Content entered during play

Mad Libs answers and other activity input are used to render the local play experience. The current MVP has no cloud account or remote content service for those answers. A tester must not copy child names, photos, audio, video, exact birthdates, or precise locations into feedback or other text fields.

## 6. Third-party components and services

The current release uses Android platform components, AndroidX libraries, Jetpack Compose, Kotlin runtime libraries, and Navigation Compose. No analytics, advertising, crash-reporting, billing, account, or cloud-storage SDK is included in the current release runtime dependency graph.

The Android email application is an optional user-selected handoff destination. Its handling of email content is controlled by its provider and the tester.

## 7. Parent-led use and children

KinPlay is intended for an adult to select, review, and guide an activity. It does not create child profiles or require a child to identify themselves. Adults remain responsible for choosing activities appropriate to their household, clearing the play area, and supervising movement and materials.

## 8. Data retention and requests

Local KinPlay state remains on the device until the tester removes it through the application's available controls, clears the application's storage, or uninstalls the application. Email records that a tester sends are retained or deleted according to the tester's email provider and the recipient's handling.

For privacy questions about the current MVP, use the approved public privacy contact address listed in the hosted policy. Do not send child names, photographs, audio, video, exact birthdates, or precise locations in a privacy request.

## 9. Changes to this policy

This policy must be updated before a release changes the application's data practices, permissions, SDK inventory, feedback recipient, account model, analytics, crash reporting, advertising, purchases, cloud synchronization, or remote content behavior.

## Publication checklist

- [ ] Legal/privacy review completed.
- [ ] Public URL selected and accessible without login.
- [ ] Effective date added.
- [ ] Contact address confirmed.
- [ ] Current APK manifest, SDK inventory, and Data safety answers reconciled.
- [ ] Email handoff disclosure reviewed against the selected Play Console declarations.
- [ ] Policy URL added to Play Console draft.
