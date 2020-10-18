package com.example.foodiary.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.foodiary.Contract.KcalSettingContract
import com.example.foodiary.Presenter.KcalSettingPresenter
import com.example.foodiary.R
import com.example.foodiary.Repository.Remote.FoodDataPOJO
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_kcal_setting.*
import kotlin.math.roundToInt

class KcalSettingActivity : AppCompatActivity(),KcalSettingContract.View {
    private lateinit var presenter: KcalSettingContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kcal_setting)
        presenter = KcalSettingPresenter()
        presenter.attachView(this)

        val intent = intent
        val bundle = intent.getBundleExtra("bundle") ?: null
        val foodData = bundle?.getSerializable("item") as? FoodDataPOJO
        foodData?.name?.let { setFoodName(it) }

        if (foodData != null) {
            init(foodData)
        }

        gram_kcal.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (foodData != null && !gram_kcal.text.isNullOrBlank()) {
                    presenter.getCoef(foodData)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        RxView.clicks(add_kcal)
            .subscribe {
                var data : FoodDataPOJO?
                data = if(!gram_kcal.text.isNullOrBlank()){
                    foodData?.let { it1 -> presenter.returnToFoodData(it1) }
                }else{
                    foodData
                }
                if (data != null) {
                    presenter.setBroadCast(this,data,1)
                }
            }

        RxView.clicks(back_kcal)
            .subscribe { finish() }

    }


    override fun setPresenter(presenter: KcalSettingContract.Presenter) {
        this.presenter = presenter
    }

    override fun getGram() : Int{
        return gram_kcal.text.toString().toInt()
    }

    override fun setCarbohydrate(num: String) {
        carbo_kcal.text = "탄수화물\n$num g"
    }

    override fun setProtein(num: String) {
        protein_kcal.text = "단백질\n$num g"
    }

    override fun setFat(num: String) {
        fat_kcal.text = "지방\n$num g"
    }

    override fun setKcal(num: Int) {
        kcal_kcal.text = "${num} kcal"
    }

    override fun setFoodName(name: String) {
        foodName_kcal.text = name
    }

    override fun init(foodData : FoodDataPOJO) {
        setKcal(foodData.cal.toDouble().roundToInt())
        setFat(foodData.fat)
        setProtein(foodData.protein)
        setCarbohydrate(foodData.carbo)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}
