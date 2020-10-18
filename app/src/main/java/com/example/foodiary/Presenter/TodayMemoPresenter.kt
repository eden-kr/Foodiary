package com.example.foodiary.Presenter

import com.example.foodiary.Contract.TodayMemoContarct

class TodayMemoPresenter : TodayMemoContarct.Presenter{
    private var view : TodayMemoContarct.View? = null
    override fun attachView(view: TodayMemoContarct.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        view = null
    }
}