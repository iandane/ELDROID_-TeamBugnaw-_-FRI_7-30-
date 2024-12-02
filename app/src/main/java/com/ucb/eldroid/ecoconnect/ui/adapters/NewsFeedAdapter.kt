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
import java.net.URL

class NewsFeedAdapter(private val context: Context, private val postList: List<Post>) :
    RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder>() {

    class NewsFeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.profileimageView)
        val usernameTextView: TextView = itemView.findViewById(R.id.username)
        val contentTextView: TextView = itemView.findViewById(R.id.postcontent)
        val projectImageView: ImageView = itemView.findViewById(R.id.imageView2)
        val heartImageView: ImageView = itemView.findViewById(R.id.heart)
        val commentsRecyclerView: RecyclerView = itemView.findViewById(R.id.commentRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFeedViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.newsfeed_list, parent, false)
        return NewsFeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsFeedViewHolder, position: Int) {
        val post = postList[position]
        holder.contentTextView.text = post.content
        holder.usernameTextView.text = post.username

        // Load images without Glide
        val profileImageUrl = post.profileImage
        val projectImageUrl = post.projectImage
        if (profileImageUrl != null) {
            loadImageFromUrl(holder.profileImageView, profileImageUrl)
        }
        if (projectImageUrl != null) {
            loadImageFromUrl(holder.projectImageView, projectImageUrl)
        }
    }

    override fun getItemCount(): Int {
        return postList.size
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
