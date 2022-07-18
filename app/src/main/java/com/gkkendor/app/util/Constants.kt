package com.gkkendor.app.util

object Constants {
    const val API_KEY = "6953a7421bb749c589e687746c85eee8"
    const val BASE_URL = "https://newsapi.org"
    const val SEARCH_NEWS_TIME_DELAY = 500L
    const val QUERY_PAGE_SIZE = 20
    val acceptedMimeTypes = arrayOf(
        "audio/*",
        "image/*",
        "video/*",
        "text/plain",
        "application/x-tar",
        "application/pdf",
        "application/zip",
        "application/gzip",
        "application/x-7z-compressed",
        "application/vnd.rar",
        "application/x-bzip",
        "application/x-bzip2"
    )

    private val ascii = buildList {
        addAll(IntRange(65, 90))
        add(32)
        addAll(IntRange(97, 122))
    }

    internal val statuses = listOf(
        "Success", "Pending", "Failed"
    )

    fun randomString(charSize: Int): String {
        return buildString(charSize) {
            repeat(charSize) {
                append(ascii.random().toChar())
            }
        }
    }
}