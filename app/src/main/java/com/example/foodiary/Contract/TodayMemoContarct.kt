package com.example.foodiary.Contract

class TodayMemoContarct {
    interface View{
        fun setPresenter(presenter: Presenter)
    }
    interface Presenter{
        fun attachView(view : View)
        fun detachView()
    }
}