package com.siwe.dutschedule.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseAuth;
import com.siwe.dutschedule.base.BaseMessage;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.base.C;
import com.siwe.dutschedule.model.Bbs;
import com.siwe.dutschedule.model.User;
import com.siwe.dutschedule.sqlite.BbsSqlite;
import com.siwe.dutschedule.util.AppUtil;
import com.siwe.dutschedule.view.ProgressDialog;

public class UiLogin extends BaseUi {

	private ActionBar actionBar;
	private ProgressDialog progressDialog;

	private String user_account;
	private String user_password;

	private EditText edt_account;
	private EditText edt_password;
	private Button bt_login;

	HashMap<String, String> urlParams = new HashMap<String, String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_login);
		this.initActionBar();
		this.getWidget();
		this.setEvent();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	void initActionBar() {
		actionBar = new ActionBar();
		actionBar.bt_message.setVisibility(View.GONE);
		actionBar.bt_more.setVisibility(View.GONE);
		actionBar.bt_refresh.setVisibility(View.GONE);

		actionBar.bt_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doFinish();
			}
		});
		progressDialog = new ProgressDialog(this);
	}

	void getWidget() {
		this.edt_account = (EditText) findViewById(R.id.autotv_account);
		this.edt_password = (EditText) findViewById(R.id.et_password);
		this.bt_login = (Button) findViewById(R.id.login);
	}

	void setEvent() {
		this.bt_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doTaskLogin();
			}
		});
	}

	private void doTaskLogin() {
		app.setLong(System.currentTimeMillis());
		this.user_account = this.edt_account.getText().toString();
		this.user_password = this.edt_password.getText().toString();
		if (isStringEmpty(this.user_account) || isStringEmpty(this.user_password)) {
			toastE("用户名密码不能为空!");
		} else {
			/*if (!PushUtils.hasBind(getApplicationContext())) { // 确认push服务已登录
				toastE("请确认您的网络连接正常");
				return;
			}*/
			urlParams.put("stuid", this.user_account);
			urlParams.put("pass", this.user_password);
			SharedPreferences sp = AppUtil.getSharedPreferences(this);
			urlParams.put(
					"baiduid",
					sp.getString("baiduid", "0") + "#"
							+ sp.getString("channelid", "0"));
			try {
				progressDialog.setMessage("正在登录...");
				progressDialog.show();
				this.doTaskAsync(C.task.pagelogin, C.api.pagelogin, urlParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		switch (taskId) {
		case C.task.pagelogin:
			User user = null;
			// login logic
			try {
				user = (User) message.getResult("User");
				// login success
				if (user.getName() != null) {
					BaseAuth.setUser(user);
					BaseAuth.setLogin(true);
					// login fail
				} else {
					BaseAuth.setUser(user); // set sid
					BaseAuth.setLogin(false);
					toastE(C.err.auth);
				}
			} catch (Exception e) {
				e.printStackTrace();
				toastE(C.err.auth);
			}
			// login complete
			long startTime = app.getLong();
			long loginTime = System.currentTimeMillis() - startTime;
			Log.w("LoginTime", Long.toString(loginTime));
			// turn to index
			if (BaseAuth.isLogin()) {
				progressDialog.setMessage("登录成功，正在初始化...");
				progressDialog.show();
				// 保存用户名密码
				SharedPreferences mShared = AppUtil.getSharedPreferences(this);
				Editor editor = mShared.edit();
				editor.putBoolean("isSaved", true);
				editor.putString("stuid", this.user_account);
				editor.putString("pass", this.user_password);
				editor.putString("id", BaseAuth.getUser().getId());
				editor.putString("username", BaseAuth.getUser().getName());
				editor.commit();

				doTaskAsync(C.task.defaultbbs, C.api.defaultbbs, urlParams);
			}
			break;
		case C.task.defaultbbs:
			if (!message.isSuccess()) {
				forward(UiHome.class);
				//toastE("读取失败");
				return;
			}
			toastS("读取成功");
			try {
				ArrayList<Bbs> datalist = (ArrayList<Bbs>) message
						.getResultList("Bbs");
				// 写库
				sqlite = new BbsSqlite(this);
				((BbsSqlite) sqlite).insertThisTerm(datalist);
			} catch (Exception e) {
				e.printStackTrace();
				toastE(C.err.server);
			}
			forward(UiHome.class);
			this.finish();
			break;
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// other methods

	@Override
	public void hideLoadBar() {
		// TODO Auto-generated method stub
		super.hideLoadBar();
		progressDialog.dismiss();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			doFinish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
