package com.example.foodiary.Retrofit

import com.example.foodiary.Repository.Remote.FoodDataPOJO
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitAPI{
    //API 사용
    //Flask + Cnn
    @Multipart
    @POST("/image")
    fun getNutrientFromImage(
        @Part file : MultipartBody.Part
    ) : Single<List<FoodDataPOJO>>

    //NutrientData
    @FormUrlEncoded
    @POST("/get_food_data")
    fun getFoodData(
        @Field("foodName") name : String
    ): Call<List<FoodDataPOJO>>

}