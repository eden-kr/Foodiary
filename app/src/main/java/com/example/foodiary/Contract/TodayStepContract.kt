package com.example.foodiary.Contract

import android.content.Context
import com.skt.Tmap.TMapPoint

class TodayStepContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun setMap()
        fun setData(mStep : Int,mKcal : Int, distance: Float)
        fun makeDialog(msg : String, array: Array<String>,code:Int)
        fun drawMap()
        fun nowDraw(list : ArrayList<TMapPoint>)
        fun setNowPlace(lat : Double,lon:Double)
    }
    interface Presenter{
        fun attachView(view : View)
        fun detachView()
        fun getStep(id : String, date: String)
        fun getCurrentPlace(context: Context,date: String)
        fun realmToLocal(email: String, date : String)  : ArrayList<TMapPoint>
    }
}