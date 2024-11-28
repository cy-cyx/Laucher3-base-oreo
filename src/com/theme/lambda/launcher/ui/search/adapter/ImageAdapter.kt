package com.theme.lambda.launcher.ui.search.adapter

import com.android.launcher3.R
import com.android.launcher3.databinding.ItemImageBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.theme.lambda.launcher.data.model.FileInfo
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.FileUtil
import com.lambda.common.utils.GlideUtil
import java.io.File

class ImageAdapter : BaseQuickAdapter<FileInfo, BaseViewHolder>(R.layout.item_image) {
    override fun convert(holder: BaseViewHolder, item: FileInfo) {
        ItemImageBinding.bind(holder.itemView).apply {
            GlideUtil.load(imageIv, item.path)
            nameTv.text = item.name

            root.setOnClickListener {
                try {
                    FileUtil.openFile(CommonUtil.appContext!!, File(item.path))
                } catch (e: Exception) {
                }
            }
        }
    }
}