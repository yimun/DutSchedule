package com.siwe.dutschedule.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.setting_edit.Flag;
import com.siwe.dutschedule.setting_edit.SetBackgroundImage;
import com.umeng.analytics.MobclickAgent;

/**
¸ü¸ÄÊÓÍ¼±³¾°
 */
public class ChangeBackgroundActivity extends Activity {

	RelativeLayout llayout,btn1, btn2, btn4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*Flag flag = ((Flag)getApplicationContext());
		int state = flag.getState();
		switch(state){
		case 1:setTheme(R.style.CustomTheme1);
		case 2:setTheme(R.style.CustomTheme2);
		case 3:setTheme(R.style.CustomTheme3);
		default:break;
		}*/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setbackg);


		btn1 = (RelativeLayout) findViewById(R.id.select1);
		btn2 = (RelativeLayout) findViewById(R.id.select2);
	
		btn4 = (RelativeLayout) findViewById(R.id.select4);
	
		llayout = (RelativeLayout) findViewById(R.id.llayout);
		SetBackgroundImage.setTheme(ChangeBackgroundActivity.this, llayout,null);
		MyOnClickListener myListener = new MyOnClickListener();
		btn1.setOnClickListener(myListener);
		btn2.setOnClickListener(myListener);
		btn4.setOnClickListener(myListener);
		
	}

	public class MyOnClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.select1:
				SetBackgroundImage.saveBackground(ChangeBackgroundActivity.this,
						"grey");
				SetBackgroundImage.setTheme(ChangeBackgroundActivity.this, llayout,null);
				Flag.setState(1);
				break;
			case R.id.select2:
				SetBackgroundImage.saveBackground(ChangeBackgroundActivity.this,
						"blue");
				SetBackgroundImage.setTheme(ChangeBackgroundActivity.this, llayout,null);
				Flag.setState(2);
				break;
			case R.id.select4:
				SetBackgroundImage.saveBackground(ChangeBackgroundActivity.this,
						"green");
				SetBackgroundImage.setTheme(ChangeBackgroundActivity.this, llayout,null);
				Flag.setState(3);
				break;
			default:			
				break;
			}

		}
	}




	public void bt_back(View v){
		finish();

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