package com.example.foodiary.etc

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class Pedometer(var context: Context) : LocationListener, SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var stepCountSensor: Sensor
    private lateinit var locationManager: LocationManager
    private var isGPSCheck = false
    private var isSensorCheck = false
    private var nowLocation: Location? = null
    private var lastLocation: Location? = null
    private var nowStep = 0
    private var prevStep = 0
    private var move =0.0
    private val REQUEST_ACTIVITY_RECOGNITION = 1000
    private val REQUEST_LOCATION_PERMISSION = 2000

    fun getStep(): Int {
        return nowStep
    }
    fun getMove(): Double{
        return move
    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val check = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            val activityCheck = ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.ACTIVITY_RECOGNITION
            )
            if (check == PackageManager.PERMISSION_DENIED) {
                Log.d("myTag","fine_location")
                if(ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    makeDialog(
                        "만보기 사용을 위해서 위치 정보가 필요합니다.",
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        ), REQUEST_LOCATION_PERMISSION
                    )
                }
                else {    // 권한요청
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        ), REQUEST_LOCATION_PERMISSION)
                }
            }
            /*
            if(activityCheck == PackageManager.PERMISSION_DENIED){
                Log.d("myTag","recognition")
                if(ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, android.Manifest.permission.ACTIVITY_RECOGNITION)){
                    makeDialog("만보기 사용을 위해 센서 정보에 대한 권한이 필요합니다.", arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),REQUEST_ACTIVITY_RECOGNITION)
                }  else {    // 권한요청
                    Toast.makeText(context,"만보기 사용을 위해 센서 정보가 필요합니다.",Toast.LENGTH_SHORT).show()
                }
            }

             */
            if(check == PackageManager.PERMISSION_GRANTED){
                initSensor()
            }
        }
    }

    private fun makeDialog(msg : String, array: Array<String>,code:Int){
        val dlg = AlertDialog.Builder(context)
        dlg.setTitle(msg)
            .setCancelable(true)
            .setPositiveButton("확인") { p0, p1 ->
                //권한 요청
                ActivityCompat.requestPermissions(
                    context as Activity,
                    array,
                    code)
            }.setNegativeButton("취소") { p0, p1 ->
            }.show()
    }

    private fun initSensor() {
        if (checkGPS() && sensorCheck()) {
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_GAME)
            Log.d("myTag","센서가 등록되었습니다.")
        }
    }
     private fun sensorCheck(): Boolean {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var list = sensorManager.getSensorList(Sensor.TYPE_STEP_COUNTER)
        if(list.size != 0) isSensorCheck = true
         Log.d("myTag","${sensorManager.getSensorList(Sensor.TYPE_STEP_COUNTER)}")
        return isSensorCheck
    }

    fun reset() {
        nowLocation = null
        lastLocation = null
    }

    private fun checkGPS(): Boolean {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGPSCheck = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGPSCheck) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context, "설정에서 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
                return false
            }
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0.toFloat(),
                this
            )
        }
        return isGPSCheck
    }

    private fun getDistance(
        lastLat: Double,
        lastLng: Double,
        nowLat: Double,
        nowLng: Double
    ): Double {
        val lat1 = lastLat * Math.PI / 180.0
        val lng1 = lastLng * Math.PI / 180.0
        val lat2 = nowLat * Math.PI / 180.0
        val lng2 = nowLng * Math.PI / 180.0

        var distance = sin(lat1) * sin(lat2) + cos(lng1) * cos(lng2) * cos(lastLng - nowLng)
        distance = acos(distance)
        distance = distance * 180 / Math.PI
        distance *= 60 * 1.1515
        distance *= 1.609344 * 1000

        return distance
    }

    private fun getMyLocation(): Double {
        var distance = 0.0
        if (checkGPS()) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return 0.0
            }
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastLocation == null) {
                lastLocation = nowLocation
            }
            if (lastLocation != null && nowLocation != null) {

                var lastLat = lastLocation?.latitude
                var lastLng = lastLocation?.longitude
                var nowLat = nowLocation?.latitude
                var nowLng = nowLocation?.longitude

                distance = getDistance(lastLat!!, lastLng!!, nowLat!!, nowLng!!)
                if (distance > 0.1) {
                    lastLocation = nowLocation
                    return distance
                }
                lastLocation = nowLocation
            }
        }
        return 0.0
    }

    override fun onLocationChanged(p0: Location?) {
        nowLocation = p0
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.toFloat(), this)
    }

    override fun onProviderDisabled(p0: String?) {
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        move = getMyLocation()
        if (p0?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            if (prevStep < 1 && move > 0.1) {
                prevStep = p0.values[0] as Int
            }
            if (move > 0.1) {
                nowStep = p0.values[0] as Int - prevStep
                Log.d("myTag", "현재 걸음 수 $nowStep")
            }
        }
    }
}