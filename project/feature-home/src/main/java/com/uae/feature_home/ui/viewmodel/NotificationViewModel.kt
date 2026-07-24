package com.uae.feature_home.ui.viewmodel

import com.uae.core_common.BaseViewModel
import com.uae.feature_home.domain.HomeRepository
import com.uae.feature_home.ui.pagination.NotificationsListDataSource
import com.uae.feature_home.ui.state.NotificationScreenState
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class NotificationViewModel @Inject constructor(private val homeRepository: HomeRepository) : BaseViewModel<NotificationScreenState>(NotificationScreenState()){


    val notificationListPaging by lazy {
        Pager(
            config = PagingConfig(
                initialLoadSize = 10,
                pageSize = 10,
            ),
            pagingSourceFactory = {
                NotificationsListDataSource(
                    homeRepository = homeRepository
                )
            }
        ).flow.cachedIn(viewModelScope).stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }

}