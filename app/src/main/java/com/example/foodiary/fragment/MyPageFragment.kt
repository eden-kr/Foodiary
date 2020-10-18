package com.example.foodiary.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.foodiary.Contract.MyPageContract
import com.example.foodiary.Presenter.MyPagePresenter
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.MyPageRepository
import com.example.foodiary.Session
import com.example.foodiary.view.ChangeDataActivity
import com.example.foodiary.view.WithdrawActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_mypage_fragment.*

class MyPageFragment : MyPageContract.View, Fragment() {
    private lateinit var email : TextView
    private lateinit var  name : TextView
    private lateinit var myImage : ImageView
    private var presenter: MyPageContract.Presenter? = null
    private val repo by lazy { MyPageRepository() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myPageView = inflater.inflate(R.layout.activity_mypage_fragment,container,false)
        val reviseData = myPageView.findViewById<TextView>(R.id.myPage_revise_data)
        val logout = myPageView.findViewById<TextView>(R.id.myPage_logout)
        val ask = myPageView.findViewById<TextView>(R.id.myPage_ask)
        myImage = myPageView.findViewById(R.id.myImage)
        val withdraw = myPageView.findViewById<TextView>(R.id.myPage_delete_account)    //회원탈퇴'
        email = myPageView.findViewById(R.id.myPage_email)
        name = myPageView.findViewById(R.id.myPage_nickname)

        //클릭 리스너 설정
        reviseData.setOnClickListener {
            presenter?.loadActivity(requireContext(), ChangeDataActivity())
        }
        logout.setOnClickListener {
            presenter?.logout(requireContext())
        }
        ask.setOnClickListener {
            presenter?.sendMail(requireContext())

        }
        withdraw.setOnClickListener {
            presenter?.loadActivity(requireContext(), WithdrawActivity())
        }


        setUserData()

        return myPageView
    }
    override fun setPresenter(presenter: MyPageContract.Presenter) {
        this.presenter = presenter
    }

    override fun setUserData() {
        email.text = Session.getEmail()
        name.text = Session.getNickname()
        val image = repo.getUserImage()
        Log.d("myTag","image url -> $image")
        if(image!=null) {
            Picasso.get().load(image).into(myImage)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
        repo.dispose()
    }
}