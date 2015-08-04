package com.core.lib.core;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.core.lib.utils.main.AppManager;
import com.core.lib.utils.main.ImageUtilBase;
import com.core.lib.utils.main.LogUtilBase;
import com.core.lib.utils.main.StringUtilBase;
import com.core.lib.utils.main.UIHelper;
import com.core.lib.utils.main.UtilityBase;
import com.lib.custom.R;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class ApiClient {

    private final static int RETRY_TIMES = 2;// 网络请求失败后，重试次数
    private final static int RETRY_SLEEP_TIME = 1000;//延时多长重试

    private final static int EXCEPTION_HTTP = 0;
    private final static int EXCEPTION_IO = 1;
    private final static int EXCEPTION_OTHER = 2;

    private static HttpClient getHttpClient() {
        HttpClient httpClient = new HttpClient();
        // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        httpClient.getParams().setCookiePolicy(
                CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策略
        httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        // 设置 连接超时时间
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(RequestUtl.TIME_OUT_1S);
        // 设置 读数据超时时间
        httpClient.getHttpConnectionManager().getParams()
                .setSoTimeout(RequestUtl.TIME_OUT_1S);
        // 设置 字符集
        httpClient.getParams().setContentCharset(RequestUtl.CHARSETNAME);
        return httpClient;
    }

    private static GetMethod getHttpGet(String url, String cookie,
                                        String userAgent) {
        GetMethod httpGet = new GetMethod(url);
        // 设置 请求超时时间
        httpGet.getParams().setSoTimeout(RequestUtl.TIME_OUT_1S);
        httpGet.setRequestHeader("Connection", "Keep-Alive");
        httpGet.setRequestHeader("Cookie", cookie);
        httpGet.setRequestHeader("User-Agent", userAgent);
        return httpGet;
    }

    private static PostMethod getHttpPost(String url, String cookie,
                                          String userAgent) {
        PostMethod httpPost = new PostMethod(url);
        // 设置 请求超时时间
        httpPost.getParams().setSoTimeout(RequestUtl.TIME_OUT_1S);
        httpPost.setRequestHeader("Connection", "Keep-Alive");
        httpPost.setRequestHeader("Cookie", cookie);
        httpPost.setRequestHeader("User-Agent", userAgent);
        return httpPost;
    }

    private static String getMimeType(String fileUrl) throws java.io.IOException {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(fileUrl);
        return type;
    }

    /**
     * get请求URL
     */

    public static void http_get(final String url, HashMap<String, String> params, final AsyncRequest callBackRequest,
                                final String request) {
        String formatUrl = UtilityBase.connectParamsUrl(url, params);
        http_get(formatUrl, callBackRequest, request);
    }

    public static void http_get(final String url, final AsyncRequest callBackRequest,
                                final String request) {

        LogUtilBase.LogD(null, "ApiClient http_get==>request==" + request + url);

        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpClient httpClient = null;
                GetMethod httpGet = null;

                String responseBody = "";
                int time = 0;
                do {
                    try {
                        httpClient = getHttpClient();
                        httpGet = getHttpGet(url, null, RequestUtl.getUserAgent());
                        int statusCode = httpClient.executeMethod(httpGet);
                        // do just go forward
                        if (statusCode == 500 || statusCode == 200) {
                            //php 服务器，有时响应500，也有正确数据。
                            responseBody = httpGet.getResponseBodyAsString();
                            responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
                        }

                        if (callBackRequest != null) {
                            callBackRequest.RequestComplete(request, responseBody);
                        }
                        break;
                    } catch (HttpException e) {
                        // 发生致命的异常，可能是协议不对或者返回的内容有问题

                        String logString = Log.getStackTraceString(e);
                        LogUtilBase.LogD(null, logString);

                        time++;
                        if (time < RETRY_TIMES) {
                            try {
                                Thread.sleep(RETRY_SLEEP_TIME);
                            } catch (InterruptedException e1) {
                            }
                            continue;
                        } else {
                            if (callBackRequest != null) {
                                callBackRequest.RequestError(request, EXCEPTION_HTTP,
                                        logString);
                            }
                        }
                    } catch (IOException e) {
                        // 发生网络异常
                        String logString = Log.getStackTraceString(e);
                        LogUtilBase.LogD(null, logString);

                        time++;
                        if (time < RETRY_TIMES) {
                            try {
                                Thread.sleep(RETRY_SLEEP_TIME);
                            } catch (InterruptedException e1) {
                            }
                            continue;
                        } else {
                            if (callBackRequest != null) {
                                callBackRequest.RequestError(request, EXCEPTION_IO,
                                        logString);
                            }
                        }
                    } catch (Exception e) {
                        // 发生异常
                        String logString = Log.getStackTraceString(e);
                        LogUtilBase.LogD(null, logString);

                        time++;
                        if (time < RETRY_TIMES) {
                            try {
                                Thread.sleep(RETRY_SLEEP_TIME);
                            } catch (InterruptedException e1) {
                            }
                            continue;
                        } else {
                            if (callBackRequest != null) {
                                callBackRequest.RequestError(request, EXCEPTION_OTHER,
                                        logString);
                            }
                        }
                    } finally {
                        // 释放连接
                        httpGet.releaseConnection();
                        httpClient = null;
                    }
                } while (time < RETRY_TIMES);
            }
        }).start();
    }

    public static void http_post(final String url, final Map<String, Object> params,
                                 final Map<String, File> files, final AsyncRequest callBackRequest,
                                 final String request) {

        LogUtilBase.LogD(null, "ApiClient http_post==>request==>" + request + url);

        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpClient httpClient = null;
                PostMethod httpPost = null;

                // post表单参数处理
                int length = (params == null ? 0 : params.size())
                        + (files == null ? 0 : files.size());
                Part[] parts = new Part[length];
                int i = 0;
                if (params != null) {
                    for (String name : params.keySet()) {
                        parts[i++] = new StringPart(name, String.valueOf(params
                                .get(name)), RequestUtl.CHARSETNAME);
                    }
                }

                if (files != null)
                    for (String file : files.keySet()) {
                        try {
                            File myFile = files.get(file);
                            String myFileName = myFile.getName();
                            FilePart fp = new FilePart(file, myFile);
                            String type = getMimeType(myFileName);
                            fp.setContentType(type);
                            parts[i++] = fp;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                int time = 0;
                String responseBody = "";
                do {
                    try {
                        httpClient = getHttpClient();
                        httpPost = getHttpPost(url, "", RequestUtl.getUserAgent());

                        // httpPost.setRequestHeader("Content-type",
                        // "multipart/form-data");

                        httpPost.setRequestEntity(new MultipartRequestEntity(parts,
                                httpPost.getParams()));
                        int statusCode = httpClient.executeMethod(httpPost);

                        Header header = httpPost.getResponseHeader("Accept-Encoding");
                        if (header != null) {
                            LogUtilBase.LogD(null,
                                    "apiClient post header===>" + header.getValue());
                        }

                        if (statusCode == 500 || statusCode == 200) {
                            //php 服务器，有时响应500，也有正确数据。
                            responseBody = httpPost.getResponseBodyAsString();
                            responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
                        }

                        if (callBackRequest != null) {
                            callBackRequest.RequestComplete(request, responseBody);
                        }
                        break;
                    } catch (HttpException e) {
                        // 发生致命的异常，可能是协议不对或者返回的内容有问题

                        String logString = Log.getStackTraceString(e);
                        LogUtilBase.LogD(null, logString);

                        time++;
                        if (time < RETRY_TIMES) {
                            try {
                                Thread.sleep(RETRY_SLEEP_TIME);
                            } catch (InterruptedException e1) {
                            }
                            continue;
                        } else {
                            if (callBackRequest != null) {
                                callBackRequest.RequestError(request, EXCEPTION_HTTP,
                                        logString);
                            }
                        }
                    } catch (IOException e) {
                        // 发生网络异常
                        String logString = Log.getStackTraceString(e);
                        LogUtilBase.LogD(null, logString);

                        time++;
                        if (time < RETRY_TIMES) {
                            try {
                                Thread.sleep(RETRY_SLEEP_TIME);
                            } catch (InterruptedException e1) {
                            }
                            continue;
                        } else {
                            if (callBackRequest != null) {
                                callBackRequest.RequestError(request, EXCEPTION_IO,
                                        logString);
                            }
                        }
                    } catch (Exception e) {
                        // 发生异常
                        String logString = Log.getStackTraceString(e);
                        LogUtilBase.LogD(null, logString);

                        time++;
                        if (time < RETRY_TIMES) {
                            try {
                                Thread.sleep(RETRY_SLEEP_TIME);
                            } catch (InterruptedException e1) {
                            }
                            continue;
                        } else {
                            if (callBackRequest != null) {
                                callBackRequest.RequestError(request, EXCEPTION_OTHER,
                                        logString);
                            }
                        }
                    } finally {
                        // 释放连接
                        httpPost.releaseConnection();
                        httpClient = null;
                    }
                } while (time < RETRY_TIMES);
            }
        }).start();
    }

    /**
     * 可以直接返回主线程
     *
     * @param url
     * @param params
     * @param files
     * @param callBackRequest
     * @param request
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void post(final String url, final Map<String, Object> params,
                            final Map<String, File> files, final AsyncRequest callBackRequest,
                            final String request) {

        // post表单参数处理
        int length = (params == null ? 0 : params.size())
                + (files == null ? 0 : files.size());
        final Part[] parts = new Part[length];
        int i = 0;
        if (params != null) {
            for (String name : params.keySet()) {
                parts[i++] = new StringPart(name, String.valueOf(params
                        .get(name)), RequestUtl.CHARSETNAME);
            }
        }
        if (files != null)
            for (String file : files.keySet()) {
                try {
                    File myFile = files.get(file);
                    String myFileName = myFile.getName();
                    FilePart fp = new FilePart(file, myFile);
                    String type = getMimeType(myFileName);
                    fp.setContentType(type);
                    parts[i++] = fp;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                HttpClient httpClient = null;
                PostMethod httpPost = null;
                int time = 0;
                String responseBody = "";
                do {
                    try {
                        httpClient = getHttpClient();
                        httpPost = getHttpPost(url, "", RequestUtl.getUserAgent());
                        httpPost.setRequestEntity(new MultipartRequestEntity(parts,
                                httpPost.getParams()));
                        int statusCode = httpClient.executeMethod(httpPost);
                        Header header = httpPost.getResponseHeader("Accept-Encoding");

                        if (header != null) {
                            LogUtilBase.LogD(null,
                                    "apiClient post header===>" + header.getValue());
                        }
                        if (statusCode == 500 || statusCode == 200) {
                            //php 服务器，有时响应500，也有正确数据。
                            responseBody = httpPost.getResponseBodyAsString();
                            responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
                        }
                        break;
                    } catch (Exception e) {
                        // 发生网络异常
                        String logString = Log.getStackTraceString(e);
                        LogUtilBase.LogD(null, logString);
                        time++;
                        if (time < RETRY_TIMES) {
                            try {
                                Thread.sleep(RETRY_SLEEP_TIME);
                            } catch (InterruptedException e1) {
                            }
                            continue;
                        } else {
                            if (e != null && e.getMessage().contains("No address associated with hostname"))
                                responseBody = "404";
                            break;
                        }
                    } finally {
                        // 释放连接
                        httpPost.releaseConnection();
                        httpClient = null;
                    }
                } while (time < RETRY_TIMES);
                return responseBody;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (o.equals("404")) {
                    UIHelper.showToast(AppManager.getAppManager().currentActivity(), R.string.lib_check_network);
                    return;
                }

                try {
                    JSONObject json = new JSONObject((String) o);
                    int status = json.getJSONObject("Result").optInt("Status");
                    if (status == 200) {
                        if (callBackRequest != null) {
                            callBackRequest.RequestComplete(request, json.getJSONObject("Result"));
                            return;
                        }
                    } else {
                        callBackRequest.RequestError(null, 0, null);
                    }
                    //要加判断提示，否则网络一请求，就提示toast了
                    String message = json.getJSONObject("Result").optString("Message");
                    if (!StringUtilBase.stringIsEmpty(message)) {
                        UIHelper.showToast(AppManager.getAppManager().currentActivity(), message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }


//    /**
//     * 发送文件和基础数据
//     *
//     * @return
//     */
//    @SuppressWarnings("rawtypes")
//    public static boolean sendForm(String url, String data, String path) {
//
//        boolean res = false;
//        try {
//            HttpPost httpPost = new HttpPost(path);
//            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName(HTTP.UTF_8));
//            reqEntity.addPart("ht", new StringBody(data, Charset.forName("UTF-8")));
//            reqEntity.addPart("Sound", new FileBody(new File(path)));
//            httpPost.setEntity(reqEntity);
//            httpPost.setHeader("Content-type", "text/html;charset=UTF-8");
//            HttpResponse response = new DefaultHttpClient().execute(httpPost);
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                res = true;
//                String resultString = EntityUtils.toString(response.getEntity(),
//                        HTTP.UTF_8);
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return res;
//    }


    /*
     * 外部BitmapManager会通过线程池方式调用获取图片，为了保持同步进行，加上同步锁
     */
    public synchronized static Bitmap getNetBitmap(String url) {
        HttpClient httpClient = null;
        GetMethod httpGet = null;
        Bitmap bitmap = null;
        int time = 0;
        do {
            try {
                httpClient = getHttpClient();
                httpGet = getHttpGet(url, null, null);
                int statusCode = httpClient.executeMethod(httpGet);
                if (statusCode == 200 || statusCode == 500) {
                    //php 服务器，有时响应500，也有正确数据。
                    InputStream inStream = httpGet.getResponseBodyAsStream();
                    bitmap = ImageUtilBase.getBitmap(inStream);

                    inStream.close();
                    break;
                } else {
                    LogUtilBase.LogD(null, "statusCode====>>error====>>" + statusCode);

                    time++;
                    if (time < RETRY_TIMES) {
                        try {
                            Thread.sleep(RETRY_SLEEP_TIME);
                        } catch (InterruptedException e1) {
                        }
                        continue;
                    }
                }
            } catch (HttpException e) {
                // 发生致命的异常，可能是协议不对或者返回的内容有问题

                String logString = Log.getStackTraceString(e);
                LogUtilBase.LogD(null, logString);

                time++;
                if (time < RETRY_TIMES) {
                    try {
                        Thread.sleep(RETRY_SLEEP_TIME);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
            } catch (IOException e) {
                // 发生网络异常
                String logString = Log.getStackTraceString(e);
                LogUtilBase.LogD(null, logString);

                time++;
                if (time < RETRY_TIMES) {
                    try {
                        Thread.sleep(RETRY_SLEEP_TIME);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
            } catch (Exception e) {
                // 发生异常
                String logString = Log.getStackTraceString(e);
                LogUtilBase.LogD(null, logString);

                time++;
                if (time < RETRY_TIMES) {
                    try {
                        Thread.sleep(RETRY_SLEEP_TIME);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
            } finally {
                // 释放连接
                httpGet.releaseConnection();
                httpClient = null;
            }
        } while (time < RETRY_TIMES);
        return bitmap;
    }

}