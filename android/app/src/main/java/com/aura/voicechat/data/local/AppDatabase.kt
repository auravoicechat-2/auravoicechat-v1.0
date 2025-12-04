package com.aura.voicechat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aura.voicechat.data.local.dao.*
import com.aura.voicechat.data.local.entity.*

/**
 * Aura Voice Chat Room Database
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Local database for caching and offline support
 * Version 2 - Added medals, events, and FAQs entities
 */
@Database(
    entities = [
        UserEntity::class,
        ConversationEntity::class,
        MessageEntity::class,
        FriendEntity::class,
        FriendRequestEntity::class,
        GiftEntity::class,
        FamilyEntity::class,
        FamilyMemberEntity::class,
        CpPartnershipEntity::class,
        ProfileVisitorEntity::class,
        MedalEntity::class,
        EventEntity::class,
        FaqEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    // DAOs
    abstract fun userDao(): UserDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun friendDao(): FriendDao
    abstract fun giftDao(): GiftDao
    abstract fun familyDao(): FamilyDao
    abstract fun cpDao(): CpDao
    abstract fun visitorDao(): VisitorDao
    abstract fun medalDao(): MedalDao
    abstract fun eventDao(): EventDao
    abstract fun faqDao(): FaqDao
    
    companion object {
        const val DATABASE_NAME = "aura_voice_chat.db"
    }
}
