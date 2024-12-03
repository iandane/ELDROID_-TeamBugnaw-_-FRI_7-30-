package com.ucb.eldroid.ecoconnect.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.ProjectResponse
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import kotlinx.coroutines.launch

class NewsFeedViewModel : ViewModel() {

    fun fetchProjects(token: String) {
        val apiService = RetrofitClient.instance?.create(ApiService::class.java)

        apiService?.let { service ->
            // Launch a coroutine to fetch projects
            viewModelScope.launch {
                try {
                    val projectResponse: ProjectResponse = service.getProjects("Bearer $token")
                    // Handle the fetched projects
                } catch (e: Exception) {
                    // Handle the error
                }
            }
        }
    }
}
