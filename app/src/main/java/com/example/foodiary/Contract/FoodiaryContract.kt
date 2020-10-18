package com.example.foodiary.Contract

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FoodiaryContract {
    interface View{
        fun clickNavBar()
    }
    interface Presenter{
        fun replace(fragmentManager: FragmentManager,view : Fragment)
        fun setSession(activity : Activity)
        fun dispose()
    }
}