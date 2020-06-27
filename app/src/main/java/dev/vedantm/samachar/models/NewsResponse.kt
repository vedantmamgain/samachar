package dev.vedantm.samachar.models

import dev.vedantm.samachar.models.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)