package com.example.foodiary.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodiary.Contract.SignUpContract
import com.example.foodiary.Presenter.SignUpPresenter
import com.example.foodiary.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(),SignUpContract.View {
    val presenter by lazy { SignUpPresenter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        sign_ok.setOnClickListener {
            presenter.createUser()
        }
        check_duplicate.setOnClickListener {
            presenter.checkDuplicate()
        }
    }

    override fun getEmail(): String {
        return sign_email.text.toString()
    }

    override fun getPw(): String {
        return sign_pw.text.toString()
    }

    override fun getNickname(): String {
        return sign_nickname.text.toString()
    }

    override fun getContext(): Context {
        return this
    }

    override fun makeToast(msg: String) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }
}
