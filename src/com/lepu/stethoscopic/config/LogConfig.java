package com.lepu.stethoscopic.config;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.content.Context;

import com.core.lib.utils.main.FileUtilBase;
import com.core.lib.utils.main.StringUtilBase;
import com.lepu.stethoscopic.utils.SdLocal;
import com.lepu.stethoscopic.utils.Utils;

/**
 * 用file的形式存取日志信息
 * 
 */
public class LogConfig {

	private static FileOutputStream mFileOutputStream = null;
	
	/*
	 * sd卡中写日志，调试模式中，方便开发人员看自己写的日志
	 */
	public static void setConfigLog(Context context,String tag,
			String message) {
		
		if (!Utils.DEBUG) {
			return;
		}

		Calendar cal = Calendar.getInstance();
		String time = String.format("%02d-%02d %02d:%02d:%02d",
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND));
		
		StringBuffer sb = new StringBuffer();
		sb.append(time);
		sb.append("<");
		sb.append(tag);
		sb.append(">:");
		sb.append(message);
		sb.append("\r\n");

		FileOutputStream fos = getFileOutputStreamInstance(context);
		if (fos != null) {
			FileUtilBase.saveFile(fos, sb.toString().getBytes());
		}
	}

	private static FileOutputStream getFileOutputStreamInstance(Context context) {
		if (mFileOutputStream != null)
			return mFileOutputStream;

		String filePath = SdLocal.getDebugLogPath(context);
		if (StringUtilBase.stringIsEmpty(filePath)) {
			return null;
		}

		try {
			mFileOutputStream = new FileOutputStream(filePath, true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return mFileOutputStream;
	}
}
