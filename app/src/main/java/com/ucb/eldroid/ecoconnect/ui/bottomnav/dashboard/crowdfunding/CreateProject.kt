package com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.Project
import com.ucb.eldroid.ecoconnect.ui.auth.Login
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.UUID

class CreateProject : AppCompatActivity() {

    private var imageUri: Uri? = null // To store selected image URI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_project)

        val deadlineDateTextView = findViewById<TextView>(R.id.deadlineDateTextView)

        deadlineDateTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this@CreateProject,
                { view, year, monthOfYear, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                    deadlineDateTextView.text = selectedDate
                },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog.show()
        }

        // Create project button click action
        val createProjectButton = findViewById<Button>(R.id.publishButton)
        createProjectButton.setOnClickListener {
            val token = getAuthToken()
            if (token.isNullOrEmpty()) {
                // Prompt the user to log in if token is not available
                Toast.makeText(this, "Please log in to create a project", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Login::class.java)) // Navigate to login screen
                finish()
            } else {
                createProject(token)  // Pass token to create project if authenticated
            }
        }

        // Add an image picker button (Assuming you have a button for this)
        val imagePickerButton = findViewById<ImageView>(R.id.addImage)
        imagePickerButton.setOnClickListener {
            pickImage()
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_REQUEST_CODE) {
            data?.data?.let { uri ->
                imageUri = uri
                // You can update your UI here to display the selected image
                // For example, set it to an ImageView
                val imageView = findViewById<ImageView>(R.id.addImage)
                imageView.setImageURI(uri)
            }
        }
    }

    private fun createProject(token: String) {
        val title = findViewById<EditText>(R.id.titleProject).text.toString()
        val description = findViewById<EditText>(R.id.projectDescription).text.toString()
        val goalAmount = findViewById<EditText>(R.id.moneyGoal).text.toString().toDoubleOrNull() ?: 0.0
        val deadline = findViewById<TextView>(R.id.deadlineDateTextView).text.toString()
        val imagePath = imageUri?.let { getImagePathFromUri(it) } // Use the selected image URI

        val project = Project(
            projectId = UUID.randomUUID().toString(), // Random UUID for projectId
            title = title,
            description = description,
            goalAmount = goalAmount,
            deadline = deadline,
            imagePath = imagePath
        )

        // Send the project to the backend
        sendProjectToBackend(project, token)
    }

    private fun sendProjectToBackend(project: Project, token: String) {
        val retrofit = RetrofitClient.instance
        val apiService = retrofit?.create(ApiService::class.java)

        // Pass the token as part of the Authorization header
        val call = apiService?.createProject(project, "Bearer $token")
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {

                    Toast.makeText(this@CreateProject, "Project successfully created!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CreateProject, "Failed to create project", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@CreateProject, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getImagePathFromUri(uri: Uri?): String? {
        // Get image path from URI (if you have image selection functionality implemented)
        return uri?.let {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                it.moveToFirst()
                val columnIndex = it.getColumnIndex(projection[0])
                return it.getString(columnIndex)
            }
        }
    }

    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("AUTH_TOKEN", null) // Retrieve the stored token
    }

    companion object {
        private const val IMAGE_PICK_REQUEST_CODE = 1000
    }
}