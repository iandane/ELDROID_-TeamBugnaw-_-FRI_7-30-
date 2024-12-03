package com.ucb.eldroid.ecoconnect.viewmodel.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.User
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> get() = _deleteSuccess

    fun fetchUserData(token: String) {
        val retrofit = RetrofitClient.instance
        val apiService = retrofit?.create(ApiService::class.java)

        apiService?.getUser("Bearer $token")?.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        _user.postValue(user)
                    } else {
                        _errorMessage.postValue("Response body is null")
                    }
                } else {
                    _errorMessage.postValue("Failed to fetch user data: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _errorMessage.postValue("Error: ${t.message}")
            }
        })
    }

    fun deleteAccount(token: String, userId: String) {
        val retrofit = RetrofitClient.instance
        val apiService = retrofit?.create(ApiService::class.java)

        apiService?.deleteUserAccount("Bearer $token", userId)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    _deleteSuccess.postValue(true)
                } else {
                    _errorMessage.postValue("Failed to delete account")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _errorMessage.postValue("Error: ${t.message}")
            }
        })
    }
}
