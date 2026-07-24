package com.uae.feature_home.ui.pagination

import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_home.domain.HomeRepository
import com.uae.feature_home.remote.model.response.CategoryListingResponse
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first

class CategoriesListDataSource(
    private val search : String? = null,
    private val active : Int? = null,
    private val homeRepository : HomeRepository,
) : PagingSource<Int, CategoryListingResponse.CategoryData>() {

    override fun getRefreshKey(state: PagingState<Int, CategoryListingResponse.CategoryData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryListingResponse.CategoryData> {
        val nextPage = params.key ?: 1

        val result = homeRepository.getCategoryListing(
            page = nextPage, limit = params.loadSize,
            search = search, active = active)
            .first()

        return when(result){
            is NetworkResult.Success -> {
                val categoriesList = result.data?.data ?: emptyList()
                val isLastPage = categoriesList.isEmpty() ?: false

                LoadResult.Page(
                    data = categoriesList,
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