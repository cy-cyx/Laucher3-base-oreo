package com.theme.lambda.launcher.appinfo;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class AppInfo {
    public int uid;
    public String label;//应用名称
    public String package_name;//应用包名
    public Drawable icon;//应用icon
    public String iconPath;//缓存下来的图标路径

    public AppInfo() {
        uid = 0;
        label = "";
        package_name = "";
        icon = null;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    @NonNull
    @Override
    public String toString() {
        return "{\"app_package_name\":\"" + package_name + "\",\"app_icon\":\"" + iconPath + "\",\"app_name\":\""+ label+ "\"}";
    }
}
