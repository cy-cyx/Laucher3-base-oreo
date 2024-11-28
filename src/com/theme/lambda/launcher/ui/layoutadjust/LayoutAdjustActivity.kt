package com.theme.lambda.launcher.ui.layoutadjust

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.android.launcher3.AdjustConfig
import com.android.launcher3.LauncherAppState
import com.android.launcher3.R
import com.android.launcher3.databinding.ActivityLayoutAdjustBinding
import com.lambda.common.base.BaseActivity
import com.lambda.common.utils.gone
import com.lambda.common.utils.marginStatusBarHeight
import com.lambda.common.utils.visible
import com.theme.lambda.launcher.ui.iconsetting.IconSettingActivity
import com.lambda.common.utils.StatusBarUtil
import com.theme.lambda.launcher.widget.pickview.ScrollPickerView
import com.theme.lambda.launcher.widget.pickview.ScrollPickerView.OnSelectedListener

class LayoutAdjustActivity : BaseActivity<ActivityLayoutAdjustBinding>() {

    companion object {

        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, LayoutAdjustActivity::class.java))
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivityLayoutAdjustBinding {
        return ActivityLayoutAdjustBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()

        viewBinding.backIv.setOnClickListener {
            finish()
        }

        viewBinding.IconSetFl.setOnClickListener {
            IconSettingActivity.start(this)
        }

        initHomeScreen()
    }

    private var columnData = arrayListOf("4", "5", "6")
    private var rowData = arrayListOf("4", "5", "6")

    private var curColumn = 4
    private var curRow = 5

    private fun initHomeScreen() {
        viewBinding.homeScreenFl.setOnClickListener {
            if (viewBinding.homeScreenLl.visibility == View.VISIBLE) {
                viewBinding.homeScreenLl.gone()
                viewBinding.homeScreenNextIv.setImageResource(R.drawable.ic_next)
            } else {
                initHomeScreenData()
                viewBinding.homeScreenLl.visible()
                viewBinding.homeScreenNextIv.setImageResource(R.drawable.ic_up)
            }
        }

        viewBinding.columnPicker.data = columnData as List<CharSequence>?
        viewBinding.columnPicker.isIsCirculation = false
        viewBinding.columnPicker.setOnSelectedListener(object : OnSelectedListener {
            override fun onSelected(scrollPickerView: ScrollPickerView<*>?, position: Int) {
                curColumn = columnData.getOrNull(position)?.toIntOrNull() ?: 4

                viewBinding.layoutPreviewLpv.setData(curColumn, curRow)
                viewBinding.gridNumTv.setText("Grid $curColumn*$curRow")
            }
        })

        viewBinding.rowPicker.data = rowData as List<CharSequence>?
        viewBinding.rowPicker.isIsCirculation = false
        viewBinding.rowPicker.setOnSelectedListener(object : OnSelectedListener {
            override fun onSelected(scrollPickerView: ScrollPickerView<*>?, position: Int) {
                curRow = rowData.getOrNull(position)?.toIntOrNull() ?: 5

                viewBinding.layoutPreviewLpv.setData(curColumn, curRow)
                viewBinding.gridNumTv.setText("Grid $curColumn*$curRow")
            }
        })

        viewBinding.homeScreenApplyTv.setOnClickListener {
            AdjustConfig.setColumn(curColumn)
            AdjustConfig.setRow(curRow)

            finish()
        }
        initHomeScreenData()
    }

    private fun initHomeScreenData() {
        curColumn = AdjustConfig.getColumn()
        if (curColumn == -1) {
            curColumn = LauncherAppState.getInstance(this).invariantDeviceProfile.numColumns
        }
        curRow = AdjustConfig.getRow()
        if (curRow == -1) {
            curRow = LauncherAppState.getInstance(this).invariantDeviceProfile.numRows
        }

        viewBinding.columnPicker.setSelectedPosition(columnData.indexOf(curColumn.toString()))
        viewBinding.rowPicker.setSelectedPosition(rowData.indexOf(curRow.toString()))
    }
}