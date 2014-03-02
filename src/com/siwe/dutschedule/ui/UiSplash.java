package com.siwe.dutschedule.ui;

import java.util.Calendar;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseMessage;
import com.siwe.dutschedule.base.BaseService;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.base.C;
import com.siwe.dutschedule.service.AutoLoginService;
import com.siwe.dutschedule.util.AppUtil;
import com.siwe.dutschedule.util.PushUtils;
import com.umeng.analytics.MobclickAgent;

public class UiSplash extends BaseUi {
	boolean isFirstIn;

	private static final int GO_HOME = 0;
	private static final int GO_GUIDE = 1;
	private static final int GO_LOGIN = 2;
	private static final long SPLASH_DELAY_MILLIS = 2000;
	private SharedPreferences preferences;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_LOGIN:
				forward(UiLogin.class);
				break;
			case GO_GUIDE:
				forward(UiGuide.class);
				break;
			case GO_HOME:
				forward(UiHome.class);
				break;
			}
			doFinish();
			super.handleMessage(msg);
		}
	};

	private boolean isActivityFocused = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_splash);
		preferences = AppUtil.getSharedPreferences(this);
		initUmeng();
		initBaiduPush();
		initBase();
	}

	private void initBase() {
		if (hasGetToday()) {
			initJump();
			return;
		} else
			new InitTask().execute((Void) null);
	}

	private boolean hasGetToday() {
		int lastGet = preferences.getInt("lastGet", -1);
		Calendar current = Calendar.getInstance();
		int today = current.get(Calendar.DAY_OF_YEAR);
		System.out.println(today);
		if (today == lastGet) {
			System.out.println("today");
			return true;
		} else {
			System.out.println("not today");
			return false;
		}
	}

	private void initUmeng() {
		// 错误收集
		MobclickAgent.onError(this);
	}

	private void initBaiduPush() {
		// Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
		// 通过share preference实现的绑定标志开关，如果已经成功绑定，就取消这次绑定
		// if (!PushUtils.hasBind(getApplicationContext())) {
		Log.d("YYY", "before start work at "
				+ Calendar.getInstance().getTimeInMillis());
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				PushUtils.getMetaValue(this, "api_key"));
		Log.d("YYY", "after start work at "
				+ Calendar.getInstance().getTimeInMillis());
		// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
		// PushManager.enableLbs(getApplicationContext());
		Log.d("YYY", "after enableLbs at "
				+ Calendar.getInstance().getTimeInMillis());
		// }
	}

	private void initJump() {

		C.api.setBase(preferences.getString("baseurl", ""));

		isFirstIn = preferences.getBoolean("isFirst", true);
		if (isFirstIn) {

			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);

		} else if (preferences.getBoolean("isSaved", false)) {
			// 启动登陆服务
			BaseService.start(this, AutoLoginService.class);
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);

		} else {
			mHandler.sendEmptyMessageDelayed(GO_LOGIN, SPLASH_DELAY_MILLIS);
		}
	}

	public class InitTask extends AsyncTask<Void, Void, Boolean> {
		private String resultSrc;

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// Looper.prepare();
			debugMemory("do task get base url");
			HttpGet httpRequest = new HttpGet(C.api.initsource+C.api.config);
			try {
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					resultSrc = EntityUtils.toString(httpResponse.getEntity());
					debugMemory(resultSrc);
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;

		}

		@Override
		public void onCancelled() {
			super.onCancelled();
		}

		public void onPostExecute(Boolean success) {
			if (!success) {
				showUnSuccess();
			} else
				dealResult(resultSrc);
		}

	}

	void dealResult(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			Editor editor = preferences.edit();
			Iterator<String> it = jsonObject.keys();
			while (it.hasNext()) {
				String jsonKey = it.next();
				editor.putString(jsonKey, jsonObject.getString(jsonKey));
			}
			editor.putInt("lastGet",
					Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
			editor.commit();
			initJump();
		} catch (Exception e) {
			debugMemory("dealResult");
			showUnSuccess();
		}
	}

	void showUnSuccess() {

		while (!isActivityFocused) {
			new InitTask().execute((Void) null);
		}
		new AlertDialog.Builder(this).setCancelable(false)
				.setIcon(R.drawable.ic_launcher).setTitle("大工助手")
				.setMessage("网络连接不成功，是否重试？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						new InitTask().execute((Void) null);
					}
				})
				.setNeutralButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						doFinish();
					}
				}).show();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus){
			isActivityFocused  = true;
		}
	}
}
