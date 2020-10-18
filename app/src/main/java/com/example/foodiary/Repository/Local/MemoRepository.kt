package com.example.foodiary.Repository.Local

import android.content.Context
import android.util.Log
import com.example.foodiary.Session
import com.example.foodiary.myDate
import com.example.foodiary.myList
import io.realm.Realm
import io.realm.RealmResults
import io.realm.exceptions.RealmException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MemoRepository {
    private val realm: Realm = Realm.getDefaultInstance()
    private val date = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Calendar.getInstance().time)

    fun insertToTotalKcal(context: Context){
        Session.getInstance(context)
        realm.executeTransactionAsync ({ realm ->
            val res = realm.where(FoodDTO::class.java).equalTo("email",Session.getEmail()).equalTo("date",myDate).findAll()
            var total = 0
            if(res.size>0) {
                res.forEach {
                    total += it.cal?.roundToInt()!!
                }
            }
            val tot = realm.where(TotalDTO::class.java).equalTo("email",Session.getEmail()).equalTo("date",myDate).findFirst()
            if(tot != null) {
                tot.totalCal = total
                tot.email = Session.getEmail()!!
                tot.date = myDate
            }else {
                val new = realm.createObject(TotalDTO::class.java)
                new.date = myDate
                new.email = Session.getEmail()!!
                new.totalCal = total
            }
            Log.d("myTag","total cal data -> date $myDate  totalcal -> $total")

        },{
            Log.d("myTag","total kcal is inserted")
        },{
            Log.d("myTag","total kcal insert is failed -> ${it.printStackTrace()}")
        })
    }
    fun getUserData() : UserDTO?{
        var check : UserDTO?
        realm.beginTransaction()
        try{
             check = realm.where(UserDTO::class.java).equalTo("email", Session.getEmail()).findFirst()
            realm.commitTransaction()
        }catch (e : Exception){
            throw e
        }
        return check
    }
    fun getCalData(today : String) : RealmResults<FoodDTO>{          //get Food Data in specified Date
        return realm.where(FoodDTO::class.java).equalTo("email", Session.getEmail())
            .equalTo("date", today)
            .findAll()
    }
    fun setCalData(context: Context, type : String){
        Log.d("myTag","list size -> ${ myList.size}")
        Session.getInstance(context)

        realm.executeTransactionAsync ({ realm ->
            for(i in 0 until myList.size){
                val todayFood = realm.createObject(FoodDTO::class.java)
                todayFood.email = Session.getEmail()
                todayFood.foodName = myList[i].name
                todayFood.date = myDate
                todayFood.type = type
                todayFood.cal = myList[i].cal.toDouble()
                todayFood.carbohydrate = myList[i].carbo.toDouble()
                todayFood.protein = myList[i].protein.toDouble()
                todayFood.fat = myList[i].fat.toDouble()
                Log.d("myTag","$myDate -> foodData is saved..")
            }
        },{
            Log.d("myTag","inserting today food data is successful")
        },{
            Log.d("myTag","${it.printStackTrace()}")
        })
    }
    fun getFoodData(email : String) : RealmResults<FoodDTO>?{
        var res : RealmResults<FoodDTO>? = null
        try{
            res = realm.where(FoodDTO::class.java).equalTo("email",email).findAll()
        }catch (e : RealmException){
            Log.d("myTag","${e.printStackTrace()}")
            throw e
        }
        return res
    }
    fun writeMemo(email: String, date : String, content: String, type: Int){
        try{
            realm.executeTransactionAsync({ realm ->
                var postNum = 0
                val res = realm.where(MemoDTO::class.java).equalTo("email",email).findAll()
                res.forEach {
                   if(postNum<it?.postNum!!){
                       postNum = it?.postNum!!
                   }
                }
                Log.d("myTag","가장 큰 postNum -> $postNum")
                val memo = realm.createObject(MemoDTO::class.java)
                memo.postNum = postNum
                memo.background = type
                memo.content = content
                memo.date = date
                memo.email = email

            },{
                Log.d("myTag","write memo is successful")
            },{
                Log.d("myTag","write memo is failed.. ${it.printStackTrace()}")
            })

        }catch (e : RealmException){
            Log.d("myTag","${e.printStackTrace()}")
            throw e
        }

    }
    fun realmClose(){
        realm.close()
    }

}