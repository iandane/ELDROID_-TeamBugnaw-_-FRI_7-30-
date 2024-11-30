package com.ucb.eldroid.ecoconnect.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ucb.eldroid.ecoconnect.R;
import com.ucb.eldroid.ecoconnect.data.ApiService;
import com.ucb.eldroid.ecoconnect.data.models.User;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registration extends AppCompatActivity {

    EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword, editTextRepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextFirstName = findViewById(R.id.firstName);
        editTextLastName = findViewById(R.id.lastName);
        editTextEmail = findViewById(R.id.emailAddress);
        editTextPassword = findViewById(R.id.password);
        editTextRepassword = findViewById(R.id.reEnterPass);

        Button button = findViewById(R.id.registerBtn);
        TextView loginHere = findViewById(R.id.loginTxt);

        button.setOnClickListener(v -> regUser());

        loginHere.setOnClickListener(v -> {
            Intent intent = new Intent(Registration.this, Login.class);
            startActivity(intent);
        });
    }

    void regUser() {
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordConfirmation = editTextRepassword.getText().toString();

        if (!password.equals(passwordConfirmation)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.0.33:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        User user = new User(firstName, lastName, email, password, passwordConfirmation);

        Call<ResponseBody> call = apiService.postRegister(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("RegistrationError", errorBody);
                        Toast.makeText(Registration.this, "Registration Failed: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(Registration.this, "Registration Failed: Unable to parse error", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("RegistrationFailure", t.getMessage());
                Toast.makeText(Registration.this, "Registration Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
