package com.example.foodiary.view

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodiary.Adapter.CheckListAdapter
import com.example.foodiary.Contract.CheckFoodListContract
import com.example.foodiary.Presenter.CheckFoodListPresenter
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.FoodDTO
import com.example.foodiary.etc.RecyclerDecoration
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_check_food_list.*

class CheckFoodListActivity : AppCompatActivity(),CheckFoodListContract.View{
    private lateinit var presenter: CheckFoodListContract.Presenter
    private var date : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_food_list)
        //presenter init
        presenter = CheckFoodListPresenter()
        presenter.attachView(this)

        setToolbar()

        //getIntent
        val intent = intent
        date  = intent.getStringExtra("date")
        //setAdapter
        if (date != null) {
            presenter.getData(this, date!!)
        }

        back_list.setOnClickListener {
            this.finish()
        }


    }

    override fun setPresenter(presenter: CheckFoodListContract.Presenter) {
        this.presenter = presenter
    }

    override fun setAdapter(list: MutableList<FoodDTO>) {
        val adapter = CheckListAdapter(list,presenter)
        foodList_recyclerView.adapter = adapter

        foodList_recyclerView.addItemDecoration(RecyclerDecoration(20))  //set interval
        val dividerItemDecoration = DividerItemDecoration(foodList_recyclerView.context, LinearLayoutManager(this).orientation)
        foodList_recyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun setToolbar() {
        setSupportActionBar(toolbar_checkList)
        title = ""
    }

    override fun setLottie() {
        check_lottie.visibility = View.VISIBLE
        val animator = ValueAnimator.ofFloat(0f,1.0f).setDuration(2000)
        animator.addUpdateListener {
            check_lottie.progress = it.animatedValue as Float
        }
        animator.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val handler = Handler()
        when(item.itemId){
            R.id.complete ->{
                item.isVisible = false
                setLottie()
                date?.let { presenter.deleteDataFromRealm(this, it) }
                val intent = Intent(this,TodayListActivity::class.java)
                intent.putExtra("date",date)
                setResult(Activity.RESULT_OK,intent)
                handler.postDelayed({
                    this.finish()
                },1000)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.check_list_menu,menu)
        return true
    }
}
