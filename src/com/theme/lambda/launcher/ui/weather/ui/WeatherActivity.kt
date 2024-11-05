package com.theme.lambda.launcher.ui.weather.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.databinding.ActivityWeatherBinding
import com.lambda.common.utils.utilcode.util.BarUtils
import com.theme.lambda.launcher.base.BaseActivity

class WeatherActivity : BaseActivity<ActivityWeatherBinding>() {
    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityWeatherBinding {
        return ActivityWeatherBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
    }
}