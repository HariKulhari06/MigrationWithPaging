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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.migrationwithpaging.MovieApp
import com.example.migrationwithpaging.R
import com.example.migrationwithpaging.data.network.ApiClient
import com.example.migrationwithpaging.repo.MovieRepository
import com.example.migrationwithpaging.repo.NetworkState
import kotlinx.android.synthetic.main.activity_with_db.*
import kotlinx.android.synthetic.main.content_with_db.*

class MovieActivity : AppCompatActivity() {
    private lateinit var viewModel: MovieViewModel
    private var adapter: MovieAdapter? = null
    private lateinit var pagingType: PagingType

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

        setUpAdapter()
        initSwipeRefreshLayout()
    }

    private fun setUpAdapter() {
        adapter = MovieAdapter()
        rc.adapter = adapter

        when (pagingType) {
            PagingType.LIST_ADAPTER -> {
                viewModel.moviesLiveData.observe(this, Observer {
                    //adapter.submitList(it)
                })
            }
            PagingType.WITH_DB -> viewModel.moviesLiveListFromDataBase.observe(this, Observer {
                adapter?.submitList(it)
            })
            PagingType.WITH_NETWORK -> viewModel.moviesLiveListFromNetwork.observe(this, Observer {
                adapter?.submitList(it)
            })
            else -> {
                viewModel.moviesLiveListFromDataBaseAndNetwork.observe(this, Observer {
                    adapter?.submitList(it)
                })
            }
        }

        viewModel.networkState.observe(this, Observer {
            Toast.makeText(this, it.status.name + "  " + it.msg, Toast.LENGTH_LONG).show()
        })
    }


    private fun initSwipeRefreshLayout() {
        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.isEnabled = pagingType == PagingType.DA_AND_NETWORK

        viewModel.refreshState.observe(this, Observer {
            swipeRefreshLayout.isRefreshing = it == NetworkState.LOADING
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return if (pagingType == PagingType.LIST_ADAPTER) {
            menuInflater.inflate(R.menu.options, menu)
            true
        } else {
            false
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_item -> {
                viewModel.addItem()
                true
            }
            R.id.action_remove_item -> {
                viewModel.removeItem(adapter?.currentList?.get(0))
                true
            }
            R.id.action_update_item -> {
                viewModel.updateItem(adapter?.currentList?.get(0))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
