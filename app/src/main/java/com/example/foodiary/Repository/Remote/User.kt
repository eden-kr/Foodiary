package com.example.foodiary.Repository.Remote

import java.io.Serializable

class User (
    var email : String?= null,
    var nickName : String? = null,
    var pw : String? = null,
    var photoUrl : String? = null
) :Serializable