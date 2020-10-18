package com.example.foodiary.Presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.foodiary.Contract.KcalSettingContract
import com.example.foodiary.Contract.SplashContract
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.LoginRepository
import com.example.foodiary.Session
import com.example.foodiary.view.FoodiaryActivity
import com.example.foodiary.view.MainActivity
import io.reactivex.disposables.Disposable
import java.util.*

class SplashPresenter: SplashContract.Presenter {
    private var view : SplashContract.View? = null
    private val repo by lazy { LoginRepository() }

    override fun attachView(view : SplashContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        this.view = null
    }

    override fun checkLogin(context: Context) :Boolean {
        var check  = false
        Session.getInstance(context)
        Log.d("myTag","Splash Activity : Session ${Session.getEmail()}")
        if(Session.getEmail()!=null){
            if(!repo.checkUser(Session.getEmail()!!))
             check = true
        }
        return check
    }

    override fun load(context: Context) {
        var intent : Intent
        val activity = context as Activity
        if(checkLogin(context)){
            intent = Intent(activity, FoodiaryActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }else{
            intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }
    }
}