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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.models.SimplifiedProject
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.dashboard_fragment.ProjectDetails

class RecentActivitiesAdapter(
    private val projects: List<SimplifiedProject>,
    private val context: Context,
    private val onDeleteClick: (id: String) -> Unit // Pass delete function
) : RecyclerView.Adapter<RecentActivitiesAdapter.ProjectViewHolder>() {

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

        if (!project.image.isNullOrEmpty()) {
            Glide.with(holder.imageUrl.context)
                .load(project.image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.errorimage)
                .into(holder.imageUrl)
        } else {
            Glide.with(holder.imageUrl.context)
                .load(R.drawable.placeholder)
                .into(holder.imageUrl)
        }

        holder.itemView.setOnClickListener {
            showProjectOptionsDialog(project)
        }
    }

    private fun showProjectOptionsDialog(project: SimplifiedProject) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.project_dialog_options, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val btnView = dialogView.findViewById<Button>(R.id.btnView)
        val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)

        btnView.setOnClickListener {
            val intent = Intent(context, ProjectDetails::class.java)
            context.startActivity(intent)
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            Log.d("RecentActivitiesAdapter", "Project ID to delete: ${project.id}")
            showDeleteConfirmationDialog(project.id)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDeleteConfirmationDialog(id: String) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Delete Project")
            .setMessage("Are you sure you want to delete this project?")
            .setPositiveButton("Yes") { _, _ ->
                Log.d("RecentActivitiesAdapter", "Confirming deletion for project ID: $id")
                onDeleteClick(id) // Call the delete function passed in the constructor
            }
            .setNegativeButton("No", null)
            .create()
        alertDialog.show()
    }

    override fun getItemCount(): Int = projects.size
}
