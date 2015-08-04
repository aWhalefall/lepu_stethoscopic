package com.core.lib.utils.main;

import android.content.Context;
import android.os.Debug;

public class TraceUtil {

	/*
	 * 程序性能分析工具
	 */
	public static void startTrace(Context context, String fileName) {
		if (UtilityBase.DEBUG && context != null) {
			Debug.startMethodTracing(context.getFilesDir() + "/" + fileName);
		}
	}

	public static void stopTrace() {
		if (UtilityBase.DEBUG) {
			Debug.stopMethodTracing();
		}
	}
}
