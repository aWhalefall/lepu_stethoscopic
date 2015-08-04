package com.lepu.stethoscopic.fun.functiion.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.lib.application.BaseActivity;
import com.core.lib.core.AsyncRequest;
import com.core.lib.utils.main.UIHelper;
import com.core.lib.utils.secure.EncodeMD5;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.application.MyApplication;
import com.lepu.stethoscopic.business.LoginManager;
import com.lepu.stethoscopic.model.Question;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guangdye on 2015/4/24.
 */
public class RegisterByProtectionActivity extends BaseActivity {
    private AsyncRequest asyncRequest = new AsyncRequest() {
        @Override
        public void RequestComplete(Object object, Object data) {
            try {
                JSONArray array = ((JSONObject) data).optJSONArray("ListInfo");
                int j = (int) Math.floor(array.length() / 2);
                for (int i = 0; i < j; i++) {
                    JSONObject item = (JSONObject) array.get(i);
                    Question question = new Question();
                    question.setSQID(item.optInt("SQID"));
                    question.setQuestion(item.optString("Question"));
                    str1.add(question);
                }

                for (int i = j; i < array.length(); i++) {
                    JSONObject item = (JSONObject) array.get(i);
                    Question question = new Question();
                    question.setSQID(item.optInt("SQID"));
                    question.setQuestion(item.optString("Question"));
                    str2.add(question);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void RequestError(Object object, int errorId, String errorMessage) {

        }
    };
    private ArrayList<Question> str1 = new ArrayList<Question>();
    private ArrayList<Question> str2 = new ArrayList<Question>();
    private Question q1, q2;
    private TextView mtxtQ1, mtxtQ2;
    private PopupWindow mPopWindow;
    private EditText edtA1, edtA2, edtNewPwd, edtNewPwdConfirm;
    private String phone;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_left:
                    finish();
                    break;
                case R.id.btn_login:
                    if (check() && q1 != null && q2 != null) {
                        register(phone, edtNewPwd.getText().toString(), q1.getSQID(), edtA1.getText().toString()
                                , q2.getSQID(), edtA2.getText().toString());
                    }
                    break;
                case R.id.layout_spiner:
                    if (mPopWindow != null && mPopWindow.isShowing()) {
                        mPopWindow.dismiss();
                    } else {
                        showQuestionAndgetQuestion(view, mtxtQ1, str1, new AsyncRequest() {
                            @Override
                            public void RequestComplete(Object object, Object data) {
                                q1 = (Question) data;
                            }

                            @Override
                            public void RequestError(Object object, int errorId, String errorMessage) {

                            }
                        });
                    }
                    break;
                case R.id.layout_spiner_q2:
                    if (mPopWindow != null && mPopWindow.isShowing()) {
                        mPopWindow.dismiss();
                    } else {
                        showQuestionAndgetQuestion(view, mtxtQ2, str2, new AsyncRequest() {

                            @Override
                            public void RequestComplete(Object object, Object data) {
                                q2 = (Question) data;
                            }

                            @Override
                            public void RequestError(Object object, int errorId, String errorMessage) {

                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private boolean check() {
        if (TextUtils.isEmpty(edtA1.getText()) || TextUtils.isEmpty(edtA2.getText())) {
            UIHelper.showToast(getApplicationContext(), R.string.answer_please);
            return false;
        }
        if (TextUtils.isEmpty(edtNewPwd.getText()) || TextUtils.isEmpty(edtNewPwdConfirm.getText())) {
            UIHelper.showToast(getApplicationContext(), R.string.pwd_cannot_null);
            return false;
        }

        if (!edtNewPwd.getText().toString().equals(edtNewPwdConfirm.getText().toString())) {
            UIHelper.showToast(getApplicationContext(), R.string.pwd_not_same);
            return false;
        }
        return true;
    }

    private void register(String phone, String pwd, int q1Id, String q1, int q2Id, String q2) {
        AsyncRequest request = new AsyncRequest() {
            @Override
            public void RequestComplete(Object object, final Object data) {
               // startActivity(new Intent(getApplicationContext(), UserCenterActivity.class));
            }

            @Override
            public void RequestError(Object object, int errorId, String errorMessage) {

            }
        };
        LoginManager.getInstance().register(request, phone, EncodeMD5.getMd5(pwd),
                "-1");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_set_protection_activity);

        LoginManager.getInstance().getSecurityQuestion(asyncRequest);
        init();
    }

    private void init() {
        View imgBack = findViewById(R.id.layout_left);
        imgBack.setOnClickListener(listener);
        phone = MyApplication.getInstance().getUser().getPhone();
        TextView txtPhone = (TextView) findViewById(R.id.txt_phone);
        txtPhone.setText(phone);

        edtA1 = (EditText) findViewById(R.id.edt_a1);
        edtA2 = (EditText) findViewById(R.id.edt_a2);
        edtNewPwd = (EditText) findViewById(R.id.edt_newPwd);
        edtNewPwdConfirm = (EditText) findViewById(R.id.edt_age);

        RelativeLayout spiner = (RelativeLayout) findViewById(R.id.layout_spiner);
        spiner.setOnClickListener(listener);
        RelativeLayout spiner2 = (RelativeLayout) findViewById(R.id.layout_spiner_q2);
        spiner2.setOnClickListener(listener);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(listener);
        mtxtQ1 = (TextView) findViewById(R.id.txt_q1);
        mtxtQ2 = (TextView) findViewById(R.id.txt_q2);
    }

    private void showQuestionAndgetQuestion(View topView, final TextView txtQuestion, final List<Question> questions, final AsyncRequest request) {

        if (questions.size() == 0) {
            return;
        }
        View view = getLayoutInflater().inflate(
                R.layout.list_popwindow, null, false);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPopWindow.dismiss();
                return false;
            }

        });
        ListView listView = (ListView) view.findViewById(R.id.dataListView);
        final String[] strs = new String[questions.size()];
        for (int i = 0; i < questions.size(); i++) {
            strs[i] = questions.get(i).getQuestion();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.list_simple, strs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPopWindow.dismiss();
                txtQuestion.setText(strs[i]);
//                question = questions.get(i);
                request.RequestComplete("", questions.get(i));
            }
        });

        mPopWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.showAsDropDown(topView);
    }
}
