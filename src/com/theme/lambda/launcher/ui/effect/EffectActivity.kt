package com.theme.lambda.launcher.ui.effect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.launcher3.AdjustConfig
import com.android.launcher3.databinding.ActivityEffectBinding
import com.theme.lambda.launcher.base.BaseActivity
import com.theme.lambda.launcher.ui.effect.adapter.EffectAdapter
import com.theme.lambda.launcher.utils.StatusBarUtil
import com.theme.lambda.launcher.utils.marginStatusBarHeight

class EffectActivity : BaseActivity<ActivityEffectBinding>() {

    companion object {

        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, EffectActivity::class.java))
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityEffectBinding {
        return ActivityEffectBinding.inflate(layoutInflater)
    }

    private var effectAdapter = EffectAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()

        viewBinding.backIv.setOnClickListener {
            finish()
        }
        viewBinding.okIv.setOnClickListener {
            AdjustConfig.setEffectId(effectAdapter.effectId)
            finish()
        }

        viewBinding.effectRv.apply {
            layoutManager = LinearLayoutManager(this@EffectActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = effectAdapter
        }

        effectAdapter.clickEffectListen = {
            effectAdapter.setCurSelectEffectId(it.effectId)
            viewBinding.previewIv.setImageResource(it.res)
        }

        effectAdapter.setCurSelectEffectId(AdjustConfig.getEffectId())
        effectAdapter.effectInfos.find { it.effectId == AdjustConfig.getEffectId() }?.let {
            viewBinding.previewIv.setImageResource(it.res)
        }
    }
}