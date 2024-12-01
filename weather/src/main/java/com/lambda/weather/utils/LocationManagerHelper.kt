package com.lambdaweather.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import java.util.*

/**
 * LocationManagerHelper2.kt
 * <p>
 * 类的描述: 定位辅助类
 * 创建时间: 2020/6/24 14:48
 * 修改备注: 6.0以上需要动态申请权限
 *   <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 *   <uses-permission android:name="android.permission..ACCESS_FINE_LOCATION" />
 */

class LocationManagerHelper private constructor() {
    companion object {
        val instance by lazy { LocationManagerHelper() }
    }

    private val TIMEOUT_MILLIS = 4000L // 设置超时时间为30秒
    private var lManager: LocationManager? = null
    private var locationProvider: String? = null
    private var locationReceived = false
    private var timeoutHandler: Handler? = null

    private fun cancelTimeout() {
        // 取消超时处理
        timeoutHandler?.removeMessages(0)
    }

    @SuppressLint("ServiceCast")
    fun build(context: Context, listener: (Location?, MutableList<Address>?) -> Unit) {
        //获取LocationManager
        lManager = (context.getSystemService(Context.LOCATION_SERVICE)
                as LocationManager?)?.also { manager ->
            val providers = manager.getProviders(true)
            when {
                providers.contains(LocationManager.NETWORK_PROVIDER) -> { // 网络定位
                    Log.d("TAG", "如果是网络定位")
                    locationProvider = LocationManager.NETWORK_PROVIDER
                }
                providers.contains(LocationManager.GPS_PROVIDER) -> { // GPS 定位
                    Log.d("TAG", "如果是GPS定位")
                    locationProvider = LocationManager.GPS_PROVIDER
                }
                providers.contains(LocationManager.PASSIVE_PROVIDER) -> {
                    Log.d("TAG", "如果是passive定位")
                    locationProvider = LocationManager.PASSIVE_PROVIDER
                }
                else -> {
                    Log.d("TAG", "没有可用的位置提供器")
                    listener.invoke(null, null)
                }
            }
            locationProvider?.let {
                if (checkSelfPermission(context)) return

                //3.获取上次的位置，一般第一次运行，此值为null

                if (manager.getLastKnownLocation(locationProvider!!) == null) {
                    // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
                    Log.d("TAG", "定位")
                    manager.requestLocationUpdates(
                        locationProvider!!, 0L, 0F,
                        getListener(context, listener)
                    )
                    // 设置超时处理
                    timeoutHandler = Handler(Looper.getMainLooper()) {
                        // 如果超时仍未收到定位信息，执行超时处理
                        if (!locationReceived) {
                            listener.invoke(null, null)
                        }
                        true
                    }
                    timeoutHandler?.sendEmptyMessageDelayed(0, TIMEOUT_MILLIS)
                } else {
                    Log.d("TAG", "定位 旧")
                    val location = manager.getLastKnownLocation(locationProvider!!)
                    getAddress(context,location){
                        listener.invoke(location,it)
                    }
                }
            }
        }
    }



    private fun getListener(
        context: Context, listener: (Location?,MutableList<Address>?) -> Unit
    ): LocationListener {
        return object : LocationListener {
            override fun onLocationChanged(p0: Location) {
                locationReceived = true
                cancelTimeout()
                p0.accuracy //精确度
                Log.d("TAG", "location:" + p0.toString())
                getAddress(context, p0) {
                    listener.invoke(p0, it)
                }

                lManager?.removeUpdates(this)
                lManager = null
            }

            override fun onProviderEnabled(provider: String) {
                Log.d("TAG", "location:onProviderEnabled")
            }

            override fun onProviderDisabled(provider: String) {
                Log.d("TAG", "location:onProviderDisabled")
            }

        }
    }

     fun checkSelfPermission(context: Context): Boolean {
        // 需要检查权限,否则编译报错,想抽取成方法都不行,还是会报错。只能这样重复 code 了。
        return ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
    }

    /**
     * 使用Android 的API 获取城市
     * Address[addressLines=[0:"福建省厦门市思明区台南路"],feature=null,admin=福建省,sub-admin=莲前街道,locality=厦门市,thoroughfare=台南路,postalCode=null,
     * countryCode=CN,countryName=中国,hasLatitude=true,latitude=24.490153327372084,hasLongitude=true,longitude=118.20273875866398,phone=null,url=null,extras=null]
     */
    private fun getAddress(
        context: Context, location: Location?,
        listener: (MutableList<Address>?) -> Unit = {}
    ) {
        var result: MutableList<Address>? = null
//        try {
//            if (location != null) {
//                val gc = Geocoder(context, Locale.getDefault())
//                result = gc.getFromLocation(location.latitude, location.longitude, 1)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
        listener.invoke(result)
    }

    fun requestLocalPermission(activity: Activity, requestCode: Int) {
        val permissionsO = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )


        //Android 10及以上申请定位权限
        val permissionsQ = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )

        var permissions = permissionsO

        if (Build.VERSION.SDK_INT >= 29) {
            permissions = permissionsQ
        }
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }
}