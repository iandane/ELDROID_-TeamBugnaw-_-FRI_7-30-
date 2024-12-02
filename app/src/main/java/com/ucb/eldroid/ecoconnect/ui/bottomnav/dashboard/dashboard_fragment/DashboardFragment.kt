package com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.dashboard_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding.CreateProject
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding.CrowdFundingFragment

class DashboardFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val createProjCardView = view.findViewById<View>(R.id.CreateProjCardview)
        val crowdFundCardView = view.findViewById<View>(R.id.crowdFundCardView)


        createProjCardView.setOnClickListener {
            val intent = Intent(activity, CreateProject::class.java)
            startActivity(intent)
        }

        crowdFundCardView.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.navHost, CrowdFundingFragment())
                .addToBackStack(null)
                .commit()
        }
        return view
    }
}