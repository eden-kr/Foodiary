package com.example.foodiary.Contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.foodiary.Repository.Local.FoodDTO
import com.example.foodiary.Repository.Remote.FoodDataPOJO
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

class MemoContract {
    interface View{
        fun setPresenter(presenter : Presenter)
        fun setDate(date : Calendar) : String
        fun setFoodCal(cal : Int, type : String, view : android.view.View)
        fun setRecommendCal(morning : TextView, lunch : TextView, dinner : TextView)
        fun setChangeImage(image : ImageView)
        fun setView(view : android.view.View)
        fun changeDataAfterInput(type: String, view : android.view.View)
        fun getKcalFromView(case : Int) : String
        fun initView(view : android.view.View)
        fun setAnim(case: Int, view : LinearLayout)
        fun setCalendar()
    }
    interface Presenter{
        fun attachView(view : View)
        fun detachView()
        fun dispose()
        fun calculateCal(type : String) : String
        fun getFoodCal(data : Intent)
        fun calculateDate(type : Int, today: Calendar) : Calendar
        fun startNewActivity(context: Context, activity: Activity)
        fun getTotal(date : String) : ArrayList<FoodDTO>?
        fun updateTotalKcal(context: Context)
        fun stringToDate(context: Context) : MutableList<CalendarDay?>
     //   fun kcalToInt(kcal : String): Int
    }
}