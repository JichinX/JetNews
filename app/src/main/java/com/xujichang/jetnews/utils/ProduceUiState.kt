package com.xujichang.jetnews.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import com.xujichang.jetnews.data.Result
import com.xujichang.jetnews.ui.state.UiState
import com.xujichang.jetnews.ui.state.copyWithResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

data class ProducerResult<T>(
    val result: State<T>,
    val onRefresh: () -> Unit,
    val onClearError: () -> Unit
)

@Composable
fun <Producer, T> produceUiState(
    producer: Producer,
    block: suspend Producer.() -> Result<T>
): ProducerResult<UiState<T>> = produceUiState(producer, Unit, block)

@Composable
fun <Producer, T> produceUiState(
    producer: Producer,
    key: Any?,
    block: suspend Producer.() -> Result<T>
): ProducerResult<UiState<T>> {
    val refreshChannel = remember {
        Channel<Unit> { Channel.CONFLATED }
    }
    val errorClearChannel = remember {
        Channel<Unit> { Channel.CONFLATED }
    }
    val result = produceState(UiState<T>(true), producer, key) {
        value = UiState(true)
        refreshChannel.send(Unit)
        launch {
            for (clearEvent in errorClearChannel) {
                value = value.copy(exception = null)
            }
        }
        for (refreshEvent in refreshChannel) {
            value = value.copy(loading = true)
            value = value.copyWithResult(producer.block())
        }
    }
    return ProducerResult(
        result = result,
        onRefresh = { refreshChannel.trySend(Unit) },
        onClearError = { errorClearChannel.trySend(Unit) }
    )
}
