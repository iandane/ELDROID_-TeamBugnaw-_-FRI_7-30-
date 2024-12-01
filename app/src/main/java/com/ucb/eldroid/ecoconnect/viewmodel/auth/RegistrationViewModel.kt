package com.ucb.eldroid.ecoconnect.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ucb.eldroid.ecoconnect.data.ApiService
import com.ucb.eldroid.ecoconnect.data.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterViewModel : ViewModel() {
    // LiveData to manage registration status and errors
    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> get() = _registrationSuccess

    private val _registrationError = MutableLiveData<String>()
    val registrationError: LiveData<String> get() = _registrationError

    private val _validationError = MutableLiveData<String>()
    val validationError: LiveData<String> get() = _validationError

    // Function to register a user
    fun registerUser(firstName: String, lastName: String, email: String, password: String, passwordConfirmation: String) {
        // Validate fields
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

        // Proceed with the registration process
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.15:8000/") // Replace with your server URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val user = User(firstName, lastName, email, password, passwordConfirmation)

        val call = apiService.postRegister(user)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
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
}