package com.lepu.stethoscopic.utils;

import android.content.Context;

import com.core.lib.utils.main.SdUtilBase;
import com.core.lib.utils.main.StringUtilBase;

import java.io.File;

public class SdLocal {

    public static final String LEPU_FOLDER = "/lepu/tzq"; //听诊器
    public static String LEPU_PATH = "";

    public static String getLepuRootPath(Context context) {
        if (StringUtilBase.stringIsEmpty(LEPU_PATH)) {
            String path = SdUtilBase.getStorgePath(context);
            if (!StringUtilBase.stringIsEmpty(path)) {
                LEPU_PATH = path + LEPU_FOLDER;
            }
        }
        return LEPU_PATH;
    }

    // =====================================================================================
    public static String getCacheDataFolder(Context context) {
        String fileFolder = StringUtilBase.combinePath(getLepuRootPath(context),
                "CacheData");

        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return fileFolder;
    }

    public static String getCacheImageFolder(Context context) {
        String iconFolder = StringUtilBase.combinePath(getLepuRootPath(context),
                "CacheImage");

        File file = new File(iconFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return iconFolder;
    }

    // 获取download folder
    public static String getDownloadFolder(Context context) {
        String fileFolder = StringUtilBase.combinePath(getLepuRootPath(context),
                "Download");

        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return fileFolder;
    }

    // 获取temp folder
    public static String getTempFolder(Context context) {
        String fileFolder = StringUtilBase.combinePath(getLepuRootPath(context),
                "Temp");

        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return fileFolder;
    }

    // 获取yuyin folder
    public static String getYuYinFolder(Context context) {
        String fileFolder = StringUtilBase.combinePath(getLepuRootPath(context),
                "YuYin");

        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return fileFolder;
    }

    // =====================================================================================
    // 获取icon路径
    public static String getIconPath(Context context, String flag) {
        return StringUtilBase.combinePath(getCacheImageFolder(context), flag + ".png");
    }

    public static String getDebugLogPath(Context context) {
        return StringUtilBase.combinePath(getTempFolder(context), "debug.log");
    }

    public static String getDatabasePath(Context context, String dbName) {
        return context.getDatabasePath(dbName).getPath();
    }

    public static String getDownloadApkPath(Context context, String path) {
        return StringUtilBase.combinePath(getDownloadFolder(context), path + ".apk");
    }

}
