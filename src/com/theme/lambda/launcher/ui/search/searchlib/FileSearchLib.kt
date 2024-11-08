package com.theme.lambda.launcher.ui.search.searchlib

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import com.theme.lambda.launcher.data.model.FileInfo
import com.theme.lambda.launcher.utils.CommonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

object FileSearchLib {

    private var files = ArrayList<FileInfo>()

    // 通过关键字找出5个文件
    fun findFiles(s: String): ArrayList<FileInfo> {
        val result = ArrayList<FileInfo>()
        val finalImage = files
        for (it in finalImage) {
            if (it.name.contains(s)) {
                result.add(it)
                if (result.size >= 5) {
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
                val projection = arrayOf(
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.TITLE,
                    MediaStore.Files.FileColumns.MIME_TYPE,
                    MediaStore.Files.FileColumns.SIZE
                )


                //相当于我们常用sql where 后面的写法
                val selection = (MediaStore.Files.FileColumns.MIME_TYPE + "= ? "
                        + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                        + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                        + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                        + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                        + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                        + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                        + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                        + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? ")

                val selectionArgs = arrayOf(
                    "text/plain", // .txt
                    "text/csv",// .csv
                    "application/msword", // .doc
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document", //.docx
                    "application/pdf", // pdf
                    "application/vnd.ms-powerpoint", // ppt
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .pptx
                    "application/vnd.ms-excel", // xls
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xlsx
                )
                c = mContentResolver?.query(
                    MediaStore.Files.getContentUri("external"),
                    projection,
                    selection,
                    selectionArgs,
                    MediaStore.Images.ImageColumns.DATE_MODIFIED + "  desc"
                )
                if (c == null) return@launch
                val columnID =
                    c.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val columnMimeType =
                    c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.MIME_TYPE)
                val columnData =
                    c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                val columnTitle =
                    c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
                val columnSize =
                    c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)

                while (c.moveToNext()) {
                    val id = c.getString(columnID)
                    val path = c.getString(columnData);
                    val minType = c.getString(columnMimeType);
                    val displayName = c.getString(columnTitle)
                    val size = c.getLong(columnSize);
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
            files = temp
        }
    }
}