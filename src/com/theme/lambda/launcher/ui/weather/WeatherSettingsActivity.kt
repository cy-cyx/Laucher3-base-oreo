package com.theme.lambda.launcher.ui.weather

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.databinding.ActivityWeatherSettingsBinding
import com.theme.lambda.launcher.base.BaseActivity

class WeatherSettingsActivity : BaseActivity<ActivityWeatherSettingsBinding>() {
    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityWeatherSettingsBinding {
        return ActivityWeatherSettingsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        viewBinding.ivBack.setOnClickListener {
            finish()
        }
        viewBinding.clWeatherLocation.setOnClickListener {
            startActivity(Intent(this, WeatherLocationActivity::class.java))
        }
        viewBinding.clTemperatureUnit.setOnClickListener {
            startActivity(Intent(this, TemperatureUnitActivity::class.java))
        }
    }
}