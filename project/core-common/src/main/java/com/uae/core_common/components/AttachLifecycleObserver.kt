package com.uae.core_common.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun AttachLifecycleObserver(lifecycleOwner  : LifecycleOwner =  LocalLifecycleOwner.current,
                            onEvent : (Lifecycle.Event) -> Unit
) {

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { owner, event ->
            onEvent(event)
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

}