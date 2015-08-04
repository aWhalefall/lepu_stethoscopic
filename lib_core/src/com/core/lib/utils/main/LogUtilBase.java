package com.core.lib.utils.main;

import android.util.Log;

public class LogUtilBase {

	private static final String TAG = "CUSTOM_TAG";

	public static void LogW(String tag, String msg) {
		if (UtilityBase.DEBUG) {
			if (tag != null)
				Log.w(tag, msg);
			else
				Log.w(TAG, msg);
		}
	}

	public static void LogD(String tag, String msg) {
		if (UtilityBase.DEBUG) {
			if (tag != null)
				Log.d(tag, msg);
			else
				Log.d(TAG, msg);
		}
	}

	public static void LogE(String tag, String msg) {
		if (UtilityBase.DEBUG) {
			if (tag != null)
				Log.e(tag, msg);
			else
				Log.e(TAG, msg);
		}
	}

}
