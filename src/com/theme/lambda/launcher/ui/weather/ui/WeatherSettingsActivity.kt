package com.theme.lambda.launcher.ui.weather.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import com.android.launcher3.databinding.ActivityWeatherSettingsBinding
import com.lambda.common.utils.utilcode.util.BarUtils
import com.theme.lambda.launcher.base.BaseActivity

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
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
        BarUtils.setStatusBarLightMode(this, true)
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