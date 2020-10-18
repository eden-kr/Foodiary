package com.example.foodiary.Repository.Local

import java.io.Serializable

data class FoodLocalDTO(
    var date: String = "",
    var email: String? = null,
    var foodName: String? = null,
    var cal: Double? = 0.0,
    var protein: Double? = 0.0,
    var carbohydrate: Double? = 0.0,
    var fat: Double? = 0.0,
    var type: String? = null       //breakfast, lunch, dinner, snack
) : Serializable