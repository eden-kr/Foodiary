package com.example.foodiary.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.foodiary.R
import com.example.foodiary.Repository.Remote.FoodDataPOJO
import com.example.foodiary.view.KcalSettingActivity
import com.jakewharton.rxbinding.view.RxView

class FoodDataAdapter(val context : Context, val items: ArrayList<FoodDataPOJO> = arrayListOf<FoodDataPOJO>()) : RecyclerView.Adapter<FoodDataHolder>(){

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FoodDataHolder, position: Int) {
        val item = items[position]
        holder.foodName.text = item.name
        holder.foodCal.text = "${item.cal} kcal"

        RxView.clicks(holder.addButton)
            .subscribe {
                val intent = Intent(context,KcalSettingActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("item",item)
                intent.putExtra("bundle",bundle)
                context.startActivity(intent)
            }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodDataHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.food_list_layout,null)
        return FoodDataHolder(view)
    }

}
class FoodDataHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val foodName : TextView = itemView.findViewById(R.id.foodName_foodList)
    val foodCal : TextView = itemView.findViewById(R.id.food_cal)
    val addButton : ImageView = itemView.findViewById(R.id.add_food)
}