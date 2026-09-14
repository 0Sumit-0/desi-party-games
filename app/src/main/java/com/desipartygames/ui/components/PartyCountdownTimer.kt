package com.desipartygames.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desipartygames.core.SoundEffects
import com.desipartygames.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun PartyCountdownTimer(
    totalSeconds: Int = 60,
    isAutoStart: Boolean = true,
    size: Dp = 190.dp,
    onTimerFinished: () -> Unit = {}
) {
    val context = LocalContext.current
    var remainingSeconds by rememberSaveable(totalSeconds) { mutableIntStateOf(totalSeconds) }
    var isRunning by rememberSaveable(isAutoStart) { mutableStateOf(isAutoStart) }

    LaunchedEffect(isRunning, remainingSeconds) {
        if (isRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
            if (remainingSeconds in 1..5) {
                SoundEffects.playWarningTick(context)
            } else if (remainingSeconds > 5 && remainingSeconds % 5 == 0) {
                SoundEffects.playTick(context)
            }

            if (remainingSeconds == 0) {
                isRunning = false
                SoundEffects.playBuzzer(context)
                onTimerFinished()
            }
        }
    }

    val progress = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds.toFloat() else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "timerProgress")

    val timerColor = when {
        remainingSeconds <= 5 -> SleekRose
        remainingSeconds <= 15 -> SleekPurpleLight
        else -> SleekPurple
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.testTag("party_timer")
    ) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                // Background Track
                drawArc(
                    color = SleekSurfaceBorder,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                )
                // Progress Arc
                drawArc(
                    color = timerColor,
                    startAngle = -90f,
                    sweepAngle = animatedProgress * 360f,
                    useCenter = false,
                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format("%02d:%02d", remainingSeconds / 60, remainingSeconds % 60),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (remainingSeconds <= 5) SleekRose else SleekTextPrimary,
                    fontSize = 38.sp
                )
                Text(
                    text = if (remainingSeconds == 0) "TIME'S UP!" else (if (isRunning) "TAP TO PAUSE" else "PAUSED"),
                    style = MaterialTheme.typography.labelSmall,
                    color = SleekTextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
            }
        }

        // Quick Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    SoundEffects.playClick(context)
                    isRunning = !isRunning
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SleekPurpleContainer)
                    .size(46.dp)
                    .testTag("timer_play_pause")
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Play",
                    tint = SleekOnPurpleContainer
                )
            }

            IconButton(
                onClick = {
                    SoundEffects.playClick(context)
                    remainingSeconds = totalSeconds
                    isRunning = true
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SleekSurfaceElevated)
                    .size(46.dp)
                    .testTag("timer_reset")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset Timer",
                    tint = SleekTextSecondary
                )
            }

            Button(
                onClick = {
                    SoundEffects.playClick(context)
                    remainingSeconds += 15
                    if (!isRunning) isRunning = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SleekSurfaceElevated,
                    contentColor = SleekPurple
                ),
                shape = RoundedCornerShape(100.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text("+15s", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
