package com.ucb.eldroid.ecoconnect.data

import com.ucb.eldroid.ecoconnect.data.models.LoginRequest
import com.ucb.eldroid.ecoconnect.data.models.Project
import com.ucb.eldroid.ecoconnect.data.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("/api/register")
    fun postRegister(
        @Body user: User
    ): Call<ResponseBody>

    @POST("/api/login")
    fun postLogin(
        @Body loginRequest: LoginRequest
    ): Call<ResponseBody>

    @POST("/api/list")
    fun postList(
        @Header("Authorization") authToken: String,
        @Body loginRequest: LoginRequest
    ): Call<ResponseBody>

    @GET("/api/user")
    fun getUser(
        @Header("Authorization") authToken: String
    ): Call<User>

    // Send the project to the server with the token as a header

    @Multipart
    @POST("/api/projects")
    fun createProject(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("money_goal") moneyGoal: RequestBody,
        @Part("deadline") deadline: RequestBody,
    ): Call<ResponseBody>
}
