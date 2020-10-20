package com.example.foodiary.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.foodiary.R

class LoadingFragment : DialogFragment()  {
    companion object{
        fun getInstance() : LoadingFragment{
            val bundle = Bundle()
            val fragment = LoadingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loading,container,false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialog?.window?.setDimAmount(0F);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(STYLE_NO_FRAME,android.R.style.Theme)
    }
}