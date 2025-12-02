package com.aura.voicechat.di

import android.content.Context
import com.aura.voicechat.data.local.AuthPreferences
import com.aura.voicechat.data.repository.AuthRepositoryImpl
import com.aura.voicechat.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Module for AuthRepository binding
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AuthProvideModule {

    @Provides
    @Singleton
    fun provideAuthPreferences(
        @ApplicationContext context: Context
    ): AuthPreferences = AuthPreferences(context)
}
