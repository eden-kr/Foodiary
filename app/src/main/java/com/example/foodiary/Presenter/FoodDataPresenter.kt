package com.example.foodiary.Presenter

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.foodiary.Adapter.MyListAdapter
import com.example.foodiary.Contract.FoodDataContract
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.MemoRepository
import com.example.foodiary.Repository.Remote.FoodDataPOJO
import com.example.foodiary.Retrofit.RxRetrofit
import com.example.foodiary.Retrofit.retrofit
import com.example.foodiary.fragment.FoodDataFragment
import com.example.foodiary.view.KcalSettingActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FoodDataPresenter : FoodDataContract.Presenter {
    private var view : FoodDataContract.View? = null
    private val REQUEST_READ_EXTERNAL_STORAGE = 10000
    private lateinit var myFile : File
    private val compositeDisposable = CompositeDisposable()
    private lateinit var myListAdapter : MyListAdapter
    private val repo by lazy { MemoRepository() }


    override fun attachView(view: FoodDataContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        this.view = null
    }
    override fun saveType(editor : SharedPreferences.Editor){
        Log.d("myTag","save data -> ${view?.getType()}")
        if(!view?.getType().isNullOrBlank()){
            editor.putString("type",view?.getType())
            editor.apply()
        }
    }

    override fun checkWords(word: String) : Boolean{
        //초성, 중성, 종성 분리
        val INITIAL_SOUND  = arrayOf("ㄱ" ,'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' )
        var word = word[word.lastIndex]
        return INITIAL_SOUND.contains(word)
    }

    override fun getFoodData(foodName: String) {        //foodAdapter와 통신
        val server = retrofit.getInstance()         //Retrofit!
        server.getFoodData(foodName).enqueue(object : Callback<List<FoodDataPOJO>>{
            override fun onFailure(call: Call<List<FoodDataPOJO>>, t: Throwable) {
                Log.d("myTag","get Food data is failed.. ${t.printStackTrace()}")
                throw t
            }
            override fun onResponse(call: Call<List<FoodDataPOJO>>, response: Response<List<FoodDataPOJO>>) {
                Log.d("myTag",response.body()?.size.toString())
                if(response.body()!=null) {
                    val res = response.body() as ArrayList<FoodDataPOJO>
                    view?.returnFragmentManager()
                        ?.let { replace(it, FoodDataFragment.newInstance(res)) }
                }
            }
        })
    }

    override fun saveFoodData(context: Context,type : String) {          //MyListAdapter와 통신
       repo.setCalData(context,type)
    }

    override fun uriToFile(context: Context, uri: Uri) {
        val dt: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c = context.contentResolver.query(uri, dt, null, null, null)
        val index = c?.getColumnIndex(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        myFile = File(index?.let { c?.getString(it) }!!)
        getFlaskData(context,myFile)
    }

    override fun getFlaskData(context: Context,file: File) {
        val requestBody = RequestBody.create(MediaType.parse("image/*"),file)
        val body = MultipartBody.Part.createFormData("file","file",requestBody)

        RxRetrofit.getInstance()?.getNutrientFromImage(body)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { it ->
                if(it.isNotEmpty()) {
                    val intent = Intent(context, KcalSettingActivity::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable("item", it[0])
                    intent.putExtra("bundle", bundle)
                    context.startActivity(intent)
                }else{
                    Toast.makeText(context,"이미지 분석에 실패했어요.",Toast.LENGTH_SHORT).show()
                }
            }?.apply { compositeDisposable.add(this) }?.let { it1 ->
                compositeDisposable.add(
                    it1
                )
            }

    }

    override fun getSearchImage(context: Context) {
        val activity = context as Activity
        if (ContextCompat.checkSelfPermission(  //권한 확인
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {   //권한 재요청
                view?.makeDialog()
            } else {    // 권한요청
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE
                )
            }
        } else {
            //이미 권한이 허용된 경우
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)   //갤러리에서 이미지 가져오기
            context.startActivityForResult(intent, REQUEST_READ_EXTERNAL_STORAGE)
        }
    }

    override fun dispose() {
        compositeDisposable.clear()
    }

    override fun setAds(context : Context) {
        val mInterstitialAd = InterstitialAd(context)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        val adListener = object : AdListener(){
            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d("myTag","ad is successful")
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                Log.d("myTag","ad is failed..")
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }
        }
        mInterstitialAd.adListener = adListener

        if(mInterstitialAd.isLoaded)
            mInterstitialAd.show()
    }

    override fun checkListSize(list : ArrayList<FoodDataPOJO>) : Boolean {
        return list.size > 0
    }

    override fun replace(fragmentManager: FragmentManager, fragment: Fragment) {
        val frag = fragmentManager.beginTransaction()
        frag.replace(R.id.memo_frameLayout,fragment)
        frag.commit()
    }

    override fun setBroadCastReceiver(context: Context,receiver: BroadcastReceiver) {
        val filter = IntentFilter()
        filter.addAction("com.example.foodiary.action.DATA_PASS")
        context.registerReceiver(receiver,filter)
    }
}