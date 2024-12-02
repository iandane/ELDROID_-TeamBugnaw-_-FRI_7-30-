package com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.ui.bottomnav.BottomNavigationBar
import com.ucb.eldroid.ecoconnect.viewmodel.auth.ProjectViewModel
import java.util.*

class CreateProject : AppCompatActivity() {

    private val projectViewModel: ProjectViewModel by viewModels()

    private lateinit var titleField: EditText
    private lateinit var descriptionField: EditText
    private lateinit var moneyGoalField: EditText
    private lateinit var deadlineField: TextView
    private lateinit var imageView: ImageView
    private lateinit var createButton: Button

    private var projectImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_project)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        titleField = findViewById(R.id.titleProject)
        descriptionField = findViewById(R.id.projectDescription)
        moneyGoalField = findViewById(R.id.moneyGoal)
        deadlineField = findViewById(R.id.deadlineDateTextView)
        imageView = findViewById(R.id.addImage)
        createButton = findViewById(R.id.publishButton)

        backBtn.setOnClickListener {
            val intent = Intent(this, BottomNavigationBar::class.java)
            startActivity(intent)
            finish()
        }

        createButton.setOnClickListener {
            val title = titleField.text.toString().trim()
            val description = descriptionField.text.toString().trim()
            val moneyGoal = moneyGoalField.text.toString().toDoubleOrNull() ?: 0.0
            val deadline = deadlineField.text.toString().trim()

            // Format the deadline date
            val inputFormat = SimpleDateFormat("M/d/yyyy", Locale.US)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

            val formattedDeadline: String
            try {
                val date = inputFormat.parse(deadline) // Convert the input date string to a Date object
                formattedDeadline = outputFormat.format(date) // Format the Date object into the correct format
            } catch (e: Exception) {
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Log the formatted date to verify
            Log.d("Formatted Date", formattedDeadline) // Output: 2024-12-02
            // Call ViewModel to create the project
            projectViewModel.createProject(title, description, moneyGoal, deadline, projectImageUri)
        }

        // Observe project creation success
        projectViewModel.projectCreationSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Project Created Successfully", Toast.LENGTH_SHORT).show()
                finish()  // Close the activity after creation
            }
        }

        // Observe validation and error messages
        projectViewModel.validationError.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        projectViewModel.projectCreationError.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_REQUEST)
        }

        deadlineField.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    deadlineField.text = selectedDate
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_REQUEST) {
            data?.data?.let { uri ->
                projectImageUri = uri
                imageView.setImageURI(uri)  // Display the image in the ImageView
            }
        }
    }

    companion object {
        const val IMAGE_PICK_REQUEST = 1001
    }
}
