package com.theme.lambda.launcher.widget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.launcher3.databinding.ItemSelectAppBinding
import com.theme.lambda.launcher.appinfo.AppInfo
import com.theme.lambda.launcher.appinfo.AppInfoCache
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.GlideUtil

class SelectAppDialogAdapter : Adapter<ViewHolder>() {

    var itemClickListen: ((AppInfo) -> Unit)? = null

    val data = ArrayList(AppInfoCache.appInfos)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return SelectAppViewHolder(ItemSelectAppBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
            layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is SelectAppViewHolder) {
            holder.init(data[position])
            holder.itemView.setOnClickListener {
                itemClickListen?.invoke(data[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class SelectAppViewHolder(view: View) : ViewHolder(view) {
        val viewBinding = ItemSelectAppBinding.bind(view)

        fun init(appInfo: AppInfo) {
            viewBinding.nameTv.text = appInfo.getLabel()
            GlideUtil.load(viewBinding.appIv, appInfo.getIconPath())
        }
    }
}