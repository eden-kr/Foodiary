package com.example.foodiary.Contract

import android.content.Context
import com.example.foodiary.Repository.Remote.User

class MainContract  {
    interface View{
        fun getKaKaoLogin() : String
        fun getGoogleLogin() : String
        fun getEmail() : String
        fun getPassword() : String
    }
    interface Presenter{
        fun clickLogin(email : String, pw : String,context: Context)
        fun forgetPassword(context: Context)        //미구현
        fun emailLogin(context: Context)
        fun socialLogin(type : String, context : Context)
        fun startLogin(context: Context,user: User)
        fun setLoginSession(context: Context,email: String)
        fun dispose()
        fun logout()
    }
}