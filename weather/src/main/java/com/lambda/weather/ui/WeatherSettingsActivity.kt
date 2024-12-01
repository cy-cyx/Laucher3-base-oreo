package com.lambda.weather.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import com.lambda.common.base.BaseActivity
import com.lambda.common.utils.StatusBarUtil
import com.lambda.weather.databinding.ActivityWeatherSettingsBinding

class WeatherSettingsActivity : BaseActivity<ActivityWeatherSettingsBinding>() {
    private val weatherLocationContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            setResult(RESULT_OK, it.data)
            finish()
        }

    private val temperatureUnitContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            finish()
        }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityWeatherSettingsBinding {
        return ActivityWeatherSettingsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        initView()
    }

    private fun initView() {
        viewBinding.ivBack.setOnClickListener {
            finish()
        }
        viewBinding.clWeatherLocation.setOnClickListener {
            weatherLocationContracts.launch(Intent(this, WeatherLocationActivity::class.java))
        }
        viewBinding.clTemperatureUnit.setOnClickListener {
            temperatureUnitContracts.launch(Intent(this, TemperatureUnitActivity::class.java))
        }
    }
}