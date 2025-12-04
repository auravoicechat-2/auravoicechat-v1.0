package com.aura.voicechat.data.model

import com.google.gson.annotations.SerializedName

/**
 * Admin System Models
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Multi-tier admin hierarchy system
 */

// Admin Levels
enum class AdminLevel {
    OWNER,              // Level 1 - Full control
    COUNTRY_ADMIN,      // Level 2 - Per country
    ADMIN_LEVEL_1,      // Level 3 - Senior admin
    ADMIN_LEVEL_2,      // Level 4 - Mid-level admin
    ADMIN_LEVEL_3,      // Level 5 - Junior admin
    CUSTOMER_SUPPORT,   // Level 6 - Support staff
    NONE                // Regular user
}

// Admin User Model
data class AdminUser(
    @SerializedName("userId") val userId: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("adminLevel") val adminLevel: AdminLevel,
    @SerializedName("country") val country: String?,
    @SerializedName("reportsTo") val reportsTo: String?, // UserID of supervisor
    @SerializedName("permissions") val permissions: AdminPermissions,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("assignedAt") val assignedAt: Long
)

// Admin Permissions
data class AdminPermissions(
    @SerializedName("canBanUsers") val canBanUsers: Boolean = false,
    @SerializedName("canKickUsers") val canKickUsers: Boolean = false,
    @SerializedName("canDeleteMessages") val canDeleteMessages: Boolean = false,
    @SerializedName("canDeleteRooms") val canDeleteRooms: Boolean = false,
    @SerializedName("canModifyUsers") val canModifyUsers: Boolean = false,
    @SerializedName("canViewReports") val canViewReports: Boolean = false,
    @SerializedName("canManageEvents") val canManageEvents: Boolean = false,
    @SerializedName("canIssueCurrency") val canIssueCurrency: Boolean = false,
    @SerializedName("canApproveCashouts") val canApproveCashouts: Boolean = false,
    @SerializedName("canManageAdmins") val canManageAdmins: Boolean = false,
    @SerializedName("canAccessAnalytics") val canAccessAnalytics: Boolean = false,
    @SerializedName("canManageGuides") val canManageGuides: Boolean = false,
    @SerializedName("countryRestriction") val countryRestriction: String? = null
)

// Guide System Models
enum class GuideStatus {
    NOT_APPLIED,
    PENDING,
    APPROVED,
    REJECTED,
    SUSPENDED
}

data class GuideApplication(
    @SerializedName("applicationId") val applicationId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("userAvatar") val userAvatar: String?,
    @SerializedName("gender") val gender: String, // Must be FEMALE
    @SerializedName("age") val age: Int,
    @SerializedName("country") val country: String,
    @SerializedName("languages") val languages: List<String>,
    @SerializedName("experience") val experience: String,
    @SerializedName("motivation") val motivation: String,
    @SerializedName("availableHours") val availableHours: Int,
    @SerializedName("status") val status: GuideStatus,
    @SerializedName("appliedAt") val appliedAt: Long,
    @SerializedName("reviewedAt") val reviewedAt: Long?,
    @SerializedName("reviewedBy") val reviewedBy: String?,
    @SerializedName("rejectionReason") val rejectionReason: String?
)

data class GuideProfile(
    @SerializedName("userId") val userId: String,
    @SerializedName("level") val level: Int,
    @SerializedName("rating") val rating: Float,
    @SerializedName("totalSessions") val totalSessions: Int,
    @SerializedName("totalEarnings") val totalEarnings: Long,
    @SerializedName("monthlyTarget") val monthlyTarget: GuideTarget,
    @SerializedName("currentProgress") val currentProgress: GuideProgress,
    @SerializedName("isActive") val isActive: Boolean
)

data class GuideTarget(
    @SerializedName("targetDiamonds") val targetDiamonds: Long,
    @SerializedName("targetSessions") val targetSessions: Int,
    @SerializedName("targetRating") val targetRating: Float,
    @SerializedName("bonus") val bonus: Long
)

data class GuideProgress(
    @SerializedName("currentDiamonds") val currentDiamonds: Long,
    @SerializedName("currentSessions") val currentSessions: Int,
    @SerializedName("currentRating") val currentRating: Float,
    @SerializedName("progressPercentage") val progressPercentage: Float
)

// Earning System Models
data class EarningTarget(
    @SerializedName("targetId") val targetId: String,
    @SerializedName("name") val name: String,
    @SerializedName("requiredDiamonds") val requiredDiamonds: Long,
    @SerializedName("cashReward") val cashReward: Double,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("validFrom") val validFrom: Long,
    @SerializedName("validUntil") val validUntil: Long
)

data class UserEarningStatus(
    @SerializedName("userId") val userId: String,
    @SerializedName("totalDiamondsReceived") val totalDiamondsReceived: Long,
    @SerializedName("currentMonthDiamonds") val currentMonthDiamonds: Long,
    @SerializedName("completedTargets") val completedTargets: List<String>,
    @SerializedName("currentTarget") val currentTarget: EarningTarget?,
    @SerializedName("progressPercentage") val progressPercentage: Float,
    @SerializedName("pendingCashouts") val pendingCashouts: List<CashoutRequest>,
    @SerializedName("totalEarnings") val totalEarnings: Double
)

data class CashoutRequest(
    @SerializedName("requestId") val requestId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("diamonds") val diamonds: Long,
    @SerializedName("status") val status: CashoutStatus,
    @SerializedName("requestedAt") val requestedAt: Long,
    @SerializedName("clearanceDate") val clearanceDate: Long, // 5 days from request
    @SerializedName("approvedBy") val approvedBy: String?,
    @SerializedName("approvedAt") val approvedAt: Long?,
    @SerializedName("rejectionReason") val rejectionReason: String?,
    @SerializedName("paymentMethod") val paymentMethod: String,
    @SerializedName("paymentDetails") val paymentDetails: String
)

enum class CashoutStatus {
    PENDING_CLEARANCE,  // Within 5-day waiting period
    PENDING_APPROVAL,   // After 5 days, waiting for owner approval
    APPROVED,           // Owner approved
    PROCESSING,         // Payment being processed
    COMPLETED,          // Payment sent
    REJECTED            // Rejected by owner
}

// Customer Support Room
data class CustomerSupportRoom(
    @SerializedName("roomId") val roomId: String = "customer_support_001",
    @SerializedName("roomName") val roomName: String = "Aura Support Center",
    @SerializedName("description") val description: String = "Get help from our support team",
    @SerializedName("coverImage") val coverImage: String = "https://auravoicechat.com/support_banner.png",
    @SerializedName("isPermanent") val isPermanent: Boolean = true,
    @SerializedName("isOfficial") val isOfficial: Boolean = true,
    @SerializedName("supportStaff") val supportStaff: List<String>, // UserIDs of support staff
    @SerializedName("isOpen24_7") val isOpen24_7: Boolean = true
)

// API Request/Response Models
data class ApplyGuideRequest(
    @SerializedName("languages") val languages: List<String>,
    @SerializedName("experience") val experience: String,
    @SerializedName("motivation") val motivation: String,
    @SerializedName("availableHours") val availableHours: Int
)

data class ReviewGuideApplicationRequest(
    @SerializedName("applicationId") val applicationId: String,
    @SerializedName("approved") val approved: Boolean,
    @SerializedName("rejectionReason") val rejectionReason: String?
)

data class RequestCashoutRequest(
    @SerializedName("amount") val amount: Double,
    @SerializedName("paymentMethod") val paymentMethod: String,
    @SerializedName("paymentDetails") val paymentDetails: String
)

data class ApproveCashoutRequest(
    @SerializedName("requestId") val requestId: String,
    @SerializedName("approved") val approved: Boolean,
    @SerializedName("rejectionReason") val rejectionReason: String?
)

// Target Sheet Models
data class EarningTargetSheet(
    @SerializedName("userId") val userId: String,
    @SerializedName("userType") val userType: String, // "USER" or "GUIDE"
    @SerializedName("targets") val targets: List<TargetTier>,
    @SerializedName("rules") val rules: List<String>,
    @SerializedName("paymentInfo") val paymentInfo: PaymentInfo
)

data class TargetTier(
    @SerializedName("tier") val tier: Int,
    @SerializedName("name") val name: String,
    @SerializedName("requiredDiamonds") val requiredDiamonds: Long,
    @SerializedName("cashReward") val cashReward: Double,
    @SerializedName("bonusCoins") val bonusCoins: Long?,
    @SerializedName("duration") val duration: String // "Monthly", "Weekly", etc.
)

data class PaymentInfo(
    @SerializedName("minCashout") val minCashout: Double,
    @SerializedName("clearanceDays") val clearanceDays: Int = 5,
    @SerializedName("requiresOwnerApproval") val requiresOwnerApproval: Boolean = true,
    @SerializedName("conversionRate") val conversionRate: String, // e.g., "100,000 Diamonds = $30"
    @SerializedName("paymentMethods") val paymentMethods: List<String>
)
