package com.example.foodiary.Repository.Local

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MemoDTO(
    @PrimaryKey
    var postNum : Int? = 0,
    var email : String? = null,
    var date : String? = null,
    var content : String? = null,
    var background : Int = 3
) : RealmObject()

data class MemoLocalDTO(
    var postNum : Int ,
    var email : String,
    var date : String,
    var content : String,
    var background : Int = 3
)