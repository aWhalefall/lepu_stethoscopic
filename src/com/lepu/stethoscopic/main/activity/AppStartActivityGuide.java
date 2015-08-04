package com.lepu.stethoscopic.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;

import com.core.lib.adapter.ViewPageAdapter;
import com.core.lib.application.BaseActivity;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.config.AppConfig;
import com.lepu.stethoscopic.fun.functiion.login.LoginActivity;
import com.lepu.stethoscopic.utils.Const;

import java.util.ArrayList;

public class AppStartActivityGuide extends BaseActivity {

    private ViewPager mHelpViewPager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_start_activity_guide);

        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finishAnim(0, 0);
    }

    /**
     * 初始化方法
     */
    private void init() {

        View view1 = getLayoutInflater().inflate(R.layout.app_start_guide_view1,
                null);
        View view2 = getLayoutInflater().inflate(R.layout.app_start_guide_view2,
                null);
        View view3 = getLayoutInflater().inflate(R.layout.app_start_guide_view3,
                null);

        ArrayList<View> viewList = new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        mHelpViewPager = (ViewPager) findViewById(R.id.helpViewPager);
        mHelpViewPager.setAdapter(new ViewPageAdapter(viewList, null, null));
        mHelpViewPager.setOnPageChangeListener(new MyViewChangeListener());

        Button goButton = (Button) view3.findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //进入主界面后，更新配置
                AppConfig.setConfig(AppStartActivityGuide.this, Const.CONFIG_APP_FIRST_USE, false);
                AppConfig.setConfig(AppStartActivityGuide.this, Const.CONFIG_APP_UPGRADE_USE, false);

                // 进入主程序
                Intent intent = new Intent();
//                if (MyApplication.getInstance().getCurrentUser() == null) {
                    intent.setClass(AppStartActivityGuide.this, LoginActivity.class);
//                } else {
//                    intent.setClass(AppStartActivityGuide.this, MainActivity.class);
//                }
                startActivityAnim(intent, 0, 0);

                finishAnim(0, 0);
            }
        });
    }

    private class MyViewChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int index) {
            mHelpViewPager.setCurrentItem(index);
        }
    }

}
