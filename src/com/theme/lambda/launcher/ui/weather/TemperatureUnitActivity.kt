package com.theme.lambda.launcher.ui.weather

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.android.launcher3.databinding.ActivityTemperatureUnitBinding
import com.lambda.common.utils.utilcode.util.BarUtils
import com.lambdaweather.utils.SharedPreferencesManager
import com.theme.lambda.launcher.base.BaseActivity

class TemperatureUnitActivity : BaseActivity<ActivityTemperatureUnitBinding>() {


    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityTemperatureUnitBinding {
        return ActivityTemperatureUnitBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
        BarUtils.setStatusBarLightMode(this, true)
        initView()
    }

    private fun initView() {
        viewBinding.ivBack.setOnClickListener { finish() }
        setTemperatureUnit()
        viewBinding.clC.setOnClickListener {
            SharedPreferencesManager.getInstance(this)
                .putString(
                    "tempUnit", getString(
                        com.lambdaweather.R.string.temp_unit
                    )
                )
            setTemperatureUnit()
        }
        viewBinding.clF.setOnClickListener {
            SharedPreferencesManager.getInstance(this)
                .putString(
                    "tempUnit", getString(
                        com.lambdaweather.R.string.temp_unit_f
                    )
                )
            setTemperatureUnit()
        }
    }

    private fun setTemperatureUnit() {
        if ((SharedPreferencesManager.getInstance(this).getString(
                "tempUnit",
                getString(com.lambdaweather.R.string.temp_unit)
            )) == getString(com.lambdaweather.R.string.temp_unit)
        ) {
            viewBinding.ivC.visibility = View.VISIBLE
            viewBinding.ivF.visibility = View.GONE
        } else {
            viewBinding.ivC.visibility = View.GONE
            viewBinding.ivF.visibility = View.VISIBLE
        }
    }
}