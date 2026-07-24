package com.uae.assist.services

import android.Manifest
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage



import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.text.Html
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.core.graphics.drawable.IconCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.uae.assist.MainActivity
import com.uae.assist.R
import com.uae.assist.domain.NotificationPayload
import com.uae.core_common.utils.fromJson
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var commonAllUseCases: CommonAllUseCases

    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    var powerManager: PowerManager? = null

    fun getPowerManagerr(): PowerManager? {
        if (powerManager == null) {
            powerManager = getSystemService(POWER_SERVICE) as PowerManager
        }
        return powerManager
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("kfnvknvkfvf", message.notification?.body.toString())
        Log.d("kfnvknvkfvf", message.notification?.title.toString())
        Log.d("kfnvknvkfvf", "".toString())
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun handleIntent(intent: Intent?) {
//        super.handleIntent(intent)

        intent?.extras?.keySet()?.forEach {
            Log.d("handleIntent_payload_data", it.toString() + " ${intent.extras?.get(it)}")
        }

        val notificationDataStr = intent?.extras?.getString("data")
        val notificationData = notificationDataStr?.fromJson<NotificationPayload>()
        Log.d("fkvnfknvf", notificationDataStr.toString())


        showNotification(notificationData?.title ?: "", notificationData?.description ?: "")

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        commonAllUseCases.updateFcmTokenUseCase(token).launchIn(coroutineScope)
    }
}


fun Context.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val channel = NotificationChannel(
            NotificationConstants.CHAT_CHANNEL_ID,
            NotificationConstants.CHAT_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for chat messages"
            enableLights(true)
            enableVibration(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun Context.showNotification(
    title: String,
    message: String
) {

    val intent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

    val pendingIntent = PendingIntent.getActivity(
        this,
        100,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(
        this,
        NotificationConstants.CHAT_CHANNEL_ID
    )
        .setSmallIcon(com.uae.core_common.R.drawable.uae_logo) // Your notification icon
        .setContentTitle(title)
        .setContentText(message)
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build()

    NotificationManagerCompat.from(this)
        .notify(System.currentTimeMillis().toInt(), notification)
}

object NotificationConstants {
    const val CHAT_CHANNEL_ID = "chat_channel"
    const val CHAT_CHANNEL_NAME = "Chat Notifications"
}