# KidPlay asset QA and provenance

Status: companion to `/DESIGN.md`
Scope: original illustrations, instructional images, fox states, icons, audio-linked visual cues, and any future generated asset

## 1. Asset policy

KidPlay uses visual assets only when they improve comprehension, orientation, or state recognition. Activity cards remain text-first. Assets must not become a requirement for understanding the rule, safety instruction, result, or next action.

Google Gemini Nano Banana is the exclusive image-generation path for new KidPlay visuals. Do not use a different image generator, vector construction workflow, programmatic geometry, copied reference artwork, or a reference product’s character or screen composition.

The untouched generated master is preserved. Do not blur, add borders, add padding, crop, resize, or rewrite DPI metadata on the master. A derivative may exist only for an explicit component requirement, with the master and transformation recorded.

No asset may introduce:

- a child identity, face, voice, name, or profile cue;
- a copied character, costume, silhouette, logo, scene, category illustration, status symbol, or distinctive composition;
- an unrequested person, animal, prop, hand, limb, or duplicate subject;
- essential text that is not also present as reviewed UI text;
- a safety instruction that is absent from the accessible text path;
- a network, account, purchase, ad, social, or telemetry dependency.

## 2. Required asset record

Keep one record for each approved master and derivative, using the fields below. Store it as JSON, Markdown, or in the project asset manifest.

| Field | Requirement |
|---|---|
| Asset ID | Stable ID, surface, state, and revision. |
| Intended use | Screen, component, activity, and state. |
| Prompt | Exact generation prompt, including count, placement, style, exclusions, and text restrictions. |
| Generation path | Google Gemini Nano Banana, model label when available, and generation date. |
| Master path | Absolute or repository-relative path to the untouched master. |
| Master SHA-256 | Hash generated from the actual file. |
| Derivatives | Each derivative path, source master, transformation, dimensions, and hash. |
| Format | File format, dimensions, color mode, and transparency result. |
| Alt text | Concise description for the image role. State whether the asset is decorative. |
| Compose semantics | Content description or empty description decision, plus any state announcement. |
| QA result | Subject, count, silhouette, edges, text, state, contrast, responsive, and originality checks. |
| Reviewer | Person or agent, date, decision, and known limitations. |

Do not record a generated asset as approved until the actual file has been inspected and hashed.

## 3. Prompt and generation controls

Every Nano Banana prompt must state:

- the intended KidPlay surface and activity state;
- exact subject count;
- original forms, palette, and silhouette constraints;
- placement and negative space required by the component;
- whether the asset is decorative, instructional, or state-bearing;
- text exclusion unless exact reviewed text is required;
- banned defects: duplicate subjects, malformed hands, detached limbs, unreadable text, copied characters, logos, watermarks, borders, padding, blur, and floating objects;
- preservation of the requested aspect and master dimensions;
- accessibility requirement that the UI carries the same meaning in text.

Do not prompt for a reference product’s exact composition, character, category set, or branded treatment. Reference captures may inform a general user problem or interaction principle only.

## 4. Image QA checklist

### Identity, count, and composition

- [ ] The subject count is exact.
- [ ] The subject, age cues, clothing cues, and intended expression are correct for the approved asset.
- [ ] No extra person, face, limb, hand, animal, prop, logo, or watermark appears.
- [ ] The asset leaves the required space for UI controls and captions.
- [ ] The visual does not cover the task target, Back, safety, completion, or primary action.
- [ ] The composition is original and does not reproduce a reference screen or illustration.

### Edges and file integrity

- [ ] Edges are clean at the intended density.
- [ ] Transparent assets contain both transparent and opaque pixels when transparency is required.
- [ ] The master has the requested dimensions and color mode.
- [ ] The master has not been blurred, bordered, padded, cropped, resized, or DPI-rewritten.
- [ ] Any derivative transformation is explicit, reversible, and hashed.
- [ ] The SHA-256 was generated from the file delivered to the repository or release bundle.

### Text and accessibility

- [ ] No generated text is used unless the exact text was requested and proofread.
- [ ] Essential meaning is present in UI text and Compose semantics.
- [ ] Alt text identifies the asset’s useful role without claiming a state the image does not show.
- [ ] Decorative images have an empty semantic description and do not duplicate adjacent text.
- [ ] Instructional images have a concise written rule immediately available.
- [ ] The asset remains understandable when sound is unavailable.

### State and layout

- [ ] The asset matches the prompt, selection, correct, incorrect, retry, completion, or next-step state it represents.
- [ ] Color and expression do not carry the only state meaning.
- [ ] The asset remains usable at 320 dp, typical phone width, and expanded layout.
- [ ] Large text does not cover or detach the asset from its related instruction.
- [ ] The fox does not interrupt successful play or obscure the active task.
- [ ] The current owner boundary on reduced motion is respected. No asset review claims reduced-motion support under this card.

## 5. Fox-specific QA

The fox is approved only for an explicit role:

| Role | QA requirement |
|---|---|
| Entry | The expression and placement point to the activity and Start action without covering either. |
| Correction | The expression is neutral or encouraging; the asset does not shame or imply a score. |
| Completion | The expression is brief and secondary to the completion text and next action. |
| Neutral | No idle loop or decorative occupancy is required. |

Fox assets must use the approved expression vocabulary: `neutral`, `welcoming`, `thinking`, `encouraging`, and `celebrating`. An expression must not add meaning that is absent from text. If the same message is visible beside the image, the image is decorative for semantics.

## 6. Asset QA evidence

For each reviewed asset, retain:

1. the untouched master;
2. the prompt and generation record;
3. the hash manifest;
4. the derivative record, if any;
5. the alt-text and semantics decision;
6. the candidate review note;
7. the final approval or rejection decision;
8. the location of the asset in the app or Dev Lab fixture.

Useful review notes are specific. Record the exact defect, state, surface, and action. Do not write “looks good” without checking count, edges, text, state, and accessibility.

## 7. Audio-linked and motion-linked assets

A visual cue linked to audio or motion must remain useful without that medium.

- Spoken guidance has visible text or a caption equivalent.
- A sound effect does not serve as the only correctness or completion signal.
- An energetic visual may be paired with automatically enabled sound under the owner decision, but the task remains readable without it.
- Sound is bundled and offline; the asset record must not imply remote media.
- No new per-activity mute control is added.
- Reduced-motion behavior is owner-deferred. Do not create a separate reduced-motion asset set under this card.

## 8. Release and rollback

Before an asset reaches a release build:

- verify the master and bundled file hashes;
- verify the asset path and fallback behavior on a clean local build;
- verify the intended state and text semantics in the Dev Lab fixture;
- preserve the prior approved asset for rollback;
- record the revision in the design or release evidence;
- do not delete a user-supplied or protected asset automatically.

If an asset fails QA, mark it rejected and keep it out of the app bundle. Retain it for audit only when policy permits. Never replace a master without recording the change.
