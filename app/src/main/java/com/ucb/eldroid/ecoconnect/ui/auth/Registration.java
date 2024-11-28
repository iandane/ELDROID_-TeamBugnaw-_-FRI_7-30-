package com.ucb.eldroid.ecoconnect.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ucb.eldroid.ecoconnect.R;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView loginHere = findViewById(R.id.loginTxt);
        loginHere.setOnClickListener(v -> {
            Intent intent = new Intent(Registration.this, Login.class);
            startActivity(intent);
        });
    }
}