package com.core.lib.core;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.core.lib.utils.UploadFileType;
import com.core.lib.utils.main.LogUtilBase;
import com.core.lib.utils.main.StringUtilBase;
import com.core.lib.utils.main.UtilityBase;

public class HttpRequest {

	private static Object lockobj = new Object();

	public static JSONObject httpRequestJSONObject(String url) {
		JSONObject object = null;
		try {
			LogUtilBase.LogD("Debug", "url=" + url);
			URL currentUrl = new URL(url);
			HttpURLConnection urlConnection = null;
			try {
				urlConnection = (HttpURLConnection) currentUrl.openConnection();
				RequestUtl.setMethod(urlConnection, RequestUtl.HTTPMETHOD_GET);

				InputStream stream = urlConnection.getInputStream();
				try {
					byte[] data = RequestUtl.readInputStream(stream,
							urlConnection.getContentEncoding());
					String json = new String(data, RequestUtl.CHARSETNAME);
					LogUtilBase.LogD("Debug", "json = " + json);
					object = new JSONObject(json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (urlConnection != null) {
					try {
						urlConnection.disconnect();
					} catch (Exception e) {
					}
					urlConnection = null;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return object;
	}

	public static String httpRequestString(String url) {
		String result = null;
		try {
			URL currentUrl = new URL(url);
			HttpURLConnection urlConnection = null;
			try {
				urlConnection = (HttpURLConnection) currentUrl.openConnection();
				RequestUtl.setMethod(urlConnection, RequestUtl.HTTPMETHOD_GET);
				InputStream stream = urlConnection.getInputStream();
				try {
					byte[] data = RequestUtl.readInputStream(stream,
							urlConnection.getContentEncoding());
					result = new String(data, RequestUtl.CHARSETNAME);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (urlConnection != null) {
					try {
						urlConnection.disconnect();
					} catch (Exception e) {
					}
					urlConnection = null;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 异步get
	public static void asyncGet(final String url) {
		HttpRequest.asyncRequest(url, null, null);
	}

	// 异步post
	public static void asyncPost(final String url, final byte[] postData) {
		HttpRequest.asyncRequest(url, postData, null);
	}

	// 异步请求，并回调
	public static void asyncRequest(final String url, final byte[] postData,
			final OnRequestComplete callback) {
		LogUtilBase.LogD("http", "out: " + url);
		Thread t = new Thread(new Runnable() {

			public void run() {
				HttpURLConnection urlConnection = null;

				try {
					String targetUrl = url;
					URL currentUrl = new URL(targetUrl);
					urlConnection = (HttpURLConnection) currentUrl
							.openConnection();

					if (postData != null && postData.length > 0) {
						RequestUtl.setMethod(urlConnection,
								RequestUtl.HTTPMETHOD_POST);
						urlConnection.setRequestProperty("Content-Length",
								String.valueOf(postData.length));
						OutputStream writer = urlConnection.getOutputStream();

						RequestUtl.writeDataBytes(postData, writer);
						// writer.write(postData);
						writer.flush();
						writer.close();
					} else {
						RequestUtl.setMethod(urlConnection,
								RequestUtl.HTTPMETHOD_GET);
					}

					urlConnection.connect();

					InputStream reader = null;
					try {
						reader = urlConnection.getInputStream();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					int httpCode = urlConnection.getResponseCode();
					Map<String, List<String>> headers = urlConnection
							.getHeaderFields();
					byte[] data = RequestUtl.readInputStream(reader,
							urlConnection.getContentEncoding());
					if (callback != null) {
						// 回调
						callback.onComplete(httpCode, headers, data);
					}

				} catch (Exception e) {
					if (e != null) {
						e.printStackTrace();
					}
					if (callback != null)
						callback.onError(e);
				} finally {
					if (urlConnection != null) {
						try {
							urlConnection.disconnect();
						} catch (Exception e) {
						}
						urlConnection = null;
					}
				}
			}
		});
		t.start();
	}

	// 下载文件
	public static boolean downloadFile(String fileUrl, String localPath) {
		URLConnection urlConnection = null;
		try {
			URL url = new URL(fileUrl);
			urlConnection = url.openConnection();
			urlConnection.connect();
			InputStream input = urlConnection.getInputStream();
			File file = new File(localPath);
			file.createNewFile();
			FileOutputStream output = new FileOutputStream(file);

			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = input.read(buffer)) != -1) {
				output.write(buffer, 0, len);
			}

			input.close();
			output.flush();
			output.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void downloadFile(final String url, final String filePath,
			final OnDownloadProgress callBack) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpURLConnection urlConnection = null;

				try {
					String targetUrl = url;
					URL currentUrl = new URL(targetUrl);
					urlConnection = (HttpURLConnection) currentUrl
							.openConnection();
					RequestUtl.setMethod(urlConnection,
							RequestUtl.HTTPMETHOD_GET);

					InputStream input = null;
					FileOutputStream output = null;
					try {
						input = urlConnection.getInputStream();
						// String contentEncoding =
						// urlConnection.getContentEncoding();
						int totalLength = urlConnection.getContentLength();
						byte[] buffer = new byte[1024];
						output = new FileOutputStream(filePath, false);
						int readLength = 0;
						int progress = 0;
						while (true) {
							int count = input.read(buffer);
							if (count <= 0) {
								break;
							}
							output.write(buffer, 0, count);
							readLength += count;
							int cur = readLength * 100 / totalLength;
							if (cur != progress && callBack != null) {
								progress = cur;
								callBack.onProgressChanged(progress);
							}
						}
						if (progress != 100 && callBack != null) {
							progress = 100;
							callBack.onProgressChanged(progress);
						}

					} catch (IOException ex) {
						ex.printStackTrace();
						if (callBack != null)
							callBack.onError(ex);
					} catch (Exception e) {
						if (callBack != null)
							callBack.onError(e);
					} finally {
						if (input != null) {
							try {
								input.close();
							} catch (Exception e) {
							}
						}
						if (output != null) {
							try {
								output.close();
							} catch (Exception e) {
							}
						}
					}

				} catch (Exception e) {
					if (e != null) {
						e.printStackTrace();
					}
					if (callBack != null)
						callBack.onError(e);
				} finally {
					if (urlConnection != null) {
						urlConnection.disconnect();
					}
				}
			}
		});
		t.start();
	}

	// 分享文件和内容
	public static void asyncUploadContentAndFile(final String url,
			final Map<String, String> param, final String filePath,
			final UploadFileType uploadFileType,
			final OnRequestComplete callback) {
		Thread t = new Thread(new Runnable() {
			public void run() {

				synchronized (lockobj) {
					HttpURLConnection urlConnection = null;
					try {
						String targetUrl = url;
						URL currentUrl = new URL(targetUrl);
						urlConnection = (HttpURLConnection) currentUrl
								.openConnection();

						String BOUNDARY = "-------------------------"
								+ UtilityBase.getBoundry(); // 定义数据分隔线
						String RETURN = "\r\n";
						String PREFIX = "--";
						RequestUtl.setMethod(urlConnection,
								RequestUtl.HTTPMETHOD_POST);
						urlConnection.setRequestProperty("Content-Type",
								"multipart/form-data; boundary=" + BOUNDARY);
						StringBuilder temp = new StringBuilder();
						if (param != null && param.size() > 0) {
							for (String key : param.keySet()) {
								temp.append(PREFIX + BOUNDARY + RETURN);
								temp.append("Content-Disposition:form-data; name=\""
										+ key + "\"" + RETURN);
								temp.append(RETURN);
								temp.append(param.get(key));
								temp.append(RETURN);
							}
						}

						byte[] end_data = (RETURN + PREFIX + BOUNDARY + PREFIX + RETURN)
								.getBytes();// 定义最后数据分隔线

						StringBuilder sb = new StringBuilder();
						sb.append(PREFIX + BOUNDARY + RETURN);
						File fileFile = new File(filePath);

						if (uploadFileType == UploadFileType.IMAGE_PNG) {
							sb.append("Content-Disposition:form-data; name=\"pic\"; filename=\""
									+ fileFile.getName() + "\"" + RETURN);
							sb.append("Content-Type:image/png" + RETURN
									+ RETURN);
						} else if (uploadFileType == UploadFileType.YUYIN_AMR) {
							sb.append("Content-Disposition:form-data; name=\"yuyin\"; filename=\""
									+ fileFile.getName() + "\"" + RETURN);
							sb.append("Content-Type:application/octet-stream"
									+ RETURN + RETURN);
						}

						urlConnection.setRequestProperty(
								"Content-Length",
								String.valueOf(sb.toString().getBytes().length
										+ temp.toString().getBytes().length
										+ fileFile.length() + end_data.length)); // 设置内容长度
						OutputStream out = new DataOutputStream(urlConnection
								.getOutputStream());
						out.write(temp.toString().getBytes("utf-8"));
						out.write(sb.toString().getBytes("utf-8"));

						// out.write(buffer);
						FileInputStream input = new FileInputStream(fileFile);
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = input.read(buffer)) != -1) {
							out.write(buffer, 0, len);
						}

						input.close();
						out.write(end_data);
						out.flush();
						out.close();

						urlConnection.connect();

						InputStream reader = null;
						try {
							reader = urlConnection.getInputStream();
						} catch (IOException ex) {
							ex.printStackTrace();
						}

						if (callback != null) {
							int httpCode = urlConnection.getResponseCode();
							Map<String, List<String>> headers = urlConnection
									.getHeaderFields();
							byte[] data = RequestUtl.readInputStream(reader,
									urlConnection.getContentEncoding());
							LogUtilBase.LogD("http_request_upload_image",
									"success");
							// 回调
							callback.onComplete(httpCode, headers, data);
						}

					} catch (Exception e) {
						if (e != null) {
							e.printStackTrace();
						}
						if (callback != null)
							callback.onError(e);
					} finally {
						if (urlConnection != null) {
							try {
								urlConnection.disconnect();
							} catch (Exception e) {
							}
							urlConnection = null;
						}
					}
				}
			}
		});
		t.start();
	}

	// ===============================================================================
	public static Map<String, String> parseArguments(String args) {
		Map<String, String> map = new HashMap<String, String>();
		if (!StringUtilBase.stringIsEmpty(args)) {
			String[] ss = args.split("&");
			for (int i = 0; i < ss.length; i++) {
				String arg = ss[i];
				if (arg.length() == 0)
					continue;
				String[] nv = arg.split("=");
				if (nv.length == 2)
					map.put(nv[0], nv[1]);
			}
		}
		return map;
	}

	public interface OnRequestComplete {
		public abstract void onComplete(int httpCode,
				Map<String, List<String>> headers, byte[] data);

		public abstract void onError(Exception e);
	}

	public static abstract class OnDownloadProgress {
		public abstract void onProgressChanged(int progress);

		public abstract void onError(Exception e);
	}
}
