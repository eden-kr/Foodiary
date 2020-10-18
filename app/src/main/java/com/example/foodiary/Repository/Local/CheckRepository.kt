package com.example.foodiary.Repository.Local

import android.util.Log
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults

class CheckRepository {
    private val realm: Realm = Realm.getDefaultInstance()

    fun getSpecifiedData(email : String, date : String) : MutableList<FoodDTO>{
        val res =realm.where(FoodDTO::class.java).equalTo("email",email).equalTo("date",date).findAllAsync()
        return realm.copyFromRealm(res)
    }
    fun deleteData(email: String, date: String , list : MutableList<String>){

        realm.executeTransactionAsync ({ realm ->
            val resList : RealmList<FoodDTO> = RealmList()
            if(list.size>0) {           //check the delete list
                list.forEach {
                    var res = realm.where(FoodDTO::class.java).equalTo("email", email)
                        .equalTo("date", date)
                        .equalTo("foodName", it)
                        .findFirst()
                    resList.add(res)
                }
            }
            if(resList?.size!!>0){      //check the realmResult
                resList?.forEach {
                    if(it.isValid){
                        it.deleteFromRealm()
                    }
                }
            }
        },{
            Log.d("myTag","checkRepository // data is successfully deleted.")
        },{
            Log.d("myTag","checkRepository // data delete is failed .. error -> ${it.printStackTrace()}")
            throw it
        })
    }

}