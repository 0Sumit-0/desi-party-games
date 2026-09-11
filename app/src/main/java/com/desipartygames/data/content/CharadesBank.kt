package com.desipartygames.data.content

data class CharadesItem(
    val title: String,
    val category: String,
    val hints: String = "",
    val difficulty: String = "Easy"
)

data class CharadesCategory(
    val id: String,
    val title: String,
    val icon: String,
    val description: String,
    val items: List<CharadesItem>
)

object CharadesBank {
    val categories: List<CharadesCategory> = listOf(
        CharadesCategory(
            id = "movies_easy",
            title = "Iconic Bollywood Movies",
            icon = "🎬",
            description = "Classic blockbusters everyone knows",
            items = listOf(
                CharadesItem("Sholay", "Iconic Bollywood", "Gabbar Singh, Jai Veeru, Basanti (1975)", "Easy"),
                CharadesItem("3 Idiots", "Iconic Bollywood", "All Izz Well college drama", "Easy"),
                CharadesItem("Dilwale Dulhania Le Jayenge", "Iconic Bollywood", "Raj & Simran train scene (1995)", "Easy"),
                CharadesItem("Kuch Kuch Hota Hai", "Iconic Bollywood", "Rahul, Anjali, Basketball summer camp", "Easy"),
                CharadesItem("Lagaan", "Iconic Bollywood", "Cricket match for tax waiver (2001)", "Easy"),
                CharadesItem("Krrish", "Iconic Bollywood", "Black mask superhero jumping from towers", "Easy"),
                CharadesItem("Dangal", "Iconic Bollywood", "Mahavir Phogat wrestling training", "Easy"),
                CharadesItem("Bahubali", "Iconic Bollywood", "Lifting Shivling & Katappa mystery", "Easy"),
                CharadesItem("Pushpa", "Iconic Bollywood", "Main Jhukega Nahi signature beard gesture", "Easy"),
                CharadesItem("Chennai Express", "Iconic Bollywood", "Meenamma & lungi dance journey", "Easy")
            )
        ),
        CharadesCategory(
            id = "movies_hardcore",
            title = "Hardcore Bizarre Titles",
            icon = "🤯",
            description = "Unusual long titles only true pros can act",
            items = listOf(
                CharadesItem("Albert Pinto Ko Gussa Kyoon Aata Hai", "Hardcore Bizarre", "Classic 80s Naseeruddin Shah movie", "Legend"),
                CharadesItem("Jal Bin Machhli Nritya Bin Bijli", "Hardcore Bizarre", "1971 Shantaram musical color riot", "Legend"),
                CharadesItem("Salim Langde Pe Mat Ro", "Hardcore Bizarre", "Saeed Mirza cult film", "Legend"),
                CharadesItem("Dulhan Wahi Jo Piya Man Bhaaye", "Hardcore Bizarre", "1977 Rajshri classic", "Medium"),
                CharadesItem("Paap Ko Jalaa Kar Raakh Kar Doonga", "Hardcore Bizarre", "1988 Dharmendra blockbuster", "Legend"),
                CharadesItem("Arvind Desai Ki Ajeeb Dastaan", "Hardcore Bizarre", "Offbeat art house drama", "Legend"),
                CharadesItem("Ghar Me Ram Gali Me Shyam", "Hardcore Bizarre", "80s double identity comedy", "Medium"),
                CharadesItem("Matru Ki Bijlee Ka Mandola", "Hardcore Bizarre", "Quirky comedy with pink buffalo", "Medium"),
                CharadesItem("Andaz Apna Apna", "Hardcore Bizarre", "Amar Prem & Crime Master Gogo", "Medium"),
                CharadesItem("Gangs of Wasseypur", "Hardcore Bizarre", "Baap ka dada ka sabka badla lega Faizal", "Medium")
            )
        ),
        CharadesCategory(
            id = "dialogues",
            title = "Iconic Desi Dialogues",
            icon = "🗣️",
            description = "Act out memorable Indian dialogues without speaking",
            items = listOf(
                CharadesItem("Mogambo Khush Hua", "Famous Dialogues", "Mr. India villain signature line", "Easy"),
                CharadesItem("Kitne Aadmi The?", "Famous Dialogues", "Gabbar Singh on rocky cliffs", "Easy"),
                CharadesItem("Picture Abhi Baaki Hai Mere Dost", "Famous Dialogues", "SRK Om Shanti Om climax dialogue", "Easy"),
                CharadesItem("Don Ko Pakadna Mushkil Hi Nahi Namumkin Hai", "Famous Dialogues", "Amitabh & SRK Don dialogue", "Easy"),
                CharadesItem("Pushpa, I Hate Tears Re", "Famous Dialogues", "Rajesh Khanna in Amar Prem", "Medium"),
                CharadesItem("Tareekh Pe Tareekh!", "Famous Dialogues", "Sunny Deol Damini courtroom roar", "Easy"),
                CharadesItem("Crime Master Gogo, Aankhein Nikaal Ke Gotiyan Khelunga", "Famous Dialogues", "Andaz Apna Apna", "Medium"),
                CharadesItem("Parampara, Pratishtha, Anushasan", "Famous Dialogues", "Mohabbatein Gurukul principal rules", "Medium")
            )
        ),
        CharadesCategory(
            id = "cricket",
            title = "Epic Cricket Moments",
            icon = "🏏",
            description = "Legendary Indian cricket celebrations & shots",
            items = listOf(
                CharadesItem("MS Dhoni 2011 World Cup Winning Six", "Cricket", "Lifting bat, ball soaring into crowd", "Easy"),
                CharadesItem("Sourav Ganguly Waving Shirt at Lord's Balcony", "Cricket", "NatWest 2002 shirtless celebration", "Easy"),
                CharadesItem("Virat Kohli Six Against Haris Rauf at MCG", "Cricket", "Back foot straight punch over long-on 2022", "Medium"),
                CharadesItem("Kapil Dev 175 Not Out Running Catch 1983", "Cricket", "1983 World Cup finals running backward catch", "Medium"),
                CharadesItem("Sachin Tendulkar Desert Storm in Sharjah", "Cricket", "1998 blasting Warne and Kasprowicz in sandstorm", "Medium"),
                CharadesItem("Ravindra Jadeja Sword Celebration", "Cricket", "Twirling bat like Rajput sword", "Easy")
            )
        ),
        CharadesCategory(
            id = "desi_action",
            title = "Desi Moments & Habits",
            icon = "🍛",
            description = "Everyday Indian quirks and street situations",
            items = listOf(
                CharadesItem("Bargaining for Free Dhaniya-Mirchi", "Desi Actions", "Asking vegetable vendor for free coriander", "Easy"),
                CharadesItem("Catching Mumbai Local Train in Peak Rush Hour", "Desi Actions", "Hanging on door with backpack", "Easy"),
                CharadesItem("Relative Forcing 500 Rupee Note into Pocket", "Desi Actions", "Saying 'Nahi nahi uncle' with open pocket", "Easy"),
                CharadesItem("Baraat Naagin Dance on the Road", "Desi Actions", "Lying on ground playing imaginary pungi", "Easy"),
                CharadesItem("Watching India vs Pakistan Match with Neighbours", "Desi Actions", "Biting nails screaming 'Out hai!'", "Easy")
            )
        )
    )
}

