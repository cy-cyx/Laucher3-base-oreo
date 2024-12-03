package com.android.launcher3.allapps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.AdjustConfig.getAppDrawerTextColor
import com.android.launcher3.ThemeIconMappingV2
import com.android.launcher3.databinding.ItemAllappRecentBinding
import com.lambda.common.utils.utilcode.util.AppUtils

class RecentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = ArrayList<String>()

    fun upData(d: MutableList<String>) {
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecentViewHolder(ItemAllappRecentBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
            layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? RecentViewHolder)?.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val viewBinding = ItemAllappRecentBinding.bind(view)

        fun bind(app: String) {
            viewBinding.icon.setTextColor(getAppDrawerTextColor())
            viewBinding.icon.setIcon(
                ThemeIconMappingV2.getIconBitmapIfNeedAsyn(
                    viewBinding.icon,
                    app,
                    ""
                )
            )
            viewBinding.icon.setText(AppUtils.getAppName(app))
            viewBinding.icon.setOnClickListener {
                AppUtils.launchApp(app)
            }
        }
    }
}