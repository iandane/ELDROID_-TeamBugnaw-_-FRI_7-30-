package com.ucb.eldroid.ecoconnect.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.ProjectResponse
import com.ucb.eldroid.ecoconnect.data.models.Project
import com.ucb.eldroid.ecoconnect.ui.adapters.NewsFeedAdapter
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import kotlinx.coroutines.launch

class NewsfeedFragment : Fragment() {

    private lateinit var adapter: NewsFeedAdapter
    private lateinit var recyclerView: RecyclerView
    private val projects = mutableListOf<Project>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news_feed, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = NewsFeedAdapter(requireContext(), projects)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        fetchProjects()

        return view
    }

    private fun fetchProjects() {
        val sharedPreferences = requireContext().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("AUTH_TOKEN", "") ?: ""

        if (authToken.isNotEmpty()) {
            val apiService = RetrofitClient.instance?.create(ApiService::class.java)

            apiService?.let { service ->
                // Launch a coroutine to fetch projects
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val projectResponse: ProjectResponse = service.getProjects("Bearer $authToken")
                        Log.d("API_RESPONSE", "Projects fetched: ${projectResponse.data}")

                        // Update projects and notify adapter
                        val projectsData = projectResponse.data ?: emptyList()  // Handle potential null data
                        projects.clear()
                        projects.addAll(projectsData)
                        adapter.notifyDataSetChanged()  // Refresh the RecyclerView
                    } catch (e: Exception) {
                        Log.e("API_ERROR", "Error fetching projects: ${e.message}")
                    }
                }
            } ?: run {
                Log.e("API_ERROR", "Retrofit instance is null")
            }
        }
    }
}
