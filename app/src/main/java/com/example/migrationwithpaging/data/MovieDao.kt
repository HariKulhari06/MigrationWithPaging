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

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(movie: Movie)

    @Query("SELECT * FROM movie ORDER BY popularity DESC")
    fun getMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie ORDER BY localId ASC")
    fun getMoviesDataSource(): DataSource.Factory<Int, Movie>

    @Query("SELECT COUNT(*) FROM movie")
    fun getCount(): Int
}