package com.example.travello.Data.Network

import com.example.travello.Data.Network.Routes.LoginApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: LoginApi by lazy {
        retrofit.create(LoginApi::class.java)
    }
}