package com.example.foodiary.Presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.foodiary.Contract.MainContract
import com.example.foodiary.FirebaseUser
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.LoginRepository
import com.example.foodiary.view.LoginActivity
import com.example.foodiary.Repository.Remote.User
import com.example.foodiary.Session
import com.example.foodiary.view.FoodiaryActivity
import com.example.foodiary.view.SignUpActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class MainPresenter() :MainContract.Presenter{
    private val RC_SIGN_IN = 1000
    //private val repo by lazy { LoginRepository() }
    private val localRepo = LoginRepository()
    //private val compositeDisposable = CompositeDisposable()
    val firebaseUser = FirebaseUser.getInstance()

    override fun logout(){
        if(FirebaseUser.getInstance()?.currentUser!= null) {
            FirebaseUser.getInstance()?.signOut()
        }
        com.facebook.login.LoginManager.getInstance().logOut()
        Session.setEmail(null)
        Session.setNickname(null)

    }

    //email 로그인
    override fun clickLogin(email: String, pw: String, context: Context) {
        val activity = context as Activity
        firebaseUser?.signInWithEmailAndPassword(email,pw)
            ?.addOnCompleteListener(activity){ task->
                if(task.isSuccessful){
                   //setLoginSession(context,email)
                    val intent = Intent(context,FoodiaryActivity::class.java)
                    intent.putExtra("email",email)
                    context.startActivity(intent)
                }else{
                    Toast.makeText(activity,"아이디, 비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun forgetPassword(context: Context) {
        //intent
    }

    override fun emailLogin(context: Context) {
        val intent = Intent(context, SignUpActivity::class.java)
        context.startActivity(intent)
    }

    //kakao , google
    override fun socialLogin(type : String ,context : Context) {
        var intent: Intent
        when (type) {
            "KaKao" -> {                //카카오 로그인
                val callback : (OAuthToken?,Throwable?) -> Unit = { token,error ->
                    if(error!=null){
                        Log.d("TAG",error.printStackTrace().toString())
                    }else if(token != null){
                        firebaseUser?.signOut()
                        UserApiClient.instance.me { user, error ->
                            if(user != null){
                                val email = user.id.toString()
                                val nickname = if(user.kakaoAccount?.profile?.nickname != null) user.kakaoAccount?.profile?.nickname else null
                                val url = if(user.kakaoAccount?.profile?.profileImageUrl != null) user.kakaoAccount?.profile?.profileImageUrl else null
                                val mUser = User(email,nickname,null,url)
                                startLogin(context,mUser)
                            }
                            Log.d("myTag",error?.printStackTrace().toString())
                        }
                    }
                }
                if(LoginClient.instance.isKakaoTalkLoginAvailable(context)){
                    LoginClient.instance.loginWithKakaoTalk(context,callback = callback)
                }else{
                    LoginClient.instance.loginWithKakaoAccount(context,callback = callback)
                }
            }
            "Google" -> {               //구글 로그인
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val signInIntent = GoogleSignIn.getClient(context, gso)

                intent = signInIntent.signInIntent
                val activity = context as Activity
                activity.startActivityForResult(intent, RC_SIGN_IN)
            }
        }
    }
    fun firebaseAuthWithGoogle(context: Context, idToken : String){
        val activity = context as Activity
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseUser?.signInWithCredential(credential)
            ?.addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user =  firebaseUser.currentUser
                    val mUser = User(user?.email,null,null,null)
                    startLogin(context,mUser)
                } else {
                    Log.d("myTag","Facebook Login Failed : "+task.exception?.printStackTrace())

                }
            }
    }

    //현재 로그인 정보 저장
    override fun setLoginSession(context: Context,email: String) {
        /*
        val user = localRepo.getUser(email)
        Session.getInstance(context)
        user?.email?.let { Session.setEmail(it) }
        user?.nickName?.let { Session.setNickname(it) }
         */
    }

    //회원정보가 존재하면 Main, 회원정보가 없으면 닉네임 등록 화면
    override fun startLogin(context: Context, user : User){
        var intent : Intent
        val res = localRepo.checkUser(user.email!!)

        if(!res){       //email이 DB내 존재 시
            //setLoginSession(context,user.email!!)
            intent = Intent(context,FoodiaryActivity::class.java)
            intent.putExtra("email",user.email)
            context.startActivity(intent)
        }else{
            intent = Intent(context,LoginActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("user",user)
            intent.putExtra("bundle",bundle)
            context.startActivity(intent)
        }

    }

    override fun dispose() {
        localRepo.realmClose()
    }
}