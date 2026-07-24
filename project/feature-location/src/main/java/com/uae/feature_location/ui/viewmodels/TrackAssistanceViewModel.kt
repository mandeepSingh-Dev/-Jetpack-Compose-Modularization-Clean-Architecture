package com.uae.feature_location.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.uae.core_common.BaseViewModel
import com.uae.core_common.UserManager
import com.uae.core_location.utils.LocationMManager
import com.uae.core_network.data.model.TrackAssistanceData
import com.uae.core_network.domain.AssistanceAllUseCases
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_socket.SocketIOManager
import com.uae.feature_location.ui.TrackAssistanceScreenEvents
import com.uae.feature_location.ui.states.TrackAssistanceScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject


@HiltViewModel
class TrackAssistanceViewModel @Inject constructor(
    @ApplicationContext  val context : Context,
    private val userManager : UserManager,
    private val assistanceAllUseCases : AssistanceAllUseCases
): BaseViewModel<TrackAssistanceScreenState>(TrackAssistanceScreenState()) {


    val  locationManager : LocationMManager  by lazy {
        val locationMManager = LocationMManager(
            context = context,
            onLocationResultUpdate = {},
            onLastLocationResultUpdate = {})
        locationMManager.initLocationSDK()
        locationMManager
    }

    init {
        getCurrentLocation()
    }


    fun startSocket(staffId : String){
        viewModelScope.launch {
            SocketIOManager.connect(authToken = userManager.getUserToken(), onConnected = {

                val jsonObject = JSONObject()
                jsonObject.put("staffId", staffId)
                SocketIOManager.emit(SocketIOManager.TRACK_STAFF,jsonObject)
            })

            SocketIOManager.events.collectLatest { socketEvent ->
                when(socketEvent){
                    is SocketIOManager.SocketEvent.StaffArrivedAtLocation -> {
                        updateState { state ->
                            state?.copy(isStaffArrivedAtLocation = true)
                        }
                    }
                    is SocketIOManager.SocketEvent.AssistanceResolved -> {
                            onEvent(TrackAssistanceScreenEvents.AssistanceResolved)
                    }
                    is SocketIOManager.SocketEvent.StaffLocationLive -> {
                        Log.d("fkbnfkbnf","live lcoation ${socketEvent?.data}")
//                            onEvent(TrackAssistanceScreenEvents.AssistanceResolved)
                    }
                    else -> Unit
                }
                Log.d("kfnvbknvbf", socketEvent.toString())
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() = viewModelScope.launch{
        locationManager.getCurrentLocation(onLocation = {location ->
            updateState { state ->
                state?.copy(currentLatLng = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0))
            }
        }, onError = {
            Log.d("fkbnkfnbf", it.toString())
        })
    }


    fun confirmStaffArrival(data: TrackAssistanceData?){


        viewModelScope.launch {
            val requestId = data?.request?.id
            val customerId = data?.request?.user?.id
            val staffId = data?.staff?.id


            val jsonObject = JSONObject()
            jsonObject.put("requestId", requestId)
            jsonObject.put("customerId", customerId)
            jsonObject.put("staffId", staffId)

            SocketIOManager.emit(SocketIOManager.STAFF_ARRIVED_AT_LOCATION_CONFIRMATION, jsonObject)
            delay(300)
            getLastAcceptedAssistance()
        }

    }


    fun getLastAcceptedAssistance(){
        assistanceAllUseCases.getLastAcceptedUseCase().onEach { networkResult ->
            when(networkResult){
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    val assistanceData = networkResult.data?.data
                    updateState { state ->
                        state?.copy(
                            isStaffArrivedAtLocation = assistanceData?.request?.arrivalStatus == 1,
                            trackAssistanceData = if(!assistanceData?.requestId.isNullOrEmpty()) assistanceData else null
                        )
                    }
                }
                is NetworkResult.Error -> {
                    updateState { state ->
                        state?.copy(
                            trackAssistanceData = null
                        )
                    }
                }
            }

        }.launchIn(viewModelScope)
    }


}