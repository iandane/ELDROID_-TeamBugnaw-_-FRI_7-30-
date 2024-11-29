package com.ucb.eldroid.ecoconnect.ui.bottomnav.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ucb.eldroid.ecoconnect.R;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        // Set up ActionBar back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle custom back button
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish()); // Closes this activity

        // Handle window insets for Edge-to-Edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the Save button with Toast functionality
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            // Show a Toast when the Save button is clicked
            Toast.makeText(EditProfile.this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();

            // Navigate back to the previous fragment
            onBackPressed(); // This will trigger the fragment back navigation
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Navigate back to the previous activity
        return true;
    }
}
