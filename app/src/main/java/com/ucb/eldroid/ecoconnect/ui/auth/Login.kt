package com.ucb.eldroid.ecoconnect.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.ui.bottomnav.BottomNavigationBar

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val createAcc = findViewById<TextView>(R.id.createAcc)
        val forgotPass = findViewById<TextView>(R.id.forgotPass)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        forgotPass.setOnClickListener { v: View? -> showForgotPasswordDialog() }

        createAcc.setOnClickListener { v: View? ->
            val intent = Intent(this@Login, Registration::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener { v: View? ->
            openBottomNav()
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

        if (dialogBuilder.window != null) {
            dialogBuilder.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        }

        dialogBuilder.show()

        val backButton = dialogView.findViewById<TextView>(R.id.bckToLogin)

        backButton.setOnClickListener { v: View? -> dialogBuilder.dismiss() }
    }
}