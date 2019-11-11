/*
 * Copyright 2019 Hari Singh Kulhari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.migrationwithpaging.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.migrationwithpaging.data.Movie
import com.example.migrationwithpaging.data.MovieDao
import com.example.migrationwithpaging.data.network.*
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

        val pagedList = movieDao.getMoviesDataSource().toLiveData(
            config,
            boundaryCallback = boundaryCallback
        )

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh()
        }


        return Listing(
            pagedList = pagedList,
            networkState = boundaryCallback.networkState,
            retry = {
                retry()
            },
            refresh = {
                refreshTrigger.postValue(Unit)
            },
            refreshState = refreshState

        )
    }

    private fun retry() = Unit


    private fun refresh(): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.postValue(NetworkState.LOADING)
        ApiRepository.callApi(apiService.discoverMovieAsync(1),
            object : ApiCallback<DiscoverMoviesResponse> {
                override fun onException(error: Throwable) {
                    networkState.postValue(NetworkState.error(error.message))
                }

                override fun onError(errorModel: ApiError) {
                    networkState.postValue(NetworkState.error(errorModel.statusMessage))
                }

                override fun onSuccess(t: DiscoverMoviesResponse?) {
                    networkState.postValue(NetworkState.LOADED)
                    t?.movies?.let {
                        movieDao.nukeTable()
                        insertIntoDatabase(t.movies)
                    }
                }
            })

        return networkState
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