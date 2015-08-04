//package com.lepu.stethoscopic.fun.functiion.recorder;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.os.Handler;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.SurfaceHolder;
//import android.view.SurfaceHolder.Callback;
//import android.view.SurfaceView;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class MusicEnergy extends SurfaceView implements Callback {
//
//    private final Handler mHandler = new Handler();
//    AudioCapture mAudioCapture = null;
//    private int[] mVizData = new int[1024];
//    private List<Integer> list = new ArrayList<Integer>();
//    private final Paint mPaint = new Paint();
//
//    private static final int frequency = 1000 / 10;
//
//    int mWidth = 0;
//    int mCenterY = 0;
//
//    private final Runnable mDrawCube = new Runnable() {
//        public void run() {
//            // drawFrame();
//        }
//    };
//
//    void drawFrame() {
//        final SurfaceHolder holder = getHolder();
//        final Rect frame = holder.getSurfaceFrame();
//        final int width = frame.width();
//        final int height = frame.height();
//
//        Canvas c = null;
//        try {
//            c = holder.lockCanvas();
//            if (c != null) {
//                // draw something
//                drawCube(c);
//            }
//        } finally {
//            if (c != null) holder.unlockCanvasAndPost(c);
//        }
//
//        mHandler.removeCallbacks(mDrawCube);
//        mHandler.postDelayed(mDrawCube, frequency);
//    }
//
//    public MusicEnergy(Context context) {
//        super(context);
//    }
//
//    public MusicEnergy(Context context, AttributeSet attributeSet) {
//        super(context, attributeSet);
//    }
//
//    public void start() {
//        mAudioCapture = new AudioCapture(AudioCapture.TYPE_PCM, 1024);
//        mAudioCapture.start();
//        mPaint.setColor(0xffffffff);
//        mPaint.setAntiAlias(true);
//        mPaint.setStrokeWidth(2);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStyle(Paint.Style.STROKE);
//        getHolder().addCallback(this);
//    }
//
//    void drawCube(Canvas c) {
//        c.save();
//        c.drawColor(0xff000000);
//
//        if (mAudioCapture != null) {
//            mVizData = mAudioCapture.getFormattedData(mCenterY, 100);
//        } else {
//            Arrays.fill(mVizData, (int) 0);
//        }
//        for (int i = 0; i < mVizData.length; i += 32) {
//            list.add(mVizData[i]);
//        }
//        while (list.size() > mWidth) {
//            list.remove(0);
//        }
//
//        int py = mCenterY;
//        if (list.size() > 0) {
//            py += list.get(0);
//        }
//        int len = list.size();
//        for (int i = 0; i < mWidth; i++) {
//            if (len <= mWidth - i) {
//                c.drawPoint(i, py, mPaint);
//            } else {
//                c.drawLine(i - 1, py, i, mCenterY + list.get(i - mWidth + len), mPaint);
//                py = mCenterY + list.get(i - mWidth + len);
//            }
//        }
//        c.restore();
//    }
//
//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//        if (mAudioCapture != null) {
//            mAudioCapture.stop();
//            mAudioCapture.release();
//            mAudioCapture = null;
//        }
//    }
//
//    public void onClose() {
//        if (mAudioCapture != null) {
//            mAudioCapture.stop();
//            mAudioCapture.release();
//            mAudioCapture = null;
//        }
//    }
//
//    public void surfaceChanged(SurfaceHolder holder, int format, int width,
//                               int height) {
//        // TODO Auto-generated method stub
//        mCenterY = height / 2;
//        mWidth = width;
//        Log.i("test", "mCenterY : " + mCenterY);
//        Log.i("test", "mWidth : " + mWidth);
//    }
//
//    public void surfaceCreated(SurfaceHolder holder) {
//        drawFrame();
//    }
//
//    public void surfaceDestroyed(SurfaceHolder holder) {
//    }
//
//}
