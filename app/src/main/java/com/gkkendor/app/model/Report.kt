package com.gkkendor.app.model

import java.io.Serializable

data class Report(
    val id: Int,
    val title: String? = null,
    val content: String? = null,
    val publishedAt: String? = null,
    val status: String
): Serializable
