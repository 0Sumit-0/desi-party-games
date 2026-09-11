package com.desipartygames.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desipartygames.core.AppLanguage
import com.desipartygames.core.AppStrings
import com.desipartygames.core.SoundEffects
import com.desipartygames.ui.components.GameTutorialDialog
import com.desipartygames.ui.components.PartyTopBar
import com.desipartygames.ui.theme.*

data class GameCardItem(
    val id: String,
    val titleKey: String,
    val descKey: String,
    val emoji: String,
    val playersBadge: String,
    val tag: String,
    val route: String
)

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onLanguageSelected: (AppLanguage) -> Unit,
    onGroupSelected: (String) -> Unit,
    onNavigateToGame: (String) -> Unit,
    onManagePlayers: () -> Unit,
    onManagePrompts: () -> Unit
) {
    val context = LocalContext.current
    val language = uiState.language
    var showTutorialDialog by remember { mutableStateOf(false) }
    var initialTutorialGameId by remember { mutableStateOf("game_imposter") }

    val gamesList = listOf(
        GameCardItem(
            id = "imposter",
            titleKey = "game_imposter",
            descKey = "imposter_desc",
            emoji = "🕵️‍♂️",
            playersBadge = "3-12 Players",
            tag = "BLUFF & SPY",
            route = "game_imposter"
        ),
        GameCardItem(
            id = "raja_mantri",
            titleKey = "game_raja_mantri",
            descKey = "raja_mantri_desc",
            emoji = "👑",
            playersBadge = "4-6 Players",
            tag = "ROYAL CHITS",
            route = "game_raja_mantri"
        ),
        GameCardItem(
            id = "truth_dare",
            titleKey = "game_truth_dare",
            descKey = "truth_dare_desc",
            emoji = "🍾",
            playersBadge = "2-15 Players",
            tag = "SPIN BOTTLE",
            route = "game_truth_dare"
        ),
        GameCardItem(
            id = "antakshari",
            titleKey = "game_antakshari",
            descKey = "antakshari_desc",
            emoji = "🎤",
            playersBadge = "2-4 Teams",
            tag = "MUSIC BATTLE",
            route = "game_antakshari"
        ),
        GameCardItem(
            id = "charades",
            titleKey = "game_charades",
            descKey = "charades_desc",
            emoji = "🎭",
            playersBadge = "2+ Teams",
            tag = "BOLLYWOOD ACTING",
            route = "game_charades"
        ),
        // Mini games
        GameCardItem(
            id = "most_likely",
            titleKey = "game_most_likely",
            descKey = "game_most_likely",
            emoji = "👉",
            playersBadge = "3+ Players",
            tag = "DESI ROAST",
            route = "game_most_likely"
        ),
        GameCardItem(
            id = "would_you_rather",
            titleKey = "game_would_you_rather",
            descKey = "game_would_you_rather",
            emoji = "🤔",
            playersBadge = "2+ Players",
            tag = "DESI DILEMMAS",
            route = "game_would_you_rather"
        ),
        GameCardItem(
            id = "pictionary",
            titleKey = "game_pictionary",
            descKey = "game_pictionary",
            emoji = "🎨",
            playersBadge = "Offline Canvas",
            tag = "DOODLE GUESS",
            route = "game_pictionary"
        )
    )

    Scaffold(
        topBar = {
            PartyTopBar(
                title = AppStrings.get("app_title", language),
                subtitle = AppStrings.get("app_subtitle", language),
                showBackButton = false,
                language = language,
                onLanguageSelected = onLanguageSelected,
                onRulesClick = {
                    initialTutorialGameId = "game_imposter"
                    showTutorialDialog = true
                }
            )
        },
        containerColor = SleekBg
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // Sleek Hero Card with High Contrast
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(28.dp)),
                        colors = CardDefaults.cardColors(containerColor = SleekPurple),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            Color(0xFF6750A4),
                                            Color(0xFF4F378A)
                                        )
                                    )
                                )
                                .padding(22.dp)
                        ) {
                            // Background watermark emoji
                            Text(
                                text = "👑",
                                fontSize = 90.sp,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(x = 16.dp, y = 16.dp),
                                color = Color.White.copy(alpha = 0.15f)
                            )

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(100.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "CLASSIC HIT • PASS & PLAY",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                                        letterSpacing = 1.sp
                                    )
                                }

                                Text(
                                    text = "Raja Mantri Chor Sipahi",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Text(
                                    text = "Pass the phone to secretly assign royal roles. Who is the Chor?",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.95f),
                                    modifier = Modifier.fillMaxWidth(0.85f)
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            SoundEffects.playClick(context)
                                            onNavigateToGame("game_raja_mantri")
                                        },
                                        shape = RoundedCornerShape(100.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.White,
                                            contentColor = SleekPurple
                                        ),
                                        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp),
                                        modifier = Modifier.testTag("hero_start_game_btn")
                                    ) {
                                        Text(
                                            text = "Play Now 👑",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = SleekPurple
                                        )
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            SoundEffects.playClick(context)
                                            initialTutorialGameId = "game_raja_mantri"
                                            showTutorialDialog = true
                                        },
                                        shape = RoundedCornerShape(100.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                                    ) {
                                        Icon(Icons.Default.HelpOutline, contentDescription = "Tutorial", modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Tutorial", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // In-App Tutorial Hub Banner Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                SoundEffects.playClick(context)
                                initialTutorialGameId = "game_imposter"
                                showTutorialDialog = true
                            }
                            .border(1.5.dp, SleekBorderLight, RoundedCornerShape(20.dp)),
                        colors = CardDefaults.cardColors(containerColor = SleekPurpleContainer)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = SleekPurple,
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = "📖", fontSize = 22.sp)
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Game Rules & In-App Tutorials",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekOnPurpleContainer
                                )
                                Text(
                                    text = "Learn how to play all 8 games step-by-step with pro-tips",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SleekOnPurpleContainer.copy(alpha = 0.85f)
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Open Tutorials",
                                tint = SleekPurple
                            )
                        }
                    }
                }

                // Active Gang / Group Selector & Quick Players preview
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = SleekPurpleContainer,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(text = "👥", fontSize = 16.sp)
                                        }
                                    }
                                    Column {
                                        Text(
                                            text = "Active Group",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SleekTextSecondary
                                        )
                                        Text(
                                            text = uiState.selectedGroup,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = SleekTextPrimary
                                        )
                                    }
                                }

                                TextButton(
                                    onClick = {
                                        SoundEffects.playClick(context)
                                        onManagePlayers()
                                    }
                                ) {
                                    Text(
                                        text = "Edit Players",
                                        color = SleekPurple,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }

                            // Players avatar chips row
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                items(uiState.playersInGroup) { player ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = SleekSurfaceElevated,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(text = player.avatarEmoji, fontSize = 16.sp)
                                            Text(
                                                text = player.name,
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = SleekTextPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Games Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Party Games Collection",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = SleekTextPrimary
                            )
                            Text(
                                text = "Tap any game to play offline",
                                style = MaterialTheme.typography.bodySmall,
                                color = SleekTextSecondary
                            )
                        }

                        IconButton(
                            onClick = {
                                SoundEffects.playClick(context)
                                onManagePrompts()
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(SleekPurpleContainer)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PostAdd,
                                contentDescription = "Custom Prompts",
                                tint = SleekPurple
                            )
                        }
                    }
                }

                // Games List Cards
                items(gamesList) { game ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                SoundEffects.playClick(context)
                                onNavigateToGame(game.route)
                            }
                            .testTag("game_card_${game.id}"),
                        colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = SleekPurpleContainer,
                                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderLight),
                                modifier = Modifier.size(54.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = game.emoji, fontSize = 28.sp)
                                }
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = SleekPurpleContainer
                                    ) {
                                        Text(
                                            text = game.tag,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = SleekOnPurpleContainer,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            fontSize = 9.sp
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = SleekSurfaceElevated
                                    ) {
                                        Text(
                                            text = game.playersBadge,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Medium,
                                            color = SleekTextSecondary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            fontSize = 9.sp
                                        )
                                    }
                                }

                                Text(
                                    text = AppStrings.get(game.titleKey, language),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekTextPrimary
                                )

                                Text(
                                    text = AppStrings.get(game.descKey, language),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SleekTextSecondary,
                                    maxLines = 2
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = SleekSurfaceElevated,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Play",
                                        tint = SleekPurple,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // In-App Tutorial Dialog
            if (showTutorialDialog) {
                GameTutorialDialog(
                    initialGameId = initialTutorialGameId,
                    language = language,
                    onDismiss = { showTutorialDialog = false }
                )
            }
        }
    }
}


