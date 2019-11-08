package com.example.migrationwithpaging.repo.dbAndNetwork

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.migrationwithpaging.data.Movie
import com.example.migrationwithpaging.data.MovieDao
import com.example.migrationwithpaging.data.network.*
import com.example.migrationwithpaging.repo.NetworkState
import com.example.migrationwithpaging.repo.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KFunction1

class MovieBoundaryCallBack(
    private val movieDao: MovieDao,
    private val webservice: ApiService,
    private val handleResponse: KFunction1<@ParameterName(name = "movies") List<Movie>, Unit>
) : PagedList.BoundaryCallback<Movie>() {

    val networkState = MutableLiveData<NetworkState>()

    override fun onZeroItemsLoaded() {
        callMovieWebService(getPageNumber())
    }

    private fun getPageNumber() = runBlocking(Dispatchers.IO) {
        val movieCount = movieDao.getCount()
        movieCount / 60 + 1
    }


    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        callMovieWebService(getPageNumber())
    }

    override fun onItemAtFrontLoaded(itemAtFront: Movie) {
        //EMPTY
    }

    private fun callMovieWebService(i: Int) {
        if (networkState.value?.status == Status.RUNNING)
            return
        networkState.postValue(NetworkState.LOADING)
        ApiRepository.callApi(webservice.discoverMovieAsync(i),
            object : ApiCallback<DiscoverMoviesResponse> {
                override fun onException(error: Throwable) {
                    networkState.postValue(NetworkState.error(error.message))
                }

                override fun onError(errorModel: ApiError) {
                    networkState.postValue(NetworkState.error(errorModel.statusMessage))
                }

                override fun onSuccess(t: DiscoverMoviesResponse?) {
                    networkState.postValue(NetworkState.LOADED)
                    t?.movies?.let { handleResponse.invoke(it) }
                }
            })

    }
}