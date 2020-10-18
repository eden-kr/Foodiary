package com.example.foodiary.view

import android.Manifest
import android.animation.ValueAnimator
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiary.Adapter.MyListAdapter
import com.example.foodiary.Contract.FoodDataContract
import com.example.foodiary.Presenter.FoodDataPresenter
import com.example.foodiary.R
import com.example.foodiary.Receiver.FoodDataReceiver
import com.example.foodiary.Repository.Remote.FoodDataPOJO
import com.example.foodiary.etc.RecyclerDecoration
import com.example.foodiary.fragment.FoodDataFragment
import com.example.foodiary.myList
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_check_food_list.*
import kotlinx.android.synthetic.main.activity_food_data.*


class FoodDataActivity : AppCompatActivity(), FoodDataContract.View {
    private lateinit var presenter: FoodDataContract.Presenter
    private var flag = true
    private val REQUEST_READ_EXTERNAL_STORAGE = 10000
    private val fragmentManager = supportFragmentManager
    private val emptyList = arrayListOf<FoodDataPOJO>()
    private val foodFrag = FoodDataFragment.newInstance(emptyList)
    private lateinit var adapter: MyListAdapter
    private lateinit var recyclerView: RecyclerView
    private val receiver: BroadcastReceiver = FoodDataReceiver()
    private lateinit var sp: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor

    override fun onStart() {
        super.onStart()
        presenter.setBroadCastReceiver(this, receiver)

    }

    override fun onResume() {
        super.onResume()
        sp = getPreferences(Context.MODE_PRIVATE)
        setResumeType()

        val intent = intent
        updateRecyclerView(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_data)
        sp = getPreferences(Context.MODE_PRIVATE)
        edit = sp.edit()
        adapter = MyListAdapter(this@FoodDataActivity, list_foodData)

        setToolbar()
        initFragment()      //attach fragment
        initCount()         //count init -> 0
        setAdapter()

        presenter = FoodDataPresenter()     //attach init
        presenter.attachView(this)

        val intent = intent
        val type: String? = intent?.getStringExtra("type")

        if (type != null) setType(type)
        presenter.saveType(edit)

        //clickListener
        RxView.clicks(selectImage_data)
            .subscribe { presenter.getSearchImage(this) }

        RxView.clicks(list_foodData)
            .subscribe {
                setAnim(1)
            }
        RxView.clicks(close_drawer)
            .subscribe {
                setAnim(2)
            }

        //TextWatcher

        search_data.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.isNotBlank()!! && !presenter.checkWords(p0.toString())) {
                    presenter.getFoodData(p0.toString())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        search_data.setOnClickListener {
            search_data.isFocusable = true
        }
        search_data.setOnFocusChangeListener { view, b ->
            if(view.hasFocus()){
                moveToolbar()
            }else{
                restoreToolbar()
            }

        }
        //steal Edittext focus1
        layout1.setOnClickListener {
            search_data.clearFocus()
        }

        //steal Edittext focus2
        cancel.setOnClickListener {
            search_data.clearFocus()
            search_data.text.clear()
        }


    }

    override fun setPresenter(presenter: FoodDataContract.Presenter) {
        this.presenter = presenter
    }

    override fun getSearchTopic(): String {
        return search_data.text.toString()
    }

    override fun makeDialog() {
        val dlg = AlertDialog.Builder(this@FoodDataActivity)
        dlg.setTitle("권한 설정")
            .setCancelable(true)
            .setPositiveButton("확인") { p0, p1 ->
                //권한 요청
                ActivityCompat.requestPermissions(
                    this@FoodDataActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE
                )
            }.setNegativeButton("취소") { p0, p1 ->
            }.show()
    }

    override fun initFragment() {
        val frag = fragmentManager.beginTransaction()
        frag.add(R.id.memo_frameLayout, foodFrag)
        frag.commit()
    }

    override fun returnFragmentManager(): FragmentManager {
        return fragmentManager
    }

    override fun setType(type: String) {
        foodData_type.text = type
    }

    override fun setCount() {
        list_foodData.text = (myList.size + 1).toString()
    }

    override fun getCount(): Int {
        return list_foodData.text.toString().toInt()
    }

    override fun initCount() {
        list_foodData.text = "0"
    }

    override fun setAdapter() {
        recyclerView = findViewById(R.id.data_drawer)
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(RecyclerDecoration(50))  //set interval
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, LinearLayoutManager(this).orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

    }

    override fun setAnim(case: Int) {
        var anim: Animation
        when (case) {
            1 -> {
                anim = AnimationUtils.loadAnimation(
                    this,
                    R.anim.up
                )
                drawer.animation = anim
                drawer.visibility = View.VISIBLE
            }
            2 -> {
                anim = AnimationUtils.loadAnimation(
                    this,
                    R.anim.down
                )
                drawer.animation = anim
                drawer.visibility = View.INVISIBLE

            }
        }
    }

    override fun setToolbar() {
        setSupportActionBar(foodData_toolbar)      //attach toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun setResumeType() {
        if (foodData_type.text.isNullOrBlank()) {
            sp.getString("type", "")?.let { setType(it) }
        }
    }

    override fun getType(): String {
        return foodData_type.text.toString()
    }

    override fun updateRecyclerView(intent: Intent) {
        //get BroadCast
        val data = intent?.getSerializableExtra("data") as? FoodDataPOJO

        if (data != null) {
            setCount()
            adapter.onUpdateItem(data)
            recyclerView.invalidate()
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun setLottie() {
        complete_food_data.bringToFront()
        complete_food_data.visibility = View.VISIBLE
        val animator = ValueAnimator.ofFloat(0f, 1.0f).setDuration(2000)
        animator.addUpdateListener {
            complete_food_data.progress = it.animatedValue as Float
        }
        animator.start()
    }

    override fun moveToolbar() {
        supportActionBar?.hide()
        cancel.visibility = View.VISIBLE
        cancel.isClickable = true
    }

    override fun restoreToolbar() {
        supportActionBar?.show()
        cancel.visibility = View.INVISIBLE
        cancel.isClickable = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { presenter.uriToFile(this, it) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_food_data, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val list = adapter.returnAllItems()
        val handler = Handler()
        when (item.itemId) {
            R.id.submit -> {
                item.isVisible = false
                setLottie()

                val intent = Intent(this, FoodiaryActivity::class.java)
                return if (presenter.checkListSize(list) && !foodData_type.text.isNullOrEmpty()) {
                    intent.putExtra("list", list)        //data
                    intent.putExtra("type", foodData_type.text)      //type
                    handler.postDelayed({
                        this@FoodDataActivity.finish()
                        startActivity(intent)
                    }, 1000)

                    presenter.saveFoodData(this, foodData_type.text.toString())
                    true
                } else {
                    handler.postDelayed({
                        onBackPressed()
                    }, 1000)
                    true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
        unregisterReceiver(receiver)
    }

}
