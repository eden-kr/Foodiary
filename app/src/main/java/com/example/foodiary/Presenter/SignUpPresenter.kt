package com.example.foodiary.Presenter

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.foodiary.Contract.SignUpContract
import com.example.foodiary.FirebaseUser
import com.example.foodiary.view.MainActivity
import java.util.regex.Pattern

class SignUpPresenter(private val view: SignUpContract.View) : SignUpContract.Presenter {
    private val localRepo by lazy { com.example.foodiary.Repository.Local.LoginRepository() }
    // private val compositeDisposable = CompositeDisposable()
    private var checkEmail = false

    override fun createUser() {
        val activity = view.getContext() as Activity

        if (checkEmail(view.getEmail()) && checkPassword(view.getPw())) {
            if (checkEmail) {
                FirebaseUser.getInstance()
                    ?.createUserWithEmailAndPassword(view.getEmail(), view.getPw())         //Create Firebase Auth
                    ?.addOnSuccessListener(activity) { task ->
                        view.makeToast("회원가입이 완료되었습니다.")
                        localRepo.insertUser(view.getEmail(), view.getNickname())       //Realm Insert
                        val intent = Intent(activity, MainActivity::class.java)         //Move to Login
                        activity.startActivity(intent)
                    }?.addOnFailureListener { it ->
                        Log.d("myTag", it.printStackTrace().toString())      //Exception
                    }

            } else {
                view.makeToast("아이디 중복 검사를 해주세요.")
            }
        }
    }

    override fun checkEmail(email: String): Boolean {
        var flag = false
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            flag = true
        } else {
            view.makeToast("이메일 형식을 확인해주세요.")
        }
        return flag
    }

    override fun checkPassword(pw: String): Boolean {
        var flag = false
        val regex = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$"
        if (Pattern.matches(regex, pw)) {
            flag = true
        } else {
            view.makeToast("숫자, 문자, 특수문자를 포함한 8-15자리를 지정해주세요.")
        }
        return flag
    }

    override fun checkDuplicate() {
        val check = localRepo.checkUser(view.getEmail())
        Log.d("myTag", "checkDuplicate -> $check")
        checkEmail = check
        if (!check) {
            view.makeToast("아이디가 중복되었습니다.")
        } else {
            view.makeToast("사용 가능한 아이디입니다.")
        }
    }

    override fun dispose() {
        localRepo.realmClose()
    }

}