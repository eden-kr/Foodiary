package com.example.foodiary.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiary.R
import com.example.foodiary.Repository.Remote.FoodDataPOJO
import com.example.foodiary.myList
import com.jakewharton.rxbinding.view.RxView
import java.util.*

class MyListAdapter(val context : Context,val countView : TextView) : RecyclerView.Adapter<MyHolder>(){
   // private val repo by lazy { FoodDataRepository() }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        if(!myList.isNullOrEmpty()) {
            val my = myList[position]
            holder.foodName.text = my.name
            holder.foodCal.text = my.cal
        }

        RxView.clicks(holder.min)
            .subscribe {
                myList.removeAt(position)
                notifyDataSetChanged()
                countView.text = myList.size.toString()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.selected_list_layout,null)
        return MyHolder(view)
    }
    fun onUpdateItem(foodData : FoodDataPOJO){
        Log.d("myTag","how much I get? ${foodData.name}")
        myList.add(foodData)
        notifyDataSetChanged()
    }
    fun returnAllItems() : ArrayList<FoodDataPOJO>{
        return myList
    }
}
class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val foodName : TextView = itemView.findViewById(R.id.foodName_selected)
    val foodCal : TextView = itemView.findViewById(R.id.food_cal_Selected)
    val min : ImageView = itemView.findViewById(R.id.min_food)
}