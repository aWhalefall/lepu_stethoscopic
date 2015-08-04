package com.lepu.stethoscopic.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.core.lib.application.BaseActivity;
import com.core.lib.utils.main.LogUtilBase;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.application.MyApplication;
import com.lepu.stethoscopic.business.LoginManager;
import com.lepu.stethoscopic.config.AppConfig;
import com.lepu.stethoscopic.config.UserConfig;
import com.lepu.stethoscopic.fun.functiion.login.LoginActivity;
import com.lepu.stethoscopic.model.User;
import com.lepu.stethoscopic.utils.Const;
import com.lepu.stethoscopic.utils.DBHelper;
import com.lepu.stethoscopic.utils.Utils;

public class AppStartActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(this, R.layout.app_start_activity, null);
        if (!UserConfig.getConfigString(getApplicationContext(), Const.USER_INFO, "").equals("")) {
            User user = LoginManager.getInstance().parseUser(UserConfig.getConfigString(getApplicationContext(), Const.USER_INFO, ""));
            if (user != null) {
                MyApplication.getInstance().setLogin(true);
            }
            MyApplication.getInstance().setCurrentUser(user);
        }

        setContentView(view);
        LogUtilBase.LogD(null, "AppStartActivity=====>>onCreate");
        // 启动首页进入动画
        startAnim(view);
        // 初始化，检查数据库
        DBHelper.checkDatabase(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // 如果是启动页，屏蔽返回键
    }

    private void startAnim(View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(2000);
        view.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                init();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    private void init() {
        // 进入程序逻辑判断
        boolean appFirstUse = AppConfig.getConfigBoolean(this,
                Const.CONFIG_APP_FIRST_USE, true);

        boolean appUpgradeUse = AppConfig.getConfigBoolean(this,
                Const.CONFIG_APP_UPGRADE_USE, false);

        Intent intent = new Intent();
        intent.setClass(AppStartActivity.this, LoginActivity.class);
//        if (appFirstUse || appUpgradeUse) {
//            // 第一次启动程序，先检查sd卡文件
//            if (appFirstUse) {
//                Utils.cleanAllFile(AppStartActivity.this);
//            }
//            intent.setClass(AppStartActivity.this, AppStartActivityGuide.class);
//        } else {
////            if (MyApplication.getInstance().getCurrentUser() == null) {
//                intent.setClass(AppStartActivity.this, LoginActivity.class);
////            } else {
////                intent.setClass(AppStartActivity.this, MainActivity.class);
////            }
//        }
        startActivityAnim(intent, 0, 0);
        finishAnim(0, 0);
    }
}
