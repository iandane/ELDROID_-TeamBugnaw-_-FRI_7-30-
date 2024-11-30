package com.ucb.eldroid.ecoconnect.ui.bottomnav.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ucb.eldroid.ecoconnect.R

class AccountInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button functionality
        val backButton = view.findViewById<ImageView>(R.id.backBtn)
        backButton.setOnClickListener { v: View? ->
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Handle Delete Account Button click
        val deleteAccountButton = view.findViewById<Button>(R.id.deleteAccount)
        deleteAccountButton.setOnClickListener { v: View? ->
            // Show a Toast when the delete button is clicked
            Toast.makeText(activity, "Account deleted", Toast.LENGTH_SHORT).show()
        }
    }
}
