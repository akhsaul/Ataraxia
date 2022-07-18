package com.gkkendor.app.model

data class ReportResponse(
    val reports: MutableList<Report>,
    val status: String,
    val totalResults: Int
)