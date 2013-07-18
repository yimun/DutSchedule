package com.siwe.dutschedule.push;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.siwe.dutschedule.R;

/** 
 * 
 * 自定义的显示通知的Activity
 *
 */
public class CustomActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Resources resource = this.getResources();
		String pkgName = this.getPackageName();
		
		setContentView(R.layout.bpush_custom_activity);

		TextView titleView = (TextView) this.findViewById(resource.getIdentifier("title", "id", pkgName));
		TextView contentView = (TextView) this.findViewById(resource.getIdentifier("content", "id", pkgName));
		
		Intent intent = getIntent();
        String title = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
        String content = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
        
        titleView.setText(title);
        contentView.setText(content);
	}
}
