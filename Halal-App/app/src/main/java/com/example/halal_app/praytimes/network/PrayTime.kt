package com.example.halal_app.praytimes.network

import com.squareup.moshi.Json

data class PrayTime (
    @field:Json(name = "date")var date: String,
    @field:Json(name = "subuh")var subuh: String,
    @field:Json(name = "terbit")var terbit: String,
    @field:Json(name = "dzuhur")var dzuhur: String,
    @field:Json(name = "ashr")var ashr: String,
    @field:Json(name = "maghrib")var maghrib: String,
    @field:Json(name = "isya")var isya: String
)