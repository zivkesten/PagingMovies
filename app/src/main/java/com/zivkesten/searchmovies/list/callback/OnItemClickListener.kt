package com.zivkesten.searchmovies.list.callback

import com.zivkesten.searchmovies.MovieDto

interface OnItemClickListener {
    fun onItemClick(item: MovieDto)
}