package com.example.foodiary.Repository.Remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FoodDataPOJO (
    @SerializedName("foodName")
    var name : String,
    @SerializedName("serving_size")
    var servingSzie : String? = null,
    @SerializedName("kcal")
    var cal : String,
    @SerializedName("fat")
    var fat : String,       //지방
    @SerializedName("protein")
    var protein : String,   //단백질
    @SerializedName("carbohydrate")
    var carbo : String     //탄수화물
) : Serializable