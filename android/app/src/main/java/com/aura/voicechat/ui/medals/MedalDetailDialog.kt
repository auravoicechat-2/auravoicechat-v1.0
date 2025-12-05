package com.aura.voicechat.ui.medals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage

/**
 * Medal Detail Dialog
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Shows detailed information about a medal
 */
@Composable
fun MedalDetailDialog(
    medal: MedalItem,
    onDismiss: () -> Unit,
    onEquip: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Close button
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Default.Close, "Close")
                    }
                }
                
                // Medal Image
                AsyncImage(
                    model = medal.iconUrl,
                    contentDescription = medal.name,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )
                
                // Medal Name
                Text(
                    text = medal.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Rarity Badge
                RarityBadge(rarity = medal.rarity)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Description
                Text(
                    text = medal.description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // How to Earn
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "How to Earn",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = medal.howToEarn,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Progress (if not unlocked)
                if (!medal.isUnlocked && medal.progress != null && medal.progressTarget != null) {
                    ProgressSection(
                        progress = medal.progress,
                        target = medal.progressTarget
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Status / Action Button
                if (medal.isUnlocked) {
                    Button(
                        onClick = onEquip,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Equip Medal")
                    }
                } else {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
private fun RarityBadge(rarity: String) {
    val (backgroundColor, textColor) = when (rarity.uppercase()) {
        "LEGENDARY" -> Color(0xFFFFD700) to Color.Black
        "EPIC" -> Color(0xFF9C27B0) to Color.White
        "RARE" -> Color(0xFF2196F3) to Color.White
        else -> Color(0xFF757575) to Color.White
    }
    
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = rarity.uppercase(),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ProgressSection(
    progress: Float,
    target: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Progress",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "${(progress * 100).toInt()}% (${(progress * target).toInt()}/$target)",
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

// Data class for Medal UI
data class MedalItem(
    val id: String,
    val name: String,
    val description: String,
    val iconUrl: String,
    val category: String,
    val rarity: String,
    val isUnlocked: Boolean,
    val progress: Float?,
    val progressTarget: Int?,
    val howToEarn: String
)
