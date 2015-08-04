package com.lepu.stethoscopic.fun.functiion.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.core.lib.application.BaseActivity;
import com.core.lib.core.AsyncRequest;
import com.core.lib.utils.main.AppManager;
import com.core.lib.utils.secure.EncodeMD5;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.application.MyApplication;
import com.lepu.stethoscopic.business.LoginManager;
import com.lepu.stethoscopic.config.UserConfig;
import com.lepu.stethoscopic.main.activity.MainActivity;
import com.lepu.stethoscopic.model.User;
import com.lepu.stethoscopic.utils.Const;
import com.lepu.stethoscopic.widget.CommonTopBar;

import org.json.JSONObject;

/**
 * Created by guangdye on 2015/4/13.
 */
public class LoginActivity extends BaseActivity implements CommonTopBar.OnCommonTopbarLeftLayoutListener {

    private EditText mEdtLoginPwd;
    private EditText mLoginPhone;

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.register: //注册界面
                    // startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    //todo 测试
                    Intent intent = new Intent(LoginActivity.this, SignInFinishActivity.class);
                    startActivity(intent);
                    break;
                case R.id.txtview_right:
                    // startActivity(new Intent(getApplicationContext(), SignInActivity.class), true);
                    break;
                case R.id.txt_forgetPwd:
                    //startActivity(new Intent(getApplicationContext(), ForgetPwdActivity.class), true);
                    break;
                case R.id.btn_login:
                    if (check()) {
                        dialog = new ProgressDialog(AppManager.getAppManager().currentActivity());
                        dialog.setMessage("登录中...");
                        dialog.show();
                        AsyncRequest request = new AsyncRequest() {
                            @Override
                            public void RequestComplete(Object object, final Object data) {
                                try {
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                    String detail = ((JSONObject) data).optJSONObject("DetailInfo").toString();
                                    User user = LoginManager.getInstance().parseUser(detail);
                                    MyApplication.getInstance().setCurrentUser(user);
                                    UserConfig.setConfig(getApplicationContext(), Const.USER_INFO, detail);
                                    MyApplication.getInstance().setLogin(true);
                                    Intent intent = new Intent(getApplication(), MainActivity.class);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void RequestError(Object object, int errorId, String errorMessage) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        };
                        LoginManager.getInstance().login(request, mLoginPhone.getText().toString().trim(), EncodeMD5.getMd5(mEdtLoginPwd.getText().toString()));
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CommonTopBar commonTopBar = (CommonTopBar) findViewById(R.id.commonbatr);
        commonTopBar.setTopbarTitle(R.string.login);
        commonTopBar.setLeftHidden();
        commonTopBar.setonCommonTopbarLeftLayoutListener(this);
        init();
        if (MyApplication.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void init() {
        TextView txtForgetPwd = (TextView) findViewById(R.id.txt_forgetPwd);
        txtForgetPwd.setOnClickListener(listener);
        TextView register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(listener);
        mEdtLoginPwd = (EditText) findViewById(R.id.edt_login_pwd);
        mLoginPhone = (EditText) findViewById(R.id.login_phone);

        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(listener);
//        RulerDialog dialog = new RulerDialog(LoginActivity.this);
//        dialog.show();
    }

    private boolean check() {
        if (mLoginPhone.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.phone_cannot_null), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mEdtLoginPwd.getText())) {
            Toast.makeText(this, getString(R.string.pwd_cannot_null), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mLoginPhone.getText().toString().trim().length() != 11) {
            Toast.makeText(this, getString(R.string.phone_must_11), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onTopbarLeftLayoutSelected() {
        finish(false);
    }
}
