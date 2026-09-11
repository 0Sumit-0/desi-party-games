package com.desipartygames.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.desipartygames.core.AppLanguage
import com.desipartygames.core.SoundEffects
import com.desipartygames.ui.theme.*
import kotlinx.coroutines.launch

data class TutorialStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val keyHighlight: String? = null
)

data class GameTutorialData(
    val gameId: String,
    val gameTitle: String,
    val gameEmoji: String,
    val subtitle: String,
    val summary: String,
    val steps: List<TutorialStep>,
    val scoringSystem: List<Pair<String, String>> = emptyList(),
    val proTips: List<String> = emptyList()
)

object TutorialRepository {
    val tutorials = listOf(
        GameTutorialData(
            gameId = "game_imposter",
            gameTitle = "Who's The Imposter",
            gameEmoji = "🕵️‍♂️",
            subtitle = "Secret Role & Deception Game",
            summary = "Everyone receives the exact same secret Indian location, Bollywood movie, or cricketer — EXCEPT 1 or 2 Imposters who receive nothing! Ask subtle questions to catch them.",
            steps = listOf(
                TutorialStep(
                    stepNumber = 1,
                    title = "Pass Phone Privately",
                    description = "Hold your finger on the privacy shield to reveal your secret word. Citizens memorize the secret word; Imposters see 'YOU ARE THE IMPOSTER'.",
                    iconEmoji = "📱",
                    keyHighlight = "Keep your screen hidden from others while pressing!"
                ),
                TutorialStep(
                    stepNumber = 2,
                    title = "Take Turns Asking Questions",
                    description = "Take turns asking one other player a clever question about the secret word. Don't be too obvious, or the Imposter will guess the word!",
                    iconEmoji = "🗣️",
                    keyHighlight = "Example: 'Is this thing found more in Mumbai or Delhi?'"
                ),
                TutorialStep(
                    stepNumber = 3,
                    title = "Imposter Bluffs to Blend In",
                    description = "If you are the Imposter, pay deep attention to everyone's clues, give vague convincing answers, and try to guess what the secret word is.",
                    iconEmoji = "🎭",
                    keyHighlight = "Stay calm and confident—never panic!"
                ),
                TutorialStep(
                    stepNumber = 4,
                    title = "Vote & Elimination",
                    description = "When the discussion timer rings, everyone points fingers at the suspect on 3-2-1! If the group votes out the Imposter, Citizens win!",
                    iconEmoji = "🗳️",
                    keyHighlight = "Citizens +500 pts if caught • Imposter +1000 pts if unmasked!"
                )
            ),
            scoringSystem = listOf(
                "Citizens Catch Imposter" to "+500 pts each",
                "Imposter Escapes Unvoted" to "+1000 pts",
                "Imposter Correctly Guesses Word" to "+800 pts"
            ),
            proTips = listOf(
                "Never give direct definitions (e.g., if word is 'Biryani', don't say 'It has rice and spices').",
                "Imposters: Ask questions about emotions or occasions rather than physical items.",
                "Watch out for players who take too long to answer or repeat what previous players said."
            )
        ),
        GameTutorialData(
            gameId = "game_raja_mantri",
            gameTitle = "Raja Mantri Chor Sipahi",
            gameEmoji = "👑",
            subtitle = "Classic Desi 4-Player Royal Court",
            summary = "The legendary Indian paper-chit game brought to your screen! 4 royal chits are shuffled: Raja (1000), Mantri (800), Sipahi (500), and Chor (0).",
            steps = listOf(
                TutorialStep(
                    stepNumber = 1,
                    title = "Draw Your Royal Chits",
                    description = "Pass the phone to each of the 4 players. Hold the screen to peek at your secret royal role.",
                    iconEmoji = "📜",
                    keyHighlight = "Only the Raja reveals identity immediately by calling court!"
                ),
                TutorialStep(
                    stepNumber = 2,
                    title = "Raja Calls the Court",
                    description = "Raja says out loud: 'Mera Mantri Kaun?' (Who is my Minister?). The Minister raises their hand and stands before the Raja.",
                    iconEmoji = "👑",
                    keyHighlight = "Raja commands Mantri to identify the thief!"
                ),
                TutorialStep(
                    stepNumber = 3,
                    title = "Sipahi / Mantri Investigates",
                    description = "The Sipahi/Mantri looks at the remaining players, studies their poker faces, and points out who is the Chor (Thief).",
                    iconEmoji = "🔍",
                    keyHighlight = "Listen carefully to nervous giggles and eye contact!"
                ),
                TutorialStep(
                    stepNumber = 4,
                    title = "Scoring & Round Results",
                    description = "If Sipahi guesses correctly: Sipahi gets 500 pts, Chor gets 0. If guess is wrong: Chor steals Sipahi's 500 pts!",
                    iconEmoji = "💰",
                    keyHighlight = "Raja always gets 1000 pts • Mantri gets 800 pts!"
                )
            ),
            scoringSystem = listOf(
                "👑 Raja (King)" to "1000 pts",
                "📜 Mantri (Minister)" to "800 pts",
                "⚔️ Sipahi (Soldier - Correct Guess)" to "500 pts",
                "🦹 Chor (Thief - Escaped / Sipahi Wrong)" to "500 pts",
                "🦹 Chor (Thief - Caught)" to "0 pts"
            ),
            proTips = listOf(
                "Chor: Act completely innocent like a Sipahi or Mantri.",
                "Raja: Stay regal and enjoy the drama between the remaining players!",
                "Play 5-10 rounds to see who crowned Supreme Emperor of the party."
            )
        ),
        GameTutorialData(
            gameId = "game_truth_dare",
            gameTitle = "Truth or Dare (Kabhi Kabhi)",
            gameEmoji = "🍾",
            subtitle = "Spin the Desi Bottle & Reveal Secrets",
            summary = "Spin the virtual bottle on the table! When it lands on a player, they must choose between answering an authentic Desi Truth or performing a daring Challenge.",
            steps = listOf(
                TutorialStep(
                    stepNumber = 1,
                    title = "Select Vibe & Category",
                    description = "Choose your party vibe: Family Safe (wholesome & funny), Hostel/Friends (spicy & chaotic), or Bollywood Masala.",
                    iconEmoji = "✨",
                    keyHighlight = "Pick the category that fits your gathering best."
                ),
                TutorialStep(
                    stepNumber = 2,
                    title = "Spin the Party Bottle",
                    description = "Tap the spin button or flick the bottle. The 3D physics animation smoothly selects the next brave player.",
                    iconEmoji = "🍾",
                    keyHighlight = "Bottle lands randomly on one player around the circle."
                ),
                TutorialStep(
                    stepNumber = 3,
                    title = "Choose Truth or Dare",
                    description = "The selected player chooses Truth (Sach) or Dare (Chunauti).",
                    iconEmoji = "⚡",
                    keyHighlight = "No chickening out! You can add custom questions anytime."
                ),
                TutorialStep(
                    stepNumber = 4,
                    title = "Perform & Complete",
                    description = "Read the prompt aloud in Hindi, Hinglish, or English. Tap 'Complete (+10)' or 'Chicken Out (0)' to update the scoreboard.",
                    iconEmoji = "🎉",
                    keyHighlight = "Complete challenges for max party glory!"
                )
            ),
            scoringSystem = listOf(
                "✅ Prompt Completed" to "+10 pts",
                "🐔 Chickened Out / Skipped" to "0 pts"
            ),
            proTips = listOf(
                "Family mode is 100% safe for parents, aunts, and younger cousins.",
                "Use the 'Add Custom Prompt' button to inject inside jokes about your friends!",
                "You can change the language toggle anytime for Hindi / Hinglish / English prompts."
            )
        ),
        GameTutorialData(
            gameId = "game_antakshari",
            gameTitle = "Antakshari Master",
            gameEmoji = "🎤",
            subtitle = "Bollywood Musical Team Battle",
            summary = "The quintessential Indian singing battle with a smart Akshara letter generator, built-in 45s countdown buzzer, and point tracker.",
            steps = listOf(
                TutorialStep(
                    stepNumber = 1,
                    title = "Divide into Teams",
                    description = "Split the party into Team Sur (🎵) and Team Taal (🥁).",
                    iconEmoji = "👥",
                    keyHighlight = "2 or more teams can compete head-to-head!"
                ),
                TutorialStep(
                    stepNumber = 2,
                    title = "Sing Starting with Given Letter",
                    description = "The app gives your team a Hindi letter (e.g., 'म', 'क', 'प', 'ल'). Your team must start singing a valid Bollywood song with that syllable.",
                    iconEmoji = "🎶",
                    keyHighlight = "Must sing at least 2 complete lines with the correct tune!"
                ),
                TutorialStep(
                    stepNumber = 3,
                    title = "Beat the 45-Second Timer",
                    description = "The ticking buzzer keeps adrenaline high. If your team starts singing before 00:00, you win the round points!",
                    iconEmoji = "⏱️",
                    keyHighlight = "Use the 'Syllable Hint' button if your team gets stuck."
                ),
                TutorialStep(
                    stepNumber = 4,
                    title = "Next Letter from Song's Ending",
                    description = "The last letter of the song you sang becomes the starting letter for the opponent team!",
                    iconEmoji = "🔄",
                    keyHighlight = "+10 pts per successful song • 0 pts on timer expiry."
                )
            ),
            scoringSystem = listOf(
                "🎵 Song Sang in Time" to "+10 pts",
                "⏱️ Timer Ran Out" to "0 pts"
            ),
            proTips = listOf(
                "Keep famous songs ready for tricky letters like 'ट', 'ठ', 'ढ', and 'य'!",
                "Sing loudly with the entire group joining the chorus for maximum fun."
            )
        ),
        GameTutorialData(
            gameId = "game_charades",
            gameTitle = "Dumb Charades / Bollywood",
            gameEmoji = "🎭",
            subtitle = "Silent Acting & Movie Guessing",
            summary = "Act out hilarious Indian movie titles, viral memes, and iconic dialogues without making a single sound while your team yells guesses.",
            steps = listOf(
                TutorialStep(
                    stepNumber = 1,
                    title = "Pick a Movie Category",
                    description = "Choose from Classic Bizarre Titles, 90s Blockbusters, Modern Hits, or Iconic Dialogues.",
                    iconEmoji = "🎬",
                    keyHighlight = "Hundreds of authentic Bollywood titles ready!"
                ),
                TutorialStep(
                    stepNumber = 2,
                    title = "Actor Peeks Privately",
                    description = "Pass the phone to the acting player. They read the secret title, word count, and hints, then tap 'Start Acting'.",
                    iconEmoji = "🤫",
                    keyHighlight = "Strict Rule: No talking, whispering, or lip-syncing allowed!"
                ),
                TutorialStep(
                    stepNumber = 3,
                    title = "Act with Body Language",
                    description = "Use standard hand gestures (fingers for word count, ear tug for 'sounds like', pinch for small words).",
                    iconEmoji = "🖐️",
                    keyHighlight = "Tap 'Gesture Guide' in the app anytime for gesture cheat codes!"
                ),
                TutorialStep(
                    stepNumber = 4,
                    title = "Team Guesses Before 60s",
                    description = "When someone shouts the right answer, tap 'Guessed It (+10)' to bank points for your team.",
                    iconEmoji = "🏆",
                    keyHighlight = "+10 pts for correct guess within 60 seconds."
                )
            ),
            scoringSystem = listOf(
                "🎯 Correct Guess" to "+10 pts",
                "⌛ Skipped / Timeout" to "0 pts"
            ),
            proTips = listOf(
                "First show the number of words with your fingers.",
                "Break down long bizarre titles word-by-word.",
                "Act out famous actors or signature dance moves from the movie."
            )
        ),
        GameTutorialData(
            gameId = "game_most_likely",
            gameTitle = "Most Likely To",
            gameEmoji = "👉",
            subtitle = "Spill Tea & Point Fingers",
            summary = "Unfiltered party banter! A juicy Indian scenario appears on screen (e.g. 'Who is most likely to barging in wedding for free food?'). On count of 3, everyone points fingers!",
            steps = listOf(
                TutorialStep(
                    stepNumber = 1,
                    title = "Read Dilemma Aloud",
                    description = "The prompt is displayed on screen in your selected language.",
                    iconEmoji = "📢",
                    keyHighlight = "Host reads the dilemma clearly for the whole room."
                ),
                TutorialStep(
                    stepNumber = 2,
                    title = "Count 3... 2... 1...",
                    description = "Everyone raises their finger and simultaneously points to the person in the room who fits the prompt most!",
                    iconEmoji = "👉",
                    keyHighlight = "No hesitating! First instinct is always the funniest."
                ),
                TutorialStep(
                    stepNumber = 3,
                    title = "Defend or Accept Defeat",
                    description = "The person with the most fingers pointed at them must explain themselves or take a fun penalty!",
                    iconEmoji = "😂",
                    keyHighlight = "Tap the player's name to track who gets called out the most."
                )
            ),
            scoringSystem = listOf(
                "👑 Most Voted Player" to "Wins the Round Crown"
            ),
            proTips = listOf(
                "Best played with close friends, hostel roommates, or cousins.",
                "Capture hilarious candid moments when everyone points at the same person!"
            )
        ),
        GameTutorialData(
            gameId = "game_would_you_rather",
            gameTitle = "Would You Rather",
            gameEmoji = "🤔",
            subtitle = "Impossible Desi Dilemmas",
            summary = "Choose between two equally absurd, delicious, or terrifying Indian situations. Compare your choice with what most players chose!",
            steps = listOf(
                TutorialStep(
                    stepNumber = 1,
                    title = "Read the Two Options",
                    description = "Two extreme choices appear (e.g. 'Never eat Biryani again' vs 'Never watch Cricket again').",
                    iconEmoji = "⚖️",
                    keyHighlight = "You MUST pick one—no neutral answers allowed!"
                ),
                TutorialStep(
                    stepNumber = 2,
                    title = "Take Group Vote & Tap Choice",
                    description = "Discuss with the group, tap your chosen option, and reveal global party percentage stats.",
                    iconEmoji = "📊",
                    keyHighlight = "See whether you're in the majority or the wild minority!"
                )
            ),
            scoringSystem = listOf(
                "🔥 Pure Debate & Entertainment" to "Infinite Fun"
            ),
            proTips = listOf(
                "Ask players to justify their crazy choices for lively group debates.",
                "Switch languages to read funny Hindi phrasing!"
            )
        ),
        GameTutorialData(
            gameId = "game_pictionary",
            gameTitle = "Pictionary Canvas",
            gameEmoji = "🎨",
            subtitle = "Draw & Guess on Single Screen",
            summary = "Pass the phone to the artist. They peek at the secret word, start sketching with colorful digital pens, and the room guesses what's on screen!",
            steps = listOf(
                TutorialStep(
                    stepNumber = 1,
                    title = "Artist Peeks at Secret Word",
                    description = "Tap the eye icon to secretly reveal the drawing prompt (e.g. 'Samosa', 'Auto Rickshaw', 'Taj Mahal').",
                    iconEmoji = "👁️",
                    keyHighlight = "Keep secret word hidden from guessers!"
                ),
                TutorialStep(
                    stepNumber = 2,
                    title = "Sketch on the Digital Canvas",
                    description = "Use various colors, pen sizes, and the instant clear button to draw your masterpiece.",
                    iconEmoji = "🖌️",
                    keyHighlight = "No writing letters, numbers, or talking allowed!"
                ),
                TutorialStep(
                    stepNumber = 3,
                    title = "Friends Shout Guesses",
                    description = "First person to guess correctly wins the round and becomes the next sketch artist.",
                    iconEmoji = "💡",
                    keyHighlight = "Tap 'New Word' anytime for fresh prompts."
                )
            ),
            scoringSystem = listOf(
                "🎨 Correct Guess" to "+10 pts to guesser & artist"
            ),
            proTips = listOf(
                "Use contrasting colors to highlight details.",
                "Start with the biggest shape first, then add iconic details!"
            )
        )
    )

    fun getTutorial(gameId: String): GameTutorialData? {
        return tutorials.find { it.gameId == gameId } ?: tutorials.firstOrNull()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTutorialDialog(
    initialGameId: String = "game_imposter",
    language: AppLanguage = AppLanguage.HINGLISH,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedGameId by remember { mutableStateOf(initialGameId) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Step-by-Step, 1 = Scoring & Pro Tips

    val currentTutorial = remember(selectedGameId) {
        TutorialRepository.getTutorial(selectedGameId) ?: TutorialRepository.tutorials.first()
    }

    val pagerState = rememberPagerState(pageCount = { currentTutorial.steps.size })

    // Reset pager when switching games
    LaunchedEffect(selectedGameId) {
        pagerState.scrollToPage(0)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f)
                    .clip(RoundedCornerShape(28.dp))
                    .border(1.5.dp, SleekPurple, RoundedCornerShape(28.dp))
                    .testTag("in_app_tutorial_dialog"),
                colors = CardDefaults.cardColors(containerColor = SleekSurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // 1. Header with Title, Badge, and Close Button
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = SleekPurpleContainer,
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = "📖", fontSize = 22.sp)
                                    }
                                }
                                Column {
                                    Text(
                                        text = "How to Play Guide",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SleekPurple
                                    )
                                    Text(
                                        text = "In-App Player Tutorials",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SleekTextSecondary
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    SoundEffects.playClick(context)
                                    onDismiss()
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = SleekTextPrimary)
                            }
                        }

                        // Game Switcher Horizontal Selector
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            items(TutorialRepository.tutorials) { tut ->
                                val isSelected = tut.gameId == selectedGameId
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) SleekPurple else SleekSurfaceElevated,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) SleekPurple else SleekSurfaceBorder
                                    ),
                                    modifier = Modifier.clickable {
                                        SoundEffects.playClick(context)
                                        selectedGameId = tut.gameId
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(text = tut.gameEmoji, fontSize = 16.sp)
                                        Text(
                                            text = tut.gameTitle,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else SleekTextPrimary
                                        )
                                    }
                                }
                            }
                        }

                        // Tab Row (Steps vs Scoring / Pro-Tips)
                        TabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = SleekSurfaceElevated,
                            contentColor = SleekPurple,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = {
                                    SoundEffects.playClick(context)
                                    selectedTab = 0
                                },
                                text = {
                                    Text(
                                        text = "🎮 Step-by-Step (${currentTutorial.steps.size})",
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedTab == 0) SleekPurple else SleekTextSecondary
                                    )
                                }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = {
                                    SoundEffects.playClick(context)
                                    selectedTab = 1
                                },
                                text = {
                                    Text(
                                        text = "🏆 Rules & Tips",
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedTab == 1) SleekPurple else SleekTextSecondary
                                    )
                                }
                            )
                        }
                    }

                    // 2. Body Content
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        if (selectedTab == 0) {
                            // Step-by-step Carousel View
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                ) { page ->
                                    val step = currentTutorial.steps[page]
                                    Card(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 4.dp),
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(containerColor = SleekSurfaceElevated),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(18.dp),
                                            verticalArrangement = Arrangement.SpaceBetween,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                // Step badge and emoji
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Surface(
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = SleekPurpleContainer
                                                    ) {
                                                        Text(
                                                            text = "STEP ${step.stepNumber} OF ${currentTutorial.steps.size}",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            color = SleekOnPurpleContainer,
                                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                        )
                                                    }
                                                }

                                                Surface(
                                                    shape = CircleShape,
                                                    color = SleekPurpleContainer.copy(alpha = 0.6f),
                                                    modifier = Modifier.size(64.dp)
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Text(text = step.iconEmoji, fontSize = 32.sp)
                                                    }
                                                }

                                                Text(
                                                    text = step.title,
                                                    style = MaterialTheme.typography.titleLarge,
                                                    fontWeight = FontWeight.Bold,
                                                    color = SleekTextPrimary,
                                                    textAlign = TextAlign.Center
                                                )

                                                Text(
                                                    text = step.description,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = SleekTextSecondary,
                                                    textAlign = TextAlign.Center,
                                                    lineHeight = 22.sp
                                                )
                                            }

                                            if (step.keyHighlight != null) {
                                                Card(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    shape = RoundedCornerShape(12.dp),
                                                    colors = CardDefaults.cardColors(containerColor = SleekPurpleContainer)
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                    ) {
                                                        Icon(
                                                            Icons.Default.Star,
                                                            contentDescription = null,
                                                            tint = SleekPurple,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                        Text(
                                                            text = step.keyHighlight,
                                                            style = MaterialTheme.typography.labelSmall,
                                                            fontWeight = FontWeight.Bold,
                                                            color = SleekOnPurpleContainer
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // Carousel indicator dots
                                Row(
                                    modifier = Modifier.padding(top = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    repeat(currentTutorial.steps.size) { index ->
                                        val isCurrent = pagerState.currentPage == index
                                        Box(
                                            modifier = Modifier
                                                .size(if (isCurrent) 10.dp else 6.dp)
                                                .clip(CircleShape)
                                                .background(if (isCurrent) SleekPurple else SleekSurfaceBorder)
                                        )
                                    }
                                }
                            }
                        } else {
                            // Scoring and Pro-Tips tab
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                item {
                                    Text(
                                        text = currentTutorial.summary,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = SleekTextSecondary,
                                        lineHeight = 22.sp
                                    )
                                }

                                if (currentTutorial.scoringSystem.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = "🏆 Scoring Breakdown",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = SleekTextPrimary
                                        )
                                    }

                                    item {
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = SleekSurfaceElevated),
                                            shape = RoundedCornerShape(16.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(14.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                currentTutorial.scoringSystem.forEach { (rule, points) ->
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = rule,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = SleekTextPrimary,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                        Surface(
                                                            shape = RoundedCornerShape(6.dp),
                                                            color = SleekPurpleContainer
                                                        ) {
                                                            Text(
                                                                text = points,
                                                                style = MaterialTheme.typography.labelSmall,
                                                                fontWeight = FontWeight.ExtraBold,
                                                                color = SleekOnPurpleContainer,
                                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                if (currentTutorial.proTips.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = "💡 Party Pro-Tips",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = SleekTextPrimary
                                        )
                                    }

                                    items(currentTutorial.proTips) { tip ->
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = SleekPurpleContainer.copy(alpha = 0.4f)),
                                            shape = RoundedCornerShape(12.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderLight),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                                verticalAlignment = Alignment.Top
                                            ) {
                                                Text(text = "🔥", fontSize = 18.sp)
                                                Text(
                                                    text = tip,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = SleekTextPrimary,
                                                    lineHeight = 18.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 3. Navigation Controls / Bottom Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selectedTab == 0) {
                            // Back Step Button
                            OutlinedButton(
                                onClick = {
                                    SoundEffects.playClick(context)
                                    coroutineScope.launch {
                                        if (pagerState.currentPage > 0) {
                                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                        }
                                    }
                                },
                                enabled = pagerState.currentPage > 0,
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(100.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SleekSurfaceBorder),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = SleekPurple,
                                    disabledContentColor = SleekTextTertiary
                                )
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Previous", fontWeight = FontWeight.Bold)
                            }

                            // Next / Finish Step Button
                            Button(
                                onClick = {
                                    SoundEffects.playClick(context)
                                    coroutineScope.launch {
                                        if (pagerState.currentPage < currentTutorial.steps.size - 1) {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        } else {
                                            onDismiss()
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(100.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SleekPurple,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = if (pagerState.currentPage == currentTutorial.steps.size - 1) "Got It! 🚀" else "Next Step",
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", modifier = Modifier.size(16.dp))
                            }
                        } else {
                            Button(
                                onClick = {
                                    SoundEffects.playClick(context)
                                    onDismiss()
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(100.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SleekPurple,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Close Guide & Play", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
