package com.aura.voicechat.ui.earning

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.NumberFormat
import java.util.Locale

/**
 * Earning Target Sheet Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Shows earning targets and rules for users and guides
 * Target-based earning system: Receive diamonds → Complete targets → Withdraw cash
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarningTargetSheetScreen(
    isGuide: Boolean = false,
    onNavigateBack: () -> Unit,
    viewModel: EarningTargetViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(isGuide) {
        viewModel.loadTargets(isGuide)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isGuide) "Guide Earning Targets" else "Earning Targets") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.EmojiEvents,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = Color(0xFFFFD700) // Gold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = if (isGuide) "Guide Earning System" else "Earning System",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        
                        Text(
                            text = "Receive Diamonds → Complete Targets → Earn Cash 💰",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            
            // Current Progress (if applicable)
            if (state.currentProgress != null) {
                item {
                    CurrentProgressCard(state.currentProgress!!)
                }
            }
            
            // Rules
            item {
                RulesCard(isGuide)
            }
            
            // Targets
            item {
                Text(
                    text = "Monthly Targets",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(state.targets) { target ->
                TargetCard(target)
            }
            
            // Payment Information
            item {
                PaymentInfoCard(isGuide)
            }
            
            // Important Notes
            item {
                ImportantNotesCard()
            }
        }
    }
}

@Composable
private fun CurrentProgressCard(progress: EarningProgress) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
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
            
            // Progress bar
            LinearProgressIndicator(
                progress = { progress.progressPercentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
                color = MaterialTheme.colorScheme.primary,
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${formatNumber(progress.currentDiamonds)} 💎",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "${formatNumber(progress.targetDiamonds)} 💎",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "${progress.progressPercentage}% Complete",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun RulesCard(isGuide: Boolean) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "How It Works",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            RuleItem(
                number = "1",
                text = "Receive diamonds from ${if (isGuide) "users during guide sessions" else "other users (gifts, calls, etc.)"}"
            )
            
            RuleItem(
                number = "2",
                text = "Accumulate diamonds to reach monthly targets"
            )
            
            RuleItem(
                number = "3",
                text = "Request cashout when target is achieved"
            )
            
            RuleItem(
                number = "4",
                text = "Wait 5 days for clearance period"
            )
            
            RuleItem(
                number = "5",
                text = "Owner approves → Cash is sent to your account"
            )
            
            if (isGuide) {
                RuleItem(
                    number = "6",
                    text = "Meet session targets for bonus rewards"
                )
            }
        }
    }
}

@Composable
private fun RuleItem(number: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primary
        ) {
            Text(
                text = number,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TargetCard(target: TargetItem) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = target.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "Tier ${target.tier}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    Text(
                        text = "$${formatMoney(target.cashReward)}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00C853) // Green
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Divider()
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoColumn(
                    label = "Required Diamonds",
                    value = "${formatNumber(target.requiredDiamonds)} 💎"
                )
                
                if (target.bonusCoins != null && target.bonusCoins > 0) {
                    InfoColumn(
                        label = "Bonus Coins",
                        value = "${formatNumber(target.bonusCoins)} 🪙"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Duration: ${target.duration}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun InfoColumn(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PaymentInfoCard(isGuide: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Payment Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            PaymentInfoRow("Min. Cashout", "$10.00")
            PaymentInfoRow("Clearance Period", "5 days")
            PaymentInfoRow("Approval", "Owner approval required")
            PaymentInfoRow("Conversion Rate", "100,000 💎 = $30")
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Payment Methods",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "• Bank Transfer\n• PayPal\n• Wise\n• Mobile Money",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun PaymentInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ImportantNotesCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                
                Text(
                    text = "Important Notes",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "• Earnings based ONLY on diamonds received, not sent\n" +
                       "• No streak-based earnings\n" +
                       "• No other earning methods - only target completion\n" +
                       "• All cashout requests require 5-day clearance\n" +
                       "• Final approval by owner required\n" +
                       "• Targets reset monthly\n" +
                       "• Can exchange diamonds to coins anytime",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun formatNumber(value: Long): String {
    return NumberFormat.getNumberInstance(Locale.US).format(value)
}

private fun formatMoney(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(value).removePrefix("$")
}

// Data classes for UI
data class TargetItem(
    val tier: Int,
    val name: String,
    val requiredDiamonds: Long,
    val cashReward: Double,
    val bonusCoins: Long?,
    val duration: String
)

data class EarningProgress(
    val currentDiamonds: Long,
    val targetDiamonds: Long,
    val progressPercentage: Int
)
