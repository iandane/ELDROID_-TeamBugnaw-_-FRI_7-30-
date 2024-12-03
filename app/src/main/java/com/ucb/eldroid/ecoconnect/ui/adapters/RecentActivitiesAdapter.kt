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
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding.CreateProject
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding.EditProject
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.dashboard_fragment.ProjectDetails
import java.net.MalformedURLException

class RecentActivitiesAdapter(var projects: List<SimplifiedProject>, private val context: Context) :
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
        Log.d("RecentActivitiesAdapter", "Binding project: ${project.title}")
        holder.projectTitle.text = project.title

        if (!project.image.isNullOrEmpty()) {
            try {
                if (project.image.startsWith("http://") || project.image.startsWith("https://")) {
                    Glide.with(holder.imageUrl.context)
                        .load(project.image)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.errorimage)
                        .into(holder.imageUrl)
                } else {
                    val fullImageUrl = "http://192.168.1.15/storage/${project.image}"
                    Log.d("ProjectImage", "Full image URL: $fullImageUrl")
                    Glide.with(holder.imageUrl.context)
                        .load(fullImageUrl)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.errorimage)
                        .into(holder.imageUrl)
                }
            } catch (e: MalformedURLException) {
                Glide.with(holder.imageUrl.context)
                    .load(R.drawable.placeholder)
                    .into(holder.imageUrl)
                Log.e("ProjectImage", "Invalid URL: ${project.image}")
            }
        } else {
            Glide.with(holder.imageUrl.context)
                .load(R.drawable.placeholder)
                .into(holder.imageUrl)
            Log.d("ProjectImage", "No image URL")
        }

        holder.itemView.setOnClickListener {
            showProjectOptionsDialog(project)
        }
    }

    override fun getItemCount(): Int = projects.size

    private fun showProjectOptionsDialog(project: SimplifiedProject) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.project_dialog_options, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val btnEdit = dialogView.findViewById<Button>(R.id.btnEdit)
        val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)

        btnEdit.setOnClickListener {
            val intent = Intent(context, EditProject::class.java)
            intent.putExtra("PROJECT_ID", project.id.toString())
            context.startActivity(intent)
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            deleteProject(project)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteProject(project: SimplifiedProject) {
        Toast.makeText(context, "Deleting project: ${project.title}", Toast.LENGTH_SHORT).show()
    }
}
