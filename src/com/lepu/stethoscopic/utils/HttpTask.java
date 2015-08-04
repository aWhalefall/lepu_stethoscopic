package com.lepu.stethoscopic.utils;

import android.R.integer;
import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.core.lib.core.AsyncRequest;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

//
//        Params 启动任务执行的输入参数，比如HTTP请求的URL。
//        Progress 后台任务执行的百分比。
//        Result 后台执行任务最终返回的结果，比如String。

@SuppressLint("ParserError")
public class HttpTask extends AsyncTask<String, integer, Void> {


    private AsyncRequest request;
    public AbsAction actions;
    public AbsModule module;
    protected AbsHttpsRequest requestInstent = new AbsHttpsRequest() {
        @Override
        protected String parseContent(InputStream data, boolean fromCache) throws Exception {
            String strData = getContent(data);
            JSONObject jsonObject = new JSONObject(strData);
            request.RequestComplete(jsonObject.getJSONObject("Result"), "");
            return strData;
        }
    };
    protected int connectTimeout = 15000, soTimeOut = 6000;
    public boolean mUseCache = false;
    protected int mCacheEffective = 360;

    /**
     * 查看既往数据 有对话框线程
     */

    @Override
    protected Void doInBackground(String... str) {

        try {
            //todo
            requestInstent.setmConnectionTimeOut(connectTimeout);
            requestInstent.setmSocketTimeOut(soTimeOut);
            HttpPost mRequest = new HttpPost(actions.url);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            //todo
            reqEntity.addPart("Sound", actions.fileBody);

            reqEntity.addPart("ht", new StringBody(actions.jsonString, Charset.forName("UTF-8")));

            reqEntity.getContentEncoding();
            mRequest.setEntity(reqEntity);
            requestInstent.setHttpRequest(mRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            requestInstent.execute(mUseCache, mCacheEffective);
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
            request.RequestError("", 0, "");
        }
        return null;
    }

    public void setActitons(AbsAction actions) {
        this.actions = actions;
    }

    public void setModule(AbsModule module) {
        this.module = module;
    }

    private String getContent(InputStream data) throws Exception {
        String str = null;
        BufferedReader r = null;
        StringBuffer sb = new StringBuffer();

        try {
            r = new BufferedReader(new InputStreamReader(data));

            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            str = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return str;
    }

    public void setAsyCallBack(AsyncRequest request) {
        this.request = request;
    }
}
