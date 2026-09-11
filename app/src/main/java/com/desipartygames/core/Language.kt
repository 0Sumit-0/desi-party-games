package com.desipartygames.core

enum class AppLanguage(val displayName: String, val code: String) {
    ENGLISH("English", "en"),
    HINDI("हिन्दी", "hi"),
    HINGLISH("Hinglish", "hinglish")
}

object AppStrings {
    fun get(key: String, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.HINDI -> hindiStrings[key] ?: englishStrings[key] ?: key
            AppLanguage.HINGLISH -> hinglishStrings[key] ?: englishStrings[key] ?: key
            AppLanguage.ENGLISH -> englishStrings[key] ?: key
        }
    }

    private val englishStrings = mapOf(
        "app_title" to "Desi Party Games",
        "app_subtitle" to "Offline Pass & Play Party Games",
        "pass_phone_title" to "Pass the Phone",
        "pass_to" to "Pass to",
        "hold_to_reveal" to "Hold to Reveal",
        "release_to_hide" to "Release to Hide",
        "tap_to_continue" to "Tap to Continue",
        "next_player" to "Next Player",
        "start_game" to "Start Game",
        "rules" to "How to Play",
        "scoreboard" to "Scoreboard",
        "round" to "Round",
        "points" to "Points",
        "winner" to "Winner",
        "tie" to "It's a Tie!",
        "congratulations" to "Congratulations!",
        "play_again" to "Play Again",
        "home" to "Home",
        "sound_on" to "Sound On",
        "sound_off" to "Sound Off",
        
        // Imposter
        "game_imposter" to "Who's the Imposter?",
        "imposter_desc" to "Find the spy among your friends with secret Indian words!",
        "imposter_role_secret" to "Your Secret Word:",
        "imposter_role_imposter" to "YOU ARE THE IMPOSTER!",
        "imposter_hint" to "Blend in, listen carefully, and bluff your way out!",
        "start_discussion" to "Start Discussion",
        "vote_imposter" to "Vote Out Imposter",
        "accuse_player" to "Who do you suspect?",
        "reveal_imposter" to "Reveal Truth",
        "imposters_count" to "Number of Imposters",
        
        // Raja Mantri
        "game_raja_mantri" to "Raja Mantri Chor Sipahi",
        "raja_mantri_desc" to "The legendary 4-player royal chit guessing game!",
        "pick_chit" to "Pick Your Chit",
        "raja_call" to "Mera Mantri Kaun?",
        "mantri_answer" to "Main hoon, Maharaj!",
        "raja_order" to "Mantri ji, find the Chor and Sipahi!",
        "sipahi_turn" to "Sipahi, identify the Chor!",
        "correct_guess" to "Chor Caught! Sipahi gets 500 pts!",
        "wrong_guess" to "Chor Escaped! Chor gets 500 pts!",
        
        // Truth or Dare
        "game_truth_dare" to "Truth or Dare (Kabhi Kabhi)",
        "truth_dare_desc" to "Spin the bottle for spicy, funny & family truths and dares!",
        "spin_bottle" to "Spin the Bottle",
        "truth" to "Truth",
        "dare" to "Dare",
        "family_pack" to "Family Sanskaari",
        "friends_pack" to "Hostel & Friends Masti",
        "spicy_pack" to "Spicy (18+)",
        "add_custom" to "Add Custom Prompt",
        "forfeit" to "Forfeit / Skip",
        
        // Antakshari
        "game_antakshari" to "Antakshari Master",
        "antakshari_desc" to "Desi music battle with letters, era filters & turn timers!",
        "start_letter" to "Sing on letter:",
        "next_letter" to "Next Letter",
        "lyrics_hint" to "Need a Song Hint?",
        "correct_song" to "Correct (+10)",
        "timeout_song" to "Time's Up (0)",
        
        // Charades
        "game_charades" to "Bollywood Dumb Charades",
        "charades_desc" to "Act out iconic movies, dialogues & cricket moments!",
        "gesture_guide" to "Gestures Cheat Sheet",
        "correct" to "Guessed Correct!",
        "skip" to "Pass / Skip",
        
        // Mini games
        "game_most_likely" to "Most Likely To (Desi Edition)",
        "game_would_you_rather" to "Desi Would You Rather",
        "game_pictionary" to "Desi Doodle / Pictionary",
        "players_groups" to "Manage Players & Groups"
    )

    private val hindiStrings = mapOf(
        "app_title" to "देसी पार्टी गेम्स",
        "app_subtitle" to "ऑफ़लाइन पास और खेलें पार्टी गेम्स",
        "pass_phone_title" to "फ़ोन आगे बढ़ाएं",
        "pass_to" to "दीजिए",
        "hold_to_reveal" to "देखने के लिए दबाए रखें",
        "release_to_hide" to "छिपाने के लिए छोड़ें",
        "tap_to_continue" to "आगे बढ़ने के लिए टैप करें",
        "next_player" to "अगला खिलाड़ी",
        "start_game" to "खेल शुरू करें",
        "rules" to "कैसे खेलें (नियम)",
        "scoreboard" to "स्कोरबोर्ड",
        "round" to "राउंड",
        "points" to "अंक",
        "winner" to "विजेता",
        "tie" to "मैच टाई!",
        "congratulations" to "बधाई हो!",
        "play_again" to "फिर से खेलें",
        "home" to "होम",
        "sound_on" to "आवाज़ चालू",
        "sound_off" to "आवाज़ बंद",
        
        // Imposter
        "game_imposter" to "कौन है जासूस? (इम्पोस्टर)",
        "imposter_desc" to "सीक्रेट देसी शब्दों के साथ अपने दोस्तों में छुपा जासूस ढूंढें!",
        "imposter_role_secret" to "आपका गुप्त शब्द:",
        "imposter_role_imposter" to "आप जासूस (IMPOSTER) हैं!",
        "imposter_hint" to "सबकी बातें ध्यान से सुनें और चालाकी से ब्लफ करें!",
        "start_discussion" to "चर्चा शुरू करें",
        "vote_imposter" to "वोटिंग करें",
        "accuse_player" to "आपको किस पर शक है?",
        "reveal_imposter" to "सच्चाई उजागर करें",
        "imposters_count" to "जासूसों की संख्या",
        
        // Raja Mantri
        "game_raja_mantri" to "राजा मंत्री चोर सिपाही",
        "raja_mantri_desc" to "बचपन का प्रसिद्ध पर्ची वाला शाही खेल!",
        "pick_chit" to "अपनी पर्ची उठाएं",
        "raja_call" to "मेरा मंत्री कौन?",
        "mantri_answer" to "मैं हूँ, महाराज!",
        "raja_order" to "मंत्री जी, चोर और सिपाही का पता लगाएं!",
        "sipahi_turn" to "सिपाही जी, बताइए चोर कौन है?",
        "correct_guess" to "चोर पकड़ा गया! सिपाhi को 500 अंक!",
        "wrong_guess" to "चोर भाग निकला! चोर को 500 अंक!",
        
        // Truth or Dare
        "game_truth_dare" to "सच या हिम्मत (Truth or Dare)",
        "truth_dare_desc" to "बोतल घुमाएं और मज़ेदार सच या डेयर का सामना करें!",
        "spin_bottle" to "बोतल घुमाएं",
        "truth" to "सच (Truth)",
        "dare" to "डेयर (Dare)",
        "family_pack" to "पारिवारिक संस्कारी",
        "friends_pack" to "दोस्ती और हॉस्टल मस्ती",
        "spicy_pack" to "स्पाइसी / 18+",
        "add_custom" to "अपना प्रश्न जोड़ें",
        "forfeit" to "छोड़ें (Penalty)",
        
        // Antakshari
        "game_antakshari" to "अंताक्षरी मास्टर",
        "antakshari_desc" to "अक्षर, टाइमर और गानों के हिंट्स के साथ देसी संगीत मुकाबला!",
        "start_letter" to "इस अक्षर पर गाएं:",
        "next_letter" to "अगला अक्षर",
        "lyrics_hint" to "गाने का हिंट चाहिए?",
        "correct_song" to "सही गाया (+10)",
        "timeout_song" to "समय समाप्त (0)",
        
        // Charades
        "game_charades" to "बॉलीवुड दम शराड्स",
        "charades_desc" to "फ़िल्में, डायलॉग्स और क्रिकेट मोमेंट्स की एक्टिंग करें!",
        "gesture_guide" to "इशारों की गाइड",
        "correct" to "सही पहचाना!",
        "skip" to "पास करें",
        
        // Mini games
        "game_most_likely" to "कौन सबसे ज़्यादा? (Most Likely)",
        "game_would_you_rather" to "यह या वह? (Would You Rather)",
        "game_pictionary" to "देसी डूडल / पिकशनरी",
        "players_groups" to "खिलाड़ी और ग्रुप्स"
    )

    private val hinglishStrings = mapOf(
        "app_title" to "Desi Party Games",
        "app_subtitle" to "Sabka Favorite Offline Adda",
        "pass_phone_title" to "Phone Aage Pass Karo",
        "pass_to" to "Handover to",
        "hold_to_reveal" to "Secret Dekhne Ke Liye Hold Karo",
        "release_to_hide" to "Chhipane Ke Liye Chhodo",
        "tap_to_continue" to "Aage Badhne Ke Liye Tap Karo",
        "next_player" to "Next Khiladi",
        "start_game" to "Game Shuru Karo",
        "rules" to "Khelne Ke Niyam",
        "scoreboard" to "Scoreboard",
        "round" to "Round",
        "points" to "Points",
        "winner" to "Jeet Gaya!",
        "tie" to "Match Tie Ho Gaya!",
        "congratulations" to "Mubarak Ho!",
        "play_again" to "Ek Aur Round!",
        "home" to "Adda Home",
        "sound_on" to "Sound ON",
        "sound_off" to "Sound OFF",
        
        // Imposter
        "game_imposter" to "Who's the Imposter? (Kaun Hai Jasoos)",
        "imposter_desc" to "Apne dosto mein chhupe imposter ko pakdo desi words se!",
        "imposter_role_secret" to "Aapka Secret Word:",
        "imposter_role_imposter" to "AREY AAP HI IMPOSTER HO!",
        "imposter_hint" to "Sabki batein suno aur smart bluff maar ke bacho!",
        "start_discussion" to "Charcha Shuru Karo",
        "vote_imposter" to "Voting Shuru Karo",
        "accuse_player" to "Kispe sabse zyada shak hai?",
        "reveal_imposter" to "Asliyat Saamne Lao",
        "imposters_count" to "Kitne Imposters",
        
        // Raja Mantri
        "game_raja_mantri" to "Raja Mantri Chor Sipahi",
        "raja_mantri_desc" to "Asli digital chits wala classic game!",
        "pick_chit" to "Apni Kismat Ki Parchi Uthao",
        "raja_call" to "Mera Mantri Kaun?",
        "mantri_answer" to "Main hoon, Maharaj!",
        "raja_order" to "Mantri ji, Chor aur Sipahi ka pata lagao!",
        "sipahi_turn" to "Sipahi ji, Chor kaun hai guess karo!",
        "correct_guess" to "Chor Pakda Gaya! Sipahi ko 500!",
        "wrong_guess" to "Chor Nikal Gaya! Chor ko 500!",
        
        // Truth or Dare
        "game_truth_dare" to "Truth or Dare (Masti & Roast)",
        "truth_dare_desc" to "Bottle ghumaao aur juicy desi truths & dares khelo!",
        "spin_bottle" to "Bottle Ghumaao",
        "truth" to "Sach (Truth)",
        "dare" to "Himmat (Dare)",
        "family_pack" to "Sanskaari Family Pack",
        "friends_pack" to "Hostel & Friends Masti",
        "spicy_pack" to "Spicy 18+ (Dating & Confessions)",
        "add_custom" to "Apna Custom Prompt Daalo",
        "forfeit" to "Darr Gaye? (Forfeit)",
        
        // Antakshari
        "game_antakshari" to "Antakshari Mehfil",
        "antakshari_desc" to "Baithe baithe kya karein, shuru karo Antakshari!",
        "start_letter" to "Is Akshar pe gaana gao:",
        "next_letter" to "Agla Akshar",
        "lyrics_hint" to "Gaana yaad nahi aa raha? Hint lo!",
        "correct_song" to "Sahi Gaya (+10)",
        "timeout_song" to "Time Khatam (0)",
        
        // Charades
        "game_charades" to "Bollywood Dumb Charades",
        "charades_desc" to "Iconic filmon, dialogues aur cricket moments ki acting karo!",
        "gesture_guide" to "Isharon Ki Guide",
        "correct" to "Sahi Pehchana (+1)",
        "skip" to "Pass / Skip",
        
        // Mini games
        "game_most_likely" to "Kaun Sabse Zyada? (Most Likely)",
        "game_would_you_rather" to "Yeh Ya Woh (Desi Edition)",
        "game_pictionary" to "Desi Pictionary / Doodle",
        "players_groups" to "Gang & Players List"
    )
}
