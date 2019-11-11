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

    val refreshState: LiveData<NetworkState> = listingByNetwork.refreshState

    val retry: () -> Unit = listingByNetwork.retry


    private val listingByDataBase = movieRepository.getListingFromDataBase()

    val moviesLiveListFromDataBaseAndNetwork = listingByDataBase.pagedList

    fun insertTestMovies() {
        movieRepository.insertDemoData()
    }

    fun addItem() {
        movieRepository.addItem()
    }

    fun removeItem(movie: Movie?) {
        movieRepository.removeItem(movie)
    }

    fun updateItem(movie: Movie?) {
        movieRepository.updateItem(movie)
    }

}