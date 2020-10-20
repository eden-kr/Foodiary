package com.example.foodiary.Contract

import android.content.Context
import com.example.foodiary.Repository.Remote.User

class MainContract  {
    interface View{
        fun getKaKaoLogin() : String
        fun getGoogleLogin() : String

    }
    interface Presenter{

        fun emailLogin(context: Context)
        fun socialLogin(type : String, context : Context)
        fun startLogin(context: Context,user: User)
        fun setLoginSession(context: Context,email: String)
        fun dispose()
        fun logout()
    }
}