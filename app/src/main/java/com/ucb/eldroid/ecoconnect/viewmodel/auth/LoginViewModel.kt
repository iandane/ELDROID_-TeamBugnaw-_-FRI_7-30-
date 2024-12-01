package com.ucb.eldroid.ecoconnect.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.LoginRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    val emailError = MutableLiveData<String?>()
    val passwordError = MutableLiveData<String?>()

    fun validateCredentials(email: String, password: String): Boolean {
        var isValid = true

        // Email validation
        if (email.isEmpty()) {
            emailError.value = "Please enter your email"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.value = "Please enter a valid email address"
            isValid = false
        } else {
            emailError.value = null  // Clear any previous error
        }

        // Password validation
        if (password.isEmpty()) {
            passwordError.value = "Please enter your password"
            isValid = false
        } else {
            passwordError.value = null  // Clear any previous error
        }

        return isValid
    }

    fun login(email: String, password: String) {
        // Retrofit setup
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.15:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val loginRequest = LoginRequest(email, password)

        // API call
        val call: Call<ResponseBody?> = apiService.postLogin(loginRequest)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    _loginResult.postValue(true)
                } else {
                    _loginResult.postValue(false)
                    _errorMessage.postValue("Login Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                _loginResult.postValue(false)
                _errorMessage.postValue("Login Failed: ${t.message}")
            }
        })
    }
}