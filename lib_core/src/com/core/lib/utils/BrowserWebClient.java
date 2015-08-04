package com.core.lib.utils;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.core.lib.utils.main.LogUtilBase;

final public class BrowserWebClient extends WebViewClient {
	
	private static final String TAG = "WebView";
	private BrowserWebClientDelegate mDelegate;
	
	public interface BrowserWebClientDelegate{
		void onPageStarted(String url);
		void onPageFinished(String url);
	}
	
	public BrowserWebClient(BrowserWebClientDelegate browserWebClientDelegate) {
		mDelegate = browserWebClientDelegate;
	}
	
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        
        LogUtilBase.LogD(TAG, "WebView onPageStarted");
        mDelegate.onPageStarted(url);
    }    
    
	public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        
        LogUtilBase.LogD(TAG, "WebView onPageFinished");
        mDelegate.onPageFinished(url);
    }
	
}
