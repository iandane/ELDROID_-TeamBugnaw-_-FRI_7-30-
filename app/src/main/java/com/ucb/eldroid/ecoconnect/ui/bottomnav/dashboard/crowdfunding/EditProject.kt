package com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.Project
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProject : AppCompatActivity() {
    private var projectId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_project)

        // Retrieve the Project ID from Intent
        projectId = intent.getStringExtra("PROJECT_ID")?.toIntOrNull() // Ensure it's converted to Int if possible
        Log.d("EditProject", "Received Project ID: $projectId")

        // Check if the projectId is valid
        if (projectId != null) {
            fetchProjectDetails(projectId!!)  // Ensure projectId is non-null
        } else {
            Toast.makeText(this, "Invalid Project ID", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to fetch project details using the projectId
    private fun fetchProjectDetails(id: Int) {
        // Get the token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("AUTH_TOKEN", null)

        if (token == null) {
            Toast.makeText(this, "Token is missing", Toast.LENGTH_SHORT).show()
            return
        }

        // Create the Retrofit service instance
        val apiService = RetrofitClient.instance?.create(ApiService::class.java)

        Log.d("APIRequest", "Token: $token")
        Log.d("APIRequest", "Fetching project details for ID: $id")

        // Make the API call with the token in the Authorization header
        val call = apiService?.getProjectDetails(id, "Bearer $token")

        call?.enqueue(object : Callback<ApiService.ProjectResponse> {
            override fun onResponse(call: Call<ApiService.ProjectResponse>, response: Response<ApiService.ProjectResponse>) {
                if (response.isSuccessful) {
                    val projectResponse = response.body()
                    Log.d("APIResponse", "Project details: $projectResponse")

                    // Check if the list is not empty and use the first project
                    projectResponse?.projects?.firstOrNull()?.let {
                        populateProjectDetails(it)  // Pass the first project to the populate function
                    } ?: run {
                        Toast.makeText(this@EditProject, "No projects found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("APIError", "Failed response: ${response.errorBody()?.string()}")
                    Toast.makeText(this@EditProject, "Failed to load project", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiService.ProjectResponse>, t: Throwable) {
                Log.e("APIError", "Error: ${t.message}")
                Toast.makeText(this@EditProject, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun populateProjectDetails(project: Project) {
        findViewById<EditText>(R.id.titleProject).setText(project.title)
        findViewById<EditText>(R.id.projectDescription).setText(project.description)
        findViewById<EditText>(R.id.moneyGoal).setText(project.moneyGoal.toString())
        findViewById<TextView>(R.id.deadlineDateTextView).text = project.deadline

        // Image
        project.image?.let {
            val imageView = findViewById<ImageView>(R.id.addImage)
            Glide.with(this).load(it).into(imageView)  // Use Glide to load image
        }
    }
}
