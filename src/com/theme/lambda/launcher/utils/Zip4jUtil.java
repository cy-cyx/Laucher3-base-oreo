package com.theme.lambda.launcher.utils;

import android.util.Log;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

import java.io.File;

/**
 * 此类是用第三方开源的zip4j操作文件（目录）的压缩、解压、加解密
 */

public class Zip4jUtil {

    private static final String TAG = "CompressOperate_zip4j";

    /**
     * zip4j解压
     *
     * @param zipFilePath 待解压的zip文件（目录）路径
     * @param filePath    解压到的保存路径
     * @param password    密码
     * @return 状态返回值
     */
    public static int uncompress(String zipFilePath, String filePath, String password) {
        ZipFile zipFile;
        ZipParameters zipParameters;
        int result = 0; //状态

        File zipFile_ = new File(zipFilePath);
        File sourceFile = new File(filePath);

        try {
            zipFile = new ZipFile(zipFile_);
            if (!zipFile.isValidZipFile()) {     //检查输入的zip文件是否是有效的zip文件
                throw new ZipException("压缩文件不合法,可能被损坏.");
            }
            if (sourceFile.isDirectory() && !sourceFile.exists()) {
                sourceFile.mkdir();
            }
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password.toCharArray());
            }
            zipFile.extractAll(filePath); //解压
            Log.i(TAG, "uncompressZip4j: 解压成功");

        } catch (ZipException e) {
            Log.e(TAG, "uncompressZip4j: 异常：" + e);
            result = -1;
            return result;
        }
        return result;
    }

}
