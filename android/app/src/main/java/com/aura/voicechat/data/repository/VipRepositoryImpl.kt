package com.aura.voicechat.data.repository

import com.aura.voicechat.data.model.PurchaseVipRequest
import com.aura.voicechat.data.model.VipPackageDto
import com.aura.voicechat.data.model.VipStatusResponse
import com.aura.voicechat.data.remote.ApiService
import com.aura.voicechat.domain.repository.VipRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * VIP Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class VipRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : VipRepository {
    
    override suspend fun getVipStatus(): Result<VipStatusResponse> {
        return try {
            val response = apiService.getVipStatus()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get VIP status: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getVipPackages(): Result<List<VipPackageDto>> {
        return try {
            val response = apiService.getVipPackages()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.packages)
            } else {
                Result.failure(Exception("Failed to get VIP packages: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun purchaseVip(packageId: String): Result<VipStatusResponse> {
        return try {
            val response = apiService.purchaseVip(PurchaseVipRequest(packageId))
            if (response.isSuccessful && response.body() != null) {
                // Map PurchaseVipResponse to VipStatusResponse
                val purchase = response.body()!!
                Result.success(VipStatusResponse(
                    tier = purchase.newTier,
                    daysRemaining = purchase.daysRemaining,
                    totalSpent = 0L, // Not available in purchase response
                    progress = 0f,
                    expiry = null
                ))
            } else {
                Result.failure(Exception("Failed to purchase VIP: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
