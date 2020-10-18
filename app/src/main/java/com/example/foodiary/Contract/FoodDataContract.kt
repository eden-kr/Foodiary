package com.example.foodiary.Contract

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.foodiary.Adapter.MyListAdapter
import com.example.foodiary.Repository.Remote.FoodDataPOJO
import java.io.File

class FoodDataContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun getSearchTopic() : String
        fun makeDialog()
        fun initFragment()
        fun returnFragmentManager() : FragmentManager
        fun setType(type : String)
        fun setCount()
        fun getCount() : Int
        fun initCount()
        fun setAdapter()
        fun setAnim(case : Int)
        fun setToolbar()
        fun setResumeType()
        fun getType() : String
        fun updateRecyclerView(intent : Intent)
        fun setLottie()
        fun moveToolbar()
        fun restoreToolbar()
    }
    interface Presenter{
        fun attachView(view : View)
        fun detachView()
        fun getFoodData(foodName : String)
        fun saveFoodData(context: Context,type : String)
        fun uriToFile(context: Context,uri: Uri)
        fun getFlaskData(context: Context,file : File)
        fun getSearchImage(context: Context)
        fun dispose()
        fun setAds(context: Context)
        fun checkListSize(list : ArrayList<FoodDataPOJO>) : Boolean
        fun replace(fragmentManager: FragmentManager,fragment : Fragment)
        fun setBroadCastReceiver(context: Context,receiver: BroadcastReceiver)
        fun saveType(editor : SharedPreferences.Editor)
        fun checkWords(word : String) : Boolean
    }
}