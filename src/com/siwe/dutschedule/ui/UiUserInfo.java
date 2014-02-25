package com.siwe.dutschedule.ui;

import java.util.HashMap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseAuth;
import com.siwe.dutschedule.base.BaseMessage;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.base.C;
import com.siwe.dutschedule.model.User;
import com.siwe.dutschedule.util.AppUtil;

public class UiUserInfo extends BaseUi {
	
	private TextView tv_name;
	private TextView tv_stuid;
	private TextView tv_grade;
	private TextView tv_department;
	private TextView tv_major;
	private ActionBar actionBar;
	private User otherUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_userinfo);
		this.initActionBar();
		this.getWidgetId();
		Bundle bd = getIntent().getExtras();
		if(bd != null){
			findViewById(R.id.flexable).setVisibility(View.GONE); // 不显示学号
			HashMap<String, String> urlParams = new HashMap<String, String>();
			urlParams.put("userid", bd.getString("userid", null));
			try {
				this.doTaskAsync(C.task.userinfo, C.api.userinfo, urlParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		if(!BaseAuth.isLogin()) {
			SharedPreferences sp = AppUtil.getSharedPreferences(this);
			tv_name.setText(sp.getString("username", ""));
			tv_stuid.setText(sp.getString("stuid", ""));
			toastE(C.err.network);
		}
		else{
			initData(BaseAuth.getUser());
		}
	}

	
	
	private void initData(User user) {
		if(user == null)
			return;
		tv_name.setText(user.getName());
		tv_stuid.setText(user.getStuid());
		tv_grade.setText(user.getGrade());
		tv_department.setText(user.getDepartment());
		tv_major.setText(user.getMajor());
		
	}

	private void initActionBar() {
		actionBar = new ActionBar();
		actionBar.setTitle("个人信息");
		actionBar.bt_message.setVisibility(View.GONE);
		actionBar.bt_more.setVisibility(View.GONE);
		actionBar.bt_refresh.setVisibility(View.GONE);
		actionBar.bt_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				doFinish();
			}
		});
		
	}

	private void getWidgetId() {
		tv_name = (TextView)findViewById(R.id.username);
		tv_stuid = (TextView)findViewById(R.id.userstuid);
		tv_grade = (TextView)findViewById(R.id.grade);
		tv_department = (TextView)findViewById(R.id.department);
		tv_major = (TextView)findViewById(R.id.major);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}



	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		switch (taskId) {
		case C.task.userinfo:
			if (!message.isSuccess()) {
				toastE("读取失败");
				return;
			}
			try {
				otherUser = null;
				otherUser = (User) message.getResult("User");
				this.initData(otherUser);
			} catch (Exception e) {
				e.printStackTrace();
				toastE(C.err.server);
			}
			break;
		}
	}
	
}
