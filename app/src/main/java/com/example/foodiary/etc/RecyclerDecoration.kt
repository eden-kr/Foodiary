package com.example.foodiary.etc

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

//리사이클러뷰 간격 조절
class RecyclerDecoration(divHeight : Int) : RecyclerView.ItemDecoration() {
    private var divHeight : Int = divHeight
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = divHeight
        outRect.bottom = divHeight
    }

}