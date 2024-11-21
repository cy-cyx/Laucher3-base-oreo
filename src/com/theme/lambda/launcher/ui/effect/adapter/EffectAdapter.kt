package com.theme.lambda.launcher.ui.effect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.launcher3.databinding.ItemEffectBinding
import com.android.launcher3.effect.TransitionEffect
import com.theme.lambda.launcher.utils.CommonUtil

class EffectAdapter : RecyclerView.Adapter<ViewHolder>() {

    var effectInfos = arrayListOf(
        EffectInfo(0, TransitionEffect.TRANSITION_EFFECT_NONE, "no effect"),
        EffectInfo(1, TransitionEffect.TRANSITION_EFFECT_ZOOM_IN, "effect 1 (zoom in)"),
        EffectInfo(2, TransitionEffect.TRANSITION_EFFECT_ZOOM_OUT, "effect2 (zoom out)"),
        EffectInfo(3, TransitionEffect.TRANSITION_EFFECT_ROTATE_UP, "effect3 (rotate up)"),
        EffectInfo(4, TransitionEffect.TRANSITION_EFFECT_ROTATE_DOWN, "effect4 (rotate down)"),
        EffectInfo(5, TransitionEffect.TRANSITION_EFFECT_CUBE_IN, "effect5 (cube in)"),
        EffectInfo(6, TransitionEffect.TRANSITION_EFFECT_CUBE_OUT, "effect6 (cube out)"),
        EffectInfo(7, TransitionEffect.TRANSITION_EFFECT_STACK, "effect7 (stack)"),
        EffectInfo(8, TransitionEffect.TRANSITION_EFFECT_ACCORDION, "effect8 (accordion)"),
        EffectInfo(9, TransitionEffect.TRANSITION_EFFECT_FLIP, "effect9 (flip)"),
        EffectInfo(10, TransitionEffect.TRANSITION_EFFECT_CYLINDER_IN, "effect10 (cylinder in)"),
        EffectInfo(11, TransitionEffect.TRANSITION_EFFECT_CYLINDER_OUT, "effect11 (cylinder out)"),
        EffectInfo(12, TransitionEffect.TRANSITION_EFFECT_CROSS_FADE, "effect12 (cross fade)"),
        EffectInfo(13, TransitionEffect.TRANSITION_EFFECT_OVERVIEW, "effect13 (overview)"),
        EffectInfo(
            14,
            TransitionEffect.TRANSITION_EFFECT_OVERVIEW_SCALE,
            "effect14 (overview scale)"
        ),
        EffectInfo(15, TransitionEffect.TRANSITION_EFFECT_PAGE, "effect15 (page)"),
        EffectInfo(16, TransitionEffect.TRANSITION_EFFECT_WINDMILL_UP, "effect16 (windmill up)"),
        EffectInfo(17, TransitionEffect.TRANSITION_EFFECT_WINDMILL_DOWN, "effect17 (windmill down)")
    )

    var clickEffectListen: ((EffectInfo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return EffectViewHolder(ItemEffectBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
            layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtil.dp2px(60f))
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is EffectViewHolder) {
            holder.bind(effectInfos[position])
            holder.itemView.setOnClickListener {
                clickEffectListen?.invoke(effectInfos[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return effectInfos.size
    }

    class EffectViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var viewBinding = ItemEffectBinding.bind(view)

        fun bind(info: EffectInfo) {
            viewBinding.textId.text = info.effectString
        }
    }
}