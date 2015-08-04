package com.lepu.stethoscopic.fun.functiion.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.lib.application.BaseFragment;
import com.core.lib.widget.MyDialog;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.application.MyApplication;
import com.lepu.stethoscopic.config.UserConfig;
import com.lepu.stethoscopic.fun.functiion.login.LoginActivity;
import com.lepu.stethoscopic.widget.CommonTopBar;


/**
 * Created by weichyang on 2015/4/28.
 */
public class MineFragment extends BaseFragment implements CommonTopBar.OnCommonTopbarLeftLayoutListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null, false);
        CommonTopBar commonTopBar = (CommonTopBar) view.findViewById(R.id.commonbatr);
        commonTopBar.setTopbarTitle(R.string.mine);
        commonTopBar.setRightHidden();
        commonTopBar.setLeftHidden();
        RelativeLayout relativeLayoutPersonInfo = (RelativeLayout) view.findViewById(R.id.relative_favorite);
        relativeLayoutPersonInfo.setOnClickListener(personInfoClick);
        TextView textviewExit = (TextView) view.findViewById(R.id.txt_exit);
        textviewExit.setOnClickListener(exitClick);
        // init();
        return view;
    }

    @Override
    public void onTopbarLeftLayoutSelected() {

    }

    private View.OnClickListener personInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!MyApplication.getInstance().isLogin()) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                return;
            }
            switchFragment(R.id.fragment_ctn, new PersonInfoFragment());
        }
    };
    private View.OnClickListener exitClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyDialog dialog = new MyDialog(getActivity());
            dialog.setTitleFill();
            dialog.setTitleLineHidden();
            dialog.setMessage(R.string.isexittips);
            dialog.setNegativeButton(R.string.cancel, null);
            dialog.setPositiveButton(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.getInstance().setCurrentUser(null);
                    UserConfig.clearShareConfig(getActivity());
                    MyApplication.getInstance().setLogin(false);
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });
            dialog.create(null).show();
        }
    };
}