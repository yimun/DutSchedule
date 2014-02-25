package com.siwe.dutschedule.base;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.siwe.dutschedule.util.AppClient;
import com.siwe.dutschedule.util.AppUtil;
import com.siwe.dutschedule.util.HttpUtil;
import com.siwe.dutschedule.util.ToastUtil;

public class BaseService extends Service {

	public static final String ACTION_START = ".ACTION_START";
	public static final String ACTION_STOP = ".ACTION_STOP";
	public static final String ACTION_PING = ".ACTION_PING";
	public static final String HTTP_TYPE = ".HTTP_TYPE";

	public static final int TASK_COMPLETE = 0;
	public static final int TASK_ERROR = 1;


	protected Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TASK_COMPLETE:
				int taskId = msg.getData().getInt("task");
				String result = msg.getData().getString("data");
				onTaskComplete(taskId, result);
				break;
			case TASK_ERROR:
				onNetworkError();
				break;
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		debugMsg("onCreate()");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		debugMsg("onStart");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		debugMsg("onDestory");
	}

	public void sendMessage(int what, int taskId, String data) {
		Bundle b = new Bundle();
		b.putInt("task", taskId);
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
	}

	public void doTaskAsync(final int taskId, final String taskUrl) {
		SharedPreferences sp = AppUtil.getSharedPreferences(this);
		final int httpType = sp.getInt(HTTP_TYPE, 0);
		ExecutorService es = Executors.newSingleThreadExecutor();
		es.execute(new Runnable() {
			@Override
			public void run() {
				try {
					AppClient client = new AppClient(taskUrl);
					if (httpType == HttpUtil.WAP_INT) {
						client.useWap();
					}
					String httpResult = client.get();
					sendMessage(TASK_COMPLETE, taskId, httpResult);
					// onTaskComplete(taskId, AppUtil.getMessage(httpResult));
				} catch (Exception e) {
					e.printStackTrace();
					sendMessage(TASK_ERROR, taskId, null);
				}

			}
		});
	}

	public void doTaskAsync(final int taskId, final String taskUrl,
			final HashMap<String, String> taskArgs) {
		SharedPreferences sp = AppUtil.getSharedPreferences(this);
		final int httpType = sp.getInt(HTTP_TYPE, 0);
		ExecutorService es = Executors.newSingleThreadExecutor();
		es.execute(new Runnable() {
			@Override
			public void run() {
				try {
					AppClient client = new AppClient(taskUrl);
					if (httpType == HttpUtil.WAP_INT) {
						client.useWap();
					}
					String httpResult = client.post(taskArgs);
					sendMessage(TASK_COMPLETE, taskId, httpResult);
				} catch (Exception e) {
					sendMessage(TASK_ERROR, taskId, null);
					e.printStackTrace();
				}

			}
		});
	}

	public void onTaskComplete(int taskId, String message) {

	}
	
	public void onNetworkError(){
		this.toastE(C.err.network);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// static functions

	public static void start(Context ctx, Class<? extends Service> sc) {
		// get some global data
		SharedPreferences sp = AppUtil.getSharedPreferences(ctx);
		Editor editor = sp.edit();
		editor.putInt(HTTP_TYPE, HttpUtil.getNetType(ctx));
		editor.commit();
		// start service
		String actionName = sc.getName() + ACTION_START;
		Intent i = new Intent(ctx, sc);
		i.setAction(actionName);
		ctx.startService(i);
	}

	public static void stop(Context ctx, Class<? extends Service> sc) {
		String actionName = sc.getName() + ACTION_STOP;
		Intent i = new Intent(ctx, sc);
		i.setAction(actionName);
		ctx.startService(i);
	}

	public static void ping(Context ctx, Class<? extends Service> sc) {
		String actionName = sc.getName() + ACTION_PING;
		Intent i = new Intent(ctx, sc);
		i.setAction(actionName);
		ctx.startService(i);
	}

	public void toastE(String msg) {
		ToastUtil.doShowEToast(this, msg);
	}

	protected void debugMsg(String tag) {
		if (C.DEBUG_MODE) {
			Log.w(this.getClass().getSimpleName(), tag);
		}
	}
}