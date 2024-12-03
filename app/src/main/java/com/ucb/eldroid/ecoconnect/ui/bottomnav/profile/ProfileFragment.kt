package com.ucb.eldroid.ecoconnect.ui.bottomnav.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.ui.auth.Login
import com.ucb.eldroid.ecoconnect.viewmodel.eco_connect.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var editProfileButton: Button
    private lateinit var accountInfoButton: View
    private lateinit var favoriteButton: View
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private val profileViewModel: ProfileViewModel by viewModels()
    private var logoutButton: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        editProfileButton = view.findViewById(R.id.editProfileBtn)
        accountInfoButton = view.findViewById(R.id.accountBtn)
        favoriteButton = view.findViewById(R.id.favbtn)
        nameTextView = view.findViewById(R.id.fullName)
        emailTextView = view.findViewById(R.id.email)
        logoutButton = view.findViewById(R.id.logoutbutton)

        logoutButton?.setOnClickListener {
            logout()
        }
        setupButtonListeners()
        fetchAndDisplayUserInfo()

        return view
    }

    override fun onResume() {
        super.onResume()
        fetchAndDisplayUserInfo()
    }

    private fun setupButtonListeners() {
        accountInfoButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.navHost, AccountInfoFragment())
                .addToBackStack(null)
                .commit()
        }

        editProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), EditProfile::class.java)
            startActivity(intent)
        }

        favoriteButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.navHost, FavoriteFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun fetchAndDisplayUserInfo() {
        val sharedPreferences = requireContext().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("AUTH_TOKEN", null)
        Log.d("ProfileFragment", "Token: $token")
        if (token.isNullOrEmpty()) {
            Log.e("ProfileFragment", "Token is null or empty")
            return
        }
        profileViewModel.fetchUserData(token)
        profileViewModel.userData.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                Log.e("ProfileFragment", "User data is null")
                return@observe
            }
            val fullName = "${user.firstName} ${user.lastName}"
            nameTextView.text = fullName
            emailTextView.text = user.email
            Log.d("ProfileFragment", "User: $fullName, Email: ${user.email}")
        }
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
        editor.apply()

        val intent = Intent(requireContext(), Login::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
