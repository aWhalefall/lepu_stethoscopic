package com.core.lib.utils.main;

import java.util.List;
import java.util.Stack;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 管理activity的manager
 */

public class AppManager {

	private static Stack<Activity> mActivityStack;
	private static AppManager mInstance;
	
	private AppManager() {
	}

	public static AppManager getAppManager() {
		if (mInstance == null) {
			mInstance = new AppManager();
		}
		return mInstance;
	}

	public void addActivity(Activity activity) {
		if (mActivityStack == null) {
			mActivityStack = new Stack<Activity>();
		}
		mActivityStack.add(activity);
	}

	public Activity currentActivity() {
		if(mActivityStack != null && mActivityStack.size() > 0)
		{
			Activity activity = mActivityStack.lastElement();
			return activity;
		}
		else
		{
			return null;
		}
	}

	public void finishActivity() {
		Activity activity = mActivityStack.lastElement();
		finishActivity(activity);
	}

	public void finishActivity(Activity activity) {
		if (activity != null) {
			mActivityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	public void finishActivity(Class<?> cls) {
		for (Activity activity : mActivityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	public void finishAllActivity() {
		for (int i = 0, size = mActivityStack.size(); i < size; i++) {
			if (null != mActivityStack.get(i)) {
				mActivityStack.get(i).finish();
			}
		}
		mActivityStack.clear();
	}

	public void AppExit(Context context) {
		try {
			finishAllActivity();
			System.exit(0);
		} catch (Exception e) {
		}
	}

	/**
	 * 获取软件版本
	 */
	public static int getVersionCode(Context context,String packageName) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(
					packageName, 0);
		} catch (NameNotFoundException e) {
			LogUtilBase.LogD("NameNotFoundException:",
					Log.getStackTraceString(e));
		}
		if (info != null)
			return info.versionCode;

		return 0;
	}
	
	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context context, String packageName) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	/*
	 * 检查是否安装此应用
	 */
	public static boolean checkIfInstallApplication(Context context,String packageName, String className) {
		boolean install = false;

		ComponentName componentName = new ComponentName(packageName,
				className);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setComponent(componentName);
		List<ResolveInfo> acts = context.getPackageManager()
				.queryIntentActivities(intent, 0);
		if (acts.size() > 0) {
			install = true;
		}

		return install;
	}

	/**
	 * 跳入该应用
	 */
	public static void jumpApplication(Context context,Bundle dataBundle,String packageName, String className) {

		ComponentName componentName = new ComponentName(packageName,
				className);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setComponent(componentName);
		if(dataBundle != null)
		{
			intent.putExtras(dataBundle);
		}
		List<ResolveInfo> acts = context.getPackageManager()
				.queryIntentActivities(intent, 0);
		if (acts.size() > 0) {
			context.startActivity(intent);
		}
	}

	/*
	 * 获取设备信息
	 */

	public static String getDeviceInfo(Context context) {
		try {
			JSONObject json = new JSONObject();
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			WifiManager wifi = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 获取设备唯一id
	 */
	public static String getDeviceUniqueId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String device_id = tm.getDeviceId();

		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		String mac = wifi.getConnectionInfo().getMacAddress();

		if (TextUtils.isEmpty(device_id)) {
			device_id = mac;
		}

		if (TextUtils.isEmpty(device_id)) {
			device_id = android.provider.Settings.Secure.getString(
					context.getContentResolver(),
					android.provider.Settings.Secure.ANDROID_ID);
		}
		return device_id;
	}

}