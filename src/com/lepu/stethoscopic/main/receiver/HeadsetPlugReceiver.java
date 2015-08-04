package com.lepu.stethoscopic.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 监听耳机的插拔
 */
public class HeadsetPlugReceiver extends BroadcastReceiver {

    private static final String TAG = "HeadsetPlugReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.hasExtra("state")) {
//            if (intent.getIntExtra("state", 0) == 0) {
//                Toast.makeText(context, "耳机没有链接", Toast.LENGTH_LONG).show();
//            } else if (intent.getIntExtra("state", 0) == 1) {
//                Toast.makeText(context, "耳机已经链接", Toast.LENGTH_LONG).show();
//            }
//        }

    }

}