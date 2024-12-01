package com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ucb.eldroid.ecoconnect.R
import java.util.Calendar

class CreateProject : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_project)



        val deadlineDateTextView = findViewById<TextView>(R.id.deadlineDateTextView)


        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.environmental_data_items,
            android.R.layout.simple_spinner_item
        )

        deadlineDateTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this@CreateProject,
                { view, year, monthOfYear, dayOfMonth ->
                    val selectedDate = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    deadlineDateTextView.text = selectedDate
                },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog.show()
        }
    }

    fun onImageClicked(view: View?) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("image/*")
        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data

            val imageView = findViewById<ImageView>(R.id.addImage)
            imageView.setImageURI(selectedImageUri)

            val imagePath = getImagePathFromUri(selectedImageUri)
        }
    }

    fun getImagePathFromUri(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(projection[0])
            val imagePath = cursor.getString(columnIndex)
            cursor.close()
            return imagePath
        }
        return null
    }

    companion object {
        private const val IMAGE_PICK_REQUEST_CODE = 1000
    }
}