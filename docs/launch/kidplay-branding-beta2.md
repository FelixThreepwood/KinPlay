# KidPlay branding beta — 0.7.0-beta2

## Scope

This private beta changes the visible Android application name from **KinPlay** to **KidPlay** and changes the active default launcher artwork to the selected Google Gemini Nano Banana concept `08_fox_heart.jpg`.

The application ID remains `com.kinplay.app`, and the existing `teal` launcher wire value and alias component name remain stable so previously stored settings continue to decode safely. The visible default launcher option is now **Fox Heart**. The existing optional Sunshine variant remains available.

## Asset provenance

- Source: `/mnt/cyberforgex-torrents/KinPlay/visual-concepts/20260807_app-icon-concepts/08_fox_heart.jpg`
- Dimensions: 1024 × 1024 JPEG
- Source SHA-256: `a8dd209cd588e0f1de4c9d58668b851ab477434ad5a5f93a5217dcb109bdbd5b`
- Packaged master: `app/src/main/res/drawable-nodpi/launcher_icon_fox_heart_master.jpg`
- Packaged-master SHA-256: `a8dd209cd588e0f1de4c9d58668b851ab477434ad5a5f93a5217dcb109bdbd5b`
- Source-to-package result: byte-identical
- Visual QA: centered orange fox curled around a gold heart on deep navy; no visible text or watermark; no obvious cropping or generation defect

## Build evidence

- Package: `com.kinplay.app`
- Visible application label: `KidPlay`
- Version name/code: `0.7.0-beta2` / `12`
- Debug APK: `/mnt/cyberforgex-torrents/KinPlay/apk-drops/working-builds/20260808_KidPlay_v0.7.0-beta2_FoxHeart_debug.apk`
- APK size: `23,103,966` bytes
- APK SHA-256: `1b1e74759cb518e04f9099ad4ca151c69ac4e36a7be5d3abaf57e0d77c53976e`
- Signature: APK Signature Scheme v2 verified
- Permissions: only the expected AndroidX dynamic receiver permission

The complete local release validator passed unit tests, Android test-source compilation, debug assembly, lint, packaged-content validation, APK badging, permission inspection, APK signature inspection, and `git diff --check`.

## Remaining physical gate

No Android device or emulator was connected during validation. Install/upgrade behavior, launcher cache refresh, touch behavior, accessibility services, large-font layout, rotation, and offline launch still require a physical-device or emulator retest.
