package com.ucb.eldroid.ecoconnect.data.response

import com.google.gson.annotations.SerializedName
import com.ucb.eldroid.ecoconnect.data.models.Project

data class ProjectResponse(
    @field:SerializedName("projects") val projects: List<Project>
)