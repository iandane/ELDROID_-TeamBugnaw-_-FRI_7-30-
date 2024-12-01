package com.ucb.eldroid.ecoconnect.data

import com.ucb.eldroid.ecoconnect.data.models.LoginRequest
import com.ucb.eldroid.ecoconnect.data.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("/api/register")
    fun postRegister(
        @Body user: User?
    ): Call<ResponseBody?>

    @POST("/api/login")
    fun postLogin(
        @Body loginRequest: LoginRequest?
    ): Call<ResponseBody?>

    @POST("/api/list")
    fun postList(
        @Header("Authorization") authToken: String?,
        @Body loginRequest: LoginRequest?
    ): Call<ResponseBody?>?
}
