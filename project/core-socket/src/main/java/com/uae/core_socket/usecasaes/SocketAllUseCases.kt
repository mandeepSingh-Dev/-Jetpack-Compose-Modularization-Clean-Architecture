package com.uae.core_socket.usecasaes

import com.uae.core_socket.SocketConstants
import com.uae.core_socket.SocketIOManager
import org.json.JSONObject
import javax.inject.Inject


data class SocketAllUseCases @Inject constructor(
    val socketLocationUpdateUseCase : SocketLocationUpdateUseCase
)

class SocketLocationUpdateUseCase @Inject constructor(){

     operator fun invoke(latitude : Double?, longitude : Double?) {
        val obj = JSONObject()
        obj.put(SocketConstants.LATITUDE, latitude)
        obj.put(SocketConstants.LONGITUDE, longitude)
        SocketIOManager.emit(event = "update_customer_location", obj)
    }
}