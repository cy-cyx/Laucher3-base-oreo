package com.theme.lambda.launcher.ui.weather

import android.view.LayoutInflater
import com.android.launcher3.databinding.ActivityWeatherBinding
import com.theme.lambda.launcher.base.BaseActivity

class WeatherActivity : BaseActivity<ActivityWeatherBinding>() {
    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityWeatherBinding {
        return ActivityWeatherBinding.inflate(layoutInflater)
    }
}