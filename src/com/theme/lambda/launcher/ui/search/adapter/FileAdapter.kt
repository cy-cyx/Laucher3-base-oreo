package com.theme.lambda.launcher.ui.search.adapter

import com.android.launcher3.R
import com.android.launcher3.databinding.ItemFileBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.theme.lambda.launcher.data.model.FileInfo
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.FileUtil
import java.io.File

class FileAdapter : BaseQuickAdapter<FileInfo, BaseViewHolder>(R.layout.item_file) {
    override fun convert(holder: BaseViewHolder, item: FileInfo) {
        ItemFileBinding.bind(holder.itemView).apply {
            nameTv.text = item.name
            sizeTv.text = FileUtil.fileSizeToB(item.size)
            fileIconIv.setImageResource(getFileIcon(item.path))
            root.setOnClickListener {
                try {
                    FileUtil.openFile(CommonUtil.appContext!!, File(item.path))
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun getFileIcon(path: String): Int {
        if (path.contains(".pdf")) {
            return R.drawable.ic_pdf
        } else if (path.contains(".doc")) {
            return R.drawable.ic_doc
        } else if (path.contains(".txt")) {
            return R.drawable.ic_txt
        } else {
            return R.drawable.ic_unknown
        }
    }
}