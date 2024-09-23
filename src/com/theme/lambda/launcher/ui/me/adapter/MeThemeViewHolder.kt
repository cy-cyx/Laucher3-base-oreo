package com.theme.lambda.launcher.ui.me.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.launcher3.databinding.ItemMeThemeBinding
import com.android.launcher3.databinding.ItemThemeBinding
import com.theme.lambda.launcher.data.model.Resources
import com.theme.lambda.launcher.data.model.ThemeRes
import com.theme.lambda.launcher.utils.GlideUtil
import com.theme.lambda.launcher.utils.withHost

class MeThemeViewHolder(view: View) : ViewHolder(view) {

    var viewBinding = ItemMeThemeBinding.bind(view)

    fun bind(data: ThemeRes) {
        GlideUtil.load(viewBinding.logoIv, data.previewUrl.withHost())
    }
}