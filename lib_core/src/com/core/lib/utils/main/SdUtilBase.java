package com.core.lib.utils.main;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

public class SdUtilBase {

    private static final int STORGE_VALUE = 50;// 50MB

    public static boolean checkSdCanUse() {
        boolean flag = false;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            flag = true;
        }
        return flag;
    }

    public static boolean checkReadWrite(File file) {
        boolean isYes = false;
        if (file != null && file.canRead() && file.canWrite())
            isYes = true;
        return isYes;
    }

    /**
     * 检查是否安装外置的SD卡
     */
    private static boolean checkExternalSDExists() {
        Map<String, String> evn = System.getenv();
        return evn.containsKey("SECONDARY_STORAGE");
    }

    /**
     * 获取手机 外置SD卡的根目录
     */
    public static String getExternalSDRoot() {
        String sdRootPath = "";
        if (checkExternalSDExists()) {
            Map<String, String> evn = System.getenv();
            sdRootPath = evn.get("SECONDARY_STORAGE");
        }
        return sdRootPath;
    }

    /**
     * 获取存储路径
     * <p/>
     * 优先获取外部的，如果不存在就取内部的存储空间
     */
    public static String getStorgePath(Context context) {

        boolean flag = false;
        String path = null;
        if (checkSdCanUse()) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();

            if (!checkIfFreeSpace(path)) {
                flag = true;
                path = null;
            }

        } else {
            flag = true;
        }

        if (flag) {
            try {
                StorageManager sm = (StorageManager) AppManager.getAppManager().currentActivity()
                        .getSystemService(Context.STORAGE_SERVICE);

                Method method = sm.getClass().getMethod("getVolumePaths", new Class<?>[]{});
                Object object = method.invoke(sm, new Object[]{});
                String[] paths = (String[]) object;
                if (paths != null && paths.length > 0) {
                    for (int i = 0; i < paths.length; i++) {
                        File file = new File(paths[i]);
                        if (checkReadWrite(file)) {
                            path = paths[i];

                            if (checkIfFreeSpace(path))
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                LogUtilBase.LogD(null, Log.getStackTraceString(e));
            }
        }

        return path;
    }

    // =======================================================================================================
    public static boolean checkIfFreeSpace(String path) {
        boolean canUse = false;

        StatFs statfs = null;
        try {
            statfs = new StatFs(path);
        } catch (Exception e) {
            LogUtilBase.LogD(null, Log.getStackTraceString(e));
        }

        if (statfs == null)
            return canUse;

        long blockSize = statfs.getBlockSize();
        long availCount = statfs.getAvailableBlocks();
        int freeSpace = (int) (availCount * blockSize / 1024 / 1024);// 空闲容量

        // 剩余空间大于50MB，存储空间可用
        canUse = (freeSpace > STORGE_VALUE);

        return canUse;
    }
}
