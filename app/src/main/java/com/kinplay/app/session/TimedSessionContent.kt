package com.kinplay.app.session

import com.kinplay.app.DetailSection
import com.kinplay.app.KinPlayItem

/** The active session deliberately exposes only the reviewed instructions needed to play. */
fun KinPlayItem.activeSessionSections(): List<DetailSection> =
    listOf(DetailSection("Steps", playSteps)).filter { it.lines.isNotEmpty() }
