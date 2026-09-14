package com.desipartygames.data.content

data class TruthDarePrompt(
    val id: String,
    val type: String, // "TRUTH" or "DARE"
    val category: String, // "FAMILY", "FRIENDS", "SPICY"
    val textEn: String,
    val textHi: String,
    val textHinglish: String
)

object TruthDareBank {
    val prompts = listOf(
        // Family Truths
        TruthDarePrompt(
            "ft1", "TRUTH", "FAMILY",
            "What is the biggest lie you ever told your parents?",
            "आपने अपने माता-पिता से बोला सबसे बड़ा झूठ क्या था?",
            "Mummy-Papa se bola hua sabse bada jhooth kya tha?"
        ),
        TruthDarePrompt(
            "ft2", "TRUTH", "FAMILY",
            "Which family relative's phone call do you avoid the most?",
            "किस रिश्तेदार का फ़ोन आप सबसे ज़्यादा इग्नोर करते हैं?",
            "Kis rishtedaar ka phone call aap sabse zyada avoid karte ho?"
        ),
        TruthDarePrompt(
            "ft3", "TRUTH", "FAMILY",
            "What is the most embarrassing thing your parents caught you doing?",
            "माता-पिता ने आपको कौन सा सबसे अजीब काम करते पकड़ा था?",
            "Gharwalo ne aapko sabse embarrassing kis kaam mein pakda tha?"
        ),
        TruthDarePrompt(
            "ft4", "TRUTH", "FAMILY",
            "If you had to trade one sibling or cousin for 1 Crore rupees, who would it be?",
            "अगर 1 करोड़ रुपये मिलें तो किस भाई/बहन को एक्सचेंज करेंगे?",
            "Agar 1 Crore mile toh kis bhai/behen ya cousin ko trade karoge?"
        ),
        
        // Family Dares
        TruthDarePrompt(
            "fd1", "DARE", "FAMILY",
            "Dance to a classic Govinda or Mithun song for 45 seconds!",
            "गोविंदा या मिथुन के गाने पर 45 सेकंड तक ज़बरदस्त डांस करें!",
            "Govinda ya Mithun ke song pe 45 seconds tak zabardast dance karo!"
        ),
        TruthDarePrompt(
            "fd2", "DARE", "FAMILY",
            "Do a dramatic mimicry of any family member sitting in the room.",
            "कमरे में बैठे किसी भी पारिवारिक सदस्य की ड्रामेटिक मिमिक्री करें।",
            "Room mein baithe kisi bhi family member ki dramatic mimicry karke dikhao."
        ),
        TruthDarePrompt(
            "fd3", "DARE", "FAMILY",
            "Sing a bhajan or devotional song in heavy rock/rap style.",
            "किसी भजन को रॉक या रैप स्टाइल में गाकर सुनाएं।",
            "Kisi bhajan ko heavy rap/rock style mein gaa ke sunao."
        ),
        TruthDarePrompt(
            "fd4", "DARE", "FAMILY",
            "Touch the feet of the youngest person in the room and ask for blessings.",
            "कमरे के सबसे छोटे सदस्य के पैर छूकर आशीर्वाद मांगें।",
            "Room ke sabse chhote member ke pair chhookar aashirwaad maango."
        ),

        // Friends / Hostel Truths
        TruthDarePrompt(
            "frt1", "TRUTH", "FRIENDS",
            "Who was your first secret crush, and did they ever find out?",
            "आपका पहला सीक्रेट क्रश कौन था, क्या उन्हें कभी पता चला?",
            "Aapka pehla secret crush kaun tha aur kya unhe kabhi pata chala?"
        ),
        TruthDarePrompt(
            "frt2", "TRUTH", "FRIENDS",
            "Have you ever stalked an ex or crush from a fake Instagram account?",
            "क्या आपने कभी फ़ेक आईडी बनाकर किसी को इंस्टाग्राम पर स्टॉक किया है?",
            "Kya kabhi fake account bana ke ex ya crush ko stalk kiya hai?"
        ),
        TruthDarePrompt(
            "frt3", "TRUTH", "FRIENDS",
            "What is the most ridiculous excuse you gave to skip a college class or office meeting?",
            "क्लास या ऑफिस बंक करने के लिए सबसे बेतुका बहाना क्या दिया था?",
            "Class ya office bunk karne ke liye sabse faltu bahana kya mara tha?"
        ),
        TruthDarePrompt(
            "frt4", "TRUTH", "FRIENDS",
            "Who among the players here would survive the shortest in a zombie apocalypse?",
            "यहां बैठे दोस्तों में से ज़ोंबी हमले में सबसे पहले कौन निपटेगा?",
            "Yahan baithe dosto mein zombie apocalypse mein sabse pehle kaun jayega?"
        ),
        TruthDarePrompt(
            "frt5", "TRUTH", "FRIENDS",
            "Have you ever eaten someone else's labeled food from the hostel/office fridge?",
            "क्या कभी हॉस्टल या ऑफिस फ्रिज से किसी और का खाना चुराकर खाया है?",
            "Hostel ya office fridge se kisi aur ka khana chori karke khaya hai kabhi?"
        ),

        // Friends / Hostel Dares
        TruthDarePrompt(
            "frd1", "DARE", "FRIENDS",
            "Call a friend or food delivery place and order 1 plate of 'Steamed Ice Cream'.",
            "किसी दोस्त या रेस्टोरेंट को कॉल करके 'उबली हुई आइसक्रीम' का ऑर्डर दें।",
            "Kisi dost ya hotel ko call karke 'Garam ubali hui Ice Cream' ka order maango."
        ),
        TruthDarePrompt(
            "frd2", "DARE", "FRIENDS",
            "Send a message 'We need to talk urgently...' to the 3rd person on your WhatsApp chat list.",
            "अपने व्हाट्सएप के तीसरे कांटेक्ट को 'हमें बहुत ज़रूरी बात करनी है...' मैसेज भेजें।",
            "Apne WhatsApp ke 3rd contact ko 'Humein urgent baat karni hai...' bhej do."
        ),
        TruthDarePrompt(
            "frd3", "DARE", "FRIENDS",
            "Put your phone on speaker and read the last 5 search queries in your YouTube/Google history.",
            "स्पीकर ऑन करके अपनी यूट्यूब/गूगल सर्च हिस्ट्री के पिछले 5 शब्द ज़ोर से पढ़ें।",
            "Speaker on karke Google/YouTube search history ke last 5 queries loudly padho."
        ),
        TruthDarePrompt(
            "frd4", "DARE", "FRIENDS",
            "Act like a loud, pushy Indian street vendor trying to sell a pen for ₹5000 to the group.",
            "सब्ज़ी या कपड़े वाले की आवाज़ में ₹5000 का पेन बेचने की ज़बरदस्त एक्टिंग करें।",
            "Market ke dukandaar ki awaaz mein ₹5000 ka pen group ko bechne ki acting karo."
        ),

        // Spicy / 18+ Truths (Opt-in)
        TruthDarePrompt(
            "st1", "TRUTH", "SPICY",
            "What is the most awkward or hilarious dating disaster you've ever experienced?",
            "आपकी लाइफ की सबसे ऑकवर्ड या मज़ेदार डेटिंग स्टोरी क्या है?",
            "Aapki life ka sabse awkward ya funny dating disaster kya tha?"
        ),
        TruthDarePrompt(
            "st2", "TRUTH", "SPICY",
            "Have you ever had a crush on a friend's partner or sibling?",
            "क्या आपको कभी किसी दोस्त के पार्टनर या भाई/बहन पर क्रश हुआ है?",
            "Kya kabhi kisi dost ke partner ya sibling pe crush hua hai?"
        ),
        TruthDarePrompt(
            "st3", "TRUTH", "SPICY",
            "What is your biggest red flag in a relationship that you secretly possess?",
            "रिलेशनशिप में आपका सबसे बड़ा 'रेड फ़्लैग' क्या है?",
            "Dating mein aapka sabse bada 'Red Flag' kya hai jo aap chupate ho?"
        ),
        TruthDarePrompt(
            "st4", "TRUTH", "SPICY",
            "Who in this room would you most likely consider going on a date with?",
            "इस कमरे में बैठे लोगों में से आप किसके साथ डेट पर जाना पसंद करेंगे?",
            "Is room mein baithe logon mein se aap kiske sath date pe jaana chahoge?"
        ),

        // Spicy / 18+ Dares
        TruthDarePrompt(
            "sd1", "DARE", "SPICY",
            "Give a 30-second seductive Bollywood slow-motion dialogue delivery to the person on your left.",
            "अपने बाईं ओर बैठे व्यक्ति को 30 सेकंड तक स्लो मोशन रोमांटिक अंदाज़ में डायलॉग बोलें।",
            "Left side baithe player ko 30 seconds tak super dramatic romantic dialogue sunao."
        ),
        TruthDarePrompt(
            "sd2", "DARE", "SPICY",
            "Post a selfie with a cheesy Bollywood pickup line as your WhatsApp status for 15 minutes.",
            "व्हाट्सएप स्टेटस पर एक फ़िल्मी पिकअप लाइन के साथ अपनी सेल्फ़ी 15 मिनट के लिए लगाएं।",
            "WhatsApp status pe ek cheesy Bollywood pickup line ke sath selfie daalo 15 min ke liye."
        ),
        TruthDarePrompt(
            "sd3", "DARE", "SPICY",
            "Let the player to your right send any one single emoji reply to the last person you texted.",
            "अपने दाईं ओर वाले खिलाड़ी को अपनी पिछली चैट पर कोई भी एक इमोजी भेजने दें।",
            "Right side wale player ko aapki last personal chat pe koi bhi emoji send karne do."
        )
    ).filterNot { it.category == "SPICY" }
}
