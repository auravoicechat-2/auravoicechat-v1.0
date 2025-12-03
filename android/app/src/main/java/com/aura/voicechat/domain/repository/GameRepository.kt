package com.aura.voicechat.domain.repository

import com.aura.voicechat.data.model.GameInfoDto
import com.aura.voicechat.data.model.GameResultResponse
import com.aura.voicechat.data.model.GameStatsResponse

/**
 * Game Repository interface
 * Developer: Hawkaye Visions LTD — Pakistan
 */
interface GameRepository {
    suspend fun getAvailableGames(): Result<List<GameInfoDto>>
    suspend fun getGameStats(): Result<GameStatsResponse>
    suspend fun startGame(gameType: String, betAmount: Long): Result<String> // Returns sessionId
    suspend fun playGame(gameType: String, sessionId: String, action: String): Result<GameResultResponse>
    suspend fun getGameHistory(gameType: String, page: Int, limit: Int): Result<List<Any>>
}
