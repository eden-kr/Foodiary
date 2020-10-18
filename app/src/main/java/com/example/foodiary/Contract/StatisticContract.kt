package com.example.foodiary.Contract

import android.content.Context
import com.example.foodiary.Repository.Local.StepDTO
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class StatisticContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun setChart(data : LineData)
        fun setX()
        fun setY()
        fun setXValue(list  : ArrayList<String>)
        fun setMargin()
        fun setChartX(list: ArrayList<String>)
        fun setStepChart(list : ArrayList<BarEntry>)
    }
    interface Presenter{
        fun attachView(view :  View)
        fun detachView()
        fun getStepValue(email : String)
        fun getXValue() : ArrayList<String>
        fun getData(context: Context,data: LineData)
        fun createSet(context: Context) : LineDataSet
        fun dispose()
    }
}