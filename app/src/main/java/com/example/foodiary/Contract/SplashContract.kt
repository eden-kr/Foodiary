package com.example.foodiary.Contract

import android.content.Context
import com.example.foodiary.Session

class SplashContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun setLottie()
    }
    interface Presenter{
        fun attachView(view : View)
        fun detachView()
        fun checkLogin(context: Context) : Boolean
        fun load(context: Context)
    }
}