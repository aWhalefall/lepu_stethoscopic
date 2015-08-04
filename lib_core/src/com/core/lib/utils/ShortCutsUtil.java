package com.core.lib.utils;

import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.text.TextUtils;

public class ShortCutsUtil {

	/**
	 * 创建一个快捷方式
	 */

	@SuppressWarnings("rawtypes")
	public static void createShortCut(Context context, Class activityClass,
			int iconId, int stringId) {
		if (!isAddShortCut(context, stringId)) {
			Intent intent = new Intent(context, activityClass);
			/* 以下两句是为了在卸载应用的时候同时删除桌面快捷方式 */
			intent.setAction("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.LAUNCHER");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			Intent shortcutintent = new Intent(
					"com.android.launcher.action.INSTALL_SHORTCUT");
			shortcutintent.putExtra("duplicate", false);
			shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
					context.getString(stringId));
			Parcelable icon = Intent.ShortcutIconResource.fromContext(
					context.getApplicationContext(), iconId);
			shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
			// 点击快捷图片，运行的程序主入口
			shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
			context.sendBroadcast(shortcutintent);
		}
	}

	/*
	 * 删除一个快捷方式
	 */
	@SuppressWarnings("rawtypes")
	public static void deleteShortCut(Context context, Class activityClass,
			int iconId, int stringId) {

		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");
		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(stringId));

		Intent intent = new Intent(context, activityClass);
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");

		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		context.sendBroadcast(shortcut);
	}

	/*
	 * 当前快捷方式是否存在创建
	 */
	public static boolean isAddShortCut(Context context, int appNameId) {
		boolean isInstallShortcut = false;
		final ContentResolver cr = context.getContentResolver();

		final String AUTHORITY = getAuthorityFromPermission(context,
				"com.android.launcher.permission.READ_SETTINGS");

		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI,
				new String[] { "title", "iconResource" }, "title=?",
				new String[] { context.getString(appNameId) }, null);

		if (c != null && c.getCount() > 0) {
			isInstallShortcut = true;
		}
		return isInstallShortcut;
	}

	private static String getAuthorityFromPermission(Context context,
			String permission) {
		if (TextUtils.isEmpty(permission)) {
			return null;
		}
		List<PackageInfo> packs = context.getPackageManager()
				.getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (packs == null) {
			return null;
		}
		for (PackageInfo pack : packs) {
			ProviderInfo[] providers = pack.providers;
			if (providers != null) {
				for (ProviderInfo provider : providers) {
					if (permission.equals(provider.readPermission)
							|| permission.equals(provider.writePermission)) {
						return provider.authority;
					}
				}
			}
		}
		return null;
	}
}
