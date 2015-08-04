package com.lepu.stethoscopic.utils;

import android.content.Context;
import android.media.AudioManager;

public class MyAudioManager {

    private static MyAudioManager mMyAudioManager = null;

    private static int mCurr_stream_Volume = 0;

    private static AudioManager mAudioManager = null;

    private MyAudioManager(Context context) {
        mAudioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
    }

    public static MyAudioManager getInstance(Context context) {
        if (mMyAudioManager == null) {
            mMyAudioManager = new MyAudioManager(context);
        }
        
       mCurr_stream_Volume = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        return mMyAudioManager;
    }

    public void setMaxVolume() {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);
    }

    public void setCurrentVolume() {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                mCurr_stream_Volume, 0);
    }

}
