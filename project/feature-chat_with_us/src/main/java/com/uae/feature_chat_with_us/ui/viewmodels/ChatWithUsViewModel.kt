package com.uae.feature_chat_with_us.ui.viewmodels

import com.uae.core_common.BaseViewModel
import com.uae.feature_chat_with_us.domain.ChatWithUsRepository
import com.uae.feature_chat_with_us.remote.model.response.ChatSupportTicketsListResponse
import com.uae.feature_chat_with_us.ui.pagination.ChatSupportListDataSource
import com.uae.feature_chat_with_us.ui.states.ChatWithUsStates
import com.uae.feature_chat_with_us.ui.utils.ChatSupportType
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@HiltViewModel
class ChatWithUsViewModel @Inject constructor(
    private val chatWithUsRepository: ChatWithUsRepository
): BaseViewModel<ChatWithUsStates>(ChatWithUsStates()){


    private val _selectedChatType = MutableStateFlow(ChatSupportType.OPEN.type)
    val selectedChatType = _selectedChatType.asStateFlow()

    val openedListPaging by lazy {
        getChatSupportList(ChatSupportType.OPEN.type).cachedIn(viewModelScope)
    }

    val closedListPaging by lazy {
        getChatSupportList(ChatSupportType.COMPLETED.type).cachedIn(viewModelScope)
    }

    fun updateType(status : Int){
        _selectedChatType.value = status
    }

    fun getChatSupportList(status : Int): Flow<PagingData<ChatSupportTicketsListResponse.ChatSupportData>> {
        return Pager(
            PagingConfig(
                initialLoadSize = 10,
                pageSize = 10,
            ),
            pagingSourceFactory = {
            ChatSupportListDataSource(
                status = status,
                chatWithUsRepository = chatWithUsRepository
            )}
        ).flow
    }

}