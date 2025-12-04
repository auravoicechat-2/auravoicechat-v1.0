package com.aura.voicechat.ui.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*

/**
 * Event Detail Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Shows detailed information about an event
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    onNavigateBack: () -> Unit,
    viewModel: EventDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(eventId) {
        viewModel.loadEventDetails(eventId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.event?.title ?: "Event Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                state.error != null -> {
                    ErrorContent(
                        error = state.error!!,
                        onRetry = { viewModel.loadEventDetails(eventId) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                state.event != null -> {
                    EventDetailContent(
                        event = state.event!!,
                        onParticipate = { viewModel.participateEvent() },
                        onClaimReward = { rewardId -> viewModel.claimReward(rewardId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EventDetailContent(
    event: EventDetail,
    onParticipate: () -> Unit,
    onClaimReward: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner
        item {
            AsyncImage(
                model = event.bannerUrl,
                contentDescription = event.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
        
        // Event Info
        item {
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    StatusBadge(status = event.status)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Time Info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TimeInfo(
                            label = "Start",
                            timestamp = event.startTime
                        )
                        
                        TimeInfo(
                            label = "End",
                            timestamp = event.endTime
                        )
                    }
                }
            }
        }
        
        // Progress (if participating)
        if (event.userProgress != null) {
            item {
                ProgressCard(progress = event.userProgress)
            }
        }
        
        // Rewards
        item {
            Text(
                text = "Rewards",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(event.rewards) { reward ->
            RewardItem(
                reward = reward,
                canClaim = false, // TODO: Determine if reward can be claimed
                onClaim = { onClaimReward(reward.id) }
            )
        }
        
        // Rules
        item {
            Text(
                text = "Event Rules",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(event.rules) { rule ->
            RuleItem(rule = rule)
        }
        
        // Participate Button
        if (!event.isParticipating && event.status == "ACTIVE") {
            item {
                Button(
                    onClick = onParticipate,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Participate in Event")
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (color, text) = when (status) {
        "ACTIVE" -> MaterialTheme.colorScheme.primary to "Active"
        "UPCOMING" -> MaterialTheme.colorScheme.tertiary to "Upcoming"
        "ENDED" -> MaterialTheme.colorScheme.surfaceVariant to "Ended"
        else -> MaterialTheme.colorScheme.surfaceVariant to status
    }
    
    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TimeInfo(label: String, timestamp: Long) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        
        Text(
            text = formatDateTime(timestamp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ProgressCard(progress: EventProgress) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Your Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${progress.currentValue} / ${progress.targetValue}",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Text(
                    text = "${(progress.currentValue.toFloat() / progress.targetValue * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = { progress.currentValue.toFloat() / progress.targetValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
            )
        }
    }
}

@Composable
private fun RewardItem(
    reward: EventReward,
    canClaim: Boolean,
    onClaim: () -> Unit
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = reward.iconUrl,
                contentDescription = reward.name,
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reward.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "${reward.value} ${reward.type}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            if (canClaim) {
                Button(onClick = onClaim) {
                    Text("Claim")
                }
            }
        }
    }
}

@Composable
private fun RuleItem(rule: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "•",
            modifier = Modifier.padding(end = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        
        Text(
            text = rule,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Error,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

private fun formatDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

// Data classes for UI
data class EventDetail(
    val id: String,
    val title: String,
    val description: String,
    val bannerUrl: String,
    val startTime: Long,
    val endTime: Long,
    val type: String,
    val status: String,
    val rewards: List<EventReward>,
    val rules: List<String>,
    val userProgress: EventProgress?,
    val isParticipating: Boolean
)

data class EventReward(
    val id: String,
    val name: String,
    val iconUrl: String,
    val value: Long,
    val type: String
)

data class EventProgress(
    val currentValue: Int,
    val targetValue: Int,
    val rewardsEarned: List<String>
)
