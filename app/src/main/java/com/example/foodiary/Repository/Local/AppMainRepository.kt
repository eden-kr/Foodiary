package com.example.foodiary.Repository.Local

import android.content.Context
import android.util.Log
import com.example.foodiary.Session
import io.realm.Realm
import io.realm.exceptions.RealmException
//import io.realm.RealmResults
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AppMainRepository {
    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())
    private val realm = Realm.getDefaultInstance()

    fun getFoodData(context: Context) : ArrayList<FoodDTO>{
       val list = arrayListOf<FoodDTO>()
        Session.getInstance(context)
        realm.beginTransaction()
        realm.where(FoodDTO::class.java).equalTo("email",Session.getEmail()).equalTo("date", today).findAll()
            ?.forEach {
            list.add(it)
        }
        realm.commitTransaction()
        return list
    }
    fun getUserData(context: Context) : UserDTO?{
        Session.getInstance(context)
        realm.beginTransaction()
        val res = realm.where(UserDTO::class.java).equalTo("email",Session.getEmail()).findFirst()
        realm.commitTransaction()
        return res
    }
    fun getMyTotal(context: Context): TotalDTO?{
        Session.getInstance(context)
        realm.beginTransaction()
        val res = realm.where(TotalDTO::class.java).equalTo("email",Session.getEmail()).equalTo("date", today).findFirst()
        realm.commitTransaction()
        return res
    }
    fun getSpecifiedDateAll(context: Context,date: String) : MutableList<FoodDTO>{
        Session.getInstance(context)
        return  realm.where(FoodDTO::class.java).equalTo("email",Session.getEmail()).equalTo("date",date).findAll().toMutableList()
    }
    fun getNominatedFoodData(context : Context, date : String): ArrayList<FoodDTO>{
        val list = arrayListOf(FoodDTO(),FoodDTO(),FoodDTO(),FoodDTO())

        Session.getInstance(context)
        realm.executeTransactionAsync ({ realm ->
            //init
            list[0].foodName =""
            list[1].foodName= ""
            list[2].foodName = ""
            list[3].foodName= ""

            val res = realm.where(FoodDTO::class.java).equalTo("email",Session.getEmail()).equalTo("date", date).findAll()
            if(res.size>0) {
                for (i in 0 until res.size) {
                    when(res[i]?.type) {
                        "breakfast" -> {
                            list[0].cal = list[0].cal?.plus(res[i]?.cal!!)
                            list[0].foodName += "${res[i]?.foodName}(${res[i]?.cal}kcal)\n"
                            list[0].type = res[i]?.type
                        }
                        "lunch" -> {
                            list[1].cal = list[1].cal?.plus(res[i]?.cal!!)
                            list[1].foodName += "${res[i]?.foodName}(${res[i]?.cal}kcal)\n"
                            list[1].type = res[i]?.type
                        }
                        "dinner" -> {
                            list[2].cal = list[2].cal?.plus(res[i]?.cal!!)
                            list[2].foodName += "${res[i]?.foodName}(${res[i]?.cal}kcal)\n"
                            list[2].type = res[i]?.type
                        }
                        "snack" -> {
                            list[3].cal = list[3].cal?.plus(res[i]?.cal!!)
                            list[3].foodName += "${res[i]?.foodName}(${res[i]?.cal}kcal)\n"
                            list[3].type = res[i]?.type
                        }
                    }
                }
            }
        },{
            Log.d("myTag","NominatedFoodData is Successfully inserted!")
        },{
            Log.d("myTag","NominatedFoodData is failed -> ${it.printStackTrace()}")
            throw it
        })

        return list
    }
    fun getStepType(context: Context) : String?{
        Session.getInstance(context)
        return realm.where(UserDTO::class.java).equalTo("email",Session.getEmail()).findFirst()?.stepType
    }
    fun setStepType(context: Context, type : String) {
        Session.getInstance(context)
        realm.beginTransaction()
        val user = realm.where(UserDTO::class.java).equalTo("email",Session.getEmail()).findFirst()
        user?.stepType = type
        Log.d("myTag","pedometer is connected .. realm")
        realm.commitTransaction()
    }
    fun saveNowStep(context: Context,step : Int, consume : Int){
        Log.d("myTag"," 받는 데이터 $step $consume")
        Session.getInstance(context)
        realm.executeTransactionAsync ({ realm ->
                val res = realm.where(StepDTO::class.java).equalTo("email", Session.getEmail())
                    .equalTo("date", today).findFirst()
                if(res!=null) {
                    res.step = step
                    res.consumeCal = consume
                }else{
                    val obj = StepDTO()
                    obj.email = Session.getEmail()!!
                    obj.date = today
                    obj.step = step
                    obj.consumeCal = consume
                    realm.copyToRealm(obj)
                }
        },{
            Log.d("myTag","realm step data is successfully saved")
        },{
            Log.d("myTag","realm step data cannot saved ${it.stackTrace}${it.message} ${it.cause}")
        })
    }
    fun getWeight(email: String): UserDTO? {
        return realm.where(UserDTO::class.java).equalTo("email",email).findFirst()
    }
}