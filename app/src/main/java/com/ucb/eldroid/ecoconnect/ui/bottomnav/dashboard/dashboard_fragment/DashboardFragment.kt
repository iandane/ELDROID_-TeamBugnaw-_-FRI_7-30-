package com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.dashboard_fragment

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.ui.adapters.RecentActivitiesAdapter
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding.CreateProject
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding.CrowdFundingFragment
import com.ucb.eldroid.ecoconnect.viewmodel.auth.DashboardViewModel
import com.ucb.eldroid.ecoconnect.viewmodel.auth.ProjectViewModel

class DashboardFragment : Fragment() {
    private lateinit var viewModel: DashboardViewModel
    private lateinit var adapter: RecentActivitiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recentActRecyclerView)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Initialize the adapter with an empty list
        adapter = RecentActivitiesAdapter(emptyList(), requireContext())
        recyclerView.adapter = adapter

        // Get authentication token
        val sharedPreferences = requireActivity().application.getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("AUTH_TOKEN", null)
        if (token != null) {
            viewModel.fetchProjectsTitleAndImage(token)
        } else {
            Toast.makeText(requireContext(), "Authentication token is missing", Toast.LENGTH_SHORT).show()
        }

        // Observe the projects LiveData
        viewModel.projects.observe(viewLifecycleOwner) { projects ->
            Log.d("DashboardFragment", "Projects received: ${projects.size}")
            if (projects.isNotEmpty()) {
                // Update the adapter with the new list of projects
                adapter.projects = projects
                adapter.notifyDataSetChanged() // Notify the adapter that the data has changed
            } else {
                Log.d("DashboardFragment", "No projects available")
            }
        }

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
