package com.core.lib.utils.main;

import com.umeng.analytics.MobclickAgent;
import android.content.Context;

public class UmengHelper {

	private static final boolean DEBUG = true;

	public static void onResume(Context context) {
		if (!DEBUG)
			MobclickAgent.onResume(context);
	}

	public static void onPause(Context context) {
		if (!DEBUG)
			MobclickAgent.onPause(context);
	}

	public static void onError(Context context) {
		if (!DEBUG)
			MobclickAgent.onError(context);
	}

	public static void updateOnlineConfig(Context context) {
		if (!DEBUG)
			MobclickAgent.updateOnlineConfig(context);
	}

	public static void setDebug(boolean debug) {
		MobclickAgent.setDebugMode(debug);
	}

}
