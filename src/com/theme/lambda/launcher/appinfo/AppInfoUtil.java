package com.theme.lambda.launcher.appinfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppInfoUtil {

    private static ArrayList<String> functionApp = new ArrayList<>();

    static {
        // 日后补充
        functionApp.add("com.airbnb.android");
        functionApp.add("com.amazon.sellermobile.android");
        functionApp.add("com.android.settings");
        functionApp.add("com.booking");
        functionApp.add("com.ebay.mobile");
        functionApp.add("com.facebook.katana");
        functionApp.add("com.facebook.orca");
        functionApp.add("com.google.android.apps.maps");
        functionApp.add("com.google.android.apps.nbu.files");
        functionApp.add("com.google.android.apps.photos");
        functionApp.add("com.google.android.apps.podcasts");
        functionApp.add("com.google.android.calculator");
        functionApp.add("com.google.android.deskclock");
        functionApp.add("com.google.android.dialer");
        functionApp.add("com.google.android.gm");
        functionApp.add("com.google.android.GoogleCamera");
        functionApp.add("com.google.android.googlequicksearchbox");
        functionApp.add("com.google.android.keep");
        functionApp.add("com.google.android.youtube");
        functionApp.add("com.instagram.android");
        functionApp.add("com.linkedin.android");
        functionApp.add("com.netflix.mediaclient");
        functionApp.add("com.pinterest");
        functionApp.add("com.sec.android.app.voicerecorder");
        functionApp.add("com.snapchat.android");
        functionApp.add("com.spotify.music");
        functionApp.add("com.ted.android");
        functionApp.add("com.twitter.android");
        functionApp.add("com.ubercab");
        functionApp.add("com.whatsapp");
        functionApp.add("com.zhiliaoapp.musically");
        functionApp.add("org.telegram.messenger");
        functionApp.add("us.zoom.videomeetings");
    }

    public static ArrayList<AppInfo> getAppInfo(Context ctx, boolean isFilterSystem) {
        ArrayList<AppInfo> appInfoList = getAllAppInfo(ctx, isFilterSystem);

        // icon目录
        String iconBasePath = ctx.getFilesDir() + "/icon/";
        File iconBasePathFile = new File(iconBasePath);
        iconBasePathFile.mkdirs();

        // 缓存一下图标
        for (AppInfo info : appInfoList) {
            String label = info.package_name;
            String iconPath = iconBasePath + label.replace(".", "_") + ".png";
            info.iconPath = iconPath;
            File iconFile = new File(iconPath);
            if (!iconFile.exists()) {
                BitmapUtil.drawableToFile(info.icon, iconPath, Bitmap.CompressFormat.PNG);
            }
        }
        return appInfoList;
    }

    private static ArrayList<AppInfo> getAllAppInfo(Context ctx, boolean isFilterSystem) {
        ArrayList<AppInfo> appBeanList = new ArrayList<>();
        AppInfo bean = null;

        PackageManager packageManager = ctx.getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        for (PackageInfo p : list) {
            bean = new AppInfo();
            bean.setIcon(p.applicationInfo.loadIcon(packageManager));
            bean.setLabel(packageManager.getApplicationLabel(p.applicationInfo).toString().replaceAll("[\\p{C}\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", ""));
            bean.setPackage_name(p.applicationInfo.packageName);
            // 判断是否是属于系统的apk
            if (isSystemApp(p) && isFilterSystem) {
                // nodo
            } else {
                appBeanList.add(bean);
            }
        }
        return appBeanList;
    }

    public static Boolean isSystemApp(PackageInfo packageInfo) {
        int flags = packageInfo.applicationInfo.flags;
        String name = packageInfo.packageName;
        return ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) && !functionApp.contains(name);
    }
}
