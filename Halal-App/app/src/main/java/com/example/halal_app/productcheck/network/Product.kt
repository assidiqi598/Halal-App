package com.example.halal_app.productcheck.network

import com.squareup.moshi.Json

data class Product (
    @field:Json(name = "title")var title: String,
    @field:Json(name = "imageUrl")var imageUrl: String,
    @field:Json(name = "ingredient")var ingredient: String,
    @field:Json(name = "status")var status: String
)