package com.example.foodiary.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiary.Contract.CheckFoodListContract
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.FoodDTO
import kotlin.math.roundToInt

class CheckListAdapter(private val list : MutableList<FoodDTO>,private val presenter : CheckFoodListContract.Presenter) : RecyclerView.Adapter<CheckListHolder>() {
    private val delete = mutableListOf<String>()
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CheckListHolder, position: Int) {
        val data = list[position]
        holder.name.text = data.foodName
        holder.kcal.text = "${data.cal?.roundToInt()} kcal"

        holder.name.setOnClickListener {
            holder.check.isChecked = !holder.check.isChecked
        }
        holder.check.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked){
               presenter.getCheckedData(data.foodName!!)
            }else{
                presenter.deleteCheckData(data.foodName!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.revise_food_list,parent,false)
        return CheckListHolder(view)
    }
}
class CheckListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val check : CheckBox = itemView.findViewById(R.id.isChecked_list)
    val name : TextView = itemView.findViewById(R.id.name_list)
    val kcal : TextView = itemView.findViewById(R.id.kcal_list)
}