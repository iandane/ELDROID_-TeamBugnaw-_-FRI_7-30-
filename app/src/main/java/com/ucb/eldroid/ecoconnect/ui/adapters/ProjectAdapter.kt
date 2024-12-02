package com.ucb.eldroid.ecoconnect.ui.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.models.Project
import java.net.URL

class ProjectAdapter(private val projects: List<Project>) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.profileimageView)
        val username: TextView = itemView.findViewById(R.id.username)
        val postContent: TextView = itemView.findViewById(R.id.postcontent)
        val projectImageView: ImageView = itemView.findViewById(R.id.imageView2)
        val heartImageView: ImageView = itemView.findViewById(R.id.heart)
        val commentsRecyclerView: RecyclerView = itemView.findViewById(R.id.commentRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.newsfeed_list, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.username.text = "${project.projectUser.first_name} ${project.projectUser.last_name}"
        holder.postContent.text = project.description

        // Load images without using Glide
        val projectImageUrl = project.imagePath
        val profileImageUrl = project.projectUser.profile?.picture
        if (projectImageUrl != null) {
            loadImageFromUrl(holder.projectImageView, projectImageUrl)
        }
        if (profileImageUrl != null) {
            loadImageFromUrl(holder.profileImageView, profileImageUrl)
        }

        // Handle comments and heart reactions here
        // TODO: Set up comments RecyclerView and heart reactions
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    // Function to load image from URL
    private fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
        AsyncTask.execute {
            try {
                val url = URL(imageUrl)
                val bitmap = BitmapFactory.decodeStream(url.openStream())
                imageView.post {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
