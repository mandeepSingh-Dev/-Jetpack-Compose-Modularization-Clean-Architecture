package com.uae.feature_chat_with_us.ui.screens

import com.uae.core.navigation.ChatWithUsScreens
import com.uae.core_common.CommonUiEvent
import com.uae.core_common.R
import com.uae.core_common.components.AnimatedTabs
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.GradientButton
import com.uae.core_common.components.HeaderView
import com.uae.core_common.components.bottomPagingLoader
import com.uae.core_common.components.clickableDebAnim
import com.uae.core_common.components.emptyView
import com.uae.core_common.extenstions.showSnackBarWithDismiss
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.core_common.theme.POPPINS_MEDIUM
import com.uae.core_common.theme.grey_4
import com.uae.core_common.utils.DateFormats
import com.uae.core_common.utils.ObserveUiEvent
import com.uae.core_common.utils.localeUtils.get_Formatted_UTC_Time
import com.uae.feature_chat_with_us.remote.model.response.ChatSupportTicketsListResponse
import com.uae.feature_chat_with_us.ui.states.ChatWithUsStates
import com.uae.feature_chat_with_us.ui.utils.ChatSupportType
import com.uae.feature_chat_with_us.ui.utils.ChatType
import com.uae.feature_chat_with_us.ui.viewmodels.ChatWithUsViewModel
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.zodiaq.ui.theme.Shape_15


@Composable
fun ChatWithUsScreen(
    chatWithUsViewModel: ChatWithUsViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current
) {
    val uiState by chatWithUsViewModel.uiState.collectAsStateWithLifecycle()

    val assistanceStatusTabs =
        ChatSupportType.entries

    val selectedChatType by chatWithUsViewModel.selectedChatType.collectAsStateWithLifecycle()


    ObserveUiEvent(chatWithUsViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUiEvent.NavigateTo -> {
                backStack.add(uIEvent.routeNavKey)
            }

            is CommonUiEvent.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }

        }
    }
    BoxCommon(
        modifier = Modifier,
        isAppearanceLightStatusBars = false,
        isAppearanceLightNavigationBars = false
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .navigationBarsPadding()
        ) {

            HeaderView(title = "Chat With Us", onBack = {
                backStack.removeLastOrNull()
            })

            AnimatedTabs(
                tabs = assistanceStatusTabs,
                selectedTabIndex = selectedChatType ?: ChatSupportType.default().type,
                labelMapper = {
                    when (it) {
                        ChatSupportType.OPEN -> "Opened"
                        ChatSupportType.COMPLETED -> "Completed"
                        else -> ""
                    }
                },
                onSelect = { assistanceType, y ->
                    chatWithUsViewModel.updateType(assistanceType.type)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp),
            )


            AnimatedContent(
                selectedChatType,
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
            ) { selectedChatType ->
                ChatSupportsLazyList(
                    chatSupportViewModel = chatWithUsViewModel,
                    uiState = uiState,
                    supportType = selectedChatType
                )
            }

            GradientButton(onClick = {
                chatWithUsViewModel.onEvent(CommonUiEvent.NavigateTo(ChatWithUsScreens.CreateTickerScreen))
            },
                text = "Create New Ticket",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp, vertical = 5.dp))

        }

    }
}


@Composable
fun ChatSupportsLazyList(
    chatSupportViewModel: ChatWithUsViewModel,
    uiState: ChatWithUsStates?,
    supportType: Int
) {

    val pagingData = if (supportType == ChatSupportType.OPEN.type) {
        chatSupportViewModel.openedListPaging
    } else {
        chatSupportViewModel.closedListPaging
    }.collectAsLazyPagingItems()


    LaunchedEffect(Unit) {
        pagingData.refresh()
    }

    LaunchedEffect(pagingData.loadState.refresh) {
        if (pagingData.loadState.refresh is LoadState.NotLoading) {

            chatSupportViewModel.updateState {
                if (supportType == ChatSupportType.OPEN.type) {
                    it?.copy(isOpenedListRefreshing = false)
                } else {
                    it?.copy(isCompletedListRefreshing = false)
                }

            }
        }
        if (pagingData.loadState.refresh is LoadState.Error) {
            (pagingData.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                chatSupportViewModel.updateState {
                    if (supportType == ChatSupportType.OPEN.type) {
                        it?.copy(isOpenedListRefreshing = false)
                    } else {
                        it?.copy(isCompletedListRefreshing = false)
                    }
                }
            }
        }
        if (pagingData.loadState.hasError) {
            (pagingData.loadState.refresh as LoadState.Error).error.let {
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }



    BoxCommon(
        isLoading = pagingData.loadState.refresh is LoadState.Loading && if (supportType == ChatSupportType.OPEN.type) {
            uiState?.isOpenedListRefreshing == false
        } else {
            uiState?.isCompletedListRefreshing == false
        }
    ) {
        PullToRefreshBox(
            isRefreshing = if (supportType == ChatSupportType.OPEN.type) {
                uiState?.isOpenedListRefreshing ?: false
            } else {
                uiState?.isCompletedListRefreshing ?: false
            },
            onRefresh = {
                chatSupportViewModel.updateState {
                    if (supportType == ChatSupportType.OPEN.type) {
                        it?.copy(isOpenedListRefreshing = true)
                    } else {
                        it?.copy(isCompletedListRefreshing = true)
                    }
                }
                pagingData.refresh()
            },
            modifier = Modifier
                .fillMaxSize()
        )
        {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            )
            {

                items(pagingData.itemCount) { index ->
                    val data = pagingData.get(index)

                    ChatSupportItemCard(modifier = Modifier.then(if(supportType == ChatSupportType.OPEN.type){
                        Modifier.clickableDebAnim{
                            chatSupportViewModel.onEvent(CommonUiEvent.NavigateTo(ChatWithUsScreens.ChatScreen(id = data?.id, chatType = ChatType.SUPPORT.value)))
                        }
                    }else{
                        Modifier
                    }), data)
//                    CompletedAssistanceItem(assistanceData, onRateUs = {
//                        assistanceData?.id?.let {
//                            chatSupportViewModel.onEvent(
//                                CommonUiEvent.NavigateTo(
//                                    routeNavKey = HomeScreens.RateUsScreen(assistanceData.id)
//                                )
//                            )
//                        }
//                    })
                }
                item {
                    bottomPagingLoader(
                        isVisible = pagingData.loadState.append is LoadState.Loading || pagingData.loadState.append is LoadState.Error,
                        isRetry = pagingData.loadState.append is LoadState.Error,
                        onRetry = {
                            pagingData.retry()
                        })
                }

                item {
                    if (pagingData.itemCount == 0) {
                        if (pagingData.loadState.refresh is LoadState.NotLoading) {
                            emptyView(
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(50.dp))
                }


            }
        }
    }
}


@Composable
fun ChatSupportItemCard(
    modifier: Modifier = Modifier,
    data: ChatSupportTicketsListResponse.ChatSupportData?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shadow = Shadow(radius = 10.dp, color = Color.Black.copy(0.08f)),
                shape = Shape_15
            )
            .background(color = Color.White, Shape_15)
            .padding(15.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                data?.title?: "",
                modifier = Modifier.weight(1f),
                color = Color.Black,
                fontFamily = POPPINS_MEDIUM
            )
            Text(data?.ticketId ?: "", color = grey_4, fontSize = 12.sp)
        }


        Text(
            data?.description ?: "",
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 13.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                data?.createdAt?.get_Formatted_UTC_Time(toFormat = DateFormats.DATE_FORMAT_27) ?: "",
                modifier = Modifier.weight(1f),
                color = Color.Black,
                fontSize = 12.sp
            )
            Image(painter = painterResource(R.drawable.baseline_arrow_forward_ios_24), contentDescription = null,modifier = Modifier.size(10.dp))
        }
    }
}