package com.aura.voicechat.data.repository

import com.aura.voicechat.data.model.*
import com.aura.voicechat.data.remote.ApiService
import com.aura.voicechat.domain.repository.GameRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Game Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class GameRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : GameRepository {
    
    override suspend fun getAvailableGames(): Result<List<GameInfoDto>> {
        return try {
            val response = apiService.getAvailableGames()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.games)
            } else {
                Result.failure(Exception("Failed to get games: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getGameStats(): Result<GameStatsResponse> {
        return try {
            val response = apiService.getGameStats()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get game stats: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun startGame(gameType: String, betAmount: Long): Result<String> {
        return try {
            val response = apiService.startGame(
                gameType,
                StartGameRequest(betAmount)
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.session.sessionId)
            } else {
                Result.failure(Exception("Failed to start game: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun playGame(
        gameType: String,
        sessionId: String,
        action: String
    ): Result<GameResultResponse> {
        return try {
            val response = apiService.gameAction(
                gameType,
                GameActionRequest(
                    sessionId = sessionId,
                    action = action
                )
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to play game: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getGameHistory(
        gameType: String,
        page: Int,
        limit: Int
    ): Result<List<Any>> {
        return try {
            val response = apiService.getGameHistory(gameType, page, limit)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.records)
            } else {
                Result.failure(Exception("Failed to get game history: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
