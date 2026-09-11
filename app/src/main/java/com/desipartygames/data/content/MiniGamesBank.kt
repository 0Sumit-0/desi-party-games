package com.desipartygames.data.content

data class MostLikelyPrompt(
    val id: String,
    val textEn: String,
    val textHi: String,
    val textHinglish: String
)

data class WouldYouRatherPrompt(
    val id: String,
    val optionA_En: String,
    val optionB_En: String,
    val optionA_Hi: String,
    val optionB_Hi: String,
    val optionA_Hinglish: String,
    val optionB_Hinglish: String
)

object MiniGamesBank {
    val mostLikelyPrompts = listOf(
        MostLikelyPrompt(
            "ml1",
            "Who is most likely to crash a wedding just to eat free Paneer Butter Masala?",
            "कौन सिर्फ़ मुफ़्त का पनीर बटर मसाला खाने किसी भी शादी में घुस जाएगा?",
            "Kaun bas free ka Paneer Butter Masala khane kisi anjaan shaadi mein ghus jayega?"
        ),
        MostLikelyPrompt(
            "ml2",
            "Who is most likely to forward fake news in the family WhatsApp group?",
            "फ़ैमिली व्हाट्सएप ग्रुप में फ़र्ज़ी अफ़वाह कौन सबसे पहले फ़ॉरवर्ड करेगा?",
            "Family WhatsApp group pe fake forward message sabse pehle kaun bhejega?"
        ),
        MostLikelyPrompt(
            "ml3",
            "Who is most likely to fight with the auto-rickshaw driver over 10 Rupees?",
            "ऑटो वाले भैया से ₹10 के लिए 20 मिनट तक कौन बहस करेगा?",
            "Auto bhaiya se ₹10 ke liye 20 minute tak kaun ladega?"
        ),
        MostLikelyPrompt(
            "ml4",
            "Who is most likely to secretly cry while watching a dramatic Bollywood movie?",
            "इमोशनल फ़िल्म देखते समय छुपकर आंसू कौन बहाता है?",
            "Emotional Bollywood movie dekhte waqt chupke se kaun rota hai?"
        ),
        MostLikelyPrompt(
            "ml5",
            "Who is most likely to say 'I am 5 minutes away' when they haven't even taken a shower?",
            "नहाए बिना भी 'बस 5 मिनट में पहुंच रहा हूँ' का झूठ कौन बोलेगा?",
            "Abhi nahaya bhi nahi hoga aur 'Bas 5 min mein pahunch raha hoon' kaun bolega?"
        ),
        MostLikelyPrompt(
            "ml6",
            "Who is most likely to pack 5 pairs of clothes for a 2-day weekend trip?",
            "दो दिन के वीकेंड ट्रिप के लिए 5 जोड़ी कपड़े और एक्स्ट्रा बैग कौन ले जाएगा?",
            "2 din ke weekend trip ke liye 15 outfits kaun pack karega?"
        ),
        MostLikelyPrompt(
            "ml7",
            "Who is most likely to become a Bigg Boss contestant and start fights on day 1?",
            "बिग बॉस में जाकर पहले ही दिन बर्तन और खाने पर लड़ाई कौन शुरू करेगा?",
            "Bigg Boss mein jaake day 1 pe hi bartan aur khane pe ladai kaun karega?"
        )
    )

    val wouldYouRatherPrompts = listOf(
        WouldYouRatherPrompt(
            "wyr1",
            "Never drink Masala Chai again",
            "Never eat Golgappe / Pani Puri again",
            "ज़िंदगी भर मसाला चाय कभी न पीना",
            "ज़िंदगी भर पानी पूरी / गोलगप्पे कभी न खाना",
            "Life mein kabhi Masala Chai nahi peena",
            "Life mein kabhi Pani Puri / Golgappe nahi khana"
        ),
        WouldYouRatherPrompt(
            "wyr2",
            "Get stuck in Bangalore traffic with your mother-in-law for 4 hours",
            "Attend an 8-hour boring distant relative wedding in peak summer",
            "सासू मां के साथ 4 घंटे बेंगलुरु के भारी ट्रैफ़िक में फंसे रहना",
            "मई की भीषण गर्मी में दूर के रिश्तेदार की 8 घंटे लंबी शादी अटेंड करना",
            "Saasu maa ke sath 4 ghante Bangalore traffic mein phasna",
            "May ki dhoop mein 8 ghante boring rishtedaar ki shaadi jhelna"
        ),
        WouldYouRatherPrompt(
            "wyr3",
            "Have your entire WhatsApp chat history read aloud by Amitabh Bachchan",
            "Have your browser search history printed in the morning newspaper",
            "अमिताभ बच्चन की दमदार आवाज़ में अपने सारे व्हाट्सएप चैट ज़ोर से पढ़वाना",
            "सुबह के अख़बार के मुख्य पृष्ठ पर अपनी पूरी इंटरनेट सर्च हिस्ट्री छपवाना",
            "Amitabh Bachchan ki awaaz mein saari WhatsApp chat public mein padhwana",
            "Subah ke newspaper mein apni secret Google search history print karwana"
        ),
        WouldYouRatherPrompt(
            "wyr4",
            "Dance the Naagin dance at every wedding you attend forever",
            "Always speak in dramatic dialogue rhyming like Navjot Singh Sidhu",
            "हर शादी में ज़मीन पर लेटकर नागिन डांस करना",
            "हमेशा सिद्धू पाजी के अंदाज़ में शायरी और ठहाकों में बात करना",
            "Har shaadi mein compulsory Naagin dance karna",
            "Hamesha Sidhu paaji ke dramatic shayari style mein baat karna"
        )
    )

    val pictionaryPrompts = listOf(
        "Pani Puri Stall", "Rickshaw with colorful horn", "Taj Mahal",
        "Cup of Cutting Chai", "Cricket Bat & Stumps", "Diwali Diya & Rangoli",
        "Police Walrus Mustache", "Royal Crown on Cushion", "Indian Wedding Horse (Ghodi)",
        "Pressure Cooker Whistle", "Samosa with Red Chutney", "Kite Flying in Sky",
        "Bollywood Hero Sunglasses", "Hostel Maggi Bowl", "Peacock Dancing in Rain"
    )
}
