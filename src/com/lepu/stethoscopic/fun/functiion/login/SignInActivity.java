package com.lepu.stethoscopic.fun.functiion.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.core.lib.application.BaseActivity;
import com.core.lib.application.BaseFragmentActivity;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.application.MyApplication;
import com.lepu.stethoscopic.model.User;
import com.lepu.stethoscopic.utils.Const;

/**
 * Created by guangdye on 2015/4/13.
 */
public class SignInActivity extends BaseActivity {
//    View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.layout_left:
//                    MyApplication.getInstance().setUser(null);
//                    finish();
//                    break;
//                case R.id.txt_man:
//                    handleGender(Const.MAN);
//                    startActivity(new Intent(getApplicationContext(), SignInStepTwoActivity.class));
//                    break;
//                case R.id.txt_women:
//                    handleGender(Const.WOMEN);
//                    startActivity(new Intent(getApplicationContext(), SignInStepTwoActivity.class));
//                    break;
//
//                default:
//                    break;
//            }
//        }
//    };
//
//    private void handleGender(int gender) {
//        User user = null;
//        if (MyApplication.getInstance().getUser() == null) {
//            user = new User();
//            MyApplication.getInstance().setUser(user);
//        } else {
//            user = MyApplication.getInstance().getUser();
//        }
//        user.setGender(gender);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        BaseActivity.mNeedSwipeBack = false;
//        BaseFragmentActivity.mNeedSwipeBack = false;
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signin_gender);
//
//        init();
//    }
//
//    private void init() {
//        View imgBack = findViewById(R.id.layout_left);
//        imgBack.setOnClickListener(listener);
//        TextView txtMan = (TextView) findViewById(R.id.txt_man);
//        txtMan.setOnClickListener(listener);
//        TextView txtWomen = (TextView) findViewById(R.id.txt_women);
//        txtWomen.setOnClickListener(listener);
//    }
}
