package com.desipartygames.core

import kotlinx.coroutines.flow.MutableStateFlow

object ActiveGroupManager {
    // Defaults to "Hostel Gang", updates UI everywhere when changed
    val currentGroup = MutableStateFlow("Hostel Gang")
}