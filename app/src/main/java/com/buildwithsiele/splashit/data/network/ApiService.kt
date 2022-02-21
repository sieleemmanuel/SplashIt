package com.buildwithsiele.splashit.data.network

import com.buildwithsiele.splashit.BuildConfig
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.model.SearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


private const val BASE_URL = "https://api.unsplash.com/"

val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

val client = OkHttpClient.Builder()
    .addInterceptor(logger)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page:Int = 1,
        @Query("per_page") per_page:Int = 18,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_API_KEY
    ): List<Photo>

    @GET("search/photos")
    suspend fun search(
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_API_KEY,
        @Query("query") query: String,
        @Query("page") page:Int = 1,
        @Query("per_page") per_page:Int = 18,
    ):SearchResponse
}

object PhotosApi {
    val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}