package com.example.foodiary.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.foodiary.Repository.Remote.FoodDataPOJO
import com.example.foodiary.view.FoodDataActivity

class FoodDataReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("myTag","catch? broadCast Recevied")
        var count = p1?.getIntExtra("count",1)
        var data = p1?.getSerializableExtra("data") as? FoodDataPOJO
        if(p0!= null && count!=null && data != null) {
            sendToActivity(p0, count, data)
        }
    }
    private fun sendToActivity(context: Context, cnt : Int, data : FoodDataPOJO){
        val intent = Intent(context,FoodDataActivity::class.java)
        intent.putExtra("count",cnt)
        intent.putExtra("data",data)
        context.startActivity(intent)
    }
}