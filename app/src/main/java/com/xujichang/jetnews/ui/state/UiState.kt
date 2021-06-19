package com.xujichang.jetnews.ui.state

import com.xujichang.jetnews.data.Result

data class UiState<T>(
    val loading: Boolean = false,
    val exception: Exception? = null,
    val data: T? = null
) {
    val hasError: Boolean
        get() = null != exception
    val initialLoad: Boolean
        get() = null == data && loading && !hasError
}

fun <T> UiState<T>.copyWithResult(value: Result<T>): UiState<T> {
    return when (value) {
        is Result.Success -> copy(false, null, value.data)
        is Result.Error -> copy(false, value.exception)
    }
}