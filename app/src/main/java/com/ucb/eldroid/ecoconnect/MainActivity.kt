package com.ucb.eldroid.ecoconnect

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        lifecycleScope.launch {
            // Example: Register User
            val registerData = mapOf(
                "name" to "John Doe",
                "email" to "johndoe@example.com",
                "password" to "password123",
                "password_confirmation" to "password123"
            )

            try {
                val registerResponse = apiService.registerUser(registerData)
                if (registerResponse.isSuccessful) {
                    Toast.makeText(this@MainActivity, "User registered successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Registration failed.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            // Example: Login User
            val loginData = mapOf(
                "email" to "johndoe@example.com",
                "password" to "password123"
            )

            try {
                val loginResponse = apiService.loginUser(loginData)
                if (loginResponse.isSuccessful) {
                    val token = loginResponse.body()?.get("token")
                    Toast.makeText(this@MainActivity, "Login successful! Token: $token", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Login failed.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}