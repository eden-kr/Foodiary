package com.example.foodiary.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.example.foodiary.Contract.LoginContract
import com.example.foodiary.Presenter.LoginPresenter
import com.example.foodiary.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() , LoginContract.View {
    val presenter by lazy {LoginPresenter(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signUp_ok.setOnClickListener {
            presenter.signUp(this)
        }
    }

    override fun getNickname(): String {
        return input_nickname.text.toString()
    }

    override fun setAnim() {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }
}
