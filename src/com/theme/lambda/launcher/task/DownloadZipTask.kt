package com.theme.lambda.launcher.task

import com.theme.lambda.launcher.data.model.Resources
import com.theme.lambda.launcher.utils.CommonUtil
import com.theme.lambda.launcher.utils.Zip4jUtil
import com.theme.lambda.launcher.utils.HttpDownloader
import com.theme.lambda.launcher.utils.SpUtil
import com.theme.lambda.launcher.utils.withHost
import java.io.File

class DownloadZipTask(private var resBean: Resources) : IBaseTask {

    companion object {
        val sKeyIsDownload = "key_is_download"
    }

    fun execute(): Boolean {
        try {
            // 数据是否完整的标志位
            var isDownload = SpUtil.getBool("$sKeyIsDownload${resBean.id}", false)

            val baseFilePath = CommonUtil.appContext!!.filesDir.absoluteFile
            val zipPath = "$baseFilePath/wallpaper/${resBean.id}/zip_file.zip"
            val manifestPath = "$baseFilePath/wallpaper/${resBean.id}/manifest.json"
            val upZipPath = "$baseFilePath/wallpaper/${resBean.id}"

            if (File(manifestPath).exists() && isDownload) {
                return true
            }
            // 删除脏数据
            File(upZipPath).delete()
            SpUtil.putBool("$sKeyIsDownload${resBean.id}", false)

            val downLoad = HttpDownloader.downFile(resBean.zipUrl.withHost(), zipPath)
            if (downLoad) {
                Zip4jUtil.uncompress(zipPath, upZipPath, "")
                SpUtil.putBool("$sKeyIsDownload${resBean.id}", true)
                return true
            } else {
                return false
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}