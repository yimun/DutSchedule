package com.siwe.dutschedule.base;

import java.util.Calendar;

import android.util.Log;

import com.baidu.frontia.FrontiaApplication;

public class BaseApp extends FrontiaApplication {
	
	@Override
	public void onCreate() {
		Log.d("YYY", "start application at " + Calendar.getInstance().getTimeInMillis());
		super.onCreate();
		Log.d("YYY", "end application at " + Calendar.getInstance().getTimeInMillis());
		
		// 以下是您原先的代码实现，保持不变
	}
	
	private String s;
	private long l;
	private int i;
	
	public int getInt () {
		return i;
	}
	
	public void setInt (int i) {
		this.i = i;
	}
	
	public long getLong () {
		return l;
	}
	
	public void setLong (long l) {
		this.l = l;
	}
	
	public String getString () {
		return s;
	}
	
	public void setString (String s) {
		this.s = s;
	}
}