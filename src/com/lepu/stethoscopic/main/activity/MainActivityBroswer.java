package com.lepu.stethoscopic.main.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.core.lib.application.BaseActivity;
import com.core.lib.utils.BrowserWebChromeClient;
import com.core.lib.utils.BrowserWebChromeClient.BrowserWebChromeDelegate;
import com.core.lib.utils.BrowserWebClient;
import com.core.lib.utils.BrowserWebClient.BrowserWebClientDelegate;
import com.core.lib.utils.main.LogUtilBase;
import com.core.lib.utils.main.StringUtilBase;
import com.lepu.stethoscopic.R;


public class MainActivityBroswer extends BaseActivity implements
        BrowserWebClientDelegate,
        BrowserWebChromeDelegate {

    private WebView mWebView = null;
    private ProgressBar mProgressBar = null;
//    private CustomTopBarNew mTopbar = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_broswer_activity);

        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
//		mTopbar = (CustomTopBarNew) findViewById(R.id.broswerTopBar);
//		mTopbar.setonTopbarNewLeftLayoutListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.topProgressBar);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new BrowserWebClient(this));
        mWebView.setWebChromeClient(new BrowserWebChromeClient(this));

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);

        String requestUrl = getIntent().getStringExtra("url");
        showWebViewData(requestUrl);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        finish(true);
    }

    public void showWebViewData(String url) {

        if (!StringUtilBase.stringIsEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }

//    @Override
//    public void onTopbarLeftLayoutSelected() {
//        finish(true);
//    }

    public void onProgressChanged(WebView view, int newProgress) {
        LogUtilBase.LogD(null, "onProgressChanged===>>" + newProgress);
        mProgressBar.setMax(100);
        if (newProgress < 100) {
            if (mProgressBar.getVisibility() == View.GONE) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            mProgressBar.setProgress(newProgress);
        } else {
            mProgressBar.setProgress(100);

            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.lib_broswer_progress);
            // 运行动画
            mProgressBar.startAnimation(animation);
            // 将 spinner 的可见性设置为不可见状态
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void onPageStarted(String url) {
        LogUtilBase.LogD(null, "onPageStarted===>>" + url);
    }

    public void onPageFinished(String url) {
        LogUtilBase.LogD(null, "onPageFinished===>>" + url);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        LogUtilBase.LogD(null, "onReceivedTitle===>>" + title);
//        mTopbar.setTopbarTitle(title);
    }

}
