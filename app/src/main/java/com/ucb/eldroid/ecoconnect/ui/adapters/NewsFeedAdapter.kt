package com.ucb.eldroid.ecoconnect.ui.adapters

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ucb.eldroid.ecoconnect.R
import java.net.URL

class NewsFeedAdapter(
    private val context: Context,
    private val postList: List<Post>
) : RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFeedViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.newsfeed_list, parent, false)
        return NewsFeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsFeedViewHolder, position: Int) {
        val post = postList[position]

        holder.username.text = post.username
        holder.postContent.text = post.content

        // If there's an image URL, load it manually in the background
        if (post.imageUrl != null) {
            // Load the image in a background thread to avoid blocking the UI thread
            Thread {
                try {
                    val url = URL(post.imageUrl)
                    val connection = url.openConnection()
                    connection.connect()
                    val inputStream = connection.getInputStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    // Run on the main thread to update the UI
                    (context as Activity).runOnUiThread {
                        holder.imageView.setImageBitmap(bitmap)
                        holder.imageView.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // In case of an error, hide the image view
                    (context as Activity).runOnUiThread {
                        holder.imageView.visibility = View.GONE
                    }
                }
            }.start()
        } else {
            holder.imageView.visibility = View.GONE
        }

        // Handle like button
        holder.heart.setOnClickListener {
            Toast.makeText(context, "Liked ${post.username}", Toast.LENGTH_SHORT).show()
        }

        // Handle comment post button
        holder.postCommentButton.setOnClickListener {
            val comment = holder.commentEditText.text.toString().trim()
            if (comment.isNotEmpty()) {
                Toast.makeText(context, "Commented: $comment", Toast.LENGTH_SHORT).show()
                holder.commentEditText.setText("")
            }
        }
    }

    override fun getItemCount(): Int = postList.size

    inner class NewsFeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val postContent: TextView = itemView.findViewById(R.id.postcontent)
        val imageView: ImageView = itemView.findViewById(R.id.imageView2)
        val heart: ImageView = itemView.findViewById(R.id.heart)
        val commentEditText: EditText = itemView.findViewById(R.id.commentEditText)
        val postCommentButton: Button = itemView.findViewById(R.id.postCommentButton)
    }
}