package com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ucb.eldroid.ecoconnect.R

class CrowdFundingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crowd_funding, container, false)
    }
}