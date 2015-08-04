package com.core.lib.widget;

import com.lib.custom.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class BufferProgressDialog {
	private Dialog progress;

	public BufferProgressDialog(final Context context) {

		final Dialog dialog = new Dialog(context,R.style.Theme_Lib_BufferDialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.buffer_dialog, null);

		dialog.setContentView(v);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		progress = dialog;
	}

	public void destroyProgressDialog() {
		//progress.cancel();
		progress.dismiss();

		// 取消网络请求
		// WebApiBase.clearQueue();
	}

	public Dialog get_progress() {
		return progress;
	}
}
