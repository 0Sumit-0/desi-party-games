package com.desipartygames.ui.screens.charades

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.window.Dialog
import com.desipartygames.core.SoundEffects
import com.desipartygames.data.content.CharadesBank
import com.desipartygames.ui.components.*
import com.desipartygames.ui.theme.*

@Composable
fun CharadesScreen(
    viewModel: CharadesViewModel,
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val currentTeam = uiState.teams.getOrNull(uiState.currentTeamIndex)
    var showTutorialDialog by rememberSaveable { mutableStateOf(false) }
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    var showPromptDetails by rememberSaveable(uiState.currentPrompt?.title) { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PartyTopBar(
                title = AppStrings.get("game_charades", language),
                subtitle = "Round ${uiState.roundNumber} • Team: ${currentTeam?.name ?: ""}",
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
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (uiState.gameState) {
                CharadesGameState.SETUP -> CharadesSetupView(
                    uiState = uiState,
                    onSettingsClick = { showSettingsDialog = true },
                    onRenameTeam = { index, name -> viewModel.renameTeam(index, name) },
                    onStartTurn = {
                        SoundEffects.playClick(context)
                        viewModel.startTurn()
                    }
                )
                CharadesGameState.PASS_TO_ACTOR -> {
                    val prompt = uiState.currentPrompt
                    PassPhoneDialog(
                        playerName = "${currentTeam?.name ?: "Team"}'s Actor",
                        playerEmoji = "🎭",
                        roleOrSecretTitle = "🎬 CHARADES SECRET PROMPT",
                        secretContent = prompt?.title ?: "",
                        secretHint = "Category: ${prompt?.category ?: ""}\nHint: ${prompt?.hints ?: ""}\n⚠️ Do NOT speak or make sounds while acting!",
                        language = language,
                        onDonePassing = { viewModel.startActing() }
                    )
                }
                CharadesGameState.PERFORMING, CharadesGameState.RESULT -> CharadesPerformingView(
                    uiState = uiState,
                    showPromptDetails = showPromptDetails,
                    onTogglePromptDetails = { showPromptDetails = !showPromptDetails },
                    onGuessedCorrect = {
                        SoundEffects.playSuccess(context)
                        viewModel.recordTurnResult(true)
                    },
                    onPassOrTimeout = {
                        SoundEffects.playBuzzer(context)
                        viewModel.recordTurnResult(false)
                    },
                    onToggleCheatSheet = { viewModel.toggleCheatSheet() }
                )
            }

            if (uiState.isCheatSheetOpen) {
                CharadesCheatSheetDialog(onDismiss = { viewModel.toggleCheatSheet() })
            }
            if (showTutorialDialog) {
                GameTutorialDialog(
                    initialGameId = "game_charades",
                    language = language,
                    onDismiss = { showTutorialDialog = false }
                )
            }
            if (uiState.isScoreboardOpen) {
                ScoreBoardDialog(
                    scores = uiState.teams.map { PlayerScore(it.name, it.score, it.emoji) },
                    title = "Charades Battle Board",
                    language = language,
                    onDismiss = { viewModel.setScoreboardOpen(false) }
                )
            }
            if (showSettingsDialog) {
                CharadesSettingsDialog(
                    selectedCategory = uiState.selectedCategory,
                    onSelectCategory = { viewModel.selectCategory(it) },
                    onDismiss = { showSettingsDialog = false }
                )
            }
        }
    }
}

@Composable
fun CharadesSetupView(
    uiState: CharadesUiState,
    onStartTurn: () -> Unit,
    onSettingsClick: () -> Unit,
    onRenameTeam: (Int, String) -> Unit
) {
    val currentTeam = uiState.teams.getOrNull(uiState.currentTeamIndex)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Active Team Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SleekPurpleContainer),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SleekPurple)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = SleekPurple.copy(alpha = 0.2f),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = currentTeam?.emoji ?: "🎭", fontSize = 28.sp)
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "UP NEXT: ${currentTeam?.name ?: "TEAM"}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = SleekOnPurpleContainer
                        )
                        Text(
                            text = "Current Score: ${currentTeam?.score ?: 0} pts",
                            style = MaterialTheme.typography.bodySmall,
                            color = SleekOnPurpleContainer.copy(alpha = 0.8f)
                        )
                    }

                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Game settings", tint = SleekPurple)
                    }
                }
            }
        }

        itemsIndexed(uiState.teams) { index, team ->
            OutlinedTextField(
                value = team.name,
                onValueChange = { onRenameTeam(index, it) },
                label = { Text("Team ${index + 1} name") },
                leadingIcon = { Text(team.emoji, fontSize = 20.sp) },
                modifier = Modifier.fillMaxWidth().testTag("team_name_$index"),
                singleLine = true
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onStartTurn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("start_charades_turn_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SleekPurple,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Pass To Actor & Get Prompt 🎬",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CharadesPerformingView(
    uiState: CharadesUiState,
    showPromptDetails: Boolean,
    onTogglePromptDetails: () -> Unit,
    onGuessedCorrect: () -> Unit,
    onPassOrTimeout: () -> Unit,
    onToggleCheatSheet: () -> Unit
) {
    val prompt = uiState.currentPrompt
    val currentTeam = uiState.teams.getOrNull(uiState.currentTeamIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SleekPurpleContainer
            ) {
                Text(
                    text = "🎭 ACTING IN PROGRESS: ${currentTeam?.name?.uppercase() ?: ""}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = SleekOnPurpleContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Text(
                text = "Actor is gesturing!\nTeam: Guess the title aloud!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = SleekTextPrimary,
                textAlign = TextAlign.Center
            )

            IconButton(onClick = onTogglePromptDetails) {
                Icon(
                    imageVector = if (showPromptDetails) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (showPromptDetails) "Hide prompt details" else "Show prompt details",
                    tint = SleekPurple
                )
            }
            if (showPromptDetails) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SleekSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder)
                ) {
                    Text(
                        text = "${prompt?.category ?: ""} • ${prompt?.hints ?: "1-3 words"}",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = SleekTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            PartyCountdownTimer(
                totalSeconds = uiState.timerSeconds,
                onTimerFinished = onPassOrTimeout
            )

            TextButton(onClick = onToggleCheatSheet) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.HelpOutline, contentDescription = null, tint = SleekPurple)
                    Text("View Gesture Cheat Sheet", color = SleekPurple, fontWeight = FontWeight.Bold)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onPassOrTimeout,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonRed),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CrimsonRed)
            ) {
                Text("Pass / Miss (0 pts)", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onGuessedCorrect,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .testTag("charades_correct_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen, contentColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Guessed It! (+10)", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
private fun CharadesSettingsDialog(
    selectedCategory: com.desipartygames.data.content.CharadesCategory,
    onSelectCategory: (com.desipartygames.data.content.CharadesCategory) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Game Settings",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = SleekTextPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close settings")
                    }
                }
                Text("Choose category", fontWeight = FontWeight.Bold, color = SleekPurple)
                LazyColumn(
                    modifier = Modifier.heightIn(max = 360.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CharadesBank.categories) { category ->
                        val isSelected = category.id == selectedCategory.id
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectCategory(category) },
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) SleekPurpleContainer else SleekSurfaceElevated,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) SleekPurple else SleekSurfaceBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(category.icon, fontSize = 24.sp)
                                Text(
                                    text = category.title,
                                    modifier = Modifier.weight(1f),
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) SleekOnPurpleContainer else SleekTextPrimary
                                )
                                if (isSelected) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = SleekPurple)
                                }
                            }
                        }
                    }
                }
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Done")
                }
            }
        }
    }
}

@Composable
fun CharadesCheatSheetDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("🖐️ Desi Charades Gesture Guide", fontWeight = FontWeight.Bold, color = SleekPurple)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    "☝️ 1/2/3 Fingers" to "Number of words in the title",
                    "🎬 Clapper Hand" to "Movie / Cinema",
                    "📻 Ear Tug" to "'Sounds like...' (rhyming clue)",
                    "🤏 Pinch Fingers" to "Short preposition / small word (ka, ki, se)",
                    "🏏 Bat Swing" to "Cricket match / cricketer clue",
                    "💃 Dance Step" to "Song or item number"
                ).forEach { (gesture, meaning) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = gesture, fontSize = 18.sp)
                        Text(text = meaning, style = MaterialTheme.typography.bodyMedium, color = SleekTextPrimary)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = SleekPurple, contentColor = Color.White)
            ) {
                Text("Got It", color = Color.White)
            }
        },
        containerColor = SleekSurfaceCard
    )
}

