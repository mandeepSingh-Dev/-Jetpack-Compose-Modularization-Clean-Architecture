package com.uae.feature_chat_with_us.ui.pagination

import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_chat_with_us.domain.ChatWithUsRepository
import com.uae.feature_chat_with_us.remote.model.response.ChatSupportTicketsListResponse
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first

class ChatSupportListDataSource(
    private val status : Int,
    private val chatWithUsRepository : ChatWithUsRepository,
) : PagingSource<Int, ChatSupportTicketsListResponse.ChatSupportData>() {

    override fun getRefreshKey(state: PagingState<Int, ChatSupportTicketsListResponse.ChatSupportData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,ChatSupportTicketsListResponse.ChatSupportData> {
        val nextPage = params.key ?: 1

        val result = chatWithUsRepository.getChatTickets(
            page = nextPage, limit = params.loadSize,
            status = status
        )
            .first()

        return when(result){
            is NetworkResult.Success -> {
                val list = result.data?.data ?: emptyList()
                val isLastPage = list.isEmpty() ?: false

                LoadResult.Page(
                    data = list,
                    prevKey = if(nextPage == 1) null else nextPage - 1,
                    nextKey = if(isLastPage) null else nextPage + 1
                )
            }
            is NetworkResult.Error -> {
                LoadResult.Error(Exception(result.error ?: "Unknown Error"))
            }
            else -> {
                LoadResult.Error(Exception("Unexpected result type"))
            }
        }
    }
}