package com.example.foodiary.Repository.Local

import android.util.Log
import com.example.foodiary.Session
import io.realm.Realm
import java.lang.Exception

class MyPageRepository {
    private val realm = Realm.getDefaultInstance()
    //유저 정보 설정
    fun setUserInfo(sex: String, height: Int, weight: Float, maxCal: Int) {
        realm.executeTransactionAsync({ realm ->
            val email = Session.getEmail()      //email now
            val user = realm.where(UserDTO::class.java).equalTo("email", email).findFirst()
            user?.sex = sex
            user?.height = height
            user?.weight = weight
            user?.maxcal = maxCal
        }, {
            Log.d("myTag", "insert user data Successful!")
            realm.close()
        }, {
            Log.d("myTag", "insert User Data error ! (height,weight ...) ${it.printStackTrace()}")
            realm.close()
        })
    }

    //유저 이미지 설정
    fun setUserImage(url: String?){
        val email = Session.getEmail()      //email now
        realm.beginTransaction()
        try{
        val user = realm.where(UserDTO::class.java).equalTo("email", email).findFirst()
        user?.photoUrl = url
        realm.commitTransaction()
        }catch (e : Throwable){
            if(realm.isInTransaction) realm.cancelTransaction()
        }
    }
    fun getUserImage() : String? {
        var url : UserDTO? = null
        realm.beginTransaction()
        try {
            url = realm.where(UserDTO::class.java).equalTo("email", Session.getEmail()).findFirst()
            realm.commitTransaction()
        }catch(e : Throwable){
            if(realm.isInTransaction) realm.cancelTransaction()
        }

        return url?.photoUrl
    }
    fun dispose(){
        realm.close()
    }

}