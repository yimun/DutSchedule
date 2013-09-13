package com.siwe.dutschedule.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.siwe.dutschedule.R;
import com.umeng.analytics.MobclickAgent;

public class AboutActivity extends Activity {
	
	//定义手势检测器实例  
    GestureDetector detector;
    //eclipse修改测试

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);
		detector = new GestureDetector(this,new backGestureListener(this)); 		
	}
	
	@Override  
    public boolean onTouchEvent(MotionEvent me)  
    {  
		return detector.onTouchEvent(me);  
    }  
	@Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        this.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }
	
	public void bt_back(View v){
		this.finish();	
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);
	}
	
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
	
	

}
