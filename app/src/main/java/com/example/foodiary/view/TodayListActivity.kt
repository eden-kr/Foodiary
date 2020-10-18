package com.example.foodiary.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.fonts.FontFamily
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodiary.Adapter.TodayAdapter
import com.example.foodiary.Contract.TodayContract
import com.example.foodiary.Presenter.AppMainPresenter
import com.example.foodiary.Presenter.TodayMemoPresenter
import com.example.foodiary.Presenter.TodayPresenter
import com.example.foodiary.Presenter.TodayStepPresenter
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.FoodDTO
import com.example.foodiary.Repository.Local.UserDTO
import com.example.foodiary.fragment.MemoFragment
import com.example.foodiary.fragment.TodayMemoFragment
import com.example.foodiary.fragment.TodayStepFragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_food_data.*
import kotlinx.android.synthetic.main.activity_main_fragment.*
import kotlinx.android.synthetic.main.activity_today_list.*

class TodayListActivity : AppCompatActivity() ,TodayContract.View{
    private var presenter : TodayContract.Presenter?= null
    private var date : String? = ""
    private val REQUEST_CODE = 1000
    private val memoFrag = TodayMemoFragment()
    private val stepFrag = TodayStepFragment()
    private val manager = supportFragmentManager
    private val bundle = Bundle()
    override fun onRestart() {
        super.onRestart()
        presenter = TodayPresenter()            //presenter init
        presenter?.attachView(this)
        update()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_list)
        presenter = TodayPresenter()            //presenter init
        presenter?.attachView(this)
        add()
        replace()

        val intent = intent
        date = intent?.getStringExtra("date")
        bundle.putSerializable("date",date)

        presenter?.getData(this,date!!)
        presenter?.getPieEntry(this,date!!)

        back_today.setOnClickListener {
            this.finish()
        }
        list_revise.setOnClickListener {
            val intent = Intent(this,CheckFoodListActivity::class.java)
            intent.putExtra("date",date)
            startActivityForResult(intent,REQUEST_CODE)
        }
    }

    override fun setPresenter(presenter: TodayContract.Presenter) {
        this.presenter = presenter
    }

    override fun setAdapter(data: UserDTO, list: ArrayList<FoodDTO>) {
        today_recycle.layoutManager = LinearLayoutManager(this)
        today_recycle.adapter = TodayAdapter(this,data,list)
    }


    override fun setChart(list: ArrayList<PieEntry>) {
        Log.d("myTag","list size Piechart -> ${list.size}")
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.transparentCircleRadius = 10f
        pieChart.holeRadius = 65f
        pieChart.centerText = "식사 밸런스"
        pieChart.setCenterTextTypeface(ResourcesCompat.getFont(this, R.font.nanum_gothic))

        pieChart.animateY(1200,Easing.EaseInOutCubic)

        //set DataSet
        val set = PieDataSet(list,"")
        set.colors = ColorTemplate.createColors(ColorTemplate.JOYFUL_COLORS)

        pieChart.legend.formSize =13f

        val data = PieData(set)
        data.setValueTextSize(15f)
        set.sliceSpace = 2f
        set.selectionShift = 2f
        data.setValueTextColor(Color.WHITE)
        pieChart.data =  data
    }

    override fun update() {
        pieChart.invalidate()
    }

    override fun add() {
        manager.beginTransaction().add(R.id.today_fragment,memoFrag).commit()
        val mPresenter = TodayMemoPresenter()
        mPresenter.attachView(memoFrag)
    }

    override fun replace() {
        val tabListener = object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when(p0?.position){
                    0->{
                        today_fragment.visibility = View.GONE
                    }
                    1->{
                        today_fragment.visibility = View.VISIBLE
                        stepFrag.arguments = bundle
                        val mPresenter = TodayStepPresenter()
                        mPresenter.attachView(stepFrag)
                        presenter?.replace(manager,stepFrag)


                        /*
                        today_fragment.visibility = View.VISIBLE
                        memoFrag.arguments = bundle
                        val mPresenter = TodayMemoPresenter()
                        mPresenter.attachView(memoFrag)
                        presenter?.replace(manager,memoFrag)
                        */
                    }
                }
            }
        }
        today_tab.addOnTabSelectedListener(tabListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CODE){
                date = data?.getStringExtra("date")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.dispose()
        presenter?.detachView()
    }
}
