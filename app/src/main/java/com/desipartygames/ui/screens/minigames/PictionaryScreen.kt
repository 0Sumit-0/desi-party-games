package com.desipartygames.ui.screens.minigames

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desipartygames.core.AppLanguage
import com.desipartygames.core.AppStrings
import com.desipartygames.core.SoundEffects
import com.desipartygames.data.content.MiniGamesBank
import com.desipartygames.ui.components.PartyTopBar
import com.desipartygames.ui.theme.*

data class DrawPath(
    val path: Path,
    val color: Color,
    val strokeWidth: Float
)

@Composable
fun PictionaryScreen(
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val paths = remember { mutableStateListOf<DrawPath>() }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    var selectedColor by remember { mutableStateOf(SleekPurple) }
    var strokeWidth by remember { mutableFloatStateOf(8f) }

    val prompts = MiniGamesBank.pictionaryPrompts
    var currentPromptIndex by remember { mutableIntStateOf(0) }
    var isWordRevealed by remember { mutableStateOf(false) }

    val currentPrompt = prompts[currentPromptIndex % prompts.size]

    val availableColors = listOf(
        SleekPurple,
        SleekRose,
        Color(0xFF0284C7), // Sky blue
        Color(0xFF059669), // Emerald
        Color(0xFFD97706), // Amber
        Color(0xFF1D1B20), // Charcoal
        Color(0xFF9333EA)  // Violet
    )

    Scaffold(
        topBar = {
            PartyTopBar(
                title = AppStrings.get("game_pictionary", language),
                subtitle = "Draw & Guess on 1 Screen",
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Secret Word Bar for Drawer
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Secret Word (For Drawer):",
                            style = MaterialTheme.typography.labelSmall,
                            color = SleekTextSecondary
                        )
                        Text(
                            text = if (isWordRevealed) currentPrompt else "🙈 [TAP REVEAL]",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isWordRevealed) SleekPurple else SleekTextTertiary
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        IconButton(
                            onClick = { isWordRevealed = !isWordRevealed }
                        ) {
                            Icon(
                                imageVector = if (isWordRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle Reveal",
                                tint = SleekPurple
                            )
                        }

                        Button(
                            onClick = {
                                SoundEffects.playClick(context)
                                currentPromptIndex += 1
                                isWordRevealed = false
                                paths.clear()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SleekPurpleContainer,
                                contentColor = SleekOnPurpleContainer
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("New Word 🎲", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Drawing Canvas Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .border(1.dp, SleekSurfaceBorder, RoundedCornerShape(20.dp))
                    .testTag("drawing_canvas")
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    val newPath = Path().apply { moveTo(offset.x, offset.y) }
                                    currentPath = newPath
                                    paths.add(DrawPath(newPath, selectedColor, strokeWidth))
                                },
                                onDrag = { change, _ ->
                                    currentPath?.lineTo(change.position.x, change.position.y)
                                    if (paths.isNotEmpty()) {
                                        val last = paths.removeAt(paths.lastIndex)
                                        paths.add(last)
                                    }
                                },
                                onDragEnd = {
                                    currentPath = null
                                }
                            )
                        }
                ) {
                    paths.forEach { drawPath ->
                        drawPath(
                            path = drawPath.path,
                            color = drawPath.color,
                            style = Stroke(
                                width = drawPath.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }
            }

            // Color Palette & Tool Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Colors
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    availableColors.forEach { col ->
                        val isSelected = selectedColor == col
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(col)
                                .border(
                                    if (isSelected) 3.dp else 1.dp,
                                    if (isSelected) SleekPurple else SleekBorderLight,
                                    CircleShape
                                )
                                .clickable { selectedColor = col }
                        )
                    }
                }

                // Action buttons: Undo & Clear
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = {
                            if (paths.isNotEmpty()) {
                                SoundEffects.playClick(context)
                                paths.removeAt(paths.lastIndex)
                            }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SleekSurfaceElevated)
                            .size(38.dp)
                    ) {
                        Icon(Icons.Default.Undo, contentDescription = "Undo", tint = SleekTextSecondary)
                    }

                    IconButton(
                        onClick = {
                            SoundEffects.playClick(context)
                            paths.clear()
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SleekRose)
                            .size(38.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear", tint = SleekOnRose)
                    }
                }
            }
        }
    }
}
