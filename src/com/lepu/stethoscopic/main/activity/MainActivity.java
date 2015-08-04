package com.lepu.stethoscopic.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.core.lib.core.AsyncRequest;
import com.core.lib.utils.main.LogUtilBase;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.application.MyApplication;
import com.lepu.stethoscopic.business.StateChange;
import com.lepu.stethoscopic.common.LpFragmentTabHost;
import com.lepu.stethoscopic.config.AppConfig;
import com.lepu.stethoscopic.fun.functiion.recorderData.DataListFragment;
import com.lepu.stethoscopic.fun.functiion.measure.MeasureFragment;
import com.lepu.stethoscopic.fun.functiion.profile.MineFragment;
import com.lepu.stethoscopic.fun.functiion.login.LoginActivity;
import com.lepu.stethoscopic.main.receiver.HeadsetPlugReceiver;
import com.lepu.stethoscopic.utils.Const;

public class MainActivity extends MainActivityBase implements AsyncRequest {

    private static MainActivity mInstance = null;
    private LpFragmentTabHost mTabHost;
    private HeadsetPlugReceiver headsetPlugReceiver; //监听广播耳机的插拔
    private StateChange stateChange;
    private StateChange dataListStateChange;

    public static MainActivity getInstance() {
        return mInstance;
    }


    // 定义数组来存放按钮图片
    private int imageIds[] = {R.drawable.tab_home_heart_selector,
            R.drawable.tab_home_data_selector, R.drawable.tab_home_my_selector
    };
    // Tab选项卡的文字
    private String[] tabLabels = new String[imageIds.length];

    private Class<?> fragments[] = {MeasureFragment.class, DataListFragment.class,
            MineFragment.class};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        LogUtilBase.LogD(null, "MainActivity=====>>onCreate");
        mInstance = this;
        init();
    }


//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mInstance = null;
//        System.exit(0);
//    }

    /*
     * 在栈中被唤醒时，会进入
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //
    }

    private void init() {
        // config
        AppConfig.setConfig(this, Const.CONFIG_APP_FIRST_USE, false);
        AppConfig.setConfig(this, Const.CONFIG_APP_UPGRADE_USE, false);

        mTabHost = (LpFragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        tabLabels[0] = getString(R.string.test);
        tabLabels[1] = getString(R.string.tab_data);
        tabLabels[2] = getString(R.string.tab_my);
        for (int i = 0; i < fragments.length; i++) {
            mTabHost.addTab(mTabHost.newTabSpec(tabLabels[i]).setIndicator(
                    getIndicator(i)), fragments[i], null);
        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                updateTab();
            }
        });
        updateTab();
        /*
         * 检查更新
		 */
        // UpdateManager.getInstance().checkUpdate(Setting.getCheckUpdate(), this,
        //false);
        //uid = MyApplication.getInstance().getCurrentUser().getUserId();
        //syncBloodData();
    }

    @Override
    public void RequestComplete(Object object, Object data) {

    }

    /**
     * 获取指示器
     *
     * @param index
     * @return
     */
    private View getIndicator(int index) {
        RelativeLayout view = (RelativeLayout) View.inflate(this,
                R.layout.tab_indicator_view, null);
        TextView textView = (TextView) view.findViewById(R.id.txt_tab_name);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(imageIds[index]), null, null);
        textView.setText(tabLabels[index]);
        //textView.setTextColor(R.drawable.tab_color);
        if (index == 0) {
            TextView tab_tip = (TextView) view.findViewById(R.id.tab_tip);
            tab_tip.setVisibility(View.VISIBLE);
        }
        return view;
    }

    void updateTab() {
        // todo 判断是否登录
        //isLogin();
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            View view = mTabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.txt_tab_name);
            if (mTabHost.getCurrentTab() == i) {
                tv.setTextColor(getResources().getColor(R.color.blue));
            } else {
                tv.setTextColor(getResources().getColor(R.color.black));
            }
        }
        if (stateChange != null) {
            stateChange.onChange();
        }
        if (dataListStateChange != null) {
            dataListStateChange.onChange();
        }
    }

    private void isLogin() {
        if (!MyApplication.getInstance().isLogin()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void RequestError(Object object, int errorId, String errorMessage) {

    }

    public void setOnStateChange(StateChange stateChange) {
        this.stateChange = stateChange;
    }

    public void setDataListStateChange(StateChange stateChange) {
        this.dataListStateChange = stateChange;
    }
}
