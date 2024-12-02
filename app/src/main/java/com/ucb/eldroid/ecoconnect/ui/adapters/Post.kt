package com.ucb.eldroid.ecoconnect.ui.adapters

data class Post(
    val content: String,
    val username: String,
    val profileImage: String?,
    val projectImage: String?
)

data class User(
    val first_name: String,
    val last_name: String,
    val profile: Profile?
)

data class Profile(
    val picture: String?
)