package com.lambda.weather.ui

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lambda.common.base.BaseActivity
import com.lambda.common.utils.PermissionUtil
import com.lambda.common.utils.StatusBarUtil
import com.lambda.weather.AppViewModel
import com.lambda.weather.adapter.CityAdapter
import com.lambda.weather.databinding.ActivityWeatherLocationBinding
import com.lambdaweather.utils.LocationManagerHelper
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
        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
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
        PermissionUtil.requestRuntimePermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            object : PermissionUtil.IPermissionCallback {
                override fun nextStep() {
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

                override fun noPermission() {
                    if (isFinishing || isDestroyed) {
                        return
                    }
                    ipLocation()
                }

                override fun gotoSet(internal: Boolean) {

                }
            })
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