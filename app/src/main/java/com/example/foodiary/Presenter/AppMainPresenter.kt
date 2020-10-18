package com.example.foodiary.Presenter

//import com.example.foodiary.Repository.Local.StepDAO

import android.content.Context
import android.util.Log
import com.example.foodiary.Contract.AppMainContract
import com.example.foodiary.Repository.Local.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


class AppMainPresenter : AppMainContract.Presenter {
    private var userDTO : UserDTO? = null
    private var foodDTO = arrayListOf<FoodDTO>()
    private var totalDTO : TotalDTO? = null
    private lateinit var fitnessOptions : FitnessOptions
    private lateinit var account : GoogleSignInAccount
    private val repo by lazy { AppMainRepository() }
    private val repository by lazy { StepRepository() }
    private var view : AppMainContract.View? = null

    override fun attachView(view : AppMainContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        view = null
    }

    override fun calculateStep() {
    }

    override fun getGoal(context: Context) {
        userDTO = repo.getUserData(context)
        foodDTO = repo.getFoodData(context)
        totalDTO = repo.getMyTotal(context)

        view?.setGoal(userDTO,totalDTO,foodDTO)
    }

    override fun getRecommendKCal() {
        val stdWeight = (userDTO?.height?.minus(100))?.times(0.9)      //표준체중
        val todayCal = stdWeight?.times(30)     //표준체중 * 활동지수
        if(totalDTO!=null) {
            todayCal?.roundToInt()?.let { view?.setTotalKCal(it, totalDTO) }
        }
    }

    override fun getNutrient() {
        var fat = 0.0
        var protein = 0.0
        var carbo = 0.0
        if(foodDTO.size>0) {
            foodDTO.forEach {
                fat += it.fat!!
                protein += it.protein!!
                carbo += it.carbohydrate!!
            }
        }
        Log.d("myTag","영양소 정보 -> $fat  $protein  $carbo")
        view?.setNutrient(fat,protein,carbo)
    }

    override fun calDistance(step: Float): Double {
        return step*0.1
    }

    override fun calKcal(context: Context, step: Int): Double {
        com.example.foodiary.Session.getInstance(context)
        var weight = 0
        var user = repo.getWeight(com.example.foodiary.Session.getEmail()!!)

        weight = if(user?.weight != null) {
            user.weight?.toInt()!!
        }else{ 0 }

        return weight*step/10000.0*5.5
    }

    override fun connectAPI(context: Context) {
        fitnessOptions = FitnessOptions.builder()       //init
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_DISTANCE_DELTA,FitnessOptions.ACCESS_READ)
            .build();
        account = GoogleSignIn.getAccountForExtension(context,fitnessOptions)
        view?.getPermission(fitnessOptions,account)
    }

    override fun accessGoogleFit(context: Context,account : GoogleSignInAccount)  {
        var step = 0
        var dist =  0.0
        var consume = 0.0

        val cal= Calendar.getInstance()
        cal.time = cal.time
        val now = Date()
        cal.time = now

        //end
        val end = cal.timeInMillis
        Log.d("myTag","끝나는 시간 $end")

        cal.add(Calendar.DAY_OF_YEAR,-1)
        //start
        val start = cal.timeInMillis
        Log.d("myTag","시작 시간$start")

        val stepRequest = DataReadRequest.Builder()
            .read(DataType.TYPE_STEP_COUNT_DELTA)
            .setTimeRange(start,end,TimeUnit.MILLISECONDS)
            .build()
        val distanceRequest = DataReadRequest.Builder()
            .read(DataType.TYPE_DISTANCE_DELTA)
            .setTimeRange(start,end,TimeUnit.MILLISECONDS)
            .build()

        //step
        Fitness.getHistoryClient(context, account)
            .readData(stepRequest).addOnSuccessListener { it ->
                val data = it.getDataSet(DataType.TYPE_STEP_COUNT_DELTA)
                data.dataPoints.forEach { it ->
                    it.dataType.fields.forEach { item ->
                        step += it.getValue(item).asInt()
                    }
                }
                view?.setStep(step)
                consume = calKcal(context,step)
                view?.setKcalBasedOnStep(consume)
                repo.saveNowStep(context,step,consume.roundToInt())
            }
        Fitness.getHistoryClient(context,account)
            .readData(distanceRequest)
            .addOnSuccessListener {
                val data = it.getDataSet(DataType.TYPE_DISTANCE_DELTA)
                data.dataPoints.forEach { data ->
                    data.dataType.fields.forEach { items ->
                        dist += data.getValue(items).asFloat()
                    }
                }
                dist /= 1000
                view?.setDistanceBasedOnStep(dist)          //구글핏에서 데이터 받아오기
            }
    }

    override fun getStepType(context: Context): String? {
        return repo.getStepType(context)
    }

    override fun saveStepData(context: Context, step: Int, distance: Float, consume: Int) {
      //  repo.saveNowStep(context,step,distance,consume)
    }


}