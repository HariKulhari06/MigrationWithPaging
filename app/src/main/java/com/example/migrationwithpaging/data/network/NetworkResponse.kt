package com.example.migrationwithpaging.data.network

import com.example.migrationwithpaging.data.Movie
import com.google.gson.annotations.SerializedName


data class DiscoverMoviesResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movies: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

