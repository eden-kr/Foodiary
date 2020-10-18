package com.example.foodiary.Contract

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.foodiary.Repository.Local.FoodDTO
import com.example.foodiary.Repository.Local.UserDTO
import com.github.mikephil.charting.data.PieEntry
import com.kakao.usermgmt.response.model.User

class TodayContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun setAdapter(data : UserDTO, list : ArrayList<FoodDTO>)
        fun setChart(list : ArrayList<PieEntry>)
        fun update()
        fun add()
        fun replace()
    }
    interface Presenter{
        fun attachView(view : View)
        fun detachView()
        fun getData(context : Context, date: String)
        fun getPieEntry(context: Context, date : String)
        fun dispose()
        fun replace(fragmentManager: FragmentManager, view: Fragment)
    }
}