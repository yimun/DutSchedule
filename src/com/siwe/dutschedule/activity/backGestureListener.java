package com.siwe.dutschedule.activity;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class backGestureListener implements OnGestureListener {

	private final int FLIP_DISTANCE = 120;
	private Context context;

	public backGestureListener(Context cont) {
		this.context = cont;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
			System.out.println(" ÷ ∆∑µªÿ");
			((Activity) context).finish();
			return true;

		} else
			return false;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}