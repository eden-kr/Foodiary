package com.example.foodiary.Contract

import android.content.Context
import android.widget.TextView
import com.example.foodiary.Repository.Local.FoodDTO
import com.example.foodiary.Repository.Local.TotalDTO
import com.example.foodiary.Repository.Local.UserDTO
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.FitnessOptions
import java.util.ArrayList

class AppMainContract {
    interface View {
        fun setPresenter(presenter: Presenter)
        fun setDate(date : TextView, day : TextView)
        fun setGoal(data : UserDTO?,total : TotalDTO?,list : ArrayList<FoodDTO>)      //칼로리 circle bar 설정
        fun setTotalKCal(recKcal : Int,total : TotalDTO?)
        fun setNutrient(myProtein: Double, myFat: Double, myCarbo: Double)
        fun attachPedometer()
        fun setKcalBasedOnStep(kcal : Double)
        fun setDistanceBasedOnStep(step: Double)
        fun setStep(step : Int)
        fun getPermission(fitnessOptions : FitnessOptions,account : GoogleSignInAccount)
    }
    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun calculateStep()
        fun getGoal(context: Context)
        fun getRecommendKCal()
        fun getNutrient()
        fun calDistance(step: Float): Double
        fun calKcal(context: Context, step: Int): Double
        fun connectAPI(context: Context)
        fun accessGoogleFit(context: Context,account : GoogleSignInAccount)
        fun getStepType(context: Context): String?
        fun saveStepData(context: Context,step : Int,distance : Float, consume : Int)
    }
}