package com.xujichang.jetnews.data.posts

import com.xujichang.jetnews.data.Result
import com.xujichang.jetnews.model.Post
import kotlinx.coroutines.flow.Flow

interface PostsRepository {
    suspend fun getPosts(): Result<List<Post>>

    fun observeFavorites(): Flow<Set<String>>
}