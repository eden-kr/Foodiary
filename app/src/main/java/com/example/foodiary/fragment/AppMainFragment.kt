package com.example.foodiary.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar
import com.dinuscxj.progressbar.CircleProgressBar
import com.example.foodiary.Contract.AppMainContract
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.FoodDTO
import com.example.foodiary.Repository.Local.TotalDTO
import com.example.foodiary.Repository.Local.UserDTO
import com.example.foodiary.Session
import com.example.foodiary.etc.Pedometer
import com.example.foodiary.view.StepActivity
import com.example.foodiary.view.TodayListActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.jakewharton.rxbinding.view.RxView
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class AppMainFragment : Fragment(), AppMainContract.View {
    private val today: String = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())
    private val dayOfWeek = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date())
    private lateinit var presenter: AppMainContract.Presenter

    private lateinit var myBar: CircleProgressBar
    private lateinit var protein: TextRoundCornerProgressBar
    private lateinit var carbo: TextRoundCornerProgressBar
    private lateinit var fat: TextRoundCornerProgressBar
    private lateinit var step: TextView
    private lateinit var move: TextView
    private lateinit var consumeKcal: TextView
    private lateinit var totalKcal: TextRoundCornerProgressBar
    private lateinit var totalKcalText: TextView
    private lateinit var pedometer: Pedometer
    private var todayStep = 0
    private val GOOGLE_FIT_PERMISSION_REQUEST = 5000

    override fun onResume() {
        super.onResume()
        presenter?.getGoal(requireContext())
        presenter?.getRecommendKCal()
        presenter?.getNutrient()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_main_fragment, container, false)
        myBar = view.findViewById(R.id.circlebar)
        protein = view.findViewById(R.id.protein)
        carbo = view.findViewById(R.id.carbohydrate)
        fat = view.findViewById(R.id.fat)
        step = view.findViewById(R.id.step)
        move = view.findViewById(R.id.movingDistance)
        consumeKcal = view.findViewById(R.id.consumeCal)
        totalKcal = view.findViewById(R.id.totalCal)
        totalKcalText = view.findViewById(R.id.totalCalText)
        val today = view.findViewById<TextView>(R.id.today)
        val day = view.findViewById<TextView>(R.id.today_yoil)
        val connectStep = view.findViewById<Button>(R.id.connect_step)

        setDate(today, day)

        presenter.getGoal(requireContext())
        presenter.getRecommendKCal()
        presenter.getNutrient()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //만보기 설정
        Session.getInstance(requireContext())
        val type: String? = presenter.getStepType(requireContext())

        if (type != null) {
            view.findViewById<LinearLayout>(R.id.prev_step_data).visibility = View.INVISIBLE
            view.findViewById<Button>(R.id.connect_step).isClickable = false
            view.findViewById<LinearLayout>(R.id.step_data).visibility = View.VISIBLE
            when (type) {
                "googleFit" -> {            //googlefit api
                    presenter.connectAPI(requireContext())
                }
                "foodiary" -> {             //자체 측정기
                    attachPedometer()
                    setStep(pedometer.getStep())
                    //set step ui
                    move.text = pedometer.getMove().toString()
                }
            }
        }
        RxView.clicks(view.findViewById(R.id.setting))
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                startActivity(Intent(requireContext(), StepActivity::class.java))
            }
        RxView.clicks(view.findViewById(R.id.next))
            .subscribe {
                val intent = Intent(requireContext(), TodayListActivity::class.java)
                intent.putExtra("date", today)
                startActivity(intent)
            }
        RxView.clicks(view.findViewById(R.id.connect_step))
            .subscribe {
                startActivity(Intent(requireContext(), StepActivity::class.java))
            }

    }

    override fun getPermission(fitnessOptions: FitnessOptions, account: GoogleSignInAccount) {
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                this@AppMainFragment,
                GOOGLE_FIT_PERMISSION_REQUEST,
                account,
                fitnessOptions
            )
        } else {
            Fitness.getRecordingClient(requireContext(), account)
                .subscribe(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener {
                    if(isAdded) {
                        presenter.accessGoogleFit(requireContext(), account)
                   }
                }.addOnFailureListener {
                    Log.d("myTag", "subscribe is failed ${it.printStackTrace()}")
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSION_REQUEST) {
                presenter.connectAPI(requireContext())
            }
        }

    }

    override fun setPresenter(presenter: AppMainContract.Presenter) {
        this.presenter = presenter
    }

    override fun setDate(date: TextView, day: TextView) {
        date.text = today
        day.text = dayOfWeek
    }

    override fun setGoal(data: UserDTO?, total: TotalDTO?, list: ArrayList<FoodDTO>) {
        if (data?.maxcal != null && total?.totalCal != null) {
            myBar.max = data.maxcal!!
            myBar.progress = total?.totalCal!!
        } else {
            myBar.max = 0
            myBar.progress = 0
        }
    }

    override fun setTotalKCal(recKcal: Int, total: TotalDTO?) {
        totalKcal.max = recKcal.toFloat()
        totalKcal.progress = total?.totalCal?.toFloat()!!
        totalKcal.progressText = "${total.totalCal} kcal"
        //totalKcalText.text = "${total.totalCal} Kcal"
    }

    override fun setNutrient(myProtein: Double, myFat: Double, myCarbo: Double) {
        protein.progress = myProtein.toFloat()
        fat.progress = myFat.toFloat()
        carbo.progress = myCarbo.toFloat()
        protein.progressText = "${((myProtein / 150) * 100).roundToInt()} %"
        fat.progressText = "${((myFat / 51) * 100).roundToInt()} %"
        carbo.progressText = "${((myCarbo / 300) * 100).roundToInt()} %"

    }

    override fun attachPedometer() {
        pedometer = Pedometer(requireContext())
        pedometer.requestPermission()
    }

    override fun setKcalBasedOnStep(kcal : Double) {
        consumeKcal.text = kcal.toString()
    }

    override fun setDistanceBasedOnStep(distance: Double) {
        move.text = String.format("%.2f",distance)
    }

    override fun setStep(s: Int) {
        step.text = s.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}
