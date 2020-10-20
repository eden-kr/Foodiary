package com.example.foodiary.Presenter

import com.example.foodiary.Contract.FindPasswordContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class FindPasswordPresenter : FindPasswordContract.Presenter {
    private var view : FindPasswordContract.View? = null
    private val auth = FirebaseAuth.getInstance()
    override fun attachView(view: FindPasswordContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        this.view = null
    }

    override fun sendEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            ?.addOnCompleteListener {
                if(it.isSuccessful){
                    view?.replacePage(email)
                }
            }
    }
}