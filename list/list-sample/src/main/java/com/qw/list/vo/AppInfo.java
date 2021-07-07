package com.qw.list.vo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


public class AppInfo {
    public String name;
    public Drawable icon;
    public String packageName;
    public String versionName;
    public long versionCode;

    public static AppInfo from(PackageInfo packageInfo, PackageManager packageManager) {
        AppInfo appInfo = new AppInfo();
        //获取应用名称
        appInfo.name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
        //获取应用包名，可用于卸载和启动应用
        appInfo.packageName = packageInfo.packageName;
        //获取应用版本名
        appInfo.versionName = packageInfo.versionName;
        //获取应用版本号
        appInfo.versionCode = packageInfo.versionCode;
        //获取应用图标
        appInfo.icon = packageInfo.applicationInfo.loadIcon(packageManager);
        return appInfo;
    }
}
