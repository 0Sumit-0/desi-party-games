package com.desipartygames.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desipartygames.core.AppLanguage
import com.desipartygames.core.SoundEffects
import com.desipartygames.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyTopBar(
    title: String,
    subtitle: String? = null,
    showBackButton: Boolean = false,
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onBackClick: () -> Unit = {},
    onRulesClick: (() -> Unit)? = null,
    onScoreboardClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var isSoundOn by remember { mutableStateOf(SoundEffects.isSoundOn()) }
    var showLangMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SleekPurple,
                    maxLines = 1
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = SleekTextSecondary.copy(alpha = 0.75f),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp,
                        maxLines = 1
                    )
                }
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = {
                        SoundEffects.playClick(context)
                        onBackClick()
                    },
                    modifier = Modifier.testTag("top_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = SleekPurple
                    )
                }
            }
        },
        actions = {
            // Rules
            if (onRulesClick != null) {
                IconButton(
                    onClick = {
                        SoundEffects.playClick(context)
                        onRulesClick()
                    },
                    modifier = Modifier.testTag("top_bar_rules_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Rules",
                        tint = SleekPurple
                    )
                }
            }

            // Scoreboard
            if (onScoreboardClick != null) {
                IconButton(
                    onClick = {
                        SoundEffects.playClick(context)
                        onScoreboardClick()
                    },
                    modifier = Modifier.testTag("top_bar_scoreboard_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Scoreboard",
                        tint = SleekPurple
                    )
                }
            }

            // Sound Toggle
            IconButton(
                onClick = {
                    isSoundOn = !isSoundOn
                    SoundEffects.setSoundEnabled(isSoundOn)
                    if (isSoundOn) SoundEffects.playClick(context)
                },
                modifier = Modifier.testTag("top_bar_sound_button")
            ) {
                Icon(
                    imageVector = if (isSoundOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                    contentDescription = if (isSoundOn) "Mute" else "Unmute",
                    tint = if (isSoundOn) SleekPurple else SleekTextTertiary
                )
            }

            // Language Selector Dropdown
            Box {
                Button(
                    onClick = {
                        SoundEffects.playClick(context)
                        showLangMenu = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SleekPurpleContainer,
                        contentColor = SleekOnPurpleContainer
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderLight),
                    modifier = Modifier.height(36.dp).testTag("top_bar_language_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Translate, contentDescription = "Language", tint = SleekPurple, modifier = Modifier.size(16.dp))
                        Text(
                            text = language.code.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = SleekOnPurpleContainer
                        )
                    }
                }

                DropdownMenu(
                    expanded = showLangMenu,
                    onDismissRequest = { showLangMenu = false },
                    modifier = Modifier.background(SleekSurfaceCard)
                ) {
                    AppLanguage.values().forEach { lang ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = lang.displayName,
                                    color = if (lang == language) SleekPurple else SleekTextPrimary,
                                    fontWeight = if (lang == language) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                SoundEffects.playClick(context)
                                onLanguageSelected(lang)
                                showLangMenu = false
                            },
                            modifier = Modifier.testTag("lang_menu_${lang.code}")
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SleekBg,
            scrolledContainerColor = SleekSurfaceCard
        )
    )
}

