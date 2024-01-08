//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.paging.PagingData
//import androidx.paging.compose.collectAsLazyPagingItems
//import com.zivkesten.searchmovies.MovieDto
//import com.zivkesten.searchmovies.UiState
//import com.zivkesten.searchmovies.viewmodels.MoviesViewModel
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.mapNotNull
//
//@Composable
//fun MainScreen() {
//    var searchText by remember { mutableStateOf(TextFieldValue()) }
//    val viewModel: MoviesViewModel = viewModel<MoviesViewModel>()
//    val uiState = viewModel.uiState.collectAsState().value
//    val flow = viewModel.uiState
//        .mapNotNull { uiState ->
//            when (uiState) {
//                is UiState.Content<*> -> uiState.data as? PagingData<MovieDto>
//                else -> null
//            }
//        }.collectAsLazyPagingItems()
//
//    Column {
//        // Search Bar
//        BasicTextField(
//            value = searchText,
//            onValueChange = { newText -> searchText = newText },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            decorationBox = { innerTextField ->
//                Row(Modifier.padding(8.dp)) {
//                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
//                    Spacer(Modifier.width(8.dp))
//                    Box(Modifier.weight(1f)) {
//                        if (searchText.text.isEmpty()) {
//                            Text("Search", color = Color.Gray)
//                        }
//                        innerTextField()
//                    }
//                }
//            }
//        )
//
//        when (uiState) {
//            is UiState.Error -> {}
//            is UiState.Loading -> {}
//            is UiState.Content<*> -> {
//
//                val pagingData =  viewModel.uiState
//                    .mapNotNull { uiState ->
//                        when (uiState) {
//                            is UiState.Content<*> -> uiState.data as? PagingData<MovieDto>
//                            else -> null
//                        }
//                    }.collectAsLazyPagingItems()
//                // Movie List
//                LazyColumn(
//                    state = rememberLazyListState(),
//                    modifier = Modifier.then(
//                        Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 16.dp, end = 8.dp, start = 8.dp)
//
//                    ),
//                    content = {
//                        items(
//                            items = pagingData.,
//                            itemContent = {
//                                Box(
//                                    Modifier
//                                        .size(100.dp)
//                                        .background(Color.Green))
//                            }
//                        )
//                    }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun MovieItem(title: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(64.dp)
//                .background(Color.Blue),  // Placeholder for image
//        )
//        Spacer(Modifier.width(8.dp))
//        Column {
//            Text(text = title, style = MaterialTheme.typography.headlineSmall)
//            Text(text = "....", style = MaterialTheme.typography.bodySmall)
//        }
//    }
//    Divider()
//}
