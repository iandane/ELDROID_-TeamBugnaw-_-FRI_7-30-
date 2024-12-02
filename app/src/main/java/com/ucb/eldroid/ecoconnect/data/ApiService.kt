package com.ucb.eldroid.ecoconnect.data

import com.ucb.eldroid.ecoconnect.data.models.LoginRequest
import com.ucb.eldroid.ecoconnect.data.models.Project
import com.ucb.eldroid.ecoconnect.data.models.User
import com.ucb.eldroid.ecoconnect.ui.adapters.Post
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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

    @POST("/api/projects")
    fun createProject(
        @Body project: Project,
        @Header("Authorization") authHeader: String
    ): Call<ResponseBody>

    @GET("/api/projects")
    fun getProjects(
        @Header("Authorization") authToken: String
    ): Call<List<Post>>
}
