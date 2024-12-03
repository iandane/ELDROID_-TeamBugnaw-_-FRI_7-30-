package com.ucb.eldroid.ecoconnect.viewmodel.auth

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.Project
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    private val _projectCreationSuccess = MutableLiveData<Boolean>()
    val projectCreationSuccess: LiveData<Boolean> get() = _projectCreationSuccess

    private val _projectCreationError = MutableLiveData<String>()
    val projectCreationError: LiveData<String> get() = _projectCreationError

    private val _validationError = MutableLiveData<String>()
    val validationError: LiveData<String> get() = _validationError

    private val _deleteResponse = MutableLiveData<String>()
    val deleteResponse: LiveData<String> = _deleteResponse

    private val apiService: ApiService = RetrofitClient.instance?.create(ApiService::class.java) ?: throw IllegalStateException("ApiService not initialized")

    // Existing method to create a project
    fun createProject(title: String, description: String, moneyGoal: Double, deadline: String, image: Uri?) {
        // Validate fields
        if (title.isEmpty() || description.isEmpty() || moneyGoal <= 0 || deadline.isEmpty()) {
            _validationError.postValue("All fields must be valid")
            return
        }

        // Get authentication token
        val sharedPreferences = getApplication<Application>().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("AUTH_TOKEN", null)
        if (token == null) {
            _projectCreationError.postValue("No authentication token found")
            return
        }

        // Prepare request parts
        val titlePart = title.toRequestBody("text/plain".toMediaType())
        val descriptionPart = description.toRequestBody("text/plain".toMediaType())
        val moneyGoalPart = moneyGoal.toString().toRequestBody("text/plain".toMediaType())
        val deadlinePart = deadline.toRequestBody("text/plain".toMediaType())

        var imagePart: MultipartBody.Part? = null
        if (image != null) {
            val imageFile = getFileFromUri(image)
            if (imageFile != null) {
                val requestBody = imageFile.asRequestBody("image/*".toMediaType())
                imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
            } else {
                _projectCreationError.postValue("Failed to process image")
                return
            }
        }

        // Make API call
        val call = apiService.createProject(
            "Bearer $token",
            titlePart,
            descriptionPart,
            moneyGoalPart,
            deadlinePart,
            imagePart
        )

        call?.enqueue(object : Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                if (response.isSuccessful) {
                    _projectCreationSuccess.postValue(true)
                } else {
                    _projectCreationError.postValue("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                _projectCreationError.postValue("Network error: ${t.message}")
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


    private fun getFileFromUri(uri: Uri): File? {
        val contentResolver = getApplication<Application>().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("temp_image", ".jpg", getApplication<Application>().cacheDir)

        return try {
            inputStream?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            Log.e("ProjectViewModel", "Error converting Uri to File: ${e.message}")
            null
        }
    }
}
