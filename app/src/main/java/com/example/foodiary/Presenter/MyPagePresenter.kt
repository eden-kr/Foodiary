package com.example.foodiary.Presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.foodiary.Contract.MainContract
import com.example.foodiary.Contract.MyPageContract
import com.example.foodiary.FirebaseUser
import com.example.foodiary.view.MainActivity
import com.google.firebase.auth.FacebookAuthCredential

class MyPagePresenter : MyPageContract.Presenter {
    private var view : MyPageContract.View? = null

    override fun logout(context: Context) {
        loadActivity(context,MainActivity())
    }

    override fun attachView(view: MyPageContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        view = null
    }

    override fun loadActivity(context: Context,activity: Activity) {
        val intent = Intent(context,activity::class.java)
        context.startActivity(intent)
    }

    override fun sendMail(context: Context) {
        if(isInstallCheck(context,"com.google.android.gm")) {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "plain/Text"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Foodiary 문의사항입니다. :)")
                intent.`package` = "com.google.android.gm"
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("yjk7768@gmail.com"))
                context.startActivity(intent)
            }catch (e : android.content.ActivityNotFoundException){
                throw e
            }
        }else{
            Toast.makeText(context,"gmail 어플을 설치해주세요.",Toast.LENGTH_SHORT).show()
        }
    }

    override fun isInstallCheck(context: Context,name : String): Boolean {
        val check = context.packageManager.getLaunchIntentForPackage(name)
        return check!=null
    }

}