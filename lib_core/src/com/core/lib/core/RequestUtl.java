package com.core.lib.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class RequestUtl {

	public static final String HTTPMETHOD_GET = "GET";
	public static final String HTTPMETHOD_POST = "POST";
	public static final String CHARSETNAME = "UTF-8";

	public static final int TIME_OUT_1S = 30 * 1000;//
	public static final int TIME_OUT_2S = 8 * 1000;//

	private static String mUserAgent = "";

	public static String getUserAgent() {
		return mUserAgent;
	}

	public static byte[] inflaterData(byte[] data) {
		byte[] result = null;
		InflaterInputStream inflater = null;
		ByteArrayOutputStream baos = null;
		try {
			inflater = new InflaterInputStream(new ByteArrayInputStream(data));
			byte[] buf = new byte[1024];
			int len;
			baos = new ByteArrayOutputStream();
			while ((len = inflater.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			result = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} finally {
			try {
				if (baos != null)
					baos.close();
				if (inflater != null)
					inflater.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (result != null && result.length > 0)
			return result;
		else
			return data;
	}

	public static byte[] unGZIPData(byte[] data) {
		byte[] result = null;
		GZIPInputStream gzip = null;
		ByteArrayOutputStream baos = null;
		try {
			gzip = new GZIPInputStream(new ByteArrayInputStream(data));
			byte[] buf = new byte[1024];
			int len;
			baos = new ByteArrayOutputStream();
			while ((len = gzip.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			result = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} finally {
			try {
				if (baos != null)
					baos.close();
				if (gzip != null)
					gzip.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (result != null && result.length > 0)
			return result;
		else
			return data;
	}

	public static byte[] readInputStream(InputStream inStream,
			String contentEncoding) throws Exception {
		if (inStream == null)
			return null;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		byte[] data = null;
		if (contentEncoding != null
				&& contentEncoding.toLowerCase().contains("deflate"))
			data = inflaterData(outStream.toByteArray());
		else if (contentEncoding != null
				&& contentEncoding.toLowerCase().contains("gzip"))
			data = unGZIPData(outStream.toByteArray());
		else
			data = outStream.toByteArray();
		outStream.close();
		outStream = null;
		return data;
	}

	public static void setMethod(HttpURLConnection urlConnection, String method)
			throws ProtocolException {
		if (urlConnection instanceof HttpsURLConnection) {
			// Create a trust manager that does not validate
			// certificate
			// chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[] {};
				}

				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}
			} };

			// Install the all-trusting trust manager
			try {
				SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				((HttpsURLConnection) urlConnection).setSSLSocketFactory(sc
						.getSocketFactory());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		urlConnection.setRequestMethod(method);
		if (method.equals(RequestUtl.HTTPMETHOD_POST)) {
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);

			urlConnection.setConnectTimeout(RequestUtl.TIME_OUT_1S);
			urlConnection.setReadTimeout(RequestUtl.TIME_OUT_2S);
		} else {
			urlConnection.setConnectTimeout(RequestUtl.TIME_OUT_1S);
			urlConnection.setReadTimeout(RequestUtl.TIME_OUT_1S);
		}

		urlConnection.setRequestProperty("If-Modified-Since",
				"Wed, 15 Nov 1995 04:58:08 GMT");
		urlConnection.setRequestProperty("Version", "HTTP/1.1");
		urlConnection.setRequestProperty("Accept", "*/*");
		urlConnection.setRequestProperty("Accept-Encoding", "deflate,gzip");
		urlConnection.setRequestProperty("Accept-Charset", "GB2312,utf-8");
		urlConnection.setRequestProperty("Connection", "keep-alive");
		urlConnection.setRequestProperty("User-agent", getUserAgent());
	}

	public static void writeDataBytes(byte[] postData, OutputStream output)
			throws Exception {
		InputStream input = new ByteArrayInputStream(postData);
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = input.read(buffer)) != -1) {
			output.write(buffer, 0, len);
		}
		input.close();
	}
}
