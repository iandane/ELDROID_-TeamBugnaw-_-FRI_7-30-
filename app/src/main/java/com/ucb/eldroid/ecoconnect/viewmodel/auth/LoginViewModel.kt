package com.ucb.eldroid.ecoconnect.viewmodel.auth

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.LoginRequest
import com.ucb.eldroid.ecoconnect.data.models.User
import com.ucb.eldroid.ecoconnect.utils.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application, private val sharedPreferences: SharedPreferences) : AndroidViewModel(application) {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    val emailError = MutableLiveData<String?>()
    val passwordError = MutableLiveData<String?>()

    fun validateCredentials(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            emailError.value = "Please enter your email"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.value = "Please enter a valid email address"
            isValid = false
        } else {
            emailError.value = null // Clear any previous error
        }

        if (password.isEmpty()) {
            passwordError.value = "Please enter your password"
            isValid = false
        } else {
            passwordError.value = null // Clear any previous error
        }

        return isValid
    }

    fun login(email: String, password: String) {
        val retrofit = RetrofitClient.instance
        if (retrofit == null) {
            _loginResult.postValue(false)
            _errorMessage.postValue("Failed to initialize network client")
            return
        }

        val apiService = retrofit.create(ApiService::class.java)
        val loginRequest = LoginRequest(email, password)

        val call: Call<ResponseBody> = apiService.postLogin(loginRequest)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    val user = parseUserFromResponse(response)
                    if (user != null) {
                        saveUser(user)
                        _loginResult.postValue(true)
                    } else {
                        _loginResult.postValue(false)
                        _errorMessage.postValue("Login Failed: Unable to parse user data")
                    }
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

    private fun parseUserFromResponse(response: Response<ResponseBody?>): User? {
        return try {
            val userJson = response.body()?.string()
            val gson = Gson()
            gson.fromJson(userJson, User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    private fun saveUser(user: User) {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("USER_FIRST_NAME", user.firstName)
        editor.putString("USER_LAST_NAME", user.lastName)
        editor.putString("USER_EMAIL", user.email)
        editor.putString("AUTH_TOKEN", user.token) // Store the token
        editor.putString("USER_ID", user.id) // Store the user ID
        editor.putBoolean("isLoggedIn", true) // Update login state
        editor.apply()
    }



    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
}