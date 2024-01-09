package com.zivkesten.searchmovies.presentation

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.zivkesten.searchmovies.data.api.model.MovieDto
import com.zivkesten.searchmovies.databinding.ActivityMainBinding
import com.zivkesten.searchmovies.domain.model.Movie
import com.zivkesten.searchmovies.presentation.list.MoviesLoadStateAdapter
import com.zivkesten.searchmovies.presentation.list.adapter.MoviesRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var moviesAdapter: MoviesRecyclerViewAdapter? = null

    private var binding: ActivityMainBinding? = null

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviesAdapter = MoviesRecyclerViewAdapter()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupBinding()
        collectUiState()
    }

    private fun collectUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                updateUi(it)
            }
        }
    }

    private fun setupBinding() {
        binding?.searchButton?.setOnClickListener { sendQuery() }
        binding?.list?.apply {
            layoutManager = LinearLayoutManager(context)
            initAdapter()
        }
        binding?.searchEditText?.doOnTextChanged { text, _, _, _ ->
            viewModel.searchQuery.value = text.toString()
            if (text?.isEmpty() == true) {
                hideMessage()
            }
        }

        binding?.searchEditText?.text = Editable.Factory.getInstance().newEditable(viewModel.searchQuery.value)
    }

    private fun hideMessage() {
        binding?.error?.visibility = View.GONE
        binding?.list?.visibility = View.VISIBLE
    }

    private fun toggleLoader(show: Boolean) {
        binding?.loader?.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun sendQuery() {
        viewModel.searchQuery.value = binding?.searchEditText?.text.toString()
    }

    private fun initAdapter() {
        binding?.list?.adapter = moviesAdapter?.withLoadStateHeaderAndFooter(
            header = MoviesLoadStateAdapter { moviesAdapter?.retry() },
            footer = MoviesLoadStateAdapter { moviesAdapter?.retry() }
        )
        moviesAdapter?.addLoadStateListener {
            viewModel.onLoadState(it)
        }
    }

    private fun updateUi(state: UiState) {
            when (state) {
                is UiState.Content<*> -> {
                    Log.d("Zivi", "UiState.Content")

                    toggleLoader(false)
                    if (state.data is String) {
                        showMessage(state.data)
                    } else {
                        hideMessage()
                        lifecycleScope.launch {
                            updateList(state)

                        }
                    }
                }
                is UiState.Loading -> {
                    hideMessage()
                    Log.d("Zivi", "UiState.Loading")
                    toggleLoader(true)
                }
                is UiState.Error -> {
                    Log.d("Zivi", "UiState.Error")
                    toggleLoader(false)
                    showMessage(state.exception.localizedMessage)
                }

        }
    }

    private suspend fun updateList(state: UiState.Content<*>) {
        state.data?.let {
            moviesAdapter?.submitData(it as PagingData<Movie>)
            var movies = moviesAdapter?.snapshot()?.items
            Log.d("Zivi", "snapshot $movies")
            delay(500)
            movies = moviesAdapter?.snapshot()?.items
            Log.d("Zivi", "snapshot $movies")
            viewModel.cacheItems(movies)
        }
    }

    private fun showMessage(message: String? = null) {
        binding?.error?.visibility = View.VISIBLE
        binding?.list?.visibility = View.GONE
        toggleLoader(false)
        message?.let {  binding?.errorMsg?.text = it }
    }
}
