package com.example.foodiary.Retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.datatype.DatatypeConstants.SECONDS

import javax.xml.datatype.DatatypeConstants.MINUTES

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


//Singleton Retrofit object

object RxRetrofit{
    private val url = "http://3.35.37.85:5000"
    private var retrofit : RetrofitAPI? = null
    private val gson = GsonBuilder()
        .setLenient()
        .create()
    private val okHttp :OkHttpClient? = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.MINUTES)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(15, TimeUnit.SECONDS)
    .build()

    fun getInstance() : RetrofitAPI? {
        if(retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(okHttp)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitAPI::class.java)
        }
        return retrofit
    }
}