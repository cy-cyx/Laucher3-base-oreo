package com.android.launcher3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ThemeIconMapping {

    /**
     * 根据包名获取映射图片
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 如果有映射，返回 {@link  BitmapFactory#decodeResource(Resources, int)} 没有映射返回 null
     */
    public static Bitmap getThemeBitmap(Context context, String packageName) {
        if (packageName.equals(context.getPackageName())) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_themes);
        }
        return null;
    }
}
