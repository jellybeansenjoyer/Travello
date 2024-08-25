package com.example.travello.Data.Repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.travello.Data.Network.RetrofitInstance
import com.example.travello.Data.Network.Routes.AuthResponse
import com.example.travello.Data.Network.Routes.GoogleClientIdResponse
import com.example.travello.Data.Network.Routes.LoginRequest
import com.example.travello.Data.Network.Routes.SignupRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.logging.Logger

class AuthRepository(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    fun login( email: String,password: String,onResult: (String?) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<AuthResponse> = RetrofitInstance.api.login(LoginRequest(email, password))
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
    fun signup(name:String , email: String, password: String, onResult: (String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<AuthResponse> = RetrofitInstance.api.signup(SignupRequest(name, email, password))

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

    fun getGoogleClientId(callback: (String?) -> Unit) {
        val call = RetrofitInstance.api.getGoogleClient()

        call.enqueue(object : retrofit2.Callback<GoogleClientIdResponse> {
            override fun onResponse(
                call: retrofit2.Call<GoogleClientIdResponse>,
                response: Response<GoogleClientIdResponse>
            ) {
                if (response.isSuccessful) {
                    val clientId = response.body()?.clientId
                    Log.d("clientId",clientId.toString())
                    callback(clientId)
                } else {
                    Log.e("clientId", "Errorrrrrr: "+response.errorBody())
                    callback(null)
                }
            }

            override fun onFailure(call: retrofit2.Call<GoogleClientIdResponse>, t: Throwable) {
                t.printStackTrace()
                callback(null)
            }
        })
    }
}