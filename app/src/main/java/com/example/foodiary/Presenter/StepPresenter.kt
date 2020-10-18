package com.example.foodiary.Presenter

import android.content.Context
import android.util.Log
import com.example.foodiary.Contract.StepContract
import com.example.foodiary.Repository.Local.AppMainRepository
import com.example.foodiary.Session

class StepPresenter : StepContract.Presenter {
    private var view : StepContract.View? = null
    private val repo by lazy { AppMainRepository() }
    override fun attachView(view: StepContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        this.view = null
    }

    override fun setStepType(context: Context,type : String) {
        repo.setStepType(context,type)
    }

}