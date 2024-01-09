package com.zivkesten.searchmovies.presentation.list.viewholders

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.zivkesten.searchmovies.R
import com.zivkesten.searchmovies.domain.model.Movie

/**
 * View Holder for a [Movie] RecyclerView list item.
 */
class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.movie_title)
    private val year: TextView = view.findViewById(R.id.movie_subtitle)
    private val media: ImageView = view.findViewById(R.id.movie_image)
    private var movie: Movie? = null

    @SuppressLint("SetTextI18n")
    fun bind(movie: Movie?) {
        if (movie == null) {
            name.text = "loading"
            year.text = "loading"
        } else {
            showData(movie)
        }
    }

    private fun showData(movie: Movie) {
        this.movie = movie
        name.text = movie.title
        year.text = movie.year

        Picasso.get()
            .load(movie.poster)
            .into(media)
    }

    companion object {
        fun create(parent: ViewGroup): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.movie_item, parent, false)
            return MovieViewHolder(view)
        }
    }
}
