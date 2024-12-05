package com.lambda.news.widget

import android.content.Context
import android.view.LayoutInflater
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.lambda.common.utils.CommonUtil
import com.lambda.news.R
import com.lambda.news.data.model.LanguageBean
import com.lambda.news.databinding.NewsLayoutLanguageSelectBinding
import com.lambda.news.widget.adapter.LanguageSelectAdapter

class LanguageSelectPopupWindow(context: Context) : PopupWindow(context) {

    private val viewBinding = NewsLayoutLanguageSelectBinding.inflate(LayoutInflater.from(context))

    var clickLanguageCallback: ((LanguageBean) -> Unit)? = null

    init {
        contentView = viewBinding.root
        width = CommonUtil.getScreenWidth() - CommonUtil.dp2px(30f)
        height = CommonUtil.dp2px(150f)

        setBackgroundDrawable(CommonUtil.getDrawable(R.drawable.shape_ffffff_radius_10))
        elevation = 10f

        isFocusable = true
        isOutsideTouchable = true

        viewBinding.dataRv.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = LanguageSelectAdapter().apply {
                clickItemCallback = {
                    clickLanguageCallback?.invoke(it)
                    dismiss()
                }
            }
        }
    }
}