package com.theme.lambda.launcher.ui.search.adapter

import android.view.View
import com.android.launcher3.R
import com.android.launcher3.databinding.ItemShortCutBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.theme.lambda.launcher.data.model.ShortCut
import com.theme.lambda.launcher.utils.GlideUtil
import com.theme.lambda.launcher.utils.gone
import com.theme.lambda.launcher.utils.visible

class UrlShortcutAdapter : BaseQuickAdapter<ShortCut, BaseViewHolder>(R.layout.item_short_cut) {

    var clickListen: ((ShortCut) -> Unit)? = null
    var longClickListen: ((ShortCut) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: ShortCut) {
        ItemShortCutBinding.bind(holder.itemView).apply {
            delIv.visibility = if (item.isEdit) View.VISIBLE else View.GONE
            if (item.isAdd) {
                logoIv.gone()
                addIV.visible()
            } else if (item.isPlaceholder) {
                logoIv.gone()
                addIV.gone()
            } else {
                addIV.gone()
                logoIv.visible()
                GlideUtil.load(logoIv, item.iconUrl)
            }
            titleTv.text = item.name

            root.setOnClickListener {
                clickListen?.invoke(item)
            }

            if (item.isEdit || item.isAdd || item.isPlaceholder) {
                root.setOnLongClickListener(null)
            } else {
                root.setOnLongClickListener {
                    longClickListen?.invoke(item)
                    true
                }
            }

            holder.itemView.tag = !item.isAdd && !item.isPlaceholder && item.isEdit
        }
    }
}