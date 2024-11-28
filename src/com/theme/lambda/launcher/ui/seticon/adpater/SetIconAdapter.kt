package com.theme.lambda.launcher.ui.seticon.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ItemBottomWhiteBinding
import com.android.launcher3.databinding.ItemSetIconBinding
import com.lambda.common.base.BaseItem
import com.theme.lambda.launcher.data.model.IconBean
import com.theme.lambda.launcher.ui.seticon.item.BottomWhiteItem
import com.theme.lambda.launcher.ui.seticon.item.IconItem
import com.lambda.common.utils.CommonUtil

class SetIconAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val sSetIconViewType = 1
        const val sBottomWhiteViewType = 2
    }

    var data = ArrayList<BaseItem>()

    var onClickRadioBnListen: ((IconBean) -> Unit)? = null
    var onClickAppIconListen: ((IconBean) -> Unit)? = null
    var onClickDownLoadOrUnLockListen: ((IconBean) -> Unit)? = null

    fun upData(d: ArrayList<BaseItem>) {
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is BottomWhiteItem -> sBottomWhiteViewType
            else -> sSetIconViewType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == sBottomWhiteViewType) {
            return BottomWhiteViewHolder(ItemBottomWhiteBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
                layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        CommonUtil.dp2px(150f)
                    )
            })
        } else {
            return SetIconViewHolder(ItemSetIconBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
                layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        CommonUtil.dp2px(83f)
                    )
            })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SetIconViewHolder) {
            holder.init((data[position] as IconItem).iconBean)
            holder.onClickAppIconListen = this.onClickAppIconListen
            holder.onClickRadioBnListen = this.onClickRadioBnListen
            holder.onClickDownLoadOrUnLockListen = this.onClickDownLoadOrUnLockListen
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}