package com.aura.voicechat.data.repository

import com.aura.voicechat.data.model.EventDto
import com.aura.voicechat.data.model.EventProgressResponse
import com.aura.voicechat.data.remote.ApiService
import com.aura.voicechat.domain.repository.EventRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Event Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class EventRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : EventRepository {
    
    override suspend fun getActiveEvents(): Result<List<EventDto>> {
        return try {
            val response = apiService.getEvents()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.events)
            } else {
                Result.failure(Exception("Failed to get events: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getEventDetails(eventId: String): Result<EventDto> {
        return try {
            val response = apiService.getEventDetails(eventId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.event)
            } else {
                Result.failure(Exception("Failed to get event details: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun participateInEvent(eventId: String): Result<Unit> {
        return try {
            val response = apiService.participateInEvent(eventId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to participate in event: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getEventProgress(eventId: String): Result<EventProgressResponse> {
        return try {
            val response = apiService.getEventProgress(eventId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get event progress: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun claimDailyReward(): Result<Any> {
        return try {
            val response = apiService.claimDailyReward()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to claim daily reward: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
