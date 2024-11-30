package com.ucb.eldroid.ecoconnect.data

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun registerUser(@Body body: Map<String, String>): Response<Void>

    @POST("login")
    suspend fun loginUser(@Body body: Map<String, String>): Response<Map<String, String>>
}
