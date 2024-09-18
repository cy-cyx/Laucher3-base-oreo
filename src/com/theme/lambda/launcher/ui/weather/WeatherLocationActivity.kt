package com.theme.lambda.launcher.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import com.android.launcher3.databinding.ActivityWeatherLocationBinding
import com.theme.lambda.launcher.base.BaseActivity

class WeatherLocationActivity : BaseActivity<ActivityWeatherLocationBinding>() {
    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityWeatherLocationBinding {
        return ActivityWeatherLocationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        viewBinding.ivBack.setOnClickListener {
            finish()
        }
        viewBinding.clAutoLocation.setOnClickListener {  }
    }
}