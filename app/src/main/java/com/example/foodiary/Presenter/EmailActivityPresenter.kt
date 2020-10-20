package com.example.foodiary.Presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.foodiary.Contract.EmailActivityContract
import com.example.foodiary.FirebaseUser
import com.example.foodiary.Repository.Local.LoginRepository
import com.example.foodiary.Repository.Remote.User
import com.example.foodiary.Session
import com.example.foodiary.view.FoodiaryActivity
import com.example.foodiary.view.LoginActivity

class EmailActivityPresenter : EmailActivityContract.Presenter {
    private val  firebaseUser = FirebaseUser.getInstance()
    private val repo by lazy { LoginRepository() }
    private var view : EmailActivityContract.View? =null
    override fun attachView(view: EmailActivityContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        this.view = null
    }

    override fun logIn(email: String, pw: String, context: Context) {
        val activity = context as Activity
        if(email.isNotBlank() && pw.isNotBlank()) {
            firebaseUser?.signInWithEmailAndPassword(email, pw)
                ?.addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        setSession(context)
                        val intent = Intent(context, FoodiaryActivity::class.java)
                        intent.putExtra("email", email)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(activity, "아이디, 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            Toast.makeText(context,"아이디나 비밀번호는 공백일 수 없습니다.",Toast.LENGTH_SHORT).show()
        }
    }



    override fun setSession(context: Context) {
        val res = view?.getEmail()?.let { repo.getUserInfo(it) }
        if(res!=null){
            Session.getInstance(context)
            Session.setNickname(res.nickName)
            Session.setEmail(view?.getEmail())
        }
    }

}