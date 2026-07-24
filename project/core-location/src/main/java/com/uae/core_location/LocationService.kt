package com.uae.core_location

import com.uae.core_common.UserManager
import com.uae.core_location.utils.LocationMManager
import com.uae.core_socket.usecasaes.SocketAllUseCases
import com.uae.core_socket.SocketIOManager
import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LocationService @Inject constructor(): Service() {

    @Inject
    lateinit var userManager : UserManager

    @Inject
    lateinit var socketAllUseCases : SocketAllUseCases

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    var locationMManager : LocationMManager? = null
    override fun onCreate() {
        super.onCreate()
        locationMManager = LocationMManager(this,
            onLocationResultUpdate = {
                socketAllUseCases.socketLocationUpdateUseCase(
                    latitude = it?.latitude,
                    longitude = it?.longitude
                )
            },
            onLastLocationResultUpdate = {
                socketAllUseCases.socketLocationUpdateUseCase(
                    latitude = it?.latitude,
                    longitude = it?.longitude
                )
                Log.d("fkbnfjbnf", it?.longitude.toString() + " 2")
            })

//        socketIOManager = SocketIOManager(this)
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        locationMManager?.initLocationSDK(
            isLocationResultCallback = true,
            shouldFetchLocation = true,
        )

        coroutineScope.launch{
            SocketIOManager.connect(authToken = userManager.getUserToken())
            locationMManager?.getCurrentLocation(onLocation = {
                socketAllUseCases.socketLocationUpdateUseCase(
                    latitude = it?.latitude,
                    longitude = it?.longitude
                )
            }, onError = {
                Log.d("fkbmfbf", it)
            })
        }

        return START_NOT_STICKY
    }

}