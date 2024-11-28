package com.ucb.eldroid.ecoconnect.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ucb.eldroid.ecoconnect.R;
import com.ucb.eldroid.ecoconnect.ui.StartingPage;
import com.ucb.eldroid.ecoconnect.ui.bottomnav.BottomNavigationBar;
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.dashboard_fragment.DashboardFragment;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView createAcc = findViewById(R.id.createAcc);
        TextView forgotPass = findViewById(R.id.forgotPass);
        Button loginBtn = findViewById(R.id.loginBtn);
        forgotPass.setOnClickListener(v -> showForgotPasswordDialog());

        createAcc.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Registration.class);
            startActivity(intent);
        });

        loginBtn.setOnClickListener(v -> {

            openBottomNav();
        });
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
}