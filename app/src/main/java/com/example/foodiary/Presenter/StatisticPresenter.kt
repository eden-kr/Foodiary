package com.example.foodiary.Presenter

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.foodiary.Contract.StatisticContract
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.StatisticRepository
import com.example.foodiary.Repository.Local.StepDTO
import com.example.foodiary.Repository.Local.TotalDTO
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StatisticPresenter : StatisticContract.Presenter {
    private var view: StatisticContract.View? = null
    private val repo by lazy { StatisticRepository() }

    override fun attachView(view: StatisticContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        this.view = null
    }

    override fun getStepValue(email : String){
        val res = repo.getStepData(email,getXValue())
        Log.d("myTag","result size -> ${res.size}")
        val barEntry = arrayListOf<BarEntry>()
        val date = arrayListOf<String>()
        for(i in 0 until res.size){
            res[i].step?.toFloat()?.let { BarEntry(i.toFloat(), it) }?.let { barEntry.add(it) }
        }
        view?.setStepChart(barEntry)
    }

    override fun getXValue(): ArrayList<String> {
        val weekList = arrayListOf<String>()
        val xList = arrayListOf<String>()
        val today = Calendar.getInstance()
        val simpleFormat = SimpleDateFormat("MM-dd", Locale.KOREA)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

        for (i in 0 until 7) {
            xList.add(simpleFormat.format(today.time))
            weekList.add(sdf.format(today.time))
            today.add(Calendar.DATE, -1)
        }
        view?.setXValue(xList)
    //    view?.setChartX(xList)

        return weekList
    }

    override fun getData(context: Context, data: LineData){
        val totalData = repo.getWeekData(context, getXValue())
        var cnt = 0
            data?.let {
                var set : ILineDataSet? = data.getDataSetByIndex(0)
                if(set ==null){
                    set = createSet(context)
                    data.addDataSet(set)
                }
                totalData.forEach {
                    if(it?.totalCal != null) {
                        data.addEntry(Entry(set.entryCount.toFloat(), it.totalCal.toFloat()), 0)
                    }else{
                        data.addEntry(Entry(set.entryCount.toFloat(),0F),0)
                    }
                }

                data.notifyDataChanged()
                view?.setChart(data)
            }
    }

    override fun createSet(context: Context): LineDataSet {
        val set = LineDataSet(null, "Kcal")
        set.apply {
            axisDependency = YAxis.AxisDependency.LEFT
            setColor(ContextCompat.getColor(context,R.color.myPageEditText))
            setCircleColor(ContextCompat.getColor(context,R.color.myPageEditText)) //데이터 색
            valueTextSize = 10f
            lineWidth = 2f
            circleRadius = 4f       //원크기
            highLightColor = Color.BLACK
            fillColor = getColor(R.color.myPageEditText)      //라인 색
            setDrawValues(true)
        }
        return set
    }

    override fun dispose() {
        repo.dispose()
    }


}