package com.uae.core_common.utils

import com.uae.core_common.UIEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest


@Composable
fun <T : UIEvent> ObserveUiEvent(uiEvent : SharedFlow<T>, onEvent : suspend (T) -> Unit) {

    LaunchedEffect(Unit) {
        uiEvent.collectLatest {uiEvent ->
            onEvent(uiEvent)
        }
    }

}