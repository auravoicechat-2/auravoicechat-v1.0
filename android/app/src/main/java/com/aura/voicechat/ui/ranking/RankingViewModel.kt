package com.aura.voicechat.ui.ranking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Ranking ViewModel (Live API Connected)
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class RankingViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    
    companion object {
        private const val TAG = "RankingViewModel"
    }
    
    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState: StateFlow<RankingUiState> = _uiState.asStateFlow()
    
    fun loadRanking(type: String, period: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                when (type) {
                    "sender" -> loadGiftSenderRankings(period)
                    "receiver" -> loadGiftReceiverRankings(period)
                    "family" -> loadFamilyRankings(period)
                    "level" -> loadLevelRankings()
                    "wealth" -> loadWealthRankings()
                    "charm" -> loadCharmRankings(period)
                    "cp" -> loadCpRankings(period)
                    else -> loadGiftSenderRankings(period)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading rankings", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    private suspend fun loadGiftSenderRankings(period: String) {
        val response = apiService.getGiftRankings(type = period, category = "sender")
        if (response.isSuccessful && response.body() != null) {
            val data = response.body()!!
            val rankings = data.rankings.map { r ->
                RankingItemData(
                    id = r.userId,
                    name = r.userName,
                    avatar = r.userAvatar,
                    level = r.userLevel,
                    value = r.value
                )
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                rankings = rankings,
                myRank = data.myRank ?: 0
            )
            Log.d(TAG, "Loaded ${rankings.size} sender rankings")
        } else {
            _uiState.value = _uiState.value.copy(isLoading = false, rankings = emptyList())
        }
    }
    
    private suspend fun loadGiftReceiverRankings(period: String) {
        val response = apiService.getGiftRankings(type = period, category = "receiver")
        if (response.isSuccessful && response.body() != null) {
            val data = response.body()!!
            val rankings = data.rankings.map { r ->
                RankingItemData(
                    id = r.userId,
                    name = r.userName,
                    avatar = r.userAvatar,
                    level = r.userLevel,
                    value = r.value
                )
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                rankings = rankings,
                myRank = data.myRank ?: 0
            )
            Log.d(TAG, "Loaded ${rankings.size} receiver rankings")
        } else {
            _uiState.value = _uiState.value.copy(isLoading = false, rankings = emptyList())
        }
    }
    
    private suspend fun loadFamilyRankings(period: String) {
        val response = apiService.getFamilyRankings(type = period)
        if (response.isSuccessful && response.body() != null) {
            val data = response.body()!!
            val rankings = data.rankings.map { r ->
                RankingItemData(
                    id = r.familyId,
                    name = r.familyName,
                    avatar = r.badge,
                    level = r.level,
                    value = r.weeklyGifts
                )
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                rankings = rankings
            )
            Log.d(TAG, "Loaded ${rankings.size} family rankings")
        } else {
            _uiState.value = _uiState.value.copy(isLoading = false, rankings = emptyList())
        }
    }
    
    private suspend fun loadLevelRankings() {
        val response = apiService.getLevelRankings()
        if (response.isSuccessful && response.body() != null) {
            val data = response.body()!!
            val rankings = data.rankings.map { r ->
                RankingItemData(
                    id = r.userId,
                    name = r.userName,
                    avatar = r.userAvatar,
                    level = r.userLevel,
                    value = r.value
                )
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                rankings = rankings,
                myRank = data.myRank ?: 0
            )
            Log.d(TAG, "Loaded ${rankings.size} level rankings")
        } else {
            _uiState.value = _uiState.value.copy(isLoading = false, rankings = emptyList())
        }
    }
    
    private suspend fun loadWealthRankings() {
        val response = apiService.getWealthRankings()
        if (response.isSuccessful && response.body() != null) {
            val data = response.body()!!
            val rankings = data.rankings.map { r ->
                RankingItemData(
                    id = r.userId,
                    name = r.userName,
                    avatar = r.userAvatar,
                    level = r.userLevel,
                    value = r.value
                )
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                rankings = rankings,
                myRank = data.myRank ?: 0
            )
            Log.d(TAG, "Loaded ${rankings.size} wealth rankings")
        } else {
            _uiState.value = _uiState.value.copy(isLoading = false, rankings = emptyList())
        }
    }
    
    private suspend fun loadCharmRankings(period: String) {
        val response = apiService.getCharmRankings(type = period)
        if (response.isSuccessful && response.body() != null) {
            val data = response.body()!!
            val rankings = data.rankings.map { r ->
                RankingItemData(
                    id = r.userId,
                    name = r.userName,
                    avatar = r.userAvatar,
                    level = r.userLevel,
                    value = r.value
                )
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                rankings = rankings,
                myRank = data.myRank ?: 0
            )
            Log.d(TAG, "Loaded ${rankings.size} charm rankings")
        } else {
            _uiState.value = _uiState.value.copy(isLoading = false, rankings = emptyList())
        }
    }
    
    private suspend fun loadCpRankings(period: String) {
        val response = apiService.getCpRankings(type = period)
        if (response.isSuccessful && response.body() != null) {
            val data = response.body()!!
            val rankings = data.rankings.map { r ->
                RankingItemData(
                    id = "${r.user1.userId}_${r.user2.userId}",
                    name = "${r.user1.name} ❤ ${r.user2.name}",
                    avatar = r.user1.avatar,
                    level = r.level,
                    value = r.points
                )
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                rankings = rankings
            )
            Log.d(TAG, "Loaded ${rankings.size} CP rankings")
        } else {
            _uiState.value = _uiState.value.copy(isLoading = false, rankings = emptyList())
        }
    }
    
    fun dismissError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class RankingUiState(
    val isLoading: Boolean = false,
    val rankings: List<RankingItemData> = emptyList(),
    val myRank: Int = 0,
    val myRankingItem: RankingItemData? = null,
    val error: String? = null
)
