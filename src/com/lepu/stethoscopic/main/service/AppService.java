package com.lepu.stethoscopic.main.service;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.core.lib.core.AsyncRequest;
import com.core.lib.utils.Reachability;

import java.util.Timer;
import java.util.TimerTask;

public class AppService extends Service implements AsyncRequest {

	private static final int NOTIFY_TIME_TEST = 1000 * 60 * 1;// 间隔1分钟
	private static final String REQUEST_TEST = "request_test";

	private static final int MSG_TEST_SUCCESS = 10;
	private static final int MSG_TEST_FAIL = 11;

	private static AppService mInstance = null;
	private NotificationManager mNotificationManager = null;
	private Timer mTestTimer = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		//startTestTimer();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		mInstance = null;

		deleteNotification();
		stopTestTimer();
	}

	public void deleteNotification() {
		// 删除通知
		if (mNotificationManager != null) {
			//mNotificationManager.cancel(NOTIFY_ID_YUJIN_LIST);
		}
	}

	public static AppService getInstance() {
		return mInstance;
	}

	/*
	 * 开启app sevice
	 */
	public static void startAppService(Context context) {
		AppService appService = AppService.getInstance();
		if (appService == null) {
			Intent serviceIntent = new Intent(context, AppService.class);
			context.startService(serviceIntent);
		}
	}

	/*
	 * 停止app service
	 */
	public static void stopAppService(Context context) {
		AppService appService = AppService.getInstance();
		if (appService != null) {
			Intent serviceIntent = new Intent(context, AppService.class);
			context.stopService(serviceIntent);
		}
	}

	private void requestTest() {
		if (Reachability.checkNetwork(mInstance)) {

		}
	}

	public void startTestTimer() {
		if (mTestTimer == null) {
			mTestTimer = new Timer();
			mTestTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					requestTest();
				}
			}, 0, NOTIFY_TIME_TEST);
		}
	}

	public void stopTestTimer() {
		if (mTestTimer != null) {
			mTestTimer.cancel();
			mTestTimer.purge();
			mTestTimer = null;
		}
	}

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_TEST_SUCCESS:

				break;
			case MSG_TEST_FAIL:

				break;
			default:
				break;
			}
		};
	};

	@Override
	public void RequestComplete(Object context, Object data) {
		if (context.equals(REQUEST_TEST)) {

		}
	}

	
	@Override
	public void RequestError(Object context, int errorId, String errorMessage) {
		if (context.equals(REQUEST_TEST)) {
			
		}
	}
}
