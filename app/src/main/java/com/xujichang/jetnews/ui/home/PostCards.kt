package com.xujichang.jetnews.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xujichang.jetnews.R
import com.xujichang.jetnews.model.Post

@Composable
fun PostCardTop(
    post: Post,
    modifier: Modifier
) {
    val typography = MaterialTheme.typography
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val imageModifier = Modifier
            .heightIn(min = 180.dp)
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
        Image(
            painter = painterResource(id = post.imageId),
            contentDescription = null,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = post.title,
            style = typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = post.metadata.author.name,
            style = typography.subtitle2,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = "${post.metadata.date} - ${post.metadata.readTimeMinutes} min read",
                style = typography.subtitle2
            )
        }
    }
}

@Composable
fun PostCardSimple(
    post: Post,
    navigationToArticle: (String) -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    val bookmarkAction = stringResource(if (isFavorite) R.string.unbookmark else R.string.bookmark)
    Row(
        modifier = Modifier
            .clickable(onClick = { navigationToArticle(post.id) })
            .padding(16.dp)
            .semantics {
                customActions = listOf(
                    CustomAccessibilityAction(
                        label = bookmarkAction,
                        action = { onToggleFavorite();true }
                    )
                )
            }
    ) {
        PostImage(post, Modifier.padding(end = 16.dp))
        Column(modifier = Modifier.weight(1f)) {
            PostTitle(post)
            AuthorAndReadTime(post)
        }
    }
}

@Composable
fun PostCardPopular(
    postsPopular: Post,
    navigationToArticle: (String) -> Unit,
    modifier: Modifier
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.size(280.dp, 240.dp)
    ) {
        Column(modifier = Modifier.clickable { navigationToArticle(postsPopular.id) }) {
            Image(
                painter = painterResource(id = postsPopular.imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = postsPopular.title,
                    style = MaterialTheme.typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = postsPopular.metadata.author.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "${postsPopular.metadata.date} - " +
                            "${postsPopular.metadata.readTimeMinutes} min read",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun PostCardHistory(post: Post, navigationToArticle: (String) -> Unit) {
    var openDialog by remember { mutableStateOf(false) }
    Row(Modifier.clickable { navigationToArticle(post.id) }) {
        PostImage(
            post = post,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )
        Column(
            Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .weight(1F)
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text = "BASED ON YOUR HISTORY", style = MaterialTheme.typography.overline)
            }
            PostTitle(post = post)
            AuthorAndReadTime(post = post, modifier = Modifier.padding(top = 4.dp))
        }
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            IconButton(onClick = { openDialog = false }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(id = R.string.cd_more_actions)
                )
            }
        }
    }
    if (openDialog) {
        AlertDialog(
            modifier = Modifier.padding(20.dp),
            onDismissRequest = { openDialog = false },
            title = {
                Text(
                    text = stringResource(id = R.string.fewer_stories),
                    style = MaterialTheme.typography.body1
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.fewer_stories_content),
                    style = MaterialTheme.typography.body1
                )
            },
            confirmButton = {
                Text(
                    text = stringResource(id = R.string.agree),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable { openDialog = false }
                )
            }
        )
    }

}

@Composable
fun AuthorAndReadTime(post: Post, modifier: Modifier = Modifier) {
    Row(modifier) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            val textStyle = MaterialTheme.typography.body2
            Text(text = post.metadata.author.name, style = textStyle)
            Text(text = " - ${post.metadata.readTimeMinutes} min read", style = textStyle)
        }
    }
}

@Composable
fun PostTitle(post: Post) {
    Text(text = post.title, style = MaterialTheme.typography.subtitle1)
}

@Composable
fun PostImage(post: Post, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(post.imageThumbId),
        contentDescription = null,
        modifier = modifier
            .size(40.dp, 40.dp)
            .clip(MaterialTheme.shapes.small)
    )
}
