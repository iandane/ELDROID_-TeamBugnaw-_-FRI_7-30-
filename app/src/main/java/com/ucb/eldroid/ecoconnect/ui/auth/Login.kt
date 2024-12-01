package com.ucb.eldroid.ecoconnect.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.LoginRequest
import com.ucb.eldroid.ecoconnect.ui.bottomnav.BottomNavigationBar
import com.ucb.eldroid.ecoconnect.viewmodel.auth.LoginViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val createAcc = findViewById<TextView>(R.id.createAcc)
        val forgotPass = findViewById<TextView>(R.id.forgotPass)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val emailField = findViewById<EditText>(R.id.username)
        val passwordField = findViewById<EditText>(R.id.password)

        // Observing errors for validation
        loginViewModel.emailError.observe(this) { errorMessage ->
            if (errorMessage != null) {
                emailField.error = errorMessage // Show error on email field
            }
        }

        loginViewModel.passwordError.observe(this) { errorMessage ->
            if (errorMessage != null) {
                passwordField.error = errorMessage // Show error on password field
            }
        }

        // Observing login result
        loginViewModel.loginResult.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                openBottomNav()
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }

        // Observing error messages from API response
        loginViewModel.errorMessage.observe(this) { errorMsg ->
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
        }

        // Forgot Password Action
        forgotPass.setOnClickListener {
            showForgotPasswordDialog()
        }

        // Create Account Action
        createAcc.setOnClickListener {
            val intent = Intent(this@Login, Registration::class.java)
            startActivity(intent)
        }

        // Login Button Action
        loginBtn.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            // Call validateCredentials from the ViewModel
            if (loginViewModel.validateCredentials(email, password)) {
                // If credentials are valid, call the login method
                loginViewModel.login(email, password)
            } else {
                // Display a general error message if validation fails
                Toast.makeText(this, "Please correct the errors", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openBottomNav() {
        val intent = Intent(this@Login, BottomNavigationBar::class.java)
        startActivity(intent)
        finish()
    }

    private fun showForgotPasswordDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_forgot_password, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogBuilder.show()

        val backButton = dialogView.findViewById<TextView>(R.id.bckToLogin)
        backButton.setOnClickListener {
            dialogBuilder.dismiss()
        }
    }
}

