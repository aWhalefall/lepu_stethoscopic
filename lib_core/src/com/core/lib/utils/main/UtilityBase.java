package com.core.lib.utils.main;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.lib.custom.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class UtilityBase {

	// 上线，需要更新这个值为false
	public static final boolean DEBUG = true;

	// 比较 版本
	public static int compareVersion(String oldVersion, String newVersion) {
		if (StringUtilBase.stringIsEmpty(newVersion)
				|| StringUtilBase.stringIsEmpty(oldVersion))
			return -1;
		String[] olds = oldVersion.split("\\.");
		String[] news = newVersion.split("\\.");
		for (int i = 0; i < olds.length; i++) {
			int m = parseInt(olds[i]);
			int n = 0;
			if (i < news.length)
				n = parseInt(news[i]);
			if (m > n)
				return -1;
			else if (m < n)
				return 1;
		}
		return 0;
	}

	public static boolean parseBoolean(String string) {
		boolean result = false;
		if (!StringUtilBase.stringIsEmpty(string)) {
			try {
				result = Boolean.parseBoolean(string);
			} catch (NumberFormatException e) {
				LogUtilBase.LogD("NumberFormatException:",
						Log.getStackTraceString(e));
			}
		}
		return result;
	}

	public static String parseString(String string) {
		String result = "";
		if (!StringUtilBase.stringIsEmpty(string)) {
			try {
				result = string;
			} catch (NumberFormatException e) {
				LogUtilBase.LogD("NumberFormatException:",
						Log.getStackTraceString(e));
			}
		}
		return result;
	}

	public static int parseInt(String string) {
		int result = 0;
		if (!StringUtilBase.stringIsEmpty(string)) {
			try {
				result = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				LogUtilBase.LogD("NumberFormatException:",
						Log.getStackTraceString(e));
				result = 0;
			}
		}
		return result;
	}

	public static long parseLong(String string) {
		long result = 0;
		if (!StringUtilBase.stringIsEmpty(string)) {
			try {
				result = Long.parseLong(string);
			} catch (NumberFormatException e) {
				LogUtilBase.LogD("NumberFormatException:",
						Log.getStackTraceString(e));
				result = 0;
			}
		}
		return result;
	}

	public static double parseDouble(String string) {
		double result = 0.0;
		if (!StringUtilBase.stringIsEmpty(string)) {
			try {
				result = Double.parseDouble(string);
			} catch (NumberFormatException e) {
				LogUtilBase.LogD("NumberFormatException:",
						Log.getStackTraceString(e));
				result = 0.0;
			}
		}
		return result;
	}

	public static float parseFloat(String string) {
		float result = 0.0f;
		if (!StringUtilBase.stringIsEmpty(string)) {
			try {
				result = Float.parseFloat(string);
			} catch (NumberFormatException e) {
				LogUtilBase.LogD("NumberFormatException:",
						Log.getStackTraceString(e));
				result = 0.0f;
			}
		}
		return result;
	}

	public static String combinePath(String path1, String path2) {
		String result = null;
		if (StringUtilBase.stringIsEmpty(path1))
			result = path2;
		else if (StringUtilBase.stringIsEmpty(path2))
			result = path1;
		else {
			if (!path1.endsWith("/"))
				result = path1 + "/";
			else
				result = path1;
			if (path2.startsWith("/"))
				result += path2.substring(1);
			else
				result += path2;
		}
		return StringUtilBase.stringIsEmpty(result) ? "" : result;
	}

	// 当get请求时，拼接参数字符串
	public static String connectParamsUrl(String url,
			HashMap<String, String> dataParams) {

		HashMap<String, String> params = (HashMap<String, String>) dataParams;
		StringBuilder sb = new StringBuilder();
		if (params != null && !params.isEmpty()) {
			try {
				if (!url.endsWith("?"))
					sb.append('&');

				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append(entry.getKey()).append('=');
					sb.append(URLEncoder.encode(entry.getValue(), "utf-8"));
					sb.append('&');
				}
				sb.deleteCharAt(sb.length() - 1);
			} catch (Exception e) {
				LogUtilBase.LogD("Exception:", Log.getStackTraceString(e));
			}
		}
		return url + sb.toString();
	}

	public static String getMapKeyValueString(HashMap<String, String> params) {
		String value = "";
		if (params != null && params.size() > 0) {
			StringBuilder sb = new StringBuilder();
			try {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append(entry.getKey()).append('=');
					sb.append(URLEncoder.encode(entry.getValue(), "utf-8"));
					sb.append('&');
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			sb.deleteCharAt(sb.length() - 1);
			value = sb.toString();
		}
		return value;
	}

	public static String getAppMetedata(Context context, String name) {
		ApplicationInfo info = null;
		try {
			info = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			LogUtilBase.LogD("NameNotFoundException:",
					Log.getStackTraceString(e));
		}
		if (info != null)
			return info.metaData.getString(name);
		else
			return null;
	}

	public static String getDataEncode(String content, String charsetName) {
		String contentEncode = "";

		try {
			contentEncode = URLEncoder.encode(content, charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return contentEncode;
	}

	/**
	 * 产生11位的boundary
	 */
	public static String getBoundry() {
		StringBuffer sb = new StringBuffer();
		for (int t = 1; t < 12; t++) {
			long time = System.currentTimeMillis() + t;
			if (time % 3 == 0) {
				sb.append((char) time % 9);
			} else if (time % 3 == 1) {
				sb.append((char) (65 + time % 26));
			} else {
				sb.append((char) (97 + time % 26));
			}
		}
		return sb.toString();
	}

	public static String getStringByResId(Context context, int resId) {
		return context.getString(resId);
	}

	public static boolean isApplicationBackground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public static void isApplicationEnterBackground(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(intent);
	}

	/*
	 * 往通讯录中添加客服电话
	 */
	public static void addCustomerPhone(Context context, String name,
			String phone) {
		ContentValues values = new ContentValues();
		Uri rawContactUri = context.getContentResolver().insert(
				RawContacts.CONTENT_URI, values);
		if (rawContactUri != null) {
			long rawContactsId = ContentUris.parseId(rawContactUri);
			// 往刚才的空记录中插入姓名
			values.clear();
			values.put(StructuredName.RAW_CONTACT_ID, rawContactsId);
			values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
			values.put(StructuredName.DISPLAY_NAME, name);
			context.getContentResolver().insert(Data.CONTENT_URI, values);
			// 插入电话
			values.clear();
			values.put(Phone.RAW_CONTACT_ID, rawContactsId);
			values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
			values.put(Phone.NUMBER, phone);
			try {
				context.getContentResolver().insert(Data.CONTENT_URI, values);
			} catch (Exception e) {
				LogUtilBase.LogD("insertPhoneException:",
						Log.getStackTraceString(e));
			}

		}
	}

	/*
	 * 判断通讯录中是否已有客服电话
	 */
	public static boolean haveCustomerPhone(Context context, String phone) {
		boolean customer = false;
		// 获得所有的联系人
		Cursor cur = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (cur != null && cur.moveToFirst()) {
			int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
			// int displayNameColumn =
			// cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			do {
				// 获得联系人的ID号
				String contactId = cur.getString(idColumn);
				// 获得联系人姓名
				// String disPlayName = cur.getString(displayNameColumn);
				// 查看该联系人有多少个电话号码。如果没有这返回值为0
				int phoneCount = cur
						.getInt(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				if (phoneCount > 0) {
					// 获得联系人的电话号码
					Cursor phones = context.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
					if (phones != null && phones.moveToFirst()) {
						do {
							// 遍历所有的电话号码
							String phoneNumber = phones
									.getString(phones
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							if (phoneNumber.equals(phone)) {
								customer = true;
							}
						} while (phones.moveToNext());
					}
				}

			} while (cur.moveToNext());

		}
		return customer;
	}

	public static void share(Context context, String name, String info,
			String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imgPath == null || imgPath.equals("")) {
			intent.setType("text/plain");
		} else {
			File f = new File(imgPath);
			if (f != null && f.exists() && f.isFile()) {
				intent.setType("image/png");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
			}
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, name);
		intent.putExtra(Intent.EXTRA_TEXT, info);
		// 彩信获取文字内容
		intent.putExtra("sms_body", info);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		context.startActivity(Intent.createChooser(intent, name));
	}

	public static boolean isMobileNum(String phoneNumber) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(phoneNumber);
		return m.matches();
	}

	/*
	 * 获取保留小数点几位的数字
	 */
	public static String getStringNumber(int bit, double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(bit, BigDecimal.ROUND_HALF_UP);

		return String.valueOf(bd);
	}

	/*
	 * 安装apk
	 */
	public static void installApk(Context context, String path) {
		if (path == null || path.length() == 0) {
			return;
		}

		File apkfile = new File(path);
		if (!apkfile.exists()) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/*
	 * 卸载apk
	 */
	public static void uninstallApk(Context context, String packageName) {
		String uri = "package:" + packageName;
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
				Uri.parse(uri));
		context.startActivity(uninstallIntent);
	}

	public static int getTextNumberSize(Activity activity) {
		int numberSize = activity.getResources().getDimensionPixelSize(
				R.dimen.lib_text_medium);
		LogUtilBase.LogD(null, "textNumberSize=" + numberSize);
		return numberSize;
	}

	public static String parseJsonKeyString(JSONObject dataObj, String key) {
		String flag = "";
		if (!dataObj.isNull(key)) {
			try {
				flag = dataObj.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public static boolean parseJsonKeyBoolean(JSONObject dataObj, String key) {
		boolean flag = false;
		if (!dataObj.isNull(key)) {
			try {
				flag = dataObj.getBoolean(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public static int parseJsonKeyInt(JSONObject dataObj, String key) {
		int flag = 0;
		if (!dataObj.isNull(key)) {
			try {
				flag = dataObj.getInt(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public static long parseJsonKeyLong(JSONObject dataObj, String key) {
		long flag = 0L;
		if (!dataObj.isNull(key)) {
			try {
				flag = dataObj.getLong(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
}
