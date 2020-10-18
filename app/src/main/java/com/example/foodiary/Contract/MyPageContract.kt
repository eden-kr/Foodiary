package com.example.foodiary.Contract

import android.app.Activity
import android.content.Context

class MyPageContract {
    interface View{
        fun setPresenter(presenter : Presenter)
        fun setUserData()
    }
    interface Presenter{
        fun logout(context: Context)
        fun attachView(view : View)
        fun detachView()
        fun loadActivity(context: Context,activity: Activity)
        fun sendMail(context: Context)
        fun isInstallCheck(context: Context,name : String) : Boolean
    }
}