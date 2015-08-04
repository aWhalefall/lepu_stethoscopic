package com.core.lib.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPageAdapter extends PagerAdapter implements IconPager {
	
	private ArrayList<View> mPageViews = null;
	private String[] mTitleArray = null;
	private int[] mDrawableArray = null;

	public ViewPageAdapter(ArrayList<View> views, String[] titleArray,
			int[] drawableArray) {
		mPageViews = views;
		mTitleArray = titleArray;
		mDrawableArray = drawableArray;
	}

	@Override
	public int getCount() {
		int size = 0;
		if(mPageViews != null && mPageViews.size() > 0)
		{
			size = mPageViews.size();
		}
		return size;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(mPageViews.get(arg1));
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(mPageViews.get(arg1));
		return mPageViews.get(arg1);
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
