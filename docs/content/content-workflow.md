# KinPlay Content Workflow

## MVP decision

Use local JSON assets first. No backend, runtime AI, account system, remote config, or analytics are required for the first playable MVP.

## Content source of truth

- Schema: `content/kinplay-content.schema.json`
- Seed pack target: `content/seed/kinplay_seed_v1.json`
- Android asset target after scaffold: `app/src/main/assets/content/kinplay_seed_v1.json`

Until the Android app scaffold exists, content work happens under `content/` and is copied into Android assets during scaffold/integration.

## Authoring steps

1. Add or edit content in `content/seed/kinplay_seed_v1.json`.
2. Validate against `content/kinplay-content.schema.json`.
3. Check every item has:
   - clear age range
   - duration
   - safety tags
   - no unsafe permissions or online dependency
   - parent-readable wording
4. During Android integration, load this JSON from app assets.
5. Keep content IDs stable after release; retire old content with `status: retired` instead of deleting it.

## MVP content pack requirements

Minimum seed pack:

- 5 activity cards
- 3 Mad Libs templates
- 2 calm-down cards
- 2 prompt cards

## Validation command

Use Python once `jsonschema` is available:

```bash
python - <<'PY'
import json
from pathlib import Path
from jsonschema import Draft202012Validator
schema = json.loads(Path('content/kinplay-content.schema.json').read_text())
data = json.loads(Path('content/seed/kinplay_seed_v1.json').read_text())
Draft202012Validator(schema).validate(data)
print('content ok')
PY
```

If `jsonschema` is unavailable, at minimum run:

```bash
python -m json.tool content/kinplay-content.schema.json >/dev/null
python -m json.tool content/seed/kinplay_seed_v1.json >/dev/null
```

## Copy-to-app step after scaffold

```bash
mkdir -p app/src/main/assets/content
cp content/seed/kinplay_seed_v1.json app/src/main/assets/content/kinplay_seed_v1.json
```
