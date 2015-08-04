package com.core.lib.utils;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.core.lib.utils.main.LogUtilBase;

final public class BrowserWebChromeClient extends WebChromeClient {

	private static final String TAG = "WebView";
	private BrowserWebChromeDelegate mDelegate;

	public interface BrowserWebChromeDelegate {
		void onProgressChanged(WebView view, int newProgress);

		void onReceivedTitle(WebView view, String title);
	}

	public BrowserWebChromeClient(BrowserWebChromeDelegate delegate) {
		mDelegate = delegate;
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		super.onProgressChanged(view, newProgress);
		LogUtilBase.LogD(TAG, newProgress + "");

		if (mDelegate != null) {
			mDelegate.onProgressChanged(view, newProgress);
		}
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		super.onReceivedTitle(view, title);
		LogUtilBase.LogD(TAG, "WebView title=" + title);

		if (mDelegate != null) {
			mDelegate.onReceivedTitle(view, title);
		}
	}
}
