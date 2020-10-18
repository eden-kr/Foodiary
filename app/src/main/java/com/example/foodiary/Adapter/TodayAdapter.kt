package com.example.foodiary.Adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.AppMainRepository
import com.example.foodiary.Repository.Local.FoodDTO
import com.example.foodiary.Repository.Local.UserDTO
import java.lang.Exception
import kotlin.math.abs
import kotlin.math.roundToInt

class TodayAdapter(
    val context: Context,
    private val myData: UserDTO,
    private val list: ArrayList<FoodDTO>
) :
    RecyclerView.Adapter<TodayHolder>() {
    private var tot = 0         //일일 권장 칼로리
    private var breakfast = 0   //일일 권장 아침 칼로리
    private var lunch = 0
    private var diner = 0


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TodayHolder, position: Int) {
        val measure = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED)
        var param = holder.c1.layoutParams
        param.width = 30

        myData.height?.let { getRecommendedKcal(it) }

        val data = list[position]
        when (data.type) {
            "breakfast" -> {
                holder.menu.text = data.foodName
                holder.menu.measure(holder.menu.width,holder.menu.height)
                param.height = holder.menu.measuredHeight - 30
                holder.c1.layoutParams = param
                holder.totalKcal.text = "${data.cal} kcal"

                if (data.cal!! > breakfast) {
                    holder.c1.background = ContextCompat.getDrawable(context, R.drawable.round_red)
                    holder.alram.text = "조금만 더 노력해요"
                    holder.alram.setTextColor(ContextCompat.getColor(context, R.color.red))
                    holder.image.setImageResource(R.drawable.bad)
                }
                if (data.cal!! < breakfast - 200) {
                    holder.c1.background =
                        ContextCompat.getDrawable(context, R.drawable.green_round)
                    holder.alram.text = "아침은 든든히!"
                    holder.alram.setTextColor(ContextCompat.getColor(context, R.color.green))
                    holder.image.setImageResource(R.drawable.good)
                }else if(data.cal!! < breakfast) {
                    holder.c1.background =
                        ContextCompat.getDrawable(context, R.drawable.yellow_drawble)
                    holder.alram.text = "적당한 식사에요"
                    holder.alram.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.myPageEditText
                        )
                    )
                    holder.image.setImageResource(R.drawable.fine)
                }

            }
            "lunch" -> {
                holder.menu.text = data.foodName
                holder.menu.measure(holder.menu.width,holder.menu.height)
                param.height = holder.menu.measuredHeight
                holder.c1.layoutParams = param
                holder.totalKcal.text = "${data.cal} kcal"

                if (data.cal!! > lunch) {
                    holder.alram.text = "조금만 더 노력해요"
                    holder.c1.background = ContextCompat.getDrawable(context, R.drawable.round_red)
                    holder.alram.setTextColor(ContextCompat.getColor(context, R.color.red))
                    holder.image.setImageResource(R.drawable.bad)
                }
                if (data.cal!! < lunch - 200) {
                    holder.c1.background =
                        ContextCompat.getDrawable(context, R.drawable.green_round)
                    holder.alram.text = "건강한 식사에요"
                    holder.alram.setTextColor(ContextCompat.getColor(context, R.color.green))
                    holder.image.setImageResource(R.drawable.good)
                }else if (data.cal!! < lunch) {
                    holder.c1.background =
                        ContextCompat.getDrawable(context, R.drawable.yellow_drawble)
                    holder.alram.text = "적당한 식사에요"
                    holder.alram.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.myPageEditText
                        )
                    )
                    holder.image.setImageResource(R.drawable.fine)
                }

            }

            "dinner" -> {
                holder.menu.text = data.foodName
                holder.menu.measure(holder.menu.width,holder.menu.height)
                param.height = holder.menu.measuredHeight
                holder.c1.layoutParams = param
                holder.totalKcal.text = "${data.cal} kcal"

                if (data.cal!! > diner) {
                    holder.c1.background = ContextCompat.getDrawable(context, R.drawable.round_red)
                    holder.alram.text = "조금만 더 노력해요"
                   holder.alram.setTextColor(ContextCompat.getColor(context, R.color.red))
                    holder.image.setImageResource(R.drawable.bad)
                }
                if (data.cal!! < diner - 200) {
                    holder.c1.background =
                        ContextCompat.getDrawable(context, R.drawable.green_round)
                    holder.alram.text = "건강한 식사에요"
                    holder.alram.setTextColor(ContextCompat.getColor(context, R.color.green))
                    holder.image.setImageResource(R.drawable.good)
                } else if (data.cal!! < diner) {
                    holder.c1.background =
                        ContextCompat.getDrawable(context, R.drawable.yellow_drawble)
                    holder.alram.text = "적당한 식사에요"
                    holder.alram.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.myPageEditText
                        )
                    )
                    holder.image.setImageResource(R.drawable.fine)
                }

            }

            "snack" -> {
                holder.menu.text = data.foodName
                holder.menu.measure(holder.menu.width,holder.menu.height)
                param.height = holder.menu.measuredHeight
                holder.c1.layoutParams = param
                holder.alram.text = "신나는 간식타임"
                holder.totalKcal.text = "${data.cal} kcal"
                holder.image.setImageResource(R.drawable.fine)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.today_food_data, parent, false)
        return TodayHolder(view)
    }

    private fun getRecommendedKcal(height: Int) {
        val stdWeight = (height.minus(100)).times(0.9)      //표준체중
        tot = stdWeight.times(30).roundToInt()     //표준체중 * 활동지수
        breakfast = abs((tot / 5))
        lunch = abs((tot / 5) * 2)
        diner = abs((tot / 5) * 2)
    }

    private fun getTotalKcal(list: ArrayList<FoodDTO>): Int {
        var kcal = 0.0
        if (list.size > 0) {
            list.forEach {
                kcal += it.cal!!
            }
        }
        return kcal.roundToInt()
    }
}

class TodayHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val alram: TextView = itemView.findViewById(R.id.alram_today)
    val image = itemView.findViewById<ImageView>(R.id.image_today)
    val totalKcal = itemView.findViewById<TextView>(R.id.total_today)
    var c1: View = itemView.findViewById(R.id.imageView5)
    val menu = itemView.findViewById<TextView>(R.id.breafast_today)

}