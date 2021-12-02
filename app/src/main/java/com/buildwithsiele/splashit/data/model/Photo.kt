package com.buildwithsiele.splashit.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Entity(tableName = "photos_table")
data class Photo(
    @SerializedName("id")
    @PrimaryKey val id: String,

    @SerializedName("width")
    @ColumnInfo(name = "width") val width: Int,

    @SerializedName("height")
    @ColumnInfo(name = "height") val height: Int,

    @SerializedName("urls")
    @ColumnInfo(name = "url") val urls: UnsplashURL

)

data class UnsplashURL(
    @SerializedName("small")
    val imageUrl: String
)

class Converter {
    @TypeConverter
    fun toJson(value: UnsplashURL?): String = Gson().toJson(value)

    @TypeConverter
    fun fromJson(value: String): UnsplashURL =
        (Gson().fromJson(value, UnsplashURL::class.java) as UnsplashURL)
}
