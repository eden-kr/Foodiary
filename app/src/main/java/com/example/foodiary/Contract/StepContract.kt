package com.example.foodiary.Contract

import android.content.Context

class StepContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun setAds()
    }
    interface Presenter{
        fun attachView(view: View)
        fun detachView()
        fun setStepType(context: Context,type: String)
    }
}