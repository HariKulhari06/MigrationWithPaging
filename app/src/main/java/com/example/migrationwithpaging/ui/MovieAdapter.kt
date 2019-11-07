package com.example.migrationwithpaging.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.migrationwithpaging.R
import com.example.migrationwithpaging.data.Movie

class MovieAdapter : PagedListAdapter<Movie, MovieAdapter.ViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_movie,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val posterImageView: ImageView = view.findViewById(R.id.poster)
        private val title: TextView = view.findViewById(R.id.title)
        private val popularity: TextView = view.findViewById(R.id.popularity)

        fun bind(movie: Movie?) {
            Glide.with(posterImageView.context).load(movie?.posterPath).into(posterImageView)
            title.text = movie?.title
            popularity.text = movie?.id

        }
    }


    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Movie>() {
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.title == newItem.title

        }
    }
}