package com.ucb.eldroid.ecoconnect.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class Registration : AppCompatActivity() {
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
    }

    fun regUser() {
        val firstName = editTextFirstName!!.text.toString()
        val lastName = editTextLastName!!.text.toString()
        val email = editTextEmail!!.text.toString()
        val password = editTextPassword!!.text.toString()
        val passwordConfirmation = editTextRepassword!!.text.toString()

        if (password != passwordConfirmation) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.228:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val user = User(firstName, lastName, email, password, passwordConfirmation)

        val call = apiService.postRegister(user)
        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Registration, "Registration Successful", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    try {
                        val errorBody = response.errorBody()!!.string()
                        Log.e("RegistrationError", errorBody)
                        Toast.makeText(
                            this@Registration,
                            "Registration Failed: $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@Registration,
                            "Registration Failed: Unable to parse error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Log.e("RegistrationFailure", t.message!!)
                Toast.makeText(
                    this@Registration,
                    "Registration Failed: " + t.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
