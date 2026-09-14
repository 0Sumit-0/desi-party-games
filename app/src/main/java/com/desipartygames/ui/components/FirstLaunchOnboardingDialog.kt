package com.desipartygames.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.desipartygames.ui.theme.SleekPurple
import com.desipartygames.ui.theme.SleekSurfaceCard
import com.desipartygames.ui.theme.SleekTextPrimary
import com.desipartygames.ui.theme.SleekTextSecondary

private data class OnboardingStep(
    val icon: String,
    val title: String,
    val description: String
)

@Composable
fun FirstLaunchOnboardingDialog(onFinished: () -> Unit) {
    val steps = listOf(
        OnboardingStep(
            icon = "1",
            title = "Set up your party",
            description = "Open Edit Players to choose your active group and add or rename the players who will play."
        ),
        OnboardingStep(
            icon = "2",
            title = "Choose a game",
            description = "Pick any game from the home screen. The app checks that enough players are ready before starting."
        ),
        OnboardingStep(
            icon = "3",
            title = "Adjust game settings",
            description = "Use the settings icon inside supported games to choose categories, teams, or other game options."
        ),
        OnboardingStep(
            icon = "4",
            title = "Pass the phone privately",
            description = "When a secret appears, pass the phone to the named player. Hold the locked area to reveal it, then continue."
        ),
        OnboardingStep(
            icon = "5",
            title = "Play and keep score",
            description = "Follow the prompts, use the game controls, and check the scoreboard when your game provides one."
        )
    )
    var currentStep by rememberSaveable { mutableIntStateOf(0) }
    val step = steps[currentStep]
    val isLastStep = currentStep == steps.lastIndex

    Dialog(onDismissRequest = onFinished) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SleekSurfaceCard,
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(
                    text = if (currentStep == 0) "Welcome to Desi Party Games" else "How to Play",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = SleekTextPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = step.icon,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = SleekPurple
                )
                Text(
                    text = step.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SleekPurple,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = step.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = SleekTextSecondary,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    steps.indices.forEach { index ->
                        Surface(
                            modifier = Modifier.padding(horizontal = 3.dp).size(if (index == currentStep) 10.dp else 7.dp),
                            shape = MaterialTheme.shapes.small,
                            color = if (index == currentStep) SleekPurple else SleekTextSecondary.copy(alpha = 0.35f)
                        ) {}
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { currentStep -= 1 },
                        enabled = currentStep > 0
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous step")
                    }
                    Button(onClick = {
                        if (isLastStep) onFinished() else currentStep += 1
                    }) {
                        Icon(
                            imageVector = if (isLastStep) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null
                        )
                        Text(if (isLastStep) "Get Started" else "Next")
                    }
                }
            }
        }
    }
}
