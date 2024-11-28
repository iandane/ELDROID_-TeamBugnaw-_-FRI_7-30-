package com.ucb.eldroid.ecoconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ucb.eldroid.ecoconnect.R;
import com.ucb.eldroid.ecoconnect.ui.auth.Login;

public class StartingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);

        Button joinButton = findViewById(R.id.joinButton);

        joinButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartingPage.this, Login.class);
            startActivity(intent);
        });
    }
}