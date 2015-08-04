package com.core.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 
 * viewpager 中多个页面，下边的线，用这个类实现
 *
 */
public class ViewPagerTab extends ViewGroup{
	
	private Context mContext;
	
	private int mWidth;
	private int mHeight;
	private Scroller mScroller;  
	
	public ViewPagerTab(Context context, AttributeSet attrs) {
		super(context, attrs);	
		this.mContext = context;
		mScroller = new Scroller(mContext);  
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(getChildCount() > 0){
			getChildAt(0).layout(0, 0, mWidth / 4, mHeight);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = MeasureSpec.getSize(widthMeasureSpec);  
        mHeight = MeasureSpec.getSize(heightMeasureSpec); 
	}

	@Override
	public void computeScroll() {
		super.computeScroll();  
        if(mScroller.computeScrollOffset()){  
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
            postInvalidate();  
        }  
	}
}
