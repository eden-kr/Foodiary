package com.example.foodiary.Presenter

import android.content.Context
import android.content.Intent
import com.example.foodiary.Contract.WithdrawContract
import com.example.foodiary.Repository.Local.LoginRepository
import com.example.foodiary.Session
import com.example.foodiary.view.MainActivity

class WithdrawPresenter : WithdrawContract.Presenter {
    private var view : WithdrawContract.View? = null
    private val repo by lazy{ LoginRepository()}

    override fun deleteUser(context: Context) {
        val email = Session.getEmail()
        if (email != null) {
            repo.deleteUser(email)
            val intent = Intent(context,MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun dispose() {
        repo.realmClose()
    }

    override fun attachView(view: WithdrawContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }
}