package com.ucb.eldroid.ecoconnect.viewmodel.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.SimplifiedProject
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {
    private val _projects = MutableLiveData<List<SimplifiedProject>>()
    val projects: LiveData<List<SimplifiedProject>> get() = _projects

    private val _deleteResponse = MutableLiveData<String>()
    val deleteResponse: LiveData<String> get() = _deleteResponse

    fun fetchProjectsTitleAndImage(authToken: String) {
        val apiService = RetrofitClient.instance?.create(ApiService::class.java)
        apiService?.getProjectsTitleAndImage("Bearer $authToken")
            ?.enqueue(object : Callback<ApiService.ProjectResponse> {
                override fun onResponse(
                    call: Call<ApiService.ProjectResponse>,
                    response: Response<ApiService.ProjectResponse>
                ) {
                    if (response.isSuccessful) {
                        val simplifiedProjects = response.body()?.projects?.map {
                            SimplifiedProject(id = it.id, title = it.title, image = it.image)
                        } ?: emptyList()
                        _projects.postValue(simplifiedProjects)
                    } else {
                        Log.e("DashboardViewModel", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ApiService.ProjectResponse>, t: Throwable) {
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
