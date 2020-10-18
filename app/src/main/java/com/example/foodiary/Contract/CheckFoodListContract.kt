package com.example.foodiary.Contract

import android.content.Context
import com.example.foodiary.Repository.Local.FoodDTO

class CheckFoodListContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun setAdapter(list : MutableList<FoodDTO>)
        fun setToolbar()
        fun setLottie()
    }
    interface Presenter{
        fun attachView(view : View)
        fun detachView()
        fun getData(context: Context,date : String)
        fun getCheckedData(str : String)
        fun deleteCheckData(str : String)
        fun deleteDataFromRealm(context: Context,date : String)
    }
}