package com.core.lib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MySlipSwitch extends View implements OnTouchListener {

	private Bitmap switch_on_Bkg, switch_off_Bkg, slip_Btn;
	private Rect on_Rect, off_Rect;

	private boolean isSlipping = false;
	private boolean isSwitchOn = false;

	private float previousX, currentX;

	private OnSwitchListener onSwitchListener;
	private boolean isSwitchListenerOn = false;
	
	private int bindDbId;
	
	public void setBindDbId(int id) {
		bindDbId = id;
	}
	
	public int getBindDbId(int id) {
		return bindDbId;
	}

	public MySlipSwitch(Context context) {
		super(context);
		init();
	}

	public MySlipSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setOnTouchListener(this);
	}

	public void setImageResource(int switchOnBkg, int switchOffBkg,
			int slipBtn) {
		switch_on_Bkg = BitmapFactory.decodeResource(getResources(),
				switchOnBkg);
		switch_off_Bkg = BitmapFactory.decodeResource(getResources(),
				switchOffBkg);
		slip_Btn = BitmapFactory.decodeResource(getResources(), slipBtn);

		on_Rect = new Rect(switch_off_Bkg.getWidth() - slip_Btn.getWidth(), 0,
				switch_off_Bkg.getWidth(), slip_Btn.getHeight());
		off_Rect = new Rect(0, 0, slip_Btn.getWidth(), slip_Btn.getHeight());
	}

	public void setSwitchState(boolean switchState) {
		isSwitchOn = switchState;
	}

	public boolean getSwitchState() {
		return isSwitchOn;
	}

	public void updateSwitchState(boolean switchState) {
		isSwitchOn = switchState;
		invalidate();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		float left_SlipBtn;

		if (currentX < (switch_on_Bkg.getWidth() / 2)) {
			canvas.drawBitmap(switch_off_Bkg, matrix, paint);
		} else {
			canvas.drawBitmap(switch_on_Bkg, matrix, paint);
		}

		//Log.d("limx debug"," isSlipping:" + isSlipping);
		
		if (isSlipping) {
			if (currentX > switch_on_Bkg.getWidth()) {
				left_SlipBtn = switch_on_Bkg.getWidth() - slip_Btn.getWidth();
			} else {
				left_SlipBtn = currentX - slip_Btn.getWidth() / 2;
			}
		} else {
			if (isSwitchOn) {
				left_SlipBtn = on_Rect.left;
			} else {
				left_SlipBtn = off_Rect.left;
			}
		}

		/*Log.d("limx debug", " left_SlipBtn:" + left_SlipBtn + " switch_on_Bkg:"
				+ switch_on_Bkg.getWidth() + " slip_Btn:" + slip_Btn.getWidth()
				+ " diff:" + (switch_on_Bkg.getWidth() - slip_Btn.getWidth()));*/

		if (left_SlipBtn < 0) {
			left_SlipBtn = 0;
		} else if (left_SlipBtn > switch_on_Bkg.getWidth()
				- slip_Btn.getWidth()) {
			left_SlipBtn = switch_on_Bkg.getWidth() - slip_Btn.getWidth();
		}

		canvas.drawBitmap(slip_Btn, left_SlipBtn, 0, paint);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		//Log.d("limx debug"," ACTION:" + event.getAction());

		switch (event.getAction()) {

		case MotionEvent.ACTION_MOVE:
			currentX = event.getX();
			break;

		case MotionEvent.ACTION_DOWN:
			if (event.getX() > switch_on_Bkg.getWidth()
					|| event.getY() > switch_on_Bkg.getHeight()) {
				return false;
			}

			isSlipping = true;
			previousX = event.getX();
			currentX = previousX;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			isSlipping = false;
			boolean previousSwitchState = isSwitchOn;

			if (event.getX() >= (switch_on_Bkg.getWidth() / 2)) {
				isSwitchOn = true;
			} else {
				isSwitchOn = false;
			}

			if (isSwitchListenerOn && (previousSwitchState != isSwitchOn)) {
				onSwitchListener.onSwitched(isSwitchOn, bindDbId);
			}
			break;

		default:
			break;
		}

		invalidate();
		return true;
	}

	public void setOnSwitchListener(OnSwitchListener listener) {
		onSwitchListener = listener;
		isSwitchListenerOn = true;
	}

	public interface OnSwitchListener {
		abstract void onSwitched(boolean isSwitchOn, int bindDbId);
	}
}
