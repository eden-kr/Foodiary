package com.example.foodiary.Presenter

import com.example.foodiary.Contract.MemoWriteContract

class MemoWritePresenter : MemoWriteContract.Presenter {
    private var view : MemoWriteContract.View? = null
    override fun attachView(view: MemoWriteContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        this.view = null
    }
}