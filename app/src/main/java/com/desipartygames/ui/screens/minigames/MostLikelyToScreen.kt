package com.desipartygames.ui.screens.minigames

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import com.desipartygames.core.randomDifferentFrom
import com.desipartygames.data.content.MiniGamesBank
import com.desipartygames.data.local.entity.PlayerEntity
import com.desipartygames.ui.components.PartyTopBar
import com.desipartygames.ui.theme.*

@Composable
fun MostLikelyToScreen(
    players: List<PlayerEntity>,
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val prompts = MiniGamesBank.mostLikelyPrompts
    var currentIndex by rememberSaveable { mutableIntStateOf((prompts.indices).random()) }
    var selectedPlayerIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    var roundNumber by rememberSaveable { mutableIntStateOf(1) }

    val currentPrompt = prompts[currentIndex % prompts.size]

    val promptText = when (language) {
        AppLanguage.HINDI -> currentPrompt.textHi
        AppLanguage.HINGLISH -> currentPrompt.textHinglish
        AppLanguage.ENGLISH -> currentPrompt.textEn
    }

    Scaffold(
        topBar = {
            PartyTopBar(
                title = AppStrings.get("game_most_likely", language),
                subtitle = "Round $roundNumber",
                showBackButton = true,
                language = language,
                onLanguageSelected = onLanguageSelected,
                onBackClick = onNavigateBack
            )
        },
        containerColor = SleekBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Prompt Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, SleekSurfaceBorder, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = SleekPurpleContainer
                        ) {
                                Text(
                                    text = "👉 WHO IS MOST LIKELY TO...",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = SleekOnPurpleContainer,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                letterSpacing = 1.sp
                            )
                        }

                        Text(
                            text = promptText,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = SleekTextPrimary,
                            textAlign = TextAlign.Center,
                            lineHeight = 32.sp
                        )
                    }
                }
            }

            // Players List for Voting
            Text(
                text = "Point your finger or tap the chosen player:",
                style = MaterialTheme.typography.bodyMedium,
                color = SleekTextSecondary
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(players) { index, player ->
                    val isSelected = selectedPlayerIndex == index
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                SoundEffects.playClick(context)
                                selectedPlayerIndex = index
                            }
                            .border(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) SleekPurple else SleekSurfaceBorder,
                                RoundedCornerShape(16.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) SleekPurpleContainer.copy(alpha = 0.5f) else SleekSurfaceCard
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = SleekPurpleContainer,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = player.avatarEmoji, fontSize = 20.sp)
                                    }
                                }
                                Text(
                                    text = player.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekTextPrimary
                                )
                            }

                            if (isSelected) {
                                Surface(
                                    shape = RoundedCornerShape(100.dp),
                                    color = SleekPurple
                                ) {
                                    Text(
                                        text = "VOTED 🎯",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Next Scenario Button
            Button(
                onClick = {
                    SoundEffects.playClick(context)
                    currentIndex = randomDifferentFrom(prompts.indices.toList(), currentIndex)
                    roundNumber += 1
                    selectedPlayerIndex = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("next_most_likely_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SleekPurple,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(100.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Next Scenario",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}
