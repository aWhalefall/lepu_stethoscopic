package com.lepu.stethoscopic.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.core.lib.utils.main.LogUtilBase;
import com.lepu.stethoscopic.utils.Const;

@SuppressWarnings("deprecation")
@SuppressLint("WorldReadableFiles")
public class AppConfig {

	/**
	 * 用share的形式存取配置信息
	 * 
	 * 和账户无关的配置信息，存在这里。
	 * 切换用户登录，信息不会被清空。只有程序卸载，才会被清空。
	 */
	private static final String TAG = "AppConfig";
	private static final String APP_NAME_SETTINGS = Const.PACKAGE_NAME + "."
			+ "app_config";

	// 设置配置值
	public static void setConfig(Context context, String name,
			Object valueObject) {
		SharedPreferences shared = context.getSharedPreferences(
				APP_NAME_SETTINGS, Context.MODE_WORLD_READABLE
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
				APP_NAME_SETTINGS, Context.MODE_WORLD_READABLE
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
				APP_NAME_SETTINGS, Context.MODE_WORLD_READABLE
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
				APP_NAME_SETTINGS, Context.MODE_WORLD_READABLE
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
				APP_NAME_SETTINGS, Context.MODE_WORLD_READABLE
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
				APP_NAME_SETTINGS, Context.MODE_WORLD_READABLE
						+ Context.MODE_MULTI_PROCESS);
		Editor editor = share.edit();
		editor.clear();
		editor.commit();
	}
}
