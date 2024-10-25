package com.theme.lambda.launcher.ui.seticon.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.databinding.ItemSetIconBinding
import com.theme.lambda.launcher.data.model.IconBean
import com.theme.lambda.launcher.utils.CommonUtil

class SetIconAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = ArrayList<IconBean>()

    var onClickRadioBnListen: ((IconBean) -> Unit)? = null
    var onClickAppIconListen: ((IconBean) -> Unit)? = null
    var onClickDownLoadOrUnLockListen: ((IconBean) -> Unit)? = null

    fun upData(d: ArrayList<IconBean>) {
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SetIconViewHolder(ItemSetIconBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
            layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtil.dp2px(83f))
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SetIconViewHolder) {
            holder.init(data[position])
            holder.onClickAppIconListen = this.onClickAppIconListen
            holder.onClickRadioBnListen = this.onClickRadioBnListen
            holder.onClickDownLoadOrUnLockListen = this.onClickDownLoadOrUnLockListen
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}