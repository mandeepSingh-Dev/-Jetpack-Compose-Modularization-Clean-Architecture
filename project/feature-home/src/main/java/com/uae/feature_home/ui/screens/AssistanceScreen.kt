package com.uae.feature_home.ui.screens

import com.uae.core.navigation.HomeScreens
import com.uae.core_common.CommonUiEvent
import com.uae.core_common.components.AnimatedTabs
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.GradientButton
import com.uae.core_common.components.HeaderView
import com.uae.core_common.components.OutlineButton
import com.uae.core_common.components.bottomPagingLoader
import com.uae.core_common.components.emptyView
import com.uae.core_common.extenstions.showSnackBarWithDismiss
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.core_common.theme.grey_5
import com.uae.core_common.theme.theme_color_10
import com.uae.core_common.utils.DateFormats
import com.uae.core_common.utils.ObserveUiEvent
import com.uae.core_common.utils.localeUtils.get_Formatted_UTC_Time
import com.uae.feature_home.ui.state.AssistanceScreenState
import com.uae.feature_home.ui.viewmodel.AssistanceViewModel
import com.uae.feature_home.utils.AssistanceType
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.uae.core.navigation.ChatWithUsScreens
import com.uae.core_network.data.model.AssistanceListResponse
import com.zodiaq.ui.theme.Shape_15


@Composable
fun AssistanceScreen(
    assistanceViewModel: AssistanceViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current
) {
    val uiState by assistanceViewModel.uiState.collectAsStateWithLifecycle()

    val assistanceStatusTabs =
        AssistanceType.entries.filter { it.status != AssistanceType.CANCELLED.status }

    val selectedAssistanceType by assistanceViewModel.assistanceType.collectAsStateWithLifecycle()


    ObserveUiEvent(assistanceViewModel.uiEvent) { uIEvent ->
        when(uIEvent){
            is CommonUiEvent.NavigateTo -> {
                backStack.add(uIEvent.routeNavKey)
            }
            is CommonUiEvent.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }

        }
    }

    BoxCommon(
        modifier = Modifier.navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            HeaderView(title = "My Assistance", onBack = {
                backStack.removeLastOrNull()
            })

            AnimatedTabs(
                tabs = assistanceStatusTabs,
                selectedTabIndex = selectedAssistanceType ?: AssistanceType.default().status,
                labelMapper = {
                    when (it) {
                        AssistanceType.PENDING -> "Pending"
                        AssistanceType.ACCEPTED -> "In Progress"
                        AssistanceType.RESOLVED -> "Completed"
                        else -> ""
                    }
                },
                onSelect = { assistanceType, y ->
                    assistanceViewModel.updateAssistanceType(assistanceType.status)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp),
            )


            AnimatedContent(
                selectedAssistanceType,
                modifier = Modifier.fillMaxSize()
            ) { selectedAssistanceType ->
                when (selectedAssistanceType) {
                    AssistanceType.PENDING.status -> {
                        PendingAssistanceList(assistanceViewModel, uiState)
                    }

                    AssistanceType.ACCEPTED.status -> {
                        AcceptedAssistanceList(assistanceViewModel, uiState)
                    }

                    AssistanceType.RESOLVED.status -> {
                        CompletedAssistanceList(assistanceViewModel, uiState)
                    }
                }
            }
        }
    }
}


@Composable
fun PendingAssistanceList(
    assistanceViewModel: AssistanceViewModel,
    uiState: AssistanceScreenState?
) {

    Log.d("kfnbkfnkb", "Pemnfing")

    val assistanceListPaging = assistanceViewModel.pendingAssistancePagingData2.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        assistanceListPaging.refresh()
    }


    LaunchedEffect(assistanceListPaging.loadState.refresh) {
        if (assistanceListPaging.loadState.refresh is LoadState.NotLoading) {
            assistanceViewModel.updateState { it?.copy(isPendingListRefreshing = false) }
        }
        if (assistanceListPaging.loadState.refresh is LoadState.Error) {
            (assistanceListPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                assistanceViewModel.updateState { it?.copy(isPendingListRefreshing = false) }
            }
        }
        if (assistanceListPaging.loadState.hasError) {
            (assistanceListPaging.loadState.refresh as LoadState.Error).error.let {
                Log.d("fkbnfbnf", it.message.toString())
            }
        }
    }

    BoxCommon(isLoading = assistanceListPaging.loadState.refresh is LoadState.Loading && uiState?.isPendingListRefreshing == false) {
        PullToRefreshBox(
            isRefreshing = (uiState?.isPendingListRefreshing ?: false),
            onRefresh = {
                assistanceViewModel.updateState {
                    it?.copy(isPendingListRefreshing = true)
                }
                assistanceListPaging.refresh()
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
                verticalArrangement = Arrangement.spacedBy(10.dp),
            )
            {

                items(assistanceListPaging.itemCount) { index ->
                    val assistanceData = assistanceListPaging.get(index)
                    PendingAssistanceItem()
                }
                item {
                    bottomPagingLoader(
                        isVisible = assistanceListPaging.loadState.append is LoadState.Loading || assistanceListPaging.loadState.append is LoadState.Error,
                        isRetry = assistanceListPaging.loadState.append is LoadState.Error,
                        onRetry = {
                            assistanceListPaging.retry()
                        })
                }

                item {
                    if (assistanceListPaging.itemCount == 0) {
                        if (assistanceListPaging.loadState.refresh is LoadState.NotLoading) {
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
fun AcceptedAssistanceList(
    assistanceViewModel: AssistanceViewModel,
    uiState: AssistanceScreenState?
) {

    val assistanceListPaging =
        assistanceViewModel.acceptedAssistancePagingData2.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        assistanceListPaging.refresh()
    }


    LaunchedEffect(assistanceListPaging.loadState.refresh) {
        if (assistanceListPaging.loadState.refresh is LoadState.NotLoading) {
            assistanceViewModel.updateState { it?.copy(isAcceptedRefreshing = false) }
        }
        if (assistanceListPaging.loadState.refresh is LoadState.Error) {
            (assistanceListPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                assistanceViewModel.updateState { it?.copy(isAcceptedRefreshing = false) }
            }
        }
        if (assistanceListPaging.loadState.hasError) {
            (assistanceListPaging.loadState.refresh as LoadState.Error).error.let {
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }



    BoxCommon(isLoading = assistanceListPaging.loadState.refresh is LoadState.Loading && uiState?.isAcceptedRefreshing == false) {
        PullToRefreshBox(
            isRefreshing = (uiState?.isAcceptedRefreshing ?: false),
            onRefresh = {
                assistanceViewModel.updateState {
                    it?.copy(isAcceptedRefreshing = true)
                }
                assistanceListPaging.refresh()
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

                items(assistanceListPaging.itemCount) { index ->
                    val assistanceData = assistanceListPaging.get(index)
                    PendingAssistanceItem()
                }
                item {
                    bottomPagingLoader(
                        isVisible = assistanceListPaging.loadState.append is LoadState.Loading || assistanceListPaging.loadState.append is LoadState.Error,
                        isRetry = assistanceListPaging.loadState.append is LoadState.Error,
                        onRetry = {
                            assistanceListPaging.retry()
                        })
                }

                item {
                    if (assistanceListPaging.itemCount == 0) {
                        if (assistanceListPaging.loadState.refresh is LoadState.NotLoading) {
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
fun CompletedAssistanceList(
    assistanceViewModel: AssistanceViewModel,
    uiState: AssistanceScreenState?
) {


    val assistanceListPaging =
        assistanceViewModel.completedAssistancePagingData2.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        assistanceListPaging.refresh()
    }

    LaunchedEffect(assistanceListPaging.loadState.refresh) {
        if (assistanceListPaging.loadState.refresh is LoadState.NotLoading) {
            assistanceViewModel.updateState { it?.copy(isCompletedListRefreshing = false) }
        }
        if (assistanceListPaging.loadState.refresh is LoadState.Error) {
            (assistanceListPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                assistanceViewModel.updateState { it?.copy(isCompletedListRefreshing = false) }
            }
        }
        if (assistanceListPaging.loadState.hasError) {
            (assistanceListPaging.loadState.refresh as LoadState.Error).error.let {
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }


    BoxCommon(isLoading = assistanceListPaging.loadState.refresh is LoadState.Loading && uiState?.isCompletedListRefreshing == false) {
        PullToRefreshBox(
            isRefreshing = (uiState?.isCompletedListRefreshing ?: false),
            onRefresh = {
                assistanceViewModel.updateState {
                    it?.copy(isCompletedListRefreshing = true)
                }
                assistanceListPaging.refresh()
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

                items(assistanceListPaging.itemCount) { index ->
                    val assistanceData = assistanceListPaging.get(index)
                    CompletedAssistanceItem(assistanceData, onRateUs = {
                         assistanceData?.id?.let {assistanceId ->
                                    assistanceViewModel.onEvent(CommonUiEvent.NavigateTo(routeNavKey = HomeScreens.RateUsScreen(assistanceId))
                                    )
                        }
                    }, onSolution = {
                        assistanceViewModel.onEvent(CommonUiEvent.NavigateTo(ChatWithUsScreens.ChatScreen(id = assistanceData?.id, chatType = "assistance"  ))) //TODO LATER CHANGE WITH CONSTANT
                    })
                }
                item {
                    bottomPagingLoader(
                        isVisible = assistanceListPaging.loadState.append is LoadState.Loading || assistanceListPaging.loadState.append is LoadState.Error,
                        isRetry = assistanceListPaging.loadState.append is LoadState.Error,
                        onRetry = {
                            assistanceListPaging.retry()
                        })
                }

                item {
                    if (assistanceListPaging.itemCount == 0) {
                        if (assistanceListPaging.loadState.refresh is LoadState.NotLoading) {
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
@Preview
fun PendingAssistanceItem() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
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
                "20-July 2026 at 11:15 AM",
                modifier = Modifier.weight(1f),
                color = grey_5,
                fontSize = 13.sp
            )
            Text("AST-00171", color = grey_5, fontSize = 13.sp)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("City: ", color = grey_5)
            Text("Noida", color = Color.Black)
        }
        Text("#Emergency Lodging/Boarding", color = Color.Black, modifier = Modifier.fillMaxWidth())

        GradientButton(
            onClick = {},
            text = "Solutions",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
        )
    }
}

@Composable
fun CompletedAssistanceItem(
    assistanceData: AssistanceListResponse.AssistanceData?,
    onRateUs: () -> Unit,
    onSolution: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
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
                assistanceData?.acceptDeadline?.get_Formatted_UTC_Time(toFormat = DateFormats.DATE_FORMAT_27)
                    ?: "",
                modifier = Modifier.weight(1f),
                color = grey_5,
                fontSize = 13.sp
            )
            Text(assistanceData?.requestId ?: "", color = grey_5, fontSize = 13.sp)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("City: ", color = grey_5)
            Text(assistanceData?.city ?: "", color = Color.Black)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Assigned: ", color = grey_5)
            Text(
                "${assistanceData?.assignedStaff?.firstName ?: ""} ${assistanceData?.assignedStaff?.lastName ?: ""}",
                color = Color.Black
            )
        }
        Text(
            assistanceData?.category?.name ?: "",
            color = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlineButton(
                onClick = {
                    onRateUs()
                },
                text = "Rate Us",
                enabled = assistanceData?.isRating == false,
                isBorder = true,
                isGradient = false,
                bgColor = Color.White,
                fontColor = theme_color_10,
                modifier = Modifier.weight(1f)
            )
            GradientButton(
                onClick = onSolution,
                text = "Solutions",
                modifier = Modifier.weight(1f)
            )
        }
    }
}