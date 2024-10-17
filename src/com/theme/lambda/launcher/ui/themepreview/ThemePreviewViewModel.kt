package com.theme.lambda.launcher.ui.themepreview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.launcher3.Launcher
import com.android.launcher3.ThemeManager
import com.theme.lambda.launcher.ad.AdName
import com.theme.lambda.launcher.ad.AdUtil
import com.theme.lambda.launcher.base.BaseViewModel
import com.theme.lambda.launcher.data.DataRepository
import com.theme.lambda.launcher.data.model.Resources
import com.theme.lambda.launcher.statistics.EventName
import com.theme.lambda.launcher.statistics.EventUtil.logEvent
import com.theme.lambda.launcher.task.DownloadZipTask
import com.theme.lambda.launcher.ui.theme.ThemeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThemePreviewViewModel : BaseViewModel() {

    var resources: Resources? = null

    var loadDialogLiveData = MutableLiveData<Boolean>()


    private var startDownLoadTimeStamp = 0L

    fun download(context: Activity) {

        if (resources == null || resources?.id == "") return

        viewModelScope.launch(Dispatchers.IO) {
            loadDialogLiveData.postValue(true)
            startDownLoadTimeStamp = System.currentTimeMillis()

            val downloadZipTask = DownloadZipTask(resources!!)
            val result = downloadZipTask.execute()
            val downLoadTime = System.currentTimeMillis() - startDownLoadTimeStamp

            logEvent(EventName.downloadSuccess, Bundle().apply {
                putString("id", resources?.id ?: "")
                putLong("duration", downLoadTime)
            })

            // 至少需要下载3秒等待
            val waitTime = 3000 - downLoadTime
            if (waitTime > 0) {
                delay(waitTime)
            }
            loadDialogLiveData.postValue(false)

            if (result) {
                ThemeManager.enterPreviewId = resources?.id ?: ""
                context.startActivity(Intent(context, Launcher::class.java))
                context.finish()
                ThemeActivity.closeThemeActivity()
                DataRepository.insertDownLoadThemeIntoDb(resources!!.toThemeRes())
            } else {
                withContext(Dispatchers.Main){
                    Toast.makeText(context, "download error!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}