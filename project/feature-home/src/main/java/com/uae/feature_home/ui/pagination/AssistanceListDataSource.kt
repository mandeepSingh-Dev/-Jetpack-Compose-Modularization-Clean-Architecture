package com.uae.feature_home.ui.pagination

import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_home.domain.HomeRepository
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.uae.core_network.data.model.AssistanceListResponse
import com.uae.core_network.data.repositoryImpl.AssistanceRepositoryImpl
import com.uae.core_network.domain.AssistanceRepository
import kotlinx.coroutines.flow.first

class AssistanceListDataSource(
    private val search : String? = null,
    private val status : Int,
    private val assistanceRepository : AssistanceRepository,
) : PagingSource<Int, AssistanceListResponse.AssistanceData>() {

    override fun getRefreshKey(state: PagingState<Int, AssistanceListResponse.AssistanceData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,AssistanceListResponse.AssistanceData> {
        val nextPage = params.key ?: 1

        val result = assistanceRepository.getAssistanceListing(
            page = nextPage, limit = params.loadSize,
            search = search, status = status)
            .first()

        return when(result){
            is NetworkResult.Success -> {
                val assistanceList = result.data?.data ?: emptyList()
                val isLastPage = assistanceList.isEmpty() ?: false

                LoadResult.Page(
                    data = assistanceList,
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