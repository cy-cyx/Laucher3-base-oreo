package com.theme.lambda.launcher.ui.me

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.launcher3.Launcher
import com.android.launcher3.ThemeManager
import com.lambda.common.base.BaseViewModel
import com.theme.lambda.launcher.data.DataRepository
import com.theme.lambda.launcher.data.model.ThemeRes
import com.theme.lambda.launcher.ui.theme.ThemeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeViewModel : BaseViewModel() {

    val dataLiveDate = MutableLiveData<ArrayList<ThemeRes>>()
    var refreshFinishLiveData = MutableLiveData<Boolean>()

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            dataLiveDate.postValue(ArrayList(DataRepository.getDownLoadThemeRecord()))
            refreshFinishLiveData.postValue(true)
        }
    }

    fun download(context: Activity, res: ThemeRes) {
        ThemeManager.enterPreviewId = res.did
        context.startActivity(Intent(context, Launcher::class.java))
        ThemeActivity.closeThemeActivity()
        context.finish()
    }
}