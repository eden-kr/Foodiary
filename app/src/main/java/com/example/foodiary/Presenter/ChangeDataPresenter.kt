package com.example.foodiary.Presenter

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.foodiary.Contract.ChangeDataContract
import com.example.foodiary.Contract.MyPageContract
import com.example.foodiary.FirebaseUser
import com.example.foodiary.Repository.Local.MyPageRepository
import com.example.foodiary.Session
import com.example.foodiary.view.ChangeDataActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

class ChangeDataPresenter : ChangeDataContract.Presenter {
    private var view : ChangeDataContract.View? = null
    private val repo by lazy {MyPageRepository()}
    private val storage = Firebase.storage
    private val REQUEST_READ_EXTERNAL_STORAGE = 5000
    private var myImageUrl : String? = null

    override fun setData() {
        val sex = view?.getSex()
        val weight = view?.getWeight()
        val height = view?.getHeight()
        val maxCal = view?.getMaxCal()
        repo.setUserInfo(sex!!,height!!,weight!!,maxCal!!)
        view?.makeToast("정보 수정이 완료되었습니다.")
    }

    override fun attachView(view: ChangeDataContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    override fun uploadMyProfile() {
         val myImageFile = File(myImageUrl)      //File
         val url = Uri.fromFile(myImageFile)     //File to Url
        //val url = myImageUrl?.toUri()
        val path = "${Session.getEmail()}/myProfile"
        if (url != null) {
            storage.reference.child(path).putFile(url).addOnSuccessListener { it ->
                try {
                    val task = it.metadata?.reference?.downloadUrl
                    val path: String? = it.metadata?.path
                    repo.setUserImage(path)         //Realm에 이미지 저장
                    task?.addOnSuccessListener { it ->
                        var url = it.toString()         //image url
                        var path = it.lastPathSegment.toString()
                        Log.d("myTag","url -> $url   path -? $path")
                        repo.setUserImage(url)
                    }
                }catch (e : Exception){
                    Log.d("myTag","${e.printStackTrace()}")
                    throw e
                }
            }.addOnFailureListener { it ->
                Log.d("myTag","fireStorage Upload faile.. ${it.stackTrace}")
            }
        }
    }

    override fun getPhotoPermission(context: Context) {
        val activity = context as Activity
        if (ContextCompat.checkSelfPermission(  //권한 확인
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {   //권한 재요청
             view?.makeDialog()
            } else {    // 권한요청
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE
                )
            }
        } else {
            //이미 권한이 허용된 경우
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)   //갤러리에서 이미지 가져오기
            activity.startActivityForResult(intent, REQUEST_READ_EXTERNAL_STORAGE)
        }
    }

    override fun geUri(context: Context,uri: Uri)  {
        var dt: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c = context.contentResolver.query(uri, dt, null, null, null)
        var index = c?.getColumnIndex(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        myImageUrl = index?.let { c?.getString(it) }
    }

    override fun dipose() {
        repo.dispose()
    }

    override fun streamToBitmap(context: Context,uri : Uri) {
        val ips = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(ips)
        view?.setMyProfile(bitmap)

    }

    override fun getMyId(context: Context) {
        Session.getInstance(context)
        Session.getEmail()?.let { view?.setMyId(it) }
    }


}