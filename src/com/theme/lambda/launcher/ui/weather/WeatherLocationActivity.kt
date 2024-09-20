package com.theme.lambda.launcher.ui.weather

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.launcher3.databinding.ActivityWeatherLocationBinding
import com.lambda.common.utils.utilcode.util.BarUtils
import com.lambda.common.utils.utilcode.util.PermissionUtils
import com.lambdaweather.AppViewModel
import com.lambdaweather.utils.LocationManagerHelper
import com.theme.lambda.launcher.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherLocationActivity : BaseActivity<ActivityWeatherLocationBinding>() {
    private val viewModel: AppViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()
    private val cityAdapter: CityAdapter by lazy { CityAdapter() }
    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityWeatherLocationBinding {
        return ActivityWeatherLocationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
        BarUtils.setStatusBarLightMode(this, true)
        initView()
    }

    private fun initView() {
        searchViewModel.geo.observe(this) {
            showRv()
            cityAdapter.setList(it.data)
        }
        viewBinding.ivBack.setOnClickListener {
            finish()
        }
        viewBinding.clAutoLocation.setOnClickListener {
            requestLocation()
        }
        viewBinding.ivSearch.setOnClickListener {
            if (viewBinding.et.text.toString().isEmpty()) {
                return@setOnClickListener
            }
            searchViewModel.getGeo(null, null, viewBinding.et.text.toString())
        }
        viewBinding.rv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewBinding.rv.adapter = cityAdapter
        cityAdapter.setOnItemClickListener { _, _, position ->
            finishWithResult(Location(cityAdapter.data[position].name).apply {
                latitude = cityAdapter.data[position].lat ?: 0.0
                longitude = cityAdapter.data[position].lon ?: 0.0
            })
        }
    }

    private fun requestLocation() {
        PermissionUtils.permission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .callback(object : PermissionUtils.SimpleCallback {
                override fun onGranted() {
                    if (isFinishing || isDestroyed) {
                        return
                    }
                    LocationManagerHelper.instance.build(this@WeatherLocationActivity) { location, _ ->
                        if (location == null) {
                            ipLocation()
                        } else {
                            finishWithResult(location)
                        }
                    }
                }

                override fun onDenied() {
                    if (isFinishing || isDestroyed) {
                        return
                    }
                    ipLocation()
                }
            }).request()
    }

    private fun ipLocation() {
        viewModel.ipModel.observe(this) {
            if (it.appException != null) {
                showNetworkError()
                return@observe
            }
            finishWithResult(Location(it.data?.city).apply {
                latitude = it?.data?.latitude ?: 0.0
                longitude = it?.data?.longitude ?: 0.0
            })
        }
        viewModel.getIp()
    }

    private fun showNetworkError() {
        viewBinding.clAutoLocation.visibility = View.GONE
        viewBinding.clNetworkError.visibility = View.VISIBLE
        viewBinding.rv.visibility = View.GONE
    }

    private fun showRv() {
        viewBinding.clAutoLocation.visibility = View.GONE
        viewBinding.clNetworkError.visibility = View.GONE
        viewBinding.rv.visibility = View.VISIBLE
    }

    private fun finishWithResult(location: Location) {
        setResult(RESULT_OK, Intent().apply {
            putExtra("location", location)
        })
        finish()
    }
}