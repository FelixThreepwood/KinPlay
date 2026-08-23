package com.kinplay.app

data class ReleaseChange(
    val itemId: String,
    val summary: String,
)

data class ReleaseVersion(
    val version: String,
    val changes: List<ReleaseChange>,
)

val KIDPLAY_RELEASE_CHANGELOG = listOf(
    ReleaseVersion(
        version = "0.7.2",
        changes = listOf(
            ReleaseChange("KPF-0022", "Kept concise descriptions visible on Level One cards"),
            ReleaseChange("KPF-0063", "Preserved favorite controls on simplified game cards"),
            ReleaseChange("KPF-0089", "Removed card expansion and enabled title navigation"),
        ),
    ),
    ReleaseVersion(
        version = "0.7.1",
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
