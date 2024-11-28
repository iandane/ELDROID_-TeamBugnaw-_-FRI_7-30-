package com.ucb.eldroid.ecoconnect;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ucb.eldroid.ecoconnect.data.ApiService;
import com.ucb.eldroid.ecoconnect.data.models.LoginRequest;
import com.ucb.eldroid.ecoconnect.data.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://10.0.2.2:8000/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Example: Register a user
        User user = new User("John Doe", "john@example.com", "password123");
        Call<ResponseBody> registerCall = apiService.registerUser(user);

        registerCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("Register", "Success!");
                } else {
                    Log.d("Register", "Failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Register", "Error: " + t.getMessage());
            }
        });

        // Example: Login a user
        LoginRequest loginRequest = new LoginRequest("john@example.com", "password123");
        Call<ResponseBody> loginCall = apiService.loginUser(loginRequest);

        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("Login", "Success!");
                } else {
                    Log.d("Login", "Failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Login", "Error: " + t.getMessage());
            }
        });
    }
}