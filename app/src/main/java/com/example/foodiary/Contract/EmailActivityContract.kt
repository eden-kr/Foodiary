package com.example.foodiary.Contract

import android.content.Context

class EmailActivityContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun getEmail() : String
        fun getPassword() : String
    }
    interface Presenter{
        fun attachView(view : View)
        fun detachView()
        fun logIn(email : String, pw : String,context: Context)
        fun setSession(context: Context)
    }
}