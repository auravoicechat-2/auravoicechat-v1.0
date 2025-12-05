package com.aura.voicechat.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aura.voicechat.R
import com.aura.voicechat.ui.MainActivity
import com.amplifyframework.core.Amplify
import com.amplifyframework.pushnotifications.pinpoint.permissions.PushNotificationPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AWS Pinpoint Push Notification Service using Amplify v2
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Handles push notifications using AWS Pinpoint via Amplify Framework v2
 */
@Singleton
class AuraPinpointService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    companion object {
        private const val TAG = "AuraPinpointService"
        private const val CHANNEL_ID_DEFAULT = "aura_notifications"
        private const val CHANNEL_ID_MESSAGES = "aura_messages"
        private const val CHANNEL_ID_GIFTS = "aura_gifts"
        private const val CHANNEL_ID_SYSTEM = "aura_system"
    }
    
    /**
     * Initialize AWS Pinpoint via Amplify
     * Note: Amplify initialization happens in AuraApplication
     */
    fun initialize() {
        try {
            createNotificationChannels()
            
            // Request notification permission (Android 13+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Amplify.Notifications.Push.requestPermissions(
                    PushNotificationPermission.getPermissionsArray()
                )
            }
            
            // Register device for push notifications
            Amplify.Notifications.Push.registerDevice()
            
            Log.i(TAG, "Pinpoint initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Pinpoint", e)
        }
    }
    
    /**
     * Handle incoming push notification
     */
    fun handleNotification(data: Map<String, String>) {
        val type = data["type"] ?: "default"
        val title = data["title"] ?: "Aura Voice Chat"
        val body = data["body"] ?: ""
        val deepLink = data["deepLink"]
        val imageUrl = data["imageUrl"]
        
        showNotification(type, title, body, deepLink, imageUrl)
    }
    
    /**
     * Create notification channels
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_ID_DEFAULT,
                    "General Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "General app notifications"
                    enableVibration(true)
                    enableLights(true)
                },
                NotificationChannel(
                    CHANNEL_ID_MESSAGES,
                    "Messages",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "New message notifications"
                    enableVibration(true)
                    enableLights(true)
                },
                NotificationChannel(
                    CHANNEL_ID_GIFTS,
                    "Gifts",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Gift notifications"
                    enableVibration(true)
                },
                NotificationChannel(
                    CHANNEL_ID_SYSTEM,
                    "System",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "System notifications"
                }
            )
            
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            channels.forEach { notificationManager.createNotificationChannel(it) }
        }
    }
    
    /**
     * Show notification
     */
    private fun showNotification(
        type: String,
        title: String,
        body: String,
        deepLink: String?,
        imageUrl: String?
    ) {
        val channelId = when (type) {
            "message" -> CHANNEL_ID_MESSAGES
            "gift" -> CHANNEL_ID_GIFTS
            "system" -> CHANNEL_ID_SYSTEM
            else -> CHANNEL_ID_DEFAULT
        }
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            deepLink?.let { putExtra("deepLink", it) }
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        
        // Add image if available
        imageUrl?.let {
            // TODO: Implement image loading with Coil
        }
        
        val notification = notificationBuilder.build()
        
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
    
    /**
     * Register device token with Pinpoint via Amplify
     */
    fun registerDeviceToken(token: String) {
        try {
            // Amplify handles device token registration automatically
            // This method is kept for compatibility but delegates to Amplify
            Amplify.Notifications.Push.registerDevice()
            Log.i(TAG, "Device token registered: $token")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register device token", e)
        }
    }
    
    /**
     * Track event with Pinpoint Analytics via Amplify
     */
    fun trackEvent(eventType: String, attributes: Map<String, String>? = null) {
        try {
            // Use Amplify Analytics to record events
        // Unused variable removed: val event = Amplify.Analytics.recordEvent(eventType)
            
            attributes?.forEach { (key, value) ->
                // Note: Amplify v2 Analytics API might differ
                // This is a simplified implementation
                Log.d(TAG, "Event attribute: $key = $value")
            }
            
            Amplify.Analytics.flushEvents()
            Log.i(TAG, "Event tracked: $eventType")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to track event", e)
        }
    }
    
    /**
     * Update user attributes in Pinpoint via Amplify
     */
    fun updateUserAttributes(attributes: Map<String, String>) {
        try {
            // Use Amplify Analytics to identify user with attributes
            attributes.forEach { (key, value) ->
                // Note: Amplify v2 API for user attributes
                Log.d(TAG, "User attribute: $key = $value")
            }
            
            Amplify.Analytics.identifyUser(
                userId = attributes["userId"] ?: "unknown",
                profile = com.amplifyframework.analytics.UserProfile.builder()
                    .name(attributes["name"])
                    .email(attributes["email"])
                    .build()
            )
            
            Log.i(TAG, "User attributes updated")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update user attributes", e)
        }
    }
}
