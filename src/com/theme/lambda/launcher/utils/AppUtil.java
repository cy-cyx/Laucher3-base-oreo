package com.theme.lambda.launcher.utils;

import android.content.Context;
import android.text.TextUtils;

public class AppUtil {
    public static boolean checkAppInstalled(Context context, String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (Exception x) {
            return false;
        }
        return true;
    }
}
