# KPF-0030 Session Configuration Specification

Status: implemented in B13–B14; pending physical retest

## Defaults

- Global activity duration uses the existing `ActivityDuration` setting: 5, 10, or 20 minutes.
- Global default rounds use `SessionRounds`: 3, 5, or 7 rounds.
- A new `SessionConfiguration` is immutable and contains one duration and one round count.
- The backward-compatible defaults are 10 minutes and 3 rounds.

## Precedence

For a game ID, the next-session override is resolved independently for duration and rounds:

1. use the stored per-game duration when present;
2. otherwise use the global activity duration;
3. use the stored per-game round count when present;
4. otherwise use the global default rounds.

A partial override therefore changes only one field. Resolving or previewing an override does not mutate global settings. Consuming a session configuration removes that game's override after the resolved immutable configuration has been returned; a later session uses the global defaults unless a new override is saved.

## Persistence and validation

The existing `SettingsKeyValueStore` boundary persists the following versioned keys:

- `settings_activity_duration_v1`
- `settings_default_rounds_v1`
- `settings_session_overrides_v1`

The override key stores a JSON object keyed by game ID. Unknown duration or round values are ignored; an override with no valid field is discarded. Invalid or malformed stored JSON falls back to no overrides. The finite enums reject values outside the supported duration and round choices. Blank game IDs cannot be saved as overrides.

## UI boundary

Settings exposes the persisted default-round choice and includes it in the current-plan summary. B14 adds the eligible details-page controls, visible applied values, reset-to-defaults action, and Start foundation. The UI saves one-shot overrides without changing global Settings values; Start consumes only the selected game's override and returns an immutable `TimedSession` for the B15 interactive surface.
