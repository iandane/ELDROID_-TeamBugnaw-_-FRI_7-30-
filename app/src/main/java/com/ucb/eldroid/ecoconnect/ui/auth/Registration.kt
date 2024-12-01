package com.ucb.eldroid.ecoconnect.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.User
import com.ucb.eldroid.ecoconnect.viewmodel.auth.RegisterViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class Registration : AppCompatActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()

    var editTextFirstName: EditText? = null
    var editTextLastName: EditText? = null
    var editTextEmail: EditText? = null
    var editTextPassword: EditText? = null
    var editTextRepassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        editTextFirstName = findViewById(R.id.firstName)
        editTextLastName = findViewById(R.id.lastName)
        editTextEmail = findViewById(R.id.emailAddress)
        editTextPassword = findViewById(R.id.password)
        editTextRepassword = findViewById(R.id.reEnterPass)

        val button = findViewById<Button>(R.id.registerBtn)
        val loginHere = findViewById<TextView>(R.id.loginTxt)

        button.setOnClickListener { v: View? -> regUser() }

        loginHere.setOnClickListener { v: View? ->
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
        val firstName = editTextFirstName!!.text.toString().trim()
        val lastName = editTextLastName!!.text.toString().trim()
        val email = editTextEmail!!.text.toString().trim()
        val password = editTextPassword!!.text.toString().trim()
        val passwordConfirmation = editTextRepassword!!.text.toString().trim()

        // Call the registerUser method in the ViewModel
        registerViewModel.registerUser(firstName, lastName, email, password, passwordConfirmation)
    }
}
