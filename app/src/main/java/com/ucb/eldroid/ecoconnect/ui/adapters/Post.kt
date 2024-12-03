

data class Post(
    val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val money_goal: Double,
    val deadline: String,
    val user: User
)

data class User(
    val firstname: String,
    val lastname: String,
    val profile: Profile?
)

data class Profile(
    val picture: String?
)
data class Project(
    val id: Int,
    val title: String,
    val description: String,
    val money_goal: String,
    val deadline: String,
    val image: String,
    val image_url: String,
    val user_id: Int,
    val created_at: String,
    val updated_at: String
)



