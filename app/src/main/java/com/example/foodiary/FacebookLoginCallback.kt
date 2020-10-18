package com.example.foodiary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.foodiary.Presenter.MainPresenter
import com.example.foodiary.Repository.Remote.User
import com.example.foodiary.view.LoginActivity
import com.example.foodiary.view.MainActivity
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

class FacebookLoginCallback(val activity : MainActivity) : FacebookCallback<LoginResult> {
    override fun onCancel() {
        activity.setResult(Activity.RESULT_CANCELED)
    }

    override fun onError(error: FacebookException?) {
        Toast.makeText(activity,"로그인이 취소되었습니다.",Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(result: LoginResult?) {
        activity.setResult(Activity.RESULT_OK)
        result?.accessToken?.let { handleFacebookAccessToken(it) }
    }
    private fun handleFacebookAccessToken(token: AccessToken){
        val auth = FirebaseAuth.getInstance()
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity){task ->
                if(task.isSuccessful){
                    val user = auth?.currentUser
                    val mUser = User(user?.email,user?.displayName,null,user?.photoUrl.toString())
                    MainPresenter().startLogin(activity,mUser)
                }else{
                    Log.d("myTag","Facebook Login Failed : "+task.exception?.printStackTrace())
                }
            }
    }
}