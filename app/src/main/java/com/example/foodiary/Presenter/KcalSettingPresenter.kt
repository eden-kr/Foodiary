package com.example.foodiary.Presenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.foodiary.Contract.KcalSettingContract
import com.example.foodiary.Repository.Remote.FoodDataPOJO

class KcalSettingPresenter : KcalSettingContract.Presenter {
    private var view: KcalSettingContract.View? = null
    private var fatCoef: Double = 0.0
    private var proteinCoef = 0.0
    private var kcalCoef = 0.0
    private var carboCoef = 0.0

    override fun getCoef(foodData: FoodDataPOJO) {
        val serving = foodData.servingSzie?.toDouble()

        fatCoef = foodData.fat.toDouble() / serving!!
        proteinCoef = foodData.protein.toDouble() / serving
        kcalCoef = foodData.cal.toDouble() / serving
        carboCoef = foodData.carbo.toDouble() / serving
        setGram()
    }

    override fun setGram() {
        val gram = view?.getGram()
        view?.setKcal((gram!! * kcalCoef).toInt())
        view?.setFat(String.format("%.2f",gram!! * fatCoef))
        view?.setProtein(String.format("%.2f",gram!! * proteinCoef))
        view?.setCarbohydrate(String.format("%.2f",gram!! * carboCoef))

    }

    override fun attachView(view: KcalSettingContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        view = null
    }

    override fun returnToFoodData(foodData: FoodDataPOJO): FoodDataPOJO {
        val gram = view?.getGram()
        foodData.cal = (gram!! * kcalCoef).toString()
        foodData.fat = (gram!! * fatCoef).toString()
        foodData.servingSzie = gram.toString()!!
        foodData.protein = (gram!! * proteinCoef).toString()
        foodData.carbo = (gram!! * carboCoef).toString()

        return foodData
    }


    override fun setBroadCast(context: Context, data: FoodDataPOJO, count: Int) {
        //broadcast Send
        val intent = Intent("com.example.foodiary.action.DATA_PASS")
        //intent.`package`= "com.example.foodiary.view.FoodDataActivity"
        intent.putExtra("data",data)    //보내줄 데이터
        intent.putExtra("count",count)  //숫자를 증가시킬 카운트
        context.sendBroadcast(intent)
        Log.d("myTag","broadcast send")
    }


}

