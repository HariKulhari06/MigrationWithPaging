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

package com.example.migrationwithpaging.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Delete
    fun delete(movie: Movie)

    @Delete
    fun deleteAll(movies: List<Movie>)

    @Update
    fun update(movie: Movie)

    @Query("SELECT * FROM movie ORDER BY popularity DESC")
    fun getMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie ORDER BY popularity DESC")
    fun getMoviesDataSource(): DataSource.Factory<Int, Movie>

    @Query("SELECT COUNT(*) FROM movie")
    fun getCount(): Int

    @Query("DELETE FROM movie")
    fun nukeTable()
}