package com.example.migrationwithpaging.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movie: Movie)

    @Delete
    fun delete(movie: Movie)

    @Delete
    fun deleteAll(movies: List<Movie>)

    @Query("SELECT * FROM movie ORDER BY id ASC")
    fun getMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie ORDER BY id ASC")
    fun getMoviesDataSource(): DataSource.Factory<Int, Movie>

    @Query("SELECT COUNT(*) FROM movie")
    fun getCount(): Int
}