package com.example.migrationwithpaging.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Movie(

    @PrimaryKey var id: Int,
    @SerializedName("popularity")
    var popularity: Double,
    @SerializedName("original_title")
    var title: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("overview")
    val overview: String
)