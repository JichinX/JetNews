package com.xujichang.jetnews.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.xujichang.jetnews.R
import com.xujichang.jetnews.data.posts.PostsRepository
import com.xujichang.jetnews.model.Post
import com.xujichang.jetnews.ui.components.InsetAwareTopAppBar
import com.xujichang.jetnews.ui.state.UiState
import com.xujichang.jetnews.utils.produceUiState
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    postsRepository: PostsRepository,
) {
    val (postUiState, refreshPost, clearError) = produceUiState(postsRepository) {
        getPosts()
    }
    val favorites by postsRepository.observeFavorites().collectAsState(setOf())
    val coroutineScope = rememberCoroutineScope()

}

@Composable
fun HomeScreen(
    posts: UiState<List<Post>>,
    onRefreshPosts: () -> Unit,
    onErrorDismiss: () -> Unit,
    openDrawer: () -> Unit,
    scaffoldState: ScaffoldState
) {
    val coroutineScope = rememberCoroutineScope()
    if (posts.hasError) {
        val errorMessage = stringResource(id = R.string.load_error)
        val retryMessage = stringResource(id = R.string.retry)

        val onRefreshPostsState by rememberUpdatedState(newValue = onRefreshPosts)
        val onErrorDismissState by rememberUpdatedState(newValue = onErrorDismiss)

        LaunchedEffect(scaffoldState.snackbarHostState) {
            val snackbarResult =
                scaffoldState.snackbarHostState.showSnackbar(
                    message = errorMessage,
                    actionLabel = retryMessage
                )
            when (snackbarResult) {
                SnackbarResult.ActionPerformed -> onRefreshPostsState()
                SnackbarResult.Dismissed -> onErrorDismissState()
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            val title = stringResource(id = R.string.app_name)
            InsetAwareTopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = { coroutineScope.launch { openDrawer() } }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_jetnews_logo),
                            contentDescription = stringResource(id = R.string.cd_open_navigation_drawer)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        LoadingContent(
            empty = posts.initialLoad,
            emptyContent = { FullScreenLoading() },
            loading = posts.loading,
            onRefresh = onRefreshPosts
        ) {
            HomeScreenErrorAndContent()
        }
    }
}

@Composable
fun HomeScreenErrorAndContent(
    posts: UiState<List<Post>>,
    onRefresh: () -> Unit,
    navigationToArticle: (String) -> Unit,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (posts.data != null) {
        PostList(posts.data, navigationToArticle, favorites, onToggleFavorite, modifier)
    } else if (!posts.hasError) {
        TextButton(onClick = onRefresh, modifier.fillMaxSize()) {
            Text(text = "Tap to load content", textAlign = TextAlign.Center)
        }
    } else {
        Box(modifier.fillMaxSize())
    }
}

@Composable
fun PostList(
    posts: List<Post>,
    navigationToArticle: (String) -> Unit,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier
) {
    val postTop = posts[3]
    val postsSimple = posts.subList(0, 2)
    val postsPopular = posts.subList(2, 7)
    val postsHistory = posts.subList(7, 10)
    LazyColumn(
        modifier = modifier,
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyTop = false
        )
    ) {
        item { PostListTopSelection(postTop, navigationToArticle) }
        item {
            PostListSimpleSelection(
                postsSimple,
                navigationToArticle,
                favorites,
                onToggleFavorite
            )
        }
        item { PostListPopularSelection(postsPopular, navigationToArticle) }
        item { PostListHistorySelection(postsHistory, navigationToArticle) }
    }
}

@Composable
fun PostListHistorySelection(
    postsHistory: List<Post>,
    navigationToArticle: (String) -> Unit
) {
    Column {
        postsHistory.forEach { post ->
            PostCardHistory(post, navigationToArticle)
            PostListDivider()
        }
    }

}

@Composable
fun PostListPopularSelection(
    postsPopular: List<Post>,
    navigationToArticle: (String) -> Unit
) {
    Column {
        Text(
            text = "Popular on Jetnnews",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.subtitle1
        )
        LazyRow(modifier = Modifier.padding(end = 16.dp)) {
            items(postsPopular) { post ->
                PostCardPopular(
                    post,
                    navigationToArticle,
                    Modifier.padding(start = 16.dp, bottom = 16.dp)
                )
            }
        }
        PostListDivider()
    }
}

@Composable
fun PostListSimpleSelection(
    postsSimple: List<Post>,
    navigationToArticle: (String) -> Unit,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit
) {
    Column {
        postsSimple.forEach { post ->
            PostCardSimple(
                post = post,
                navigationToArticle = navigationToArticle,
                isFavorite = favorites.contains(post.id),
                onToggleFavorite = { onToggleFavorite(post.id) }
            )
            PostListDivider()
        }
    }
}

@Composable
fun PostListTopSelection(postTop: Post, navigationToArticle: (String) -> Unit) {
    Text(
        text = "Top stories for you",
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
    )
    PostCardTop(
        post = postTop, modifier = Modifier.clickable { navigationToArticle(postTop.id) }
    )
    PostListDivider()
}

@Composable
fun PostListDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
    )
}

@Composable
fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = loading),
            onRefresh = onRefresh,
            content = content
        )
    }
}