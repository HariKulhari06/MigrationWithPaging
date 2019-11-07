package com.example.migrationwithpaging.repo.dbAndNetwork

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.migrationwithpaging.data.Movie
import com.example.migrationwithpaging.data.MovieDao
import com.example.migrationwithpaging.data.network.*
import com.example.migrationwithpaging.repo.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KFunction1

class MovieBoundryCallBack(
    private val movieDao: MovieDao,
    private val webservice: ApiService,
    private val handleResponse: KFunction1<@ParameterName(name = "movies") List<Movie>, Unit>
) : PagedList.BoundaryCallback<Movie>() {

    var isLoading = false
    val networkState = MutableLiveData<NetworkState>()

    override fun onZeroItemsLoaded() {
        callMovieWebService(getPageNumber())
    }

    private fun getPageNumber() = runBlocking(Dispatchers.IO) {
        val movieCount = movieDao.getCount()
        movieCount / 20 + 1
    }


    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        callMovieWebService(getPageNumber())
    }

    override fun onItemAtFrontLoaded(itemAtFront: Movie) {
        //EMPTY
    }

    private fun callMovieWebService(i: Int) {
        if (isLoading) return
        isLoading = true
        networkState.postValue(NetworkState.LOADING)
        ApiRepository.callApi(webservice.discoverMovieAsync(i),
            object : ApiCallback<DiscoverMoviesResponse> {
                override fun onException(error: Throwable) {
                    networkState.postValue(NetworkState.error(error.message))
                    isLoading = false
                }

                override fun onError(errorModel: ApiError) {
                    networkState.postValue(NetworkState.error(errorModel.statusMessage))
                    isLoading = false
                }

                override fun onSuccess(t: DiscoverMoviesResponse?) {
                    networkState.postValue(NetworkState.LOADED)
                    isLoading = false
                    t?.movies?.let { handleResponse.invoke(it) }
                }
            })

    }
}