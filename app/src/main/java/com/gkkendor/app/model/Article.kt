package com.gkkendor.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Article(
    val id: Int,
    val author: String? = null,
    val content: String? = null,
    val publishedAt: String,
    val title: String? = null,
    val imageUrl: String? = null,
) : Serializable, Parcelable
