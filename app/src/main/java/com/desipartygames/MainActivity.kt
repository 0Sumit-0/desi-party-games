package com.desipartygames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.desipartygames.core.AppLanguage
import com.desipartygames.data.local.AppDatabase
import com.desipartygames.data.repository.PartyGamesRepository
import com.desipartygames.ui.screens.antakshari.AntakshariScreen
import com.desipartygames.ui.screens.antakshari.AntakshariViewModel
import com.desipartygames.ui.screens.charades.CharadesScreen
import com.desipartygames.ui.screens.charades.CharadesViewModel
import com.desipartygames.ui.screens.home.HomeScreen
import com.desipartygames.ui.screens.home.HomeViewModel
import com.desipartygames.ui.screens.imposter.ImposterGameScreen
import com.desipartygames.ui.screens.imposter.ImposterViewModel
import com.desipartygames.ui.screens.manage.CustomPromptsScreen
import com.desipartygames.ui.screens.manage.PlayerGroupsScreen
import com.desipartygames.ui.screens.minigames.MostLikelyToScreen
import com.desipartygames.ui.screens.minigames.PictionaryScreen
import com.desipartygames.ui.screens.minigames.WouldYouRatherScreen
import com.desipartygames.ui.screens.rajamantri.RajaMantriScreen
import com.desipartygames.ui.screens.rajamantri.RajaMantriViewModel
import com.desipartygames.ui.screens.truthdare.TruthDareViewModel
import com.desipartygames.ui.screens.truthdare.TruthOrDareScreen
import com.desipartygames.ui.theme.DesiPartyTheme

private class AppViewModelFactory(
    private val repository: PartyGamesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository)
            modelClass.isAssignableFrom(ImposterViewModel::class.java) -> ImposterViewModel(repository)
            modelClass.isAssignableFrom(RajaMantriViewModel::class.java) -> RajaMantriViewModel(repository)
            modelClass.isAssignableFrom(TruthDareViewModel::class.java) -> TruthDareViewModel(repository)
            modelClass.isAssignableFrom(AntakshariViewModel::class.java) -> AntakshariViewModel(repository)
            modelClass.isAssignableFrom(CharadesViewModel::class.java) -> CharadesViewModel(repository)
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        } as T
    }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = PartyGamesRepository(database.partyGamesDao())

        setContent {
            DesiPartyTheme {
                val navController = rememberNavController()
                var currentLanguage by rememberSaveable { mutableStateOf(AppLanguage.HINGLISH) }
                val viewModelFactory = remember { AppViewModelFactory(repository) }

                val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
                val homeUiState by homeViewModel.uiState.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 1. Home Hub
                    composable("home") {
                        HomeScreen(
                            uiState = homeUiState.copy(language = currentLanguage),
                            onLanguageSelected = { currentLanguage = it },
                            onGroupSelected = { homeViewModel.selectGroup(it) },
                            onNavigateToGame = { route -> navController.navigate(route) },
                            onManagePlayers = { navController.navigate("manage_players") },
                            onManagePrompts = { navController.navigate("manage_prompts") }
                        )
                    }

                    // 2. Who's The Imposter Game
                    composable("game_imposter") {
                        val imposterViewModel: ImposterViewModel = viewModel(factory = viewModelFactory)
                        ImposterGameScreen(
                            viewModel = imposterViewModel,
                            language = currentLanguage,
                            onLanguageSelected = { currentLanguage = it },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 3. Raja Mantri Chor Sipahi
                    composable("game_raja_mantri") {
                        val rajaViewModel: RajaMantriViewModel = viewModel(factory = viewModelFactory)
                        RajaMantriScreen(
                            viewModel = rajaViewModel,
                            language = currentLanguage,
                            onLanguageSelected = { currentLanguage = it },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 4. Truth or Dare
                    composable("game_truth_dare") {
                        val truthDareViewModel: TruthDareViewModel = viewModel(factory = viewModelFactory)
                        TruthOrDareScreen(
                            viewModel = truthDareViewModel,
                            language = currentLanguage,
                            onLanguageSelected = { currentLanguage = it },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 5. Antakshari Master
                    composable("game_antakshari") {
                        val antakshariViewModel: AntakshariViewModel = viewModel(factory = viewModelFactory)
                        AntakshariScreen(
                            viewModel = antakshariViewModel,
                            language = currentLanguage,
                            onLanguageSelected = { currentLanguage = it },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 6. Dumb Charades / Bollywood Acting
                    composable("game_charades") {
                        val charadesViewModel: CharadesViewModel = viewModel(factory = viewModelFactory)
                        CharadesScreen(
                            viewModel = charadesViewModel,
                            language = currentLanguage,
                            onLanguageSelected = { currentLanguage = it },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 7. Most Likely To
                    composable("game_most_likely") {
                        MostLikelyToScreen(
                            players = homeUiState.playersInGroup,
                            language = currentLanguage,
                            onLanguageSelected = { currentLanguage = it },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 8. Would You Rather
                    composable("game_would_you_rather") {
                        WouldYouRatherScreen(
                            language = currentLanguage,
                            onLanguageSelected = { currentLanguage = it },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 9. Pictionary Canvas
                    composable("game_pictionary") {
                        PictionaryScreen(
                            language = currentLanguage,
                            onLanguageSelected = { currentLanguage = it },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 10. Manage Players & Gangs
                    composable("manage_players") {
                        PlayerGroupsScreen(
                            repository = repository,
                            language = currentLanguage,
                            onLanguageSelected = { currentLanguage = it },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 11. Manage Custom Prompts
                    composable("manage_prompts") {
                        CustomPromptsScreen(
                            repository = repository,
                            language = currentLanguage,
                            onLanguageSelected = { currentLanguage = it },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
