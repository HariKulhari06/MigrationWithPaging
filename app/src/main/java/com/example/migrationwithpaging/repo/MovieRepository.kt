package com.example.migrationwithpaging.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.migrationwithpaging.data.Movie
import com.example.migrationwithpaging.data.MovieDao
import com.example.migrationwithpaging.data.network.ApiService
import com.example.migrationwithpaging.repo.dbAndNetwork.MovieBoundryCallBack
import com.example.migrationwithpaging.repo.withNetwork.MovieDataSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MovieRepository(private val movieDao: MovieDao, private val apiService: ApiService) {

    fun getMoviesLiveDataFromDataBase() = movieDao.getMovies()

    fun getMoviesDataSourceFromDataBase(): LiveData<PagedList<Movie>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(50)
            .setInitialLoadSizeHint(100)
            .setPrefetchDistance(10)
            .build()

        return movieDao.getMoviesDataSource().toLiveData(pageSize = 20)

    }

    fun getListingFromNetwork(): Listing<Movie> {
        val dataSourceFactory =
            MovieDataSourceFactory(apiService)

        val pagedList = dataSourceFactory.toLiveData(20)

        val refreshState = Transformations.switchMap(dataSourceFactory.sourceData) {
            it.initialLoad
        }

        return Listing(
            pagedList = pagedList,
            networkState = Transformations.switchMap(dataSourceFactory.sourceData) {
                it.networkState
            },
            retry = {
                dataSourceFactory.sourceData.value?.retryAllFailed()
            },
            refresh = {
                dataSourceFactory.sourceData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }


    fun getListingFromDataBase(): Listing<Movie> {
        val boundaryCallback = MovieBoundryCallBack(
            movieDao = movieDao,
            webservice = apiService,
            handleResponse = this::insertIntoDatabase
        )

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh()
        }

        val pagedList = movieDao.getMoviesDataSource().toLiveData(
            20,
            boundaryCallback = boundaryCallback
        )

        return Listing(
            pagedList = pagedList,
            networkState = boundaryCallback.networkState,
            retry = {
                refresh()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState

        )
    }

    private fun refresh(): MutableLiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()

        return networkState
    }


    private fun insertIntoDatabase(movies: List<Movie>) {
        GlobalScope.launch(Dispatchers.IO) {
            movieDao.insert(movies)
        }
    }


    fun insertDemoData() {
        GlobalScope.launch(Dispatchers.IO) {
            val list = ArrayList<Movie>()
            for (i in 1..100000) {
                list.add(
                    Movie(
                        i,
                        i.toDouble(),
                        "Movie Title $i",
                        "/989zACwuc4EiBUNA3Ul7bgMoh0O.jpg"
                    )
                )
            }
            insertIntoDatabase(list)
        }
    }

}