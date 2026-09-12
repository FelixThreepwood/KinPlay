#!/usr/bin/env python3
"""Repeatable, privacy-conscious visual audit harness for the KidPlay APK.

The harness deliberately uses argument lists instead of shell commands.  It can
operate against a physical/attached device or an emulator selected by --serial.
Every invocation writes a JSON result and an appendable evidence manifest under
build/android-audit/<run-id> by default.
"""

from __future__ import annotations

import argparse
import hashlib
import json
import os
import re
import shutil
import subprocess
import time
from dataclasses import dataclass
from datetime import datetime, timezone
from pathlib import Path
from typing import Any, Mapping, Sequence
from xml.etree import ElementTree

PROJECT_ROOT = Path(__file__).resolve().parents[1]
DEFAULT_PACKAGE = "com.kinplay.app"
DEFAULT_ACTIVITY = ".LauncherTeal"
DEFAULT_APK = PROJECT_ROOT / "app/build/outputs/apk/debug/app-debug.apk"
DEFAULT_ARTIFACT_ROOT = PROJECT_ROOT / "build/android-audit"
DEFAULT_SERVICE_UNIT = "hogwarts-android-emulator.service"
MAX_LOGCAT_BYTES = 2_000_000


class AuditError(RuntimeError):
    """A harness operation could not be completed safely."""


class TargetUnavailable(AuditError):
    """No ready Android target is available for a device operation."""


@dataclass(frozen=True)
class Device:
    serial: str
    state: str
    details: str


@dataclass(frozen=True)
class CommandResult:
    command: tuple[str, ...]
    returncode: int
    stdout: bytes
    stderr: bytes

    @property
    def stdout_text(self) -> str:
        return self.stdout.decode("utf-8", errors="replace")

    @property
    def stderr_text(self) -> str:
        return self.stderr.decode("utf-8", errors="replace")


class CommandRunner:
    """Small subprocess boundary that never invokes a shell."""

    def run(self, command: Sequence[str], timeout: float = 30.0) -> CommandResult:
        command_tuple = tuple(str(part) for part in command)
        try:
            completed = subprocess.run(
                list(command_tuple),
                capture_output=True,
                check=False,
                timeout=timeout,
            )
        except FileNotFoundError as exc:
            raise AuditError(f"command not found: {command_tuple[0]}") from exc
        except subprocess.TimeoutExpired as exc:
            raise AuditError(f"command timed out after {timeout:g}s: {command_tuple[0]}") from exc
        return CommandResult(
            command=command_tuple,
            returncode=completed.returncode,
            stdout=completed.stdout,
            stderr=completed.stderr,
        )


def sha256_bytes(value: bytes) -> str:
    return hashlib.sha256(value).hexdigest()


def sha256_file(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as handle:
        for chunk in iter(lambda: handle.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def parse_adb_devices(output: str) -> list[Device]:
    """Parse `adb devices -l` without treating offline/unauthorized rows as ready."""
    devices: list[Device] = []
    for raw_line in output.splitlines():
        line = raw_line.strip()
        if not line or line.lower().startswith("list of devices attached"):
            continue
        if "\t" in line:
            serial, remainder = line.split("\t", 1)
        else:
            parts = line.split(None, 2)
            if len(parts) < 2:
                continue
            serial, remainder = parts[0], " ".join(parts[1:])
        parts = remainder.split(None, 1)
        state = parts[0] if parts else "unknown"
        details = parts[1] if len(parts) > 1 else ""
        devices.append(Device(serial=serial, state=state, details=details))
    return devices


def select_device(devices: Sequence[Device], requested: str | None = None) -> str:
    ready = [device for device in devices if device.state == "device"]
    if requested:
        matching = next((device for device in devices if device.serial == requested), None)
        if matching is None:
            raise TargetUnavailable(f"requested Android target is not listed: {requested}")
        if matching.state != "device":
            raise TargetUnavailable(f"requested Android target {requested} is not ready (state={matching.state})")
        return matching.serial
    if not ready:
        states = ", ".join(f"{device.serial}={device.state}" for device in devices) or "none"
        raise TargetUnavailable(f"no ready Android target (observed: {states})")
    if len(ready) > 1:
        serials = ", ".join(device.serial for device in ready)
        raise AuditError(f"multiple ready Android targets; pass --serial ({serials})")
    return ready[0].serial


def parse_bounds(value: str | None) -> tuple[int, int, int, int] | None:
    if not value:
        return None
    match = re.fullmatch(r"\[(\d+),(\d+)\]\[(\d+),(\d+)\]", value.strip())
    if not match:
        return None
    left, top, right, bottom = (int(part) for part in match.groups())
    if right <= left or bottom <= top:
        return None
    return left, top, right, bottom


def find_semantic_node(
    xml: str,
    *,
    text: str | None = None,
    content_description: str | None = None,
    resource_id: str | None = None,
) -> dict[str, Any]:
    selectors = [value for value in (text, content_description, resource_id) if value is not None]
    if len(selectors) != 1:
        raise AuditError("provide exactly one of --text, --content-description, or --resource-id")
    try:
        root = ElementTree.fromstring(xml)
    except ElementTree.ParseError as exc:
        raise AuditError(f"accessibility XML is invalid: {exc}") from exc

    matches: list[dict[str, Any]] = []
    for element in root.iter("node"):
        if text is not None and element.attrib.get("text") != text:
            continue
        if content_description is not None and element.attrib.get("content-desc") != content_description:
            continue
        if resource_id is not None and element.attrib.get("resource-id") != resource_id:
            continue
        bounds = parse_bounds(element.attrib.get("bounds"))
        if bounds is None:
            continue
        matches.append(
            {
                "text": element.attrib.get("text", ""),
                "contentDescription": element.attrib.get("content-desc", ""),
                "resourceId": element.attrib.get("resource-id", ""),
                "className": element.attrib.get("class", ""),
                "bounds": bounds,
                "clickable": element.attrib.get("clickable", "false") == "true",
            }
        )
    if not matches:
        selector = text if text is not None else content_description if content_description is not None else resource_id
        raise AuditError(f"accessibility tree matched no node for {selector!r}")
    if len(matches) > 1:
        selector = text if text is not None else content_description if content_description is not None else resource_id
        raise AuditError(f"accessibility tree matched {len(matches)} nodes for {selector!r}; use a unique selector")
    return matches[0]


def sanitize_text(value: str, *, home: str | None = None) -> str:
    """Remove common personal/credential values before text enters an evidence bundle."""
    text = value
    if home:
        text = text.replace(str(Path(home).expanduser()), "[HOME]")
    text = re.sub(r"(?i)(authorization\s*:\s*bearer\s+)[^\s]+", r"\1[REDACTED]", text)
    text = re.sub(
        r"(?i)((?:api[_-]?key|secret|password|passwd|token|private[_-]?key)\s*[:=]\s*)[^\s,;]+",
        r"\1[REDACTED]",
        text,
    )
    text = re.sub(r"[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}", "[EMAIL]", text)
    text = re.sub(
        r"(?<![\d.])(?:\+\d[\d ()-]{7,}\d|\(\d{3}\)[ -]?\d{3}[ -]?\d{4}|\d{3}[ -]\d{3}[ -]\d{4})(?![\d.])",
        "[PHONE]",
        text,
    )
    text = re.sub(r"(?<![\d.])(?:\d{1,3}\.){3}\d{1,3}(?![\d.])", "[IP]", text)
    return text


def safe_label(value: str) -> str:
    label = re.sub(r"[^A-Za-z0-9._-]+", "-", value).strip(".-")
    if not label:
        raise AuditError("artifact label must contain at least one safe character")
    return label[:96]


def new_run_id() -> str:
    now = datetime.now(timezone.utc)
    return now.strftime("%Y%m%dT%H%M%SZ")


def _utc_now() -> str:
    return datetime.now(timezone.utc).isoformat(timespec="seconds").replace("+00:00", "Z")


class ArtifactStore:
    """Write evidence files plus a verifiable, appendable manifest."""

    def __init__(self, root: Path, run_id: str) -> None:
        self.root = root.expanduser().resolve()
        self.root.mkdir(parents=True, exist_ok=True)
        existing = self.root / "manifest.json"
        if existing.is_file():
            try:
                loaded = json.loads(existing.read_text(encoding="utf-8"))
            except (OSError, json.JSONDecodeError) as exc:
                raise AuditError(f"cannot read existing evidence manifest: {existing}") from exc
            if loaded.get("runId") != run_id:
                raise AuditError(f"evidence directory already belongs to run {loaded.get('runId')!r}")
            self.manifest: dict[str, Any] = loaded
            self.manifest.setdefault("artifacts", [])
            self.manifest.setdefault("actions", [])
        else:
            self.manifest = {
                "schemaVersion": "kidplay-android-visual-audit/v1",
                "runId": run_id,
                "createdAtUtc": _utc_now(),
                "updatedAtUtc": _utc_now(),
                "artifacts": [],
                "actions": [],
            }

    def _safe_path(self, relative_path: str) -> tuple[Path, str]:
        relative = Path(relative_path)
        if relative.is_absolute() or ".." in relative.parts:
            raise AuditError(f"artifact path escapes evidence root: {relative_path}")
        normalized = relative.as_posix()
        if not normalized or normalized == ".":
            raise AuditError("artifact path must name a file")
        return self.root / relative, normalized

    def add_bytes(self, relative_path: str, data: bytes, *, kind: str) -> dict[str, Any]:
        target, normalized = self._safe_path(relative_path)
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_bytes(data)
        digest = sha256_bytes(data)
        sidecar = target.with_name(target.name + ".sha256")
        sidecar.write_text(f"{digest}  {target.name}\n", encoding="utf-8")
        record = {
            "path": normalized,
            "kind": kind,
            "sizeBytes": len(data),
            "sha256": digest,
            "sha256Sidecar": f"{normalized}.sha256",
        }
        self.manifest["artifacts"] = [
            item for item in self.manifest.get("artifacts", []) if item.get("path") != normalized
        ]
        self.manifest["artifacts"].append(record)
        self.manifest["updatedAtUtc"] = _utc_now()
        return record

    def add_text(self, relative_path: str, text: str, *, kind: str) -> dict[str, Any]:
        return self.add_bytes(relative_path, text.encode("utf-8"), kind=kind)

    def add_action(self, name: str, status: str, details: Mapping[str, Any] | None = None) -> None:
        action: dict[str, Any] = {"name": name, "status": status, "atUtc": _utc_now()}
        if details:
            action["details"] = dict(details)
        self.manifest.setdefault("actions", []).append(action)
        self.manifest["updatedAtUtc"] = _utc_now()

    def write_manifest(self) -> Path:
        target = self.root / "manifest.json"
        target.write_text(json.dumps(self.manifest, indent=2, sort_keys=True) + "\n", encoding="utf-8")
        return target


def _tool_candidates(name: str, sdk: Path) -> list[Path]:
    if name == "adb":
        relative = Path("platform-tools")
        filenames = ("adb", "adb.exe")
    elif name == "emulator":
        relative = Path("emulator")
        filenames = ("emulator", "emulator.exe")
    elif name == "aapt":
        build_tools = sdk / "build-tools"
        versions = sorted((path for path in build_tools.glob("*") if path.is_dir()), reverse=True)
        return [version / "aapt" for version in versions] + [version / "aapt.exe" for version in versions]
    else:
        raise AuditError(f"unsupported Android SDK tool: {name}")
    return [sdk / relative / filename for filename in filenames]


def resolve_sdk_tool(
    name: str,
    explicit: str | None = None,
    *,
    env: Mapping[str, str] | None = None,
    project_root: Path | None = None,
) -> Path:
    """Resolve SDK tools by explicit path or SDK roots, never by shell PATH."""
    environment = os.environ if env is None else env
    candidates: list[Path] = []
    if explicit:
        candidates.append(Path(explicit).expanduser())
    roots: list[Path] = []
    for variable in ("ANDROID_HOME", "ANDROID_SDK_ROOT"):
        value = environment.get(variable)
        if value:
            roots.append(Path(value).expanduser())
    if project_root:
        roots.append(project_root / ".android-sdk")
    roots.append(Path.home() / "Android/Sdk")
    seen: set[Path] = set()
    for root in roots:
        for candidate in _tool_candidates(name, root):
            if candidate not in seen:
                candidates.append(candidate)
                seen.add(candidate)
    for candidate in candidates:
        if candidate.is_file():
            return candidate.resolve()
    attempted = ", ".join(str(path) for path in candidates)
    raise AuditError(f"{name} was not found at an explicit SDK path ({attempted})")


def _truncate(value: str, max_chars: int) -> str:
    if len(value) <= max_chars:
        return value
    marker = "\n[TRUNCATED]\n"
    remaining = max_chars - len(marker)
    head = max(0, remaining // 2)
    tail = max(0, remaining - head)
    return value[:head] + marker + value[-tail:]


def _parse_properties(output: str) -> dict[str, str]:
    properties: dict[str, str] = {}
    for line in output.splitlines():
        if "=" in line:
            key, value = line.split("=", 1)
            properties[key] = value
    return properties


def _display_command(result: CommandResult, *, home: str | None = None) -> str:
    return sanitize_text(" ".join(result.command), home=home)


class AuditHarness:
    def __init__(
        self,
        *,
        adb: Path,
        emulator: Path | None,
        store: ArtifactStore,
        serial: str | None,
        package: str,
        activity: str,
        timeout: float,
        service_unit: str,
        runner: CommandRunner | None = None,
    ) -> None:
        self.adb = adb
        self.emulator = emulator
        self.store = store
        self.serial = serial
        self.package = package
        self.activity = activity
        self.timeout = timeout
        self.service_unit = service_unit
        self.runner = runner or CommandRunner()
        self.home = str(Path.home())
        self.store.manifest.setdefault("toolchain", {})["adb"] = str(adb)
        if emulator:
            self.store.manifest["toolchain"]["emulator"] = str(emulator)
        self.store.manifest.setdefault("target", {})["requestedSerial"] = serial
        self.store.manifest["target"]["package"] = package
        self.store.manifest["target"]["activity"] = activity

    def _run(self, command: Sequence[str], *, timeout: float | None = None, check: bool = True) -> CommandResult:
        result = self.runner.run(command, timeout=self.timeout if timeout is None else timeout)
        if check and result.returncode != 0:
            details = _truncate(
                sanitize_text(result.stdout_text + result.stderr_text, home=self.home).strip(),
                1_000,
            )
            suffix = f": {details}" if details else ""
            raise AuditError(f"command failed ({result.returncode}): {_display_command(result, home=self.home)}{suffix}")
        return result

    def _adb(self, *arguments: str, include_serial: bool = True) -> list[str]:
        command = [str(self.adb)]
        if include_serial and self.serial:
            command.extend(("-s", self.serial))
        command.extend(arguments)
        return command

    def devices(self) -> list[Device]:
        result = self._run(self._adb("devices", "-l", include_serial=False), check=False)
        if result.returncode != 0:
            raise AuditError(_truncate(sanitize_text(result.stderr_text, home=self.home), 500))
        return parse_adb_devices(result.stdout_text)

    def ensure_device(self) -> str:
        selected = select_device(self.devices(), self.serial)
        self.serial = selected
        self.store.manifest.setdefault("target", {})["serial"] = selected
        boot = self._run(self._adb("shell", "getprop", "sys.boot_completed"), check=False)
        if boot.stdout_text.strip() != "1":
            raise TargetUnavailable(f"Android target {selected} is connected but not boot-complete")
        return selected

    def _device_run(self, *arguments: str, timeout: float | None = None, check: bool = True) -> CommandResult:
        self.ensure_device()
        return self._run(self._adb(*arguments), timeout=timeout, check=check)

    def health(self) -> dict[str, Any]:
        result: dict[str, Any] = {
            "status": "unavailable",
            "adb": {"path": str(self.adb)},
            "host": {
                "kvm": {
                    "path": "/dev/kvm",
                    "present": Path("/dev/kvm").exists(),
                    "accessible": os.access("/dev/kvm", os.R_OK | os.W_OK),
                }
            },
            "target": {"requestedSerial": self.serial},
        }
        try:
            version = self._run([str(self.adb), "version"], check=False)
            result["adb"]["version"] = _truncate(sanitize_text(version.stdout_text, home=self.home), 2_000).strip()
            devices = self.devices()
            result["target"]["devices"] = [
                {"serial": item.serial, "state": item.state, "details": item.details} for item in devices
            ]
            if self.emulator:
                emulator_list = self._run([str(self.emulator), "-list-avds"], check=False)
                result["emulator"] = {
                    "path": str(self.emulator),
                    "avds": [line.strip() for line in emulator_list.stdout_text.splitlines() if line.strip()],
                }
                acceleration = self._run([str(self.emulator), "-accel-check"], check=False)
                result["emulator"]["accelCheck"] = _truncate(
                    sanitize_text(acceleration.stdout_text + acceleration.stderr_text, home=self.home),
                    1_000,
                ).strip()
            try:
                selected = select_device(devices, self.serial)
            except AuditError as exc:
                result["reason"] = str(exc)
            else:
                self.serial = selected
                boot = self._run(self._adb("shell", "getprop", "sys.boot_completed"), check=False)
                sdk = self._run(self._adb("shell", "getprop", "ro.build.version.sdk"), check=False)
                size = self._run(self._adb("shell", "wm", "size"), check=False)
                density = self._run(self._adb("shell", "wm", "density"), check=False)
                package_path = self._run(self._adb("shell", "pm", "path", self.package), check=False)
                activity_state = self._run(self._adb("shell", "dumpsys", "activity", "activities"), check=False)
                activity_lines = [
                    line.strip()
                    for line in activity_state.stdout_text.splitlines()
                    if self.package in line and ("mResumedActivity" in line or "mFocusedApp" in line)
                ]
                result["target"].update(
                    {
                        "serial": selected,
                        "bootCompleted": boot.stdout_text.strip(),
                        "apiLevel": sdk.stdout_text.strip(),
                        "wmSize": _truncate(sanitize_text(size.stdout_text, home=self.home), 500).strip(),
                        "wmDensity": _truncate(sanitize_text(density.stdout_text, home=self.home), 500).strip(),
                        "appInstalled": package_path.stdout_text.strip().startswith("package:"),
                        "foregroundApp": bool(activity_lines),
                    }
                )
                result["status"] = "ready" if boot.stdout_text.strip() == "1" else "not-boot-complete"
        except AuditError as exc:
            result["reason"] = str(exc)

        systemctl = shutil.which("systemctl")
        if systemctl:
            service = self._run(
                [systemctl, "--user", "show", self.service_unit, "-p", "ActiveState", "-p", "SubState", "-p", "NRestarts", "-p", "ExecMainPID", "-p", "UnitFileState"],
                check=False,
            )
            result["service"] = {"unit": self.service_unit, **_parse_properties(service.stdout_text)}
        self.store.manifest["health"] = result
        self.store.add_action("health", result["status"], {"reason": result.get("reason", "")})
        return result

    def _component(self) -> str:
        if "/" in self.activity:
            return self.activity
        if self.activity.startswith("."):
            return f"{self.package}/{self.activity}"
        return f"{self.package}/{self.activity}"

    def _require_apk(self, apk: Path | None) -> Path:
        path = (apk or DEFAULT_APK).expanduser()
        if not path.is_file():
            raise AuditError(f"APK does not exist: {path}")
        return path.resolve()

    def install(self, apk: Path | None) -> dict[str, Any]:
        path = self._require_apk(apk)
        digest = sha256_file(path)
        self.store.manifest["apk"] = {"path": str(path), "sha256": digest}
        result = self._device_run("install", "-r", "-d", str(path), timeout=max(self.timeout, 120.0))
        output = _truncate(sanitize_text(result.stdout_text + result.stderr_text, home=self.home), 2_000).strip()
        if "success" not in output.casefold():
            raise AuditError(f"APK install did not report Success: {output}")
        payload = {"status": "passed", "apk": str(path), "apkSha256": digest, "output": output}
        self.store.add_action("install", "passed", {"apk": path.name, "apkSha256": digest})
        return payload

    def reset(self) -> dict[str, Any]:
        self._device_run("shell", "am", "force-stop", self.package)
        cleared = self._device_run("shell", "pm", "clear", self.package)
        output = _truncate(sanitize_text(cleared.stdout_text + cleared.stderr_text, home=self.home), 1_000).strip()
        if "success" not in output.casefold():
            raise AuditError(f"package data reset did not report Success: {output}")
        payload = {"status": "passed", "package": self.package, "output": output}
        self.store.add_action("reset", "passed", {"package": self.package})
        return payload

    def launch(self) -> dict[str, Any]:
        component = self._component()
        result = self._device_run("shell", "am", "start", "-W", "-n", component)
        output = _truncate(sanitize_text(result.stdout_text + result.stderr_text, home=self.home), 2_000).strip()
        if re.search(r"(?im)^\s*error:", output):
            raise AuditError(f"activity launch reported an error: {output}")
        payload = {"status": "passed", "component": component, "output": output}
        self.store.add_action("launch", "passed", {"component": component})
        return payload

    def prepare(self, apk: Path | None) -> dict[str, Any]:
        installed = self.install(apk)
        reset = self.reset()
        launched = self.launch()
        screenshot = self.capture_screenshot("prepare")
        accessibility = self.dump_accessibility("prepare")
        logs = self.capture_logcat("prepare")
        return {
            "status": "passed",
            "install": installed,
            "reset": reset,
            "launch": launched,
            "screenshot": screenshot,
            "accessibility": {key: value for key, value in accessibility.items() if not key.startswith("_")},
            "logcat": logs,
        }

    def capture_screenshot(self, label: str) -> dict[str, Any]:
        normalized = safe_label(label)
        result = self._device_run("exec-out", "screencap", "-p")
        data = result.stdout
        if not data.startswith(b"\x89PNG\r\n\x1a\n"):
            raise AuditError("screencap did not return a PNG")
        record = self.store.add_bytes(f"screens/{normalized}.png", data, kind="screenshot")
        payload = {"status": "passed", **record}
        self.store.add_action("capture-screenshot", "passed", {"path": record["path"]})
        return payload

    def record_screen(self, label: str, seconds: int) -> dict[str, Any]:
        if seconds < 1 or seconds > 60:
            raise AuditError("screen recording duration must be between 1 and 60 seconds")
        normalized = safe_label(label)
        remote = f"/sdcard/kidplay-audit-{safe_label(self.store.manifest['runId'])}-{normalized}.mp4"
        local = self.store.root / "recordings" / f"{normalized}.part.mp4"
        local.parent.mkdir(parents=True, exist_ok=True)
        try:
            self._device_run(
                "shell",
                "screenrecord",
                "--time-limit",
                str(seconds),
                "--bit-rate",
                "4000000",
                remote,
                timeout=seconds + self.timeout,
            )
            pulled = self._device_run("pull", remote, str(local), timeout=max(self.timeout, 120.0))
            if not local.is_file() or local.stat().st_size == 0:
                raise AuditError(f"screenrecord pull produced no file: {pulled.stdout_text.strip()}")
            record = self.store.add_bytes(
                f"recordings/{normalized}.mp4",
                local.read_bytes(),
                kind="screen-recording",
            )
            payload = {"status": "passed", **record}
            self.store.add_action("record-screen", "passed", {"path": record["path"], "seconds": seconds})
            return payload
        finally:
            try:
                self._device_run("shell", "rm", "-f", remote, check=False)
            except AuditError:
                pass
            local.unlink(missing_ok=True)

    def capture_logcat(self, label: str) -> dict[str, Any]:
        normalized = safe_label(label)
        result = self._device_run("logcat", "-d", "-v", "threadtime", timeout=max(self.timeout, 60.0))
        text = sanitize_text(result.stdout_text + result.stderr_text, home=self.home)
        text = _truncate(text, MAX_LOGCAT_BYTES)
        record = self.store.add_text(f"logs/{normalized}.log", text, kind="sanitized-logcat")
        payload = {"status": "passed", **record}
        self.store.add_action("capture-logcat", "passed", {"path": record["path"]})
        return payload

    def dump_accessibility(self, label: str) -> dict[str, Any]:
        normalized = safe_label(label)
        remote = f"/sdcard/kidplay-audit-{safe_label(self.store.manifest['runId'])}-window.xml"
        dump = self._device_run("shell", "uiautomator", "dump", "--compressed", remote, check=False)
        try:
            xml_result = self._device_run("exec-out", "cat", remote, check=False)
            xml = xml_result.stdout_text.strip()
        finally:
            try:
                self._device_run("shell", "rm", "-f", remote, check=False)
            except AuditError:
                pass
        is_null_dump = xml.strip().casefold() == "null"
        parsed_tree: ElementTree.Element | None = None
        if dump.returncode == 0 and xml_result.returncode == 0 and not is_null_dump:
            try:
                parsed_tree = ElementTree.fromstring(xml)
            except ElementTree.ParseError:
                parsed_tree = None
        valid_tree = parsed_tree is not None and (
            parsed_tree.tag == "hierarchy" or parsed_tree.tag.endswith("}hierarchy")
        )
        if valid_tree:
            sanitized_xml = sanitize_text(xml, home=self.home)
            record = self.store.add_text(f"accessibility/{normalized}.xml", sanitized_xml, kind="uiautomator-tree")
            node_count = sum(1 for element in parsed_tree.iter() if element.tag == "node")
            payload: dict[str, Any] = {
                "status": "uiautomator",
                "nodeCount": node_count,
                **record,
                "_xml": sanitized_xml,
            }
            self.store.add_action("capture-accessibility", "uiautomator", {"path": record["path"], "nodeCount": node_count})
            return payload

        accessibility = self._device_run("shell", "dumpsys", "accessibility", check=False)
        windows = self._device_run("shell", "dumpsys", "window", "windows", check=False)
        fallback = (
            "UIAutomator tree unavailable or returned null.\n"
            "This diagnostic fallback is not an equivalent accessibility tree.\n"
            "Use coordinate input only after visual review, and treat semantic input as unavailable.\n\n"
            "--- dumpsys accessibility ---\n"
            f"{sanitize_text(accessibility.stdout_text + accessibility.stderr_text, home=self.home)}\n"
            "--- dumpsys window windows ---\n"
            f"{sanitize_text(windows.stdout_text + windows.stderr_text, home=self.home)}\n"
        )
        record = self.store.add_text(f"accessibility/{normalized}.fallback.txt", _truncate(fallback, 500_000), kind="accessibility-diagnostic-fallback")
        reason = _truncate(sanitize_text(dump.stdout_text + dump.stderr_text, home=self.home).strip(), 1_000)
        payload = {
            "status": "fallback",
            "reason": reason or "UIAutomator returned null or invalid XML",
            **record,
        }
        self.store.add_action("capture-accessibility", "fallback", {"path": record["path"], "reason": payload["reason"]})
        return payload

    def tap(self, x: int, y: int, label: str | None = None) -> dict[str, Any]:
        if x < 0 or y < 0:
            raise AuditError("tap coordinates must be non-negative")
        self._device_run("shell", "input", "tap", str(x), str(y))
        payload: dict[str, Any] = {"status": "passed", "action": "tap", "x": x, "y": y}
        self.store.add_action("tap", "passed", {"x": x, "y": y, "label": label or ""})
        if label:
            payload["screenshot"] = self.capture_screenshot(label)
        return payload

    def swipe(self, x1: int, y1: int, x2: int, y2: int, duration_ms: int, label: str | None = None) -> dict[str, Any]:
        if min(x1, y1, x2, y2) < 0:
            raise AuditError("swipe coordinates must be non-negative")
        if duration_ms < 0 or duration_ms > 60_000:
            raise AuditError("swipe duration must be between 0 and 60000 milliseconds")
        self._device_run("shell", "input", "swipe", str(x1), str(y1), str(x2), str(y2), str(duration_ms))
        payload: dict[str, Any] = {"status": "passed", "action": "swipe", "durationMs": duration_ms}
        self.store.add_action("swipe", "passed", {"x1": x1, "y1": y1, "x2": x2, "y2": y2, "durationMs": duration_ms})
        if label:
            payload["screenshot"] = self.capture_screenshot(label)
        return payload

    def back(self, label: str | None = None) -> dict[str, Any]:
        self._device_run("shell", "input", "keyevent", "4")
        payload: dict[str, Any] = {"status": "passed", "action": "back"}
        self.store.add_action("back", "passed", {})
        if label:
            payload["screenshot"] = self.capture_screenshot(label)
        return payload

    def semantic_tap(
        self,
        *,
        text: str | None,
        content_description: str | None,
        resource_id: str | None,
        label: str,
    ) -> dict[str, Any]:
        tree = self.dump_accessibility(f"{safe_label(label)}-tree")
        if tree["status"] != "uiautomator":
            raise AuditError(
                "UIAutomator returned null or invalid XML; semantic input is unavailable. "
                f"Diagnostic fallback: {tree['path']}"
            )
        node = find_semantic_node(
            tree["_xml"],
            text=text,
            content_description=content_description,
            resource_id=resource_id,
        )
        left, top, right, bottom = node["bounds"]
        tap_result = self.tap((left + right) // 2, (top + bottom) // 2)
        screenshot = self.capture_screenshot(label)
        return {"status": "passed", "node": {key: value for key, value in node.items() if key != "bounds"}, "bounds": node["bounds"], "tap": tap_result, "screenshot": screenshot}

    def stress_tap(self, x: int, y: int, count: int, interval: float, label: str) -> dict[str, Any]:
        if count < 1 or count > 1_000:
            raise AuditError("stress-tap count must be between 1 and 1000")
        if interval < 0 or interval > 5:
            raise AuditError("stress-tap interval must be between 0 and 5 seconds")
        self.ensure_device()
        for index in range(count):
            self._device_run("shell", "input", "tap", str(x), str(y))
            if index + 1 < count and interval:
                time.sleep(interval)
        self.store.add_action("stress-tap", "passed", {"x": x, "y": y, "count": count, "intervalSeconds": interval})
        return {"status": "passed", "action": "stress-tap", "x": x, "y": y, "count": count, "intervalSeconds": interval, "screenshot": self.capture_screenshot(label)}

    def _setting(self, namespace: str, key: str) -> str:
        result = self._device_run("shell", "settings", "get", namespace, key)
        return result.stdout_text.strip()

    def _put_setting(self, namespace: str, key: str, value: str) -> None:
        self._device_run("shell", "settings", "put", namespace, key, value)

    def _restore_setting(self, namespace: str, key: str, value: str) -> None:
        if value in ("", "null", "Null", "null\r"):
            self._device_run("shell", "settings", "delete", namespace, key, check=False)
        else:
            self._put_setting(namespace, key, value)

    def scenario_orientation(self, orientation: str) -> dict[str, Any]:
        if orientation not in {"portrait", "landscape"}:
            raise AuditError("orientation must be portrait or landscape")
        old_accelerometer = self._setting("system", "accelerometer_rotation")
        old_rotation = self._setting("system", "user_rotation")
        target_rotation = "0" if orientation == "portrait" else "1"
        try:
            self._put_setting("system", "accelerometer_rotation", "0")
            self._put_setting("system", "user_rotation", target_rotation)
            self.launch()
            screenshot = self.capture_screenshot(f"orientation-{orientation}")
            tree = self.dump_accessibility(f"orientation-{orientation}")
            result = {"status": "passed", "orientation": orientation, "screenshot": screenshot, "accessibility": {key: value for key, value in tree.items() if not key.startswith("_")}}
        finally:
            self._restore_setting("system", "accelerometer_rotation", old_accelerometer)
            self._restore_setting("system", "user_rotation", old_rotation)
            self.launch()
        self.store.add_action("scenario-orientation", "passed", {"orientation": orientation})
        return result

    def scenario_font_scale(self, scale: float) -> dict[str, Any]:
        if scale < 0.8 or scale > 2.0:
            raise AuditError("font scale must be between 0.8 and 2.0")
        old_scale = self._setting("system", "font_scale")
        try:
            self._put_setting("system", "font_scale", f"{scale:g}")
            self.launch()
            screenshot = self.capture_screenshot(f"font-scale-{scale:g}")
            tree = self.dump_accessibility(f"font-scale-{scale:g}")
            result = {"status": "passed", "fontScale": scale, "screenshot": screenshot, "accessibility": {key: value for key, value in tree.items() if not key.startswith("_")}}
        finally:
            self._restore_setting("system", "font_scale", old_scale)
            self.launch()
        self.store.add_action("scenario-font-scale", "passed", {"fontScale": scale})
        return result

    def scenario_window_size(self, size: str) -> dict[str, Any]:
        match = re.fullmatch(r"(\d{3,5})x(\d{3,5})", size)
        if not match:
            raise AuditError("window size must use WIDTHxHEIGHT, for example 800x1280")
        width, height = (int(part) for part in match.groups())
        if width < 320 or height < 320:
            raise AuditError("window dimensions must each be at least 320 pixels")
        current = self._device_run("shell", "wm", "size").stdout_text
        override_match = re.search(r"Override size:\s*(\d+x\d+)", current)
        old_override = override_match.group(1) if override_match else None
        try:
            self._device_run("shell", "wm", "size", f"{width}x{height}")
            self.launch()
            screenshot = self.capture_screenshot(f"window-size-{width}x{height}")
            tree = self.dump_accessibility(f"window-size-{width}x{height}")
            result = {"status": "passed", "windowSize": f"{width}x{height}", "screenshot": screenshot, "accessibility": {key: value for key, value in tree.items() if not key.startswith("_")}}
        finally:
            if old_override:
                self._device_run("shell", "wm", "size", old_override, check=False)
            else:
                self._device_run("shell", "wm", "size", "reset", check=False)
            self.launch()
        self.store.add_action("scenario-window-size", "passed", {"windowSize": f"{width}x{height}"})
        return result

    def _wifi_enabled(self) -> bool | None:
        result = self._device_run("shell", "cmd", "wifi", "status", check=False)
        match = re.search(r"(?i)wi-fi is (enabled|disabled)", result.stdout_text)
        return match and match.group(1).casefold() == "enabled"

    def scenario_offline(self, duration: float) -> dict[str, Any]:
        if duration < 0 or duration > 60:
            raise AuditError("offline duration must be between 0 and 60 seconds")
        wifi_before = self._wifi_enabled()
        mobile_before = self._setting("global", "mobile_data")
        try:
            self._device_run("shell", "svc", "wifi", "disable")
            self._device_run("shell", "svc", "data", "disable")
            self.launch()
            if duration:
                time.sleep(duration)
            screenshot = self.capture_screenshot("offline")
            tree = self.dump_accessibility("offline")
            logs = self.capture_logcat("offline")
            result = {"status": "passed", "offline": True, "screenshot": screenshot, "accessibility": {key: value for key, value in tree.items() if not key.startswith("_")}, "logcat": logs}
        finally:
            if wifi_before is True:
                self._device_run("shell", "svc", "wifi", "enable", check=False)
            elif wifi_before is False:
                self._device_run("shell", "svc", "wifi", "disable", check=False)
            if mobile_before == "1":
                self._device_run("shell", "svc", "data", "enable", check=False)
            elif mobile_before == "0":
                self._device_run("shell", "svc", "data", "disable", check=False)
            self.launch()
        self.store.add_action("scenario-offline", "passed", {"durationSeconds": duration})
        return result

    def scenario_back_navigation(self, count: int) -> dict[str, Any]:
        if count < 1 or count > 20:
            raise AuditError("back-navigation count must be between 1 and 20")
        captures: list[dict[str, Any]] = []
        for index in range(count):
            captures.append(self.back(f"back-{index + 1}"))
        self.store.add_action("scenario-back-navigation", "passed", {"count": count})
        return {"status": "passed", "count": count, "captures": captures}


def _scrub_private_keys(value: Any) -> Any:
    if isinstance(value, dict):
        return {key: _scrub_private_keys(item) for key, item in value.items() if not str(key).startswith("_")}
    if isinstance(value, list):
        return [_scrub_private_keys(item) for item in value]
    return value


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description=__doc__)
    subparsers = parser.add_subparsers(dest="command", required=True)

    def add_common(subparser: argparse.ArgumentParser) -> None:
        subparser.add_argument("--adb", help="absolute path to adb; SDK roots are resolved without PATH")
        subparser.add_argument("--emulator", help="absolute path to emulator for health AVD listing")
        subparser.add_argument("--serial", default=os.environ.get("ANDROID_SERIAL"), help="ADB serial; required when multiple targets are ready")
        subparser.add_argument("--package", default=DEFAULT_PACKAGE)
        subparser.add_argument("--activity", default=DEFAULT_ACTIVITY)
        subparser.add_argument("--artifacts", type=Path, default=DEFAULT_ARTIFACT_ROOT, help="evidence root; a run-id subdirectory is created")
        subparser.add_argument("--run-id", help="stable directory name for appending actions to one evidence bundle")
        subparser.add_argument("--timeout", type=float, default=30.0)
        subparser.add_argument("--service-unit", default=DEFAULT_SERVICE_UNIT)

    def add(name: str, help_text: str) -> argparse.ArgumentParser:
        subparser = subparsers.add_parser(name, help=help_text)
        add_common(subparser)
        return subparser

    add("health", "report SDK, ADB, emulator, service, and target readiness")
    prepare = add("prepare", "install, clear data, launch, and capture initial evidence")
    prepare.add_argument("--apk", type=Path)
    install = add("install", "install an APK without clearing package data")
    install.add_argument("--apk", type=Path)
    add("reset", "force-stop and clear the package data")
    add("launch", "launch the configured activity explicitly")
    capture = add("capture", "capture a PNG screenshot and SHA-256 sidecar")
    capture.add_argument("--label", default="screen")
    record = add("record", "capture a short MP4 screen recording and hash it")
    record.add_argument("--label", default="screen")
    record.add_argument("--seconds", type=int, default=5)
    logs = add("logcat", "capture sanitized logcat and hash it")
    logs.add_argument("--label", default="logcat")
    accessibility = add("accessibility", "capture UIAutomator tree or documented diagnostic fallback")
    accessibility.add_argument("--label", default="screen")
    tap = add("tap", "send a coordinate tap")
    tap.add_argument("x", type=int)
    tap.add_argument("y", type=int)
    tap.add_argument("--label")
    swipe = add("swipe", "send a coordinate swipe")
    swipe.add_argument("x1", type=int)
    swipe.add_argument("y1", type=int)
    swipe.add_argument("x2", type=int)
    swipe.add_argument("y2", type=int)
    swipe.add_argument("--duration-ms", type=int, default=300)
    swipe.add_argument("--label")
    semantic = add("semantic-tap", "find one unique accessibility node and tap its center")
    selector = semantic.add_mutually_exclusive_group(required=True)
    selector.add_argument("--text")
    selector.add_argument("--content-description")
    selector.add_argument("--resource-id")
    semantic.add_argument("--label", default="semantic-tap")
    back = add("back", "send Android Back")
    back.add_argument("--label")
    stress = add("stress-tap", "repeat a coordinate tap and capture the final screen")
    stress.add_argument("x", type=int)
    stress.add_argument("y", type=int)
    stress.add_argument("--count", type=int, default=25)
    stress.add_argument("--interval", type=float, default=0.1)
    stress.add_argument("--label", default="stress-tap-final")
    offline = add("offline", "disable radios temporarily, capture, then restore prior state")
    offline.add_argument("--duration", type=float, default=2.0)
    orientation = add("orientation", "exercise a portrait or landscape configuration and restore it")
    orientation.add_argument("orientation", choices=("portrait", "landscape"))
    font_scale = add("font-scale", "exercise a font-scale configuration and restore it")
    font_scale.add_argument("scale", type=float)
    window_size = add("window-size", "exercise a wm size override and restore it")
    window_size.add_argument("size")
    back_nav = add("back-navigation", "send repeated Back events with screenshots")
    back_nav.add_argument("--count", type=int, default=3)
    scenario = add("scenario", "run one named visual-interaction scenario")
    scenario.add_argument("name", choices=("orientation", "font-scale", "window-size", "offline", "back-navigation", "stress-tap"))
    scenario.add_argument("--orientation", choices=("portrait", "landscape"))
    scenario.add_argument("--scale", type=float)
    scenario.add_argument("--size")
    scenario.add_argument("--duration", type=float, default=2.0)
    scenario.add_argument("--count", type=int, default=3)
    scenario.add_argument("--x", type=int)
    scenario.add_argument("--y", type=int)
    scenario.add_argument("--interval", type=float, default=0.1)
    scenario.add_argument("--label", default="stress-tap-final")
    return parser


def _dispatch(harness: AuditHarness, args: argparse.Namespace) -> dict[str, Any]:
    command = args.command
    if command == "health":
        return harness.health()
    if command == "prepare":
        return harness.prepare(args.apk)
    if command == "install":
        return harness.install(args.apk)
    if command == "reset":
        return harness.reset()
    if command == "launch":
        return harness.launch()
    if command == "capture":
        return harness.capture_screenshot(args.label)
    if command == "record":
        return harness.record_screen(args.label, args.seconds)
    if command == "logcat":
        return harness.capture_logcat(args.label)
    if command == "accessibility":
        return {key: value for key, value in harness.dump_accessibility(args.label).items() if not key.startswith("_")}
    if command == "tap":
        return harness.tap(args.x, args.y, args.label)
    if command == "swipe":
        return harness.swipe(args.x1, args.y1, args.x2, args.y2, args.duration_ms, args.label)
    if command == "semantic-tap":
        return harness.semantic_tap(text=args.text, content_description=args.content_description, resource_id=args.resource_id, label=args.label)
    if command == "back":
        return harness.back(args.label)
    if command == "stress-tap":
        return harness.stress_tap(args.x, args.y, args.count, args.interval, args.label)
    if command == "offline":
        return harness.scenario_offline(args.duration)
    if command == "orientation":
        return harness.scenario_orientation(args.orientation)
    if command == "font-scale":
        return harness.scenario_font_scale(args.scale)
    if command == "window-size":
        return harness.scenario_window_size(args.size)
    if command == "back-navigation":
        return harness.scenario_back_navigation(args.count)
    if command == "scenario":
        if args.name == "orientation":
            if not args.orientation:
                raise AuditError("scenario orientation requires --orientation")
            return harness.scenario_orientation(args.orientation)
        if args.name == "font-scale":
            if args.scale is None:
                raise AuditError("scenario font-scale requires --scale")
            return harness.scenario_font_scale(args.scale)
        if args.name == "window-size":
            if not args.size:
                raise AuditError("scenario window-size requires --size")
            return harness.scenario_window_size(args.size)
        if args.name == "offline":
            return harness.scenario_offline(args.duration)
        if args.name == "back-navigation":
            return harness.scenario_back_navigation(args.count)
        if args.x is None or args.y is None:
            raise AuditError("scenario stress-tap requires --x and --y")
        return harness.stress_tap(args.x, args.y, args.count, args.interval, args.label)
    raise AuditError(f"unsupported command: {command}")


def main(argv: Sequence[str] | None = None) -> int:
    parser = build_parser()
    args = parser.parse_args(argv)
    run_id = safe_label(args.run_id or new_run_id())
    store = ArtifactStore(args.artifacts / run_id, run_id)
    result: dict[str, Any]
    exit_code = 0
    try:
        adb = resolve_sdk_tool("adb", args.adb, project_root=Path(__file__).resolve().parents[1])
        try:
            emulator = resolve_sdk_tool("emulator", args.emulator, project_root=Path(__file__).resolve().parents[1])
        except AuditError:
            emulator = None
        harness = AuditHarness(
            adb=adb,
            emulator=emulator,
            store=store,
            serial=args.serial,
            package=args.package,
            activity=args.activity,
            timeout=args.timeout,
            service_unit=args.service_unit,
        )
        result = _dispatch(harness, args)
        if result.get("status") in {"unavailable", "not-boot-complete"}:
            exit_code = 2
    except TargetUnavailable as exc:
        result = {"status": "unavailable", "error": str(exc)}
        store.add_action(args.command, "unavailable", {"error": str(exc)})
        exit_code = 2
    except AuditError as exc:
        result = {"status": "failed", "error": str(exc)}
        store.add_action(args.command, "failed", {"error": str(exc)})
        exit_code = 1
    except (OSError, ValueError) as exc:
        result = {"status": "failed", "error": str(exc)}
        store.add_action(args.command, "failed", {"error": str(exc)})
        exit_code = 1
    result = _scrub_private_keys(result)
    store.add_text("result.json", json.dumps(result, indent=2, sort_keys=True) + "\n", kind="command-result")
    store.write_manifest()
    print(json.dumps({"runId": run_id, "artifactRoot": str(store.root), "result": result}, indent=2, sort_keys=True))
    return exit_code


if __name__ == "__main__":
    raise SystemExit(main())
