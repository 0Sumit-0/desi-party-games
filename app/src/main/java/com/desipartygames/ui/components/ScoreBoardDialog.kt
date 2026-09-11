package com.desipartygames.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.desipartygames.core.AppLanguage
import com.desipartygames.core.SoundEffects
import com.desipartygames.ui.theme.*

data class PlayerScore(
    val name: String,
    var score: Int,
    val emoji: String = "🎲"
)

@Composable
fun ScoreBoardDialog(
    scores: List<PlayerScore>,
    title: String = "Party Scoreboard",
    language: AppLanguage = AppLanguage.HINGLISH,
    onScoreChanged: (index: Int, newScore: Int) -> Unit = { _, _ -> },
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sortedScores = scores.sortedByDescending { it.score }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .border(1.dp, SleekSurfaceBorder, RoundedCornerShape(28.dp))
                .testTag("scoreboard_dialog"),
            colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🏆 $title",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SleekPurple
                    )
                    IconButton(
                        onClick = {
                            SoundEffects.playClick(context)
                            onDismiss()
                        }
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = SleekTextSecondary)
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = SleekSurfaceBorder
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(sortedScores) { rank, item ->
                        val originalIndex = scores.indexOf(item)
                        val rankEmoji = when (rank) {
                            0 -> "👑"
                            1 -> "🥈"
                            2 -> "🥉"
                            else -> "#${rank + 1}"
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (rank == 0) SleekPurpleContainer else SleekSurfaceElevated
                            ),
                            shape = RoundedCornerShape(16.dp),
                            border = if (rank == 0) androidx.compose.foundation.BorderStroke(1.5.dp, SleekPurple) else androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = rankEmoji,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SleekPurple
                                    )
                                    Text(text = item.emoji, fontSize = 22.sp)
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = SleekTextPrimary
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            SoundEffects.playClick(context)
                                            if (originalIndex != -1) {
                                                onScoreChanged(originalIndex, item.score - 50)
                                            }
                                        },
                                        modifier = Modifier.size(32.dp).clip(CircleShape).background(SleekSurfaceCard)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Minus", tint = SleekTextSecondary, modifier = Modifier.size(16.dp))
                                    }

                                    Text(
                                        text = "${item.score}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SleekPurple
                                    )

                                    IconButton(
                                        onClick = {
                                            SoundEffects.playClick(context)
                                            if (originalIndex != -1) {
                                                onScoreChanged(originalIndex, item.score + 50)
                                            }
                                        },
                                        modifier = Modifier.size(32.dp).clip(CircleShape).background(SleekSurfaceCard)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Plus", tint = SleekPurple, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        SoundEffects.playClick(context)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SleekPurple,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
