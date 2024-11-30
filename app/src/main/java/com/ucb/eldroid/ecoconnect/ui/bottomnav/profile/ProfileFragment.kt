package com.ucb.eldroid.ecoconnect.ui.bottomnav.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.ucb.eldroid.ecoconnect.R

class ProfileFragment : Fragment() {
    private var editProfileButton: Button? = null
    private var accountInfoButton: View? = null
    private var favoriteButton: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize UI components
        editProfileButton = view.findViewById(R.id.editProfileBtn)
        accountInfoButton = view.findViewById(R.id.accountBtn)
        favoriteButton = view.findViewById(R.id.favbtn)

        // Set up button listeners
        setupButtonListeners()

        return view
    }

    private fun setupButtonListeners() {
        // Account Info button
        accountInfoButton!!.setOnClickListener { v: View? ->
            // Navigate to AccountInfoFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.navHost, AccountInfoFragment())
                .addToBackStack(null)
                .commit()
        }

        // Edit Profile button
        editProfileButton!!.setOnClickListener { v: View? ->
            // Navigate to EditProfile activity
            val intent = Intent(requireContext(), EditProfile::class.java)
            startActivity(intent)
        }

        // Favorites button
        favoriteButton!!.setOnClickListener { v: View? ->
            // Navigate to FavoriteFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.navHost, FavoriteFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
