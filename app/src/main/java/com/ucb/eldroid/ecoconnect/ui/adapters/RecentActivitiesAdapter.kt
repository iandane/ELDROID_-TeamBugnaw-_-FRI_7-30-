package com.ucb.eldroid.ecoconnect.ui.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.models.SimplifiedProject
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.dashboard_fragment.ProjectDetails
import java.net.MalformedURLException

class RecentActivitiesAdapter(private val projects: List<SimplifiedProject>, private val context: Context) :
    RecyclerView.Adapter<RecentActivitiesAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageUrl: ShapeableImageView = view.findViewById(R.id.imageUrl)
        val projectTitle: TextView = view.findViewById(R.id.projectTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recent_activities_list, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.projectTitle.text = project.title

        // Use the Retrofit BASE_URL for constructing the image URL

        if (!project.image.isNullOrEmpty()) {
            // Check if the image is a valid URL
            try {
                // Check if the image path starts with "http" or "https"
                if (project.image.startsWith("http://") || project.image.startsWith("https://")) {
                    // If it's a full URL, load it directly
                    Glide.with(holder.imageUrl.context)
                        .load(project.image)  // Load the image from the full URL
                        .placeholder(R.drawable.placeholder)  // Show a placeholder while the image is loading
                        .error(R.drawable.errorimage)  // Show an error image if loading fails
                        .into(holder.imageUrl)
                } else {
                    // If it's not a URL, assume it's a relative path and construct the full URL
                    val fullImageUrl = "http://192.168.1.15/storage/${project.image}"

                    // Log the constructed URL for debugging
                    Log.d("ProjectImage", "Full image URL: $fullImageUrl")

                    Glide.with(holder.imageUrl.context)
                        .load(fullImageUrl)  // Load the image from the full URL
                        .placeholder(R.drawable.placeholder)  // Show a placeholder while the image is loading
                        .error(R.drawable.errorimage)  // Show an error image if loading fails
                        .into(holder.imageUrl)
                }
            } catch (e: MalformedURLException) {
                // If the image URL is malformed, fallback to placeholder and log the error
                Glide.with(holder.imageUrl.context)
                    .load(R.drawable.placeholder)  // Fallback to placeholder image
                    .into(holder.imageUrl)
                Log.e("ProjectImage", "Invalid URL: ${project.image}")
            }
        } else {
            // If there's no image URL, use the placeholder image
            Glide.with(holder.imageUrl.context)
                .load(R.drawable.placeholder)  // Fallback to placeholder image
                .into(holder.imageUrl)
            Log.d("ProjectImage", "No image URL")
        }

        holder.itemView.setOnClickListener {
            // Show the dialog when a project is clicked
            showProjectOptionsDialog(project)
        }
    }
    override fun getItemCount(): Int = projects.size
    private fun showProjectOptionsDialog(project: SimplifiedProject) {
        // Inflate the dialog layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.project_dialog_options, null)

        // Create the AlertDialog
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        // Find the buttons
        val btnView = dialogView.findViewById<Button>(R.id.btnView)
        val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)

        // Handle View button click
        btnView.setOnClickListener {
            // Perform the view action, e.g., open project details screen
            val intent = Intent(context, ProjectDetails::class.java)
            context.startActivity(intent)
            dialog.dismiss()
        }

        // Handle Delete button click
        btnDelete.setOnClickListener {
            // Handle deletion logic here
            deleteProject(project)
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }
    private fun deleteProject(project: SimplifiedProject) {
        // Implement project deletion logic here
        // This could be a network call or database operation to remove the project
        Toast.makeText(context, "Deleting project: ${project.title}", Toast.LENGTH_SHORT).show()
    }
}


