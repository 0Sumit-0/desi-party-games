package com.desipartygames.ui.screens.antakshari

import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desipartygames.core.AppLanguage
import com.desipartygames.core.AppStrings
import com.desipartygames.core.SoundEffects
import com.desipartygames.data.content.AntakshariBank
import com.desipartygames.ui.components.*
import com.desipartygames.ui.theme.*

@Composable
fun AntakshariScreen(
    viewModel: AntakshariViewModel,
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val currentTeam = uiState.teams.getOrNull(uiState.currentTeamIndex)
    var showTutorialDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PartyTopBar(
                title = AppStrings.get("game_antakshari", language),
                subtitle = "Round ${uiState.roundNumber} • Era: ${uiState.selectedEra}",
                showBackButton = true,
                language = language,
                onLanguageSelected = onLanguageSelected,
                onBackClick = onNavigateBack,
                onRulesClick = { showTutorialDialog = true },
                onScoreboardClick = { viewModel.setScoreboardOpen(true) }
            )
        },
        containerColor = SleekBg
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Traditional Opening Shloka Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SleekPurpleContainer),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, SleekPurple)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "🪕 🎤 🎶", fontSize = 24.sp)
                            Text(
                                text = if (language == AppLanguage.HINDI) AntakshariBank.traditionalIntroShloka else AntakshariBank.traditionalIntroShlokaRoman,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SleekOnPurpleContainer,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }

            // Teams Score Summary Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    uiState.teams.forEachIndexed { index, team ->
                        val isTurn = index == uiState.currentTeamIndex
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    if (isTurn) 2.dp else 1.dp,
                                    if (isTurn) SleekPurple else SleekSurfaceBorder,
                                    RoundedCornerShape(16.dp)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isTurn) SleekPurpleContainer else SleekSurfaceCard
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = team.emoji, fontSize = 16.sp)
                                    Text(
                                        text = team.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isTurn) SleekOnPurpleContainer else SleekTextPrimary
                                    )
                                }
                                Text(
                                    text = "${team.score} pts",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isTurn) SleekPurple else SleekTextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Era Filter Selector
            item {
                Text(
                    text = "Era Filter:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = SleekPurple
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    items(AntakshariBank.eras) { era ->
                        val isSelected = uiState.selectedEra == era
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) SleekPurple else SleekSurfaceElevated,
                            modifier = Modifier.clickable {
                                SoundEffects.playClick(context)
                                viewModel.selectEra(era)
                            }
                        ) {
                            Text(
                                text = era,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else SleekTextPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // Main Active Letter Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .border(2.dp, SleekPurple, RoundedCornerShape(24.dp)),
                    colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SleekPurpleContainer
                        ) {
                            Text(
                                text = "NOW SINGING: ${currentTeam?.name?.uppercase() ?: "TEAM"}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = SleekOnPurpleContainer,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                letterSpacing = 1.2.sp
                            )
                        }

                        Text(
                            text = "Sing song starting with:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SleekTextSecondary
                        )

                        // Giant Letter Display
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uiState.currentLetter.devanagari,
                                fontSize = 56.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SleekPurple
                            )
                            Text(
                                text = "(${uiState.currentLetter.roman})",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = SleekPurpleDark
                            )
                        }

                        // Countdown Timer
                        PartyCountdownTimer(
                            totalSeconds = uiState.turnSeconds,
                            onTimerFinished = {
                                SoundEffects.playBuzzer(context)
                            }
                        )

                        // Hint Expander
                        Button(
                            onClick = {
                                SoundEffects.playClick(context)
                                viewModel.toggleHint()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SleekPurpleContainer,
                                contentColor = SleekOnPurpleContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = SleekPurple, modifier = Modifier.size(18.dp))
                                Text(
                                    text = if (uiState.isHintRevealed) "Hide Song Hints" else "Stuck? Need a Song Hint?",
                                    color = SleekOnPurpleContainer,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        AnimatedVisibility(visible = uiState.isHintRevealed) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = SleekSurfaceElevated,
                                border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Sample Classic Songs:",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SleekPurple
                                    )
                                    uiState.currentLetter.sampleSongs.forEach { song ->
                                        Text(
                                            text = "• $song",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = SleekTextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Next Letter or Randomizer Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            SoundEffects.playClick(context)
                            viewModel.generateRandomLetter()
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder)
                    ) {
                        Text("Shuffle Letter 🎲", color = SleekPurple, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Scoring / Result Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            SoundEffects.playBuzzer(context)
                            viewModel.recordTeamResult(0)
                        },
                        modifier = Modifier.weight(1f).height(54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed, contentColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Time's Up (0)", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = {
                            SoundEffects.playSuccess(context)
                            viewModel.recordTeamResult(10)
                        },
                        modifier = Modifier.weight(1f).height(54.dp).testTag("antakshari_correct_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen, contentColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Sang Well (+10)", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = {
                            SoundEffects.playFanfare(context)
                            viewModel.recordTeamResult(20)
                        },
                        modifier = Modifier.weight(1.1f).height(54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SleekPurple, contentColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Golden (+20) 🌟", fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
        }

        // In-App Tutorial Dialog
        if (showTutorialDialog) {
            GameTutorialDialog(
                initialGameId = "game_antakshari",
                language = language,
                onDismiss = { showTutorialDialog = false }
            )
        }

        // Scoreboard Modal
        if (uiState.isScoreboardOpen) {
            ScoreBoardDialog(
                scores = uiState.teams.map { PlayerScore(it.name, it.score, it.emoji) },
                title = "Antakshari Mehfil Scores",
                language = language,
                onDismiss = { viewModel.setScoreboardOpen(false) }
            )
        }
    }
}

