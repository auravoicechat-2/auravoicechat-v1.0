package com.aura.voicechat.di

import com.aura.voicechat.data.repository.*
import com.aura.voicechat.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Dependency Injection Module
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindRoomRepository(impl: RoomRepositoryImpl): RoomRepository
    
    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
    
    @Binds
    @Singleton
    abstract fun bindWalletRepository(impl: WalletRepositoryImpl): WalletRepository
    
    @Binds
    @Singleton
    abstract fun bindRewardsRepository(impl: RewardsRepositoryImpl): RewardsRepository
    
    @Binds
    @Singleton
    abstract fun bindKycRepository(impl: KycRepositoryImpl): KycRepository
    
    @Binds
    @Singleton
    abstract fun bindVipRepository(impl: VipRepositoryImpl): VipRepository
    
    @Binds
    @Singleton
    abstract fun bindGameRepository(impl: GameRepositoryImpl): GameRepository
    
    @Binds
    @Singleton
    abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository
    
    @Binds
    @Singleton
    abstract fun bindReferralRepository(impl: ReferralRepositoryImpl): ReferralRepository
    
    @Binds
    @Singleton
    abstract fun bindStoreRepository(impl: StoreRepositoryImpl): com.aura.voicechat.domain.repository.StoreRepository
    
    @Binds
    @Singleton
    abstract fun bindGiftRepository(impl: GiftRepositoryImpl): com.aura.voicechat.domain.repository.GiftRepository
    
    @Binds
    @Singleton
    abstract fun bindInventoryRepository(impl: InventoryRepositoryImpl): com.aura.voicechat.domain.repository.InventoryRepository
}
