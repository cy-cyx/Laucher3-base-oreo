package com.theme.lambda.launcher.ui.seticon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.launcher3.R
import com.android.launcher3.ThemeManager
import com.android.launcher3.databinding.ActivitySetIconBinding
import com.lambda.common.base.BaseActivity
import com.lambda.common.utils.gone
import com.lambda.common.utils.marginStatusBarHeight
import com.lambda.common.utils.visible
import com.lambda.common.statistics.EventName
import com.lambda.common.statistics.EventUtil
import com.theme.lambda.launcher.ui.seticon.adpater.SetIconAdapter
import com.lambda.common.utils.StatusBarUtil
import com.lambda.common.vip.VipManager

class SetIconActivity : BaseActivity<ActivitySetIconBinding>() {

    companion object {
        val sKeyId = "key_id"

        fun start(context: Context, id: String) {
            context.startActivity(Intent(context, SetIconActivity::class.java).apply {
                putExtra(sKeyId, id)
            })
        }
    }

    override fun initViewBinding(layoutInflater: LayoutInflater): ActivitySetIconBinding {
        return ActivitySetIconBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<SetIconViewModel>()

    private val setIconAdapter = SetIconAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarLightMode(this.window)
        viewBinding.containerLl.marginStatusBarHeight()

        viewBinding.dataRv.apply {
            layoutManager = LinearLayoutManager(this@SetIconActivity)
            adapter = setIconAdapter
        }

        setIconAdapter.apply {
            onClickDownLoadOrUnLockListen = {
                if (it.isLock) {
                    viewModel.unLock(this@SetIconActivity, it)
                } else {
                    viewModel.downLoad(this@SetIconActivity, it)
                }
            }
            onClickRadioBnListen = {
                viewModel.clickRadioBn(it)
            }
            onClickAppIconListen = {
                viewModel.selectOrChangeAppInfo(this@SetIconActivity, it)
            }
        }
        viewBinding.selectAllTv.setOnClickListener {
            viewModel.selectAllOrDeselectAll()
        }
        viewBinding.backIv.setOnClickListener {
            finish()
        }
        viewBinding.getAllTv.setOnClickListener {
            viewModel.unLockAll(this)
            EventUtil.logEvent(EventName.getAllClick, Bundle().apply {
                putString("id", ThemeManager.getThemeManagerIfExist()?.previewThemeId)
            })
        }
        viewModel.iconInfoLiveData.observe(this, Observer {
            setIconAdapter.upData(it)
        })
        viewModel.isAllSelectLiveData.observe(this, Observer {
            if (it) {
                viewBinding.selectAllTv.setText(R.string.deselect_all)
            } else {
                viewBinding.selectAllTv.setText(R.string.select_all)
            }
        })
        viewModel.showGetAllBnLiveData.observe(this, Observer {
            if (it) {
                viewBinding.getAllTv.visible()
            } else {
                viewBinding.getAllTv.gone()
            }
        })
        VipManager.isVip.observe(this, Observer {
            if (it) {
                viewModel.unLockAllIcon()
            }
        })

        val id = intent.getStringExtra(sKeyId)
        if (id == null) {
            finish()
            return
        }
        viewModel.init(id)

        EventUtil.logEvent(EventName.iconPageView, Bundle().apply {
            putString("id", ThemeManager.getThemeManagerIfExist()?.previewThemeId)
        })
    }
}