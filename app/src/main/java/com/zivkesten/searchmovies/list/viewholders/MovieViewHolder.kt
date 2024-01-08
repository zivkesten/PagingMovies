package com.zivkesten.searchmovies.list.viewholders

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.zivkesten.searchmovies.data.model.MovieDto
import com.zivkesten.searchmovies.R

/**
 * View Holder for a [Movie] RecyclerView list item.
 */
class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.movie_title)
    private val description: TextView = view.findViewById(R.id.movie_subtitle)
    private val media: ImageView = view.findViewById(R.id.movie_image)
    private var movie: MovieDto? = null

    fun bind(photo: MovieDto?) {
        if (photo == null) {
            name.text = "loading"
            description.text = "loading"
        } else {
            showData(photo)
        }
    }

    private fun showData(movieDto: MovieDto) {
        this.movie= movieDto
        name.text = movieDto.title
        description.text = movieDto.imdbID

        Picasso.get()
            .load(movieDto.poster)
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
