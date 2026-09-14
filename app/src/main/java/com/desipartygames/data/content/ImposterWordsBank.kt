package com.desipartygames.data.content

data class ImposterCategory(
    val id: String,
    val title: String,
    val titleHindi: String,
    val icon: String,
    val words: List<String>
)

object ImposterWordsBank {
    private val playableCategories = listOf(
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
                "Anushka Sharma", "Vicky Kaushal", "Madhuri Dixit", "Govinda",
                "Varun Dhawan", "Taapsee Pannu", "Ayushmann Khurrana", "Kangana Ranaut",
                "Rajkummar Rao", "Shraddha Kapoor", "Nawazuddin Siddiqui", "Vidya Balan",
                "Pankaj Tripathi", "Tabu", "Sanya Malhotra", "Shahid Kapoor",
                "Rani Mukerji", "Manoj Bajpayee", "Bhumi Pednekar", "Saif Ali Khan",
                "Kriti Sanon", "Irrfan Khan", "R Madhavan", "Sonam Kapoor",
                "John Abraham", "Juhi Chawla", "Farhan Akhtar", "Konkona Sen Sharma"
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
                , "Ruturaj Gaikwad", "Shreyas Iyer", "Ishan Kishan", "Sanju Samson",
                "Axar Patel", "Kuldeep Yadav", "Bhuvneshwar Kumar", "Harbhajan Singh",
                "VVS Laxman", "Sanjay Manjrekar", "Gujarat Titans", "Suryakumar Yadav",
                "Washington Sundar", "Rinku Singh", "Mohammed Siraj", "Ravichandran Ashwin",
                "Zaheer Khan", "Suresh Raina", "Gautam Gambhir", "Ajinkya Rahane",
                "Dinesh Karthik", "Ambati Rayudu", "Manish Pandey", "Robin Uthappa",
                "Shikhar Dhawan", "Yuzvendra Chahal", "Umesh Yadav", "Vijay Shankar"
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
                , "Kathi Roll", "Misal Pav", "Pesarattu", "Amritsari Kulcha",
                "Malai Kofta", "Kheer", "Kachori", "Thepla",
                "Dabeli", "Mysore Pak", "Lassi", "Chicken Tikka",
                "Appam with Stew", "Pongal", "Sarson Ka Saag", "Makki Di Roti",
                "Bhutta Masala", "Kulfi Falooda", "Thandai", "Puri Bhaji",
                "Methi Matar Malai", "Gajar Ka Halwa", "Bhel Puri", "Idli Sambar",
                "Rogan Josh", "Modak", "Khandvi", "Chakli"
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
                , "Janmashtami Dahi Handi", "Mahashivratri Temple Visit", "Baisakhi Harvest Fair",
                "Pongal Kolam", "Buddha Purnima", "Bhai Dooj", "Akshaya Tritiya",
                "Holi Water Balloons", "Diwali Rangoli", "Christmas Midnight Mass", "Eid Namaz",
                "Lohri Dance", "Navratri Dandiya", "Ganesh Visarjan Procession", "Karva Chauth Sargi",
                "Independence Day Parade", "Republic Day Flag Hoisting", "Teacher's Day Celebration", "Children's Day Function",
                "Wedding Mehendi", "Wedding Reception", "Baby Shower Godh Bharai", "Housewarming Puja",
                "Thread Ceremony", "Puja at Home", "New Year Countdown", "Basant Panchami"
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
                , "Pune Shaniwar Wada", "Lucknow Bara Imambara", "Mysuru Palace",
                "Darjeeling Toy Train", "Andaman Radhanagar Beach", "Kochi Fort Area",
                "Jodhpur Mehrangarh Fort", "Pushkar Camel Fair", "Shimla Mall Road",
                "Agra Fatehpur Sikri", "Bhopal Upper Lake", "Chennai Marina Beach",
                "Ahmedabad Sabarmati Ashram", "Srinagar Dal Lake", "Nashik Vineyards",
                "Munnar Tea Gardens", "Ooty Botanical Garden", "Mahabaleshwar Hills",
                "Kanyakumari Sunset", "Bhubaneswar Temples", "Guwahati Kamakhya Temple",
                "Patna Gandhi Maidan", "Noida Film City", "Surat Diamond Market",
                "Nagpur Orange City", "Coimbatore Waterfalls", "Vijayawada Kanaka Durga Temple"
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
                , "The Kapil Sharma Show", "Indian Idol Auditions", "Kaun Banega Crorepati Lifeline",
                "Panchayat Pradhan Ji", "Mirzapur Guddu Bhaiya", "FIR Chandramukhi Chautala",
                "Wagle Ki Duniya", "Office Office Musaddi Lal", "Hum Paanch", "Malgudi Days",
                "Flop Show Jaspal Bhatti", "Ramayan Ramanand Sagar", "Mahabharat B.R. Chopra",
                "Saath Nibhaana Saathiya Meme", "Anupamaa Rupali Ganguly", "Yeh Rishta Kya Kehlata Hai",
                "Roadies Gang Leader", "Splitsvilla Task", "MTV Hustle Rap Battle", "Indian Matchmaking",
                "Scam 1992 Harshad Mehta", "The Family Man Srikant Tiwari", "Paatal Lok Hathoda Tyagi",
                "Kota Factory Jeetu Bhaiya", "Aspirants UPSC Prep", "TVF Pitchers Startup", "Meme of Rasode Mein Kaun Tha"
            )
        )
    ).map { category ->
        category.copy(words = category.words.take(30))
    }

    val allCategories = ImposterCategory(
        id = "all",
        title = "All Categories",
        titleHindi = "सभी श्रेणियां",
        icon = "🎲",
        words = playableCategories.flatMap { it.words }
    )

    val categories = listOf(allCategories) + playableCategories
}
