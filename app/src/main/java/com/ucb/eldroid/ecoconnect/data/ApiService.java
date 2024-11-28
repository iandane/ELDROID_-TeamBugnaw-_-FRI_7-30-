package com.ucb.eldroid.ecoconnect.data;

import com.ucb.eldroid.ecoconnect.data.models.LoginRequest;
import com.ucb.eldroid.ecoconnect.data.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("register")
    Call<ResponseBody> registerUser(@Body User user);

    @POST("login")
    Call<ResponseBody> loginUser(@Body LoginRequest loginRequest);
}
