package com.gkkendor.app.model

import java.io.Serializable

data class Article(
    var id: Int,
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String? = null,
    val title: String? = null,
    val url: String? = null,
    val urlToImage: String? = null
) : Serializable
