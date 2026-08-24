package com.kinplay.app

data class ReleaseChange(
    val itemId: String,
    val summary: String,
)

data class ReleaseVersion(
    val version: String,
    val releaseDate: String,
    val changes: List<ReleaseChange>,
)

val KIDPLAY_RELEASE_CHANGELOG = listOf(
    ReleaseVersion(
        version = "0.7.3",
        releaseDate = "2026-08-23",
        changes = listOf(
            ReleaseChange("KPF-0032", "Reduced repetitive safety copy while retaining essential warnings"),
            ReleaseChange("KPF-0093", "Removed three-second handoff lock from game surfaces"),
            ReleaseChange("KPF-0094", "Kept feedback available throughout every app screen"),
            ReleaseChange("KPF-0095", "Added dated version history to About the app"),
            ReleaseChange("KPF-0096", "Standardized concise five-to-ten-word change summaries"),
            ReleaseChange("KPF-0097", "Styled All games heading with accessible small caps"),
            ReleaseChange("KPF-0098", "Removed taxonomy explanations from Level 1 browsing"),
            ReleaseChange("KPF-0099", "Made complete Level 1 cards open details"),
            ReleaseChange("KPF-0100", "Split Would You Rather prompts across two lines"),
            ReleaseChange("KPF-0101", "Introduced concise step-based detail section template"),
            ReleaseChange("KPF-0102", "Rewrote I Spy with concise guided sections"),
            ReleaseChange("KPF-0103", "Retired Shape Detective from active content discovery"),
            ReleaseChange("KPF-0104", "Redesigned Backyard Safari around three-minute observation"),
            ReleaseChange("KPF-0105", "Added vertical per-game duration picker for Safari"),
        ),
    ),
    ReleaseVersion(
        version = "0.7.2",
        releaseDate = "2026-08-22",
        changes = listOf(
            ReleaseChange("KPF-0022", "Kept concise descriptions visible on Level One cards"),
            ReleaseChange("KPF-0063", "Preserved favorite controls on simplified game cards"),
            ReleaseChange("KPF-0089", "Removed card expansion and enabled title navigation"),
        ),
    ),
    ReleaseVersion(
        version = "0.7.1",
        releaseDate = "2026-08-15",
        changes = listOf(
            ReleaseChange("KPF-0022", "Clarified every game card with concise descriptions"),
            ReleaseChange("KPF-0043", "Fixed single and multiple feedback attachment sharing"),
            ReleaseChange("KPF-0060", "Added bright Lavender theme to color choices"),
            ReleaseChange("KPF-0078", "Protected every owner-supplied prompt from removal"),
            ReleaseChange("KPF-0079", "Removed Washable Coloring Together from active content"),
            ReleaseChange("KPF-0080", "Grouped similar activities under familiar game formats"),
            ReleaseChange("KPF-0081", "Removed launcher icon selection from Settings"),
            ReleaseChange("KPF-0082", "Arranged six themes in three-column grid"),
            ReleaseChange("KPF-0083", "Added reviewed picture metadata to every Charades card"),
            ReleaseChange("KPF-0084", "Created original Charades artwork from approved direction"),
            ReleaseChange("KPF-0085", "Removed generic Prepare Play Share card graphic"),
            ReleaseChange("KPF-0086", "Rewrote prompts for shorter natural read-aloud delivery"),
            ReleaseChange("KPF-0087", "Reduced each prompt category to forty entries"),
            ReleaseChange("KPF-0088", "Tightened gross-humor rules for child-appropriate content"),
        ),
    ),
    ReleaseVersion(
        version = "0.6.3",
        releaseDate = "2026-08-06",
        changes = listOf(
            ReleaseChange("KPF-0004", "Added Race Like an Animal activity"),
            ReleaseChange("KPF-0010", "Reduced setup decisions for easier family play"),
            ReleaseChange("KPF-0017", "Added persistent timer duration and theme settings"),
            ReleaseChange("KPF-0055", "Removed nonessential metadata from instruction pages"),
            ReleaseChange("KPF-0058", "Moved primary browsing actions above recent games"),
            ReleaseChange("KPF-0064", "Added flickable animal selector with accessible results"),
            ReleaseChange("KPF-0065", "Simplified theme choices to clear names only"),
            ReleaseChange("KPF-0066", "Stacked theme choices in a vertical list"),
            ReleaseChange("KPF-0067", "Added another-pick action to random game flow"),
            ReleaseChange("KPF-0068", "Ordered all games by familiar family formats"),
            ReleaseChange("KPF-0069", "Added six activity categories to Home"),
            ReleaseChange("KPF-0070", "Arranged Home categories in responsive two-column grid"),
            ReleaseChange("KPF-0071", "Added expandable game categories drawer on Home"),
            ReleaseChange("KPF-0072", "Added animated activity themes drawer on Home"),
            ReleaseChange("KPF-0073", "Placed primary Home browsing actions side by side"),
            ReleaseChange("KPF-0074", "Changed game cards to full-width single column"),
            ReleaseChange("KPF-0075", "Added visual instruction cues throughout play flows"),
            ReleaseChange("KPF-0076", "Added original Tiny Monster visual guide"),
            ReleaseChange("KPF-0077", "Hid timers when timing is not essential"),
        ),
    ),
)
