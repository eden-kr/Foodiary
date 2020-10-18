package com.example.foodiary.view

import android.animation.ValueAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.postDelayed
import com.example.foodiary.Contract.MemoWriteContract
import com.example.foodiary.Presenter.MemoWritePresenter
import com.example.foodiary.R
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_memo.*

class MemoActivity : AppCompatActivity(),MemoWriteContract.View {
    private lateinit var presenter: MemoWriteContract.Presenter
    private var type = 0
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        val intent = intent
        val date = intent.getStringExtra("date")        //get Date

        presenter = MemoWritePresenter()
        presenter.attachView(this)

        limitLine()
        setOutLine()
        setMemoBackground()
        setDate(date)

        RxView.clicks(complete_button)
            .subscribe {
                //database insert
                complete_button.visibility = View.INVISIBLE
                setLottie()
                val handler = Handler()
                handler.postDelayed({
                    this.finish()
                },2000)
            }
        RxView.clicks(back_memo)
            .subscribe {
                this.finish()
            }

    }

    override fun setPresenter(presenter: MemoWriteContract.Presenter) {
        this.presenter = presenter
    }

    override fun limitLine() {
        //editText의 라인을 15줄로 제한
        var prev = ""
        memo_edit.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(memo_edit.lineCount>10){
                    memo_edit.setSelection(memo_edit.length())
                    memo_edit.setText(prev)
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                prev = p0.toString()
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun setOutLine() {
        monun_green.clipToOutline = true
        monun_white.clipToOutline = true
        monun_yellow.clipToOutline = true
    }

    override fun setMemoBackground() {
        val list = arrayListOf(yello_memo,pink_memo,purple_memo,white_memo,sky_memo,monun_yellow,monun_green,monun_white)
        val clickListener = View.OnClickListener {
            when(it){
                yello_memo -> {
                    memo_edit.setBackgroundResource(R.drawable.yello_memo)
                    type =1
                }
                pink_memo-> {
                    memo_edit.setBackgroundResource(R.drawable.pink_memo)
                    type=2
                }
                white_memo->{
                    memo_edit.setBackgroundResource(R.drawable.white_round_stroke)
                    type =3
                }
                sky_memo-> {
                    memo_edit.setBackgroundResource(R.drawable.blue_memo)
                    type=4
                }
                purple_memo-> {
                    memo_edit.setBackgroundResource(R.drawable.purple_memo)
                    type=5
                }
                monun_yellow -> {
                    memo_edit.setBackgroundResource(R.drawable.monun_yellow)
                    type=6
                }
                monun_green -> {
                    memo_edit.setBackgroundResource(R.drawable.monun_green)
                    type =7
                }
                monun_white -> {
                    memo_edit.setBackgroundResource(R.drawable.monun_white)
                    type=8
                }
            }
        }
        list.forEach {
            it.setOnClickListener(clickListener)
        }
    }

    override fun setLottie() {
        complete_memo.bringToFront()
        complete_memo.visibility = View.VISIBLE
        val animator = ValueAnimator.ofFloat(0f, 1.0f).setDuration(2000)
        animator.addUpdateListener {
            complete_memo.progress = it.animatedValue as Float
        }
        animator.start()
    }

    override fun setDate(date : String) {
        memo_date.text = date.replace("-",".")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }
}
