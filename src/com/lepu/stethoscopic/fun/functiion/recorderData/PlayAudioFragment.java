package com.lepu.stethoscopic.fun.functiion.recorderData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.core.lib.application.BaseFragment;
import com.core.lib.utils.main.AppManager;
import com.core.lib.utils.main.UIHelper;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.business.StateChange;
import com.lepu.stethoscopic.main.activity.MainActivity;
import com.lepu.stethoscopic.utils.SdLocal;
import com.lepu.stethoscopic.widget.CommonTopBar;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guangdye on 2015/4/20.
 */
public class PlayAudioFragment extends BaseFragment implements CommonTopBar.OnCommonTopbarLeftLayoutListener, View.OnClickListener {
    private MediaPlayer mPlayer;
    private String fileName;
    private String filePath;
    private boolean isPlaying;
    private ImageView ic_middle;
    private Boolean isFromNet;
    private TextView startTime, endTime;
    private SeekBar seekbarModel;//播放进度条
    private TextView textView;
    private ImageView icRecle;//循环按钮
    private boolean btnLoop = false;//是否循环
    Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fileName = getArguments().getString("filename");
            filePath = getArguments().getString("filepath");
            isFromNet = getArguments().getBoolean("isFromNet", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, null, false);
        CommonTopBar commonTopBar = (CommonTopBar) view.findViewById(R.id.commonbatr);
        commonTopBar.setTopbarTitle(R.string.app_data);
        commonTopBar.setonCommonTopbarLeftLayoutListener(this);
        textView = (TextView) view.findViewById(R.id.textview_title);
        ic_middle = (ImageView) view.findViewById(R.id.ic_middle);
        startTime = (TextView) view.findViewById(R.id.textview_start_time);
        seekbarModel = (SeekBar) view.findViewById(R.id.seekbar_model);
        endTime = (TextView) view.findViewById(R.id.textview_end_time);
        icRecle = (ImageView) view.findViewById(R.id.ic_recle);
        icRecle.setOnClickListener(this);
        ic_middle.setOnClickListener(icMiddleClicK);
        textView.setText(fileName);
        commonTopBar.setRightHidden();
        initTime();
        seekbarModel.setOnSeekBarChangeListener(new ProcessBarListener());
        return view;
    }


    @Override
    public void onTopbarLeftLayoutSelected() {
        finishFragment();
    }

    //点击播放
    private View.OnClickListener icMiddleClicK = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isPlaying) {
                if (isFromNet) {
                    download();
                } else {
                    ic_middle.setImageResource(R.drawable.pause_icon);
                    play(filePath);
                    isPlaying = true;
                }
            } else {
                stop();
                isPlaying = false;
            }
        }
    };

    ProgressDialog dialog;

    public void download() {
        File file = new File(SdLocal.getYuYinFolder(getActivity()) + "/" + fileName);
        if (!file.exists()) {
            dialog = new ProgressDialog(AppManager.getAppManager().currentActivity());
            dialog.setMessage("正在缓冲中...");
            dialog.show();
        }

        String path = filePath;
        HttpUtils http = new HttpUtils();
        http.download(path, SdLocal.getYuYinFolder(getActivity()) + "/" + fileName, true, true, new RequestCallBack<File>() {
            @Override
            public void onStart() {
                //  textView.setText("正在连接...");
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                //textView.setText(current + "/" + total);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //textView.setText(msg);
                if (msg != null && msg.equals("maybe the file has downloaded completely")) {
                    ic_middle.setImageResource(R.drawable.pause_icon);
                    play(SdLocal.getYuYinFolder(getActivity()) + "/" + fileName);
                    isPlaying = true;
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                } else {
                    UIHelper.showToast(getActivity(), "连接异常，请稍后重试");
                }
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                // TODO Auto-generated method stub
                downloadPath = responseInfo.result.getPath();
                ic_middle.setImageResource(R.drawable.pause_icon);
                play(responseInfo.result.getPath());
                isPlaying = true;
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

    }

    String downloadPath;

    /**
     * 停止播放
     */
    private void stop() {
        if (mPlayer != null) {
            mPlayer.release();
            ic_middle.setImageResource(R.drawable.play);
            mPlayer = null;
            isPlaying = false;
        }
    }

    /**
     * 播放
     *
     * @param path
     */
    private void play(String path) {
        try {
            if (mPlayer == null)
                mPlayer = new MediaPlayer();
            mPlayer.reset();
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();
            StrartbarUpdate();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    ic_middle.setImageResource(R.drawable.play);
                    startTime.setText("00:00");
                    seekbarModel.setProgress(0);
                    stop();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示showTime
     *
     * @param mPlayer
     */
    private void setShowTime(MediaPlayer mPlayer) {
        startTime.setText("00:00");
        endTime.setText(formatTime(mPlayer.getDuration()));
    }

    public StateChange stateChange = new StateChange() {
        @Override
        public void onChange() {
            if (mPlayer == null || !mPlayer.isPlaying())
                return;
            if (mPlayer != null) {
                mPlayer.release();
                ic_middle.setImageResource(R.drawable.play);
                mPlayer = null;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity mainActivity = (MainActivity) activity;
        mainActivity.setOnStateChange(stateChange);
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

    public String formatTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null)
            mPlayer = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ic_recle: //点击循环按钮
                LoopMusic();
                break;
        }
    }

    public void LoopMusic() {
        //互斥，循环-不循环
        if (mPlayer != null) {
            mPlayer.setLooping(mPlayer.isLooping());
        }
    }
    //播放进度条

    class ProcessBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (fromUser == true && mPlayer != null) {
                mPlayer.seekTo(progress);
                if (startTime != null)
                    startTime.setText(formatTime(progress));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    /**
     * 开始进行自动更新
     */

    public void StrartbarUpdate() {
        handler.post(r);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            if (mPlayer == null)
                return;
            int CurrentPosition = mPlayer.getCurrentPosition();
            startTime.setText(formatTime(CurrentPosition));
            int mMax = mPlayer.getDuration();
            seekbarModel.setMax(mMax);
            seekbarModel.setProgress(CurrentPosition);
            handler.postDelayed(r, 50);
        }
    };

    /**
     * 初始化显示 音频的 开始时间和结束时间
     */
    private void initTime() {
        if (filePath != null && !filePath.isEmpty()) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(filePath);
                mPlayer.prepare();
                setShowTime(mPlayer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

