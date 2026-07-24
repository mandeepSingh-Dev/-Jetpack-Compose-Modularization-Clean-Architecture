package com.uae.feature_chat_with_us.ui.pagination

import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_chat_with_us.domain.ChatWithUsRepository
import com.uae.feature_chat_with_us.remote.model.response.ChatsListResponse
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first

class ChatsListDataSource(
    private val id : String,
    private val chatWithUsRepository : ChatWithUsRepository,
) : PagingSource<Int, ChatsListResponse.Data.MsgData>() {

    override fun getRefreshKey(state: PagingState<Int, ChatsListResponse.Data.MsgData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,ChatsListResponse.Data.MsgData> {
        val nextPage = params.key ?: 1

        val result = chatWithUsRepository.getChatsList(
            page = nextPage,
            limit = params.loadSize,
            id = id,
        )
            .first()

        Log.d("fkbnjfbnf", result.toString())

        return when(result){
            is NetworkResult.Success -> {
                val list = result.data?.data?.msgData ?: emptyList()
                val isLastPage = list.isEmpty() ?: false

                LoadResult.Page(
                    data = list.reversed(),
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