package com.theme.lambda.launcher.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.android.launcher3.databinding.ActivityTemperatureUnitBinding
import com.lambda.common.http.Preference
import com.theme.lambda.launcher.base.BaseActivity

class TemperatureUnitActivity : BaseActivity<ActivityTemperatureUnitBinding>() {
    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityTemperatureUnitBinding {
        return ActivityTemperatureUnitBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        viewBinding.ivBack.setOnClickListener { finish() }
        setTemperatureUnit()
        viewBinding.clC.setOnClickListener {
            temperatureUnit = "°C"
            setTemperatureUnit()
        }
        viewBinding.clF.setOnClickListener {
            temperatureUnit = "°F"
            setTemperatureUnit()
        }
    }

    private fun setTemperatureUnit() {
        if (temperatureUnit == "°C") {
            viewBinding.ivC.visibility = View.VISIBLE
            viewBinding.ivF.visibility = View.GONE
        } else {
            viewBinding.ivC.visibility = View.GONE
            viewBinding.ivF.visibility = View.VISIBLE
        }
    }

    companion object {
        @JvmStatic
        var temperatureUnit: String by Preference("temperature_unit", "°C")
    }
}