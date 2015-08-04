package com.core.lib.application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;

import com.core.lib.utils.main.AppManager;
import com.core.lib.utils.main.UmengHelper;
import com.core.lib.widget.BufferProgressDialog;
import com.core.lib.widget.SwipeBackLayout;
import com.lib.custom.R;

public class BaseFragmentActivity extends FragmentActivity {
	
	//是否加入左滑动，慢慢关闭页面效果
	public static boolean mNeedSwipeBack = false;
	//loading加载框
	private BufferProgressDialog mProfressDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AppManager.getAppManager().addActivity(this);
		if(mNeedSwipeBack)
		{
			SwipeBackLayout layout = (SwipeBackLayout) LayoutInflater.from(this)
					.inflate(R.layout.base, null);
			layout.attachToActivity(this);
		}
		
		// umeng
        UmengHelper.onError(this);
        UmengHelper.updateOnlineConfig(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();

		UmengHelper.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		UmengHelper.onPause(this);
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		
		AppManager.getAppManager().finishActivity(this);
	}
	
	/**
	 * 
	 * @param intent
	 * @param needAnim
	 * true,会有默认的左右动画
	 * false，没有动画
	 */
	public void startActivity(Intent intent, boolean needAnim) {
		startActivity(intent);
		
		if(needAnim)
		{
			overridePendingTransition(R.anim.lib_push_left_right_in, 0);
		}
	}
	
	public void startActivityAnim(Intent intent, int resAnimEnter,
			int resAnimExit) {
		if (intent != null) {
			startActivity(intent);
			overridePendingTransition(resAnimEnter, resAnimExit);
		}
	}
	
	/**
	 * 
	 * @param needAnim
	 * true,关闭时，有动画
	 * false，关闭时，没有动画
	 */
	public void finish(boolean needAnim) {
		finish();
		
		if(needAnim)
		{
			overridePendingTransition(0, R.anim.lib_push_left_right_out);
		}
	}
	
	/**
	 * 0,代表不要动画
	 * @param resAnimEnter
	 * @param resAnimExit
	 */
	public void finishAnim(int resAnimEnter, int resAnimExit) {
		finish();
		overridePendingTransition(resAnimEnter, resAnimExit);
	}

	/*
	 * 必须在主线程中调用
	 */
	public void showProgressDialog() {
		if (mProfressDialog == null) {
			mProfressDialog = new BufferProgressDialog(
					BaseFragmentActivity.this);
		}
	}

	/*
	 * 必须在主线程中调用
	 */
	public void hideProgressDialog() {
		if (mProfressDialog != null) {
			mProfressDialog.destroyProgressDialog();
			mProfressDialog = null;
		}
	}
}
