package com.desipartygames.core

object GameRules {
    const val IMPOSTER = "game_imposter"
    const val RAJA_MANTRI = "game_raja_mantri"
    const val TRUTH_DARE = "game_truth_dare"
    const val ANTAKSHARI = "game_antakshari"
    const val CHARADES = "game_charades"
    const val MOST_LIKELY = "game_most_likely"
    const val WOULD_YOU_RATHER = "game_would_you_rather"
    const val PICTIONARY = "game_pictionary"

    fun minimumPlayers(route: String): Int = when (route) {
        IMPOSTER -> 3
        RAJA_MANTRI -> 4
        MOST_LIKELY -> 3
        else -> 2
    }

    fun minimumPlayersMessage(route: String): String =
        "Add at least ${minimumPlayers(route)} players to start this game."
}
