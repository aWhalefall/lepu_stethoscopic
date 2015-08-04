package com.lepu.stethoscopic.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.core.lib.utils.main.UIHelper;
import com.lepu.stethoscopic.R;

/**
 * 
 * @author wuxd
 * 暂时没有使用这个广播，
 * 大家不要使用!!!!!!!!!!!!
 *
 */
public class AppReceiver extends BroadcastReceiver
{
	public static final String ACTION_CHECK_NETWORK = "check_network";
	
	private static AppReceiver mAppReceiver = null;
	
	private AppReceiver()
	{
		
	}
	
	public static AppReceiver getInstance()
	{
		if(mAppReceiver == null)
		{
			mAppReceiver = new AppReceiver();
		}
		return mAppReceiver;
	}
	
	public static Intent getAppReceiverIntent(String action)
	{
		return new Intent(action);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if(action.equals(ACTION_CHECK_NETWORK))
		{
			UIHelper.showToast(context, R.string.check_network);
		}
	}
}
