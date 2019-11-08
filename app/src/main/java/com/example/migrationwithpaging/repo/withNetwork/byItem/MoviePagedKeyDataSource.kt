package com.example.migrationwithpaging.repo.withNetwork.byItem

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.migrationwithpaging.data.Movie
import com.example.migrationwithpaging.data.network.*
import com.example.migrationwithpaging.repo.NetworkState

class MoviePagedKeyDataSource(private val apiService: ApiService) :
    PageKeyedDataSource<Int, Movie>() {
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

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
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
                        callback.onResult(t.movies, null, 2)
                        networkState.postValue(NetworkState.LOADED)
                        retry = null
                    }
                }
            })

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        ApiRepository.callApi(
            apiService.discoverMovieAsync(params.key),
            object : ApiCallback<DiscoverMoviesResponse> {
                override fun onException(error: Throwable) {
                    networkState.postValue(
                        NetworkState.error(
                            error.message
                        )
                    )
                    retry = {
                        loadAfter(params, callback)
                    }
                }

                override fun onError(errorModel: ApiError) {
                    networkState.postValue(
                        NetworkState.error(
                            errorModel.statusMessage
                        )
                    )
                    retry = {
                        loadAfter(params, callback)
                    }
                }

                override fun onSuccess(t: DiscoverMoviesResponse?) {
                    if (t != null) {
                        callback.onResult(t.movies, params.key.plus(1))
                        networkState.postValue(NetworkState.LOADED)
                        retry = null
                    }
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        //EMPTY
    }
}