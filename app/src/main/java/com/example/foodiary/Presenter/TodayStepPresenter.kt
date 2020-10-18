package com.example.foodiary.Presenter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.foodiary.Contract.TodayStepContract
import com.example.foodiary.Repository.Local.StepRepository
import com.example.foodiary.Session
import com.skt.Tmap.TMapPoint
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TodayStepPresenter : TodayStepContract.Presenter {
    private var view: TodayStepContract.View? = null
    private val REQUEST_LOCATION_PERMISSION = 2000
    private val repo by lazy { StepRepository() }
    private val composit = CompositeDisposable()
    override fun attachView(view: TodayStepContract.View) {
        this.view = view
        view.setPresenter(this)
    }

    override fun detachView() {
        this.view
    }

    override fun getStep(id: String, date: String) {
        val now = repo.getStepData(id, date)
        Log.d("myTag", "step -> ${now?.step}")

        if (now?.step != null && now.consumeCal !=null && now.distance!=null) {
            Log.d("myTag", "step is not empty")
            view?.setData(now.step!!, now.consumeCal!!, now.distance!!)
        }
    }

    override fun getCurrentPlace(context: Context, date: String) {
        Session.getInstance(context)
        val list = arrayListOf<TMapPoint>()
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), REQUEST_LOCATION_PERMISSION);
        }else {

            val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (location != null) {
                view?.setNowPlace(location.latitude, location.longitude)
                list.add(TMapPoint(location.latitude, location.longitude))
            } else {
                val internetLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                view?.setNowPlace(internetLocation.latitude, internetLocation.longitude)
                list.add(TMapPoint(internetLocation.latitude, internetLocation.longitude))
            }

        }
        val gpsListener = object : LocationListener {
            override fun onLocationChanged(p0: Location?) {
                val lon = p0?.longitude
                val lat = p0?.latitude
                if (lat != null && lon!=null) {
                    repo.saveNowPlace(Session.getEmail()!!, date,lat,lon)
                    list.add(TMapPoint(lat,lon))
                    view?.nowDraw(list)
                }
                Log.d("myTag","lon -> $lon lat-> $lat")
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            }

            override fun onProviderEnabled(p0: String?) {
            }

            override fun onProviderDisabled(p0: String?) {
            }
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1F, gpsListener)
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1F, gpsListener)

    }

    override fun realmToLocal(email: String, date: String): ArrayList<TMapPoint> {
        return repo.copyGeo(email, date)
    }
}