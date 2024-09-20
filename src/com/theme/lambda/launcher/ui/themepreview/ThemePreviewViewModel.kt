package com.theme.lambda.launcher.ui.themepreview

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.launcher3.ThemeManager
import com.theme.lambda.launcher.base.BaseViewModel
import com.theme.lambda.launcher.data.model.Resources
import com.theme.lambda.launcher.task.DownloadZipTask
import com.theme.lambda.launcher.ui.theme.ThemeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                ThemeManager.getThemeManagerIfExist()?.enterPreviewModeWithId(resources!!.id)
                context.finish()
                ThemeActivity.closeThemeActivity()
            } else {
                Toast.makeText(context, "download error!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}