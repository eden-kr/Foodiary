package com.example.foodiary

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.foodiary.Repository.Remote.FoodDataPOJO
import com.kakao.sdk.common.KakaoSdk
import io.realm.Realm
import io.realm.RealmConfiguration
import java.text.SimpleDateFormat
import java.util.*

//import io.realm.Realm
val myList = arrayListOf<FoodDataPOJO>()
var myDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())

class GlobalApplication : Application() {
    lateinit var realm : Realm
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val realmConfig = RealmConfiguration
            .Builder()
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfig)

        KakaoSdk.init(this, applicationContext.getString(R.string.kakao_api_key))
    }
}