package com.core.lib.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentPageAdapter extends FragmentPagerAdapter implements
		IconPager {

	private List<Fragment> mFragmentList;
	private String[] mTitleArray;
	private int[] mDrawableArray;

	public FragmentPageAdapter(FragmentManager fm, List<Fragment> fragmentList,
			String[] titleArray, int[] drawableArray) {
		super(fm);

		mFragmentList = fragmentList;
		mTitleArray = titleArray;
		mDrawableArray = drawableArray;
	}

	@Override
	public Fragment getItem(int index) {
		Fragment fragment = null;
		if (mFragmentList != null && mFragmentList.size() > 0) {
			fragment = mFragmentList.get(index);
		}
		return fragment;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (mFragmentList != null && mFragmentList.size() > 0) {
			count = mFragmentList.size();
		}
		return count;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		CharSequence charS = "";
		if (mTitleArray != null && mTitleArray.length > 0) {
			charS = mTitleArray[position];
		}
		return charS;
	}

	@Override
	public int getIconResId(int index) {
		int resId = 0;
		if (mDrawableArray != null && mDrawableArray.length > 0) {
			resId = mDrawableArray[index];
		}
		return resId;
	}
}
