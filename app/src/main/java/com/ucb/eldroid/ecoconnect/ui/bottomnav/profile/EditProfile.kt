package com.ucb.eldroid.ecoconnect.ui.bottomnav.profile

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ucb.eldroid.ecoconnect.R

class EditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        // Set up ActionBar back button
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        // Handle custom back button
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener { v: View? -> finish() } // Closes this activity

        // Handle window insets for Edge-to-Edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up the Save button with Toast functionality
        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener { v: View? ->
            // Show a Toast when the Save button is clicked
            Toast.makeText(this@EditProfile, "Profile saved successfully!", Toast.LENGTH_SHORT)
                .show()

            // Navigate back to the previous fragment
            onBackPressed() // This will trigger the fragment back navigation
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Navigate back to the previous activity
        return true
    }
}
