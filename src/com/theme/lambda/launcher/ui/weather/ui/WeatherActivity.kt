package com.theme.lambda.launcher.ui.weather.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.databinding.ActivityWeatherBinding
import com.lambda.common.utils.utilcode.util.BarUtils
import com.theme.lambda.launcher.base.BaseActivity

class WeatherActivity : BaseActivity<ActivityWeatherBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, WeatherActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityWeatherBinding {
        return ActivityWeatherBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
    }
}