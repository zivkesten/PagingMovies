package com.zivkesten.searchmovies.list.types

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.zivkesten.searchmovies.MovieDto
import com.zivkesten.searchmovies.R
import com.zivkesten.searchmovies.list.callback.OnItemClickListener

/**
 * View Holder for a [Moview] RecyclerView list item.
 */
class MovieViewHolder(view: View, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.primary_text)
    private val description: TextView = view.findViewById(R.id.sub_text)
    private val media: ImageView = view.findViewById(R.id.media_image)
    private var movie: MovieDto? = null

    fun bind(photo: MovieDto?) {
        if (photo == null) {
            val resources = itemView.resources
            name.text = "loading"
            description.text = "loading"
        } else {
            showData(photo)
        }
    }

    private fun showData(movieDto: MovieDto) {
        this.movie= movieDto
        name.text = movieDto.title
        description.text = movieDto.language

        Picasso.get()
            .load(movieDto.poster)
            .into(media)
    }

    companion object {
        fun create(parent: ViewGroup, listener: OnItemClickListener): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.movie_item, parent, false)
            return MovieViewHolder(view, listener)
        }
    }
}
