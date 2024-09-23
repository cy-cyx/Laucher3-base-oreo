package com.theme.lambda.launcher.ui.me.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ItemThemeBinding
import com.theme.lambda.launcher.data.model.ThemeRes
import com.theme.lambda.launcher.utils.CommonUtil

class MeThemeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = ArrayList<ThemeRes>()
    var clickItemListen: ((ThemeRes) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun upData(d: ArrayList<ThemeRes>) {
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MeThemeViewHolder(ItemThemeBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
            layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtil.dp2px(330f))
        })
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MeThemeViewHolder) {
            holder.bind(data[position])
            holder.itemView.setOnClickListener {
                clickItemListen?.invoke(data[position])
            }
        }
    }
}