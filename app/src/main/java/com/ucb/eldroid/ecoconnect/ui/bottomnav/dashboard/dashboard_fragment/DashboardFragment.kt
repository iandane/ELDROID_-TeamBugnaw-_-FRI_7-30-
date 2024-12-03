package com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.dashboard_fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

        val sharedPreferences = requireActivity().application.getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("AUTH_TOKEN", null)
        if (token != null) {
            viewModel.fetchProjectsTitleAndImage(token)
        } else {
            Toast.makeText(requireContext(), "Authentication token is missing", Toast.LENGTH_SHORT).show()
        }

        // DashboardFragment.kt
        viewModel.projects.observe(viewLifecycleOwner) { projects ->
            adapter = RecentActivitiesAdapter(projects, requireContext()) { projectId ->
                if (token != null) {
                    viewModel.deleteProject(projectId, token)
                    viewModel.deleteResponse.observe(viewLifecycleOwner) { response ->
                        Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show()
                        if (response == "Project deleted successfully") {
                            viewModel.fetchProjectsTitleAndImage(token)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Authentication token is missing", Toast.LENGTH_SHORT).show()
                }
            }
            recyclerView.adapter = adapter
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
