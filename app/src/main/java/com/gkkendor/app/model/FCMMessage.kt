package com.gkkendor.app.model

data class FCMMessage(
    val priority: String = "high",
    val to: String = "/topics/ataraxia_help",
    val notification: FCMNotification? = null,
    val data: Map<String, String>
)
