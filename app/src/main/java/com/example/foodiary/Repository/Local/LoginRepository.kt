package com.example.foodiary.Repository.Local

import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration


//import io.realm.kotlin.createObject

class LoginRepository {
    private val realm = Realm.getDefaultInstance()
    fun getUserInfo(email: String) : UserDTO? {
        return realm.where(UserDTO::class.java).equalTo("email",email).findFirst()
    }
    //유저 정보 생성
    fun insertUser(email : String, nickname : String) {
        realm.executeTransactionAsync ({ realm ->
            val check = realm.where(UserDTO::class.java).equalTo("email", email).findFirst()
            if (check == null) {
                val user = UserDTO()
                user.email = email
                user.nickName = nickname
                realm.copyToRealm(user)
                Log.d("myTag","Realm :: Success SignUp!")
            }
        },{
            Log.d("myTag","Realm :: Success SignUp!")
        },{ error ->
            Log.d("myTag","Realm :: signUp Failed\n ${error.printStackTrace()}")
        })
    }
    //중복확인
    fun checkUser(email : String) : Boolean{
        val res = realm.where(UserDTO::class.java).equalTo("email",email).findFirst()
        return res==null
    }
    //유저정보 가져오기
    fun getUser(email : String) : UserDTO?{
        var user : UserDTO? = null
        realm.executeTransaction { realm ->
            user = realm.where(UserDTO::class.java).equalTo("email",email).findFirst()
        }
        return user
    }
    fun deleteUser(email : String) {
        realm.executeTransactionAsync ({ realm ->
            realm.where(UserDTO::class.java).equalTo("email", email).findFirst()?.deleteFromRealm()
        },{
            Log.d("myTag","delete is Successful")
            realm.close()
        },{
            Log.d("myTag","delete is failed -> ${it.printStackTrace()}")
            realm.close()
        })
    }
    fun realmClose(){
        realm.close()
    }

}


