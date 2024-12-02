package com.ucb.eldroid.ecoconnect.viewmodel.auth

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    fun createProject(title: String, description: String, moneyGoal: Double, deadline: String, image: Uri?) {
        // Validation logic similar to registration
        if (title.isEmpty()) {
            _validationError.postValue("Title is required")
            return
        }
        if (description.isEmpty()) {
            _validationError.postValue("Description is required")
            return
        }
        if (moneyGoal <= 0) {
            _validationError.postValue("Money goal must be greater than zero")
            return
        }
        if (deadline.isEmpty()) {
            _validationError.postValue("Deadline is required")
            return
        }

        // Get the token from SharedPreferences (same as in registration)
        val sharedPreferences = getApplication<Application>().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("AUTH_TOKEN", null)
        if (token == null) {
            _projectCreationError.postValue("No authentication token found")
            return
        }

        val retrofit = RetrofitClient.instance
        if (retrofit == null) {
            _projectCreationError.postValue("Failed to initialize network client")
            return
        }

        val apiService = retrofit.create(ApiService::class.java)

        // Prepare request body for the project fields
        val titlePart = title.toRequestBody("text/plain".toMediaType())
        val descriptionPart = description.toRequestBody("text/plain".toMediaType())
        val moneyGoalPart = moneyGoal.toString().toRequestBody("text/plain".toMediaType())
        val deadlinePart = deadline.toRequestBody("text/plain".toMediaType())

        Log.d("RequestData", "Title: $title, Description: $description, Money Goal: $moneyGoal, Deadline: $deadline")


// Prepare image part if present
        var imagePart: MultipartBody.Part? = null
        if (image != null) {
            val imageFile = getFileFromUri(image)
            if (imageFile != null) {
                // Use the getImageRequestBody function to create RequestBody from the file
                val requestBody = getImageRequestBody(imageFile)
                imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
            } else {
                _projectCreationError.postValue("Image file not found")
                return
            }
        }

// Call API with proper headers and parts
        val call = apiService.createProject("Bearer $token", titlePart, descriptionPart, imagePart, moneyGoalPart, deadlinePart)
        Log.d("APIRequest", "Sending project creation request")
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("APIRequest", "Response Code: ${response.code()}")
                if (response.isSuccessful) {
                    _projectCreationSuccess.postValue(true)
                    Log.d("APIRequest", "Project created successfully: ${response.body()?.string()}")
                } else {
                    _projectCreationError.postValue("Failed to create project: ${response.message()}")
                    Log.e("APIRequest", "Failed response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("APIRequest", "Request failed: ${t.message}")
            }
        })
    }

    // Function to convert file to RequestBody
    private fun getImageRequestBody(imageFile: File): RequestBody {
        val fileBytes = imageFile.readBytes()  // Read file into a ByteArray
        return fileBytes.toRequestBody("image/*".toMediaType())  // Convert ByteArray to RequestBody
    }
    // Function to get the actual file from Uri
    private fun getFileFromUri(uri: Uri): File? {
        val contentResolver = getApplication<Application>().applicationContext.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val tempFile = File(getApplication<Application>().cacheDir, "temp_image_${System.currentTimeMillis()}")

        try {
            inputStream?.use { input ->
                val outputStream = tempFile.outputStream()
                input.copyTo(outputStream)
                return tempFile
            }
        } catch (e: Exception) {
            Log.e("ProjectViewModel", "Error while copying file: ${e.message}")
        }
        return null
    }
}
