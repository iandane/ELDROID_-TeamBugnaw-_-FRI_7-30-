package com.ucb.eldroid.ecoconnect.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ucb.eldroid.ecoconnect.R

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val loginHere = findViewById<TextView>(R.id.loginTxt)
        loginHere.setOnClickListener { v: View? ->
            val intent = Intent(this@Registration, Login::class.java)
            startActivity(intent)
        }
    }
}