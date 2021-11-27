package com.buildwithsiele.splashit.data.model

import com.google.gson.annotations.SerializedName

data class Image(
    // @field:Json(name = "id")
    @SerializedName("id")
    val id: String,
    // @field:Json(name = "width")
    @SerializedName("width")
    val width: Int,
    // @field:Json(name = "height")
    @SerializedName("height")
    val height: Int,
    // @field:Json(name = "urls")
    @SerializedName("urls")
    val urls: UnsplashURL
)

data class UnsplashURL(
    // @field:Json(name = "small")
    @SerializedName("small")
    val imageUrl: String
)
