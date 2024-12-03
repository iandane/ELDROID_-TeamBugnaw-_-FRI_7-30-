package com.ucb.eldroid.ecoconnect.data

import com.google.gson.annotations.SerializedName
import com.ucb.eldroid.ecoconnect.data.models.LoginRequest
import com.ucb.eldroid.ecoconnect.data.models.Project
import com.ucb.eldroid.ecoconnect.data.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

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

    @Multipart
    @POST("/api/projects")
    fun createProject(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("money_goal") moneyGoal: RequestBody,
        @Part("deadline") deadline: RequestBody,
        @Part image: MultipartBody.Part? // Image file as a part
    ): Call<Project>

    @PUT("/api/user/{token}")
    fun updateUserProfile(
        @Header("Authorization") token: String,
        @Path("token") tokenParam: String,
        @Body userProfile: Map<String, String>
    ): Call<ResponseBody>

    @DELETE("/api/user/{id}")
    fun deleteUserAccount(
        @Header("Authorization") token: String,
        @Path("id") userId: String
    ): Call<ResponseBody>

    data class ProjectResponse(
        @field:SerializedName("projects") val projects: List<Project>
    )

    @GET("/api/projects/titles-images")
    fun getProjectsTitleAndImage(
        @Header("Authorization") authToken: String
    ): Call<ProjectResponse>

    @GET("api/projects/{id}")
    fun getProjectID(
        @Path("id") projectId: Int,
        @Header("Authorization") token: String // Add this header to the API request
    ): Call<Project>

    @GET("api/projects/{id}")
    fun getProjectDetails(
        @Path("id") projectId: Int,
        @Header("Authorization") token: String,
    ): Call<ProjectResponse>



}
