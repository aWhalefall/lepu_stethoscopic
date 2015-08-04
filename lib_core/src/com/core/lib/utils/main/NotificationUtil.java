package com.core.lib.utils.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationUtil {

	public static final int REQUEST_CODE = 1000;

	private static NotificationUtil mNotificationUtil = null;
	private static NotificationManager mNotificationManager = null;
	
	private NotificationUtil()
	{
		
	}

	public static NotificationUtil getInstance(Context context) {
		if (mNotificationUtil == null) {
			mNotificationUtil = new NotificationUtil();
			mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		return mNotificationUtil;
	}

	public void showNotification(Context context, int iconId, int notifyId,
			String title, String content, Intent openIntent) {

		long when = System.currentTimeMillis();

		Notification notification = new Notification(iconId, title, when);// 第一个参数为图标,第二个参数为短暂提示标题,第三个为通知时间
		notification.defaults = Notification.DEFAULT_SOUND;// 发出默认声音
		notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击通知后自动清除通知

		PendingIntent contentIntent = PendingIntent.getActivity(context,
				REQUEST_CODE, openIntent, PendingIntent.FLAG_UPDATE_CURRENT);// 当点击消息时就会向系统发送openintent意图

		notification.setLatestEventInfo(context, title, content, contentIntent);
		mNotificationManager.notify(notifyId, notification);// 第一个参数为自定义的通知唯一标识
	}

}
