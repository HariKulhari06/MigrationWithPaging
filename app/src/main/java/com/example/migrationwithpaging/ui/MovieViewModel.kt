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

package com.example.migrationwithpaging.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.migrationwithpaging.data.Movie
import com.example.migrationwithpaging.repo.MovieRepository
import com.example.migrationwithpaging.repo.NetworkState

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    val moviesLiveData: LiveData<List<Movie>> = movieRepository.getMoviesLiveDataFromDataBase()

    val moviesLiveListFromDataBase: LiveData<PagedList<Movie>> =
        movieRepository.getMoviesDataSourceFromDataBase()

    private val listingByNetwork =
        movieRepository.getListingFromNetwork()

    val moviesLiveListFromNetwork = listingByNetwork.pagedList

    val networkState: LiveData<NetworkState> = listingByNetwork.networkState

    private val listingByDataBase = movieRepository.getListingFromDataBase()

    val moviesLiveListFromDataBaseAndNetwork = listingByDataBase.pagedList

    val refreshState: LiveData<NetworkState> = listingByDataBase.refreshState


    fun addItem() {
        movieRepository.addItem()
    }

    fun removeItem(movie: Movie?) {
        movieRepository.removeItem(movie)
    }

    fun updateItem(movie: Movie?) {
        movieRepository.updateItem(movie)
    }

    fun refresh() {
        listingByDataBase.refresh.invoke()
    }

}