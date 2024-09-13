package com.android.launcher3;

import android.content.ComponentName;

import com.theme.lambda.launcher.utils.CommonUtil;

public class AppFilter {

    public boolean shouldShowApp(ComponentName app) {
        // 隐藏了自己
        if (app.getPackageName().equals(CommonUtil.INSTANCE.getAppContext().getPackageName())) {
            return false;
        }
        return true;
    }
}
