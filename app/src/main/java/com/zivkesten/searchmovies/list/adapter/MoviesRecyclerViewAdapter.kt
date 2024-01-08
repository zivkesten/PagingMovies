package com.zivkesten.searchmovies.list.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.zivkesten.searchmovies.data.model.MovieDto
import com.zivkesten.searchmovies.list.viewholders.MovieViewHolder

class MoviesRecyclerViewAdapter: PagingDataAdapter<MovieDto, MovieViewHolder>(PHOTO_COMPARATOR) {

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<MovieDto>() {
            override fun areItemsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean =
                oldItem.imdbID == newItem.imdbID

            override fun areContentsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { photo ->
            holder.bind(photo)
        }
    }
}
