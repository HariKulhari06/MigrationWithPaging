package com.example.migrationwithpaging.repo.withNetwork.byposition

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.example.migrationwithpaging.data.Movie
import com.example.migrationwithpaging.data.network.*
import com.example.migrationwithpaging.repo.NetworkState

class MoviePositionalDataSource(private val apiService: ApiService) :
    PositionalDataSource<Movie>() {

    val networkState = MutableLiveData<NetworkState>()

    private var retry: (() -> Any)? = null

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            it.invoke()
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Movie>) {
        networkState.postValue(NetworkState.LOADING)
        ApiRepository.callApi(
            apiService.discoverMovieAsync(1),
            object : ApiCallback<DiscoverMoviesResponse> {
                override fun onException(error: Throwable) {
                    networkState.postValue(
                        NetworkState.error(
                            error.message
                        )
                    )
                }

                override fun onError(errorModel: ApiError) {
                    networkState.postValue(
                        NetworkState.error(
                            errorModel.statusMessage
                        )
                    )
                    retry = {
                        loadInitial(params, callback)
                    }
                }

                override fun onSuccess(t: DiscoverMoviesResponse?) {
                    if (t != null) {
                        callback.onResult(t.movies, 0, t.totalResults)
                        networkState.postValue(NetworkState.LOADED)
                        retry = null
                    }
                }
            })
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Movie>) {
        networkState.postValue(NetworkState.LOADING)
        ApiRepository.callApi(
            apiService.discoverMovieAsync(params.startPosition.div(params.loadSize).plus(1)),
            object : ApiCallback<DiscoverMoviesResponse> {
                override fun onException(error: Throwable) {
                    networkState.postValue(
                        NetworkState.error(
                            error.message
                        )
                    )
                    retry = {
                        loadRange(params, callback)
                    }
                }

                override fun onError(errorModel: ApiError) {
                    networkState.postValue(
                        NetworkState.error(
                            errorModel.statusMessage
                        )
                    )
                    retry = {
                        loadRange(params, callback)
                    }
                }

                override fun onSuccess(t: DiscoverMoviesResponse?) {
                    if (t != null) {
                        callback.onResult(t.movies)
                        networkState.postValue(NetworkState.LOADED)
                        retry = null
                    }
                }
            })
    }
}