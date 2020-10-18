package com.example.foodiary.Repository.Local

import com.skt.Tmap.TMapPoint
import io.realm.RealmCollection
import io.realm.RealmList
import io.realm.RealmObject
import java.text.SimpleDateFormat
import java.util.*

private val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())

open class GeoDTO(
    var email : String? = null,
    var date : String? = today,
    var point : RealmList<MyPoint>? = null
) : RealmObject()

open class MyPoint(
    var date : String = today,
    var lat : Double? = null,
    var lng :  Double? = null
) : RealmObject()

