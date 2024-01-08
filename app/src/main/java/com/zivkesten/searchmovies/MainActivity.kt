package com.zivkesten.searchmovies

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.zivkesten.searchmovies.databinding.ActivityMainBinding
import com.zivkesten.searchmovies.list.adapter.MoviesRecyclerViewAdapter
import com.zivkesten.searchmovies.list.callback.OnItemClickListener
import com.zivkesten.searchmovies.ui.theme.SearchMoviesTheme
import com.zivkesten.searchmovies.viewmodels.MoviesViewModel
import com.zivkesten.searchmovies.list.MoviesLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var moviesAdapter: MoviesRecyclerViewAdapter? = null

    var binding: ActivityMainBinding? = null

    val viewModel: MoviesViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_main)
        moviesAdapter = MoviesRecyclerViewAdapter(listener = object :
            OnItemClickListener {
            override fun onItemClick(item: MovieDto) {
                TODO("Not yet implemented")
            }
        })
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding!!.searchEditText.doOnTextChanged { text, _, _, _ ->
            Log.d("Zivi", "doOnTextChanged $text")
            viewModel._searchQuery.value = text.toString()
        }
        binding!!.searchButton.setOnClickListener {
            Log.d("Zivi", "Click")
        }
        setupBinding()

        lifecycleScope.launch {
            viewModel.uiState.collect() {
                Log.d("Zivi", "render $it")
                render(it)
            }
        }

      //  observeSearchTextChanges(binding!!)
    }

    private fun observeSearchTextChanges(binding: ActivityMainBinding) {
        Log.d("Zivi", "observeSearchTextChanges")

      //  binding.searchEditText.setOn
    }

    private fun initiateSearch(query: String) {
        // Implement your search logic here
        // This function will be called with the latest query after the debounce time
    }


    private fun setupBinding() {
        binding?.list?.apply {
            layoutManager = LinearLayoutManager(context)
            //addItemDecoration(VerticalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.list_item_decoration)))
            initAdapter()
        }
        binding?.retryButton?.setOnClickListener { Log.d("Zivi", "click") }

    }

    private fun initAdapter() {
        binding?.list?.adapter = moviesAdapter?.withLoadStateHeaderAndFooter(
            header = MoviesLoadStateAdapter { moviesAdapter?.retry() },
            footer = MoviesLoadStateAdapter { moviesAdapter?.retry() }
        )
        moviesAdapter?.addLoadStateListener {
            Log.w("Zivi", "loading state: ${it.toString()}")
            viewModel.onLoadState(it)

        }
    }

    private fun render(state: UiState) {
        lifecycleScope.launch {
            when (state) {
                is UiState.Content<*> -> {
                    binding?.error?.visibility = View.GONE
                    if (state.data is String) {
                        binding?.error?.visibility = View.VISIBLE
                        binding?.errorMsg?.text = state.data
                    } else {
                        state.data?.let { moviesAdapter?.submitData(it as PagingData<MovieDto>) }
                    }
                }
                is UiState.Error -> {
                    binding?.error?.visibility = View.VISIBLE
                }

                else -> {}
            }

        }
//        state.errorVisibility?.let {
//            binding.mainListErrorMsg.visibility = it
//            binding.retryButton.visibility = it
//            state.errorMessage?.let { binding.mainListErrorMsg.text = state.errorMessage }
//            state.errorMessageResource?.let { binding.mainListErrorMsg.text = getString(state.errorMessageResource) }
//        }
    }

}
