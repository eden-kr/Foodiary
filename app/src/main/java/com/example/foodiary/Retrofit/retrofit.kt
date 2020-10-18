package com.example.foodiary.Retrofit

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object retrofit {
    private const val url = "http://3.35.37.85:5000"
    private val gs = GsonBuilder()
        .setLenient()
        .create()

    @Volatile
    private var server: RetrofitAPI? = null

    fun getInstance(): RetrofitAPI =
        server ?: synchronized(this) {
            server ?: retrofit(url).also { server = it }
        }

    private fun retrofit(url: String): RetrofitAPI {
        return Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gs))
            .build().create(RetrofitAPI::class.java)
    }
}