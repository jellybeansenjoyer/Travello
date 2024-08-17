package com.example.travello.Data.Repository

import android.content.Context
import android.content.SharedPreferences
import com.example.travello.Data.Network.RetrofitInstance
import com.example.travello.Data.Network.Routes.LoginRequest
import com.example.travello.Data.Network.Routes.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthRepository(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun login(email: String, password: String, onResult: (String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<LoginResponse> = RetrofitInstance.api.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                val token = response.body()?.token
                if (token != null) {
                    saveToken(token)
                }
                onResult(token)
            } else {
                onResult(null)
            }
        }
    }

    private fun saveToken(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    fun logout(onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.logout()

            if (response.isSuccessful) {
                sharedPreferences.edit().remove("jwt_token").apply()
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }
}