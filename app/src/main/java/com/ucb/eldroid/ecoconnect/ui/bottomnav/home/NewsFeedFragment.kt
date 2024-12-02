package com.ucb.eldroid.ecoconnect.ui.bottomnav.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.ui.adapters.NewsFeedAdapter
import com.ucb.eldroid.ecoconnect.ui.adapters.Post
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFeedFragment : Fragment() {
    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var newsFeedAdapter: NewsFeedAdapter
    private val postList = mutableListOf<Post>()
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news_feed, container, false)
        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView)
        messagesRecyclerView.layoutManager = LinearLayoutManager(context)

        apiService = RetrofitClient.instance?.create(ApiService::class.java)!!

        fetchPosts()

        return view
    }

    private fun fetchPosts() {
        val authToken = "Bearer YOUR_AUTH_TOKEN"
        apiService.getProjects(authToken).enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    val posts = response.body() ?: emptyList()
                    postList.clear()
                    postList.addAll(posts)
                    newsFeedAdapter = NewsFeedAdapter(requireContext(), postList)
                    messagesRecyclerView.adapter = newsFeedAdapter
                } else {
                    Log.e("NewsFeedFragment", "Failed to fetch posts: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.e("NewsFeedFragment", "API call failed: ${t.message}")
            }
        })
    }
}
