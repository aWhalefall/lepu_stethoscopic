package com.lepu.stethoscopic.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;


public abstract class AbsHttpsRequest {
    private Object requestData;
    public JSONObject backResult;
    private HttpPost httpRequest;
    private int mConnectionTimeOut = 6000;
    private int mSocketTimeOut = 6000;
    boolean isAutoRetry = false;
    private int mRetryTimes = 3;
    private int responseCode;
    private String message;

    protected void executeRequest(boolean mUseCache, int mCacheEffective)
            throws ConnectTimeoutException, Exception {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, mConnectionTimeOut);
        HttpConnectionParams.setSoTimeout(httpParams, mSocketTimeOut);
        DefaultHttpClient client = new DefaultHttpClient(httpParams);
        HttpClientParams.setCookiePolicy(client.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
        InputStream in = null;
        String cache = null;

        try {
            if (isAutoRetry) {
                client.setHttpRequestRetryHandler(requestRetryHandler);
            }

            if (in == null) {
                HttpResponse httpResponse = client.execute(httpRequest);
                responseCode = httpResponse.getStatusLine().getStatusCode();
                if (responseCode != 200
                        || httpResponse.getEntity().getContentLength() == 0) {
                    throw new Exception(responseCode + " -- "
                            + httpResponse.getStatusLine().getReasonPhrase()
                            + " httpResponse.getEntity().getContentLength():"
                            + httpResponse.getEntity().getContentLength());
                }
                message = httpResponse.getStatusLine().getReasonPhrase();
                HttpEntity entity = httpResponse.getEntity();

                if (entity != null) {
                    in = entity.getContent();
                    //Todo 转换文字
                    parseContent(in, false);
                    // XkCache.addBizCache(msKey, cache.getBytes());
                }
            } else {
                parseContent(in, false);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            if (e.getClass().equals(ConnectTimeoutException.class)
                    || e.getClass().equals(SocketTimeoutException.class)) {
                throw new ConnectTimeoutException();
            } else {
                throw e;
            }
        } catch (JSONException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {

            }
            client.getConnectionManager().shutdown();
        }

    }

    private HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
        @Override
        public boolean retryRequest(IOException exception, int executionCount,
                                    HttpContext context) {
            if (executionCount >= mRetryTimes) {
                return false;
            }
            HttpRequest request = (HttpRequest) context
                    .getAttribute(ExecutionContext.HTTP_REQUEST);
            boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
            if (!idempotent) {
                return true;
            }
            return false;
        }
    };

    protected abstract String parseContent(InputStream data, boolean fromCache)
            throws Exception;

    public void execute(final boolean mUseCache, final int mCacheEffective)
            throws ConnectTimeoutException, Exception {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    executeRequest(mUseCache, mCacheEffective);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public int getmConnectionTimeOut() {
        return mConnectionTimeOut;
    }

    public void setmConnectionTimeOut(int mConnectionTimeOut) {
        this.mConnectionTimeOut = mConnectionTimeOut;
    }

    public int getmSocketTimeOut() {
        return mSocketTimeOut;
    }

    public void setmSocketTimeOut(int mSocketTimeOut) {
        this.mSocketTimeOut = mSocketTimeOut;
    }

    public boolean isAutoRetry() {
        return isAutoRetry;
    }

    public void setAutoRetry(boolean isAutoRetry) {
        this.isAutoRetry = isAutoRetry;
    }

    public int getmRetryTimes() {
        return mRetryTimes;
    }

    public void setmRetryTimes(int mRetryTimes) {
        this.mRetryTimes = mRetryTimes;
    }

    public Object getRequestContent() {
        return requestData;
    }

    public void setRequestContent(Object requestData) {
        this.requestData = requestData;
    }

    public HttpPost getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpPost httpRequest) {
        this.httpRequest = httpRequest;
    }

}
