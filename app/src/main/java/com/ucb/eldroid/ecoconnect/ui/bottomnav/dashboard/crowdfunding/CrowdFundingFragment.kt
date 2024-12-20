package com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.ui.adapters.CrowdfundingAdapter
import com.ucb.eldroid.ecoconnect.ui.adapters.CrowdfundingProject

class CrowdFundingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_crowd_funding, container, false)

        val listView = rootView.findViewById<ListView>(R.id.listview)

        val projects = listOf(
            CrowdfundingProject("Save the Rainforest", "Eco Warriors", "₱1M"),
            CrowdfundingProject("Clean Ocean Initiative", "Blue Planet", "₱500K"),
            CrowdfundingProject("Solar Energy for Schools", "Bright Future", "₱750K")
        )

        val adapter = CrowdfundingAdapter(requireContext(), projects)
        listView.adapter = adapter

        return rootView
    }
}