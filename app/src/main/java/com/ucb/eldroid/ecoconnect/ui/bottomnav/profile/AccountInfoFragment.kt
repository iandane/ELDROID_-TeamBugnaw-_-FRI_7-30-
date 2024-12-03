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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.ui.auth.Login
import com.ucb.eldroid.ecoconnect.viewmodel.auth.AccountInfoViewModel

class AccountInfoFragment : Fragment() {

    private lateinit var fullNameTextView: TextView
    private lateinit var emailTextView: TextView
    private val accountInfoViewModel: AccountInfoViewModel by viewModels()

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
        val sharedPreferences = requireContext().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("AUTH_TOKEN", null)

        if (!token.isNullOrEmpty()) {
            accountInfoViewModel.fetchUserData(token)
        } else {
            Toast.makeText(requireContext(), "Authentication token is missing", Toast.LENGTH_SHORT).show()
        }

        // Observe ViewModel LiveData
        accountInfoViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            fullNameTextView.text = "${user.firstName} ${user.lastName}"
            emailTextView.text = user.email

            // Store the user ID in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("USER_ID", user.id)
            editor.apply()
        })

        accountInfoViewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        accountInfoViewModel.deleteSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show()
                logout()
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

        accountInfoViewModel.deleteAccount(token!!, userId!!)
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

