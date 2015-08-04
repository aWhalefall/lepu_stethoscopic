package com.lepu.stethoscopic.fun.functiion.recorderData;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lepu.stethoscopic.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordActivity extends Activity implements OnClickListener {

    private static final String TAG = "RecordActivity";
    Button startButton, stopButton;
    MediaRecorder mMediaRecorder;
    SurfaceView energy;

    ClsOscilloscope clsOscilloscope = new ClsOscilloscope();

    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    static final int frequency = 44100;
    // 设置双声道
    static final int channelConfiguration = AudioFormat.CHANNEL_IN_STEREO;
    // 音频数据格式：每个样本16位
    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    int recBufSize;//录音最小buffer大小  
    AudioRecord audioRecord;
    Paint mPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        stopButton = (Button) this.findViewById(R.id.stop);
        startButton = (Button) this.findViewById(R.id.start);
        energy = (SurfaceView) findViewById(R.id.music_record);
        stopButton.setOnClickListener(this);
        startButton.setOnClickListener(this);

        //录音组件
        recBufSize = AudioRecord.getMinBufferSize(frequency,
                channelConfiguration, audioEncoding);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
                channelConfiguration, audioEncoding, recBufSize);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);// 画笔为白色
        mPaint.setStrokeWidth(2);// 设置画笔粗细 
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:

                startButton.setText("正在录音...");
                startButton.setEnabled(false);
                startRecording();

                break;

            case R.id.stop:

                startButton.setText("开始录音");
                startButton.setEnabled(true);
                stopRecording();

                break;

            default:
                break;
        }
    }

    private void startRecording() {
        if (!SDCardUtil.sdCardExists()) {
            startButton.setText("开始录音");
            startButton.setEnabled(true);
            return;
        }

        String fileName = new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date(System.currentTimeMillis())) + ".wav";
        clsOscilloscope.baseLine = energy.getHeight() / 2;
        //clsOscilloscope.Start(audioRecord, recBufSize, energy, mPaint, fileName);
    }

    private void stopRecording() {
        clsOscilloscope.Stop();
    }

    @Override
    protected void onDestroy() {
        clsOscilloscope.Stop();
        //android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}
