package com.kinplay.app

data class ContentVisualAsset(
    val id: String,
    val resource: String,
    val altText: String,
)

data class PaperAirplaneModel(
    val id: String,
    val name: String,
    val shapeDescription: String,
    val diagramAsset: String,
    val steps: List<String>,
)
