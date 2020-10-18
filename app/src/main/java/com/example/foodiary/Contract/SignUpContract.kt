package com.example.foodiary.Contract

import android.content.Context

class SignUpContract {
    interface View{
        fun getEmail() : String
        fun getPw() : String
        fun getNickname() : String
        fun getContext() : Context
        fun makeToast(msg : String)
    }
    interface Presenter{
        fun createUser()
        fun checkEmail(email : String) : Boolean
        fun checkPassword(pw : String) : Boolean
        fun checkDuplicate()
       fun dispose()
    }
}