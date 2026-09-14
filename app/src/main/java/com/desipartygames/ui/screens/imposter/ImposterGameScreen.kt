package com.desipartygames.ui.screens.imposter

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.desipartygames.core.SoundEffects
import com.desipartygames.data.content.ImposterWordsBank
import com.desipartygames.ui.components.*
import com.desipartygames.ui.theme.*

@Composable
fun ImposterGameScreen(
    viewModel: ImposterViewModel,
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var newPlayerName by rememberSaveable { mutableStateOf("") }
    var showTutorialDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PartyTopBar(
                title = AppStrings.get("game_imposter", language),
                subtitle = "Round ${uiState.roundNumber}",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState.gameState) {
                ImposterGameState.SETUP -> {
                    ImposterSetupView(
                        uiState = uiState,
                        language = language,
                        newPlayerName = newPlayerName,
                        onPlayerNameChanged = { newPlayerName = it },
                        onAddPlayer = {
                            viewModel.addPlayer(newPlayerName)
                            newPlayerName = ""
                        },
                        onRemovePlayer = { viewModel.removePlayer(it) },
                        onSelectCategory = { viewModel.selectCategory(it) },
                        onSetImposterCount = { viewModel.setImposterCount(it) },
                        onStartGame = {
                            SoundEffects.playClick(context)
                            viewModel.startNewGame()
                        }
                    )
                }

                ImposterGameState.PASS_PHONE -> {
                    val currentAssignment = uiState.assignments.getOrNull(uiState.currentPassingIndex)
                    if (currentAssignment != null) {
                        PassPhoneDialog(
                            playerName = currentAssignment.player.name,
                            playerEmoji = currentAssignment.player.avatarEmoji,
                            roleOrSecretTitle = if (currentAssignment.isImposter) "🤫 SECRET STATUS" else "📌 SECRET WORD (${currentAssignment.categoryName})",
                            secretContent = if (currentAssignment.isImposter) "YOU ARE THE IMPOSTER" else currentAssignment.secretWord,
                            secretHint = if (currentAssignment.isImposter) "Blend in! Listen to what others say and act like you know the word." else "Ask clever questions to spot who doesn't know the word!",
                            isImposterOrSpecial = currentAssignment.isImposter,
                            language = language,
                            onDonePassing = { viewModel.nextPassingPlayer() }
                        )
                    }
                }

                ImposterGameState.DISCUSSION -> {
                    ImposterDiscussionView(
                        uiState = uiState,
                        language = language,
                        onStartVoting = {
                            SoundEffects.playClick(context)
                            viewModel.startVotingPhase()
                        }
                    )
                }

                ImposterGameState.VOTING -> {
                    ImposterVotingView(
                        uiState = uiState,
                        language = language,
                        onSelectAccused = { viewModel.selectAccusedPlayer(it) },
                        onConfirmVoting = {
                            SoundEffects.playFanfare(context)
                            viewModel.confirmVotingAndReveal()
                        }
                    )
                }

                ImposterGameState.REVEAL, ImposterGameState.SUMMARY -> {
                    ImposterRevealView(
                        uiState = uiState,
                        language = language,
                        onNextRound = {
                            SoundEffects.playClick(context)
                            viewModel.playNextRound()
                        }
                    )
                }
            }

            // In-App Tutorial Dialog
            if (showTutorialDialog) {
                GameTutorialDialog(
                    initialGameId = "game_imposter",
                    language = language,
                    onDismiss = { showTutorialDialog = false }
                )
            }

            // Scoreboard Modal
            if (uiState.isScoreboardOpen) {
                ScoreBoardDialog(
                    scores = uiState.scores,
                    title = "Imposter Game Scores",
                    language = language,
                    onScoreChanged = { idx, score -> viewModel.updateScore(idx, score) },
                    onDismiss = { viewModel.setScoreboardOpen(false) }
                )
            }
        }
    }
}

@Composable
fun ImposterSetupView(
    uiState: ImposterUiState,
    language: AppLanguage,
    newPlayerName: String,
    onPlayerNameChanged: (String) -> Unit,
    onAddPlayer: () -> Unit,
    onRemovePlayer: (Int) -> Unit,
    onSelectCategory: (com.desipartygames.data.content.ImposterCategory) -> Unit,
    onSetImposterCount: (Int) -> Unit,
    onStartGame: () -> Unit
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text(
                text = "1. Choose Category",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SleekPurple
            )
        }

        items(ImposterWordsBank.categories) { cat ->
            val isSelected = cat.id == uiState.selectedCategory.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        SoundEffects.playClick(context)
                        onSelectCategory(cat)
                    }
                    .border(
                        if (isSelected) 2.dp else 1.dp,
                        if (isSelected) SleekPurple else SleekSurfaceBorder,
                        RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) SleekPurpleContainer else SleekSurfaceCard
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(text = cat.icon, fontSize = 28.sp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.HINDI) cat.titleHindi else cat.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) SleekOnPurpleContainer else SleekTextPrimary
                        )
                        Text(
                            text = "${cat.words.size} authentic Indian words",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSelected) SleekOnPurpleContainer.copy(alpha = 0.8f) else SleekTextSecondary
                        )
                    }
                    if (isSelected) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = SleekPurple)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "2. Number of Imposters",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SleekPurple
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf(1, 2).forEach { count ->
                    val isSelected = uiState.imposterCount == count
                    Button(
                        onClick = {
                            SoundEffects.playClick(context)
                            onSetImposterCount(count)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) SleekPurple else SleekSurfaceElevated,
                            contentColor = if (isSelected) Color.White else SleekTextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (count == 1) "1 Imposter" else "2 Imposters",
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else SleekTextPrimary
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "3. Players (${uiState.players.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SleekPurple
                )
                Text(
                    text = "Min: 3 players",
                    style = MaterialTheme.typography.labelSmall,
                    color = SleekTextSecondary
                )
            }
        }

        // Add player input
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newPlayerName,
                    onValueChange = onPlayerNameChanged,
                    placeholder = { Text("Enter Player Name", color = SleekTextTertiary) },
                    modifier = Modifier.weight(1f).testTag("input_player_name"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SleekPurple,
                        unfocusedBorderColor = SleekSurfaceBorder,
                        focusedTextColor = SleekTextPrimary,
                        unfocusedTextColor = SleekTextPrimary
                    ),
                    singleLine = true
                )

                Button(
                    onClick = onAddPlayer,
                    enabled = newPlayerName.isNotBlank(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SleekPurple,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(54.dp).testTag("btn_add_player")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }

        itemsIndexed(uiState.players) { index, player ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = player.avatarEmoji, fontSize = 20.sp)
                        Text(
                            text = player.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SleekTextPrimary
                        )
                    }

                    if (uiState.players.size > 3) {
                        IconButton(
                            onClick = { onRemovePlayer(index) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Remove", tint = CrimsonRed)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            if (uiState.players.size>=3){
                Button(
                    onClick = onStartGame,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("start_imposter_game_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SleekPurple,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                        Text(
                            text = "Pass Phone & Start Game",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ImposterDiscussionView(
    uiState: ImposterUiState,
    language: AppLanguage,
    onStartVoting: () -> Unit
) {
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
                    text = "🕵️‍♂️ DISCUSSION ROUND",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = SleekOnPurpleContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Text(
                text = "Interrogate Each Other!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = SleekTextPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Category: ${uiState.selectedCategory.title}\nAsk questions around the room without giving away the exact word!",
                style = MaterialTheme.typography.bodyMedium,
                color = SleekTextSecondary,
                textAlign = TextAlign.Center
            )
        }

        PartyCountdownTimer(
            totalSeconds = uiState.discussionSeconds,
            onTimerFinished = onStartVoting
        )

        Button(
            onClick = onStartVoting,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("ready_to_vote_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = SleekPurple,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.HowToVote, contentDescription = null, tint = Color.White)
                Text(
                    text = "Ready to Vote Out Imposter",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ImposterVotingView(
    uiState: ImposterUiState,
    language: AppLanguage,
    onSelectAccused: (Int) -> Unit,
    onConfirmVoting: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "🗳️ Who Is The Imposter?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = SleekPurple,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Discuss as a group and select the player accused by majority vote:",
                style = MaterialTheme.typography.bodyMedium,
                color = SleekTextSecondary,
                textAlign = TextAlign.Center
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(uiState.assignments) { index, assignment ->
                val isSelected = uiState.accusedPlayerIndex == index
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            SoundEffects.playClick(context)
                            onSelectAccused(index)
                        }
                        .border(
                            if (isSelected) 2.dp else 1.dp,
                            if (isSelected) CrimsonRed else SleekSurfaceBorder,
                            RoundedCornerShape(16.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) SleekRoseContainer else SleekSurfaceCard
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(text = assignment.player.avatarEmoji, fontSize = 24.sp)
                            Text(
                                text = assignment.player.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) CrimsonRed else SleekTextPrimary
                            )
                        }

                        if (isSelected) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = CrimsonRed
                            ) {
                                Text(
                                    text = "ACCUSED 🎯",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = onConfirmVoting,
            enabled = uiState.accusedPlayerIndex != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("confirm_vote_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = CrimsonRed,
                disabledContainerColor = SleekSurfaceElevated,
                contentColor = Color.White,
                disabledContentColor = SleekTextTertiary
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Reveal The Truth 💥",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun ImposterRevealView(
    uiState: ImposterUiState,
    language: AppLanguage,
    onNextRound: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "🎭 Round Revelation",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = SleekPurple
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(2.dp, SleekPurple, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "THE SECRET WORD WAS:",
                        style = MaterialTheme.typography.labelSmall,
                        color = SleekTextSecondary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )

                    Text(
                        text = uiState.secretWord,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = SleekPurple,
                        textAlign = TextAlign.Center
                    )

                    HorizontalDivider(color = SleekSurfaceBorder, modifier = Modifier.padding(vertical = 4.dp))

                    Text(
                        text = uiState.roundResultText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SleekTextPrimary,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Text(
                text = "Player Roles:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SleekTextPrimary
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 240.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.assignments) { a ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (a.isImposter) SleekRoseContainer else SleekSurfaceCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (a.isImposter) CrimsonRed else SleekSurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = a.player.avatarEmoji, fontSize = 20.sp)
                                Text(
                                    text = a.player.name,
                                    fontWeight = FontWeight.Bold,
                                    color = if (a.isImposter) CrimsonRed else SleekTextPrimary
                                )
                            }

                            Text(
                                text = if (a.isImposter) "🕵️‍♂️ IMPOSTER" else "😇 INNOCENT",
                                fontWeight = FontWeight.ExtraBold,
                                color = if (a.isImposter) CrimsonRed else EmeraldGreen
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = onNextRound,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("play_next_round_imposter"),
            colors = ButtonDefaults.buttonColors(
                containerColor = SleekPurple,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Play Next Round 🔄",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

