package com.uae.feature_chat_with_us.ui.screens

import com.uae.core_common.CommonUiEvent
import com.uae.core_common.R
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.HeaderView
import com.uae.core_common.components.ImageOptionsSelector
import com.uae.core_common.components.TextFieldOuterLabel
import com.uae.core_common.components.bottomPagingLoader
import com.uae.core_common.components.clickableDebAnim
import com.uae.core_common.components.emptyView
import com.uae.core_common.extenstions.showSnackBarWithDismiss
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.core_common.theme.blue_sky_2
import com.uae.core_common.theme.gray_light_3
import com.uae.core_common.theme.grey_5
import com.uae.core_common.utils.AsyncImageWithLoader
import com.uae.core_common.utils.DateFormats
import com.uae.core_common.utils.ObserveUiEvent
import com.uae.core_common.utils.createFolder
import com.uae.core_common.utils.localeUtils.convertToDateFormat
import com.uae.core_network.extensions.getMediaDetails
import com.uae.feature_chat_with_us.remote.model.requestBody.SendMessageBody
import com.uae.feature_chat_with_us.remote.model.response.ChatsListResponse
import com.uae.feature_chat_with_us.ui.event.ChatScreenEvent
import com.uae.feature_chat_with_us.ui.utils.MessageType
import com.uae.feature_chat_with_us.ui.utils.RoleType
import com.uae.feature_chat_with_us.ui.utils.extensions.getMessageTypeFromMimeType
import com.uae.feature_chat_with_us.ui.viewmodels.ChatViewModel
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.zodiaq.ui.theme.Shape_15
import java.io.File


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChatScreen(
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    chatViewModel: ChatViewModel = hiltViewModel(), id: String?, chatType: String?,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current
) {


    val context = LocalContext.current

    val localKeyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }

    val pagingData = chatViewModel.messagesListPaging.collectAsLazyPagingItems()

    val uiState by chatViewModel.uiState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    var message by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(id) {
        chatViewModel.getChatsList(id = id)
    }

    LaunchedEffect(pagingData?.loadState?.refresh) {
        if (pagingData?.loadState?.refresh is LoadState.NotLoading) {

            chatViewModel.updateState {
                it?.copy(isRefreshing = false)
            }
        }
        if (pagingData.loadState.refresh is LoadState.Error) {
            (pagingData.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                chatViewModel.updateState {
                    it?.copy(isRefreshing = false)
                }
            }
        }
    }
    if (pagingData.loadState.hasError) {
        (pagingData.loadState.refresh as LoadState.Error).error.let {
            Log.d("fkbnfbnf", it.message.toString())
        }

    }




    ObserveUiEvent(chatViewModel.uiEvent) { uIEvent ->

        when (uIEvent) {
            is CommonUiEvent.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }
            is CommonUiEvent.NavigateTo -> {}
            is ChatScreenEvent.SendMessage -> {

                message = ""
                localKeyboardController?.hide()
                focusRequester.freeFocus()
                chatViewModel.sendMessage(uIEvent.sendMessageBody)
                chatViewModel.addInPagingList(pagingData.itemSnapshotList, uIEvent.sendMessageBody)
            }
            is ChatScreenEvent.SendMediaMessage -> {

                message = ""
                localKeyboardController?.hide()
                focusRequester.freeFocus()
                chatViewModel.sendMediaMessage(uIEvent.sendMessageBody)
                chatViewModel.addInPagingList(pagingData.itemSnapshotList, uIEvent.sendMessageBody)
            }
        }

    }
    var showImageSelectorDialog by remember { mutableStateOf(false) }
    var fileUri: Uri? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        var fileName = "profile_image_${System.currentTimeMillis()}"
        if (!fileName.isNullOrEmpty()) {
            val folderFile = File(context.cacheDir, fileName).createFolder()
            val file = File(folderFile, fileName)
            fileUri = FileProvider.getUriForFile(
                context, "${context.packageName}.provider", file
            )
        }
    }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                fileUri?.let { uri ->
                    val mediaDetails = uri.getMediaDetails(context)

                    val body = SendMessageBody(
                        chatId = id,
                        chatType = chatType,
                        fileUri = it.toString(),
                        fileName = mediaDetails.fileName,
                        size = mediaDetails.size.toString(),
                        mimeType = mediaDetails.mimeType,
                        messageType = mediaDetails.mimeType.getMessageTypeFromMimeType().value,
                    )
                    chatViewModel.onEvent(ChatScreenEvent.SendMediaMessage(body))
                }
            }
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                uri?.let {
                    val mediaDetails = uri.getMediaDetails(context)

                    val body = SendMessageBody(
                        chatId = id,
                        chatType = chatType,
                        fileUri = it.toString(),
                        fileName = mediaDetails.fileName,
                        size = mediaDetails.size.toString(),
                        mimeType = mediaDetails.mimeType,
                        messageType = mediaDetails.mimeType.getMessageTypeFromMimeType().value,
                    )
                    chatViewModel.onEvent(ChatScreenEvent.SendMediaMessage(body))
                }
//                profileSetupViewModel.updateState { state ->
//                    state.copy()
//                }
//                profileViewModel.updateState { state ->
//                    state?.copy(userDetails = state?.userDetails?.copy(photo = uri.toString()))
//                }
//                profileViewModel.uploadItem(galleryMediaItemSelection?.copy(uri = uri.toString()))
            }
        }




    BoxCommon(
        isLoading = pagingData.loadState.refresh is LoadState.Loading && uiState?.isRefreshing == false,
        isAppearanceLightStatusBars = false,
        isAppearanceLightNavigationBars = false
    )
    {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
        ) {
            HeaderView(title = "Chat") {
                backStack.removeLastOrNull()
            }
            PullToRefreshBox(
                isRefreshing = uiState?.isRefreshing ?: false,
                onRefresh = {
                    chatViewModel.updateState {
                        it?.copy(isRefreshing = true)
                    }
                    pagingData.refresh()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            {

                LazyColumn(
//                    state = listState,
                    reverseLayout = true,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                )
                {

                    items(pagingData?.itemCount ?: 0) { index ->
                        val data = pagingData?.get(index)

                        if (data?.role == RoleType.USER.type) {
                            if(data.messageType == MessageType.TEXT.value) {
                                RightMessageCard(data)
                            }else{
                                RightMediaMsgCard(data)
                            }
                        } else {
                            if(data?.messageType == MessageType.TEXT.value) {
                                LeftMessageCard(data)
                            }else{
                                LeftMediaMsgCard(data)
                            }
                        }
                    }
                    item {
                        bottomPagingLoader(
                            isVisible = pagingData?.loadState?.append is LoadState.Loading || pagingData?.loadState?.append is LoadState.Error,
                            isRetry = pagingData?.loadState?.append is LoadState.Error,
                            onRetry = {
                                pagingData?.retry()
                            })
                    }

                    item {
                        if (pagingData?.itemCount == 0) {
                            if (pagingData.loadState.refresh is LoadState.NotLoading) {
//                                emptyView(
//                                    modifier = Modifier
//                                        .fillMaxSize(),
//                                    isShowImage = false
//                                )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(R.drawable.attach), contentDescription = null,
                    modifier = Modifier.size(45.dp).clickableDebAnim {
                        showImageSelectorDialog = true
                    })
                TextFieldOuterLabel(
                    focusRequester = focusRequester,
                    value = message,
                    onValueChange = {
                        message = it
                    },
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(onSend = {
                        val body = SendMessageBody(
                            chatId = id,
                            chatType = chatType,
                            message = message,
                            messageType = MessageType.TEXT.value
                        )

                        chatViewModel.onEvent(ChatScreenEvent.SendMessage(body))
                    }),
                    hint = "Type here...",
                    modifier = Modifier.weight(1f)
                )

                AnimatedContent(uiState?.isMessageSending ?: false) {
                    if (!it) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(R.drawable.send),
                            contentDescription = null,
                            modifier = Modifier.size(45.dp).clickableDebAnim {
                                val body = SendMessageBody(
                                    chatId = id,
                                    chatType = chatType,
                                    message = message,
                                    messageType = MessageType.TEXT.value
                                )

                                chatViewModel.onEvent(ChatScreenEvent.SendMessage(body))

                            })
                    } else {
                        LoadingIndicator(modifier = Modifier.size(30.dp))
                    }
                }
            }
        }
    }

    ImageOptionsSelector(showDialog = showImageSelectorDialog, onDismiss = {
        showImageSelectorDialog = false
    }, onCamera = {
        showImageSelectorDialog = false
        fileUri?.let {
            takePictureLauncher.launch(it)
        }
    }, onGallery = {
        showImageSelectorDialog = false
        galleryLauncher.launch("image/*")
    })
}


@Composable
fun RightMessageCard(data: ChatsListResponse.Data.MsgData?) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                data?.createdAt?.convertToDateFormat(toFormat = DateFormats.DATE_FORMAT_27) ?: "",
                color = grey_5,
                fontSize = 10.sp,
                modifier = Modifier.padding(end = 45.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                Box(
                    modifier = Modifier
                        .background(
                            color = gray_light_3,
                            shape = RoundedCornerShape(
                                topStart = 10.dp,
                                bottomStart = 10.dp,
                                bottomEnd = 10.dp
                            )
                        )
                        .padding(12.dp)
                ) {
                    Text(data?.message ?: "", color = Color.Black)
                }
                AsyncImageWithLoader(
                    image = data?.user?.profilePic,
                    modifier = Modifier.size(35.dp),
                    placeholder = painterResource(R.drawable.profile)
                )


            }
        }
    }
}

@Composable
fun RightMediaMsgCard(data: ChatsListResponse.Data.MsgData?) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                data?.createdAt?.convertToDateFormat(toFormat = DateFormats.DATE_FORMAT_27) ?: "",
                color = grey_5,
                fontSize = 10.sp,
                modifier = Modifier.padding(end = 45.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                    AsyncImageWithLoader(
                        image = data?.url ?: data?.fileName,
                        modifier = Modifier.size(100.dp)
                            .clip(Shape_15),
                        placeholder = painterResource(R.drawable.profile)
                    )
                AsyncImageWithLoader(
                    image = data?.user?.profilePic,
                    modifier = Modifier.size(35.dp),
                    placeholder = painterResource(R.drawable.profile)
                )
            }
        }
    }
}

@Composable
fun LeftMessageCard(data: ChatsListResponse.Data.MsgData?) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                data?.createdAt?.convertToDateFormat(toFormat = DateFormats.DATE_FORMAT_27) ?: "",
                color = grey_5,
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 45.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AsyncImageWithLoader(
                    image = data?.user?.profilePic,
                    modifier = Modifier.size(35.dp),
                    placeholder = painterResource(R.drawable.headphone)
                )

                Box(
                    modifier = Modifier
                        .background(
                            color = blue_sky_2,
                            shape = RoundedCornerShape(
                                topEnd = 10.dp,
                                bottomStart = 10.dp,
                                bottomEnd = 10.dp
                            )
                        )
                        .padding(12.dp)
                ) {
                    Text(data?.message ?: "", color = Color.Black)
                }

            }
        }
    }
}

@Composable
fun LeftMediaMsgCard(data: ChatsListResponse.Data.MsgData?) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                data?.createdAt?.convertToDateFormat(toFormat = DateFormats.DATE_FORMAT_27) ?: "",
                color = grey_5,
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 45.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AsyncImageWithLoader(
                    image = data?.user?.profilePic,
                    modifier = Modifier.size(35.dp),
                    placeholder = painterResource(R.drawable.headphone)
                )

                AsyncImageWithLoader(
                    image = data?.url ?: data?.fileName,
                    modifier = Modifier.size(100.dp)
                        .clip(Shape_15),
                    placeholder = painterResource(R.drawable.profile)
                )

            }
        }
    }
}
