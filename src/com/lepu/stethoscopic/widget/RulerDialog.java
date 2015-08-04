package com.lepu.stethoscopic.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.core.lib.utils.DensityUtil;
import com.core.lib.widget.MyDialog;
import com.lepu.stethoscopic.R;

/**
 * Created by guangdye on 2015/4/16.
 */
public class RulerDialog {
    private MyDialog dialog;
    private String value;

    public RulerDialog(final Activity activity) {
        dialog = new MyDialog(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.dialog_ruler, null);
        final TextView txtValue = (TextView) view.findViewById(R.id.txtValue);
        final ObservableHorizontalScrollView scrollView = (ObservableHorizontalScrollView) view.findViewById(R.id.scrollview);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    scrollView.startScrollerTask();
                }
                return false;
            }
        });
        scrollView.setOnScrollStopListner(new ObservableHorizontalScrollView.OnScrollStopListner() {
            public void onScrollChange(int index) {
                value = DensityUtil.px2dip(activity, index) / 7 + 50 + "";
                txtValue.setText(value);
            }
        });
        dialog.addView(view);
        dialog.setTitle("时间");
        dialog.setPositiveButton(R.string.app_ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        dialog.setNegativeButton(R.string.app_cancel, null);
        dialog.create(null);

    }


    public void show() {
        if (dialog != null) {
            dialog.showMyDialog();
        }
    }
}
