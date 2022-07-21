package com.gkkendor.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Report(
    val id: Int,
    val title: String? = null,
    val content: String? = null,
    val publishedAt: String? = null,
    val status: String
): Serializable, Parcelable
