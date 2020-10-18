package com.example.foodiary.view

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.foodiary.Contract.SplashContract
import com.example.foodiary.Presenter.SplashPresenter
import com.example.foodiary.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() ,SplashContract.View{
    private var presenter : SplashContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        presenter = SplashPresenter()
        presenter?.attachView(this)
        setLottie()
        val handler = Handler()
        handler.postDelayed({
            presenter?.load(this)
                    },5000)
    }

    override fun onBackPressed() {
    }


    override fun setPresenter(presenter: SplashContract.Presenter) {
        this.presenter = presenter
    }

    override fun setLottie() {
        val animator = ValueAnimator.ofFloat(0f, 1.0f).setDuration(2000)
        animator.addUpdateListener {
            lottie_main.progress = it.animatedValue as Float
        }
        animator.start()

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }
}
