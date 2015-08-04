package com.lepu.stethoscopic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lepu.stethoscopic.R;


public class CommonTopBar extends RelativeLayout {

    private OnCommonTopbarLeftLayoutListener onCommonTopbarLeftLayoutListener = null;
    private OnCommonTopbarRightButtonListener onCommonTopbarRightButtonListener = null;
    private OnCommonTopbarRightTextLayoutListener onCommonTopbarRightTextLayoutListener = null;

    private RelativeLayout mainItemLayout = null;
    private LinearLayout leftLayout = null;
    private TextView textViewRight = null;
    private TextView textview_pictext = null;

    private TextView topTitleTextView = null;

    public CommonTopBar(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_common_topbar, this,
                true);
        init();
    }

    public CommonTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_common_topbar, this,
                true);
        init();
    }

    private void init() {
        mainItemLayout = (RelativeLayout) findViewById(R.id.mainItemLayout);
        leftLayout = (LinearLayout) findViewById(R.id.topbarLeftLinearLayout);
        textViewRight = (TextView) findViewById(R.id.textview_right);
        leftLayout.setOnClickListener(new MyViewOnClickListener());
        textViewRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommonTopbarRightButtonListener != null) {
                    onCommonTopbarRightButtonListener.onTopbarRightButtonSelected();
                }
            }
        });

        topTitleTextView = (TextView) findViewById(R.id.topbarTitle);
        textview_pictext = (TextView) findViewById(R.id.textview_pictext);
        textview_pictext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommonTopbarRightTextLayoutListener != null) {
                    onCommonTopbarRightTextLayoutListener.onTopbarRightTextLayoutSelected();
                }
            }
        });

    }

    public void setTopbarBackground(int drawable) {
        if (mainItemLayout != null) {
            mainItemLayout.setBackgroundResource(drawable);
        }
    }

    public void setTopbarLeftLayout(int backImageResId, int backTitleResId, int backTitleColorResId) {
        if (leftLayout != null) {
            if (backImageResId > 0) {
                ImageView backImage = (ImageView) findViewById(R.id.topbarLeftBackImageView);
                backImage.setImageResource(backImageResId);
            }

            TextView backTitle = (TextView) findViewById(R.id.topbarLeftBackTitle);
            if (backTitleResId > 0) {
                backTitle.setText(backTitleResId);
            }

            if (backTitleColorResId > 0) {
                backTitle.setBackgroundResource(backTitleColorResId);
            }
        }
    }

    public void setTxtPicShow() {
        TextView textView = (TextView) findViewById(R.id.textview_pictext);
        textView.setVisibility(VISIBLE);
    }

    public void setRightButton(int resBackgroundId) {
        if (textViewRight != null) {
            textViewRight.setVisibility(View.VISIBLE);
            textViewRight.setBackgroundResource(resBackgroundId);
        }
    }

    public void setRightHidden() {
        if (textViewRight != null) {
            textViewRight.setVisibility(View.INVISIBLE);
        }
    }

    public void setLeftHidden() {
        if (leftLayout != null) {
            leftLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setRightText(int str) {
        if (textViewRight != null) {
            textViewRight.setVisibility(View.VISIBLE);
            textViewRight.setText(str);
        }
    }


    public void setTopbarTitle(int titleResId) {
        if (topTitleTextView != null) {
            topTitleTextView.setText(titleResId);
        }
    }

    public void setTopbarTitle(String title) {
        if (topTitleTextView != null) {
            topTitleTextView.setText(title);
        }
    }

    public void setOnCommonTopbarRightButtonListener(
            OnCommonTopbarRightButtonListener listener) {
        onCommonTopbarRightButtonListener = listener;
    }

    public void setonCommonTopbarLeftLayoutListener(
            OnCommonTopbarLeftLayoutListener listener) {
        onCommonTopbarLeftLayoutListener = listener;
    }

    public void setOnCommonTopbarRightTextLayoutListener(
            OnCommonTopbarRightTextLayoutListener listener) {
        onCommonTopbarRightTextLayoutListener = listener;
    }

    public interface OnCommonTopbarRightButtonListener {
        public void onTopbarRightButtonSelected();
    }

    public interface OnCommonTopbarLeftLayoutListener {
        public void onTopbarLeftLayoutSelected();
    }

    public interface OnCommonTopbarRightTextLayoutListener {
        public void onTopbarRightTextLayoutSelected();
    }

    class MyViewOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int rid = v.getId();
            if (rid == R.id.topbarLeftLinearLayout) {
                if (onCommonTopbarLeftLayoutListener != null) {
                    onCommonTopbarLeftLayoutListener.onTopbarLeftLayoutSelected();
                }
            }
        }
    }

}