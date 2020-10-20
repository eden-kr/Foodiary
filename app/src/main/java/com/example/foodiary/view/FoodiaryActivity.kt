package com.example.foodiary.view

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.foodiary.Contract.FoodiaryContract
import com.example.foodiary.Presenter.*
import com.example.foodiary.R
import com.example.foodiary.fragment.AppMainFragment
import com.example.foodiary.fragment.MemoFragment
import com.example.foodiary.fragment.MyPageFragment
import com.example.foodiary.fragment.StatisitcsFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_foodiary.*

class FoodiaryActivity : AppCompatActivity(),FoodiaryContract.View {
    private val memoView = MemoFragment()
    private val myPageView = MyPageFragment()
    private val statisticsView = StatisitcsFragment()
    private val mainView = AppMainFragment()

    val presenter by lazy { FoodiaryPresenter() }
    private val manager = supportFragmentManager
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        val intent = intent
        val type = intent?.getStringExtra("type")

        if(type !=null ){
            val mPresenter = MemoPresenter()
            mPresenter.attachView(memoView)
            presenter.replace(manager, memoView)
            tabBar.getTabAt(2)?.select()
            //tabBar.getTabAt(2)?.

        }else{
            val mPresenter = AppMainPresenter()
            mPresenter.attachView(mainView)
        }
    }

    override fun onRestart() {
        super.onRestart()
        val mPresenter = AppMainPresenter()
        mPresenter.attachView(mainView)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foodiary)

        clickNavBar()
        //init
        manager.beginTransaction().add(R.id.main_frame,mainView).commit()
        val mPresenter = AppMainPresenter()
        mPresenter.attachView(mainView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun clickNavBar() {
        val tabListener = object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTabUnselected(p0: TabLayout.Tab?) {
                p0?.icon?.setTint(getColor(R.color.default_color))
            }
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> {          //home
                        tab.icon?.setTint(getColor(R.color.myPageEditText))
                        val mPresenter = AppMainPresenter()
                        mPresenter.attachView(mainView)
                        presenter.replace(manager,mainView)
                    }
                    1 -> {          //
                        tab.icon?.setTint(getColor(R.color.myPageEditText))
                        val mPresenter = StatisticPresenter()
                        mPresenter.attachView(statisticsView)
                        presenter.replace(manager, statisticsView)
                    }
                    3-> {           //myPage
                        tab.icon?.setTint(getColor(R.color.myPageEditText))
                        val mPresenter = MyPagePresenter()
                        mPresenter.attachView(myPageView)
                        presenter.replace(manager,myPageView)
                    }
                    2->{            //memo
                        tab.icon?.setTint(getColor(R.color.myPageEditText))
                        val mPresenter = MemoPresenter()
                        mPresenter.attachView(memoView)
                        presenter.replace(manager, memoView)
                    }
                }
            }
        }
        tabBar.addOnTabSelectedListener(tabListener)
        tabBar.setTabTextColors(Color.parseColor("#2B2B2B"),Color.parseColor("#FFC107"))
        tabBar.getTabAt(0)?.icon?.setTint(getColor(R.color.myPageEditText))
    }

    override fun onBackPressed() {

    }

    override fun onStart() {
        super.onStart()
        presenter.setSession(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }


}
