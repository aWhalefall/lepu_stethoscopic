package com.lepu.stethoscopic.fun.functiion.recorderData;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SDCardUtil {

    public static final String DATA_DIRECTORY = "/sdcard/.i5suoi";

    public static void createDirectory() {
        if (sdCardExists()) {
            File file = new File(DATA_DIRECTORY);
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            Log.e("U", "无sd卡...");
        }
    }

    public static void createDirectory(String path) {
        if (sdCardExists()) {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            Log.e("U", "无sd卡...");
        }
    }

    public static boolean sdCardExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    @SuppressLint("SimpleDateFormat")
    public static String millis2CalendarString(long millis, String format) {
        if (millis > 0) {
            SimpleDateFormat yFormat = new SimpleDateFormat(format);
            Calendar yCalendar = Calendar.getInstance();
            yCalendar.setTimeInMillis(millis);

            try {
                return yFormat.format(yCalendar.getTime());
            } catch (Exception e) {

            } finally {
                yCalendar = null;
                yFormat = null;
                format = null;
            }
        }

        return "";
    }

}
