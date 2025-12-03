package com.aura.voicechat.data.repository

import com.aura.voicechat.data.model.*
import com.aura.voicechat.data.remote.ApiService
import com.aura.voicechat.domain.repository.ReferralRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Referral Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class ReferralRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ReferralRepository {
    
    override suspend fun bindReferralCode(code: String): Result<Unit> {
        return try {
            val response = apiService.bindReferralCode(BindReferralRequest(code))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to bind referral code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getReferralCoinsSummary(): Result<ReferralCoinsSummaryResponse> {
        return try {
            val response = apiService.getReferralCoinsSummary()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get referral coins summary: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun withdrawReferralCoins(amount: Long): Result<Unit> {
        return try {
            val response = apiService.withdrawReferralCoins(WithdrawCoinsRequest(amount))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to withdraw referral coins: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getReferralRecords(
        page: Int,
        pageSize: Int
    ): Result<ReferralRecordsResponse> {
        return try {
            val response = apiService.getReferralRecords(page, pageSize)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get referral records: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getReferralCashSummary(): Result<ReferralCashSummaryResponse> {
        return try {
            val response = apiService.getReferralCashSummary()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get referral cash summary: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun withdrawReferralCash(destination: String): Result<Unit> {
        return try {
            val response = apiService.withdrawReferralCash(WithdrawCashRequest(destination))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to withdraw referral cash: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
