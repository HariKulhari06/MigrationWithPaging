package com.example.migrationwithpaging.ui.withDB

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.migrationwithpaging.R
import com.example.migrationwithpaging.data.Movie
import com.example.migrationwithpaging.ui.MovieAdapter
import kotlinx.android.synthetic.main.activity_with_db.*
import kotlinx.android.synthetic.main.content_with_db.*

class WithDBActivity : AppCompatActivity() {
    lateinit var viewModel: WithDBViewModel

    private val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            return WithDBViewModel(application) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_db)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this, factory).get(WithDBViewModel::class.java)

        val adapter = MovieAdapter()
        rc.adapter = adapter

        viewModel.movies?.observe(this, Observer {
            Log.e("Observer","${it.size}")
            adapter.submitList(it)
        })


        Handler().postDelayed({
            viewModel.insertDemoData()
        },2000)

    }


}
