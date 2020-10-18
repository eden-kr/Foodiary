package com.example.foodiary.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.foodiary.Contract.MainContract
import com.example.foodiary.FacebookLoginCallback
import com.example.foodiary.Presenter.MainPresenter
import com.example.foodiary.R
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.View {

    val presenter by lazy {MainPresenter()}
    private val RC_SIGN_IN = 1000
    private val callbackManager = CallbackManager.Factory.create()
    override fun onStart() {
        super.onStart()
        presenter.logout()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //페이스북 버튼에 콜백 달기
        login_facebook.registerCallback(callbackManager,FacebookLoginCallback(this))

        login_ok.setOnClickListener {
            presenter.clickLogin(getEmail(),getPassword(),this)
        }
        fake_facebook.setOnClickListener {
            login_facebook.performClick()       //Perform
        }
        login_email.setOnClickListener {
            presenter.emailLogin(this)
        }
        login_kakao.setOnClickListener {
            presenter.socialLogin(getKaKaoLogin(),this)
        }
        fake_google.setOnClickListener {
            presenter.socialLogin(getGoogleLogin(),this)
        }

    }
    override fun getKaKaoLogin(): String {
        return "KaKao"
    }

    override fun getGoogleLogin(): String {
        return "Google"
    }

    override fun getEmail(): String {
        return email.text.toString()
    }

    override fun getPassword(): String {
        return pw.text.toString()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            RC_SIGN_IN ->{
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try{
                    val account = task.getResult(ApiException::class.java)!!
                    presenter.firebaseAuthWithGoogle(this,account.idToken!!)

                }catch(e: ApiException){
                    Log.d("TAG",e.printStackTrace().toString())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

}
