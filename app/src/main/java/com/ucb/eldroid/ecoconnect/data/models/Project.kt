package com.ucb.eldroid.ecoconnect.data.models

import com.google.gson.annotations.SerializedName

class Project(
    @field:SerializedName("title") var title: String,
    @field:SerializedName("description") var description: String,
    @field:SerializedName("money_goal") var moneyGoal: Double,
    @field:SerializedName("deadline") var deadline: String,
    @field:SerializedName("image") var image: String? = null,
    var token: String? = null
)
