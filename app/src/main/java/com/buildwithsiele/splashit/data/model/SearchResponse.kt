package com.buildwithsiele.splashit.data.model


data class SearchResponse(
    val results: List<Photo>,
    val total: Int,
    val total_pages: Int
)