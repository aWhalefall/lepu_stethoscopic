package com.lepu.stethoscopic.fun.functiion.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.core.lib.application.BaseFragment;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.application.MyApplication;
import com.lepu.stethoscopic.model.User;
import com.lepu.stethoscopic.widget.CommonTopBar;

/**
 * Created by guangdye on 2015/4/20.
 */
public class PersonInfoFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personinfofragment, null, false);
        CommonTopBar commonTopBar = (CommonTopBar) view.findViewById(R.id.commonbatr);
        commonTopBar.setTopbarTitle(R.string.personinfo);
        commonTopBar.setRightHidden();
        commonTopBar.setonCommonTopbarLeftLayoutListener(new CommonTopBar.OnCommonTopbarLeftLayoutListener() {
            @Override
            public void onTopbarLeftLayoutSelected() {
                finishFragment();
            }
        });
        init(view);
        return view;
    }

    private void init(View view) {
        User user = MyApplication.getInstance().getCurrentUser();
        TextView textView = (TextView) view.findViewById(R.id.txt_phone);
        EditText edt_username = (EditText) view.findViewById(R.id.edt_username);
        EditText edt_sex = (EditText) view.findViewById(R.id.edt_sex);
        EditText edt_age = (EditText) view.findViewById(R.id.edt_age);
        textView.setText("当前账号" + user.getPhone());
        edt_username.setText(user.getTrueName());
        edt_sex.setText(user.getGender() == 1 ? "男" : "女");
    }
}

