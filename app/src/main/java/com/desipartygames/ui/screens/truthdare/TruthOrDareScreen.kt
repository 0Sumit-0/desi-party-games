package com.desipartygames.ui.screens.truthdare

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.desipartygames.core.AppLanguage
import com.desipartygames.core.AppStrings
import com.desipartygames.core.GameRules
import com.desipartygames.core.SoundEffects
import com.desipartygames.ui.components.*
import com.desipartygames.ui.theme.*

@Composable
fun TruthOrDareScreen(
    viewModel: TruthDareViewModel,
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showTutorialDialog by rememberSaveable { mutableStateOf(false) }
    var showMinimumPlayersDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PartyTopBar(
                title = AppStrings.get("game_truth_dare", language),
                subtitle = "Category: ${uiState.selectedCategory}",
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
                TruthDareState.SPIN_BOTTLE -> {
                    BottleSpinView(
                        uiState = uiState,
                        language = language,
                        onSelectCategory = { cat -> viewModel.selectCategory(cat) },
                        onSpinBottle = {
                            if (uiState.players.size < GameRules.minimumPlayers(GameRules.TRUTH_DARE)) {
                                showMinimumPlayersDialog = true
                            } else {
                                SoundEffects.playFanfare(context)
                                viewModel.spinBottle()
                            }
                        },
                        onSpinAnimationFinished = { viewModel.onBottleSpinFinished() },
                        onOpenCustomPromptDialog = { viewModel.setAddPromptDialogOpen(true) }
                    )
                }

                TruthDareState.CHOOSE_TRUTH_OR_DARE -> {
                    val chosenPlayer = uiState.players.getOrNull(uiState.selectedPlayerIndex)
                    ChooseTruthOrDareView(
                        playerName = chosenPlayer?.name ?: "Player",
                        playerEmoji = chosenPlayer?.avatarEmoji ?: "🍾",
                        language = language,
                        onChoose = { choice ->
                            SoundEffects.playClick(context)
                            viewModel.chooseTruthOrDare(choice)
                        }
                    )
                }

                TruthDareState.SHOW_PROMPT, TruthDareState.SUMMARY -> {
                    val chosenPlayer = uiState.players.getOrNull(uiState.selectedPlayerIndex)
                    ShowPromptView(
                        uiState = uiState,
                        playerName = chosenPlayer?.name ?: "Player",
                        playerEmoji = chosenPlayer?.avatarEmoji ?: "🎭",
                        language = language,
                        onComplete = {
                            SoundEffects.playSuccess(context)
                            viewModel.completeChallenge(10)
                        },
                        onForfeit = {
                            SoundEffects.playBuzzer(context)
                            viewModel.forfeitChallenge()
                        }
                    )
                }
            }

            // Add Custom Prompt Dialog
            if (uiState.isAddPromptDialogOpen) {
                AddCustomPromptDialog(
                    onDismiss = { viewModel.setAddPromptDialogOpen(false) },
                    onSave = { type, cat, text ->
                        viewModel.saveCustomPrompt(type, cat, text)
                        viewModel.setAddPromptDialogOpen(false)
                    }
                )
            }

            // In-App Tutorial Dialog
            if (showTutorialDialog) {
                GameTutorialDialog(
                    initialGameId = "game_truth_dare",
                    language = language,
                    onDismiss = { showTutorialDialog = false }
                )
            }

            if (showMinimumPlayersDialog) {
                AlertDialog(
                    onDismissRequest = { showMinimumPlayersDialog = false },
                    title = { Text("Not enough players") },
                    text = { Text(GameRules.minimumPlayersMessage(GameRules.TRUTH_DARE)) },
                    confirmButton = {
                        TextButton(onClick = { showMinimumPlayersDialog = false }) { Text("OK") }
                    }
                )
            }

            // Scoreboard Modal
            if (uiState.isScoreboardOpen) {
                ScoreBoardDialog(
                    scores = uiState.scores,
                    title = "Truth or Dare Tally",
                    language = language,
                    onScoreChanged = { idx, score -> viewModel.updateScore(idx, score) },
                    onDismiss = { viewModel.setScoreboardOpen(false) }
                )
            }
        }
    }
}

@Composable
fun BottleSpinView(
    uiState: TruthDareUiState,
    language: AppLanguage,
    onSelectCategory: (String) -> Unit,
    onSpinBottle: () -> Unit,
    onSpinAnimationFinished: () -> Unit,
    onOpenCustomPromptDialog: () -> Unit
) {
    val animatedAngle by animateFloatAsState(
        targetValue = uiState.bottleRotationAngle,
        animationSpec = tween(
            durationMillis = if (uiState.isSpinning) 2400 else 0,
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            if (uiState.isSpinning) {
                onSpinAnimationFinished()
            }
        },
        label = "bottleSpin"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Category Selector Chips
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Choose Pack:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = SleekPurple
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Triple("FAMILY", "👨‍👩‍👧‍👦 Family", EmeraldGreen),
                    Triple("FRIENDS", "🎉 Friends", SleekPurple)
                ).forEach { (key, label, color) ->
                    val isSelected = uiState.selectedCategory == key
                    Button(
                        onClick = { onSelectCategory(key) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) color else SleekSurfaceElevated,
                            contentColor = if (isSelected) Color.White else SleekTextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else SleekTextPrimary
                        )
                    }
                }
            }
        }

        // Bottle Area surrounded by Player Avatars Circle
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(CircleShape)
                .background(SleekSurfaceCard)
                .border(2.dp, SleekPurple.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Center Spinning Bottle background circle
            Surface(
                shape = CircleShape,
                color = SleekPurpleContainer,
                modifier = Modifier.size(100.dp)
            ) {}

            Column(
                modifier = Modifier
                    .rotate(animatedAngle)
                    .fillMaxHeight(0.75f)
                    .width(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Bottle Neck & Cork
                Surface(
                    shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp),
                    color = SleekPurpleDark,
                    modifier = Modifier.size(12.dp, 20.dp)
                ) {}
                // Bottle Body
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SleekPurple,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🍾", fontSize = 22.sp)
                    }
                }
            }
        }

        // Bottom Controls
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onSpinBottle,
                enabled = !uiState.isSpinning,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("spin_bottle_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SleekPurple,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (uiState.isSpinning) "Spinning... 🌀" else "Spin The Bottle 🍾",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            OutlinedButton(
                onClick = onOpenCustomPromptDialog,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = SleekPurple)
                    Text("Add Custom Truth/Dare", color = SleekPurple, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun ChooseTruthOrDareView(
    playerName: String,
    playerEmoji: String,
    language: AppLanguage,
    onChoose: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(2.dp, SleekPurple, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = SleekPurpleContainer,
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = playerEmoji, fontSize = 40.sp)
                    }
                }

                Text(
                    text = "Bottle Pointed At:\n$playerName",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = SleekPurple,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Make your choice, $playerName:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SleekTextSecondary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Button(
                        onClick = { onChoose("TRUTH") },
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .testTag("choose_truth_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = SleekPurple, contentColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("😇 TRUTH", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.White)
                    }

                    Button(
                        onClick = { onChoose("DARE") },
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .testTag("choose_dare_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = RaniPink, contentColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("🔥 DARE", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ShowPromptView(
    uiState: TruthDareUiState,
    playerName: String,
    playerEmoji: String,
    language: AppLanguage,
    onComplete: () -> Unit,
    onForfeit: () -> Unit
) {
    val prompt = uiState.currentPrompt
    val isDare = uiState.currentPromptType == "DARE"
    val promptText = when (language) {
        AppLanguage.HINDI -> prompt?.textHi ?: prompt?.textEn ?: ""
        AppLanguage.HINGLISH -> prompt?.textHinglish ?: prompt?.textEn ?: ""
        AppLanguage.ENGLISH -> prompt?.textEn ?: ""
    }

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
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isDare) SleekRoseContainer else SleekPurpleContainer
            ) {
                Text(
                    text = if (isDare) "🔥 DARE FOR $playerName" else "😇 TRUTH FOR $playerName",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isDare) CrimsonRed else SleekOnPurpleContainer,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(2.dp, if (isDare) RaniPink else SleekPurple, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = promptText,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = SleekTextPrimary,
                        textAlign = TextAlign.Center,
                        lineHeight = 30.sp
                    )
                }
            }

            if (isDare) {
                Text(
                    text = "Dare Timer:",
                    style = MaterialTheme.typography.labelMedium,
                    color = SleekTextSecondary
                )
                PartyCountdownTimer(totalSeconds = 45)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onForfeit,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonRed),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CrimsonRed)
            ) {
                Text("Forfeit (0 pts)", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onComplete,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .testTag("completed_prompt_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen, contentColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Completed! (+10)", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun AddCustomPromptDialog(
    onDismiss: () -> Unit,
    onSave: (type: String, category: String, text: String) -> Unit
) {
    var promptType by rememberSaveable { mutableStateOf("TRUTH") }
    var category by rememberSaveable { mutableStateOf("FRIENDS") }
    var text by rememberSaveable { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(2.dp, SleekPurple, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "✨ Add Custom Prompt",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SleekPurple
                )

                // Type
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("TRUTH", "DARE").forEach { t ->
                        val isSel = promptType == t
                        Button(
                            onClick = { promptType = t },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSel) (if (t == "TRUTH") SleekPurple else RaniPink) else SleekSurfaceElevated,
                                contentColor = if (isSel) Color.White else SleekTextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(t, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Category
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("FAMILY" to "Family", "FRIENDS" to "Friends").forEach { (c, lbl) ->
                        val isSel = category == c
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) SleekPurple else SleekSurfaceElevated,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { category = c }
                                .padding(2.dp)
                        ) {
                            Text(
                                text = lbl,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = TextAlign.Center,
                                color = if (isSel) Color.White else SleekTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Write your custom question or dare...", color = SleekTextTertiary) },
                    modifier = Modifier.fillMaxWidth().height(110.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SleekPurple,
                        unfocusedBorderColor = SleekSurfaceBorder,
                        focusedTextColor = SleekTextPrimary,
                        unfocusedTextColor = SleekTextPrimary
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder)
                    ) {
                        Text("Cancel", color = SleekTextSecondary)
                    }
                    Button(
                        onClick = { onSave(promptType, category, text) },
                        enabled = text.isNotBlank(),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = SleekPurple, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

