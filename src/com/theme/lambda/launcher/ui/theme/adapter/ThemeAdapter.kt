package com.theme.lambda.launcher.ui.theme.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ItemThemeBinding
import com.theme.lambda.launcher.data.model.Resources
import com.theme.lambda.launcher.utils.CommonUtil

class ThemeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = ArrayList<Resources>()
    var clickItemListen: ((Resources) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun upData(d: ArrayList<Resources>) {
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ThemeViewHolder(ItemThemeBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
            layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtil.dp2px(330f))
        })
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ThemeViewHolder) {
            holder.bind(data[position])
            holder.itemView.setOnClickListener {
                clickItemListen?.invoke(data[position])
            }
        }
    }
}