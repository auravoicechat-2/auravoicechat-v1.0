package com.aura.voicechat.ui.room.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.model.CinemaSession
import com.aura.voicechat.data.repository.CinemaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Cinema Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class CinemaViewModel @Inject constructor(
    private val repository: CinemaRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(CinemaState())
    val state: StateFlow<CinemaState> = _state.asStateFlow()
    
    fun loadCinemaSession(roomId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            repository.getCinemaSession(roomId).fold(
                onSuccess = { session ->
                    _state.value = _state.value.copy(
                        session = session,
                        isLoading = false,
                        isHost = false // TODO: Check if current user is host
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load cinema session"
                    )
                }
            )
        }
    }
    
    fun togglePlayPause() {
        viewModelScope.launch {
            val session = _state.value.session ?: return@launch
            val newPlayingState = !session.isPlaying
            
            repository.syncPlayback(
                roomId = session.roomId,
                currentPosition = session.currentPosition,
                isPlaying = newPlayingState
            ).fold(
                onSuccess = {
                    _state.value = _state.value.copy(
                        session = session.copy(isPlaying = newPlayingState)
                    )
                },
                onFailure = { error ->
                    // Handle error
                }
            )
        }
    }
    
    fun seekTo(position: Long) {
        viewModelScope.launch {
            val session = _state.value.session ?: return@launch
            
            repository.syncPlayback(
                roomId = session.roomId,
                currentPosition = position,
                isPlaying = session.isPlaying
            ).fold(
                onSuccess = {
                    _state.value = _state.value.copy(
                        session = session.copy(currentPosition = position)
                    )
                },
                onFailure = { error ->
                    // Handle error
                }
            )
        }
    }
    
    fun sendReaction(reaction: String) {
        // TODO: Implement reaction sending through Socket.IO or WebSocket
        viewModelScope.launch {
            // Send reaction to server
        }
    }
    
    fun sendMessage(message: String) {
        viewModelScope.launch {
            // TODO: Implement message sending
            val chatMessage = ChatMessage(
                id = System.currentTimeMillis().toString(),
                userId = "current_user", // TODO: Get current user ID
                userName = "You", // TODO: Get current user name
                userAvatar = null,
                content = message,
                timestamp = System.currentTimeMillis()
            )
            
            val currentMessages = _state.value.chatMessages.toMutableList()
            currentMessages.add(chatMessage)
            _state.value = _state.value.copy(chatMessages = currentMessages)
        }
    }
    
    fun stopCinema() {
        viewModelScope.launch {
            val session = _state.value.session ?: return@launch
            repository.stopCinema(session.roomId)
        }
    }
}

data class CinemaState(
    val session: CinemaSession? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isHost: Boolean = false,
    val chatMessages: List<ChatMessage> = emptyList()
)
