package com.example.foodiary.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.foodiary.Contract.ChangeDataContract
import com.example.foodiary.Presenter.ChangeDataPresenter
import com.example.foodiary.R
import com.jakewharton.rxbinding.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_change_data.*
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ChangeDataActivity : AppCompatActivity(), ChangeDataContract.View {
    private var presenter : ChangeDataContract.Presenter? = null
    private val REQUEST_READ_EXTERNAL_STORAGE = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_data)

        presenter = ChangeDataPresenter()
        presenter?.attachView(this)
        presenter?.getMyId(this)

        RxView.clicks(myPage_ok)
            .throttleFirst(1,TimeUnit.SECONDS)
            .subscribe{ it ->
                presenter?.setData()
                onBackPressed()
            }

        myPage_close.setOnClickListener {
            finish()
        }

        //연속클릭 방지
        RxView.clicks(myImage)
            .throttleFirst(1,TimeUnit.SECONDS)  //첫번째 이벤트 실행 후 다른 이벤트는 버림
            .subscribe{ it->
                presenter?.getPhotoPermission(this)
            }


    }

    override fun getHeight() : Int {
        var height = 0
        if(!myHeight.text.isNullOrBlank()){
            height = myHeight.text.toString().toInt()
        }
        return height
    }

    override fun getWeight() : Float {
        var weight = 0F
        if(!myWeight.text.isNullOrBlank()){
            weight = myWeight.text.toString().toFloat()
        }
        return weight
    }

    override fun getSex() : String {
        var sex = "여성"
        if(sex_man.isChecked || !sex_women.isChecked){
            sex ="남성"
        }
        return sex
    }

    override fun getMaxCal() : Int{
        var maxKcal = 0
        if(!myMaxCal.text.isNullOrBlank()){
            maxKcal = myMaxCal.text.toString().toInt()
        }
        return maxKcal
    }

    override fun setMyProfile(bitmap: Bitmap) {
        myImage.setImageBitmap(bitmap)
    }

    override fun makeToast(messege : String) {
        Toast.makeText(this,messege,Toast.LENGTH_SHORT).show()
    }

    override fun makeDialog() {
        val dlg = AlertDialog.Builder(this@ChangeDataActivity)
        dlg.setTitle("권한 설정")
            .setCancelable(true)
            .setPositiveButton("확인") { p0, p1 ->
                //권한 요청
                ActivityCompat.requestPermissions(
                    this@ChangeDataActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE
                )
            }.setNegativeButton("취소"){ p0,p1 ->
            }.show()
    }

    override fun setMyId(id: String) {
        setmyId.text = id
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_READ_EXTERNAL_STORAGE && resultCode == Activity.RESULT_OK){
            try{
                if(data?.data!=null) {
                    presenter?.streamToBitmap(this,data.data!!)     //image 변경
                    presenter?.geUri(this, data.data!!)             //이미지 uri 변경 + db저장
                    presenter?.uploadMyProfile()
                }
            }catch (e : IOException){
                Log.d("myTag","${e.stackTrace}")
                throw e
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.dipose()
    }

}
