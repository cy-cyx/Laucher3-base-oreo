package com.theme.lambda.launcher.ui.theme.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.launcher3.R
import com.android.launcher3.databinding.ItemThemeBinding
import com.theme.lambda.launcher.data.model.Resources
import com.theme.lambda.launcher.utils.GlideUtil
import com.theme.lambda.launcher.utils.withHost

class ThemeViewHolder(view: View) : ViewHolder(view) {

    var viewBinding = ItemThemeBinding.bind(view)

    fun bind(data: Resources) {
        GlideUtil.load(
            viewBinding.logoIv,
            data.previewUrl.withHost(),
            placeholder = R.drawable.ic_default
        )
    }
}