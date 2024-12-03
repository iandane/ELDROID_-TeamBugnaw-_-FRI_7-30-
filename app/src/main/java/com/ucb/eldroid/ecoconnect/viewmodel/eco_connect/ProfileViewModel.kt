package com.ucb.eldroid.ecoconnect.viewmodel.eco_connect

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.User
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _userData = MutableLiveData<User?>()
    val userData: MutableLiveData<User?> get() = _userData

    fun fetchUserData(token: String) {
        val retrofit = RetrofitClient.instance
        if (retrofit == null) {
            Log.e("ProfileViewModel", "Failed to initialize network client")
            return
        }

        val apiService = retrofit.create(ApiService::class.java)

        val call = apiService.getUser("Bearer $token")
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        _userData.postValue(user)
                    } else {
                        Log.e("ProfileViewModel", "Error parsing user data from response")
                    }
                } else {
                    Log.e("ProfileViewModel", "Error response: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("ProfileViewModel", "Network call failed: ${t.message}")
            }
        })
    }
}