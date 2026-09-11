package com.desipartygames.data.content

data class AntakshariLetter(
    val devanagari: String,
    val roman: String,
    val sampleSongs: List<String>,
    val era: String = "All"
)

object AntakshariBank {
    val traditionalIntroShloka = "बैठे बैठे क्या करें, करना है कुछ काम...\nशुरू करो अंताक्षरी लेके प्रभु का नाम!"
    val traditionalIntroShlokaRoman = "Baithe baithe kya karein, karna hai kuch kaam...\nShuru karo Antakshari leke Prabhu ka naam!"

    val letters = listOf(
        AntakshariLetter("म", "M", listOf("Mere Samne Wali Khidki Mein", "Mehbooba Mehbooba", "Main Koi Aisa Geet Gaoon", "Makhna", "Maiyya Yashoda")),
        AntakshariLetter("र", "R", listOf("Roop Tera Mastana", "Ruk Ja O Dil Deewane", "Raabta", "Rang Barse Bheege Chunarwali", "Radha Kaise Na Jale")),
        AntakshariLetter("क", "K", listOf("Kabhi Kabhi Mere Dil Mein", "Koi Mil Gaya", "Kal Ho Naa Ho", "Kajra Re", "Khaike Paan Banaraswala")),
        AntakshariLetter("प", "P", listOf("Pehla Nasha", "Pal Pal Dil Ke Paas", "Pyaar Hua Iqraar Hua", "Pee Loon", "Papa Kehte Hain")),
        AntakshariLetter("द", "D", listOf("Dil Deewana Bin Sajna Ke", "Dum Maro Dum", "Dil Diyan Gallan", "Deewani Mastani", "Didi Tera Devar Deewana")),
        AntakshariLetter("त", "T", listOf("Tujhe Dekha Toh Yeh Jaana Sanam", "Tum Hi Ho", "Tere Bina Zindagi Se", "Tip Tip Barsa Paani", "Tera Ban Jaunga")),
        AntakshariLetter("स", "S", listOf("Suraj Hua Maddham", "Saathiya", "Senorita", "Shayrana", "Sandese Aate Hain")),
        AntakshariLetter("न", "N", listOf("Na Tum Jaano Na Hum", "Namo Namo", "Neele Neele Ambar Par", "Nagada Sang Dhol", "Naina Da Kya Kasoor")),
        AntakshariLetter("ल", "L", listOf("Lag Ja Gale Ke Phir", "Lungi Dance", "Laila Main Laila", "London Thumakda", "Luka Chuppi")),
        AntakshariLetter("ब", "B", listOf("Badtameez Dil", "Bole Chudiyan", "Baharon Phool Barsao", "Banjaara", "Bheegey Hont Tere")),
        AntakshariLetter("ज", "J", listOf("Jai Jai Shivshankar", "Jab Koi Baat Bigad Jaaye", "Jashn-e-Bahara", "Jee Karda", "Jaane Tu Ya Jaane Na")),
        AntakshariLetter("ह", "H", listOf("Hawa Hawaii", "Humma Humma", "Hasi Ban Gaye", "Hawayein", "Har Ghadi Badal Rahi Hai")),
        AntakshariLetter("च", "Ch", listOf("Chaiyya Chaiyya", "Chura Liya Hai Tumne", "Chaleya", "Channa Mereya", "Chikni Chameli")),
        AntakshariLetter("य", "Y", listOf("Yeh Dosti Hum Nahi Todenge", "Yeh Shaam Mastani", "Yeh Jawaani Hai Deewani", "Yaar Naa Miley", "Yaad Piya Ki Aane Lagi")),
        AntakshariLetter("ग", "G", listOf("Ghungroo Toot Gaye", "Gallan Goodiyaan", "Gerua", "Ghoomar Ghoomar", "Gulabi Aankhen")),
        AntakshariLetter("अ / आ", "A", listOf("Aap Jaisa Koi", "Aankh Maarey", "Apna Bana Le", "Ajeeb Dastan Hai Yeh", "Abhi Na Jao Chhod Kar")),
        AntakshariLetter("व", "V", listOf("Woh Ladki Hai Kahan", "Vaaste", "Vande Mataram", "Woh Pehli Baar", "Voh Lamhe")),
        AntakshariLetter("भ", "Bh", listOf("Bheege Hont", "Bhole O Bhole", "Bhaag D.K. Bose", "Bhool Bhulaiyaa", "Bhangra Ta Sajda")),
        AntakshariLetter("ख", "Kh", listOf("Khoya Khoya Chand", "Khuda Jaane", "Khabar Nahi", "Khairiyat", "Kaho Na Pyaar Hai")),
        AntakshariLetter("श", "Sh", listOf("Sholay Title Beat", "Sheila Ki Jawani", "Show Me The Thumka", "Shayad", "Shanivaar Raati"))
    )

    val eras = listOf(
        "All Eras",
        "Golden 60s & 70s",
        "Romantic 90s Hits",
        "2000s & 2010s Blockbusters",
        "Latest Party & Punjabi",
        "Devotional & Regional"
    )
}
