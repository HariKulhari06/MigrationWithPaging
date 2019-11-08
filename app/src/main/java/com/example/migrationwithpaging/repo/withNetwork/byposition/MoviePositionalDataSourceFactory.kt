package com.example.migrationwithpaging.repo.withNetwork.byposition

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.migrationwithpaging.data.Movie
import com.example.migrationwithpaging.data.network.ApiService

class MoviePositionalDataSourceFactory(
    private val apiService: ApiService
) : DataSource.Factory<Int, Movie>() {
    val sourceData = MutableLiveData<MoviePositionalDataSource>()
    override fun create(): DataSource<Int, Movie> {
        val dataSource =
            MoviePositionalDataSource(
                apiService
            )
        sourceData.postValue(dataSource)
        return dataSource
    }
}