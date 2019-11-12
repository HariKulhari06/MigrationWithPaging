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

package com.example.migrationwithpaging

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.migrationwithpaging.ui.MovieActivity

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
