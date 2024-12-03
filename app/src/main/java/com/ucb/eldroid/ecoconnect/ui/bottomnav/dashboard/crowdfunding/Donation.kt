package com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.Contribution
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Donation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation)

        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val seekBarValue = findViewById<EditText>(R.id.seekBarValue)
        val contributeBtn = findViewById<Button>(R.id.contributeBtn)

        seekBarValue.setText("$0")

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekBarValue.setText("$" + progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do nothing
            }
        })

        contributeBtn.setOnClickListener {
            val token = getAuthToken() // Retrieve the auth token
            val amount = seekBar.progress.toDouble()

            Log.d("Donation", "Contribute Button Clicked")
            Log.d("Donation", "Token: $token")
            Log.d("Donation", "Amount: $amount")

            if (token != null) {
                val contribution = Contribution(amount)
                sendContribution(token, contribution)
            } else {
                Toast.makeText(this, "Authentication token not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendContribution(token: String, contribution: Contribution) {
        val retrofit = RetrofitClient.instance
        val apiService = retrofit?.create(ApiService::class.java)

        Log.d("Donation", "Sending Contribution: $contribution")

        apiService?.contribute("Bearer $token", contribution)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("Donation", "Response: $response")
                if (response.isSuccessful) {
                    showPopupMessage()
                } else {
                    Toast.makeText(this@Donation, "Failed to contribute", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Donation", "Error: ${t.message}")
                Toast.makeText(this@Donation, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showPopupMessage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
        builder.setMessage("Contribution successful!")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        builder.show()
    }

    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("AUTH_TOKEN", null)
    }
}