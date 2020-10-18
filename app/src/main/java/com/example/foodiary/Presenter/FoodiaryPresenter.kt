package com.example.foodiary.Presenter

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.foodiary.Contract.FoodiaryContract
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.LoginRepository
import com.example.foodiary.Session

class FoodiaryPresenter : FoodiaryContract.Presenter {
    val localRepo by lazy { LoginRepository() }

    override fun replace(fragmentManager: FragmentManager,view: Fragment) {
        val frag = fragmentManager.beginTransaction()
        frag.replace(R.id.main_frame,view)
        frag.commit()
    }

    override fun setSession(activity: Activity) {
        val intent = activity.intent
        val email : String? = intent.getStringExtra("email")
        val user = email?.let { localRepo.getUser(it) }
        if(email!=null) {
            Session.getInstance(activity)
            Session.setEmail(user?.email)
            Session.setNickname(user?.nickName)
        }else{
            Session.getInstance(activity)
            Log.d("myTag","main Foodiary Realm user check -> ${localRepo.checkUser(Session.getEmail()!!)}")
        }
    }

    override fun dispose() {
        localRepo.realmClose()
    }
}