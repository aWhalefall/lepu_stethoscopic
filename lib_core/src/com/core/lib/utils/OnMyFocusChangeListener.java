package com.core.lib.utils;

import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;

public class OnMyFocusChangeListener implements OnFocusChangeListener {

	private Context mContext;
	
	public OnMyFocusChangeListener(Context context)
	{
		mContext = context;
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			((InputMethodManager) mContext
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.showSoftInputFromInputMethod(v.getWindowToken(),
							InputMethodManager.SHOW_FORCED);
		} else {
			((InputMethodManager) mContext
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(v.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
