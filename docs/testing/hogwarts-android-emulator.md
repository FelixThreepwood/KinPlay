# Hogwarts Android emulator

## Persistent configuration

- AVD: `Hogwarts_API35_Pixel7`
- AVD directory: `/home/phantomatic/.android/avd/Hogwarts_API35_Pixel7.avd`
- Image: Android API 35, Google APIs, x86_64
- Emulator: Android Emulator 37.1.11
- Acceleration: KVM via `/dev/kvm`
- Renderer: `swiftshader_indirect` software rendering
- Display mode: headless (`-no-window`); runtime interaction uses `adb`, screenshots, UIAutomator, and input events
- User service: `hogwarts-android-emulator.service`
- Service file: `/home/phantomatic/.config/systemd/user/hogwarts-android-emulator.service`
- Automatic startup: enabled through the user `default.target`
- Reboot persistence: user linger is enabled (`Linger=yes`)
- Restart policy: `Restart=always`, 15-second delay
- Data policy: the service does not use `-wipe-data`; installed apps and emulator data remain in the AVD data partition

## Health check

```bash
ADB="$HOME/Android/Sdk/platform-tools/adb"
"$ADB" devices -l
"$ADB" shell getprop sys.boot_completed
systemctl --user show hogwarts-android-emulator.service \\
  -p ActiveState -p SubState -p NRestarts -p ExecMainPID -p UnitFileState
```

A ready emulator reports `emulator-5554` as `device`, `sys.boot_completed=1`, and the service as `active/running` and `enabled`.

Useful service commands:

```bash
systemctl --user status hogwarts-android-emulator.service
systemctl --user restart hogwarts-android-emulator.service
journalctl --user -u hogwarts-android-emulator.service -e
```

## Verification performed

- SDK licenses accepted.
- Emulator package and API 35 system image installed.
- AVD created and listed by `avdmanager`.
- `systemd-analyze --user verify` passed for the service.
- User service enabled and started successfully.
- Cold boot reached `adb` state `device` with `sys.boot_completed=1`.
- Dev Lab `0.2.1` and KidPlay `0.7.0` package installations survived service-managed cold restarts.
- A newly saved Dev Lab feedback note survived a service-managed cold restart: `Unsent: 1` and `Created this app revision: 1` were read back from the live form.
- The Dev Lab spinner changed its selected result during live emulator interaction.

## Boundaries

The host was not rebooted during setup because that would interrupt Hogwarts. Boot persistence is configured through the enabled user unit and `Linger=yes`; the service lifecycle and AVD cold-restart behavior were exercised directly. Physical Pixel 8 Pro checks remain necessary for hardware performance, launcher behavior, accessibility services, and final usability validation.

The emulator is headless on this Hogwarts session because no graphical desktop is available to the computer-control layer. A future graphical session can use the same AVD with a windowed renderer if visual desktop interaction is required.
