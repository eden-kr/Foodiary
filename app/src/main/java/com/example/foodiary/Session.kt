package com.example.foodiary

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object Session{
    private var pref : SharedPreferences? = null
    fun getInstance(context: Context) : SharedPreferences?{
        if(pref == null){
            pref = PreferenceManager.getDefaultSharedPreferences(context)
        }
        return pref
    }
    fun setEmail(email: String?){
        pref?.edit()?.putString("email",email)?.apply()
    }
    fun setNickname(nickName: String?){
        pref?.edit()?.putString("nickName",nickName)?.apply()
    }
    fun getEmail() : String?{
        return pref?.getString("email","")
    }
    fun getNickname() : String?{
        return pref?.getString("nickName","")
    }
}
