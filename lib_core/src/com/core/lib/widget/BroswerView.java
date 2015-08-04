package com.core.lib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.core.lib.utils.BrowserWebChromeClient;
import com.core.lib.utils.BrowserWebChromeClient.BrowserWebChromeDelegate;
import com.core.lib.utils.BrowserWebClient;
import com.core.lib.utils.BrowserWebClient.BrowserWebClientDelegate;
import com.core.lib.utils.main.StringUtilBase;
import com.lib.custom.R;

public class BroswerView extends LinearLayout implements
		BrowserWebClientDelegate, BrowserWebChromeDelegate {

	private Context mContextInstance = null;
	private WebView mWebView = null;
	private ProgressBar mProgressBar = null;

	public BroswerView(Context context) {
		super(context);

		mContextInstance = context;
		LayoutInflater.from(context).inflate(R.layout.broswer_view, this, true);
		init();
	}

	public BroswerView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContextInstance = context;
		LayoutInflater.from(context).inflate(R.layout.broswer_view, this, true);
		init();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		
		mWebView.destroy();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init() {

		mProgressBar = (ProgressBar) findViewById(R.id.topProgressBar);

		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.setWebViewClient(new BrowserWebClient(this));
		mWebView.setWebChromeClient(new BrowserWebChromeClient(this));

		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		webSettings.setBuiltInZoomControls(true);
	}

	public void showWebViewData(String url) {

		if (!StringUtilBase.stringIsEmpty(url)) {
			mWebView.loadUrl(url);
		}
	}

	public void onProgressChanged(WebView view, int newProgress) {
		mProgressBar.setMax(100);
		if (newProgress < 100) {
			if (mProgressBar.getVisibility() == View.GONE) {
				mProgressBar.setVisibility(View.VISIBLE);
			}
			mProgressBar.setProgress(newProgress);
		} else {
			mProgressBar.setProgress(100);

			Animation animation = AnimationUtils.loadAnimation(
					mContextInstance, R.anim.lib_broswer_progress);
			// 运行动画
			mProgressBar.startAnimation(animation);
			// 将 spinner 的可见性设置为不可见状态
			mProgressBar.setVisibility(View.GONE);
		}
	}

	public void onPageStarted(String url) {

	}

	public void onPageFinished(String url) {

	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		
	}
}
