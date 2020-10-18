package com.example.foodiary.Presenter

import android.content.Context
import android.util.Log
import com.example.foodiary.Contract.CheckFoodListContract
import com.example.foodiary.Repository.Local.CheckRepository
import com.example.foodiary.Repository.Local.FoodDTO
import com.example.foodiary.Session

class CheckFoodListPresenter : CheckFoodListContract.Presenter {
    private val checkedList = mutableListOf<String>()
    private var view : CheckFoodListContract.View? = null
    private val repo by lazy { CheckRepository() }
    override fun attachView(view: CheckFoodListContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        this.view =null
    }

    override fun getData(context: Context, date: String) {
        Session.getInstance(context)
        val list = repo.getSpecifiedData(Session.getEmail()!!,date)
        view?.setAdapter(list)

    }

    override fun getCheckedData(str : String) {
        checkedList.add(str)
        Log.d("myTag","checkedList size -> ${checkedList.size}")
    }

    override fun deleteCheckData(str: String) {
        checkedList.remove(str)
        Log.d("myTag","checkedList size -> ${checkedList.size}")
    }

    override fun deleteDataFromRealm(context: Context,date : String) {
        Session.getInstance(context)
        repo.deleteData(Session.getEmail()!!,date,checkedList)
    }
}