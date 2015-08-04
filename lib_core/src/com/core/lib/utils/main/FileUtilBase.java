package com.core.lib.utils.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

import com.core.lib.core.RequestUtl;
import com.core.lib.utils.main.LogUtilBase;

public class FileUtilBase {

	/**
	 * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
	 * 
	 * @param context
	 * @param msg
	 */
	public static void write(Context context, String fileName, String content) {
		if (content == null)
			content = "";

		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			fos.write(content.getBytes());

			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文本文件
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String read(Context context, String fileName) {
		try {
			FileInputStream inStream = context.openFileInput(fileName);
			String dataString = RequestUtl.readInputStream(inStream, null)
					.toString();
			return dataString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void saveFile(FileOutputStream fos, byte[] data) {
		try {
			fos.write(data);
		} catch (FileNotFoundException e) {
			LogUtilBase.LogD("FileNotFoundException:",
					Log.getStackTraceString(e));
		} catch (IOException e) {
			LogUtilBase.LogD("IOException:", Log.getStackTraceString(e));
		} catch (Exception e) {
			LogUtilBase.LogD("Exception:", Log.getStackTraceString(e));
		} finally {
		}
	}

	public static void saveFile(String filePath, byte[] data, boolean isAppend) {
		File file = new File(filePath);

		if (file.exists()) {
			deleteFile(file);
		}

		try {
			file.createNewFile();
		} catch (IOException e) {
			LogUtilBase.LogD("createNewFile exception:",
					Log.getStackTraceString(e));
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath, isAppend);
			fos.write(data);
		} catch (FileNotFoundException e) {
			LogUtilBase.LogD("FileNotFoundException:",
					Log.getStackTraceString(e));
		} catch (IOException e) {
			LogUtilBase.LogD("IOException:", Log.getStackTraceString(e));
		} catch (Exception e) {
			LogUtilBase.LogD("Exception:", Log.getStackTraceString(e));
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (Exception e) {
					LogUtilBase.LogD("Exception:", Log.getStackTraceString(e));
				}
		}
	}

	public static void deleteFile(File file) {
		if (file == null)
			return;
		if (file.exists()) {
			if (file.isFile()) {
				try {
					file.delete();
				} catch (Exception e) {
				}
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
				try {
					file.delete();
				} catch (Exception e) {
				}
			}
		}
	}

	public static byte[] toBytes(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch;
		try {
			while ((ch = in.read()) != -1) {
				out.write(ch);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out.toByteArray();
	}

}
