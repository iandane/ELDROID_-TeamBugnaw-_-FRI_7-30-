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

class NewsFeedAdapter(private val context: Context, private val projectList: List<Project>) :
    RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder>() {

    class NewsFeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        val moneyGoalTextView: TextView = itemView.findViewById(R.id.money_goal)
        val deadlineTextView: TextView = itemView.findViewById(R.id.deadline)
        val projectImageView: ImageView = itemView.findViewById(R.id.imageView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFeedViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.newsfeed_list, parent, false)
        return NewsFeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsFeedViewHolder, position: Int) {
        val project = projectList[position]

        // Set title and description from the project
        holder.titleTextView.text = project.title
        holder.descriptionTextView.text = project.description

        // Set money goal and deadline
        holder.moneyGoalTextView.text = project.moneyGoal.toString()
        holder.deadlineTextView.text = project.deadline

        // Load project image
        val projectImageUrl = project.image
        if (projectImageUrl != null) {
            loadImageFromUrl(holder.projectImageView, projectImageUrl)
        }
    }

    override fun getItemCount(): Int {
        return projectList.size
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
