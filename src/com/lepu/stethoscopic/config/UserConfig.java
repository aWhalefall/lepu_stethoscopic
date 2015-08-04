package com.lepu.stethoscopic.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.core.lib.utils.main.LogUtilBase;
import com.lepu.stethoscopic.model.User;
import com.lepu.stethoscopic.utils.Const;

@SuppressWarnings("deprecation")
@SuppressLint("WorldReadableFiles")
public class UserConfig {

    /**
     * 用share的形式存取配置信息
     * <p/>
     * 和账户有关的配置信息，存在这里。
     * 切换用户登录，信息会被清空。		！！！
     */
    private static final String TAG = "UserConfig";
    private static final String USER_NAME_SETTINGS = Const.PACKAGE_NAME + "."
            + "user_config";

//    public static void setUserInfo(Context context, User user) {
//        setConfig(context, Const.UserInfo.UserID, user.getUserId());
//        setConfig(context, Const.UserInfo.NickName, user.getNickName());
//        setConfig(context, Const.UserInfo.TrueName, user.getTrueName());
//        setConfig(context, Const.UserInfo.BirthDay, user.getBirthDay());
//        setConfig(context, Const.UserInfo.BirthMonth, user.getBirthMonth());
//        setConfig(context, Const.UserInfo.BirthYear, user.getBirthYear());
//        setConfig(context, Const.UserInfo.Gender, user.getGender());
//        setConfig(context, Const.UserInfo.Height, user.getHeight());
//        setConfig(context, Const.UserInfo.Weight, user.getWeight());
//
//        setConfig(context, Const.UserInfo.MobilePhone, user.getPhone());
//        setConfig(context, Const.UserInfo.LoginToken, user.getToken());
//
//        setConfig(context, Const.UserInfo.CoronaryHeartDisease, user.isGuanxin ? 1 : 0);
//        setConfig(context, Const.UserInfo.Hypertension, user.isHypertension ? 1 : 0);
//        setConfig(context, Const.UserInfo.Nephroma, user.isNephroma ? 1 : 0);
//        setConfig(context, Const.UserInfo.Oculopathy, user.isEyeproblem ? 1 : 0);
//        setConfig(context, Const.UserInfo.FamilyGlycuresis, user.isSugarHistory ? 1 : 0);
//    }
//
//    public static User getUserInfo(Context context) {
//
//        User user = new User();
//        user.setUserId(UserConfig.getConfigString(context, Const.UserInfo.UserID, ""));
//        user.setNickName(UserConfig.getConfigString(context, Const.UserInfo.NickName, ""));
//        user.setTrueName(UserConfig.getConfigString(context, Const.UserInfo.TrueName, ""));
//        user.setBirthDay(UserConfig.getConfigString(context, Const.UserInfo.BirthDay, ""));
//
//        user.setBirthMonth(UserConfig.getConfigString(context, Const.UserInfo.BirthMonth, ""));
//        user.setBirthYear(UserConfig.getConfigString(context, Const.UserInfo.BirthYear, ""));
//
//        user.setGender(UserConfig.getConfigInt(context, Const.UserInfo.Gender, 0));
//        user.setHeight(UserConfig.getConfigString(context, Const.UserInfo.Height, ""));
//        user.setWeight(UserConfig.getConfigString(context, Const.UserInfo.Weight, ""));
//
//        user.setPhone(UserConfig.getConfigString(context, Const.UserInfo.MobilePhone, ""));
//        user.setToken(UserConfig.getConfigString(context, Const.UserInfo.LoginToken, ""));
//
//        user.isGuanxin = (UserConfig.getConfigInt(context, Const.UserInfo.CoronaryHeartDisease, 0) == 1);
//        user.isHypertension = (UserConfig.getConfigInt(context, Const.UserInfo.Hypertension, 0) == 1);
//        user.isNephroma = (UserConfig.getConfigInt(context, Const.UserInfo.Nephroma, 0) == 1);
//        user.isEyeproblem = (UserConfig.getConfigInt(context, Const.UserInfo.Oculopathy, 0) == 1);
//        user.isSugarHistory = (UserConfig.getConfigInt(context, Const.UserInfo.FamilyGlycuresis, 0) == 1);
//
//        return user;
//    }

    // 设置配置值
    public static void setConfig(Context context, String name,
                                 Object valueObject) {
        SharedPreferences shared = context.getSharedPreferences(
                USER_NAME_SETTINGS, Context.MODE_WORLD_READABLE
                        + Context.MODE_MULTI_PROCESS);
        Editor editor = shared.edit();
        if (valueObject instanceof String) {
            String value = (String) valueObject;
            editor.putString(name, value);
        } else if (valueObject instanceof Integer) {
            int value = (Integer) valueObject;
            editor.putInt(name, value);
        } else if (valueObject instanceof Float) {
            float value = (Float) valueObject;
            editor.putFloat(name, value);
        } else if (valueObject instanceof Long) {
            long value = (Long) valueObject;
            editor.putLong(name, value);
        } else if (valueObject instanceof Boolean) {
            boolean value = (Boolean) valueObject;
            editor.putBoolean(name, value);
        }
        editor.commit();
    }

    // 获取配置 string
    public static String getConfigString(Context context, String name,
                                         String defaultValue) {
        Context app360context = null;
        try {
            app360context = context.createPackageContext(Const.PACKAGE_NAME,
                    Context.CONTEXT_IGNORE_SECURITY);
        } catch (NameNotFoundException e) {
            Log.d(TAG, "NameNotFoundException:" + Log.getStackTraceString(e));
            return defaultValue;
        }
        SharedPreferences share = app360context.getSharedPreferences(
                USER_NAME_SETTINGS, Context.MODE_WORLD_READABLE
                        + Context.MODE_MULTI_PROCESS);
        return share.getString(name, defaultValue);
    }

    // 获取配置 int
    public static int getConfigInt(Context context, String name,
                                   int defaultValue) {
        Context app360context = null;
        try {
            app360context = context.createPackageContext(Const.PACKAGE_NAME,
                    Context.CONTEXT_IGNORE_SECURITY);
        } catch (NameNotFoundException e) {
            Log.d(TAG, "NameNotFoundException:" + Log.getStackTraceString(e));
            return defaultValue;
        }
        SharedPreferences share = app360context.getSharedPreferences(
                USER_NAME_SETTINGS, Context.MODE_WORLD_READABLE
                        + Context.MODE_MULTI_PROCESS);
        return share.getInt(name, defaultValue);
    }

    // 获取配置 boolean
    public static boolean getConfigBoolean(Context context, String name,
                                           boolean defaultValue) {
        Context app360context = null;
        try {
            app360context = context.createPackageContext(Const.PACKAGE_NAME,
                    Context.CONTEXT_IGNORE_SECURITY);
        } catch (NameNotFoundException e) {
            Log.d(TAG, "NameNotFoundException:" + Log.getStackTraceString(e));
            return defaultValue;
        }
        SharedPreferences share = app360context.getSharedPreferences(
                USER_NAME_SETTINGS, Context.MODE_WORLD_READABLE
                        + Context.MODE_MULTI_PROCESS);
        return share.getBoolean(name, defaultValue);
    }

    // 获取配置long
    public static long getConfigLong(Context context, String name,
                                     long defaultValue) {
        Context app360context = null;
        try {
            app360context = context.createPackageContext(Const.PACKAGE_NAME,
                    Context.CONTEXT_IGNORE_SECURITY);
        } catch (NameNotFoundException e) {
            Log.d(TAG, "NameNotFoundException:" + Log.getStackTraceString(e));
            return defaultValue;
        }
        SharedPreferences share = app360context.getSharedPreferences(
                USER_NAME_SETTINGS, Context.MODE_WORLD_READABLE
                        + Context.MODE_MULTI_PROCESS);

        return share.getLong(name, defaultValue);
    }

    public static void clearShareConfig(Context context) {
        Context app360context = null;
        try {
            app360context = context.createPackageContext(Const.PACKAGE_NAME,
                    Context.CONTEXT_IGNORE_SECURITY);
        } catch (NameNotFoundException e) {
            LogUtilBase.LogD(TAG,
                    "NameNotFoundException:" + Log.getStackTraceString(e));
            return;
        }
        SharedPreferences share = app360context.getSharedPreferences(
                USER_NAME_SETTINGS, Context.MODE_WORLD_READABLE
                        + Context.MODE_MULTI_PROCESS);
        Editor editor = share.edit();
        editor.clear();
        editor.commit();
    }
}
