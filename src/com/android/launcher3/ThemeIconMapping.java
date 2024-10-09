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
import com.theme.lambda.launcher.data.model.IconBean;
import com.theme.lambda.launcher.data.model.ManifestBean;
import com.theme.lambda.launcher.utils.CommonUtil;

import java.util.ArrayList;

public class ThemeIconMapping {


    static String TAG = "ThemeIconMapping";

    private static float iconZoom = 0.6f;
    private static float whiteFrame = CommonUtil.INSTANCE.dp2px(3f);
    private static float roundedCorners = CommonUtil.INSTANCE.dp2px(10f);

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

                // 都没有 看看通用的
                for (IconBean iconBean : icons) {
                    if (iconBean.getPn().equals("general_background")) {
                        try {
                            String iconPath = themeManager.getManifestResRootPath() + iconBean.getIcon();
                            Bitmap frameBmTemp = BitmapFactory.decodeFile(iconPath);
                            Bitmap frameBm = frameBmTemp.copy(Bitmap.Config.ARGB_8888, true);

                            float frameWidth = frameBm.getWidth();
                            float frameHeight = frameBm.getHeight();
                            Canvas frameCanvas = new Canvas(frameBm);

                            Bitmap appBitmap = ConvertUtils.drawable2Bitmap(AppUtils.getAppIcon(packageName));
                            float width = appBitmap.getWidth();
                            float height = appBitmap.getHeight();

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


                            RectF whiteFrameRect = new RectF(left - whiteFrame,top - whiteFrame,right + whiteFrame, bottom + whiteFrame);
                            Paint paint = new Paint();
                            paint.setAntiAlias(true);
                            paint.setColor(Color.WHITE);

                            frameCanvas.drawRoundRect(whiteFrameRect, roundedCorners,roundedCorners,paint);
                            frameCanvas.drawBitmap(appBitmap, resRect, dstRect, paint);
                            return frameBm;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return null;
    }
}
