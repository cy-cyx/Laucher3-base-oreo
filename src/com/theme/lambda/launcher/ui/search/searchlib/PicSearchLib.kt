package com.theme.lambda.launcher.ui.search.searchlib

import android.annotation.SuppressLint
import android.database.Cursor
import android.provider.MediaStore
import com.theme.lambda.launcher.data.model.FileInfo
import com.theme.lambda.launcher.utils.CommonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

object PicSearchLib {

    private var images = ArrayList<FileInfo>()

    // 通过关键字找出30条图片
    fun findImages(s: String): ArrayList<FileInfo> {
        val result = ArrayList<FileInfo>()
        val finalImage = images
        for (it in finalImage) {
            if (it.name.contains(s)) {
                result.add(it)
                if (result.size >= 30) {
                    return result
                }
            }
        }
        return result
    }

    private var load = false
    fun loadData() {
        if (load) return
        load = true
        load()
    }

    @SuppressLint("SimpleDateFormat")
    private fun load() {
        GlobalScope.launch(Dispatchers.IO) {
            val temp = ArrayList<FileInfo>()
            // 扫描files文件库
            var c: Cursor? = null
            try {
                val mContentResolver = CommonUtil.appContext?.contentResolver;
                c = mContentResolver?.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_MODIFIED + "  desc"
                )
                if (c == null) return@launch
                val columnID =
                    c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
                val columnMimeType =
                    c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.MIME_TYPE)
                val columnData =
                    c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)
                val columnSize =
                    c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE)
                // 更改时间
                val columnDateModified =
                    c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED)

                while (c.moveToNext()) {
                    val id = c.getString(columnID)
                    val path = c.getString(columnData);
                    val minType = c.getString(columnMimeType);
                    val positionDo = path.lastIndexOf(".");
                    if (positionDo == -1) {
                        continue
                    }
                    val positionX = path.lastIndexOf(File.separator);
                    if (positionX == -1) {
                        continue
                    }
                    val displayName = path.substring(positionX + 1, path.length);
                    val size = c.getLong(columnSize);
                    c.getLong(columnDateModified);
                    val file = File(path);
                    val time =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(file.lastModified()));
                    val info = FileInfo()
                    info.minType = minType
                    info.name = displayName
                    info.path = path
                    info.size = size
                    info.id = id
                    info.time = time
                    temp.add(info)
                }
            } catch (e: Exception) {
            } finally {
                c?.close();
            }
            images = temp
        }
    }
}