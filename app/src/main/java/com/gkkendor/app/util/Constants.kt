package com.gkkendor.app.util

import com.google.gson.Gson

object Constants {
    const val API_KEY = "6953a7421bb749c589e687746c85eee8"
    const val BASE_URL = "https://newsapi.org"
    const val URL_FCM = "https://fcm.googleapis.com/fcm/send"
    const val SEARCH_NEWS_TIME_DELAY = 500L
    const val QUERY_PAGE_SIZE = 20
    const val FCM_LEGACY_API =
        "AAAA_SbyOz4:APA91bHsQb7tHsX0FwQSSGJ_TRfywZmoStG0NUstHya04mXJQ-08SotPBUjttSF6GXZ8bJYLMqRR-8_zIclnVJXrTA6elQ3lzznlfgpMqw7KhbU7jBiR9Ky5yKvCQSE2NK6_BZjFCwJ1"
    const val PATTERN_DATE = "dd MMM yyyy, hh:mm a"
    const val PATTERN_PICK_DATE = "dd/MM/yyyy"
    const val PATTERN_DATE_RESPONSE = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    var email = "Unknown"
    private val gson = Gson()
    private var token_fcm: String? = null
    val TOKEN_FCM: String
        get() {
            return requireNotNull(token_fcm) {
                "Token FCM doesn't set it!"
            }
        }

    fun setTokenFCM(token: String, log: (token: String) -> Unit) {
        token_fcm = token
        log(TOKEN_FCM)
    }

    fun toJson(src: Any): String {
        return gson.toJson(src)
    }

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