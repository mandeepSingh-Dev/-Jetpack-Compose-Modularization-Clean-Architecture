package com.uae.feature_home.ui.viewmodel

import com.uae.core.navigation.HomeScreens
import com.uae.core_common.BaseViewModel
import com.uae.core_common.CommonUiEvent
import com.uae.core_common.UserManager
import com.uae.core_common.extenstions.openDialPad
import com.uae.core_common.extenstions.openUrl
import com.uae.core_common.utils.fromJson
import com.uae.core_common.utils.toJson
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_socket.SocketIOManager
import com.uae.feature_home.domain.HomeRepository
import com.uae.feature_home.domain.usecase.HomeAllUseCases
import com.uae.feature_home.remote.model.response.CategoryListingResponse
import com.uae.feature_home.ui.events.HomeScreenEvent
import com.uae.feature_home.ui.pagination.CategoriesListDataSource
import com.uae.feature_home.ui.state.HomeScreenState
import com.uae.feature_home.utils.ActionType
import com.uae.feature_profile.domain.usecase.ProfileAllUseCases
import com.uae.feature_profile.remote.model.response.ProfileResponse
import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.firebase.messaging.FirebaseMessaging
import com.uae.core_network.data.model.TrackAssistanceData
import com.uae.core_network.data.model.requestBody.AssistanceClickRequestBody
import com.uae.core_network.domain.AssistanceAllUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val homeRepository: HomeRepository,
    private val profileAllUseCases: ProfileAllUseCases,
    private val homeAllUseCases: HomeAllUseCases,
    private val commonAllUseCases: CommonAllUseCases,
    private val assistanceAllUseCases: AssistanceAllUseCases,
    private val userManager: UserManager
) : BaseViewModel<HomeScreenState>(HomeScreenState()) {

    private val _categoriesPagingData =
        MutableStateFlow(PagingData.empty<CategoryListingResponse.CategoryData>())
    val categoriesPagingData = _categoriesPagingData.asStateFlow()

    init {
        updateFcmToken()
        getCategoriesList()
        getUserData()
        startSocket()
    }


    fun updateFcmToken(){
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            commonAllUseCases.updateFcmTokenUseCase(fcmToken = it).launchIn(viewModelScope)
        }
    }
    fun startSocket(){
        viewModelScope.launch {
            SocketIOManager.connect(authToken = userManager.getUserToken())


            SocketIOManager.events.collectLatest { socketEvent ->
                when(socketEvent){
                    is SocketIOManager.SocketEvent.TrackAssistance -> {
                        val data = socketEvent.data.toString().fromJson<TrackAssistanceData>()
                            updateState { state ->
                                state?.copy(trackAssistanceData = if(!data?.requestId.isNullOrEmpty()) data else null)
                            }
                    }
                    else -> Unit
                }
            }
        }
    }
    fun getCategoriesList() {
        Pager(
            config = PagingConfig(
                initialLoadSize = 10,
                pageSize = 10,
            ),
            pagingSourceFactory = {
                CategoriesListDataSource(
                    active = 1,
                    homeRepository = homeRepository
                )
            }
        ).flow.cachedIn(viewModelScope).onEach {
            _categoriesPagingData.value = it
        }.launchIn(viewModelScope)

    }

    fun getUserData() = viewModelScope.launch {
        val userData = userManager.getUserDataString().fromJson<ProfileResponse.UserData>()
        Log.d("fkbnjkfnbf", userData.toString())
        updateState { state ->
            state?.copy(
                userData = userData
            )
        }
        profileAllUseCases.getProfileUseCase().onEach { networkResult ->
            when (networkResult) {
                is NetworkResult.Success -> {
                    updateState { state ->
                        state?.copy(
                            userData = networkResult.data?.data
                        )
                    }
                }

                else -> Unit
            }
        }.launchIn(this)
    }

    fun performCategoryAction(categoryData: CategoryListingResponse.CategoryData?){

        val action = categoryData?.action

        val assistanceClickRequestBody = AssistanceClickRequestBody(
            category = categoryData?.id,
            type = categoryData?.actionItem?.type
        )

        assistanceAllUseCases.assistanceClickUseCase(assistanceClickRequestBody = assistanceClickRequestBody).launchIn(CoroutineScope(Dispatchers.IO))

        if (action == false) {
            categoryData.id?.let { categoryId ->
                val subCategoriesJson = categoryData.subCategories?.take(5).toJson()
                onEvent(
                    CommonUiEvent.NavigateTo(
                        HomeScreens.SubCategoriesScreen(
                            categoryId = categoryId,
                            categoryName = categoryData.name ?: "",
                            subCategoriesListData = subCategoriesJson
                        )
                    )
                )
            }
        } else {
            val actionItem = categoryData?.actionItem
            val actionType = categoryData?.actionItem?.type
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

    fun logout(){
        commonAllUseCases.logoutUseCase().onEach { networkResult ->
            when(networkResult){
                is NetworkResult.Loading -> {
                    updateState { state -> state?.copy(isLoading = true) }
                }
                is NetworkResult.Success -> {
                    updateState { state -> state?.copy(isLoading = false) }
                    onEvent(HomeScreenEvent.LogoutSuccess)
                }
                is NetworkResult.Error -> {
                    updateState { state -> state?.copy(isLoading = false) }
                }
            }
        }.launchIn(viewModelScope)
    }


    fun getLastAcceptedAssistance(){
        assistanceAllUseCases.getLastAcceptedUseCase().onEach { networkResult ->
            when(networkResult){
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    val assistanceData = networkResult.data?.data
                    updateState { state ->
                        state?.copy(trackAssistanceData = if(!assistanceData?.requestId.isNullOrEmpty()) assistanceData else null)
                    }
                    Log.d("fkbjkfnbf",uiState.value?.trackAssistanceData.toString())

                }
                is NetworkResult.Error -> {
                    Log.d("fkbjkfnbf",networkResult.error.toString())
                    updateState { state ->
                        state?.copy(trackAssistanceData = null)
                    }
                }
            }

        }.launchIn(viewModelScope)
    }


}