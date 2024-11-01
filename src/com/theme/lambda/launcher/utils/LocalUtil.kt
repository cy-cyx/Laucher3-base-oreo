package com.theme.lambda.launcher.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.lambda.common.http.Preference
import java.util.Locale

object LocalUtil {

    // 维度(默认美国)
    var lat = -1.0

    // 经度(默认美国)
    var lon = -1.0

    private val lManager: LocationManager by lazy {
        CommonUtil.appContext!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private var locationProvider: String? = null

    var keyLastLat: Float by Preference(SpKey.keyLastLat, -1f)
    var keyLastLon: Float by Preference(SpKey.keyLastLon, -1f)

    fun init() {
        val lastLat = keyLastLat
        val lastLon = keyLastLon

        if (lastLat != -1f && lastLon != -1f) {
            lat = lastLat.toDouble()
            lon = lastLon.toDouble()
        } else {
            when (Locale.getDefault().country.toUpperCase()) {
                "CN" -> {
                    // 北京
                    lat = 23.1301964
                    lon = 113.2592945
                }

                "RU" -> {
                    // 俄罗斯
                    lat = 55.63229
                    lon = 37.75077
                }

                else -> {
                    lat = 42.6511674
                    lon = -73.754968
                }
            }
        }

    }

    fun getLocal(listener: ((Double, Double) -> Unit)? = null) {

        if (!checkSelfPermission(CommonUtil.appContext!!)) {
            listener?.invoke(lat, lon)
            return
        }

        val providers = lManager.getProviders(true)
        when {
            providers.contains(LocationManager.NETWORK_PROVIDER) -> { // 网络定位
                locationProvider = LocationManager.NETWORK_PROVIDER
            }

            else -> {
                listener?.invoke(lat, lon)
            }
        }
        locationProvider?.let {
            val location = lManager.getLastKnownLocation(locationProvider!!)
            if (location == null) {
                lManager.requestLocationUpdates(
                    locationProvider!!, 0L, 0F,
                    object : LocationListener {
                        override fun onLocationChanged(p0: Location) {
                            location?.latitude?.let {
                                lat = it
                                keyLastLat = it.toFloat()
                            }
                            location?.longitude?.let {
                                lon = it
                                keyLastLon = it.toFloat()
                            }
                            listener?.invoke(lat, lon)
                            lManager.removeUpdates(this)
                        }
                    }
                )

            } else {
                lat = location.latitude
                lon = location.longitude
                listener?.invoke(lat, lon)
            }
        }
    }

    private fun checkSelfPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    }
}

