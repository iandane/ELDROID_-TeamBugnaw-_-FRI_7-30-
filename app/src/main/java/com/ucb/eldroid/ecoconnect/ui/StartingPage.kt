package com.ucb.eldroid.ecoconnect.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.ui.auth.Login

class StartingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting_page)

        val joinButton = findViewById<Button>(R.id.joinButton)

        joinButton.setOnClickListener { v: View? ->
            val intent = Intent(this@StartingPage, Login::class.java)
            startActivity(intent)
        }
    }
}