package com.aura.voicechat.domain.repository

import com.aura.voicechat.data.model.*

/**
 * Referral Repository interface
 * Developer: Hawkaye Visions LTD — Pakistan
 */
interface ReferralRepository {
    suspend fun bindReferralCode(code: String): Result<Unit>
    suspend fun getReferralCoinsSummary(): Result<ReferralCoinsSummaryResponse>
    suspend fun withdrawReferralCoins(amount: Long): Result<Unit>
    suspend fun getReferralRecords(page: Int, pageSize: Int): Result<ReferralRecordsResponse>
    suspend fun getReferralCashSummary(): Result<ReferralCashSummaryResponse>
    suspend fun withdrawReferralCash(destination: String): Result<Unit>
}
