package com.core.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class MenuBarView extends LinearLayout {

	private OnMenuBarListener menuBarListener = null;

	public MenuBarView(Context context) {
		super(context);
	}

	public MenuBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init();
	}

	private void init() {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				setSelectedView(v);
			}
		};
		int count = this.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = this.getChildAt(i);
			child.setOnClickListener(listener);
		}
	}

	public boolean isSelectedByIndex(int index) {
		boolean isSelected = false;
		if (index >= 0) {
			View child = this.getChildAt(index);
			isSelected = child.isSelected();
		}
		return isSelected;
	}

	public void setSelectedIndex(int index) {
		if (index >= 0 && index < this.getChildCount())
			setSelectedView(this.getChildAt(index));
	}

	private void setSelectedView(View view) {

		if (view.isSelected())
			return;

		int count = this.getChildCount();
		int menuIndex = 0;
		for (int i = 0; i < count; i++) {
			View child = this.getChildAt(i);
			if (child == view) {
				child.setSelected(true);
				menuIndex = i;
			} else
				child.setSelected(false);
		}
		if (menuBarListener != null)
			menuBarListener.onMenuBarItemSelected(menuIndex);
	}

	public void setOnMenuBarListener(OnMenuBarListener listener) {
		menuBarListener = listener;
	}

	public interface OnMenuBarListener {
		public void onMenuBarItemSelected(int menuIndex);
	}

}
