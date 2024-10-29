package com.theme.lambda.launcher.utils

import android.text.TextUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException


object FileUtil {
    fun readStringFromFile(file: File): String {
        var text = StringBuilder()
        try {
            var br = BufferedReader(FileReader(file))
            var line: String
            line = br.readLine()
            while (line != null) {
                text.append(line)
                line = br.readLine()
            }

        } catch (e: Exception) {
        }
        return text.toString()
    }

    fun getFolder(file: String): String {
        val last = file.lastIndexOf("/")
        if (last == -1) return file
        return file.substring(0, last)
    }

    fun getFileNameWithSuffix(path: String): String {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        val start = path.lastIndexOf("/");
        if (start != -1) {
            return path.substring(start + 1);
        } else {
            return "";
        }
    }

    fun copy(source: File?, target: File?) {
        var fileInputStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            fileInputStream = FileInputStream(source)
            fileOutputStream = FileOutputStream(target)
            val buffer = ByteArray(1024)
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileInputStream?.close()
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}