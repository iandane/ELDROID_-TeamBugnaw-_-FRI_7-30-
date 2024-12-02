package com.ucb.eldroid.ecoconnect.data.models

class Project(
    val projectId: String? = null,   // Nullable projectId, will be assigned later
    val title: String,
    val description: String,
    val goalAmount: Double,
    val deadline: String,
    val imagePath: String?
)
