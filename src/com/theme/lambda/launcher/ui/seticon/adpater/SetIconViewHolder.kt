package com.theme.lambda.launcher.ui.seticon.adpater

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.launcher3.R
import com.android.launcher3.databinding.ItemSetIconBinding
import com.theme.lambda.launcher.data.model.IconBean
import com.theme.lambda.launcher.utils.GlideUtil
import com.theme.lambda.launcher.utils.gone
import com.theme.lambda.launcher.utils.noDoubleClick
import com.theme.lambda.launcher.utils.visible

class SetIconViewHolder(view: View) : ViewHolder(view) {

    val viewBinding = ItemSetIconBinding.bind(view)

    var onClickRadioBnListen: ((IconBean) -> Unit)? = null
    var onClickAppIconListen: ((IconBean) -> Unit)? = null
    var onClickDownLoadOrUnLockListen: ((IconBean) -> Unit)? = null

    fun init(icon: IconBean) {
        GlideUtil.load(viewBinding.iconIv, icon.icon)

        if ((icon.appIconInfo != null || icon.AppInfo != null)) {
            if (icon.appIconInfo != null) {
                viewBinding.appIv.setImageResource(icon.appIconInfo!!.icon)
            } else if (icon.AppInfo != null) {
                GlideUtil.load(viewBinding.appIv, icon.AppInfo!!.getIconPath())
            }
            viewBinding.editIv.visible()
        } else {
            viewBinding.appIv.setImageResource(R.drawable.ic_set_icon_add)
            viewBinding.editIv.gone()
        }

        if ((icon.appIconInfo != null || icon.AppInfo != null) && !icon.isInstall) {
            viewBinding.appIv.alpha = 0.5f
        } else {
            viewBinding.appIv.alpha = 1f
        }

        if (icon.isSelect) {
            viewBinding.radioIv.setImageResource(R.drawable.ic_radio_bn_select)
        } else {
            viewBinding.radioIv.setImageResource(R.drawable.ic_radio_bn_unselect)
        }

        if (icon.isLock) {
            viewBinding.downloadFl.gone()
            viewBinding.adBnIv.visible()
        } else {
            viewBinding.downloadFl.visible()
            viewBinding.adBnIv.gone()
        }

        viewBinding.radioIv.noDoubleClick {
            onClickRadioBnListen?.invoke(icon)
        }
        viewBinding.appIv.noDoubleClick {
            onClickAppIconListen?.invoke(icon)
        }
        viewBinding.downloadFl.noDoubleClick {
            onClickDownLoadOrUnLockListen?.invoke(icon)
        }
        viewBinding.adBnIv.noDoubleClick {
            onClickDownLoadOrUnLockListen?.invoke(icon)
        }
    }
}