package com.example.migrationwithpaging.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.migrationwithpaging.MovieApp
import com.example.migrationwithpaging.R
import com.example.migrationwithpaging.data.network.ApiClient
import com.example.migrationwithpaging.repo.MovieRepository
import kotlinx.android.synthetic.main.activity_with_db.*
import kotlinx.android.synthetic.main.content_with_db.*

class MovieActivity : AppCompatActivity() {
    lateinit var viewModel: MovieViewModel

    lateinit var pagingType: PagingType

    private val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val appDb = (application as MovieApp).appDataBase
            return MovieViewModel(
                MovieRepository(
                    appDb.movieDao(),
                    ApiClient.getApiService()
                )
            ) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_db)
        setSupportActionBar(toolbar)

        pagingType = intent.extras?.getSerializable(TYPE) as PagingType

        viewModel = ViewModelProviders.of(this, factory).get(MovieViewModel::class.java)

        val adapter = MovieAdapter()
        rc.adapter = adapter

        when (pagingType) {
            PagingType.LIST_ADAPTER -> {
                viewModel.moviesLiveData.observe(this, Observer {
                    Log.e("moviesLiveData", "" + it.size)
                    //adapter.submitList(it)
                })
            }
            PagingType.WITH_DB -> viewModel.moviesLiveListFromDataBase.observe(this, Observer {
                Log.e("FromDataBase", "" + it.size)
                adapter.submitList(it)
            })
            PagingType.WITH_NETWORK -> viewModel.moviesLiveListFromNetwork.observe(this, Observer {
                Log.e("FromNetwork", "" + it.size)
                adapter.submitList(it)
            })
            else -> {
                viewModel.moviesLiveListFromDataBaseAndNetwork.observe(this, Observer {
                    Log.e("DataBaseAndNetwork", "" + it.size)
                    adapter.submitList(it)
                })
            }
        }

        viewModel.networkState.observe(this, Observer {
            Toast.makeText(this, it.status.name + "  " + it.msg, Toast.LENGTH_LONG).show()
        })
    }


    companion object {
        private const val TYPE = "type"

        enum class PagingType {
            LIST_ADAPTER,
            WITH_DB,
            WITH_NETWORK,
            DA_AND_NETWORK
        }

        fun getLaunchIntent(context: Context, pagingType: PagingType): Intent {
            val intent = Intent(context, MovieActivity::class.java)
            intent.putExtra(TYPE, pagingType)
            return intent
        }
    }

}
