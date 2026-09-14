package com.desipartygames.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.desipartygames.core.AppLanguage
import com.desipartygames.core.AppStrings
import com.desipartygames.core.SoundEffects
import com.desipartygames.ui.theme.*

@Composable
fun PassPhoneDialog(
    playerName: String,
    playerEmoji: String = "🎭",
    roleOrSecretTitle: String,
    secretContent: String,
    secretHint: String = "",
    isImposterOrSpecial: Boolean = false,
    language: AppLanguage = AppLanguage.HINGLISH,
    onDonePassing: () -> Unit
) {
    val context = LocalContext.current
    var isRevealed by rememberSaveable { mutableStateOf(false) }
    var hasRevealedAtLeastOnce by rememberSaveable { mutableStateOf(false) }

    val scale by animateFloatAsState(if (isRevealed) 1.02f else 1f, label = "cardScale")

    Dialog(
        onDismissRequest = { /* Modal must be acted upon explicitly */ },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.92f))
                .testTag("pass_phone_dialog"),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale)
                    .clip(RoundedCornerShape(0.dp))
                    .border(
                        1.5.dp,
                        if (isRevealed) (if (isImposterOrSpecial) SleekRose else SleekPurple) else SleekSurfaceBorder,
                        RoundedCornerShape(0.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // Header with player name & handover shield
                    Surface(
                        shape = CircleShape,
                        color = SleekPurpleContainer,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = playerEmoji, fontSize = 36.sp)
                        }
                    }

                    Text(
                        text = "${AppStrings.get("pass_to", language)} $playerName",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = SleekTextPrimary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = if (!isRevealed) "Make sure only $playerName is looking!" else "Memorize your secret word!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SleekTextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Privacy Shield Box with Hold-to-Reveal Gesture
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isRevealed) {
                                    if (isImposterOrSpecial) SleekRoseContainer else SleekPurpleContainer
                                } else {
                                    SleekSurfaceElevated
                                }
                            )
                            .border(
                                1.5.dp,
                                if (isRevealed) (if (isImposterOrSpecial) SleekRose else SleekPurple) else SleekSurfaceBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        SoundEffects.playClick(context)
                                        isRevealed = true
                                        hasRevealedAtLeastOnce = true
                                        tryAwaitRelease()
                                        isRevealed = false
                                    }
                                )
                            }
                            .testTag("reveal_touch_target"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!isRevealed) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Icon(
                                    imageVector = if (isRevealed) Icons.Default.LockOpen else Icons.Default.Lock,
                                    contentDescription = if (isRevealed) "Unlocked" else "Locked",
                                    tint = SleekPurple,
                                    modifier = Modifier.size(36.dp)
                                )
                                Text(
                                    text = AppStrings.get("hold_to_reveal", language),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekTextPrimary
                                )
                                Text(
                                    text = "👆 Press and hold finger here",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SleekTextTertiary
                                )
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = roleOrSecretTitle.uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isImposterOrSpecial) CrimsonRed else SleekPurple,
                                    letterSpacing = 2.sp
                                )

                                Text(
                                    text = secretContent,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isImposterOrSpecial) CrimsonRed else SleekTextPrimary,
                                    textAlign = TextAlign.Center
                                )

                                if (secretHint.isNotEmpty()) {
                                    Text(
                                        text = secretHint,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = SleekTextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    // Next / Done Button
                    Button(
                        onClick = {
                            SoundEffects.playClick(context)
                            onDonePassing()
                        },
                        enabled = hasRevealedAtLeastOnce,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("pass_done_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SleekPurple,
                            disabledContainerColor = SleekSurfaceElevated,
                            contentColor = Color.White,
                            disabledContentColor = SleekTextTertiary
                        ),
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Text(
                            text = if (hasRevealedAtLeastOnce) AppStrings.get("tap_to_continue", language) else "Hold to view first",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
