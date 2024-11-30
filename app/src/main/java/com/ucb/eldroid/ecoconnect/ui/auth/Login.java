package com.ucb.eldroid.ecoconnect.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.ucb.eldroid.ecoconnect.R;
import com.ucb.eldroid.ecoconnect.data.ApiService;
import com.ucb.eldroid.ecoconnect.data.models.LoginRequest;
import com.ucb.eldroid.ecoconnect.ui.bottomnav.BottomNavigationBar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView createAcc = findViewById(R.id.createAcc);
        TextView forgotPass = findViewById(R.id.forgotPass);
        Button loginBtn = findViewById(R.id.loginBtn);
        editTextEmail = findViewById(R.id.username); // Initialize email field
        editTextPassword = findViewById(R.id.password);  // Initialize password field

        forgotPass.setOnClickListener(v -> showForgotPasswordDialog());

        createAcc.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Registration.class);
            startActivity(intent);
        });

        loginBtn.setOnClickListener(v -> loginUser());
    }

    private void openBottomNav() {
        Intent intent = new Intent(Login.this, BottomNavigationBar.class);
        startActivity(intent);
        finish();
    }

    private void showForgotPasswordDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_forgot_password, null);

        AlertDialog dialogBuilder = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        if (dialogBuilder.getWindow() != null) {
            dialogBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialogBuilder.show();

        TextView backButton = dialogView.findViewById(R.id.bckToLogin);

        backButton.setOnClickListener(v -> dialogBuilder.dismiss());
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.0.33:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<ResponseBody> call = apiService.postLogin(loginRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    openBottomNav();
                } else {
                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
