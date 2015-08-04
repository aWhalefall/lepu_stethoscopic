package com.lepu.stethoscopic.fun.functiion.recorderData;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.core.lib.application.BaseFragment;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.business.StateChange;
import com.lepu.stethoscopic.main.activity.MainActivity;
import com.lepu.stethoscopic.widget.CommonTopBar;
import com.lepu.stethoscopic.widget.UndataViewPager;

import java.util.ArrayList;


/**
 * Created by weichyang on 2015/4/28.
 */
public class DataListFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private UndataViewPager viewPager;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_data, null, false);
        CommonTopBar commonTopBar = (CommonTopBar) view.findViewById(R.id.commonbatr);
        commonTopBar.setTopbarTitle(R.string.app_data);
        commonTopBar.setLeftHidden();
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rdiogroup_total);
        radioGroup.setOnCheckedChangeListener(this);
        init();
        return view;
    }

    NotUploadListFragment notUploadListFragment;
    UploadedListFragment uploadedListFragment;
    FragmentStatePagerAdapter adapter;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        MainActivity mainActivity = (MainActivity) activity;
        mainActivity.setDataListStateChange(new StateChange() {
            @Override
            public void onChange() {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void init() {
        notUploadListFragment = new NotUploadListFragment();
        uploadedListFragment = new UploadedListFragment();
        fragmentList.add(notUploadListFragment);
        fragmentList.add(uploadedListFragment);

        viewPager = (UndataViewPager) view.findViewById(R.id.viewPager);
        adapter = new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentList.get(i);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        viewPager.setNoScroll(true);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.layout_left:
                notUploadListFragment.loadDb();
                viewPager.setCurrentItem(0);
                break;
            case R.id.layout_right:
                uploadedListFragment.onResume();
                viewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }
}
