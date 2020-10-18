package com.example.foodiary.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodiary.Contract.StatisticContract
import com.example.foodiary.R
import com.example.foodiary.Session
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_statistic_fragment.*
import java.security.KeyStore

class StatisitcsFragment : Fragment(),StatisticContract.View {
    private var presenter : StatisticContract.Presenter?=null
    private lateinit var search : LineChart
    private lateinit var step : BarChart
    override fun setPresenter(presenter: StatisticContract.Presenter) {
        this.presenter = presenter
    }

    override fun setChart(data: LineData) {
        search.apply{
            axisRight.isEnabled = false
            legend.apply {
                textSize = 10f
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
                description = null
                setDrawInside(false)
            }
        }
        search.data = data
        search.invalidate()
    }


    override fun setX() {
        val xAxis = search.xAxis
        xAxis.apply {
            textSize = 10f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f
            isGranularityEnabled = true
        }
    }

    override fun setY() {
    // y축 오른쪽 비활성화
       val yRAxis = search.axisRight
        yRAxis.setDrawLabels(false)
        yRAxis.setDrawAxisLine(false)
        yRAxis.setDrawGridLines(true)
        //y 그리드 활성화
        val yr = search.axisLeft
        yr.setDrawAxisLine(false)
        yr.setDrawGridLines(true)
    }

    override fun setXValue(list: ArrayList<String>) {
        val xAxis = search.xAxis
        xAxis.valueFormatter = object : ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                return if(list.size>value.toInt())
                    list[value.toInt()]
                else ""
            }
        }
    }

    override fun setMargin() {
        search.xAxis.yOffset = 15f
        search.axisLeft.xOffset = 15f
    }

    override fun setChartX(list: ArrayList<String>) {
        val xAxis = step.xAxis
        xAxis.valueFormatter = object : ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                return if(list.size>value.toInt())
                    list[value.toInt()]
                else ""
            }
        }
    }

    override fun setStepChart(list : ArrayList<BarEntry>) {
        val dataset = BarDataSet(list,"step count")
        dataset.axisDependency = YAxis.AxisDependency.LEFT
        dataset.colors = ColorTemplate.createColors(ColorTemplate.PASTEL_COLORS)
        val barData = BarData(dataset)
        step.data = barData
       // step.xAxis.yOffset = 13f
       // step.axisLeft.xOffset = 20f
        step.description.isEnabled = false
       // step.xAxis.position = XAxis.XAxisPosition.BOTTOM
        step.invalidate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_statistic_fragment,container,false)
        search = view.findViewById(R.id.chart)
        Session.getInstance(requireContext())
        val lineData = LineData()
        step = view.findViewById(R.id.step_chart)
        search.data = lineData
        presenter?.getData(requireContext(),search.data)
        presenter?.getStepValue(Session.getEmail()!!)

        setX()
        setY()
        setMargin()

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}