package com.ucb.eldroid.ecoconnect.viewmodel.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.Project
import com.ucb.eldroid.ecoconnect.data.models.SimplifiedProject
import com.ucb.eldroid.ecoconnect.data.response.ProjectResponse
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val _projects = MutableLiveData<List<SimplifiedProject>>()
    val projects: LiveData<List<SimplifiedProject>> get() = _projects

    private val _projectIds = MutableLiveData<List<String>>() // LiveData for project IDs
    val projectIds: LiveData<List<String>> get() = _projectIds

    private val _deleteResponse = MutableLiveData<String>()
    val deleteResponse: LiveData<String> get() = _deleteResponse

    fun fetchProjectsTitleAndImage(authToken: String) {
        val apiService = RetrofitClient.instance?.create(ApiService::class.java)
        apiService?.getProjectsTitleAndImage("Bearer $authToken")
            ?.enqueue(object : Callback<ProjectResponse> {
                override fun onResponse(
                    call: Call<ProjectResponse>,
                    response: Response<ProjectResponse>
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

                override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                    Log.e("DashboardViewModel", "Failed: ${t.message}")
                }
            })
    }
    fun deleteProject(id: String, token: String) {
        viewModelScope.launch {
            try {
                Log.d("DeleteProject", "Attempting to delete project with ID: $id")
                Log.d("DeleteProject", "Using token: Bearer $token")
                val apiService = RetrofitClient.instance?.create(ApiService::class.java)
                val call = apiService?.deleteProject("Bearer $token", id.toString())
                call?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Log.d("DeleteProject", "Project deleted successfully")
                            _deleteResponse.postValue("Project deleted successfully")
                        } else {
                            val errorMessage = response.errorBody()?.string() ?: "Error deleting project"
                            Log.e("DeleteProject", "API error: $errorMessage, Response code: ${response.code()}")
                            _deleteResponse.postValue("Error deleting project: ${response.code()} - $errorMessage")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("DeleteProject", "Network error: ${t.message}")
                        _deleteResponse.postValue("Network error: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("DeleteProject", "Exception: ${e.localizedMessage}")
                _deleteResponse.postValue("Exception: ${e.localizedMessage}")
            }
        }
    }

}