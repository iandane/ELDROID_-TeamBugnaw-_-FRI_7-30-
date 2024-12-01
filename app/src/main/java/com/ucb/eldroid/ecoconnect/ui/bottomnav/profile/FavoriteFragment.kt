package com.ucb.eldroid.ecoconnect.ui.bottomnav.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.ucb.eldroid.ecoconnect.R

class FavoriteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button functionality
        val backButton = view.findViewById<ImageView>(R.id.backBtn)
        backButton.setOnClickListener { v: View? ->
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}