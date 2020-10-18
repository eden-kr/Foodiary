package com.example.foodiary.Repository.Local

import android.content.Context
import android.util.Log
import com.example.foodiary.Session
import com.skt.Tmap.TMapPoint
import io.realm.Realm
import io.realm.RealmList
import retrofit2.http.GET
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ln

class StepRepository {
    private val realm: Realm = Realm.getDefaultInstance()
    private val date = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Calendar.getInstance().time)
    fun getStepData(id : String, date : String): StepDTO?{
        return realm.where(StepDTO::class.java)
            .equalTo("email",id)
            .equalTo("date",date)
            .findFirst()
    }
    fun saveStepData(context: Context,mStep : Int,kcal : Int,distance : Float){
        Session.getInstance(context)
        val check : StepDTO? = realm.where(StepDTO::class.java).equalTo("email",Session.getEmail()).equalTo("date",date).findFirst()

        if(check!=null){
            realm.beginTransaction()
            val step = realm.createObject(StepDTO::class.java)
            step.email = Session.getEmail()!!
            step.date = date
            step.consumeCal = kcal
            step.distance = distance
            step.step = mStep
            realm.commitTransaction()
        }else{
            realm.executeTransactionAsync ({ realm ->
                val check  = realm.where(StepDTO::class.java).equalTo("email",Session.getEmail()).equalTo("date",date).findFirst()
                check?.step = mStep
                check?.consumeCal = kcal
                check?.distance = distance
            },{

            },{

            })
        }
    }
    fun saveNowPlace(email : String, date : String, lat : Double, lng : Double) {
        realm.executeTransactionAsync({ realm ->
            val res = realm.where(GeoDTO::class.java).equalTo("email",email)
                .equalTo("date",date)
                .findFirst()

            if(res==null){
                val geo = realm.createObject(GeoDTO::class.java)
                geo.date = date
                geo.email = email
                geo.point = RealmList()
                geo.point!!.add(MyPoint(email,lat,lng))

            }else{
                res.point!!.add(MyPoint(email,lat,lng))
            }
        }, {
            Log.d("myTag","place data is successfully saved")
        }, {
            Log.d("myTag","place data cannot saved ${it.printStackTrace()}")
        })
    }
    fun copyGeo(email : String, date : String) : ArrayList<TMapPoint>{
        val list = arrayListOf<TMapPoint>()
        realm.executeTransactionAsync { realm ->
            val res = realm.where(GeoDTO::class.java).equalTo("email",email)
                .equalTo("date",date)
                .findFirst()
            if(res?.point!=null) {
                res.point?.forEach {
                    list.add(TMapPoint(it.lat!!, it.lng!!))
                }
            }
            Log.d("myTag","Tmap list size -> ${list.size}")
        }
        return list
    }
}