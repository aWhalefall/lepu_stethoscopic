package com.lepu.stethoscopic.fun.functiion.measure;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.lib.application.BaseFragment;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.widget.CommonTopBar;

/**
 * Created by weichyang on 2015/4/28.
 */
public class MeasureFragment extends BaseFragment {


    private MediaPlayer mPlayer;
    private TextView textview_top;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_measure, null, false);
        CommonTopBar commonTopBar = (CommonTopBar) mainView.findViewById(R.id.commonbatr);
        commonTopBar.setTopbarTitle(R.string.tab_test);
        commonTopBar.setLeftHidden();

        //录音
        mPlayer = new MediaPlayer();
        ImageView record = (ImageView) mainView.findViewById(R.id.imgview_startrecorder);
        ImageView textviewHeart = (ImageView) mainView.findViewById(R.id.textview_heart);
        textview_top = (TextView) mainView.findViewById(R.id.textview_top);
        textviewHeart.setOnClickListener(replaceClick);
        record.setOnClickListener(replaceClick);
        return mainView;

    }

    private View.OnClickListener replaceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int Type = 1;
            switch (v.getId()) {
                case R.id.textview_heart:
                    Type = 1;  //心音
                    break;
                case R.id.imgview_startrecorder:
                    Type = 2; //肺音
                    break;
            }
            //RecorderStartFragmentAmr recorderStartFragment = new RecorderStartFragmentAmr();
            RecorderStartFragment recorderStartFragment = new RecorderStartFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", Type);
            recorderStartFragment.setArguments(bundle);
            switchFragment(R.id.layout_container, recorderStartFragment);
        }
    };

    public View getView() {
        return getView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
//        activity.registerReceiver(new HeadsetPlugReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if (intent.hasExtra("state")) {
//                    if (intent.getIntExtra("state", 0) == 0) {
//                        textview_top.setVisibility(View.VISIBLE);
//                    } else if (intent.getIntExtra("state", 0) == 1) {
//                        textview_top.setVisibility(View.INVISIBLE);
//                    }
//                }
//            }
//        }, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
