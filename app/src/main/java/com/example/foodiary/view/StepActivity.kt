package com.example.foodiary.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.foodiary.Contract.StepContract
import com.example.foodiary.Presenter.StepPresenter
import com.example.foodiary.R
import com.example.foodiary.fragment.AppMainFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_step.*

class StepActivity : AppCompatActivity(),StepContract.View {
    private lateinit var presenter : StepContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step)
        MobileAds.initialize(this)      //ads init
        setAds()

        presenter = StepPresenter()
        presenter.attachView(this)

        RxView.clicks(googlefit)
            .subscribe {
                presenter.setStepType(this,"googleFit")
                this.finish()
                startActivity(Intent(this,FoodiaryActivity::class.java))
            }
        RxView.clicks(foodiary)
            .subscribe{
                presenter.setStepType(this,"foodiary")
                this.finish()
                startActivity(Intent(this,FoodiaryActivity::class.java))
            }
        RxView.clicks(back_step)
            .subscribe {
                finish()
            }
    }

    override fun setPresenter(presenter: StepContract.Presenter) {
        this.presenter = presenter
    }

    override fun setAds() {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

}
