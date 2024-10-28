package com.theme.lambda.launcher.ui.seticon

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.launcher3.R
import com.theme.lambda.launcher.ad.AdName
import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.ad.IAdCallBack
import com.theme.lambda.launcher.appinfo.AppIconMap
import com.theme.lambda.launcher.appinfo.AppInfoCache
import com.theme.lambda.launcher.base.BaseItem
import com.theme.lambda.launcher.base.BaseViewModel
import com.theme.lambda.launcher.data.model.IconBean
import com.theme.lambda.launcher.data.model.ManifestBean
import com.theme.lambda.launcher.shortcut.ShortCutUtil
import com.theme.lambda.launcher.ui.iap.VipActivity
import com.theme.lambda.launcher.ui.seticon.item.BottomWhiteItem
import com.theme.lambda.launcher.ui.seticon.item.IconItem
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.FileUtil
import com.theme.lambda.launcher.utils.GsonUtil
import com.theme.lambda.launcher.widget.dialog.SelectAppDialog
import com.theme.lambda.launcher.widget.dialog.UnLockAllIconDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class SetIconViewModel : BaseViewModel() {

    var id = ""

    var iconInfoLiveData = MutableLiveData<ArrayList<BaseItem>>(arrayListOf())

    var isAllSelectLiveData = MutableLiveData<Boolean>()

    var showGetAllBnLiveData = MutableLiveData<Boolean>(true)

    fun init(i: String) {
        id = i
        viewModelScope.launch(Dispatchers.IO) {
            val filesDir: String = CommonUtil.appContext!!.getFilesDir().getPath()
            val manifestFile = File("$filesDir/wallpaper/$id/manifest.json")
            if (!manifestFile.exists()) return@launch

            val manifestJson: String = FileUtil.readStringFromFile(manifestFile)
            val manifest: ManifestBean = GsonUtil.gson.fromJson(
                manifestJson,
                ManifestBean::class.java
            )
            val icons = manifest.icons

            // 先纠正一下icon路径
            icons.forEach {
                it.icon = "$filesDir/wallpaper/$id/${it.icon}"
            }

            // 绑定一下对应app信息
            icons.forEach {
                it.appIconInfo = AppIconMap.appIconList.find { it1 -> it1.pn == it.pn }
            }

            // 判断是否安装
            icons.forEach {
                it.isInstall =
                    AppInfoCache.appInfos.find { it1 -> it1.package_name == it.pn } != null
            }
            icons.sort()

            val result = ArrayList<BaseItem>()
            result.addAll(icons.map { IconItem(it) })
            result.add(BottomWhiteItem())
            iconInfoLiveData.postValue(result)
        }
    }

    fun clickRadioBn(iconBean: IconBean) {
        iconBean.isSelect = !iconBean.isSelect
        iconInfoLiveData.postValue(iconInfoLiveData.value)

        var isAllSelect = true
        iconInfoLiveData.value?.forEach {
            if (it is IconItem) {
                if (!it.iconBean.isSelect) {
                    isAllSelect = false
                }
            }
        }
        isAllSelectLiveData.postValue(isAllSelect)
    }

    fun selectOrChangeAppInfo(context: Context, iconBean: IconBean) {
        SelectAppDialog(context).apply {
            selectAppListen = {
                dismiss()
                iconBean.appIconInfo = null
                iconBean.isInstall = true
                iconBean.AppInfo = it
                iconInfoLiveData.postValue(iconInfoLiveData.value)
            }
        }.show()
    }

    fun unLock(context: Context, iconBean: IconBean) {
        if (AdUtil.isReady(AdName.icon_unlock)) {
            AdUtil.showAd(AdName.icon_unlock, object : IAdCallBack {
                override fun onNoReady() {

                }

                override fun onAdClose(status: Int) {
                    iconBean.isLock = false
                    iconInfoLiveData.postValue(iconInfoLiveData.value)
                }
            })
        } else {
            Toast.makeText(context, R.string.ad_no_fill_tip, Toast.LENGTH_SHORT).show()
        }
    }

    fun downLoad(context: Context, iconBean: IconBean) {
        if (iconBean.isInstall) {
            if (iconBean.appIconInfo != null) {
                ShortCutUtil.addShortCut(
                    iconBean.appIconInfo?.pn ?: "",
                    iconBean.icon,
                    iconBean.appIconInfo?.name ?: ""
                )
            } else if (iconBean.AppInfo != null) {
                ShortCutUtil.addShortCut(
                    iconBean.AppInfo?.getPackage_name() ?: "",
                    iconBean.icon,
                    iconBean.AppInfo?.getLabel() ?: ""
                )
            }
        } else {
            Toast.makeText(context, R.string.app_no_install, Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun selectAllOrDeselectAll() {
        if (isAllSelectLiveData.value == true) {
            iconInfoLiveData.value?.forEach {
                if (it is IconItem) {
                    it.iconBean.isSelect = false
                }
            }
            iconInfoLiveData.postValue(iconInfoLiveData.value)
            isAllSelectLiveData.value = false
        } else {
            iconInfoLiveData.value?.forEach {
                if (it is IconItem) {
                    it.iconBean.isSelect = true
                }
            }
            iconInfoLiveData.postValue(iconInfoLiveData.value)
            isAllSelectLiveData.value = true
        }
    }

    fun unLockAll(context: Context) {
        UnLockAllIconDialog(context).apply {
            clickVipListen = {
                dismiss()
                VipActivity.start(context, VipActivity.FromGetAllInTheme)
            }
            unLockAllListen = {
                dismiss()
                unLockAllIcon()
            }
        }.show()
    }

    fun unLockAllIcon() {
        showGetAllBnLiveData.postValue(false)
        iconInfoLiveData.value?.forEach {
            if (it is IconItem) {
                it.iconBean.isLock = false
            }
        }
        iconInfoLiveData.postValue(iconInfoLiveData.value)
    }
}