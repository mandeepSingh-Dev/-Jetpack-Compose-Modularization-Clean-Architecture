package com.uae.core_common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel<T : UIState>(initialState : T? = null) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    @Inject
    lateinit var userMgr : UserManager

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun updateState(update : (T?) -> T?) {
        _uiState.update { state ->
            update(state)
        }
    }

    fun onEvent(event : UIEvent) = viewModelScope.launch{
        _uiEvent.emit(event)
    }

}