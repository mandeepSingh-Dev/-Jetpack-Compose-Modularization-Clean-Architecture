package com.uae.assist.ui

import com.uae.core_location.LocationService
import com.uae.core_location.utils.LocationMManager
import com.uae.core_socket.SocketService
import android.app.Application
import android.content.Intent
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.initialize
import com.google.firebase.installations.installations
import com.uae.assist.services.createNotificationChannel
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltAndroidApp
class BaseApp : Application() {

    val coroutineScope = CoroutineScope(  Dispatchers.IO )

    override fun onCreate() {
        super.onCreate()
        startService(Intent(this, SocketService::class.java))
        FirebaseApp.initializeApp(this)
        createNotificationChannel()
        coroutineScope.launch {
            LocationMManager.startLocationService.collectLatest {shouldStart ->
                Log.d("fbnmkfnbf", shouldStart.toString())
                if(shouldStart){
                    startLocationService()
                }else{
                   stopLocationService()
                }
            }
        }

    }

    fun startLocationService(){
        val intent = Intent(this, LocationService::class.java)
        startService(intent)
    }
    fun stopLocationService(){
        val intent = Intent(this, LocationService::class.java)
        stopService(intent)
    }


}