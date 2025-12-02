# ProGuard Rules for Aura Voice Chat
# Developer: Hawkaye Visions LTD — Pakistan

# ========================
# General Rules
# ========================

# Keep source file names and line numbers for crash reports
-keepattributes SourceFile,LineNumberTable

# Keep annotations
-keepattributes *Annotation*

# Keep generic signatures and inner class info
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# ========================
# Application Models
# ========================

# Keep model classes for JSON parsing
-keep class com.aura.voicechat.data.model.** { *; }
-keep class com.aura.voicechat.domain.model.** { *; }

# Keep fields annotated with @SerializedName
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ========================
# AWS Amplify / AWS SDK
# ========================

-keep class com.amplifyframework.** { *; }
-dontwarn com.amplifyframework.**

-keep class com.amazonaws.** { *; }
-dontwarn com.amazonaws.**

# AWS SDK Kotlin
-keep class aws.sdk.kotlin.** { *; }
-dontwarn aws.sdk.kotlin.**

# AWS SDK v2 (Java)
-keep class software.amazon.awssdk.** { *; }
-dontwarn software.amazon.awssdk.**

# AWS CRT (Common Runtime) - optional native components
-dontwarn software.amazon.awssdk.crt.**

# Cognito
-keep class com.amazonaws.mobileconnectors.cognitoidentityprovider.** { *; }

# S3
-keep class com.amazonaws.services.s3.** { *; }

# Pinpoint
-keep class com.amazonaws.mobileconnectors.pinpoint.** { *; }

# ========================
# Reactor / Netty BlockHound (optional)
# ========================

-dontwarn reactor.blockhound.**
-dontwarn io.netty.util.internal.Hidden$NettyBlockHoundIntegration

# ========================
# Apache HTTP Client
# ========================

# JNDI/LDAP classes used by Apache HTTP hostname verifier (not available on Android)
-dontwarn javax.naming.**
-dontwarn org.apache.http.conn.ssl.DefaultHostnameVerifier

# ========================
# Retrofit
# ========================

-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**

# ========================
# OkHttp
# ========================

-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**

# ========================
# Netty native / tcnative (not available on Android)
# ========================
-dontwarn io.netty.internal.tcnative.**

# Log4J 1.x and 2.x (Netty logging integration; not used directly on Android)
-dontwarn org.apache.log4j.**
-dontwarn org.apache.logging.log4j.**

# Jetty ALPN / NPN (only present on desktop/server JVMs)
-dontwarn org.eclipse.jetty.alpn.**
-dontwarn org.eclipse.jetty.npn.**

# JGSS / Kerberos (Apache HttpClient optional auth; not on Android)
-dontwarn org.ietf.jgss.**

# ========================
# Gson
# ========================

-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from removing fields that Gson uses
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ========================
# Coroutines
# ========================

-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# ========================
# Kotlin
# ========================

-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-keep class kotlin.reflect.jvm.internal.** { *; }

# ========================
# Jetpack Compose / Lifecycle
# ========================

-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**
-keep class androidx.lifecycle.** { *; }

# ========================
# Hilt / Dagger
# ========================

# Keep Hilt ViewModels annotated with @HiltViewModel
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

# Keep Hilt Android managers and key internal components (narrower than dagger.hilt.**)
-keep class dagger.hilt.android.internal.managers.** { *; }
-keep class dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }
-keep class dagger.hilt.EntryPoints { *; }
-keep class dagger.hilt.internal.GeneratedComponent { *; }

# Keep javax.inject annotations and types
-keep class javax.inject.** { *; }

# Keep FragmentContextWrapper inner class
-keep class dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# ========================
# WebRTC
# ========================

-keep class org.webrtc.** { *; }
-dontwarn org.webrtc.**

# ========================
# ExoPlayer / Media3
# ========================

-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# ========================
# ML Kit
# ========================

-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**

# ========================
# CameraX
# ========================

-keep class androidx.camera.** { *; }
-dontwarn androidx.camera.**

# ========================
# Room Database
# ========================

-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# ========================
# Lottie
# ========================

-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** { *; }

# ========================
# Coil 3.x
# ========================

-keep class coil3.** { *; }
-dontwarn coil3.**

# ========================
# Facebook SDK
# ========================

-keep class com.facebook.** { *; }
-dontwarn com.facebook.**

# ========================
# Google Play Services
# ========================

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# ========================
# Security Crypto
# ========================

-keep class androidx.security.crypto.** { *; }

# ========================
# Remove Logging
# ========================

-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# ========================
# Enum
# ========================

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ========================
# Parcelable
# ========================

-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# ========================
# Serializable
# ========================

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ========================
# Native Methods
# ========================

-keepclasseswithmembernames class * {
    native <methods>;
}

# ========================
# Views with setters/getters
# ========================

-keepclassmembers class * extends android.view.View {
    void set*(***);
    *** get*();
}
