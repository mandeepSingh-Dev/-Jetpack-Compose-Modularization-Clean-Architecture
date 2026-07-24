package com.uae.feature_home.ui.viewmodel

import com.uae.core_common.BaseViewModel
import com.uae.core_common.extenstions.openDialPad
import com.uae.core_common.extenstions.openUrl
import com.uae.feature_home.domain.HomeRepository
import com.uae.feature_home.domain.usecase.HomeAllUseCases
import com.uae.feature_home.remote.model.response.SubCategoriesListResponse
import com.uae.feature_home.ui.pagination.SubCategoriesListDataSource
import com.uae.feature_home.ui.state.SubCategoriesScreenState
import com.uae.feature_home.utils.ActionType
import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.uae.core_network.data.model.requestBody.AssistanceClickRequestBody
import com.uae.core_network.domain.AssistanceAllUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch


@HiltViewModel
class SubCategoriesViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val homeRepository: HomeRepository,
    private val homeAllUseCases : HomeAllUseCases,
    private val assistanceAllUseCases : AssistanceAllUseCases
) : BaseViewModel<SubCategoriesScreenState>(SubCategoriesScreenState()) {


    private val _subCategoriesPagingData = MutableStateFlow(PagingData.empty<SubCategoriesListResponse.SubCategoryData>())
    val subCategoriesPagingData = _subCategoriesPagingData.asStateFlow()

    fun getSubCategoriesList(categoryId : String, subCategoriesList : List<SubCategoriesListResponse.SubCategoryData>?) {
        subCategoriesList?.let {
            _subCategoriesPagingData.value = PagingData.from(data = subCategoriesList)
        }
        viewModelScope.launch {
            delay(100)
            Pager(
                config = PagingConfig(
                    initialLoadSize = 10,
                    pageSize = 10,
                ),
                pagingSourceFactory = {
                    SubCategoriesListDataSource(
                        active = 1,
                        categoryId = categoryId,
                        homeRepository = homeRepository
                    )
                }
            ).flow.cachedIn(viewModelScope).collectLatest {
                _subCategoriesPagingData.value = it
            }
        }
    }

    fun performCategoryAction(subCategoryData: SubCategoriesListResponse.SubCategoryData?, categoryId: String,type : String?) {

        val action = subCategoryData?.action

        val assistanceClickRequestBody = AssistanceClickRequestBody(
            category = categoryId,
            subCategory = subCategoryData?.id,
            type = type
        )

        assistanceAllUseCases.assistanceClickUseCase(assistanceClickRequestBody = assistanceClickRequestBody)
            .launchIn(
                CoroutineScope(Dispatchers.IO)
            )

        if (action == false) {
        } else {
            val actionItem = subCategoryData?.actionItem
            val actionType = subCategoryData?.actionItem?.type
            if (actionType == ActionType.CALL.type) {
                actionItem.phone?.let {
                    openDialPad(context, phoneNumber = it)
                }
            } else {
                actionItem?.link?.let {
                    openUrl(context = context, url = it)
                }
            }
        }
    }

}



