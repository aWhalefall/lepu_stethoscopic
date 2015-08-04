package com.core.lib.utils.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";
	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static CrashHandler mInstance = null;
	// 程序的Context对象
	private Context mContext;
	private CrashHandlerListener mListener;
	// 用来存储设备信息和异常信息
	private Map<String, String> mInfos = new HashMap<String, String>();

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if(mInstance == null)
		{
			mInstance = new CrashHandler();
		}
		return mInstance;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context, CrashHandlerListener listener) {
		mContext = context;
		mListener = listener;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				LogUtilBase.LogD(TAG, "error : " + e);
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				if (mContext != null && mListener != null) {
					int titleId = mListener.getToastStringId();
					if (titleId != 0)
					{
						UIHelper.showToast(mContext, titleId,Toast.LENGTH_LONG);
					}
				}
				Looper.loop();
			}
		}.start();

		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		String exinfo = GetCrashInfo(ex);
		if (mListener != null)
			mListener.setException(true, exinfo);
		return true;
	}

	public void sendMessageToServer(final String exinfo, final Context context, boolean needDialog) {
		
		if(!needDialog)
		{
			new Thread(new Runnable() {
				public void run() {
					// 上传log
					uploadLog(exinfo);
				}
			}).start();
			return;
		}
		
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						// 上传log
						uploadLog(exinfo);
						// savelocal log
						saveLogLocal(context, exinfo);
					}
				}).start();
			}
		};
		OnClickListener cancleListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null)
					mListener.setException(false, "");
			}
		};

		if (mListener != null) {
			mListener.showExceptionDialog(listener, cancleListener);
		}
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);

			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				mInfos.put("versionName", versionName);
				mInfos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			LogUtilBase.LogD(TAG, "an error occured when collect package info "
					+ e);
		}

		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mInfos.put(field.getName(), field.get(null).toString());
				LogUtilBase
						.LogD(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				LogUtilBase.LogD(TAG,
						"an error occured when collect crash info " + e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String GetCrashInfo(Throwable ex) {

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : mInfos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}

		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		return sb.toString();
	}

	public void uploadLog(String crashLog) {
		URL url = null;

		try {
			url = new URL(mListener.getUploadLogUrl());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return;
		}

		HttpURLConnection urlConnection = null;

		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			if (urlConnection != null) {
				urlConnection.setConnectTimeout(5000);
				urlConnection.setReadTimeout(5000);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			OutputStream output = null;
			if (urlConnection != null) {
				urlConnection.setDoOutput(true);
				output = urlConnection.getOutputStream();
			}
			String body = "";
			if (crashLog != null && crashLog.length() > 0) {
				body = "l=" + URLEncoder.encode(crashLog, "UTF-8");
			}
			if (output != null) {
				output.write(body.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			if (urlConnection != null) {
				int responseCode = urlConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					// success
					// 更新本地异常为空
					mListener.setException(false, "");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveLogLocal(Context context, String exinfo) {
		File appdir = null;
		if (context != null) {
			appdir = context.getFilesDir();
		}
		if (appdir != null && appdir.exists()) {
			String fileName = "crash.log";
			String path = appdir.getAbsolutePath();
			try {
				FileOutputStream fos = new FileOutputStream(path + fileName);

				if (fos != null && exinfo != null && exinfo.length() > 0) {
					fos.write(exinfo.getBytes());
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				LogUtilBase.LogD(TAG, "an error occured while writing file..."
						+ e);
			}
		}
	}

	public interface CrashHandlerListener {
		public int getToastStringId();
		public void setException(boolean hasException, String exceptionString);
		public void showExceptionDialog(OnClickListener okClick,
				OnClickListener cancelClick);
		public String getUploadLogUrl();
	}
}
