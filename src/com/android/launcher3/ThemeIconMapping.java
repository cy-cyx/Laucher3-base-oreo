package com.android.launcher3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.theme.lambda.launcher.data.model.IconBean;
import com.theme.lambda.launcher.data.model.ManifestBean;

import java.util.ArrayList;

public class ThemeIconMapping {


    static String TAG = "ThemeIconMapping";

    /**
     * 根据包名获取映射图片
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 如果有映射，返回 {@link  BitmapFactory#decodeResource(Resources, int)} 没有映射返回 null
     */
    public static Bitmap getThemeBitmap(Context context, String packageName) {
        // 把自己伪装成主题
        if (packageName.equals(context.getPackageName())) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_themes);
        }

        // 看一下主题用不用替换icon
        ThemeManager themeManager = ThemeManager.Companion.getThemeManagerIfExist();
        if (themeManager != null) {
            ManifestBean curManifest = themeManager.getCurManifest();
            if (curManifest != null) {
                ArrayList<IconBean> icons = curManifest.getIcons();
                for (IconBean iconBean : icons) {
                    if (iconBean.getPn().equals(packageName)) {
                        String iconPath = themeManager.getManifestResRootPath() + iconBean.getIcon();

                        try {
                            return BitmapFactory.decodeFile(iconPath);
                        } catch (Exception e) {
                            Log.e(TAG, "icon path no exit:" + iconPath);
                        }
                    }
                }
            }
        }

        return null;
    }
}
