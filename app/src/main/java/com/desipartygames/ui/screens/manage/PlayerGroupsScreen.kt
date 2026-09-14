package com.desipartygames.ui.screens.manage

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desipartygames.core.ActiveGroupManager
import com.desipartygames.core.AppLanguage
import com.desipartygames.core.SoundEffects
import com.desipartygames.data.local.entity.PlayerEntity
import com.desipartygames.data.repository.PartyGamesRepository
import com.desipartygames.ui.components.PartyTopBar
import com.desipartygames.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun PlayerGroupsScreen(
    repository: PartyGamesRepository,
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val allPlayers by repository.allPlayers.collectAsState(initial = emptyList())

    val availableGroups = listOf("Hostel Gang", "Family Adda")
    val selectedGroup by ActiveGroupManager.currentGroup.collectAsState()
    LaunchedEffect(selectedGroup) {
        if (selectedGroup == "Chai Pe Charcha" || selectedGroup == "Office Squad") {
            ActiveGroupManager.currentGroup.value = "Hostel Gang"
        }
    }
    var newPlayerName by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("🔥") }

    val emojis = listOf("🔥", "🌸", "👑", "💃", "😎", "✨", "🦁", "🎩", "🚀", "☕", "🍿", "🎉")
    val playersInSelectedGroup = allPlayers.filter { it.groupTag == selectedGroup }

    var playerToEdit by remember { mutableStateOf<PlayerEntity?>(null) }
    var editPlayerName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            PartyTopBar(
                title = "Player Groups",
                subtitle = "Active: $selectedGroup",
                showBackButton = true,
                language = language,
                onLanguageSelected = onLanguageSelected,
                onBackClick = onNavigateBack
            )
        },
        containerColor = SleekBg
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Group Selection Tabs
            item {
                Text(
                    text = "Select Party Group",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SleekTextPrimary
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    items(availableGroups) { group ->
                        val isSelected = selectedGroup == group
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) SleekPurple else SleekSurfaceElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) SleekPurple else SleekSurfaceBorder),
                            modifier = Modifier.clickable {
                                SoundEffects.playClick(context)
                                ActiveGroupManager.currentGroup.value = group
                            }
                        ) {
                            Text(
                                text = group,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else SleekTextSecondary,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // Add New Player Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Add Player to $selectedGroup",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SleekTextPrimary
                        )

                        // Emoji Selector Row
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(emojis) { emoji ->
                                val isSelected = selectedEmoji == emoji
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) SleekPurpleContainer else SleekSurfaceElevated)
                                        .border(
                                            if (isSelected) 2.dp else 1.dp,
                                            if (isSelected) SleekPurple else SleekBorderLight,
                                            CircleShape
                                        )
                                        .clickable { selectedEmoji = emoji },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = emoji, fontSize = 20.sp)
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = newPlayerName,
                                onValueChange = { newPlayerName = it },
                                placeholder = { Text("Player Name (e.g. Rahul)", color = SleekTextTertiary) },
                                modifier = Modifier.weight(1f).testTag("input_manage_player_name"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SleekPurple,
                                    unfocusedBorderColor = SleekSurfaceBorder,
                                    focusedTextColor = SleekTextPrimary,
                                    unfocusedTextColor = SleekTextPrimary
                                ),
                                singleLine = true
                            )

                            Button(
                                onClick = {
                                    if (newPlayerName.isNotBlank()) {
                                        SoundEffects.playClick(context)
                                        coroutineScope.launch {
                                            repository.addPlayer(
                                                PlayerEntity(
                                                    name = newPlayerName.trim(),
                                                    avatarEmoji = selectedEmoji,
                                                    groupTag = selectedGroup
                                                )
                                            )
                                            newPlayerName = ""
                                        }
                                    }
                                },
                                enabled = newPlayerName.isNotBlank(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SleekPurple,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.height(54.dp).testTag("btn_save_player")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    }
                }
            }

            // Current Group Players List
            item {
                Text(
                    text = "Players in $selectedGroup (${playersInSelectedGroup.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SleekTextPrimary
                )
            }

            if (playersInSelectedGroup.isEmpty()) {
                item {
                    Text(
                        text = "No players added yet to this group. Add some above!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SleekTextTertiary
                    )
                }
            } else {
                items(playersInSelectedGroup) { player ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = player.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = SleekTextPrimary
                                    )
                                    Text(
                                        text = player.groupTag,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SleekTextSecondary
                                    )
                                }

                                Row {
                                    IconButton(
                                        onClick = {
                                            SoundEffects.playClick(context)
                                            playerToEdit = player
                                            editPlayerName = player.name
                                        }
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = SleekPurple)
                                    }
                                    IconButton(
                                        onClick = {
                                            SoundEffects.playClick(context)
                                            coroutineScope.launch {
                                                repository.removePlayer(player)
                                            }
                                        }
                                    ) {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = CrimsonRed)
                                    }
                                }
                            }


                        }
                    }
                }
            }
        }
        
        if (playerToEdit != null) {
            AlertDialog(
                onDismissRequest = { playerToEdit = null },
                title = { Text("Edit Player Name", fontWeight = FontWeight.Bold) },
                text = {
                    OutlinedTextField(
                        value = editPlayerName,
                        onValueChange = { editPlayerName = it },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SleekPurple,
                            unfocusedBorderColor = SleekSurfaceBorder,
                            focusedTextColor = SleekTextPrimary,
                            unfocusedTextColor = SleekTextPrimary
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (editPlayerName.isNotBlank()) {
                                SoundEffects.playClick(context)
                                coroutineScope.launch {
                                    playerToEdit?.let {
                                        repository.addPlayer(it.copy(name = editPlayerName.trim()))
                                    }
                                    playerToEdit = null
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SleekPurple, contentColor = Color.White)
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { playerToEdit = null }) {
                        Text("Cancel", color = SleekTextSecondary)
                    }
                },
                containerColor = SleekSurfaceCard,
                titleContentColor = SleekTextPrimary
            )
        }
    }
}

