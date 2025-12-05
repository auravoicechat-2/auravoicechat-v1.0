package com.aura.voicechat.domain.model

/**
 * VIP Tier domain model
 * Developer: Hawkaye Visions LTD — Pakistan
 */
data class VipTier(
    val tier: Int,
    val name: String,
    val rechargeRequired: Int,
    val expMultiplier: Double,
    val dailyRewardMultiplier: Double,
    val giftBonusPercent: Int,
    val features: List<String>,
    val monthlyCoins: Long
)

/**
 * VIP tiers with multipliers as per documentation
 * VIP1-VIP10 with multipliers 1.2x-3.0x
 */
object VipTiers {
    val tiers = listOf(
        VipTier(tier = 1, name = "VIP 1", rechargeRequired = 10, expMultiplier = 1.1, dailyRewardMultiplier = 1.2, giftBonusPercent = 5, features = listOf("vip_badge", "priority_support"), monthlyCoins = 10_000),
        VipTier(tier = 2, name = "VIP 2", rechargeRequired = 50, expMultiplier = 1.15, dailyRewardMultiplier = 1.4, giftBonusPercent = 7, features = listOf("vip_badge", "priority_support", "exclusive_frame_7d"), monthlyCoins = 25_000),
        VipTier(tier = 3, name = "VIP 3", rechargeRequired = 100, expMultiplier = 1.2, dailyRewardMultiplier = 1.6, giftBonusPercent = 10, features = listOf("vip_badge", "priority_support", "exclusive_frame_7d", "custom_id_discount"), monthlyCoins = 50_000),
        VipTier(tier = 4, name = "VIP 4", rechargeRequired = 250, expMultiplier = 1.25, dailyRewardMultiplier = 1.8, giftBonusPercent = 12, features = listOf("vip_badge", "priority_support", "exclusive_frame_14d", "custom_id_discount", "vip_vehicle_7d"), monthlyCoins = 100_000),
        VipTier(tier = 5, name = "VIP 5", rechargeRequired = 500, expMultiplier = 1.3, dailyRewardMultiplier = 2.0, giftBonusPercent = 15, features = listOf("vip_badge", "priority_support", "exclusive_frame_30d", "custom_id_discount", "vip_vehicle_14d", "super_mic_access"), monthlyCoins = 200_000),
        VipTier(tier = 6, name = "VIP 6", rechargeRequired = 1000, expMultiplier = 1.35, dailyRewardMultiplier = 2.2, giftBonusPercent = 18, features = listOf("vip_badge", "priority_support", "exclusive_frame_30d", "custom_id_free", "vip_vehicle_30d", "super_mic_access", "vip_theme_14d"), monthlyCoins = 350_000),
        VipTier(tier = 7, name = "VIP 7", rechargeRequired = 2500, expMultiplier = 1.4, dailyRewardMultiplier = 2.4, giftBonusPercent = 20, features = listOf("vip_badge", "priority_support", "exclusive_frame_permanent", "custom_id_free", "vip_vehicle_30d", "super_mic_access", "vip_theme_30d", "exclusive_games"), monthlyCoins = 500_000),
        VipTier(tier = 8, name = "VIP 8", rechargeRequired = 5000, expMultiplier = 1.45, dailyRewardMultiplier = 2.6, giftBonusPercent = 22, features = listOf("vip_badge", "priority_support", "exclusive_frame_permanent", "custom_id_free", "vip_vehicle_permanent", "super_mic_access", "vip_theme_30d", "exclusive_games", "vip_seat_effect"), monthlyCoins = 750_000),
        VipTier(tier = 9, name = "VIP 9", rechargeRequired = 10000, expMultiplier = 1.5, dailyRewardMultiplier = 2.8, giftBonusPercent = 25, features = listOf("svip_badge", "priority_support", "exclusive_frame_permanent", "custom_id_free", "vip_vehicle_permanent", "super_mic_access", "vip_theme_permanent", "exclusive_games", "vip_seat_effect", "personal_manager"), monthlyCoins = 1_000_000),
        VipTier(tier = 10, name = "VIP 10 (SVIP)", rechargeRequired = 25000, expMultiplier = 1.6, dailyRewardMultiplier = 3.0, giftBonusPercent = 30, features = listOf("svip_badge", "priority_support", "exclusive_frame_permanent", "custom_id_free", "svip_vehicle_permanent", "super_mic_access", "svip_theme_permanent", "exclusive_games", "svip_seat_effect", "personal_manager", "legendary_set"), monthlyCoins = 2_000_000)
    )
    
    fun getMultiplier(tier: Int): Double {
        return tiers.find { it.tier == tier }?.dailyRewardMultiplier ?: 1.0
    }
}
