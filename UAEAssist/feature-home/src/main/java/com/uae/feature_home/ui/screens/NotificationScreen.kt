package com.uae.feature_home.ui.screens

import com.uae.core_common.CommonUiEvent
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.HeaderView
import com.uae.core_common.components.bottomPagingLoader
import com.uae.core_common.components.emptyView
import com.uae.core_common.extenstions.showSnackBarWithDismiss
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.core_common.theme.POPPINS_MEDIUM
import com.uae.core_common.theme.green_4
import com.uae.core_common.theme.grey_5
import com.uae.core_common.utils.DateFormats
import com.uae.core_common.utils.ObserveUiEvent
import com.uae.core_common.utils.localeUtils.get_Formatted_UTC_Time
import com.uae.feature_home.remote.model.response.NotificationListResponse
import com.uae.feature_home.ui.viewmodel.NotificationViewModel
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
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
fun NotificationScreen(
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    notificationViewModel: NotificationViewModel = hiltViewModel(),
                       snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current
) {

    val uiState by notificationViewModel.uiState.collectAsStateWithLifecycle()

    val notificationListPaging = notificationViewModel.notificationListPaging.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        notificationListPaging.refresh()
    }

    LaunchedEffect(notificationListPaging.loadState.refresh) {
        if (notificationListPaging.loadState.refresh is LoadState.NotLoading) {
            notificationViewModel.updateState { it?.copy(isRefreshing = false) }
        }
        if (notificationListPaging.loadState.refresh is LoadState.Error) {
            (notificationListPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                notificationViewModel.updateState { it?.copy(isRefreshing = false) }
            }
        }
        if (notificationListPaging.loadState.hasError) {
            (notificationListPaging.loadState.refresh as LoadState.Error).error.let {
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }
    ObserveUiEvent(notificationViewModel.uiEvent) {uIEvent ->
        when(uIEvent){
            is CommonUiEvent.NavigateTo -> {
//                backStack.add(uIEvent.routeNavKey)
            }
            is CommonUiEvent.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }
        }
    }
    BoxCommon(isLoading = notificationListPaging.loadState.refresh is LoadState.Loading && uiState?.isRefreshing == false,
        isAppearanceLightStatusBars = false,
        isAppearanceLightNavigationBars = false){
        Column(modifier = Modifier.fillMaxSize()
            .navigationBarsPadding()) {

            HeaderView(title = "Notifications", onBack = {
                backStack.removeLastOrNull()
            })

            PullToRefreshBox(
                isRefreshing = (uiState?.isRefreshing ?: false),
                onRefresh = {
                    notificationViewModel.updateState {
                        it?.copy(isRefreshing = true)
                    }
                    notificationListPaging.refresh()
                },
                modifier = Modifier
                    .fillMaxSize()
            )
            {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize(),
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                )
                {

                    items(notificationListPaging.itemCount) { index ->
                        val data = notificationListPaging.get(index)
                        NotificationCard(data, modifier = Modifier.padding(horizontal = 20.dp) )
                    }
                    item {
                        bottomPagingLoader(
                            isVisible = notificationListPaging.loadState.append is LoadState.Loading || notificationListPaging.loadState.append is LoadState.Error,
                            isRetry = notificationListPaging.loadState.append is LoadState.Error,
                            onRetry = {
                                notificationListPaging.retry()
                            })
                    }

                    item {
                        if (notificationListPaging.itemCount == 0) {
                            if (notificationListPaging.loadState.refresh is LoadState.NotLoading) {
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

}


@Composable

fun NotificationCard(data: NotificationListResponse.NotificationData?, modifier: Modifier = Modifier){


    Column(
        modifier = modifier.fillMaxWidth()
            .dropShadow(shadow = androidx.compose.ui.graphics.shadow.Shadow(color = Color.Black.copy(0.1f), radius = 10.dp), shape = Shape_15)
            .clip(Shape_15)
            .background(color = Color.White, shape = Shape_15)
            .padding(15.dp)) {

        Row(modifier = Modifier.fillMaxWidth())  {
            Text(data?.title ?: "", fontFamily = POPPINS_MEDIUM,modifier = Modifier.weight(1f))
            if(data?.isRead == false){
                Box(modifier = Modifier.background(shape = CircleShape, color = green_4).size(6.dp))
            }
        }
        Text(data?.description ?: "",modifier = Modifier.padding(top = 7.dp))
        Text(data?.createdAt?.get_Formatted_UTC_Time(toFormat = DateFormats.DATE_FORMAT_27) ?: "", color = grey_5, fontSize = 12.sp,modifier = Modifier.padding(top = 10.dp))


    }

}