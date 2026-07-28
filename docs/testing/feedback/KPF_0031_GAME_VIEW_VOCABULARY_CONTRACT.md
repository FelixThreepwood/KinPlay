# KPF-0031 — Game-view vocabulary contract

Status: complete; fixed awaiting independent retest
Baseline: B0 commit `48e93fed4541d0c4ae1902b21073fc1f837c62db`

This contract is normative for requirements, tests, feedback, and implementation notes. Use the three terms below; do not use “game tile,” “preview page,” “open card,” or “detail card” as synonyms.

| Canonical term | Kotlin/UI mapping | State and navigation boundary | Current visible content |
| --- | --- | --- | --- |
| **collapsed card** | `ContentCard(item, favoriteIds, navController)` in `MainActivity.kt` | `expanded == false`; this is the default established by `CONTENT_CARD_DEFAULT_EXPANDED == false`. Tapping the card toggles the remembered per-item state. It remains on the current list/home route. | Favorite marker and title, participant-fit label when present, and `collapsedCardPreviewLines()` (setup burden plus setup preview). |
| **expanded card** | The same `ContentCard` with `expanded == true`, which adds `CompactCardDetails(item, navController)` | In-place state of the same card, not a page or route. Tapping the card toggles it back to collapsed. `Open` calls `NavController.openItem(item)`. | Everything in the collapsed card plus summary, setup burden, optional Mad Libs field count, duration, age, energy, and `Open`. |
| **details page** | `ActivityDetailScreen(...)`, normally reached through `Routes.Detail == "detail/{itemId}"` and `Routes.detail(itemId)` | A distinct navigation destination. `itemDestination(item)` may intentionally route special collection/play items elsewhere; those destinations are not expanded cards or the generic details page. | Title and summary, play plan, age/duration/energy and participant fit, `detailSections()` (materials/setup/steps/prompt/follow-ups/read-aloud/variations), parent note, play controls, and safety tags. |

## Usage rules

1. Always include “collapsed,” “expanded,” or “details” when a requirement affects only one representation.
2. “Card” means either state of `ContentCard`; it never means `ActivityDetailScreen`.
3. “Page” means the distinct details navigation destination; expanding a card does not navigate.
4. A requirement that says “every representation” covers collapsed card, expanded card, and details page explicitly.
5. Safety changes must identify the representation and may not infer that moving text off one representation authorizes deletion. The KPF-0032 decision matrix and independent safety/spec review control such changes.

## Regression anchors

- Default state: `CONTENT_CARD_DEFAULT_EXPANDED`.
- Shared card state owner: `ContentCard` and its `rememberSaveable(item.id)` state.
- Expanded-only body: `CompactCardDetails`.
- Generic page route/composable: `Routes.Detail` / `ActivityDetailScreen`.
- Card-to-destination boundary: `NavController.openItem` and `itemDestination`.
