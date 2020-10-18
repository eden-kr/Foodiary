package com.example.foodiary.Repository.Local

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

private val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())

open class UserDTO (
    @PrimaryKey
    var email : String = "",
    var nickName : String? = null,
    var weight : Float? = null,
    var photoUrl : String? = null,
    var height : Int? = null,
    var sex : String? = null,
    var maxcal : Int? = null,
    var stepType : String? = null
): RealmObject()

open class TotalDTO(
    var date : String = today,
    var email : String = "",
    var totalCal : Int = 0  //총 칼로리
):RealmObject()

open class FoodDTO(
    var date : String = today,
    var email : String? = null,
    var foodName : String? = null,
    var cal : Double? = 0.0,
    var protein : Double? = 0.0,
    var carbohydrate : Double? = 0.0,
    var fat : Double? = 0.0,
    var type : String? = null       //breakfast, lunch, dinner, snack
) : RealmObject()


