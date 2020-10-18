package com.example.foodiary.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.foodiary.Contract.MemoContract
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.MemoRepository
import com.example.foodiary.Repository.Local.FoodDTO
import com.example.foodiary.Repository.Remote.FoodDataPOJO
import com.example.foodiary.etc.Decorator
import com.example.foodiary.etc.SaturdayDecorator
import com.example.foodiary.etc.SundayDecorator
import com.example.foodiary.myDate
import com.example.foodiary.myList
import com.example.foodiary.view.FoodDataActivity
import com.example.foodiary.view.MemoActivity
import com.example.foodiary.view.TodayListActivity
import com.jakewharton.rxbinding.view.RxView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_food_data.*
import kotlinx.android.synthetic.main.activity_memo_fragment.*
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList

class MemoFragment : Fragment(), MemoContract.View{
    private var presenter : MemoContract.Presenter? = null
    private lateinit var memoToday : TextView
    private lateinit var breakfast : ImageView
    private lateinit var lunch : ImageView
    private lateinit var dinner : ImageView
    private lateinit var snack : ImageView
    private lateinit var calendar : MaterialCalendarView

    private var today : Calendar = Calendar.getInstance()       //today

    private val foodImageArr = arrayListOf(R.drawable.food1,R.drawable.food2,R.drawable.food3,R.drawable.food4,R.drawable.food5,R.drawable.food6,R.drawable.food7)

    override fun onResume() {
        super.onResume()
        val intent = requireActivity().intent
        val type: String? = intent?.getStringExtra("type")
        val data = intent?.getSerializableExtra("list") as ArrayList<FoodDataPOJO>?
        if (type != null) {
            changeDataAfterInput(type,requireView())
            setView(requireView())
        }
        /*
        if(type!= null && data !=null) {
            var tot = presenter?.getTotal(data)
            changeDataAfterInput(type, requireView())
            setFoodCal(tot!!,type,requireView())
        }

         */
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myList.clear()  //init
    }
    override fun setPresenter(presenter: MemoContract.Presenter) {
        this.presenter = presenter
    }

    override fun setDate(date: Calendar) : String {
        val sdf = SimpleDateFormat("yyyy-MM-dd",Locale.KOREA)
        val day = sdf.format(date.time)
        memoToday.text = day

        return day
    }

    override fun setFoodCal(cal : Int , type: String,view : View) {
        val br = view.findViewById<TextView>(R.id.breakCal)
        val lu = view.findViewById<TextView>(R.id.lunchCal)
        val di = view.findViewById<TextView>(R.id.dinnerCal)
        val sn = view.findViewById<TextView>(R.id.snackCal)

        when(type){
            "breakfast" ->{
                br.text = "$cal kcal"
            }
            "lunch"->{
                lu.text= "$cal kcal"
            }
            "dinner" ->{
                di.text= "$cal kcal"
            }
            "snack" ->{
                sn.text = "$cal kcal"
            }
        }


    }
    override fun setRecommendCal(morning: TextView, lunch: TextView, dinner: TextView) {
        morning.text = presenter?.calculateCal("아침")
        lunch.text = presenter?.calculateCal("점심")
        dinner.text = presenter?.calculateCal("저녁")
    }

    override fun setChangeImage(image: ImageView) {
        val random = Random()
        var pos = random.nextInt(foodImageArr.size)
        image.setImageResource(foodImageArr[pos])
    }
    override fun changeDataAfterInput(type: String,view : View){
        when(type){
            "breakfast" ->{
                setChangeImage(view.findViewById(R.id.breakfast_memo))
            }
            "lunch"->{
                setChangeImage(view.findViewById(R.id.lunch_memo))
            }
            "dinner" ->{
                setChangeImage(view.findViewById(R.id.dinner_memo))
            }
            "snack" ->{
                setChangeImage(view.findViewById(R.id.snack_memo))
            }
        }
    }

    override fun getKcalFromView(case: Int) : String{
        var text = ""
        when(case){
            1->{
                text = requireView().findViewById<TextView>(R.id.breakCal).text.toString()
            }
            2->{
                text = requireView().findViewById<TextView>(R.id.lunchCal).text.toString()

            }
            3->{
                text = requireView().findViewById<TextView>(R.id.dinnerCal).text.toString()

            }
            4->{
                text = requireView().findViewById<TextView>(R.id.snackCal).text.toString()
            }

        }
        Log.d("myTag","get Kcal data -> $text")
        return text
    }

    override fun setView(view: View) {
        //Realm에서 데이터를 받아와 변경
        //date를 바꿀 때 같이 뷰도 변경
        val res : ArrayList<FoodDTO>? = presenter?.getTotal(myDate)
        res?.forEach {
            when (it.type) {
                "breakfast" -> {
                    setChangeImage(view.findViewById(R.id.breakfast_memo))
                    view.findViewById<TextView>(R.id.breakCal).text = "${it.cal} kcal"
                }
                "lunch" -> {
                    setChangeImage(view.findViewById(R.id.lunch_memo))
                    view.findViewById<TextView>(R.id.lunchCal).text = "${it.cal} kcal"
                }
                "dinner" -> {
                    setChangeImage(view.findViewById(R.id.dinner_memo))
                    view.findViewById<TextView>(R.id.dinnerCal).text = "${it.cal} kcal"
                }
                "snack" -> {
                    setChangeImage(view.findViewById(R.id.snack_memo))
                    view.findViewById<TextView>(R.id.snackCal).text = "${it.cal} kcal"
                }
            }
        }

    }
    override fun initView(view : View){
        //view init
        view.findViewById<ImageView>(R.id.breakfast_memo).setImageResource(R.drawable.memodefault)
        view.findViewById<ImageView>(R.id.lunch_memo).setImageResource(R.drawable.memodefault)
        view.findViewById<ImageView>(R.id.dinner_memo).setImageResource(R.drawable.memodefault)
        view.findViewById<ImageView>(R.id.snack_memo).setImageResource(R.drawable.memodefault)

        //cal init
        view.findViewById<TextView>(R.id.breakCal).text = "0 kcal"
        view.findViewById<TextView>(R.id.lunchCal).text = "0 kcal"
        view.findViewById<TextView>(R.id.dinnerCal).text = "0 kcal"
        view.findViewById<TextView>(R.id.snackCal).text = "0 kcal"
    }

    override fun setAnim(case: Int, view: LinearLayout) {
            var anim: Animation
            when(case){
                1->{
                    anim = AnimationUtils.loadAnimation(requireContext(),
                        R.anim.up)
                    view.animation = anim
                    view.visibility = View.VISIBLE
                    memo_background.setBackgroundColor(Color.TRANSPARENT)
                    calendar_drawer.setBackgroundColor(Color.WHITE)
                }
                2->{
                    anim = AnimationUtils.loadAnimation(requireContext(),
                        R.anim.down)
                    view.animation = anim
                    view.visibility = View.INVISIBLE
                    memo_background.setBackgroundColor(Color.WHITE)

                }
            }
    }


    override fun setCalendar() {
        val intent = Intent(requireContext(), TodayListActivity::class.java)

        //init
        calendar.addDecorators(Decorator(requireContext(),presenter?.stringToDate(requireContext())),SundayDecorator(),SaturdayDecorator())
        calendar.setOnDateChangedListener { widget, date, selected ->
            //startActivity ->
            Log.d("myTag", "calendar date -> ${date.month+1}${date.day}")
            var day = ""
            if (date.month+1 in 1..9) {
                var month = "0${date.month+1}"
                when (date.day) {
                    in 1..9 -> {
                        day = "0${date.day}"
                        intent.putExtra("date", "${date.year}-$month-$day")
                        startActivity(intent)
                    }
                    else -> {
                        intent.putExtra("date", "${date.year}-$month-${date.day}")
                        startActivity(intent)
                    }
                }
            }else{
                when (date.day) {
                    in 1..9 -> {
                        day = "0${date.day}"
                        intent.putExtra("date", "${date.year}-${date.month+1}-$day")
                        startActivity(intent)
                    }
                    else -> {
                        intent.putExtra("date", "${date.year}-${date.month+1}-${date.day}")
                        startActivity(intent)
                    }
                }
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myView = inflater.inflate(R.layout.activity_memo_fragment,container,false)

        memoToday = myView.findViewById(R.id.memo_today)
        memoToday.text = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(today.time)    //init
        calendar = myView.findViewById(R.id.myCalendar)
        breakfast = myView.findViewById(R.id.breakfast_memo)        //view init
        lunch = myView.findViewById(R.id.lunch_memo)
        dinner = myView.findViewById(R.id.dinner_memo)
        snack = myView.findViewById(R.id.snack_memo)
        setRecommendCal(myView.findViewById(R.id.rec_breakCal),myView.findViewById(R.id.rec_lunchCal),myView.findViewById(R.id.rec_dinnerCal))
        setView(myView)
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.updateTotalKcal(requireContext())
        setCalendar()
        RxView.clicks(view.findViewById(R.id.breakfast_memo))
            .subscribe {
                val intent = Intent(requireActivity(), FoodDataActivity::class.java)
                intent.putExtra("type","breakfast")
                startActivity(intent)
            }
        RxView.clicks(view.findViewById(R.id.lunch_memo))
            .subscribe {
                val intent = Intent(requireActivity(),FoodDataActivity::class.java)
                intent.putExtra("type","lunch")
                startActivity(intent)
            }
        RxView.clicks(view.findViewById(R.id.dinner_memo))
            .subscribe {
                val intent = Intent(requireActivity(),FoodDataActivity::class.java)
                intent.putExtra("type","dinner")
                startActivity(intent)
            }
        RxView.clicks(view.findViewById(R.id.snack_memo))
            .subscribe {
                val intent = Intent(requireActivity(),FoodDataActivity::class.java)
                intent.putExtra("type","snack")
                startActivity(intent)
            }

        RxView.clicks(view.findViewById(R.id.memo_calendar))
            .subscribe {
                setAnim(1,view.findViewById(R.id.calendar_drawer))
            }

        RxView.clicks(view.findViewById(R.id.create_memo))
            .subscribe {
                val intent = Intent(requireActivity(),MemoActivity::class.java)
                intent.putExtra("date", myDate)
                startActivity(intent)
            }

        RxView.clicks(view.findViewById(R.id.memo_right))
            .subscribe {
                var newDay = presenter?.calculateDate(1,today)!!
                myDate = setDate(today)
                initView(view)
                setView(view)
                presenter?.updateTotalKcal(requireContext())

            }
        RxView.clicks(view.findViewById(R.id.memo_left))
            .subscribe {
                var newDay = presenter?.calculateDate(2,today)!!
                myDate = setDate(today)
                initView(view)
                setView(view)
                presenter?.updateTotalKcal(requireContext())
            }
        RxView.clicks(view.findViewById(R.id.close_calendar))
            .subscribe {
                setAnim(2,view.findViewById(R.id.calendar_drawer))
            }

    }
    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }



}