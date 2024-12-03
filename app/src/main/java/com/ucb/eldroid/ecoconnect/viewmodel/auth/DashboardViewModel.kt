package com.ucb.eldroid.ecoconnect.viewmodel.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.Project
import com.ucb.eldroid.ecoconnect.data.models.SimplifiedProject
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {
    private val _projects = MutableLiveData<List<SimplifiedProject>>()
    val projects: LiveData<List<SimplifiedProject>> get() = _projects

    private val _projectIds = MutableLiveData<List<String>>() // LiveData for project IDs
    val projectIds: LiveData<List<String>> get() = _projectIds

    fun fetchProjectsTitleAndImage(authToken: String) {
        val apiService = RetrofitClient.instance?.create(ApiService::class.java)
        apiService?.getProjectsTitleAndImage("Bearer $authToken")
            ?.enqueue(object : Callback<ApiService.ProjectResponse> {
                override fun onResponse(
                    call: Call<ApiService.ProjectResponse>,
                    response: Response<ApiService.ProjectResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("DashboardViewModel", "API Response: ${response.body()}")

                        // Log the projects before mapping to SimplifiedProject
                        response.body()?.projects?.forEach { project ->
                            Log.d("DashboardViewModel", "Project: id=${project.id}, title=${project.title}, image=${project.image}")
                        }

                        val simplifiedProject = response.body()?.projects?.mapNotNull {
                            // Check if the ID is not empty or invalid
                            if (!it.id.isNullOrEmpty()) {
                                SimplifiedProject(id = it.id, title = it.title, image = it.image)
                            } else {
                                null // Skip entries with empty or invalid IDs
                            }
                        } ?: emptyList()


                        // Log after transformation to ensure correct mapping
                        Log.d("DashboardViewModel", "Mapped Projects: $simplifiedProject")

                        _projects.postValue(simplifiedProject)

                    } else {
                        Log.e("DashboardViewModel", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ApiService.ProjectResponse>, t: Throwable) {
                    Log.e("DashboardViewModel", "Failed: ${t.message}")
                }
            })
    }

}
