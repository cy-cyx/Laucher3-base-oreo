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

        viewBinding.effectRv.apply {
            layoutManager = LinearLayoutManager(this@EffectActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = effectAdapter
        }

        effectAdapter.clickEffectListen = {
            AdjustConfig.setEffectId(it.effectId)
            Toast.makeText(this, "别怀疑你已经换了效果，退回到首页滑动看看", Toast.LENGTH_SHORT)
                .show()
        }
    }
}