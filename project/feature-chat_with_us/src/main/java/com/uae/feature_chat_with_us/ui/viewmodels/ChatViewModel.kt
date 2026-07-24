package com.uae.feature_chat_with_us.ui.viewmodels

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import com.uae.core_common.BaseViewModel
import com.uae.core_common.CommonUiEvent
import com.uae.core_common.utils.fromJson
import com.uae.core_network.data.model.ImageUploadResponse
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.utils.FolderName
import com.uae.feature_chat_with_us.domain.ChatWithUsRepository
import com.uae.feature_chat_with_us.domain.mapper.toMsgData
import com.uae.feature_chat_with_us.domain.usecase.ChatWithUsAllUseCases
import com.uae.feature_chat_with_us.remote.model.requestBody.SendMessageBody
import com.uae.feature_chat_with_us.remote.model.response.ChatsListResponse
import com.uae.feature_chat_with_us.ui.pagination.ChatsListDataSource
import com.uae.feature_chat_with_us.ui.states.ChatScreenState
import com.uae.feature_chat_with_us.ui.utils.ChatType
import com.uae.feature_chat_with_us.ui.utils.MessageType
import com.uae.feature_profile.remote.model.response.ProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.collections.fold


@HiltViewModel
class ChatViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val commonAllUseCases: CommonAllUseCases,
    private val chatWithUsAllUseCases: ChatWithUsAllUseCases,
    private val chatWithUsRepository: ChatWithUsRepository
) : BaseViewModel<ChatScreenState>(ChatScreenState()) {
    private var currentPagingData: PagingData<ChatsListResponse.Data.MsgData> = PagingData.empty()


    private val pendingMessages = MutableStateFlow<List<ChatsListResponse.Data.MsgData>>(emptyList())

    // holds the *current* Pager flow; starts empty until getChatsList() is called
    private val pagerFlowHolder =
        MutableStateFlow<Flow<PagingData<ChatsListResponse.Data.MsgData>>>(flowOf(PagingData.empty()))

    // defined ONCE — identity never changes, safe to collect from Compose immediately
    val messagesListPaging: Flow<PagingData<ChatsListResponse.Data.MsgData>> =
        pagerFlowHolder
            .flatMapLatest { it } // switches to whichever Pager flow is current
            .cachedIn(viewModelScope)
            .combine(pendingMessages) { pagingData, pending ->
                pending.fold(pagingData) { acc, item ->
                    acc.insertHeaderItem(item = item)
                }
            }

    fun getChatsList(id: String?) {
        pagerFlowHolder.value = Pager(
            config = PagingConfig(
                initialLoadSize = 10,
                pageSize = 10
            ),
            pagingSourceFactory = {
                ChatsListDataSource(
                    id = id ?: "",
                    chatWithUsRepository = chatWithUsRepository
                )
            }
        ).flow
    }

    fun addInPagingList(sendMessageBody: SendMessageBody?) {
        viewModelScope.launch {
            if (userDetails == null) {
                userDetails = userMgr.getUserDataString().fromJson<ProfileResponse.UserData>()
            }
            val newMessage = sendMessageBody.toMsgData(userDetails)
            pendingMessages.value = pendingMessages.value + newMessage
        }
    }

    var userDetails : ProfileResponse.UserData? = null
//    fun getChatsList(id: String?) {
//        Log.d("kfnbkfnbjf", id.toString())
//        Pager(
//            config = PagingConfig(
//                initialLoadSize = 10,
//                pageSize = 10
//            ),
//            pagingSourceFactory = {
//                ChatsListDataSource(
//                    id = id ?: "",
//                    chatWithUsRepository = chatWithUsRepository
//                )
//            }
//        ).flow.cachedIn(viewModelScope)
//            .onEach {
//                currentPagingData = it
//                _messagesListPaging.value = it
//            }.launchIn(viewModelScope)
//    }


    fun sendMediaMessage(body: SendMessageBody?){
        val folderName = if(body?.chatType == ChatType.ASSISTANCE.value) FolderName.ASSISTANCE_CHAT_MEDIA_FOLDER.folderName else FolderName.SUPPORT_CHAT_MEDIA_FOLDER.folderName
        if(body?.fileUri.isNullOrEmpty()) return
        uploadImage(body.fileUri, folderName).onEach { networkResult ->
            if(networkResult is NetworkResult.Success){

                val updatedBody = body.copy(
                    url = networkResult.data?.data?.file,
                )

                sendMessage(sendMessageBody = updatedBody)
            }
        }.launchIn(viewModelScope)
    }
    fun sendMessage(sendMessageBody: SendMessageBody?) {


        val error = validateMessage(sendMessageBody)
        if(!error.isNullOrEmpty()){
            onEvent(CommonUiEvent.ShowError(error))
        }else {

            chatWithUsAllUseCases.sendMessageUseCase(sendMessageBody)?.onEach {networkResult ->
                when(networkResult){
                    is NetworkResult.Error -> {
                        updateState { state ->
                            state?.copy(isMessageSending = true)
                        }
                    }
                    is NetworkResult.Loading -> {
                        updateState { state ->
                            state?.copy(isMessageSending = false)
                        }
                    }
                    is NetworkResult.Success -> {
                        updateState { state ->
                            state?.copy(isMessageSending = false)
                        }
                    }
                }
            }
                ?.launchIn(viewModelScope)
        }
    }

//    fun addInPagingList(
//        sendMessageBody: SendMessageBody?
//    ) {
//        viewModelScope.launch {
//            if (userDetails == null) {
//                userDetails = userMgr.getUserDataString().fromJson<ProfileResponse.UserData>()
//            }
//
//            val newMessage = sendMessageBody.toMsgData(userDetails)
//            currentPagingData = currentPagingData.insertHeaderItem(item = newMessage)
//            _messagesListPaging.value = currentPagingData
//        }
//    }

    fun validateMessage(body: SendMessageBody?): String? {

        return when(body?.messageType){
            MessageType.TEXT.value -> {
                if(body.message.isNullOrEmpty()) "Please type your message!" else null
            }
            MessageType.IMAGE.value,MessageType.AUDIO.value,MessageType.VIDEO.value,MessageType.DOCUMENT.value,MessageType.FILE.value -> {
                if(body.url.isNullOrEmpty()) "Invalid file, Please select file again."  else null
            }

            else -> null
        }
    }

    fun uploadImage(fileUri : String?,folderName : String?): Flow<NetworkResult<ImageUploadResponse>> {
        return commonAllUseCases.imageUploadUseCase(
            fileUri = fileUri,
            folderName = folderName
        )
    }


}