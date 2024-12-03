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

    fun fetchProjectsTitleAndImage(authToken: String) {
        val apiService = RetrofitClient.instance?.create(ApiService::class.java)
        apiService?.getProjectsTitleAndImage("Bearer $authToken")
            ?.enqueue(object : Callback<ApiService.ProjectResponse> {
                override fun onResponse(
                    call: Call<ApiService.ProjectResponse>,
                    response: Response<ApiService.ProjectResponse>
                ) {
                    if (response.isSuccessful) {
                        val simplifiedProject = response.body()?.projects?.map {
                            SimplifiedProject(title = it.title, image = it.image)
                        } ?: emptyList()

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
