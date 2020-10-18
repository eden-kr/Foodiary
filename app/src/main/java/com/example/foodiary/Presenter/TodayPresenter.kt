package com.example.foodiary.Presenter

import android.content.Context
import android.util.Log
import androidx.collection.arrayMapOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.foodiary.Contract.TodayContract
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.AppMainRepository
import com.example.foodiary.Repository.Local.FoodDTO
import com.example.foodiary.Repository.Local.FoodLocalDTO
import com.example.foodiary.Repository.Local.UserDTO
import com.github.mikephil.charting.data.PieEntry
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import java.security.Key
import kotlin.math.roundToInt

class TodayPresenter : TodayContract.Presenter {
    private var view: TodayContract.View? = null
    private val disposable  = CompositeDisposable()
    private val repo by lazy { AppMainRepository() }
    override fun attachView(view: TodayContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        this.view = null
    }

    override fun getData(context: Context, date: String) {
        val data = repo.getUserData(context)
        val list = repo.getNominatedFoodData(context, date)
        Log.d("myTag", "리사이클러뷰에 들어갈 데이터 확인  -> ${data?.email}  / ${list.size}")
        if (data != null && list.size > 0) {
            view?.setAdapter(data, list)
        }
    }

    override fun getPieEntry(context: Context, date : String) {
        val data = repo.getSpecifiedDateAll(context,date)
        Log.d("myTag","get Realm Food data list Size -> ${data.size}")
        //copy
        //realm cannot access other Thread
        /*
        realm은 쓰레드를 넘을 수 없다는 제약이 있음
        다른 객체에 받아온 데이터를 복사한 다음 pieChart에 들어갈 수 있도록 데이터 가공 후 Chart 설정
        */
        val list = arrayListOf<FoodLocalDTO>()      //local same data
        data.forEach {
            list.add(FoodLocalDTO(it.date,it.email,it.foodName,it.cal,it.protein,it.carbohydrate,it.fat,it.type))
        }
        val kcalList = arrayMapOf(Pair("아침",0), Pair("점심",0),Pair("저녁",0),Pair("간식",0))
        val pieList = arrayListOf<PieEntry>()

        disposable.add(Observable.fromCallable {
            //doInbackground
            //list convert to PieEntryList
            list.forEach {
                when(it.type){
                    "breakfast" -> {
                        kcalList["아침"] = kcalList["아침"]?.plus(it.cal?.roundToInt()!!)
                    }
                    "lunch" -> {
                        kcalList["점심"] = kcalList["점심"]?.plus(it.cal?.roundToInt()!!)
                    }
                    "dinner" -> {
                        kcalList["저녁"] = kcalList["저녁"]?.plus(it.cal?.roundToInt()!!)
                    }
                    "snack" -> {
                        kcalList["간식"] = kcalList["간식"]?.plus(it.cal?.roundToInt()!!)
                    }
                }
            }
            kcalList.forEach {
                pieList.add(PieEntry(it.value.toFloat(),it.key))
            }
            Log.d("myTag","pieList size ->> ${pieList.size}")
        }.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                //postExecute
                view?.setChart(pieList)
            }
        )
    }

    override fun dispose() {
        disposable.dispose()
    }

    override fun replace(fragmentManager: FragmentManager, view: Fragment) {
        val frag = fragmentManager.beginTransaction()
        frag.replace(R.id.today_fragment,view)
        frag.commit()    }
}