package com.example.migrationwithpaging

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.migrationwithpaging.ui.MovieActivity
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    fun launchForListAdapter(view: View) {
        startActivity(
            MovieActivity.getLaunchIntent(
                this,
                MovieActivity.Companion.PagingType.LIST_ADAPTER
            )
        )
    }

    fun pagingWithDB(view: View) {
        startActivity(
            MovieActivity.getLaunchIntent(
                this,
                MovieActivity.Companion.PagingType.WITH_DB
            )
        )

    }

    fun pagingWithNetwork(view: View) {
        startActivity(
            MovieActivity.getLaunchIntent(
                this,
                MovieActivity.Companion.PagingType.WITH_NETWORK
            )
        )
    }

    fun pagingWithDataBase(view: View) {
        startActivity(
            MovieActivity.getLaunchIntent(
                this,
                MovieActivity.Companion.PagingType.DA_AND_NETWORK
            )
        )

    }

}
