package com.uae.feature_home.ui.pagination

import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_home.domain.HomeRepository
import com.uae.feature_home.remote.model.response.NotificationListResponse
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first

class NotificationsListDataSource(
    private val homeRepository : HomeRepository,
) : PagingSource<Int, NotificationListResponse.NotificationData>() {

    override fun getRefreshKey(state: PagingState<Int, NotificationListResponse.NotificationData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationListResponse.NotificationData> {
        val nextPage = params.key ?: 1

        val result = homeRepository.getNotifications(
            page = nextPage, limit = params.loadSize
        ).first()

        return when(result){
            is NetworkResult.Success -> {
                val notificationsList = result.data?.data ?: emptyList()
                val isLastPage = notificationsList.isEmpty() ?: false

                LoadResult.Page(
                    data = notificationsList,
                    prevKey = if(nextPage == 1) null else nextPage - 1,
                    nextKey = if(isLastPage) null else nextPage + 1
                )
            }
            is NetworkResult.Error -> {
                Log.d("fkbnfkbnf", result.error.toString())
                LoadResult.Error(Exception(result.error ?: "Unknown Error"))
            }
            else -> {
                LoadResult.Error(Exception("Unexpected result type"))
            }
        }
    }
}