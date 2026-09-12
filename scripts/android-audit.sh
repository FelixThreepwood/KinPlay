#!/usr/bin/env bash
# Project-local entry point for the KidPlay Android visual audit harness.
set -eu
SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
# Keep SDK discovery deterministic for non-interactive workers. The Python
# harness still passes absolute SDK tool paths to every subprocess.
if [ -f "$SCRIPT_DIR/android-env.sh" ]; then
  # shellcheck source=/dev/null
  . "$SCRIPT_DIR/android-env.sh"
fi
exec python3 "$SCRIPT_DIR/android_audit.py" "$@"
