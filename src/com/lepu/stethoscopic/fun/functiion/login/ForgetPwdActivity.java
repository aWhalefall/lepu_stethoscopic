//package com.lepu.stethoscopic.fun.functiion.login;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.widget.TextView;
//
//import com.core.lib.adapter.FragmentPageAdapter;
//import com.lepu.stethoscopic.R;
//import com.lepu.stethoscopic.config.AppConfig;
//import com.lepu.stethoscopic.utils.Const;
//import com.lepu.stethoscopic.widget.UndataViewPager;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by guangdye on 2015/4/20.
// */
//public class ForgetPwdActivity extends FragmentActivity {
//
//    View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.layout_left:
//                    finish();
//                    break;
//                case R.id.txt_phone:
//                    viewPager.setCurrentItem(0);
//                    updateChooseTitle(0);
//                    break;
////                case R.id.txt_pwd:
////                    viewPager.setCurrentItem(1);
////                    updateChooseTitle(1);
////                    break;
////                case R.id.txt_artificial:
////                    viewPager.setCurrentItem(2);
////                    updateChooseTitle(2);
////                    break;
//
//                default:
//                    break;
//            }
//        }
//    };
//    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
//    private List<TextView> views;
//    private UndataViewPager viewPager;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        AppConfig.setConfig(this, Const.CONFIG_APP_FIRST_USE, false);
//        AppConfig.setConfig(this, Const.CONFIG_APP_UPGRADE_USE, false);
//
//        setContentView(R.layout.fragment_notupdata);
//        init();
//    }
//
//
//    private void init() {
//        View imgBack = findViewById(R.id.layout_left);
//        imgBack.setOnClickListener(listener);
//
//        TextView txtPhone = (TextView) findViewById(R.id.txt_phone);
//////        TextView txtPwd = (TextView) findViewById(R.id.txt_pwd);
//////        TextView txtArtificial = (TextView) findViewById(R.id.txt_artificial);
////        views = new ArrayList<TextView>();
////        views.add(txtPhone);
////        views.add(txtPwd);
////        views.add(txtArtificial);
////        txtPhone.setOnClickListener(listener);
////        txtPwd.setOnClickListener(listener);
////        txtArtificial.setOnClickListener(listener);
//
//        FindPwdByPhoneFragment findPwdByPhoneFragment = new FindPwdByPhoneFragment();
//        fragmentList.add(findPwdByPhoneFragment);
////        FindPwdByProtectionFragment fragment = new FindPwdByProtectionFragment();
////        fragmentList.add(fragment);
//        FindPwdByArtificialFragment findPwdByArtificialFragment = new FindPwdByArtificialFragment();
//        fragmentList.add(findPwdByArtificialFragment);
//
//        viewPager = (UndataViewPager) findViewById(R.id.viewPager);
//        viewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(), fragmentList, null, null));
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i2) {
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//            }
//        });
//        viewPager.setNoScroll(true);
//    }
//
//    private void updateChooseTitle(int item) {
//        for (int i = 0; i < views.size(); i++) {
//            if (i == item) {
//                views.get(i).setBackgroundColor(getResources().getColor(R.color.white));
//            } else {
//                views.get(i).setBackgroundColor(getResources().getColor(R.color.gray_text_hint));
//            }
//        }
//    }
//}
