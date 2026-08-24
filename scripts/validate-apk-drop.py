#!/usr/bin/env python3
"""Validate the active local or Drive APK-drop inventory.

This is a release guard, not a cleanup tool. It fails closed when the active
folder contains an extra APK, a stale version, a temporary/status filename, an
orphan checksum sidecar, or an APK whose manifest does not match policy.
Superseded files must be moved to the reversible archive by the publication
workflow after the replacement passes byte verification.
"""

from __future__ import annotations

import argparse
import hashlib
import json
import os
import re
import shutil
import subprocess
import sys
from pathlib import Path
from typing import Any

APK_MIME = "application/vnd.android.package-archive"
PACKAGE_RE = re.compile(
    r"package:\s+name='(?P<package>[^']+)'\s+"
    r"versionCode='(?P<code>[^']+)'\s+versionName='(?P<name>[^']+)'"
)
SHA256_RE = re.compile(r"^(?P<digest>[0-9a-fA-F]{64})\s+(?P<name>\S+)\s*$")


def load_policy(path: Path) -> dict[str, Any]:
    policy = json.loads(path.read_text(encoding="utf-8"))
    apps = policy.get("registeredApps")
    if not isinstance(apps, list) or not apps:
        raise ValueError("policy must define at least one registeredApps entry")
    names = [app.get("activeFilename") for app in apps]
    if any(not isinstance(name, str) or not name.endswith(".apk") for name in names):
        raise ValueError("every registered app needs an activeFilename ending in .apk")
    if len(set(names)) != len(names):
        raise ValueError("registered active filenames must be unique")
    return policy


def sha256(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as handle:
        for chunk in iter(lambda: handle.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def find_aapt(explicit: str | None) -> str:
    candidates: list[Path] = []
    if explicit:
        candidates.append(Path(explicit))
    for command in (os.environ.get("AAPT"), shutil.which("aapt")):
        if command:
            candidates.append(Path(command))
    for sdk_var in ("ANDROID_HOME", "ANDROID_SDK_ROOT"):
        sdk = os.environ.get(sdk_var)
        if sdk:
            candidates.extend(sorted(Path(sdk).glob("build-tools/*/aapt"), reverse=True))
    for candidate in candidates:
        if candidate.is_file() and os.access(candidate, os.X_OK):
            return str(candidate)
    raise FileNotFoundError("aapt was not found; set --aapt or ANDROID_HOME")


def inspect_apk(path: Path, aapt: str) -> dict[str, str]:
    result = subprocess.run(
        [aapt, "dump", "badging", str(path)],
        check=False,
        capture_output=True,
        text=True,
    )
    output = result.stdout + result.stderr
    match = PACKAGE_RE.search(output)
    if result.returncode != 0 or not match:
        raise ValueError(f"could not read APK manifest metadata from {path.name}")
    return {
        "packageName": match.group("package"),
        "versionCode": match.group("code"),
        "versionName": match.group("name"),
    }


def validate_filename(name: str, policy: dict[str, Any], errors: list[str]) -> None:
    pattern = policy.get("activeFilenamePattern")
    if isinstance(pattern, str) and re.fullmatch(pattern, name) is None:
        errors.append(f"active APK filename violates policy: {name}")
    lowered = name.casefold()
    for token in policy.get("forbiddenActiveFilenameTokens", []):
        if str(token).casefold() in lowered:
            errors.append(f"active APK filename contains forbidden token '{token}': {name}")


def expected_by_name(policy: dict[str, Any]) -> dict[str, dict[str, Any]]:
    return {app["activeFilename"]: app for app in policy["registeredApps"]}


def validate_local(root: Path, policy: dict[str, Any], aapt: str) -> dict[str, Any]:
    errors: list[str] = []
    expected = expected_by_name(policy)
    archive = root / policy.get("archiveDirectoryName", "_archived-apk-drops")
    if not root.is_dir():
        return {"mode": "local", "status": "failed", "errors": [f"drop root is not a directory: {root}"]}
    if not archive.is_dir():
        errors.append(f"reversible archive directory is missing: {archive}")

    apk_files = sorted(path for path in root.iterdir() if path.is_file() and path.suffix.casefold() == ".apk")
    observed_names = {path.name for path in apk_files}
    expected_names = set(expected)
    for name in sorted(expected_names - observed_names):
        errors.append(f"missing registered active APK: {name}")
    for name in sorted(observed_names - expected_names):
        errors.append(f"unexpected APK in active drop: {name}")

    sidecars = sorted(path for path in root.iterdir() if path.is_file() and path.name.endswith(".apk.sha256"))
    expected_sidecars = {f"{name}.sha256" for name in expected_names}
    observed_sidecars = {path.name for path in sidecars}
    for name in sorted(expected_sidecars - observed_sidecars):
        errors.append(f"missing checksum sidecar: {name}")
    for name in sorted(observed_sidecars - expected_sidecars):
        errors.append(f"orphan checksum sidecar in active drop: {name}")

    observed: list[dict[str, Any]] = []
    for apk in apk_files:
        validate_filename(apk.name, policy, errors)
        record: dict[str, Any] = {"name": apk.name, "size": apk.stat().st_size}
        if apk.name in expected:
            app = expected[apk.name]
            try:
                metadata = inspect_apk(apk, aapt)
                record.update(metadata)
                for field in ("packageName", "versionName", "versionCode"):
                    expected_value = str(
                        app[{
                            "packageName": "packageName",
                            "versionName": "versionName",
                            "versionCode": "versionCode",
                        }[field]]
                    )
                    if metadata[field] != expected_value:
                        errors.append(
                            f"{apk.name} {field}={metadata[field]!r} does not match policy {expected_value!r}"
                        )
            except (OSError, ValueError, subprocess.SubprocessError) as exc:
                errors.append(str(exc))
        observed.append(record)

    for sidecar in sidecars:
        match = SHA256_RE.match(sidecar.read_text(encoding="utf-8").splitlines()[0] if sidecar.read_text(encoding="utf-8").splitlines() else "")
        if not match:
            errors.append(f"invalid checksum sidecar format: {sidecar.name}")
            continue
        apk_name = sidecar.name.removesuffix(".sha256")
        apk = root / apk_name
        if not apk.is_file():
            continue
        actual = sha256(apk)
        if match.group("digest").casefold() != actual:
            errors.append(f"checksum mismatch for {apk_name}: sidecar={match.group('digest')} actual={actual}")
        if match.group("name") != apk_name:
            errors.append(f"checksum sidecar names {match.group('name')} but is paired with {apk_name}")

    return {
        "mode": "local",
        "root": str(root),
        "status": "passed" if not errors else "failed",
        "expectedActiveApks": sorted(expected_names),
        "observedActiveApks": sorted(observed_names),
        "observed": observed,
        "errors": errors,
    }


def validate_drive_inventory(path: Path, policy: dict[str, Any]) -> dict[str, Any]:
    errors: list[str] = []
    payload = json.loads(path.read_text(encoding="utf-8"))
    entries = payload.get("files", []) if isinstance(payload, dict) else payload
    if not isinstance(entries, list):
        raise ValueError("Drive inventory must be a JSON list or an object with a files list")
    apk_entries = [
        item for item in entries
        if isinstance(item, dict)
        and (item.get("mimeType") == APK_MIME or str(item.get("name", "")).casefold().endswith(".apk"))
    ]
    expected_names = set(expected_by_name(policy))
    observed_names = [str(item.get("name", "")) for item in apk_entries]
    if len(observed_names) != len(set(observed_names)):
        errors.append("duplicate APK names are present in the active Drive inventory")
    for name in sorted(expected_names - set(observed_names)):
        errors.append(f"missing registered active Drive APK: {name}")
    for name in sorted(set(observed_names) - expected_names):
        errors.append(f"unexpected APK in active Drive folder: {name}")
    for name in observed_names:
        validate_filename(name, policy, errors)
    return {
        "mode": "drive-inventory",
        "inventory": str(path),
        "status": "passed" if not errors else "failed",
        "expectedActiveApks": sorted(expected_names),
        "observedActiveApks": sorted(observed_names),
        "errors": errors,
    }


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--policy", type=Path, default=Path("docs/launch/apk-drop-policy.json"))
    parser.add_argument("--local", type=Path, help="validate a local active drop root")
    parser.add_argument("--drive-inventory", type=Path, help="validate JSON from a live Drive active-folder query")
    parser.add_argument("--aapt", help="path to aapt for local APK manifest inspection")
    args = parser.parse_args()
    if bool(args.local) == bool(args.drive_inventory):
        parser.error("choose exactly one of --local or --drive-inventory")
    try:
        policy = load_policy(args.policy)
        if args.local:
            result = validate_local(args.local, policy, find_aapt(args.aapt))
        else:
            result = validate_drive_inventory(args.drive_inventory, policy)
    except (OSError, ValueError, json.JSONDecodeError) as exc:
        result = {"status": "failed", "errors": [str(exc)]}
    print(json.dumps(result, indent=2, sort_keys=True))
    return 0 if result.get("status") == "passed" else 1


if __name__ == "__main__":
    raise SystemExit(main())
