package com.theme.lambda.launcher.utils

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

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
}