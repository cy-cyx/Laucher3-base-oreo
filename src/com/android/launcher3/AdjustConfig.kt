package com.android.launcher3

import com.android.launcher3.effect.TransitionEffect
import com.theme.lambda.launcher.utils.SpKey
import com.theme.lambda.launcher.utils.getSpInt
import com.theme.lambda.launcher.utils.putSpInt

// 相关调整参数
object AdjustConfig {

    // 切换特效

    fun setEffectId(id: Int) {
        SpKey.keyEffectId.putSpInt(id)
    }

    @JvmStatic
    fun getEffectId(): Int {
        return SpKey.keyEffectId.getSpInt(TransitionEffect.TRANSITION_EFFECT_NONE)
    }
}