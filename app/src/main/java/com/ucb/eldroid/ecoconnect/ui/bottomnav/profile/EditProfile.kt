package com.ucb.eldroid.ecoconnect.ui.bottomnav.profile

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.User
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        // Set up ActionBar back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle custom back button
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener { finish() } // Closes this activity

        // Handle window insets for Edge-to-Edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up EditTexts
        val nameEditText = findViewById<EditText>(R.id.edit_name)
        val emailEditText = findViewById<EditText>(R.id.edit_email)
        val passwordEditText = findViewById<EditText>(R.id.edit_password)

        // Load existing profile data
        loadProfileData(nameEditText, emailEditText)

        // Set up the Save button
        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            // Get input values
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validate and update profile
            if (name.isNotEmpty() && email.isNotEmpty()) {
                val nameParts = name.split(" ")
                val firstName = nameParts.getOrNull(0) ?: ""
                val lastName = nameParts.getOrNull(1) ?: ""
                updateProfile(firstName, lastName, email, password)
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadProfileData(nameEditText: EditText, emailEditText: EditText) {
        val sharedPreferences = getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val firstName = sharedPreferences.getString("FIRST_NAME", "")
        val lastName = sharedPreferences.getString("LAST_NAME", "")
        val email = sharedPreferences.getString("EMAIL", "")

        nameEditText.setText("$firstName $lastName")
        emailEditText.setText(email)

        fetchProfileData(nameEditText, emailEditText)
    }

    private fun fetchProfileData(nameEditText: EditText, emailEditText: EditText) {
        val token = getAuthToken()
        if (token != null) {
            val retrofit = RetrofitClient.instance
            val apiService = retrofit?.create(ApiService::class.java)

            apiService?.getUser("Bearer $token")?.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val userProfile = response.body()
                        if (userProfile != null) {
                            nameEditText.setText("${userProfile.firstName} ${userProfile.lastName}")
                            emailEditText.setText(userProfile.email)
                            saveProfileData(userProfile.firstName, userProfile.lastName, userProfile.email)
                        }
                    } else {
                        Toast.makeText(this@EditProfile, "Failed to fetch profile data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@EditProfile, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun updateProfile(firstName: String, lastName: String, email: String, password: String) {
        val token = getAuthToken()
        if (token != null) {
            val retrofit = RetrofitClient.instance
            val apiService = retrofit?.create(ApiService::class.java)

            val userProfile = mutableMapOf<String, String>()
            userProfile["first_name"] = firstName
            userProfile["last_name"] = lastName
            userProfile["email"] = email
            if (password.isNotEmpty()) {
                userProfile["password"] = password
                userProfile["password_confirmation"] = password
            }

            apiService?.updateUserProfile("Bearer $token", token, userProfile)?.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditProfile, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        saveProfileData(firstName, lastName, email)

                        // Navigate back to ProfileFragment and refresh data
                        finish()  // Closes EditProfile activity
                    } else {
                        Toast.makeText(this@EditProfile, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@EditProfile, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Authentication token not found", Toast.LENGTH_SHORT).show()
        }
    }



    private fun saveProfileData(firstName: String, lastName: String, email: String) {
        val sharedPreferences = getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("FIRST_NAME", firstName)
            putString("LAST_NAME", lastName)
            putString("EMAIL", email)
            apply()
        }
    }

    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("AUTH_TOKEN", null)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Navigate back to the previous activity
        return true
    }
}
