package com.lambda.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.lambdaweather.utils.SharedPreferencesManager
import com.lambda.common.base.BaseActivity
import com.lambda.common.utils.StatusBarUtil
import com.lambda.weather.R
import com.lambda.weather.databinding.ActivityTemperatureUnitBinding

class TemperatureUnitActivity : BaseActivity<ActivityTemperatureUnitBinding>() {


    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityTemperatureUnitBinding {
        return ActivityTemperatureUnitBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        initView()
    }

    private fun initView() {
        viewBinding.ivBack.setOnClickListener { finish() }
        setTemperatureUnit()
        viewBinding.clC.setOnClickListener {
            SharedPreferencesManager.getInstance(this)
                .putString(
                    "tempUnit", getString(
                        R.string.temp_unit
                    )
                )
            setTemperatureUnit()
        }
        viewBinding.clF.setOnClickListener {
            SharedPreferencesManager.getInstance(this)
                .putString(
                    "tempUnit", getString(
                        R.string.temp_unit_f
                    )
                )
            setTemperatureUnit()
        }
    }

    private fun setTemperatureUnit() {
        if ((SharedPreferencesManager.getInstance(this).getString(
                "tempUnit",
                getString(R.string.temp_unit)
            )) == getString(R.string.temp_unit)
        ) {
            viewBinding.ivC.visibility = View.VISIBLE
            viewBinding.ivF.visibility = View.GONE
        } else {
            viewBinding.ivC.visibility = View.GONE
            viewBinding.ivF.visibility = View.VISIBLE
        }
    }
}