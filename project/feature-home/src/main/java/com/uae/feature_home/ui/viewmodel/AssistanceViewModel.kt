package com.uae.feature_home.ui.viewmodel


import com.uae.core_common.BaseViewModel
import com.uae.core_common.extenstions.openDialPad
import com.uae.core_common.extenstions.openUrl
import com.uae.feature_home.domain.HomeRepository
import com.uae.feature_home.domain.usecase.HomeAllUseCases
import com.uae.feature_home.remote.model.response.SubCategoriesListResponse
import com.uae.feature_home.ui.pagination.AssistanceListDataSource
import com.uae.feature_home.ui.state.AssistanceScreenState
import com.uae.feature_home.utils.ActionType
import com.uae.feature_home.utils.AssistanceType
import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.uae.core_network.data.model.AssistanceListResponse
import com.uae.core_network.data.model.requestBody.AssistanceClickRequestBody
import com.uae.core_network.domain.AssistanceAllUseCases
import com.uae.core_network.domain.AssistanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn


@HiltViewModel
class AssistanceViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val assistanceRepository : AssistanceRepository,
    private val assistanceAllUseCases : AssistanceAllUseCases,
) : BaseViewModel<AssistanceScreenState>(AssistanceScreenState()) {


    private val _assistanceType = MutableStateFlow<Int?>(AssistanceType.PENDING.status)
    val assistanceType = _assistanceType.asStateFlow()

    val completedAssistancePagingData2 by lazy {
        getAssistance(AssistanceType.RESOLVED.status)
            .cachedIn(viewModelScope)
    }

    val pendingAssistancePagingData2 by lazy {
        getAssistance(AssistanceType.PENDING.status)
            .cachedIn(viewModelScope)
    }

    val acceptedAssistancePagingData2 by lazy {
        getAssistance(AssistanceType.ACCEPTED.status)
            .cachedIn(viewModelScope)
    }

    fun updateAssistanceType(status : Int){
        _assistanceType.value = status
    }

    fun getAssistance(status : Int): Flow<PagingData<AssistanceListResponse.AssistanceData>> {
Log.d("kfnbjknfb", status.toString())
       return Pager(
                config = PagingConfig(
                    initialLoadSize = 10,
                    pageSize = 10,
                ),
                pagingSourceFactory = {
                    AssistanceListDataSource(
                        status = status,
                        assistanceRepository = assistanceRepository
                    )
                }
            ).flow
    }

    fun performCategoryAction(subCategoryData: SubCategoriesListResponse.SubCategoryData?, categoryId: String,
                              type : String?) {

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
