package com.example.foodiary.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodiary.Contract.WithdrawContract
import com.example.foodiary.Presenter.WithdrawPresenter
import com.example.foodiary.R
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_withdraw.*

class WithdrawActivity : AppCompatActivity() ,WithdrawContract.View{
    private var presenter : WithdrawContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw)

        presenter = WithdrawPresenter()
        presenter?.attachView(this)

        RxView.clicks(withdraw_close)
            .subscribe {
                onBackPressed()
            }

        RxView.clicks(withdraw_ok)
            .subscribe {
                presenter?.deleteUser(this)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.dispose()
        presenter?.detachView()

    }
}
