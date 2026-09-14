package com.desipartygames.ui.screens.minigames

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.desipartygames.ui.components.PartyTopBar
import com.desipartygames.ui.theme.*

@Composable
fun WouldYouRatherScreen(
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val prompts = MiniGamesBank.wouldYouRatherPrompts
    var currentIndex by rememberSaveable { mutableIntStateOf(prompts.indices.random()) }
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) } // "A" or "B"
    var roundNumber by rememberSaveable { mutableIntStateOf(1) }

    val currentPrompt = prompts[currentIndex % prompts.size]

    val optAText = when (language) {
        AppLanguage.HINDI -> currentPrompt.optionA_Hi
        AppLanguage.HINGLISH -> currentPrompt.optionA_Hinglish
        AppLanguage.ENGLISH -> currentPrompt.optionA_En
    }

    val optBText = when (language) {
        AppLanguage.HINDI -> currentPrompt.optionB_Hi
        AppLanguage.HINGLISH -> currentPrompt.optionB_Hinglish
        AppLanguage.ENGLISH -> currentPrompt.optionB_En
    }

    val percentageA = remember(currentIndex) { 40 + (currentIndex * 13) % 45 }
    val percentageB = 100 - percentageA

    Scaffold(
        topBar = {
            PartyTopBar(
                title = AppStrings.get("game_would_you_rather", language),
                subtitle = "Dilemma #$roundNumber",
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
            Text(
                text = "Pick Your Choice",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = SleekPurple,
                textAlign = TextAlign.Center
            )

            // Option A Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable {
                        SoundEffects.playClick(context)
                        selectedOption = "A"
                    }
                    .border(
                        if (selectedOption == "A") 2.5.dp else 1.dp,
                        if (selectedOption == "A") SleekPurple else SleekSurfaceBorder,
                        RoundedCornerShape(24.dp)
                    )
                    .testTag("option_a_card"),
                colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (selectedOption == "A") SleekPurpleContainer.copy(alpha = 0.5f) else SleekSurfaceCard
                        )
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
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
                                text = "OPTION A",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = SleekOnPurpleContainer,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                letterSpacing = 1.sp
                            )
                        }

                        Text(
                            text = optAText,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SleekTextPrimary,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp
                        )

                        if (selectedOption != null) {
                            Text(
                                text = "$percentageA% of players picked this",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = SleekPurple
                            )
                        }
                    }
                }
            }

            Text(
                text = "⚡ OR ⚡",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = SleekTextSecondary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Option B Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable {
                        SoundEffects.playClick(context)
                        selectedOption = "B"
                    }
                    .border(
                        if (selectedOption == "B") 2.5.dp else 1.dp,
                        if (selectedOption == "B") SleekRose else SleekSurfaceBorder,
                        RoundedCornerShape(24.dp)
                    )
                    .testTag("option_b_card"),
                colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (selectedOption == "B") SleekRoseContainer.copy(alpha = 0.5f) else SleekSurfaceCard
                        )
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = SleekRose
                        ) {
                            Text(
                                text = "OPTION B",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = SleekOnRose,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                letterSpacing = 1.sp
                            )
                        }

                        Text(
                            text = optBText,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SleekTextPrimary,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp
                        )

                        if (selectedOption != null) {
                            Text(
                                text = "$percentageB% of players picked this",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = SleekPurpleDark
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Next Button
            Button(
                onClick = {
                    SoundEffects.playClick(context)
                    currentIndex = randomDifferentFrom(prompts.indices.toList(), currentIndex)
                    roundNumber += 1
                    selectedOption = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("next_wyr_btn"),
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
                        text = "Next Dilemma",
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
