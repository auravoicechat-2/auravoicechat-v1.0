package com.aura.voicechat

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.notifications.pushnotifications.PushNotificationsException
import com.amplifyframework.pushnotifications.pinpoint.AWSPinpointPushNotificationsPlugin
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import dagger.hilt.android.HiltAndroidApp

/**
 * Aura Voice Chat Application class
 * Developer: Hawkaye Visions LTD — Pakistan
 *
 * Main application class with Hilt dependency injection and AWS Amplify.
 * This class also provides a custom WorkManager configuration, which is required
 * because the default initializer is disabled in the AndroidManifest.xml.
 */
@HiltAndroidApp
class AuraApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        // Initialize AWS Amplify
        initializeAmplify()
    }

    /**
     * Provides the custom configuration for WorkManager on-demand.
     * This method is required by the Configuration.Provider interface.
     */
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .build()

    private fun initializeAmplify() {
        try {
            // Add Cognito Auth plugin
            Amplify.addPlugin(AWSCognitoAuthPlugin())

            // Add S3 Storage plugin
            Amplify.addPlugin(AWSS3StoragePlugin())

            // Add Pinpoint Push Notifications plugin
            Amplify.addPlugin(AWSPinpointPushNotificationsPlugin())

            // Configure Amplify
            Amplify.configure(applicationContext)

            Log.i(TAG, "AWS Amplify initialized successfully")
        } catch (error: AmplifyException) {
            Log.e(TAG, "Could not initialize Amplify", error)
        } catch (error: PushNotificationsException) {
            Log.e(TAG, "Could not initialize Push Notifications", error)
        }
    }

    companion object {
        private const val TAG = "AuraApplication"
    }
}
