package com.example.foodiary.fragment

import androidx.fragment.app.Fragment
import com.example.foodiary.Contract.TodayMemoContarct

class TodayMemoFragment : Fragment(),TodayMemoContarct.View {
    private var presenter : TodayMemoContarct.Presenter? = null
    override fun setPresenter(presenter: TodayMemoContarct.Presenter) {
        this.presenter = presenter
    }
}