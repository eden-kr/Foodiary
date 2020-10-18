package com.example.foodiary.Repository.Local

import android.content.Context
import android.util.Log
import com.example.foodiary.Session
import io.realm.Realm
import java.lang.NullPointerException
import kotlin.collections.ArrayList

class StatisticRepository {
    private val realm = Realm.getDefaultInstance()

    fun getWeekData(context: Context, week: ArrayList<String>): ArrayList<TotalDTO?> {
        var list = arrayListOf<TotalDTO?>()
        Session.getInstance(context)

        realm.beginTransaction()
        var day = realm.where(TotalDTO::class.java).equalTo("email", Session.getEmail())
            .equalTo("date", week[6]).findFirst()
        list.add(day)
        day = realm.where(TotalDTO::class.java).equalTo("email", Session.getEmail())
            .equalTo("date", week[5]).findFirst()
        list.add(day)
        day = realm.where(TotalDTO::class.java).equalTo("email", Session.getEmail())
            .equalTo("date", week[4]).findFirst()
        list.add(day)
        day = realm.where(TotalDTO::class.java).equalTo("email", Session.getEmail())
            .equalTo("date", week[3]).findFirst()
        list.add(day)
        day = realm.where(TotalDTO::class.java).equalTo("email", Session.getEmail())
            .equalTo("date", week[2]).findFirst()
        list.add(day)
        day = realm.where(TotalDTO::class.java).equalTo("email", Session.getEmail())
            .equalTo("date", week[1]).findFirst()
        list.add(day)
        day = realm.where(TotalDTO::class.java).equalTo("email", Session.getEmail())
            .equalTo("date", week[0]).findFirst()
        list.add(day)
        realm.commitTransaction()

        Log.d("myTag", "Total kcal list Size -> ${list.size}")
        Log.d("myTag", "Total kcal -> ${list[0]?.totalCal}")
        return list
    }

    fun getStepData(email: String, week: ArrayList<String>) : ArrayList<StepDTO>{
        val list = arrayListOf<StepDTO>()
        try {
            for (i in 0 until week.size) {
                var step = realm.where(StepDTO::class.java).equalTo("email", email)
                    .equalTo("date", week[i]).findFirst()
                if (step != null) {
                    list.add(step!!)
                }
            }
        } catch (e: NullPointerException) {
            throw e
        }
        return list
    }

    fun dispose() {
        realm.close()
    }

}