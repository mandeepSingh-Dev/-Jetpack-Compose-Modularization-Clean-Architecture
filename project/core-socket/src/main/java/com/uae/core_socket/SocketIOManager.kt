// SocketIOManager.kt
package com.uae.core_socket

import com.uae.core_network.BuildConfig
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException
import kotlin.random.Random


object SocketIOManager {

//  val SOCKET_BASE_URL = "https://appsstart.com:5100"
  val SOCKET_BASE_URL = "https://api.myzodiaq.in"
    const val TRACK_ASSISTANCE = "trackAssistance"
    const val TRACK_STAFF = "track_staff"
    const val STAFF_ARRIVED_AT_LOCATION = "staff_arrived_at_location"
    const val STAFF_ARRIVED_AT_LOCATION_CONFIRMATION = "staff_arrived_at_location_confirmation"
    const val ASSISTANCE_RESOLVED = "assistance_resolved"
    const val STAFF_LOCATION_LIVE = "staff_location_live"

  sealed class SocketEvent() {
    object Connected : SocketEvent()
    object Disconnected : SocketEvent()
    data class TrackAssistance(val data : Any?) : SocketEvent()
    data class StaffArrivedAtLocation(val data : Any?) : SocketEvent()
    data class AssistanceResolved(val data : Any?) : SocketEvent()
    data class StaffLocationLive(val data : Any?) : SocketEvent()
    data class Error(val throwable: Throwable) : SocketEvent()
  }

  // coroutine scope owned by this manager; recreated in clear()
  private var scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

  private val socketMutex = Mutex()
   var socket: Socket? = null

  private val _events = MutableSharedFlow<SocketEvent>(extraBufferCapacity = 64)
  val events: SharedFlow<SocketEvent> = _events

  // reconnection control
  private var manualDisconnect = false
  private var reconnectAttempts = 0

  private fun createOptions(authToken: String?): IO.Options {
    return IO.Options().apply {
      auth = authToken?.let { mapOf("authorization" to it) }
      path = "/socket.io"
      reconnection = true // we control reconnection logic
    }
  }
  suspend fun connect(authToken: String? = null, onConnected : () -> Unit = {}) {
    socketMutex.withLock {

      Log.d("fbjfnbjf", socket?.connected().toString() + " Socket Connected")
      if (socket != null) {
        return
      }
      manualDisconnect = false
      reconnectAttempts = 0

      try {
        val opts = createOptions(authToken)
        val s = IO.socket(BuildConfig.SOCKET_BASE_URL, opts)
        socket = s

        // register listeners
        s.on(Socket.EVENT_CONNECT, onConnect)
        s.on(Socket.EVENT_DISCONNECT, onDisconnect)
        s.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        s.on(TRACK_ASSISTANCE,onTrackAssistance)
        s.on(TRACK_STAFF,onTrackStaff)
        s.on(STAFF_ARRIVED_AT_LOCATION,onStaffArrivedAtLocation)
        s.on(ASSISTANCE_RESOLVED,onAssistanceResolved)
        s.on(STAFF_LOCATION_LIVE,onStartLocationLive)

        s.on(Socket.EVENT_CONNECT_ERROR,{
          Log.d("kfbnkfnbf", it.firstOrNull().toString())
        })

         val socket  = s.connect()
        if(socket.connected()){
          onConnected()
        }

      } catch (e: URISyntaxException) {
        Log.d("fkbnkfnbf", e.message.toString())
        scope.launch { _events.emit(SocketEvent.Error(e)) }
      }catch (e: Exception){
        Log.d("fkbfkbnf", e.message.toString())
      }
    }
  }

  private val onConnect = Emitter.Listener {
    scope.launch {
      Log.d("rkbnjkjnbvf", "Connected")
      reconnectAttempts = 0
      _events.emit(SocketEvent.Connected)
    }
  }
  private val onTrackAssistance = Emitter.Listener {
    scope.launch {
        Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onTrackAssistance")
      reconnectAttempts = 0
      _events.emit(SocketEvent.TrackAssistance(data = it.firstOrNull()))
    }
  }

  private val onTrackStaff = Emitter.Listener {
    scope.launch {
        Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onTrackStaff")
      reconnectAttempts = 0
      _events.emit(SocketEvent.TrackAssistance(data = it.firstOrNull()))
    }
  }
  private val onStaffArrivedAtLocation = Emitter.Listener {
    scope.launch {
        Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onStaffArrivedAtLocation")
      reconnectAttempts = 0
      _events.emit(SocketEvent.StaffArrivedAtLocation(data = it.firstOrNull()))
    }
  }
  private val onAssistanceResolved = Emitter.Listener {
    scope.launch {
        Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onAssistanceResolved")
      reconnectAttempts = 0
      _events.emit(SocketEvent.AssistanceResolved(data = it.firstOrNull()))
    }
  }
  private val onStartLocationLive = Emitter.Listener {
    scope.launch {
        Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onStartLocationLive")
      reconnectAttempts = 0
      _events.emit(SocketEvent.StaffLocationLive(data = it.firstOrNull()))
    }
  }

  private val onDisconnect = Emitter.Listener { args ->
    scope.launch {
      Log.d("rkbnjkjnbvf", "oNdISCONNECTED")
      _events.emit(SocketEvent.Disconnected)
      maybeReconnect()
    }
  }
  private val onConnectError = Emitter.Listener { args ->
    scope.launch {
      val err = args?.firstOrNull() as? Throwable ?: Exception("Unknown socket error: ${args?.joinToString()}")
      _events.emit(SocketEvent.Error(err))

      val error = args.firstOrNull()
      Log.e("Socket43433d3", "Connect Error: $error")
      if (error is Exception) {
        error.printStackTrace()
      }

      maybeReconnect()
    }
  }

  fun addEvent(event : String, listener : Emitter.Listener){
    scope.launch {
      socket?.on(event,listener)
    }
  }

  // Emit message
  fun emit(event: String, vararg args: Any): Boolean {
    val s = socket ?: return false
    return try {
      s.emit(event, *args)
      true
    } catch (t: Throwable) {
      scope.launch { _events.emit(SocketEvent.Error(t)) }
      false
    }
  }

  // Disconnect (manual indicates user-initiated)
  fun disconnect(manual: Boolean = true) {
    scope.launch {
      socketMutex.withLock {
        manualDisconnect = manual
        socket?.off()
        socket?.disconnect()
        socket?.close()
        socket = null
      }
    }
  }

  // Complete cleanup: cancel coroutines, remove refs
  fun clear() {
    // mark manual to avoid reconnect attempts
    manualDisconnect = true

    // cancel ongoing tasks and recreate scope
    scope.cancel()
    scope = SupervisorJob().let { CoroutineScope(it + Dispatchers.IO) }

    // disconnect socket and null
    runBlocking {
      socketMutex.withLock {
        socket?.let { s ->
          s.off()
          s.disconnect()
          s.close()
        }
        socket = null
      }
    }
    reconnectAttempts = 0
  }

  // Reconnect strategy controlled by us; prevents infinite tight reconnects
  private fun maybeReconnect() {
    scope.launch {
      if (manualDisconnect) return@launch

      reconnectAttempts++
      val max = 10
      if (reconnectAttempts > max) {
        _events.emit(SocketEvent.Error(Exception("Max reconnect attempts reached")))
        return@launch
      }
      val base = 1000L
      val backoff = base * (1L shl reconnectAttempts.coerceAtMost(6))
      val jitter = Random.nextLong(0, base)
      val delayMs = (backoff + jitter).coerceAtMost(60_000L)

      delay(delayMs)

      socketMutex.withLock {
        if (socket == null && !manualDisconnect) {
          // Try reconnect. If token-refresh needed, caller should call connect(newToken)
          try {
            val s = IO.socket(SOCKET_BASE_URL, createOptions(null)) // pass fresh token if available
            socket = s
            s.on(Socket.EVENT_CONNECT, onConnect)
            s.on(Socket.EVENT_DISCONNECT, onDisconnect)
            s.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
            s.connect()
          } catch (e: Exception) {
            _events.emit(SocketEvent.Error(e))
            maybeReconnect()
          }
        }
      }
    }
  }
}
