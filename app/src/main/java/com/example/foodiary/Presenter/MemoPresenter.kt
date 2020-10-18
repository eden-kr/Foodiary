package com.example.foodiary.Presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.foodiary.Contract.MemoContract
import com.example.foodiary.Repository.Local.FoodDTO
import com.example.foodiary.Repository.Local.MemoRepository
import com.example.foodiary.Repository.Remote.FoodDataPOJO
import com.example.foodiary.Session
import com.prolificinteractive.materialcalendarview.CalendarDay
import io.realm.RealmResults
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class MemoPresenter : MemoContract.Presenter {
    private var view : MemoContract.View? = null
    private val repo = MemoRepository()
    private val userData = repo.getUserData()

    override fun attachView(view : MemoContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        this.view = null
        repo.realmClose()
    }

    override fun dispose() {
        repo.realmClose()
    }

    override fun calculateCal(type : String) : String {
        val stdWeight = (userData?.height?.minus(100))?.times(0.9)      //표준체중
        val todayCal = stdWeight?.times(30)     //표준체중 * 활동지수
        var cal  = ""
        when(type){
            "아침" ->{
                if (todayCal != null) {
                    cal = "권장 : ${abs((todayCal/5)).toInt()} cal"
                }
            }
            "점심" ->{
                if (todayCal != null) {
                    cal = "권장 : ${abs((todayCal/5)*2).toInt()} cal"
                }
            }
            "저녁" ->{
                if (todayCal != null) {
                    cal = "권장 : ${abs((todayCal/5)*2).toInt()} cal"
                }
            }
        }
        return cal
    }

    override fun getFoodCal(data : Intent) {

    }

    override fun calculateDate(type : Int, today: Calendar) : Calendar  {
        var date = today
        if(type ==1 ){      //right button is clicked
            date.add(Calendar.DATE,1)
        }else if(type==2){              //left button is clicked
            date.add(Calendar.DATE,-1)
        }
        return date
    }

    override fun startNewActivity(context: Context, activity: Activity) {
        val intent = Intent(context , activity::class.java)
        context.startActivity(intent)
    }


    override fun getTotal(date : String): ArrayList<FoodDTO>? {
        val list = arrayListOf(FoodDTO(), FoodDTO(),
            FoodDTO(),FoodDTO())
        val res = repo.getCalData(date)
        Log.d("myTag","${res.size}")

        if(res.size>0){
            res.forEach {
                when(it?.type){
                    "breakfast" ->{
                        list[0].cal = list[0].cal?.plus(it?.cal!!)
                        list[0].type = "breakfast"
                    }
                    "lunch" ->{
                        list[1].cal = list[1].cal?.plus(it?.cal!!)
                        list[1].type = "lunch"
                    }
                    "dinner" ->{
                        list[2].cal = list[2].cal?.plus(it?.cal!!)
                        list[2].type = "dinner"
                    }
                    else -> {
                        list[3].cal = list[3].cal?.plus(it?.cal!!)
                        list[3].type = "snack"
                    }
                }
            }
        }

        return list
    }

    override fun updateTotalKcal(context: Context) {
        repo.insertToTotalKcal(context)
    }

    override fun stringToDate(context: Context): MutableList<CalendarDay?> {
     //   var calendar = Calendar.getInstance()
        val list : MutableList<CalendarDay?> = arrayListOf()
        Session.getInstance(context)
        Log.d("myTag","stringToDate.Id -> ${Session.getEmail()}")
        val data : RealmResults<FoodDTO>? = repo.getFoodData(Session.getEmail()!!)
        if(data?.size!! >0){
            data.forEach {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
             //   var date = SimpleDateFormat("yyyy-MM-dd").parse(it.date)
             //   calendar.time = date
           //     LocalDate.parse(it.date)
                Log.d("myTag","sdf date? -> ${sdf.parse(it.date)}")
                list.add(CalendarDay.from(sdf.parse(it.date)))
            }
        }
        return list
    }

}