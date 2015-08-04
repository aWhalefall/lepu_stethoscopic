package com.core.lib.utils.secure;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

import com.core.lib.utils.main.LogUtilBase;

public class EncodeMD5 {

	private static final String charSetName = "UTF-8";
	private static String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String getMd5(byte b[]) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(b, 0, b.length);
		return byteArrayToHexString(md5.digest());
	}

	public static String getMd5(String data) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte b[] = data.getBytes(charSetName);
			md5.update(b, 0, b.length);
			return byteArrayToHexString(md5.digest());
		} catch (NoSuchAlgorithmException e) {
			LogUtilBase.LogD("NoSuchAlgorithmException:", Log.getStackTraceString(e));
		} catch (UnsupportedEncodingException e) {
			LogUtilBase.LogD("UnsupportedEncodingException:", Log.getStackTraceString(e));
		}
		return "";
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			sb.append(byteToHexString(b[i]));

		return sb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
}
