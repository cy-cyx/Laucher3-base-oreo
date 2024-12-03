package com.lambda.news.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.lambda.common.utils.gone
import com.lambda.common.utils.noDoubleClick
import com.lambda.common.utils.visible
import com.lambda.news.databinding.NewsItemSortBinding

class SortItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    val viewBinding = NewsItemSortBinding.inflate(LayoutInflater.from(context), this, true)

    private var isAdd = false
    private var category = ""

    var stateChangeCallback: ((Boolean, String) -> Unit)? = null

    init {
        noDoubleClick {
            isAdd = !isAdd
            stateChangeCallback?.invoke(isAdd, category)

            if (isAdd) {
                viewBinding.selectFl.visible()
                viewBinding.addFl.gone()
            } else {
                viewBinding.selectFl.gone()
                viewBinding.addFl.visible()
            }
        }

    }

    fun bindData(add: Boolean, c: String) {
        isAdd = add
        category = c

        if (isAdd) {
            viewBinding.selectFl.visible()
            viewBinding.addFl.gone()
        } else {
            viewBinding.selectFl.gone()
            viewBinding.addFl.visible()
        }

        viewBinding.selectCategoryTv.text = c
        viewBinding.addTv.text = c
    }

}