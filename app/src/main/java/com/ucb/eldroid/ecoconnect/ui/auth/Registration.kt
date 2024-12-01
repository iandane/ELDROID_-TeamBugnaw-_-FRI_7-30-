package com.ucb.eldroid.ecoconnect.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.viewmodel.auth.RegisterViewModel

class Registration : AppCompatActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()

    private lateinit var firstNameField: EditText
    private lateinit var lastNameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var reEnterPasswordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        firstNameField = findViewById(R.id.firstName)
        lastNameField = findViewById(R.id.lastName)
        emailField = findViewById(R.id.emailAddress)
        passwordField = findViewById(R.id.password)
        reEnterPasswordField = findViewById(R.id.reEnterPass)

        val button = findViewById<Button>(R.id.registerBtn)
        val loginHere = findViewById<TextView>(R.id.loginTxt)

        button.setOnClickListener { regUser() }

        loginHere.setOnClickListener {
            val intent = Intent(this@Registration, Login::class.java)
            startActivity(intent)
        }

        // Observe LiveData for validation errors
        registerViewModel.validationError.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Observe LiveData for registration success
        registerViewModel.registrationSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                // Navigate to the Login activity
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Observe LiveData for registration failure
        registerViewModel.registrationError.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun regUser() {
        val firstName = firstNameField.text.toString().trim()
        val lastName = lastNameField.text.toString().trim()
        val email = emailField.text.toString().trim()
        val password = passwordField.text.toString().trim()
        val passwordConfirmation = reEnterPasswordField.text.toString().trim()

        // Call the registerUser method in the ViewModel
        registerViewModel.registerUser(firstName, lastName, email, password, passwordConfirmation)
    }
}
