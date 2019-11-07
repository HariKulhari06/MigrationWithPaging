package com.example.migrationwithpaging.ui.withDB

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.migrationwithpaging.data.AppDataBase
import com.example.migrationwithpaging.data.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WithDBViewModel(application: Application) : ViewModel() {
    private val db = AppDataBase.getDatabase(application.applicationContext)
    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(50)
        .setInitialLoadSizeHint(100)
        .setPrefetchDistance(10)
        .build()

    val movies =
        db?.movieDao()?.getMoviesDataSource()
            ?.let { LivePagedListBuilder<Int, Movie>(it, config).build() }


    fun insertDemoData() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = ArrayList<Movie>()
            for (i in 1..200000) {
                list.add(
                    Movie(
                        i.toString(),
                        "Movie Title $i",
                        "https://images-na.ssl-images-amazon.com/images/I/71nsvxFpSTL._SY606_.jpg"
                    )
                )
            }
            db?.movieDao()?.insert(list)
        }
    }
}