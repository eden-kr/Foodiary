package com.example.foodiary.Repository.Local

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

private val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())

open class StepDTO (
    @PrimaryKey
    var email : String= "",
    var date : String = today,
    var step : Int? = null,
    var distance : Float? = null,
    var consumeCal : Int? = null
    ) : RealmObject()


