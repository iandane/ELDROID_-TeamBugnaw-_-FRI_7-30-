package com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ucb.eldroid.ecoconnect.R;

import java.util.Calendar;

public class CreateProject extends AppCompatActivity {

    private static final int IMAGE_PICK_REQUEST_CODE = 1000; // Arbitrary request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);


        Spinner envDataSpinner = findViewById(R.id.envDataSpinner);

        TextView deadlineDateTextView = findViewById(R.id.deadlineDateTextView);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.environmental_data_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        envDataSpinner.setAdapter(adapter);

        deadlineDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateProject.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Set the selected date to the TextView
                                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                deadlineDateTextView.setText(selectedDate);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });
    }
    public void onImageClicked(View view) {
        // Create an intent to open the image picker
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");  // Filter for images
        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE); // Start the image picker activity
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get the image URI
            Uri selectedImageUri = data.getData();

            // Optionally, you can set the image in an ImageView
            ImageView imageView = findViewById(R.id.addImage); // Your ImageView where you want to display the image
            imageView.setImageURI(selectedImageUri);

            // If you want to get the path of the selected image, you can use:
            String imagePath = getImagePathFromUri(selectedImageUri);
            // Do something with the image path, like uploading the image to a server
        }
    }

    public String getImagePathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        }
        return null;
    }
}