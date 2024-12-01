package com.ucb.eldroid.ecoconnect.viewmodel.auth

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> get() = _registrationSuccess

    private val _registrationError = MutableLiveData<String>()
    val registrationError: LiveData<String> get() = _registrationError

    private val _validationError = MutableLiveData<String>()
    val validationError: LiveData<String> get() = _validationError

    fun registerUser(firstName: String, lastName: String, email: String, password: String, passwordConfirmation: String) {
        if (firstName.isEmpty()) {
            _validationError.postValue("First name is required")
            return
        }

        if (lastName.isEmpty()) {
            _validationError.postValue("Last name is required")
            return
        }

        if (email.isEmpty()) {
            _validationError.postValue("Email address is required")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _validationError.postValue("Enter a valid email address")
            return
        }

        if (password.isEmpty()) {
            _validationError.postValue("Password is required")
            return
        }

        if (password.length < 6) {
            _validationError.postValue("Password must be at least 6 characters long")
            return
        }

        if (password != passwordConfirmation) {
            _validationError.postValue("Passwords do not match")
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.228:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val user = User(firstName, lastName, email, password, passwordConfirmation)

        val call = apiService.postRegister(user)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val gson = Gson()
                    val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                    val token = jsonObject.get("token").asString
                    user.token = token // Set the token in the user object
                    saveUser(user)
                    _registrationSuccess.postValue(true)
                } else {
                    _registrationError.postValue("Registration Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                _registrationError.postValue("Registration Failed: ${t.message}")
            }
        })
    }

    private fun saveUser(user: User) {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("USER_FIRST_NAME", user.firstName)
        editor.putString("USER_LAST_NAME", user.lastName)
        editor.putString("USER_EMAIL", user.email)
        editor.putString("AUTH_TOKEN", user.token) // Save the token
        editor.apply()
    }
}
