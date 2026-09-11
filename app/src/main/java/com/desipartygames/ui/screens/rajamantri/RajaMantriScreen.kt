package com.desipartygames.ui.screens.rajamantri

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
import com.desipartygames.ui.components.*
import com.desipartygames.ui.theme.*

@Composable
fun RajaMantriScreen(
    viewModel: RajaMantriViewModel,
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var newPlayerName by remember { mutableStateOf("") }
    var showTutorialDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PartyTopBar(
                title = AppStrings.get("game_raja_mantri", language),
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
                RajaGameState.SETUP -> {
                    RajaSetupView(
                        uiState = uiState,
                        language = language,
                        newPlayerName = newPlayerName,
                        onPlayerNameChanged = { newPlayerName = it },
                        onAddPlayer = {
                            viewModel.addPlayer(newPlayerName)
                            newPlayerName = ""
                        },
                        onRemovePlayer = { viewModel.removePlayer(it) },
                        onStartGame = {
                            SoundEffects.playChitShuffle(context)
                            viewModel.startChitShuffling()
                        }
                    )
                }

                RajaGameState.PASS_CHITS -> {
                    val currentChit = uiState.chits.getOrNull(uiState.currentPassingIndex)
                    if (currentChit != null) {
                        PassPhoneDialog(
                            playerName = currentChit.player.name,
                            playerEmoji = currentChit.player.avatarEmoji,
                            roleOrSecretTitle = "📜 YOUR ROYAL CHIT",
                            secretContent = "${currentChit.role.emoji} ${currentChit.role.title.uppercase()} (${currentChit.role.basePoints} pts)",
                            secretHint = when (currentChit.role) {
                                RajaRole.RAJA -> "You are Maharaj! When the time comes, ask 'Mera Mantri Kaun?'"
                                RajaRole.MANTRI -> "You are Mantri! Identify yourself when Maharaj asks."
                                RajaRole.SIPAHI -> "You are Sipahi! Keep quiet until you are commanded to catch the Chor."
                                RajaRole.CHOR -> "You are Chor! Keep a straight face and act innocent to escape!"
                                else -> "Keep your identity secret until the court orders!"
                            },
                            isImposterOrSpecial = currentChit.role == RajaRole.CHOR,
                            language = language,
                            onDonePassing = { viewModel.nextChitPass() }
                        )
                    }
                }

                RajaGameState.RAJA_CALL -> {
                    RajaCallCourtView(
                        uiState = uiState,
                        language = language,
                        onProceedToGuess = {
                            SoundEffects.playClick(context)
                            viewModel.proceedToSipahiGuess()
                        }
                    )
                }

                RajaGameState.SIPAHI_GUESS -> {
                    SipahiGuessView(
                        uiState = uiState,
                        language = language,
                        onSelectSuspect = { viewModel.selectSuspectedChor(it) },
                        onConfirmGuess = {
                            SoundEffects.playFanfare(context)
                            viewModel.confirmSipahiGuess()
                        }
                    )
                }

                RajaGameState.REVEAL, RajaGameState.SUMMARY -> {
                    RajaRevealScoreView(
                        uiState = uiState,
                        language = language,
                        onNextRound = {
                            SoundEffects.playChitShuffle(context)
                            viewModel.nextRound()
                        }
                    )
                }
            }

            // In-App Tutorial Dialog
            if (showTutorialDialog) {
                GameTutorialDialog(
                    initialGameId = "game_raja_mantri",
                    language = language,
                    onDismiss = { showTutorialDialog = false }
                )
            }

            // Scoreboard Modal
            if (uiState.isScoreboardOpen) {
                ScoreBoardDialog(
                    scores = uiState.scores,
                    title = "Raja Mantri Ledger",
                    language = language,
                    onScoreChanged = { idx, score -> viewModel.updateScore(idx, score) },
                    onDismiss = { viewModel.setScoreboardOpen(false) }
                )
            }
        }
    }
}

@Composable
fun RajaSetupView(
    uiState: RajaMantriUiState,
    language: AppLanguage,
    newPlayerName: String,
    onPlayerNameChanged: (String) -> Unit,
    onAddPlayer: () -> Unit,
    onRemovePlayer: (Int) -> Unit,
    onStartGame: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SleekPurpleContainer),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SleekPurple)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "👑 📜 🎭 🛡️", fontSize = 32.sp)
                    Text(
                        text = "Raja Mantri Chor Sipahi",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SleekOnPurpleContainer
                    )
                    Text(
                        text = "Classic 4-Player Indian Courtroom Battle! Shuffle digital chits, reveal roles in secret, and catch the thief.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SleekOnPurpleContainer.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Royal Players (${uiState.players.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SleekTextPrimary
                )
                Text(
                    text = "Classic: 4 players (Up to 6)",
                    style = MaterialTheme.typography.labelSmall,
                    color = SleekTextSecondary
                )
            }
        }

        if (uiState.players.size < 6) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newPlayerName,
                        onValueChange = onPlayerNameChanged,
                        placeholder = { Text("Player Name", color = SleekTextTertiary) },
                        modifier = Modifier.weight(1f).testTag("input_raja_player"),
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
                        modifier = Modifier.height(54.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
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
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
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

                    if (uiState.players.size > 4) {
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
            Spacer(modifier = Modifier.height(8.dp))
            if (uiState.players.size>=4){
                Button(
                    onClick = onStartGame,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("shuffle_chits_btn"),
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
                        Text(text = "🎲", fontSize = 20.sp)
                        Text(
                            text = "Shuffle Chits & Pass Phone",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun RajaCallCourtView(
    uiState: RajaMantriUiState,
    language: AppLanguage,
    onProceedToGuess: () -> Unit
) {
    val raja = uiState.chits.getOrNull(uiState.rajaIndex)?.player
    val mantri = uiState.chits.getOrNull(uiState.mantriIndex)?.player

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SleekPurpleContainer
            ) {
                Text(
                    text = "👑 SHAHI DARBAR (COURTROOM)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = SleekOnPurpleContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            // Raja's Dialogue Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SleekPurple),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "👑 Maharaj Speaks:", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.85f), fontWeight = FontWeight.Bold)
                    Text(
                        text = "\"MERA MANTRI KAUN?\"",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Raja (${raja?.name ?: "Maharaj"}) asks who the Mantri is!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // Mantri's Response Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SleekPurple)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "📜 Mantri Speaks:", style = MaterialTheme.typography.labelSmall, color = SleekPurple, fontWeight = FontWeight.Bold)
                    Text(
                        text = "\"MAIN HOON, MAHARAJ!\"",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = SleekPurple,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Mantri (${mantri?.name ?: "Mantri"}) reveals identity to the court!",
                        style = MaterialTheme.typography.bodySmall,
                        color = SleekTextSecondary
                    )
                }
            }

            Text(
                text = "Next: Sipahi will be called to interrogate the remaining players and catch the Chor!",
                style = MaterialTheme.typography.bodyMedium,
                color = SleekTextSecondary,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = onProceedToGuess,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("call_sipahi_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = SleekPurple,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Sipahi: Catch The Chor 🛡️",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun SipahiGuessView(
    uiState: RajaMantriUiState,
    language: AppLanguage,
    onSelectSuspect: (Int) -> Unit,
    onConfirmGuess: () -> Unit
) {
    val context = LocalContext.current
    val sipahi = uiState.chits.getOrNull(uiState.sipahiIndex)?.player

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "🛡️ Sipahi's Interrogation",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = SleekPurple
            )

            Text(
                text = "Sipahi (${sipahi?.name ?: "Sipahi"}), look closely at the remaining players. Which one is the Chor?",
                style = MaterialTheme.typography.bodyMedium,
                color = SleekTextSecondary,
                textAlign = TextAlign.Center
            )
        }

        // List unknown suspects (exclude Raja and Mantri who are already known!)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(uiState.chits) { index, chit ->
                if (index != uiState.rajaIndex && index != uiState.mantriIndex) {
                    val isSelected = uiState.suspectedChorIndex == index
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                SoundEffects.playClick(context)
                                onSelectSuspect(index)
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
                                Text(text = chit.player.avatarEmoji, fontSize = 24.sp)
                                Column {
                                    Text(
                                        text = chit.player.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) CrimsonRed else SleekTextPrimary
                                    )
                                    Text(
                                        text = "Is ${chit.player.name} the Chor?",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SleekTextSecondary
                                    )
                                }
                            }

                            if (isSelected) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = CrimsonRed
                                ) {
                                    Text(
                                        text = "CHOR 🎭",
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
        }

        Button(
            onClick = onConfirmGuess,
            enabled = uiState.suspectedChorIndex != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("confirm_raja_guess_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = CrimsonRed,
                disabledContainerColor = SleekSurfaceElevated,
                contentColor = Color.White,
                disabledContentColor = SleekTextTertiary
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Unfold All Chits & Reveal 📜",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun RajaRevealScoreView(
    uiState: RajaMantriUiState,
    language: AppLanguage,
    onNextRound: () -> Unit
) {
    val isCaught = uiState.isChorCaught

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
                text = if (isCaught) "🎉 CHOR CAUGHT!" else "🏃💨 CHOR ESCAPED!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = if (isCaught) EmeraldGreen else CrimsonRed,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (isCaught)
                    "Sipahi accurately identified the Chor! Sipahi takes 500 pts, Chor gets 0."
                else
                    "Chor fooled the Sipahi! Chor steals 500 pts, Sipahi gets 0.",
                style = MaterialTheme.typography.bodyMedium,
                color = SleekTextPrimary,
                textAlign = TextAlign.Center
            )

            // Unfolded Chits Grid / List
            Text(
                text = "All Chits Revealed:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SleekPurple
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 280.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.chits) { chit ->
                    val earnedPoints = when (chit.role) {
                        RajaRole.RAJA -> 1000
                        RajaRole.MANTRI -> 800
                        RajaRole.RANI -> 900
                        RajaRole.SENAPATI -> 700
                        RajaRole.SIPAHI -> if (isCaught) 500 else 0
                        RajaRole.CHOR -> if (!isCaught) 500 else 0
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (chit.role == RajaRole.RAJA) SleekPurple else SleekSurfaceBorder)
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
                                Text(text = chit.role.emoji, fontSize = 22.sp)
                                Column {
                                    Text(
                                        text = chit.player.name,
                                        fontWeight = FontWeight.Bold,
                                        color = SleekTextPrimary
                                    )
                                    Text(
                                        text = chit.role.title,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SleekTextSecondary
                                    )
                                }
                            }

                            Text(
                                text = "+$earnedPoints pts",
                                fontWeight = FontWeight.ExtraBold,
                                color = if (earnedPoints > 0) EmeraldGreen else CrimsonRed
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
                .testTag("next_raja_round_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = SleekPurple,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Next Chit Round 🔄",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

