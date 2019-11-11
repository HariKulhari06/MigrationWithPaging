package com.example.migrationwithpaging.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.migrationwithpaging.data.Movie
import com.example.migrationwithpaging.data.MovieDao
import com.example.migrationwithpaging.data.network.ApiService
import com.example.migrationwithpaging.repo.dbAndNetwork.MovieBoundaryCallBack
import com.example.migrationwithpaging.repo.withNetwork.byItem.MoviePagedKeyDataSourceFactory
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
        val dataSourceFactory = MoviePagedKeyDataSourceFactory(apiService)

        val pagedList = dataSourceFactory.toLiveData(5)

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
        val boundaryCallback = MovieBoundaryCallBack(
            movieDao = movieDao,
            webservice = apiService,
            handleResponse = this::insertIntoDatabase
        )

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh()
        }

        val pagedList = movieDao.getMoviesDataSource().toLiveData(
            config,
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
        return MutableLiveData()
    }


    private fun insertIntoDatabase(movies: List<Movie>) {
        GlobalScope.launch(Dispatchers.IO) {
            movieDao.insert(movies)
        }
    }

    fun addItem() {
        GlobalScope.launch(Dispatchers.IO) {
            movieDao.insert(
                Movie(
                    1212,
                    21231.0,
                    "New Item Added",
                    "cv",
                    "Item added for testing."
                )
            )
        }

    }

    fun removeItem(movie: Movie?) {
        GlobalScope.launch(Dispatchers.IO) {
            if (movie != null) {
                movieDao.delete(movie)
            }
        }
    }

    fun updateItem(movie: Movie?) {
        GlobalScope.launch(Dispatchers.IO) {
            if (movie != null) {
                movie.id = 1321
                movie.title = "Updated Title"
                movie.popularity = 12456.2
                movieDao.update(movie)
            }
        }
    }

    companion object {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .build()
    }

}