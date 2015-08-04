package com.lepu.stethoscopic.fun.functiion.recorderData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.core.lib.application.BaseFragment;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.widget.CommonTopBar;

/**
 * Created by guangdye on 2015/4/20.
 */
public class DiagnosisFragment extends BaseFragment implements CommonTopBar.OnCommonTopbarLeftLayoutListener {

    private String diagnosis;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            diagnosis = getArguments().getString("filename");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diagnosis, null, false);
        CommonTopBar commonTopBar = (CommonTopBar) view.findViewById(R.id.commonbatr);
        commonTopBar.setTopbarTitle("专家诊断");
        commonTopBar.setonCommonTopbarLeftLayoutListener(this);
        TextView textView = (TextView) view.findViewById(R.id.txt_diagnosis);
        textView.setText(diagnosis);
        commonTopBar.setRightHidden();
        return view;
    }

    @Override
    public void onTopbarLeftLayoutSelected() {
        finishFragment();
    }


}