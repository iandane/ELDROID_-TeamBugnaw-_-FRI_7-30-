package com.ucb.eldroid.ecoconnect.data.models

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("id") var id: String,
    @SerializedName("first_name") var firstName: String,
    @SerializedName("last_name") var lastName: String,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String? = null,
    @SerializedName("password_confirmation") var passwordConfirmation: String? = null,
    @SerializedName("token") var token: String? = null
)
