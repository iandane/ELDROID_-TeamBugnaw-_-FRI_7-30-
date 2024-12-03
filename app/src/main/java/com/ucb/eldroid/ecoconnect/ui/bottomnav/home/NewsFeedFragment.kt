package com.ucb.eldroid.ecoconnect.ui.bottomnav.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.ui.adapters.NewsFeedAdapter
import com.ucb.eldroid.ecoconnect.ui.adapters.Post


class NewsFeedFragment : Fragment() {

    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var newsFeedAdapter: NewsFeedAdapter
    private val postList = mutableListOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news_feed, container, false)

        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView)
        messagesRecyclerView.layoutManager = LinearLayoutManager(context)

        newsFeedAdapter = NewsFeedAdapter(requireContext(), postList)
        messagesRecyclerView.adapter = newsFeedAdapter

        loadDummyPosts()

        return view
    }

    private fun loadDummyPosts() {
        // Add some sample posts
        postList.add(Post("Jackie Chan", "World Environment Day!", "https://example.com/image1.jpg"))
        postList.add(Post("Emma Watson", "Support Clean Energy!", "https://example.com/image2.jpg"))
        postList.add(Post("Leonardo DiCaprio", "Save the Oceans!", null)) // Post without an image

        // Notify adapter of data change
        newsFeedAdapter.notifyDataSetChanged()
    }
}