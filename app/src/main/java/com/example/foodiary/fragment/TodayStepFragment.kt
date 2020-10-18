package com.example.foodiary.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.foodiary.Contract.TodayContract
import com.example.foodiary.Contract.TodayStepContract
import com.example.foodiary.R
import com.example.foodiary.Repository.Local.StepRepository
import com.example.foodiary.Session
import com.skt.Tmap.TMapCircle
import com.skt.Tmap.TMapPoint
import com.skt.Tmap.TMapPolyLine
import com.skt.Tmap.TMapView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import rx.Observable
import java.util.*
import kotlin.collections.ArrayList

class TodayStepFragment : Fragment(), TodayStepContract.View {
    private var presenter: TodayStepContract.Presenter? = null
    private lateinit var tmap: LinearLayout
    private lateinit var date: TextView
    private lateinit var kcal: TextView
    private lateinit var move: TextView
    private lateinit var step: TextView
    private lateinit var tmapView: TMapView
    private var getDate: String? = ""
    override fun setPresenter(presenter: TodayStepContract.Presenter) {
        this.presenter = presenter
    }

    override fun setMap() {

    }

    override fun setData(mStep: Int, mKcal: Int, distance: Float) {
        step.text = mStep.toString()
        kcal.text = mKcal.toString()
        move.text = distance.toString()
    }

    override fun makeDialog(msg: String, array: Array<String>, code: Int) {
        val dlg = AlertDialog.Builder(requireContext())
        dlg.setTitle(msg)
            .setCancelable(true)
            .setPositiveButton("확인") { p0, p1 ->
                //권한 요청
                ActivityCompat.requestPermissions(
                    context as Activity,
                    array,
                    code
                )
            }.setNegativeButton("취소") { p0, p1 ->
            }.show()
    }

    override fun drawMap() {
       val requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        Session.getInstance(requireContext())
        val list = presenter?.realmToLocal(Session.getEmail()!!, date.text.toString())!!
        if(list.isNotEmpty()) {
            val tmapLine = TMapPolyLine()
            tmapLine.lineColor = Color.BLACK
            tmapLine.lineWidth = 2F
            list.forEach {
                tmapLine.addLinePoint(it)
            }
            tmapView.addTMapPolyLine("line", tmapLine)
        }
    }

    override fun nowDraw(list: ArrayList<TMapPoint>) {
        val line = TMapPolyLine()
        line.lineColor = Color.BLACK
        line.lineWidth = 2F
        list.forEach {
            line.addLinePoint(it)
        }
        tmapView.addTMapPolyLine("line",line)
    }

    override fun setNowPlace(lat: Double, lon: Double) {
        tmapView = TMapView(requireContext())
        tmapView.setSKTMapApiKey(requireActivity().getString(R.string.tmap_api_key))
        tmap.addView(tmapView)

        tmapView.setCenterPoint(lon, lat,true)
        tmapView.setLocationPoint(lon, lat)
        //now place circle
        val now = TMapPoint(lat,lon)
        val circle = TMapCircle()
        circle.centerPoint = now
        circle.radius = 50.0
        circle.circleWidth = 2F
        circle.lineColor = Color.BLACK
        circle.areaColor = Color.GRAY
        circle.areaAlpha = 100
        tmapView.addTMapCircle("circle1",circle)
        tmapView.zoomLevel = 15

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 2000) {
            presenter?.getCurrentPlace(requireContext(), date.text.toString())
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDate = arguments?.getString("date")
        val view = inflater.inflate(R.layout.fragment_step, container, false)
        tmap = view.findViewById(R.id.tmap)
        date = view.findViewById(R.id.fStep_today)
        kcal = view.findViewById(R.id.fStep_kcal_value)
        move = view.findViewById(R.id.fStep_move_value)
        step = view.findViewById(R.id.fStep_step_value)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Session.getInstance(requireContext())
        presenter?.getCurrentPlace(requireContext(), date.text.toString())
        date.text = getDate
        presenter?.getStep(Session.getEmail()!!, date.text.toString())
        drawMap()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }
}