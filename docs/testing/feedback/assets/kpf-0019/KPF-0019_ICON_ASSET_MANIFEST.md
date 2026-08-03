# KPF-0019 Gemini icon candidate manifest

Status: B16 approved for B17 Android integration: Candidate 1A (Teal) and Candidate 1C (Sunshine).

Operation: constrained original KinPlay launcher-icon revision.
Provider/model: Google Gemini Nano Banana path, `gemini-3-pro-image-preview`.
Generation date: 2026-08-03 (local verified date).

## Preserved candidates

| Label | Untouched master | Pixels | SHA-256 | Initial visual QA |
|---|---|---|---|---|
| Candidate 1A | `KPF-0019_Candidate-1A_Teal-Master.jpg` | 1024x1024 JPEG, RGB, 300x300 DPI metadata | `d2d83adde2909da14f24dbd89c75b24f75ae9d5a961ae4a93ee139a90fe375aa` | Stronger pass: K and P are immediately legible, rounded strokes retained, flat teal/yellow treatment, centered margins, no additional objects or text observed. |
| Candidate 1B | `KPF-0019_Candidate-1B_Sunshine-Master.jpg` | 1024x1024 JPEG, RGB, 300x300 DPI metadata | `51e17e29e55cb01e116ea28441d1570d94adf7e4f868d1ba2d06259038c05fa6` | K and P are legible, but overlap produces a less stable counter and weaker separation at launcher scale. Retained as an unapproved candidate. |
| Candidate 1C | `KPF-0019_Candidate-1C_Sunshine-Master.jpg` | 1024x1024 JPEG, RGB, 300x300 DPI metadata | `cc4f917b0cbf8324aad8961c17a0296fd14bdff228f21164e32a12f36415d91a` | Selected Sunshine master after bounded Nano Banana correction: K and P are immediately legible, the P bowl is clear, rounded ends are retained, safe margins are present, and no extra objects or text were observed. |

Prompt provenance is preserved beside each master in the matching `_prompt.txt` file. The exact returned Gemini files were copied without crop, resize, padding, blur, border, or other pixel transformation.

## B16 gate

Independent visual QA selected Candidate 1A and Candidate 1C for B17. Both pass K/P legibility, rounded-curve continuity, adaptive safe-zone margins, flat two-color treatment, launcher-scale readability, and no visible third-party imitation. Candidate 1B failed the separation/launcher-scale review and remains preserved as rejected. Exact file checks passed for both selected masters: 1024x1024 RGB JPEG, 300x300 DPI metadata, and the SHA-256 values recorded above. The approval is a project visual QA decision, not formal trademark clearance.
