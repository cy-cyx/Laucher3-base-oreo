package com.theme.lambda.launcher.ui.themepreview

import android.app.Activity
import android.content.Intent
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
import com.theme.lambda.launcher.task.DownloadZipTask
import com.theme.lambda.launcher.ui.theme.ThemeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThemePreviewViewModel : BaseViewModel() {

    var resources: Resources? = null

    var loadDialogLiveData = MutableLiveData<Boolean>()

    fun download(context: Activity) {

        if (resources == null || resources?.id == "") return

        viewModelScope.launch(Dispatchers.IO) {
            loadDialogLiveData.postValue(true)

            val downloadZipTask = DownloadZipTask(resources!!)
            val result = downloadZipTask.execute()
            loadDialogLiveData.postValue(false)
            if (result) {
                ThemeManager.enterPreviewId = resources?.id ?: ""
                if (!Launcher.isExist()) {
                    context.startActivity(Intent(context, Launcher::class.java))
                }
                context.finish()
                ThemeActivity.closeThemeActivity()
                DataRepository.insertDownLoadThemeIntoDb(resources!!.toThemeRes())
            } else {
                Toast.makeText(context, "download error!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}