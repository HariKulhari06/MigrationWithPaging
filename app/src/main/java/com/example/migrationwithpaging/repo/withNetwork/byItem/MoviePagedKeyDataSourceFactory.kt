package com.example.migrationwithpaging.repo.withNetwork.byItem

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.migrationwithpaging.data.Movie
import com.example.migrationwithpaging.data.network.ApiService

class MoviePagedKeyDataSourceFactory(
    private val apiService: ApiService
) : DataSource.Factory<Int, Movie>() {
    val sourceData = MutableLiveData<MoviePagedKeyDataSource>()
    override fun create(): DataSource<Int, Movie> {
        val dataSource =
            MoviePagedKeyDataSource(
                apiService
            )
        sourceData.postValue(dataSource)
        return dataSource
    }
}