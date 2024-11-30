package com.ucb.eldroid.ecoconnect.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.LoginRequest
import com.ucb.eldroid.ecoconnect.ui.bottomnav.BottomNavigationBar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login : AppCompatActivity() {
    var editTextEmail: EditText? = null
    var editTextPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val createAcc = findViewById<TextView>(R.id.createAcc)
        val forgotPass = findViewById<TextView>(R.id.forgotPass)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        editTextEmail = findViewById(R.id.username) // Initialize email field
        editTextPassword = findViewById(R.id.password) // Initialize password field

        forgotPass.setOnClickListener { v: View? -> showForgotPasswordDialog() }

        createAcc.setOnClickListener { v: View? ->
            val intent = Intent(this@Login, Registration::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener { v: View? -> loginUser() }
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

        if (dialogBuilder.window != null) {
            dialogBuilder.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        }

        dialogBuilder.show()

        val backButton = dialogView.findViewById<TextView>(R.id.bckToLogin)

        backButton.setOnClickListener { v: View? -> dialogBuilder.dismiss() }
    }

    private fun loginUser() {
        val email = editTextEmail!!.text.toString()
        val password = editTextPassword!!.text.toString()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.15:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val loginRequest = LoginRequest(email, password)

        val call = apiService.postLogin(loginRequest)
        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Login, "Login Successful", Toast.LENGTH_SHORT).show()
                    openBottomNav()
                } else {
                    Toast.makeText(this@Login, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@Login, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
