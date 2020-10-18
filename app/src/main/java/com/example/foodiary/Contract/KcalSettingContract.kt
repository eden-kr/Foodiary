package com.example.foodiary.Contract

import android.content.Context
import com.example.foodiary.Repository.Remote.FoodDataPOJO

class KcalSettingContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun getGram() : Int
        fun setCarbohydrate(num : String)
        fun setProtein(num : String)
        fun setFat(num : String)
        fun setKcal(num: Int)
        fun setFoodName(name : String)
        fun init(foodData : FoodDataPOJO)
    }
    interface Presenter{
        fun getCoef(foodData : FoodDataPOJO)
        fun setGram()
        fun attachView(view : View)
        fun detachView()
        fun returnToFoodData(foodData: FoodDataPOJO): FoodDataPOJO
        fun setBroadCast(context : Context, data : FoodDataPOJO, count : Int)
    }
}