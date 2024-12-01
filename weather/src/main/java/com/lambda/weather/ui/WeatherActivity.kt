package com.lambda.weather.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.lambda.common.base.BaseActivity
import com.lambda.common.utils.StatusBarUtil
import com.lambda.weather.databinding.ActivityWeatherBinding

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
        StatusBarUtil.transparencyBar(this)
    }
}