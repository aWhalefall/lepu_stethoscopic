package com.lepu.stethoscopic.fun.functiion.measure;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.lib.application.BaseFragment;
import com.core.lib.core.AsyncRequest;
import com.core.lib.utils.main.AppManager;
import com.core.lib.utils.main.LogUtilBase;
import com.core.lib.utils.main.UIHelper;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.application.MyApplication;
import com.lepu.stethoscopic.business.AudioManagerCustom;
import com.lepu.stethoscopic.business.StateChange;
import com.lepu.stethoscopic.business.UndataManager;
import com.lepu.stethoscopic.fun.functiion.recorderData.ClsOscilloscope;
import com.lepu.stethoscopic.main.activity.MainActivity;
import com.lepu.stethoscopic.model.SoundFile;
import com.lepu.stethoscopic.model.User;
import com.lepu.stethoscopic.utils.DateUtil;
import com.lepu.stethoscopic.utils.SdLocal;
import com.lepu.stethoscopic.utils.WaveView;
import com.lepu.stethoscopic.widget.CommonTopBar;

/**
 * Created by guangdye on 2015/4/30.
 */
public class RecorderStartFragment extends BaseFragment implements CommonTopBar.OnCommonTopbarLeftLayoutListener {

    private boolean isRecord = true;
    //private String filePath;
    private MediaPlayer mPlayer;

    private ImageView imgviewTypeicon;
    private WaveView waveview;
    private String fileName;
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    static final int frequency = 44100;//44100
    // 设置双声道
    static final int channelConfiguration = AudioFormat.CHANNEL_IN_STEREO;
    // 音频数据格式：每个样本16位
    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    private int soundType;
    int recBufSize;//录音最小buffer大小
    AudioRecord audioRecord;
    private CountDownTimer countDownTimer;

    ClsOscilloscope clsOscilloscope = new ClsOscilloscope();
    
    private AudioManager mAudioManager = null;
    private int mCurrentStreamVolume = 0;
    private  TextView timeCountDown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            soundType = getArguments().getInt("type");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_record, null, false);
        CommonTopBar commonTopBar = (CommonTopBar) mainView.findViewById(R.id.commonbatr);
        commonTopBar.setTopbarTitle(R.string.test);
        commonTopBar.setonCommonTopbarLeftLayoutListener(this);
        
        mAudioManager = (AudioManager) getActivity()
				.getSystemService(Context.AUDIO_SERVICE);
		mCurrentStreamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		
		int maxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume,
				0);
		
        //录音组件
        recBufSize = AudioRecord.getMinBufferSize(frequency,
                channelConfiguration, audioEncoding);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
                channelConfiguration, audioEncoding, recBufSize);
        ic_middle = (ImageView) mainView.findViewById(R.id.ic_middle);
        waveview = (WaveView) mainView.findViewById(R.id.waveview);
        imgviewTypeicon = (ImageView) mainView.findViewById(R.id.imgview_typeicon);
        ic_middle.setImageResource(R.drawable.ic_stop);
        ic_middle.setOnClickListener(listener);
        ic_left = (ImageView) mainView.findViewById(R.id.ic_left);
        ic_left.setImageResource(R.drawable.ic_play_lock);
        ic_left.setOnClickListener(listener);
        ic_right = (ImageView) mainView.findViewById(R.id.ic_right);
        ic_right.setImageResource(R.drawable.ic_upload_lock);
        ic_right.setOnClickListener(listener);
         timeCountDown= (TextView) mainView.findViewById(R.id.tips_coutdown);
        setupCountDownTimer();
        startRecord();
        return mainView;
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	// 恢复声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				mCurrentStreamVolume, 0);
    }

    private ImageView ic_middle, ic_left, ic_right;
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_left:
                    finishFragment();
                    clsOscilloscope.Stop();
                    break;
                case R.id.ic_middle:
                    try {
                        isRecord = !isRecord;
                        if (isRecord) {
                            startRecord();
                        } else {
                            UIHelper.showToast(getActivity(), R.string.recordend);
                            showState(3);
                            updateDb();  ///写数据库
                        }
                    } catch (Exception e) {
                        LogUtilBase.LogE("error", e.getMessage());
                    }
                    break;
                case R.id.ic_left:
                    if (isPlaying) {
                        showState(3);
                        isPlaying = false;
                    } else {
                        isPlaying = true;
                        try {
                            play(SdLocal.getYuYinFolder(getActivity()) + "/" + fileName);
                            showState(2);
                        } catch (Exception e) {
                            UIHelper.showToast(getActivity(), "播放失败");
                        }
                    }
                    break;
                case R.id.ic_right://upload
                    dialog = new ProgressDialog(AppManager.getAppManager().currentActivity());
                    dialog.setMessage("正在上传音频文件...");
                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            File file = new File(SdLocal.getYuYinFolder(getActivity()) + "/" + fileName);
                            if (file.exists()) {
                                SoundFile soundFile = new SoundFile();
                                soundFile.Time = DateUtil.getData2str(new Date());
                                soundFile.Position = 1;
                                soundFile.SoundType = soundType;
                                soundFile.SoundName = fileName;
                                soundFile.Filepath = SdLocal.getYuYinFolder(getActivity()) + "/" + fileName;
                                UndataManager.getInstance().updataVideoStream(asyncRequest, soundFile);
                            } else {
                                dialog.dismiss();
                                UIHelper.showToast(getActivity(), R.string.filereaderror);
                            }
                        }


                    }, 20);
                    break;
                default:
                    break;
            }
        }
    };

    private ProgressDialog dialog;
    private AsyncRequest asyncRequest = new AsyncRequest() {
        @Override
        public void RequestComplete(Object object, Object data) {
            /*
               1.修改数据库的状态 上传状态
               2.插入raid
               3.重新加载出来
             */
            JSONObject jsonObject = (JSONObject) object;
            String arid = jsonObject.optJSONObject("DetailInfo").optString("ARID");
            String sound = jsonObject.optJSONObject("DetailInfo").optString("Sound");
            String soundName = jsonObject.optJSONObject("DetailInfo").optString("SoundName");
            User user = MyApplication.getInstance().getCurrentUser();
            AudioManagerCustom.getInstance().insertServiceBack(user.getUserId(), arid, sound, soundName);
            if (dialog != null)
                dialog.dismiss();
        }
        @Override
        public void RequestError(Object object, int errorId, String errorMessage) {
            dialog.dismiss();
            AppManager.getAppManager().currentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UIHelper.showToast(AppManager.getAppManager().currentActivity(), R.string.connectfail);
                }
            });

        }
    };

    SoundFile sound = null;

    private void updateDb() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                File file = new File(SdLocal.getYuYinFolder(getActivity()) + "/" + fileName);
//                if (file.exists()) {
        sound = new SoundFile();
        sound.SoundName = fileName;
        sound.SoundType = soundType;
        sound.UploadState = 0;
        sound.Time = DateUtil.getData2str(new Date());
        sound.Filepath = SdLocal.getYuYinFolder(getActivity()) + "/" + fileName;
        if (MyApplication.getInstance().getCurrentUser() != null) {
            sound.UserId = Integer.valueOf(MyApplication.getInstance().getCurrentUser().getUserId());
        }
        AudioManagerCustom.getInstance().replaceAudioFile(sound);
    }
    // }
//        }, 10);
//
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean isPlaying = false;

    /**
     * 显示状态
     */
    private void showState(int type) {
        waveview.setVisibility(View.GONE);
        timeCountDown.setVisibility(View.GONE);
        ic_left.setFocusable(true);
        ic_right.setFocusable(true);
        switch (type) {
            case 1: //录音状态
                timeCountDown.setVisibility(View.VISIBLE);
                ic_left.setFocusable(false);
                ic_right.setFocusable(false);
                ic_left.setImageResource(R.drawable.ic_play_lock);
                ic_middle.setImageResource(R.drawable.ic_stop);
                // waveview.setVisibility(View.VISIBLE);
                ic_right.setImageResource(R.drawable.ic_upload_lock);
                imgviewTypeicon.setImageResource(R.drawable.huatong);
                break;
            case 2: //播放状态
                timeCountDown.setVisibility(View.GONE);
                ic_left.setImageResource(R.drawable.ic_pause);
                ic_middle.setImageResource(R.drawable.ic_start);
                ic_right.setImageResource(R.drawable.ic_upload);
                imgviewTypeicon.setImageResource(R.drawable.pause_write);
                countDownTimer.cancel();
                break;
            case 3: //停止状态
                stopPlayAndRecord();
                timeCountDown.setVisibility(View.GONE);
                ic_left.setImageResource(R.drawable.ic_play);
                ic_middle.setImageResource(R.drawable.ic_start);
                ic_right.setImageResource(R.drawable.ic_upload);
                imgviewTypeicon.setImageResource(R.drawable.play_write);
                break;
        }
    }

    private void stopPlayAndRecord() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        clsOscilloscope.Stop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRecord = false;
    }

    /**
     * 开始录音
     */
    private void startRecord() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (fileName != null) {
            fileName = null;
        }

        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }

        setupCountDownTimer();
        fileName = new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date(System.currentTimeMillis())) + ".wav";
        //todo  AudioFile
        clsOscilloscope.Start(getActivity(), audioRecord, recBufSize, null, null, fileName);
        showState(1);
    }
    //播放音频
    private void play(String path) {
        try {
            mPlayer = new MediaPlayer();
            mPlayer.reset();
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();
            //播放完毕进行状态还原
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    showState(3);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopPlayAndRecord();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private void setupCountDownTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeCountDown.setText(""+DateUtil.getData2strmmss(60*1000-millisUntilFinished));
            }
            public void onFinish() {
                showState(3);
            }
        };
        countDownTimer.start();
    }
    public StateChange stateChange = new StateChange() {
        @Override
        public void onChange() {
            showState(3);
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity mainActivity = (MainActivity) activity;
        mainActivity.setOnStateChange(stateChange);
    }

    @Override
    public void onTopbarLeftLayoutSelected() {
        finishFragment();
    }
}

