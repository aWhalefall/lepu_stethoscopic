package com.lepu.stethoscopic.main.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.core.lib.application.BaseActivity;
import com.core.lib.application.BaseFragmentActivity;
import com.core.lib.utils.main.AppManager;
import com.core.lib.utils.main.CrashHandler;
import com.core.lib.utils.main.StringUtilBase;
import com.core.lib.utils.main.UmengHelper;
import com.core.lib.widget.MyDialog;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.config.AppConfig;
import com.lepu.stethoscopic.main.receiver.AppReceiver;
import com.lepu.stethoscopic.utils.Const;
import com.lepu.stethoscopic.utils.Setting;
import com.lepu.stethoscopic.utils.Utils;

public class MainActivityBase extends BaseFragmentActivity implements
        CrashHandler.CrashHandlerListener {

    /**
     * 主程序中的自动升级，等配置，抽象到外层。
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注册自定义广播
        registerAppReceiver();

        // umeng
        UmengHelper.onError(this);
        UmengHelper.updateOnlineConfig(this);

        // 初始化自定义异常
        if (!Utils.DEBUG) {
//            CrashHandler crashHandler = CrashHandler.getInstance();
//            crashHandler.init(getApplicationContext(), this);

            // 提示用户有未提交服务器的异常
            //doWithCacheException();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BaseActivity.mNeedSwipeBack = false;
        BaseFragmentActivity.mNeedSwipeBack = false;

        UmengHelper.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BaseActivity.mNeedSwipeBack = true;// true
        BaseFragmentActivity.mNeedSwipeBack = true;

        UmengHelper.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        BaseActivity.mNeedSwipeBack = false;
        BaseFragmentActivity.mNeedSwipeBack = false;
        unRegisterAppReceiver();
    }

    @Override
    public void onBackPressed() {
        backFinishCheck();
    }

    private void registerAppReceiver() {
        IntentFilter appFilter = new IntentFilter();
        appFilter.addAction(AppReceiver.ACTION_CHECK_NETWORK);
        registerReceiver(AppReceiver.getInstance(), appFilter);
    }

    private void unRegisterAppReceiver() {
        unregisterReceiver(AppReceiver.getInstance());
    }

    /*
     * 处理缓存到本地的最后一次异常
     */
    @SuppressWarnings("unused")
    private void doWithCacheException() {
        boolean hasException = AppConfig.getConfigBoolean(this,
                Const.CONFIG_APP_EXCEPTION_IF, false);
        if (hasException) {
            String exceptionString = AppConfig.getConfigString(this,
                    Const.CONFIG_APP_EXCEPTION_VALUE, "");
            if (!StringUtilBase.stringIsEmpty(exceptionString)) {
                CrashHandler instance = CrashHandler.getInstance();
                if (instance != null) {
                    instance.sendMessageToServer(exceptionString, this, true);
                }
            }
        }
    }

    private void backFinishCheck() {
        MyDialog dialog = new MyDialog(this).setTitle(R.string.app_tip)
                .setMessage(R.string.app_quit_message)
                .setPositiveButton(R.string.app_ok, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AppManager.getAppManager().AppExit(MainActivityBase.this);
                    }
                })
                .setNegativeButton(R.string.app_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

        dialog.create(null);
        dialog.showMyDialog();
    }

    // =====================自定义异常处理，抓取log传入自己的服务器中============================================================
    @Override
    public int getToastStringId() {
        return R.string.ok;
//                R.string.exception_toast_user;
    }

    @Override
    public void setException(boolean hasException, String exceptionString) {
        // 设置最后一次异常缓存到本地，
        AppConfig.setConfig(this, Const.CONFIG_APP_EXCEPTION_IF, hasException);
        AppConfig.setConfig(this, Const.CONFIG_APP_EXCEPTION_VALUE, exceptionString);
    }

    @Override
    public void showExceptionDialog(OnClickListener okClick,
                                    OnClickListener cancelClick) {
//        if (!this.isFinishing()) {
//            // show dialog
//            new MyDialog(this).setTitle(R.string.exception_tip_title)
//                    .setMessage(R.string.exception_message_desc)
//                    .setPositiveButton(R.string.exception_ok, okClick)
//                    .setNegativeButton(R.string.exception_cancel, cancelClick)
//                    .create(cancelClick).show();
//        }
    }

    @Override
    public String getUploadLogUrl() {
        return Setting.getUploadLogUrl();
    }
}
