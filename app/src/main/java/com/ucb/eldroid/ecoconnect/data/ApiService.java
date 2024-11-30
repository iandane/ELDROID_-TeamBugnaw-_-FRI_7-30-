package com.ucb.eldroid.ecoconnect.data;

import com.ucb.eldroid.ecoconnect.data.models.LoginRequest;
import com.ucb.eldroid.ecoconnect.data.models.User;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
<<<<<<< HEAD
    @POST("register")
    Call<Void> registerUser(@Body Map<String, String> body);

    @POST("login")
    Call<Map<String, String>> loginUser(@Body Map<String, String> body);
=======

    @POST("/api/register")
    Call<ResponseBody> postRegister(
            @Body User user);

    @POST("/api/login")
    Call<ResponseBody> postLogin(
            @Body LoginRequest loginRequest);
    @POST("/api/list")
    Call<ResponseBody> postList(
            @Header("Authorization") String authToken,
            @Body LoginRequest loginRequest);
>>>>>>> wagas
}
