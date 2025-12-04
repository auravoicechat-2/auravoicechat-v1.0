package com.aura.voicechat.ui.guide

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Guide Application Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Application process for becoming a guide (Girls only)
 * Accessible from Me section
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideApplicationScreen(
    onNavigateBack: () -> Unit,
    viewModel: GuideApplicationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var languages by remember { mutableStateOf(listOf<String>()) }
    var experience by remember { mutableStateOf("") }
    var motivation by remember { mutableStateOf("") }
    var availableHours by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Become a Guide") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Groups,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Join Our Guide Team",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "Help users and earn rewards! 💎",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Requirements
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Requirements",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    RequirementItem("Female users only")
                    RequirementItem("Age 18+ years")
                    RequirementItem("Good communication skills")
                    RequirementItem("Minimum 10 hours/week availability")
                    RequirementItem("Friendly and helpful attitude")
                }
            }
            
            // Benefits
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Guide Benefits",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    BenefitItem("💎 Diamond rewards per session")
                    BenefitItem("🎯 Monthly earning targets")
                    BenefitItem("👑 Special guide badge")
                    BenefitItem("⭐ Priority support")
                    BenefitItem("🎁 Exclusive bonuses")
                }
            }
            
            // Application Form
            Text(
                text = "Application Form",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            // Languages
            OutlinedTextField(
                value = languages.joinToString(", "),
                onValueChange = { languages = it.split(",").map { lang -> lang.trim() } },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Languages (comma separated)") },
                placeholder = { Text("English, Spanish, French") }
            )
            
            // Experience
            OutlinedTextField(
                value = experience,
                onValueChange = { experience = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                label = { Text("Previous Experience") },
                placeholder = { Text("Describe your relevant experience...") },
                maxLines = 4
            )
            
            // Motivation
            OutlinedTextField(
                value = motivation,
                onValueChange = { motivation = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                label = { Text("Why do you want to be a guide?") },
                placeholder = { Text("Share your motivation...") },
                maxLines = 6
            )
            
            // Available Hours
            OutlinedTextField(
                value = availableHours,
                onValueChange = { availableHours = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Available Hours per Week") },
                placeholder = { Text("e.g., 15") },
                leadingIcon = {
                    Icon(Icons.Default.Schedule, "Hours")
                }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Submit Button
            Button(
                onClick = {
                    val hours = availableHours.toIntOrNull() ?: 0
                    viewModel.submitApplication(
                        languages = languages,
                        experience = experience,
                        motivation = motivation,
                        availableHours = hours
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = languages.isNotEmpty() && 
                         experience.isNotBlank() && 
                         motivation.isNotBlank() && 
                         availableHours.isNotBlank() &&
                         !state.isSubmitting
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submitting...")
                } else {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit Application")
                }
            }
            
            // Success/Error Messages
            if (state.submitted) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Application Submitted!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = "We'll review your application and get back to you within 24-48 hours.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            if (state.error != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        
                        Text(
                            text = state.error!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RequirementItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun BenefitItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
