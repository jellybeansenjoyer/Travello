package com.example.travello.Data.Network.Routes

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)

data class SignupRequest(val name:String, val email: String, val password: String)
data class AuthResponse(val token: String)
data class GoogleClientIdResponse(
    val clientId: String
)

interface LoginApi {
    @POST("api/auth/register")
    suspend fun signup(@Body request: SignupRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<Void>

    @GET("api/getClientId")
    fun getGoogleClient(): Call<GoogleClientIdResponse>
}