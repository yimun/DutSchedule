package com.siwe.dutschedule.setting_edit;

import android.app.Application;

public class Flag extends Application {
	private static int flag=3;
	
	public int getState() {
		return flag;
		}	
	public static void setState(int s) {
		flag=s;
		}



}
