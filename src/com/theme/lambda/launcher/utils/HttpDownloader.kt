package com.theme.lambda.launcher.utils

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

object HttpDownloader {

    fun downFile(urlStr: String?, filePath: String): Boolean {
        var inputStream: InputStream? = null
        var fileStream: FileOutputStream? = null
        try {
            val file = File(filePath)
            if (file.exists()) {
                return true
            } else {
                File(FileUtil.getFolder(file.path)).mkdirs()

                val url = URL(urlStr)
                val conn = url.openConnection()
                conn.connect()
                inputStream = conn.getInputStream()

                //把数据存入路径+文件名
                fileStream = FileOutputStream(file)
                val buf = ByteArray(1024)
                do {
                    //循环读取
                    val numread: Int = inputStream.read(buf)
                    if (numread == -1) {
                        break
                    }
                    fileStream.write(buf, 0, numread)
                } while (true)
                fileStream.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            try {
                inputStream?.close()
                fileStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return true
    }
}