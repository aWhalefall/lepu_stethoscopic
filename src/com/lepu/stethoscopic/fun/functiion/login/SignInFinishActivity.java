package com.lepu.stethoscopic.fun.functiion.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.core.lib.application.BaseActivity;
import com.core.lib.core.AsyncRequest;
import com.core.lib.utils.main.AppManager;
import com.core.lib.utils.main.UIHelper;
import com.core.lib.utils.secure.EncodeMD5;
import com.core.lib.widget.MyDialog;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.business.LoginManager;
import com.lepu.stethoscopic.widget.CommonTopBar;

/**
 * Created by guangdye on 2015/4/13.
 */
public class SignInFinishActivity extends BaseActivity implements CommonTopBar.OnCommonTopbarLeftLayoutListener {

    private EditText edtPhone;
    private EditText edtPwd, edtCheckCode;
    private int sendTimes = 0;
    private String phone = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_finish_activity);
        CommonTopBar commonTopBar = (CommonTopBar) findViewById(R.id.commonbatr);
        commonTopBar.setTopbarTitle(R.string.register);
        commonTopBar.setonCommonTopbarLeftLayoutListener(this);
        init();
    }

    private Boolean isNotGetCode = false;
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_code:
                    if (checkPhone()) {
                        sendTimes = sendTimes + 1;
                        if (sendTimes > 1 && edtPhone.getText().toString().trim().equals(phone)) {
                            isNotGetCode = true;
                            MyDialog dialog = new MyDialog(SignInFinishActivity.this);
                            LayoutInflater inflater = LayoutInflater.from(SignInFinishActivity.this);
                            View viewContent = inflater.inflate(R.layout.dialog_skip_code, null);
                            dialog.addView(viewContent);
                            dialog.setPositiveButton(R.string.app_ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //register();
                                    layoutCheckCode.setVisibility(View.INVISIBLE);
                                }
                            });
                            dialog.setNegativeButton(R.string.app_cancel, null);
                            dialog.setTitleFill();
                            dialog.create(null);
                            dialog.showMyDialog();
                        } else {
                            sendCheckCode();
                            isNotGetCode = false;
                            layoutCheckCode.setVisibility(View.VISIBLE);
                        }
                    }

                    break;
                case R.id.btn_login:
                    if (check()) {
                        register(layoutCheckCode.getVisibility() == View.INVISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private ProgressDialog dialog;

    private boolean check() {
        checkPhone();

        if (TextUtils.isEmpty(edtPwd.getText())) {
            Toast.makeText(this, getString(R.string.pwd_cannot_null), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtPwd.length() < 6) {
            Toast.makeText(this, getString(R.string.pwd_must_6), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkPhone() {
        if (edtPhone.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.phone_cannot_null), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtPhone.length() != 11) {
            Toast.makeText(this, getString(R.string.phone_must_11), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Button btnCode;
    private RelativeLayout layoutCheckCode;

    private void init() {
        layoutCheckCode = (RelativeLayout) findViewById(R.id.layoutCheckCode);
        btnCode = (Button) findViewById(R.id.btn_code);
        btnCode.setOnClickListener(listener);
        edtCheckCode = (EditText) findViewById(R.id.edtCheckCode);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtPwd = (EditText) findViewById(R.id.edt_login_pwd);
        findViewById(R.id.btn_login).setOnClickListener(listener);

//        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
//        checkBox.setChecked(true);
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (isChecked) {
//
//                } else {
//
//                }
//            }
//        });
    }

    public void register(Boolean isNotGetCode) {
        AsyncRequest request = new AsyncRequest() {
            @Override
            public void RequestComplete(Object object, final Object data) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                UIHelper.showToast(getApplicationContext(), "注册成功");
                finish();
            }
            @Override
            public void RequestError(Object object, int errorId, String errorMessage) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        };

        String code = isNotGetCode ? "-1" : edtCheckCode.getText().toString();
        if (code.equals("")) {
            UIHelper.showToast(getApplicationContext(), "验证码不能为空");
            return;
        }
        dialog = new ProgressDialog(AppManager.getAppManager().currentActivity());
        dialog.setMessage("注册中...");
        dialog.show();
        LoginManager.getInstance().register(request, edtPhone.getText().toString().trim(), EncodeMD5.getMd5(edtPwd.getText().toString()),
                code);

    }

    MyCount mc;

    @Override
    public void onStop() {
        super.onStop();
        if (mc != null) {
            mc.cancel();
        }
    }

    private void sendCheckCode() {
        AsyncRequest request = new AsyncRequest() {
            @Override
            public void RequestComplete(Object object, final Object data) {
                //UIHelper.showToast(getApplicationContext(), "已发送验证码");
            }

            @Override
            public void RequestError(Object object, int errorId, String errorMessage) {
            }
        };
        phone = edtPhone.getText().toString().trim();
        LoginManager.getInstance().sendCheckCode(request, edtPhone.getText().toString().trim());
        mc = new MyCount(60000, 1000);
        mc.start();
    }

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btnCode.setBackgroundColor(getResources().getColor(R.color.blue));
            btnCode.setTextColor(getResources().getColor(R.color.white));
            btnCode.setText("获取验证码");
            btnCode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnCode.setBackgroundColor(getResources().getColor(R.color.bg_btn_send_identify_code));
            btnCode.setTextColor(getResources().getColor(R.color.white));
//            btnCode.setText("已发送(" + millisUntilFinished / 1000 + ")");
            btnCode.setText(millisUntilFinished / 1000 + "");
            btnCode.setEnabled(false);
        }
    }

    @Override
    public void onTopbarLeftLayoutSelected() {
        finish(false);
    }
}
