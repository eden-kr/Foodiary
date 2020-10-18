package com.example.foodiary.Contract

import android.content.Context

class WithdrawContract {
    interface View{

    }
    interface Presenter{
        fun deleteUser(context: Context)
        fun dispose()
        fun attachView(view : View)
        fun detachView()
    }
}