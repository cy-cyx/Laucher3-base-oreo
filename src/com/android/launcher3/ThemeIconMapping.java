package com.android.launcher3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.lambda.common.utils.utilcode.util.AppUtils;
import com.lambda.common.utils.utilcode.util.ConvertUtils;
import com.theme.lambda.launcher.Constants;
import com.theme.lambda.launcher.data.model.IconBean;
import com.theme.lambda.launcher.data.model.ManifestBean;
import com.theme.lambda.launcher.utils.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class ThemeIconMapping {


    static String TAG = "ThemeIconMapping";

    private static float iconZoom = 0.6f;
    private static float whiteFrame = CommonUtil.INSTANCE.dp2px(3f);
    private static float roundedCorners = CommonUtil.INSTANCE.dp2px(10f);

    private static Bitmap generalFrameBm;
    private static Bitmap defaultFrameBm;

    static HashMap<String, Bitmap> cacheBitmap = new HashMap();

    public static void cleanThemeIconCache() {
        generalFrameBm = null;
        cacheBitmap.clear();
    }

    /**
     * 根据包名获取映射图片
     *
     * @param context     上下文
     * @param packageName 包名
     * @param url         图标源路径（特殊情况下不为空）
     * @return 如果有映射，返回 {@link  BitmapFactory#decodeResource(Resources, int)} 没有映射返回 null
     */
    public static Bitmap getThemeBitmap(Context context, String packageName, String url) {
        // 使用缓存
        if (cacheBitmap.containsKey(packageName)) {
            return cacheBitmap.get(packageName);
        }

        Bitmap result = null;

        // 主题,all apps 默认
        if (packageName.equals(context.getPackageName())) {
            result = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_themes);
            cacheBitmap.put(packageName, result);
        } else if (packageName.equals(Constants.sAllppAction)) {
            result = BitmapFactory.decodeResource(context.getResources(), R.mipmap.all_apps);
            cacheBitmap.put(packageName, result);
        }
        // 推荐
        if (packageName.contains(RecommendAppManager.getActionHost())) {
            var temp = BitmapFactory.decodeFile(url);
            // 配置的图片需要强制控制一下宽高
            Bitmap target = Bitmap.createBitmap(LauncherAppState.getInstance(context).getInvariantDeviceProfile().iconBitmapSize, LauncherAppState.getInstance(context).getInvariantDeviceProfile().iconBitmapSize, Bitmap.Config.ARGB_8888);
            Canvas temp_canvas = new Canvas(target);
            temp_canvas.drawBitmap(temp, null, new Rect(0, 0, target.getWidth(), target.getHeight()), null);
            result = target;
            cacheBitmap.put(packageName, result);
        }

        // 往后走走看看需不需要换主题

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
                            Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
                            cacheBitmap.put(packageName, bitmap);
                            return bitmap;
                        } catch (Exception e) {
                            Log.e(TAG, "icon path no exit:" + iconPath);
                        }
                    }
                }

                // 都没有 看看通用的
                for (IconBean iconBean : icons) {
                    if (iconBean.getPn().equals("general_background")) {
                        try {
                            if (generalFrameBm == null) {
                                String iconPath = themeManager.getManifestResRootPath() + iconBean.getIcon();
                                generalFrameBm = BitmapFactory.decodeFile(iconPath);
                            }
                            Bitmap frameBm = generalFrameBm.copy(Bitmap.Config.ARGB_8888, true);

                            Bitmap appBitmap;
                            if (result != null) {
                                appBitmap = result;
                            } else {
                                appBitmap = ConvertUtils.drawable2Bitmap(AppUtils.getAppIcon(packageName));
                            }
                            if (frameBm == null) {
                                result = appBitmap;
                            } else {
                                result = addFrameIcon(frameBm, appBitmap);
                            }

                            cacheBitmap.put(packageName, result);
                            return result;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        // 都没有套一层默认的
        if (defaultFrameBm == null) {
            defaultFrameBm = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_default_icon);
        }
        Bitmap frameBm = defaultFrameBm.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap appBitmap;
        if (result != null) {
            appBitmap = result;
        } else {
            appBitmap = ConvertUtils.drawable2Bitmap(AppUtils.getAppIcon(packageName));
        }
        result = addFrameIcon(frameBm, appBitmap);
        cacheBitmap.put(packageName, result);
        return result;
    }

    static Bitmap addFrameIcon(Bitmap frameBm, Bitmap appBm) {
        float frameWidth = frameBm.getWidth();
        float frameHeight = frameBm.getHeight();
        Canvas frameCanvas = new Canvas(frameBm);

        float width = appBm.getWidth();
        float height = appBm.getHeight();

        Rect resRect = new Rect(0, 0, (int) width, (int) height);

        // 计算缩放
        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;
        if (width >= height) {
            float iconWidth = frameWidth * iconZoom;
            float iconHeight = iconWidth / width * height;

            top = (frameHeight - iconHeight) / 2f;
            bottom = top + iconHeight;
            left = (frameWidth - iconWidth) / 2f;
            right = left + iconWidth;
        } else {
            float iconHeight = frameHeight * iconZoom;
            float iconWidth = iconHeight / height * width;

            top = (frameHeight - iconHeight) / 2f;
            bottom = top + iconHeight;
            left = (frameWidth - iconWidth) / 2f;
            right = left + iconWidth;
        }

        Rect dstRect = new Rect((int) left, (int) top, (int) right, (int) bottom);


        RectF whiteFrameRect = new RectF(left - whiteFrame, top - whiteFrame, right + whiteFrame, bottom + whiteFrame);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        frameCanvas.drawRoundRect(whiteFrameRect, roundedCorners, roundedCorners, paint);
        frameCanvas.drawBitmap(appBm, resRect, dstRect, paint);

        return frameBm;
    }
}
