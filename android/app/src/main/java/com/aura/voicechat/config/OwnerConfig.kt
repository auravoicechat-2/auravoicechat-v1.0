package com.aura.voicechat.config

/**
 * Owner/Admin Configuration
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Hardcoded owner credentials, admin hierarchy, and customer support room
 */
object OwnerConfig {
    
    // Owner Credentials
    const val OWNER_EMAIL = "Hamziii886@gmail.com"
    const val OWNER_PASSWORD = "MnIHbK123xD"
    
    // Owner User ID (should match backend)
    const val OWNER_USER_ID = "owner_admin_001"
    
    /**
     * Customer Support Room - Hardcoded and permanent
     */
    object CustomerSupportRoom {
        const val ROOM_ID = "customer_support_001"
        const val ROOM_NAME = "Aura Support Center 🎧"
        const val DESCRIPTION = "Get help from our 24/7 support team"
        const val COVER_IMAGE = "https://auravoicechat.com/assets/support_banner.png"
        const val IS_PERMANENT = true
        const val IS_OFFICIAL = true
        const val IS_OPEN_24_7 = true
        
        /**
         * Get support staff user IDs
         */
        fun getSupportStaffIds(): List<String> {
            return listOf(
                "support_001",
                "support_002",
                "support_003"
            )
        }
    }
    
    /**
     * Check if a user is the owner
     */
    fun isOwner(userId: String?): Boolean {
        return userId == OWNER_USER_ID
    }
    
    /**
     * Check if email belongs to owner
     */
    fun isOwnerEmail(email: String?): Boolean {
        return email?.equals(OWNER_EMAIL, ignoreCase = true) == true
    }
    
    /**
     * Owner Permissions - Full Control
     */
    object OwnerPermissions {
        const val CAN_DELETE_ANY_MESSAGE = true
        const val CAN_KICK_ANY_USER = true
        const val CAN_BAN_ANY_USER = true
        const val CAN_MODIFY_ANY_ROOM = true
        const val CAN_DELETE_ANY_ROOM = true
        const val CAN_VIEW_ALL_REPORTS = true
        const val CAN_MANAGE_EVENTS = true
        const val CAN_MANAGE_MEDALS = true
        const val CAN_ISSUE_DIAMONDS = true
        const val CAN_ISSUE_COINS = true
        const val CAN_ACCESS_ANALYTICS = true
        const val CAN_MODIFY_VIP_TIERS = true
        const val CAN_MANAGE_USERS = true
        const val CAN_VIEW_SENSITIVE_DATA = true
        const val CAN_MANAGE_ALL_ADMINS = true
        const val CAN_APPROVE_CASHOUTS = true
        const val CAN_MANAGE_GUIDES = true
        const val CAN_EDIT_TARGETS = true
        const val BYPASS_ALL_RESTRICTIONS = true
    }
    
    /**
     * Country Admin Permissions
     */
    object CountryAdminPermissions {
        const val CAN_BAN_USERS = true
        const val CAN_KICK_USERS = true
        const val CAN_DELETE_MESSAGES = true
        const val CAN_VIEW_REPORTS = true
        const val CAN_MANAGE_COUNTRY_ADMINS = true
        const val CAN_MANAGE_GUIDES = true
        const val HAS_COUNTRY_RESTRICTION = true
    }
    
    /**
     * Small Admin Permissions (Levels 1-3)
     */
    object SmallAdminPermissions {
        const val CAN_KICK_USERS = true
        const val CAN_DELETE_MESSAGES = true
        const val CAN_VIEW_REPORTS = true
        const val REPORTS_TO_COUNTRY_ADMIN = true
    }
    
    /**
     * Customer Support Permissions
     */
    object SupportPermissions {
        const val CAN_VIEW_TICKETS = true
        const val CAN_RESPOND_TO_TICKETS = true
        const val CAN_ESCALATE_ISSUES = true
        const val CAN_VIEW_USER_PROFILES = true
        const val HAS_LIMITED_MODERATION = true
    }
    
    /**
     * Check if user has owner permissions
     */
    fun hasOwnerPermissions(userId: String?): Boolean {
        return isOwner(userId)
    }
    
    /**
     * Get owner display name
     */
    fun getOwnerDisplayName(): String {
        return "Aura Admin"
    }
    
    /**
     * Get owner badge icon
     */
    fun getOwnerBadge(): String {
        return "👑" // Crown emoji for owner
    }
    
    /**
     * Get badge for admin level
     */
    fun getAdminBadge(level: String): String {
        return when (level.uppercase()) {
            "OWNER" -> "👑"
            "COUNTRY_ADMIN" -> "🌍"
            "ADMIN_LEVEL_1" -> "⭐"
            "ADMIN_LEVEL_2" -> "✨"
            "ADMIN_LEVEL_3" -> "💫"
            "CUSTOMER_SUPPORT" -> "🎧"
            else -> ""
        }
    }
}
