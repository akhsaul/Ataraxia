package com.gkkendor.app.model

data class ArticleResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)