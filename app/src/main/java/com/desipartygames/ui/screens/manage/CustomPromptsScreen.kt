package com.desipartygames.ui.screens.manage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.desipartygames.core.AppLanguage
import com.desipartygames.core.SoundEffects
import com.desipartygames.data.repository.PartyGamesRepository
import com.desipartygames.ui.components.PartyTopBar
import com.desipartygames.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun CustomPromptsScreen(
    repository: PartyGamesRepository,
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val customPrompts by repository.allCustomPrompts.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            PartyTopBar(
                title = "Custom Prompts",
                subtitle = "${customPrompts.size} custom items saved",
                showBackButton = true,
                language = language,
                onLanguageSelected = onLanguageSelected,
                onBackClick = onNavigateBack
            )
        },
        containerColor = SleekBg
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "💡 Offline Prompt Bank",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SleekPurple
                        )
                        Text(
                            text = "All custom Truths, Dares, and Game Prompts you create are safely saved in your device's local database.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SleekTextSecondary
                        )
                    }
                }
            }

            if (customPrompts.isEmpty()) {
                item {
                    Text(
                        text = "No custom prompts added yet. Add custom questions during Truth or Dare to see them here!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SleekTextTertiary,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            } else {
                items(customPrompts) { prompt ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (prompt.gameType == "DARE") SleekRose else SleekPurpleContainer
                                    ) {
                                        Text(
                                            text = prompt.gameType,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (prompt.gameType == "DARE") SleekOnRose else SleekOnPurpleContainer,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    Text(
                                        text = "• ${prompt.category}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SleekTextSecondary
                                    )
                                }

                                Text(
                                    text = prompt.content,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SleekTextPrimary
                                )
                            }

                            IconButton(
                                onClick = {
                                    SoundEffects.playClick(context)
                                    coroutineScope.launch {
                                        repository.removeCustomPrompt(prompt)
                                    }
                                }
                            ) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = CrimsonRed)
                            }
                        }
                    }
                }
            }
        }
    }
}

