package com.ucb.eldroid.ecoconnect.data.models

class Project(
    val projectId: String? = null,   // Nullable projectId, will be assigned later
    val title: String,
    val description: String,
    val goalAmount: Double,
    val deadline: String,
    val imagePath: String?,
    val projectUser: ProjectUser // Include User data class
)

data class ProjectUser(
    val first_name: String,
    val last_name: String,
    val profile: Profile? // Include Profile data class
)
data class Profile(
    val picture: String?
)
