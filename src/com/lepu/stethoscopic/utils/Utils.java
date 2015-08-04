package com.lepu.stethoscopic.utils;

import java.io.File;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.MediaRecorder;
import android.os.Build;
import android.view.Display;

import com.core.lib.utils.main.DateUtilBase;
import com.core.lib.utils.main.FileUtilBase;
import com.core.lib.utils.main.LogUtilBase;
import com.core.lib.utils.main.StringUtilBase;

public class Utils {

	public static final boolean DEBUG = true;

	/*
	 * 程序安装第一次启动后，删除sd卡根目录文件
	 */
	public static void cleanAllFile(final Context context) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String filePath = SdLocal.getLepuRootPath(context);
				if (!StringUtilBase.stringIsEmpty(filePath)) {
					File file = new File(filePath);
					if (file.exists()) {
						FileUtilBase.deleteFile(file);
						LogUtilBase.LogD(null, "clean all file");
					}
				}
			}
		}).start();
	}

	/*
	 * 删除缓存文件
	 */
	public static void cleanCacheFile() {

	}

	public static void openOrCloseBluetooth(boolean open) {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();// 获取本地蓝牙设备
		if (bluetoothAdapter != null) {
			if (open) {
				bluetoothAdapter.enable();
			} else {
				bluetoothAdapter.disable();
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static boolean canBle(Context context) {
		boolean flag = context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE);
		return flag;
	}

	/**
	 * * 返回当前屏幕是否为竖屏。 *
	 * 
	 * @param context
	 *            * @return 当且仅当当前屏幕为竖屏时返回true,否则返回false。
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static boolean isScreenOriatationPortrait(Activity activity) {
		// return context.getResources().getConfiguration().orientation ==
		// Configuration.ORIENTATION_PORTRAIT;

		boolean flag = false;
		Display display = activity.getWindowManager().getDefaultDisplay();

		int width = display.getWidth();
		int height = display.getHeight();

		if (width < height) {
			flag = true;
		}
		return flag;
	}

	/*
	 * 判断手机当前环境是否可以录音
	 */
	public static boolean isCanAudioRecord() {
		boolean isCan = false;
		try {
			new MediaRecorder();
			isCan = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isCan;
	}

	public static int getStatusBarHeight(Activity activity) {
		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		return statusBarHeight;
	}

	/**
	 * 判断某字符串数组中是否含有某字符串
	 * 
	 * @return 存在与否
	 */
	public static boolean isHave(String[] strs, String s) {
		boolean flag = false;
		if (strs != null && strs.length > 0) {
			for (int i = 0; i < strs.length; i++) {
				if (s.startsWith(strs[i])) {
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	/*
	 * 获取当前时间，是测血糖的什么时间
	 */
	public static int getCurrentBloodMeal() {
		int meal = 0;

		String currentDate = DateUtilBase.stringFromDate(new Date(),
				DateUtilBase.DATE_HOUR_MINUTE);
		Double time = Double.valueOf(currentDate.replace(":", "."));

		if (time >= 2.3 && time <= 7.3) {
			meal = 0;
		} else if (time >= 7.3 && time <= 9.3) {
			meal = 1;
		} else if (time >= 9.3 && time <= 11.3) {
			meal = 2;
		} else if (time >= 11.3 && time <= 14.3) {
			meal = 3;
		} else if (time >= 14.3 && time <= 18.3) {
			meal = 4;
		} else if (time >= 18.3 && time <= 20.3) {
			meal = 5;
		} else if (time >= 20.3 && time <= 22.3) {
			meal = 6;
		} else if (time >= 22.3 && time <= 2.3) {
			meal = 7;
		}

		return meal;
	}

	// 将二进制字符串转换回字节
	public static byte bit2byte(String bString) {
		byte result = 0;
		for (int i = bString.length() - 1, j = 0; i >= 0; i--, j++) {
			result += (Byte.parseByte(bString.charAt(i) + "") * Math.pow(2, j));
		}
		return result;
	}

	public static void testInitResultDouble(double[] tempDoubleArray) {
		
		tempDoubleArray[0] = 0;
		tempDoubleArray[1] = 0;
		tempDoubleArray[2] = 0;
		tempDoubleArray[3] = 0;
		tempDoubleArray[4] = 0;

		tempDoubleArray[5] = 0;
		tempDoubleArray[6] = 0;
		tempDoubleArray[7] = 0;
		tempDoubleArray[8] = 0;
		tempDoubleArray[9] = 0;

		tempDoubleArray[10] = 0;
		tempDoubleArray[11] = 0;
		tempDoubleArray[12] = 0;
		tempDoubleArray[13] = 0;
		tempDoubleArray[14] = 0;

		tempDoubleArray[15] = 0;
		tempDoubleArray[16] = 0;
		tempDoubleArray[17] = 0.0183;
		tempDoubleArray[18] = -1.537;
		tempDoubleArray[19] = 100.44;

		tempDoubleArray[20] = 0;
		tempDoubleArray[21] = 0;
		tempDoubleArray[22] = -0.0369;
		tempDoubleArray[23] = 0.0918;
		tempDoubleArray[24] = 11.774;
	}
}
