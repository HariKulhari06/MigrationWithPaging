package com.example.migrationwithpaging.data.network


import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("3/discover/movie?language=en&sort_by=popularity.desc")
    fun discoverMovieAsync(@Query("page") page: Int): Deferred<Response<DiscoverMoviesResponse>>


}