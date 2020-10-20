package com.example.foodiary.view

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.foodiary.Contract.FindPasswordContract
import com.example.foodiary.Presenter.FindPasswordPresenter
import com.example.foodiary.R
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_find_password.*
import java.util.concurrent.TimeUnit

class FindPasswordActivity : AppCompatActivity(),FindPasswordContract.View {
    private lateinit var presenter: FindPasswordContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)
        presenter = FindPasswordPresenter()
        presenter.attachView(this)

        RxView.clicks(send_email)
            .throttleFirst(1,TimeUnit.SECONDS)
            .subscribe {
                presenter.sendEmail(find_email.text.toString())
            }
        RxView.clicks(find_ok)
            .throttleFirst(1,TimeUnit.SECONDS)
            .subscribe {
                this.finish()
            }
        RxView.clicks(back_find_pw)
            .throttleFirst(1,TimeUnit.SECONDS)
            .subscribe { this.finish() }
    }

    override fun setPresenter(presenter: FindPasswordContract.Presenter) {
        this.presenter = presenter
    }

    override fun replacePage(email : String) {
        findPw1.visibility = View.GONE
        findPw2.visibility = View.VISIBLE
        email_find.text = email
        setLottie()
        find_ok.isClickable = true
        send_email.isClickable = false

    }

    override fun setLottie() {
        val animation = ValueAnimator.ofFloat(0f,1.0f).setDuration(2000)
        animation.addUpdateListener {
            email_lottie.progress = it.animatedValue as Float
        }

        animation.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
