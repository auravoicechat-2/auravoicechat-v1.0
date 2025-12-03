package com.aura.voicechat.domain.repository

import com.aura.voicechat.data.model.VipPackageDto
import com.aura.voicechat.data.model.VipStatusResponse

/**
 * VIP Repository interface
 * Developer: Hawkaye Visions LTD — Pakistan
 */
interface VipRepository {
    suspend fun getVipStatus(): Result<VipStatusResponse>
    suspend fun getVipPackages(): Result<List<VipPackageDto>>
    suspend fun purchaseVip(packageId: String): Result<VipStatusResponse>
}
