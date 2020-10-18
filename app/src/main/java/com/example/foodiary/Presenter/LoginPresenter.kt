package com.example.foodiary.Presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.foodiary.Contract.LoginContract
import com.example.foodiary.Repository.Remote.User
import com.example.foodiary.Session
import com.example.foodiary.view.FoodiaryActivity

class LoginPresenter(val view : LoginContract.View): LoginContract.Presenter {
    private val localRepo by lazy { com.example.foodiary.Repository.Local.LoginRepository() }

    override fun setLoginSession(context: Context, user : User?) {
        /*
        Session.getInstance(context)
        Session.setNickname(user?.nickName!!)
        Session.setEmail(user?.email!!)

         */
    }

    override fun signUp(context: Context) {
        val activity = context as Activity
        var intent = activity.intent
        val bundle = intent.getBundleExtra("bundle")
        val user = bundle?.getSerializable("user") as? User
        user?.nickName = view.getNickname()
       // setLoginSession(context,user)
        localRepo.insertUser(user?.email!!, user.nickName!!)      //realm
        intent = Intent(context,FoodiaryActivity::class.java)
        intent.putExtra("email",user?.email)
        context.startActivity(intent)
        }


    override fun dispose() {
       localRepo.realmClose()
    }
}