package com.lambda.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.core.content.FileProvider
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import java.text.DecimalFormat
import java.util.Locale


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

    fun openFile(context: Context, file: File) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val uriForFile: Uri
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //Android 7.0之后
                uriForFile = FileProvider.getUriForFile(
                    context,
                    MyFileProvider.getFileProviderName(context),
                    file
                )
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            } else {
                uriForFile = Uri.fromFile(file)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) //系统会检查当前所有已创建的Task中是否有该要启动的Activity的Task;
            // 若有，则在该Task上创建Activity；若没有则新建具有该Activity属性的Task，并在该新建的Task上创建Activity。
            intent.setDataAndType(uriForFile, getMimeTypeFromFile(file))
            context.startActivity(intent)
        } catch (e: Exception) {
        }

    }

    /**
     * 使用自定义方法获得文件的MIME类型
     */
    private fun getMimeTypeFromFile(file: File): String? {
        var type = "*/*"
        val fName = file.name
        //获取后缀名前的分隔符"."在fName中的位置。
        val dotIndex = fName.lastIndexOf(".")
        if (dotIndex > 0) {
            //获取文件的后缀名
            val end = fName.substring(dotIndex, fName.length).lowercase(Locale.getDefault())
            //在MIME和文件类型的匹配表中找到对应的MIME类型。
            val map: HashMap<String, String> = MimeMap.mimeMap
            if (!TextUtils.isEmpty(end) && map.keys.contains(end)) {
                type = map[end] ?: ""
            }
        }
        return type
    }

    fun fileSizeToB(size: Long): String {
        val oneGb = 1024 * 1024 * 1024  //定义GB的计算常量
        val oneMb = 1024 * 1024         //定义MB的计算常量
        val oneKb = 1024                //定义KB的计算常量
        val df = DecimalFormat("0.00")
        return if (size / oneGb >= 1) {
            df.format(size / oneGb.toFloat()) + "GB";
        } else if (size / oneMb >= 1) {
            df.format(size / oneMb.toFloat()) + "MB";
        } else if (size / oneKb >= 1) {
            df.format(size / oneKb.toFloat()) + "KB";
        } else {
            "${size}B"
        }
    }
}

private object MimeMap {
    private val mapSimple = HashMap<String, String>()

    /**
     * 常用"文件扩展名—MIME类型"匹配表。
     * 注意，此表并不全，也并不是唯一的，就像有人喜欢用浏览器打开TXT一样，你可以根据自己的爱好自定义。
     */
    val mimeMap: HashMap<String, String>
        get() {
            if (mapSimple.size == 0) {
                mapSimple[".3gp"] = "video/3gpp"
                mapSimple[".apk"] = "application/vnd.android.package-archive"
                mapSimple[".asf"] = "video/x-ms-asf"
                mapSimple[".avi"] = "video/x-msvideo"
                mapSimple[".bin"] = "application/octet-stream"
                mapSimple[".bmp"] = "image/bmp"
                mapSimple[".c"] = "text/plain"
                mapSimple[".chm"] = "application/x-chm"
                mapSimple[".class"] = "application/octet-stream"
                mapSimple[".conf"] = "text/plain"
                mapSimple[".cpp"] = "text/plain"
                mapSimple[".doc"] = "application/msword"
                mapSimple[".docx"] = "application/msword"
                mapSimple[".exe"] = "application/octet-stream"
                mapSimple[".gif"] = "image/gif"
                mapSimple[".gtar"] = "application/x-gtar"
                mapSimple[".gz"] = "application/x-gzip"
                mapSimple[".h"] = "text/plain"
                mapSimple[".htm"] = "text/html"
                mapSimple[".html"] = "text/html"
                mapSimple[".jar"] = "application/java-archive"
                mapSimple[".java"] = "text/plain"
                mapSimple[".jpeg"] = "image/jpeg"
                mapSimple[".jpg"] = "image/jpeg"
                mapSimple[".js"] = "application/x-javascript"
                mapSimple[".log"] = "text/plain"
                mapSimple[".m3u"] = "audio/x-mpegurl"
                mapSimple[".m4a"] = "audio/mp4a-latm"
                mapSimple[".m4b"] = "audio/mp4a-latm"
                mapSimple[".m4p"] = "audio/mp4a-latm"
                mapSimple[".m4u"] = "video/vnd.mpegurl"
                mapSimple[".m4v"] = "video/x-m4v"
                mapSimple[".mov"] = "video/quicktime"
                mapSimple[".mp2"] = "audio/x-mpeg"
                mapSimple[".mp3"] = "audio/x-mpeg"
                mapSimple[".mp4"] = "video/mp4"
                mapSimple[".mpc"] = "application/vnd.mpohun.certificate"
                mapSimple[".mpe"] = "video/mpeg"
                mapSimple[".mpeg"] = "video/mpeg"
                mapSimple[".mpg"] = "video/mpeg"
                mapSimple[".mpg4"] = "video/mp4"
                mapSimple[".mpga"] = "audio/mpeg"
                mapSimple[".msg"] = "application/vnd.ms-outlook"
                mapSimple[".ogg"] = "audio/ogg"
                mapSimple[".pdf"] = "application/pdf"
                mapSimple[".png"] = "image/png"
                mapSimple[".pps"] = "application/vnd.ms-powerpoint"
                mapSimple[".ppt"] = "application/vnd.ms-powerpoint"
                mapSimple[".pptx"] = "application/vnd.ms-powerpoint"
                mapSimple[".prop"] = "text/plain"
                mapSimple[".rar"] = "application/x-rar-compressed"
                mapSimple[".rc"] = "text/plain"
                mapSimple[".rmvb"] = "audio/x-pn-realaudio"
                mapSimple[".rtf"] = "application/rtf"
                mapSimple[".sh"] = "text/plain"
                mapSimple[".tar"] = "application/x-tar"
                mapSimple[".tgz"] = "application/x-compressed"
                mapSimple[".txt"] = "text/plain"
                mapSimple[".wav"] = "audio/x-wav"
                mapSimple[".wma"] = "audio/x-ms-wma"
                mapSimple[".wmv"] = "audio/x-ms-wmv"
                mapSimple[".wps"] = "application/vnd.ms-works"
                mapSimple[".xml"] = "text/plain"
                mapSimple[".xls"] = "application/vnd.ms-excel"
                mapSimple[".xlsx"] = "application/vnd.ms-excel"
                mapSimple[".z"] = "application/x-compress"
                mapSimple[".zip"] = "application/zip"
                mapSimple[""] = "*/*"
            }
            return mapSimple
        }
}
