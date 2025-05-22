package com.example.newsaggregator.ui.screens.mainscreen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.newsaggregator.R
import com.example.newsaggregator.fromHtmlToPlainText
import com.example.newsaggregator.ui.model.NewsUiModel
import com.example.newsaggregator.ui.model.parseAndFormatDate
import com.example.newsaggregator.ui.screens.mainscreen.state.NewsState
import com.example.newsaggregator.ui.theme.Dimens
import com.example.newsaggregator.ui.theme.LoadableImage
import com.example.newsaggregator.ui.theme.PurpleGrey80
import androidx.compose.foundation.lazy.rememberLazyListState

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    onNavigateToNews: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = viewModel) {
        viewModel.loadAllNews()
    }

    when (state) {
        NewsState.Loading -> NewsLoading()

        is NewsState.Error -> {
            val message = (state as NewsState.Error).message
            NewsError(
                errorMessage = message,
                onRefreshNews = { viewModel.loadAllNews() }
            )
        }

        is NewsState.Ready -> {
            val news = (state as NewsState.Ready).data
            NewsList(
                news = news,
                query = viewModel.query,
                onQueryChange = viewModel::onQueryChanged,
                onNavigateToNews = onNavigateToNews,
                listState = listState,
                onSearch = viewModel::onSearch,
                onMenuClick = viewModel::onMenuClicked,
                isDateSorted = viewModel.isDateSorted
            )
        }
    }
}

@Composable
fun NewsList(
    news: List<NewsUiModel>,
    query: String,
    onQueryChange: (String) -> Unit,
    onNavigateToNews: (String) -> Unit,
    listState: LazyListState,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    isDateSorted: Boolean
) {

    var filteredNews = if (query.isBlank()) news else news.filter {
        it.title.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
    }

    if (isDateSorted) {
        filteredNews = filteredNews.sortedByDescending { it.pubDate }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBarMain(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            onMenuClick = onMenuClick,
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(filteredNews, key = { it.guid }) { item ->
                NewsItem(
                    item = item,
                    onClick = { onNavigateToNews(item.guid) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun NewsItem(
    item: NewsUiModel,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            if (!item.imageUrlLarge.isNullOrEmpty()) {
                LoadableImage(
                    photoUri = item.imageUrlLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(Dimens.paddingMedium))
                )
            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 12.dp)
            )
            Row(
                modifier = modifier
                    .semantics(mergeDescendants = true) {}
                    .padding(Dimens.paddingSmall)
            ) {
                Image(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    colorFilter = ColorFilter.tint(LocalContentColor.current),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        text = item.author.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = item.parseAndFormatDate(item.pubDate),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            Text(
                text = fromHtmlToPlainText(item.description),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun NewsLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = PurpleGrey80)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}



@Composable
fun NewsError(
    modifier: Modifier = Modifier,
    errorMessage: String?,
    onRefreshNews: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = errorMessage ?: "Unknown error",
            color = Color.Red,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .clickable(onClick = onRefreshNews),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = stringResource(R.string.update)
            )
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarMain(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {
            onSearch(query)
        },
        active = false,
        onActiveChange = { },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .heightIn(min = 48.dp),
        placeholder = { Text(text = stringResource(R.string.enter_words)) },
        leadingIcon = {
            Crossfade(targetState = query.isNotEmpty()) { hasText ->
                if (hasText) {
                    IconButton({ onQueryChange("") }) {
                        Icon(Icons.Default.Close, null)
                    }
                } else {
                    Icon(Icons.Default.Search, "search")
                }
            }
        },
        trailingIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, "filter", Modifier.size(20.dp))
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {}
}







