package com.aura.voicechat.domain.repository

import com.aura.voicechat.data.model.EventDto
import com.aura.voicechat.data.model.EventProgressResponse

/**
 * Event Repository interface
 * Developer: Hawkaye Visions LTD — Pakistan
 */
interface EventRepository {
    suspend fun getActiveEvents(): Result<List<EventDto>>
    suspend fun getEventDetails(eventId: String): Result<EventDto>
    suspend fun participateInEvent(eventId: String): Result<Unit>
    suspend fun getEventProgress(eventId: String): Result<EventProgressResponse>
    suspend fun claimDailyReward(): Result<Any>
}
