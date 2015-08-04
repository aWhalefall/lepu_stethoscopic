package com.lepu.stethoscopic.fun.functiion.measure;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.core.lib.application.BaseFragment;
import com.core.lib.utils.main.LogUtilBase;
import com.core.lib.utils.main.UIHelper;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.utils.SdLocal;
import com.lepu.stethoscopic.utils.WaveView;
import com.lepu.stethoscopic.widget.CommonTopBar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guangdye on 2015/4/30.
 */
public class RecorderStartFragmentCopy extends BaseFragment {

    private boolean isRecord = true;
    //private String filePath;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private String fileFullName;
    private ImageView imgviewTypeicon;
    private WaveView waveview;
    private String fileName;
//
//    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
//    static final int frequency = 44100;
//    // 设置双声道
//    static final int channelConfiguration = AudioFormat.CHANNEL_IN_STEREO;
//    // 音频数据格式：每个样本16位
//    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
//
//    int recBufSize;//录音最小buffer大小
//    AudioRecord audioRecord;
    // ClsOscilloscope clsOscilloscope = new ClsOscilloscope();
    
   

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_record, null, false);
        CommonTopBar commonTopBar = (CommonTopBar) mainView.findViewById(R.id.commonbatr);
        commonTopBar.setTopbarTitle(R.string.test);

//        //录音组件
//        recBufSize = AudioRecord.getMinBufferSize(frequency,
//                channelConfiguration, audioEncoding);
//        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
//                channelConfiguration, audioEncoding, recBufSize);

        ic_middle = (ImageView) mainView.findViewById(R.id.ic_middle);
        //waveview = (WaveView) mainView.findViewById(R.id.waveview);
        imgviewTypeicon = (ImageView) mainView.findViewById(R.id.imgview_typeicon);
        ic_middle.setImageResource(R.drawable.ic_stop);
        ic_middle.setOnClickListener(listener);
        ic_left = (ImageView) mainView.findViewById(R.id.ic_left);
        ic_left.setImageResource(R.drawable.ic_play_lock);
        ic_left.setOnClickListener(listener);
        ic_right = (ImageView) mainView.findViewById(R.id.ic_right);
        ic_right.setImageResource(R.drawable.ic_upload_lock);
        ic_right.setOnClickListener(listener);
        
       
		
        startRecord();
        return mainView;
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	
    }

    private ImageView ic_middle, ic_left, ic_right;
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_left:
                    finishFragment();
                    break;
                case R.id.ic_middle:
                    try {
                        isRecord = !isRecord;
                        if (isRecord) {
                            startRecord();
                        } else {
                            showState(3);
                            mRecorder.stop();
                            mRecorder.release();
                            mRecorder = null;
                            //clsOscilloscope.Stop();

                        }
                    } catch (Exception e) {
                        LogUtilBase.LogE("error", e.getMessage());
                    }
                    break;
                case R.id.ic_left:
                    try {
//                        play(SdLocal.getYuYinFolder(getActivity()) + "/" + fileName, new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                showState(3);
//                            }
//                        });

                        play(fileFullName);
                        showState(2);
                    } catch (Exception e) {
                        UIHelper.showToast(getActivity(), "播放失败");
                    }
                    break;
                case R.id.ic_right://upload
                    UIHelper.showToast(getActivity(), "upload");
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 显示状态
     */
    private void showState(int type) {
        // waveview.setVisibility(View.GONE);
        ic_left.setFocusable(true);
        ic_right.setFocusable(true);
        switch (type) {
            case 1: //录音状态
                ic_left.setFocusable(false);
                ic_right.setFocusable(false);
                ic_left.setImageResource(R.drawable.ic_play_lock);
                ic_middle.setImageResource(R.drawable.ic_start);
                // waveview.setVisibility(View.VISIBLE);
                ic_right.setImageResource(R.drawable.ic_upload_lock);
                imgviewTypeicon.setImageResource(R.drawable.huatong);
                break;

            case 2: //播放状态
                ic_left.setImageResource(R.drawable.ic_pause);
                ic_middle.setImageResource(R.drawable.ic_start);
                ic_right.setImageResource(R.drawable.ic_upload);
                imgviewTypeicon.setImageResource(R.drawable.pause_write);
                break;

            case 3: //停止状态
                ic_left.setImageResource(R.drawable.ic_play);
                ic_middle.setImageResource(R.drawable.ic_start);
                ic_right.setImageResource(R.drawable.ic_upload);
                imgviewTypeicon.setImageResource(R.drawable.play_write);
                break;
        }
    }

    /**
     * 开始录音
     */
    private void startRecord() {
    	
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        String fileName = new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date(System.currentTimeMillis())) + ".wav";
        fileFullName = SdLocal.getYuYinFolder(getActivity()) + "/" + fileName;
        mRecorder.setOutputFile(fileFullName);
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            LogUtilBase.LogE("error", e.getMessage());
        }
//        fileName = new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date(System.currentTimeMillis())) + ".wav";
//        clsOscilloscope.Start(getActivity(), audioRecord, recBufSize, null, null, fileName);
        showState(1);
    }

    private void play(String path) {
        try {
            mPlayer = new MediaPlayer();
            mPlayer.reset();
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
