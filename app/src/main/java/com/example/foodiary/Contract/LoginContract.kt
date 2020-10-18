package com.example.foodiary.Contract

import android.content.Context
import com.example.foodiary.Repository.Remote.User

class LoginContract {
    interface View{
        fun getNickname() : String
        fun setAnim()
    }
    interface Presenter{
        fun setLoginSession(context: Context,user : User?)
        fun signUp(context: Context)
        fun dispose()
    }
}