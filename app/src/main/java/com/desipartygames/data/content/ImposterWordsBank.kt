package com.desipartygames.data.content

data class ImposterCategory(
    val id: String,
    val title: String,
    val titleHindi: String,
    val icon: String,
    val words: List<String>
)

object ImposterWordsBank {
    val categories = listOf(
        ImposterCategory(
            id = "bollywood",
            title = "Bollywood Superstars",
            titleHindi = "बॉलीवुड सितारे",
            icon = "🎬",
            words = listOf(
                "Shah Rukh Khan", "Amitabh Bachchan", "Deepika Padukone", "Salman Khan",
                "Alia Bhatt", "Rajinikanth", "Kareena Kapoor", "Ranveer Singh",
                "Priyanka Chopra", "Hrithik Roshan", "Katrina Kaif", "Akshay Kumar",
                "Ranbir Kapoor", "Aishwarya Rai", "Aamir Khan", "Kajol",
                "Anushka Sharma", "Vicky Kaushal", "Madhuri Dixit", "Govinda"
            )
        ),
        ImposterCategory(
            id = "cricket",
            title = "Cricket Legends & Stars",
            titleHindi = "क्रिकेट के सितारे",
            icon = "🏏",
            words = listOf(
                "Virat Kohli", "MS Dhoni", "Sachin Tendulkar", "Rohit Sharma",
                "Kapil Dev", "Hardik Pandya", "Jasprit Bumrah", "Sourav Ganguly",
                "Yuvraj Singh", "Shubman Gill", "Rahul Dravid", "Rishabh Pant",
                "Shikhar Dhawan", "Virender Sehwag", "Suryakumar Yadav", "KL Rahul",
                "Ravindra Jadeja", "Anil Kumble", "Mohammed Shami", "Gautam Gambhir"
            )
        ),
        ImposterCategory(
            id = "food",
            title = "Desi Street Food & Dishes",
            titleHindi = "देसी स्ट्रीट फ़ूड और पकवान",
            icon = "🍛",
            words = listOf(
                "Pani Puri / Golgappe", "Butter Chicken", "Masala Dosa", "Hyderabadi Biryani",
                "Vada Pav", "Gulab Jamun", "Chole Bhature", "Pav Bhaji",
                "Samosa with Adrak Chai", "Jalebi Rabri", "Rajma Chawal", "Litti Chokha",
                "Kaju Katli", "Dal Makhani", "Momos with Spicy Chutney", "Rasgulla",
                "Poha Jalebi", "Aloo Paratha with Makhan", "Rasmalai", "Dhokla"
            )
        ),
        ImposterCategory(
            id = "festivals",
            title = "Indian Festivals & Functions",
            titleHindi = "भारतीय त्योहार और शादियां",
            icon = "🪔",
            words = listOf(
                "Diwali Fireworks & Sweets", "Holi Gulal & Gujiya", "Big Fat Indian Wedding",
                "Durga Puja Pandal", "Ganesh Chaturthi Visarjan", "Eid Biryani Feast",
                "Navratri Garba Night", "Raksha Bandhan", "Sangeet Dance Performance",
                "Baraat on Ghodi", "Karwa Chauth", "Lohri Bonfire",
                "Haldi Ceremony", "Makar Sankranti Kite Flying", "Onam Sadya", "Chhath Puja"
            )
        ),
        ImposterCategory(
            id = "cities",
            title = "Indian Cities & Hotspots",
            titleHindi = "भारतीय शहर और मशहूर जगहें",
            icon = "🏰",
            words = listOf(
                "Mumbai Local Train", "Delhi Chandni Chowk", "Goa Baga Beach",
                "Bengaluru Silk Board Traffic", "Taj Mahal Agra", "Varanasi Ganga Ghats",
                "Jaipur Hawa Mahal", "Kolkata Howrah Bridge", "Manali Snow Mountains",
                "Hyderabad Charminar", "Amritsar Golden Temple", "Kerala Backwaters",
                "Udaipur Lake Palace", "Rishikesh River Rafting", "Ladakh Pangong Lake"
            )
        ),
        ImposterCategory(
            id = "tv_memes",
            title = "Desi TV, Shows & Memes",
            titleHindi = "देसी टीवी शोज़ और मीम्स",
            icon = "📺",
            words = listOf(
                "Taarak Mehta Ka Ooltah Chashmah", "CID (Daya Tod Do Darwaza)",
                "Kyunki Saas Bhi Kabhi Bahu Thi", "Mirzapur (Kaleen Bhaiya)",
                "Sacred Games (Gaitonde)", "Shark Tank India (Yeh Sab Doglapan Hai)",
                "Sarabhai vs Sarabhai (Monisha Beta)", "Khichdi (Hansa & Praful)",
                "Bigg Boss Weekend Ka Vaar", "Shaktimaan", "Crime Patrol", "KBC (Lock Kiya Jaaye)"
            )
        )
    )
}
