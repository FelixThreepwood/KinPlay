#!/usr/bin/env python3
"""Unit tests for the Android visual-interaction audit harness."""

from __future__ import annotations

import hashlib
import sys
import tempfile
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent))

from android_audit import (  # noqa: E402
    AuditError,
    AuditHarness,
    ArtifactStore,
    CommandResult,
    Device,
    find_semantic_node,
    parse_adb_devices,
    parse_bounds,
    resolve_sdk_tool,
    safe_label,
    sanitize_text,
    select_device,
)


class FakeRunner:
    def __init__(self) -> None:
        self.calls: list[tuple[str, ...]] = []

    def run(self, command: list[str] | tuple[str, ...], timeout: float = 30.0) -> CommandResult:
        del timeout
        call = tuple(command)
        self.calls.append(call)
        if call[-2:] == ("devices", "-l"):
            return CommandResult(call, 0, b"List of devices attached\nemulator-5554\tdevice product:sdk model:Pixel_7\n", b"")
        if call[-1:] == ("version",):
            return CommandResult(call, 0, b"Android Debug Bridge version 1.0.41\n", b"")
        if "getprop" in call and "sys.boot_completed" in call:
            return CommandResult(call, 0, b"1\n", b"")
        if "exec-out" in call and "screencap" in call:
            return CommandResult(call, 0, b"\x89PNG\r\n\x1a\nfake", b"")
        if "exec-out" in call and "cat" in call:
            return CommandResult(
                call,
                0,
                b'<hierarchy><node text="Home" bounds="[0,0][100,100]" /></hierarchy>',
                b"",
            )
        if "uiautomator" in call:
            return CommandResult(call, 0, b"UI hierchary dumped to: /sdcard/window.xml", b"")
        if "logcat" in call:
            return CommandResult(call, 0, b"09-12 20:00:00.000 I/KidPlay: ready\n", b"")
        if "install" in call:
            return CommandResult(call, 0, b"Success\n", b"")
        if "pm" in call and "clear" in call:
            return CommandResult(call, 0, b"Success\n", b"")
        if "am" in call and "start" in call:
            return CommandResult(call, 0, b"Status: ok\n", b"")
        return CommandResult(call, 0, b"", b"")


class NullTreeRunner(FakeRunner):
    def run(self, command: list[str] | tuple[str, ...], timeout: float = 30.0) -> CommandResult:
        call = tuple(command)
        if "exec-out" in call and "cat" in call:
            self.calls.append(call)
            return CommandResult(call, 0, b"null\n", b"")
        if "dumpsys" in call:
            self.calls.append(call)
            return CommandResult(call, 0, b"diagnostic output\n", b"")
        return super().run(command, timeout)


class AndroidAuditHelpersTest(unittest.TestCase):
    def test_prepare_runs_install_reset_launch_and_writes_initial_evidence(self) -> None:
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            apk = root / "app-debug.apk"
            apk.write_bytes(b"test apk")
            store = ArtifactStore(root / "bundle", run_id="bundle")
            runner = FakeRunner()
            harness = AuditHarness(
                adb=Path("/sdk/platform-tools/adb"),
                emulator=None,
                store=store,
                serial=None,
                package="com.kinplay.app",
                activity=".LauncherTeal",
                timeout=2.0,
                service_unit="unused.service",
                runner=runner,
            )

            result = harness.prepare(apk)

            self.assertEqual(result["status"], "passed")
            self.assertTrue((root / "bundle/screens/prepare.png").is_file())
            self.assertTrue((root / "bundle/accessibility/prepare.xml").is_file())
            self.assertTrue((root / "bundle/logs/prepare.log").is_file())
            self.assertTrue(any("install" in call for call in runner.calls))
            self.assertTrue(any("pm" in call and "clear" in call for call in runner.calls))
            self.assertTrue(any("am" in call and "start" in call for call in runner.calls))

    def test_accessibility_null_dump_writes_explicit_diagnostic_fallback(self) -> None:
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            store = ArtifactStore(root / "bundle", run_id="bundle")
            runner = NullTreeRunner()
            harness = AuditHarness(
                adb=Path("/sdk/platform-tools/adb"),
                emulator=None,
                store=store,
                serial=None,
                package="com.kinplay.app",
                activity=".LauncherTeal",
                timeout=2.0,
                service_unit="unused.service",
                runner=runner,
            )

            result = harness.dump_accessibility("home")

            self.assertEqual(result["status"], "fallback")
            self.assertEqual(result["kind"], "accessibility-diagnostic-fallback")
            self.assertTrue((root / "bundle/accessibility/home.fallback.txt").is_file())
            self.assertIn("diagnostic fallback", (root / "bundle/accessibility/home.fallback.txt").read_text(encoding="utf-8"))

    def test_parse_adb_devices_keeps_serial_state_and_details(self) -> None:
        devices = parse_adb_devices(
            "List of devices attached\n"
            "emulator-5554\tdevice product:sdk_gphone model:Pixel_7\n"
            "ABC123\toffline usb:1-2\n"
        )

        self.assertEqual(
            devices,
            [
                Device("emulator-5554", "device", "product:sdk_gphone model:Pixel_7"),
                Device("ABC123", "offline", "usb:1-2"),
            ],
        )

    def test_resolve_sdk_tool_uses_sdk_root_without_shell_path(self) -> None:
        with tempfile.TemporaryDirectory() as directory:
            sdk = Path(directory) / "sdk"
            adb = sdk / "platform-tools/adb"
            adb.parent.mkdir(parents=True)
            adb.write_bytes(b"adb")

            resolved = resolve_sdk_tool("adb", env={"ANDROID_HOME": str(sdk), "PATH": ""})

            self.assertEqual(resolved, adb.resolve())

    def test_select_device_requires_explicit_serial_when_multiple_are_ready(self) -> None:
        devices = [Device("emulator-5554", "device", ""), Device("pixel", "device", "")]

        with self.assertRaisesRegex(AuditError, "multiple ready Android targets"):
            select_device(devices)

    def test_select_device_rejects_requested_non_ready_target(self) -> None:
        with self.assertRaisesRegex(AuditError, "not ready"):
            select_device([Device("emulator-5554", "offline", "")], "emulator-5554")

    def test_sanitize_text_redacts_sensitive_and_personal_values(self) -> None:
        raw = (
            "Authorization: Bearer abc123 email=a.person@example.test "
            "phone=+1 (555) 010-1234 home=/home/tigger/private"
        )

        sanitized = sanitize_text(raw, home="/home/tigger")

        self.assertNotIn("abc123", sanitized)
        self.assertNotIn("a.person@example.test", sanitized)
        self.assertNotIn("+1 (555) 010-1234", sanitized)
        self.assertNotIn("/home/tigger", sanitized)
        self.assertIn("[REDACTED]", sanitized)

    def test_sanitize_text_does_not_redact_dotted_tool_versions_as_phone_numbers(self) -> None:
        sanitized = sanitize_text("Version 37.0.0-14910828 on 10.20.30.40")

        self.assertIn("37.0.0-14910828", sanitized)
        self.assertNotIn("10.20.30.40", sanitized)

    def test_safe_label_removes_path_separators(self) -> None:
        self.assertEqual(safe_label("../audit/run"), "audit-run")

    def test_parse_bounds_returns_centerable_coordinates(self) -> None:
        self.assertEqual(parse_bounds("[12,34][112,234]"), (12, 34, 112, 234))
        self.assertEqual(parse_bounds("null"), None)

    def test_find_semantic_node_matches_exact_text_and_content_description(self) -> None:
        xml = """
            <hierarchy>
              <node text=\"Random game\" content-desc=\"Choose a random game\" bounds=\"[20,30][220,130]\" />
              <node text=\"Random games\" content-desc=\"Other\" bounds=\"[0,0][1,1]\" />
            </hierarchy>
        """

        by_text = find_semantic_node(xml, text="Random game")
        by_description = find_semantic_node(xml, content_description="Choose a random game")

        self.assertEqual(by_text["bounds"], (20, 30, 220, 130))
        self.assertEqual(by_description["text"], "Random game")

    def test_find_semantic_node_rejects_ambiguous_matches(self) -> None:
        xml = """
            <hierarchy>
              <node text=\"Open\" bounds=\"[0,0][10,10]\" />
              <node text=\"Open\" bounds=\"[10,10][20,20]\" />
            </hierarchy>
        """

        with self.assertRaisesRegex(AuditError, "matched 2 nodes"):
            find_semantic_node(xml, text="Open")

    def test_artifact_store_writes_hash_sidecar_and_manifest_record(self) -> None:
        with tempfile.TemporaryDirectory() as directory:
            store = ArtifactStore(Path(directory), run_id="test-run")
            record = store.add_bytes("screens/home.png", b"png-bytes", kind="screenshot")
            manifest_path = store.write_manifest()

            artifact = Path(directory) / "screens/home.png"
            sidecar = Path(directory) / "screens/home.png.sha256"
            self.assertEqual(record["sha256"], hashlib.sha256(b"png-bytes").hexdigest())
            self.assertEqual(sidecar.read_text(encoding="utf-8").strip(), f"{record['sha256']}  home.png")
            self.assertTrue(manifest_path.is_file())
            self.assertEqual(store.manifest["artifacts"][0]["path"], "screens/home.png")
            self.assertEqual(artifact.read_bytes(), b"png-bytes")


if __name__ == "__main__":
    unittest.main()
