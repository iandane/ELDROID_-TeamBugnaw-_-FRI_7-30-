package com.ucb.eldroid.ecoconnect.data.models

import com.google.gson.annotations.SerializedName

class User(
    @field:SerializedName("first_name") var firstName: String,
    @field:SerializedName("last_name") var lastName: String,
    @field:SerializedName("email") var email: String,
    @field:SerializedName("password") var password: String,
    @field:SerializedName("password_confirmation") var passwordConfirmation: String,
    var token: String? = null
)
