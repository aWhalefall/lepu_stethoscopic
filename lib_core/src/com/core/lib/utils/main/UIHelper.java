package com.core.lib.utils.main;

import com.lib.custom.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import android.widget.TextView;
import android.widget.Toast;

public class UIHelper {

	public static void showToast(Context context, int textId) {
		if(context != null)
		{
			Toast toast = new Toast(context);
			toast.setDuration(Toast.LENGTH_SHORT);
			
			TextView textView = new TextView(context);  
		    textView.setText(textId);  
		    textView.setTextColor(Color.WHITE);
		    textView.setPadding(15, 10, 15, 10);
		    textView.setBackgroundResource(R.drawable.shape_toast_bg);
		    toast.setView(textView);
			toast.show();
		}
	}

	public static void showToast(Context context, String text) {
		if(context != null)
		{
			Toast toast = new Toast(context);
			toast.setDuration(Toast.LENGTH_SHORT);
			
			TextView textView = new TextView(context);  
		    textView.setText(text);  
		    textView.setTextColor(Color.WHITE);
		    textView.setPadding(15, 10, 15, 10);
		    textView.setBackgroundResource(R.drawable.shape_toast_bg);
		    toast.setView(textView);
			toast.show();
		}
	}
	
	public static void showToast(Context context, int textId, int toastDuration) {
		if(context != null)
		{
			Toast toast = new Toast(context);
			toast.setDuration(toastDuration);
			
			TextView textView = new TextView(context);  
		    textView.setText(textId);  
		    textView.setTextColor(Color.WHITE);
		    textView.setPadding(15, 10, 15, 10);
		    textView.setBackgroundResource(R.drawable.shape_toast_bg);
		    toast.setView(textView);
			toast.show();
		}
	}
	
	public static void showToast(Context context, String text, int toastDuration) {
		if(context != null)
		{
			Toast toast = new Toast(context);
			toast.setDuration(toastDuration);
			
			TextView textView = new TextView(context);  
		    textView.setText(text);  
		    textView.setTextColor(Color.WHITE);
		    textView.setPadding(15, 10, 15, 10);
		    textView.setBackgroundResource(R.drawable.shape_toast_bg);
		    toast.setView(textView);
			toast.show();
		}
	}

	public static void postInUIThread(Runnable runnable, long delayMillis) {
		Handler handler = new Handler(Looper.getMainLooper());
		handler.postDelayed(runnable, delayMillis);
	}

}
