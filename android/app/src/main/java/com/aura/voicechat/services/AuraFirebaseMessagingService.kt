package com.aura.voicechat.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aura.voicechat.R
import com.aura.voicechat.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint

/**
 * Firebase Messaging Service for Push Notifications
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@AndroidEntryPoint
class AuraFirebaseMessagingService : FirebaseMessagingService() {
    
    companion object {
        private const val CHANNEL_ID_DEFAULT = "aura_notifications"
        private const val CHANNEL_ID_MESSAGES = "aura_messages"
        private const val CHANNEL_ID_GIFTS = "aura_gifts"
        private const val CHANNEL_ID_SYSTEM = "aura_system"
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send token to server
        // TODO: Implement token registration with backend
    }
    
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        message.data.let { data ->
            val type = data["type"] ?: "default"
            val title = data["title"] ?: "Aura Voice Chat"
            val body = data["body"] ?: ""
            val deepLink = data["deepLink"]
            
            showNotification(type, title, body, deepLink)
        }
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_ID_DEFAULT,
                    "General Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "General app notifications"
                },
                NotificationChannel(
                    CHANNEL_ID_MESSAGES,
                    "Messages",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "New message notifications"
                },
                NotificationChannel(
                    CHANNEL_ID_GIFTS,
                    "Gifts",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Gift notifications"
                },
                NotificationChannel(
                    CHANNEL_ID_SYSTEM,
                    "System",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "System notifications"
                }
            )
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            channels.forEach { notificationManager.createNotificationChannel(it) }
        }
    }
    
    private fun showNotification(type: String, title: String, body: String, deepLink: String?) {
        val channelId = when (type) {
            "message" -> CHANNEL_ID_MESSAGES
            "gift" -> CHANNEL_ID_GIFTS
            "system" -> CHANNEL_ID_SYSTEM
            else -> CHANNEL_ID_DEFAULT
        }
        
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            deepLink?.let { putExtra("deepLink", it) }
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
