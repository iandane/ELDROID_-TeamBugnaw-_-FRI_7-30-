package com.ucb.eldroid.ecoconnect.data.models

import com.google.gson.annotations.SerializedName

data class SimplifiedProject(
    @field:SerializedName("title") val title: String,
    @field:SerializedName("image") val image: String? = null
)
