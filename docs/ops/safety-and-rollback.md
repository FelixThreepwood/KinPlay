# Safety and Rollback Policy

## Sudo policy

Hogwarts has passwordless sudo available for Hermione. Use it only when it materially improves the task.

Ask before running sudo commands that could alter critical system state, including:

- kernel, bootloader, GPU driver, network stack, firewall, Docker/container runtime, disk partitions, mounts, or system Python changes
- package removals or major upgrades
- commands that recursively delete, overwrite, or chmod/chown broad directories
- service restarts that could interrupt active workloads

Low-risk sudo uses may proceed when needed, such as reading system status or installing clearly scoped development dependencies, but prefer user-local tools when they are equally reliable.

## KinPlay project rollback

For app code, rollback is handled through Git:

1. Commit small changes frequently.
2. Use feature branches for risky work.
3. Before a large refactor, create a checkpoint branch or tag.
4. Verify with Gradle build/tests before merging.

Useful commands:

```bash
git status --short --branch
git branch checkpoint/<name>
git tag checkpoint-YYYYMMDD-HHMM
```

Rollback examples:

```bash
# Inspect recent history
git log --oneline --decorate -10

# Restore one file from main
git restore --source main -- path/to/file

# Revert a bad commit safely
git revert <commit-sha>
```

## System rollback

Hermes filesystem checkpoints can help for session-level file edits when available, but they do not replace OS snapshots for system package or driver changes.

For system-wide changes, use this order:

1. Prefer user-local install if it is adequate.
2. Record current state before changing packages:

```bash
dpkg-query -W > ~/system-package-list-before.txt
systemctl list-units --state=running > ~/running-services-before.txt
```

3. For risky system changes, create an OS-level snapshot first if a snapshot tool is configured.
4. If no OS snapshot tool is configured, ask before proceeding.

## Android toolchain decision

The Android SDK and JDK may remain user-local unless system-wide access becomes necessary. User-local Android SDKs are normal for Android development and reduce risk to unrelated services on Hogwarts.
