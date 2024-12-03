package com.ucb.eldroid.ecoconnect.ui.bottomnav.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.User
import com.ucb.eldroid.ecoconnect.ui.auth.Login
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountInfoFragment : Fragment() {

    private lateinit var fullNameTextView: TextView
    private lateinit var emailTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        val backButton = view.findViewById<ImageView>(R.id.backBtn)
        fullNameTextView = view.findViewById(R.id.name_value)
        emailTextView = view.findViewById(R.id.email_value)
        val deleteAccountButton = view.findViewById<Button>(R.id.deleteAccount)

        // Back button functionality
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Handle Delete Account Button click
        deleteAccountButton.setOnClickListener {
            deleteAccount()
        }

        // Fetch and display user data
        fetchUserData()
    }

    private fun fetchUserData() {
        val sharedPreferences = requireContext().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("AUTH_TOKEN", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Authentication token is missing", Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = RetrofitClient.instance
        val apiService = retrofit?.create(ApiService::class.java)

        apiService?.getUser("Bearer $token")?.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body() // Use Retrofit's GSON deserialization
                    if (user != null) {
                        Log.d("AccountInfoFragment", "Fetched User: ${Gson().toJson(user)}")
                        fullNameTextView.text = "${user.firstName} ${user.lastName}"
                        emailTextView.text = user.email

                        if (!user.id.isNullOrEmpty()) {
                            val editor = sharedPreferences.edit()
                            editor.putString("USER_ID", user.id)
                            editor.apply()
                        } else {
                            Log.e("AccountInfoFragment", "User ID is missing in the response")
                        }
                    } else {
                        Log.e("AccountInfoFragment", "Response body is null")
                    }
                } else {
                    Log.e("AccountInfoFragment", "Failed to fetch user data: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun deleteAccount() {
        val sharedPreferences = requireContext().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("AUTH_TOKEN", null)
        val userId = sharedPreferences.getString("USER_ID", null)

        // Log the token and user ID for debugging purposes
        Log.d("AccountInfoFragment", "AUTH_TOKEN: $token")
        Log.d("AccountInfoFragment", "USER_ID: $userId")

        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "User ID is missing", Toast.LENGTH_SHORT).show()
            return
        } else if (token.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Authentication token is missing", Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = RetrofitClient.instance
        val apiService = retrofit?.create(ApiService::class.java)

        apiService?.deleteUserAccount("Bearer $token", userId)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show()
                    logout()
                } else {
                    Toast.makeText(requireContext(), "Failed to delete account", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun logout() {
        val sharedPreferences = requireContext().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Clear all user-related data
        editor.putBoolean("isLoggedIn", false)
        editor.remove("USER_FIRST_NAME")
        editor.remove("USER_LAST_NAME")
        editor.remove("USER_EMAIL")
        editor.remove("AUTH_TOKEN")
        editor.remove("USER_ID") // Ensure USER_ID is removed
        editor.apply()

        // Navigate to Login screen
        val intent = Intent(requireContext(), Login::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
