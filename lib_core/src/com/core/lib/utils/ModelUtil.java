package com.core.lib.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.util.Log;

import com.core.lib.utils.main.LogUtilBase;

public class ModelUtil {

	public static ArrayList<HashMap<String, Object>> query(Cursor cursor) {
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		try {
			while (cursor.moveToNext()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				int count = cursor.getColumnCount();
				for (int i = 0; i < count; i++) {
					map.put(cursor.getColumnName(i), getColumnValue(cursor, i));
				}
				result.add(map);
			}
		} catch (Exception e) {
			LogUtilBase.LogD("Exception:", Log.getStackTraceString(e));
		} finally {
			try {
				if (cursor != null)
					cursor.close();
			} catch (Exception e) {
				LogUtilBase.LogD("Exception:", Log.getStackTraceString(e));
			}
		}

		return result;
	}

	public static Object getColumnValue(Cursor cursor, int index) {
		Object result = null;
		try {
			result = cursor.getString(index);
		} catch (Exception e) {
		}
		return result;
	}

}
