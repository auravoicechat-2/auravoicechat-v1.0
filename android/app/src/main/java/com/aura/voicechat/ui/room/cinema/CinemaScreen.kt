package com.aura.voicechat.ui.room.cinema

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage

/**
 * Cinema Screen - Together Watch Mode
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Features:
 * - Video player with ExoPlayer
 * - Synchronized playback across users
 * - Chat overlay
 * - Viewer list
 * - Reaction buttons
 */
@Composable
fun CinemaScreen(
    roomId: String,
    onNavigateBack: () -> Unit,
    viewModel: CinemaViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(roomId) {
        viewModel.loadCinemaSession(roomId)
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            state.error != null -> {
                ErrorContent(
                    error = state.error!!,
                    onRetry = { viewModel.loadCinemaSession(roomId) },
                    onBack = onNavigateBack
                )
            }
            
            state.session != null -> {
                CinemaContent(
                    state = state,
                    onPlayPause = { viewModel.togglePlayPause() },
                    onSeek = { position -> viewModel.seekTo(position) },
                    onReaction = { reaction -> viewModel.sendReaction(reaction) },
                    onSendMessage = { message -> viewModel.sendMessage(message) },
                    onStop = {
                        viewModel.stopCinema()
                        onNavigateBack()
                    },
                    onBack = onNavigateBack
                )
            }
            
            else -> {
                EmptyContent(onBack = onNavigateBack)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CinemaContent(
    state: CinemaState,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onReaction: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onStop: () -> Unit,
    onBack: () -> Unit
) {
    val session = state.session ?: return
    var showChat by remember { mutableStateOf(false) }
    var showViewers by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(session.videoTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showViewers = !showViewers }) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text("${session.viewers.size}")
                        }
                        Icon(Icons.Default.People, "Viewers")
                    }
                    
                    if (state.isHost) {
                        IconButton(onClick = onStop) {
                            Icon(Icons.Default.Stop, "Stop Cinema")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.7f)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Video Player
            VideoPlayer(
                videoUrl = session.videoUrl,
                isPlaying = session.isPlaying,
                currentPosition = session.currentPosition,
                duration = session.videoDuration,
                onPlayPause = onPlayPause,
                onSeek = onSeek,
                modifier = Modifier.fillMaxSize()
            )
            
            // Chat Overlay
            if (showChat) {
                ChatOverlay(
                    messages = state.chatMessages,
                    onSendMessage = onSendMessage,
                    onClose = { showChat = false },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .fillMaxWidth(0.4f)
                        .fillMaxHeight(0.6f)
                )
            }
            
            // Viewers List
            if (showViewers) {
                ViewersListOverlay(
                    viewers = session.viewers,
                    onClose = { showViewers = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .fillMaxWidth(0.3f)
                        .padding(top = 8.dp)
                )
            }
            
            // Reaction Buttons
            ReactionButtons(
                onReaction = onReaction,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            )
            
            // Chat Toggle Button
            IconButton(
                onClick = { showChat = !showChat },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Default.Chat,
                    contentDescription = "Chat",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun ChatOverlay(
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }
    
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.8f),
        shape = RoundedCornerShape(topStart = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Chat",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, "Close", tint = Color.White)
                }
            }
            
            Divider(color = Color.White.copy(alpha = 0.2f))
            
            // Messages
            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = true
            ) {
                items(messages.reversed()) { message ->
                    ChatMessageItem(message)
                }
            }
            
            // Input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.1f)
                    )
                )
                
                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            onSendMessage(messageText)
                            messageText = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Send, "Send", tint = Color.White)
                }
            }
        }
    }
}

@Composable
private fun ChatMessageItem(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        AsyncImage(
            model = message.userAvatar,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Column {
            Text(
                message.userName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ViewersListOverlay(
    viewers: List<ViewerInfo>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.8f),
        shape = RoundedCornerShape(bottomStart = 16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Viewers (${viewers.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, "Close", tint = Color.White)
                }
            }
            
            Divider(color = Color.White.copy(alpha = 0.2f))
            
            LazyColumn {
                items(viewers) { viewer ->
                    ViewerItem(viewer)
                }
            }
        }
    }
}

@Composable
private fun ViewerItem(viewer: ViewerInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = viewer.userAvatar,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            viewer.userName,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}

@Composable
private fun ReactionButtons(
    onReaction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val reactions = listOf("❤️", "😂", "👍", "🎉", "😮", "😢")
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        reactions.forEach { reaction ->
            FloatingActionButton(
                onClick = { onReaction(reaction) },
                modifier = Modifier.size(48.dp),
                containerColor = Color.White.copy(alpha = 0.2f)
            ) {
                Text(reaction, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Error,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            "Error",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Text(
            error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onBack) {
                Text("Go Back")
            }
            
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun EmptyContent(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.VideoLibrary,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            "No Cinema Session",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Text(
            "Start a cinema session to watch together",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = onBack) {
            Text("Go Back")
        }
    }
}

// Data classes for UI state
data class ViewerInfo(
    val userId: String,
    val userName: String,
    val userAvatar: String?
)

data class ChatMessage(
    val id: String,
    val userId: String,
    val userName: String,
    val userAvatar: String?,
    val content: String,
    val timestamp: Long
)
