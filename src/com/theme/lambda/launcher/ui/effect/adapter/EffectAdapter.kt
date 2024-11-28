package com.theme.lambda.launcher.ui.effect.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.launcher3.R
import com.android.launcher3.databinding.ItemEffectBinding
import com.android.launcher3.effect.TransitionEffect

class EffectAdapter : RecyclerView.Adapter<ViewHolder>() {

    var effectInfos = arrayListOf(
        EffectInfo(
            0,
            TransitionEffect.TRANSITION_EFFECT_NONE,
            R.string.text_default,
            R.drawable.ic_effect_none
        ),
        EffectInfo(
            1,
            TransitionEffect.TRANSITION_EFFECT_ZOOM_IN,
            R.string.zoom_in,
            R.drawable.ic_effect_1
        ),
        EffectInfo(
            2,
            TransitionEffect.TRANSITION_EFFECT_ZOOM_OUT,
            R.string.zoom_out,
            R.drawable.ic_effect_2
        ),
        EffectInfo(
            3,
            TransitionEffect.TRANSITION_EFFECT_ROTATE_UP,
            R.string.rotate_up,
            R.drawable.ic_effect_3
        ),
        EffectInfo(
            4,
            TransitionEffect.TRANSITION_EFFECT_ROTATE_DOWN,
            R.string.rotate_down,
            R.drawable.ic_effect_4
        ),
        EffectInfo(
            5,
            TransitionEffect.TRANSITION_EFFECT_CUBE_IN,
            R.string.cube_in,
            R.drawable.ic_effect_5
        ),
        EffectInfo(
            6,
            TransitionEffect.TRANSITION_EFFECT_CUBE_OUT,
            R.string.cube_out,
            R.drawable.ic_effect_6
        ),
        EffectInfo(
            7,
            TransitionEffect.TRANSITION_EFFECT_STACK,
            R.string.stack,
            R.drawable.ic_effect_7
        ),
        EffectInfo(
            8,
            TransitionEffect.TRANSITION_EFFECT_ACCORDION,
            R.string.accordion,
            R.drawable.ic_effect_8
        ),
        EffectInfo(
            9,
            TransitionEffect.TRANSITION_EFFECT_FLIP,
            R.string.flip,
            R.drawable.ic_effect_9
        ),
        EffectInfo(
            10,
            TransitionEffect.TRANSITION_EFFECT_CYLINDER_IN,
            R.string.cylinder_in,
            R.drawable.ic_effect_10
        ),
        EffectInfo(
            11,
            TransitionEffect.TRANSITION_EFFECT_CYLINDER_OUT,
            R.string.cylinder_out,
            R.drawable.ic_effect_11
        ),
        EffectInfo(
            12,
            TransitionEffect.TRANSITION_EFFECT_CROSS_FADE,
            R.string.cross_fade,
            R.drawable.ic_effect_12
        ),
        EffectInfo(
            13,
            TransitionEffect.TRANSITION_EFFECT_OVERVIEW,
            R.string.overview,
            R.drawable.ic_effect_13
        ),
        EffectInfo(
            14,
            TransitionEffect.TRANSITION_EFFECT_OVERVIEW_SCALE,
            R.string.overview_scale,
            R.drawable.ic_effect_14
        ),
        EffectInfo(
            15,
            TransitionEffect.TRANSITION_EFFECT_PAGE,
            R.string.page,
            R.drawable.ic_effect_15
        ),
        EffectInfo(
            16,
            TransitionEffect.TRANSITION_EFFECT_WINDMILL_UP,
            R.string.windmill_up,
            R.drawable.ic_effect_16
        ),
        EffectInfo(
            17,
            TransitionEffect.TRANSITION_EFFECT_WINDMILL_DOWN,
            R.string.windmill_down,
            R.drawable.ic_effect_17
        )
    )

    var effectId = TransitionEffect.TRANSITION_EFFECT_NONE

    @SuppressLint("NotifyDataSetChanged")
    fun setCurSelectEffectId(id: Int) {
        effectId = id
        notifyDataSetChanged()
    }

    var clickEffectListen: ((EffectInfo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return EffectViewHolder(ItemEffectBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
            layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, com.lambda.common.utils.CommonUtil.dp2px(60f))
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

    inner class EffectViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var viewBinding = ItemEffectBinding.bind(view)

        fun bind(info: EffectInfo) {
            viewBinding.textId.setText(info.effectString)
            if (effectId == info.effectId) {
                viewBinding.selectIv.setImageResource(R.drawable.ic_select)
            } else {
                viewBinding.selectIv.setImageResource(R.drawable.ic_select_no)
            }
        }
    }
}