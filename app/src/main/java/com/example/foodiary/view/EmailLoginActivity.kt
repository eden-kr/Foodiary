package com.example.foodiary.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodiary.Contract.EmailActivityContract
import com.example.foodiary.Presenter.EmailActivityPresenter
import com.example.foodiary.R
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_email_login.*
import java.util.concurrent.TimeUnit

class EmailLoginActivity : AppCompatActivity(),EmailActivityContract.View {
    private lateinit var presenter : EmailActivityContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)
        presenter = EmailActivityPresenter()
        presenter.attachView(this)

        RxView.clicks(back_loginEmail)
            .subscribe { this.finish() }

        RxView.clicks(logIn_email)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                presenter?.logIn(getEmail(),getPassword(),this)
            }
        RxView.clicks(forget_pw)
            .throttleFirst(1,TimeUnit.SECONDS)
            .subscribe {
                startActivity(Intent(this,FindPasswordActivity::class.java))
            }
    }

    override fun setPresenter(presenter: EmailActivityContract.Presenter) {
        this.presenter = presenter
    }

    override fun getEmail(): String {
        return email.text.toString()
    }

    override fun getPassword(): String {
        return pw.text.toString()
    }
}
