package com.siwe.dutschedule.service;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.siwe.dutschedule.base.BaseAuth;
import com.siwe.dutschedule.base.BaseMessage;
import com.siwe.dutschedule.base.BaseService;
import com.siwe.dutschedule.base.C;
import com.siwe.dutschedule.model.User;
import com.siwe.dutschedule.util.AppUtil;

public class AutoLoginService extends BaseService {

	private static final String NAME = AutoLoginService.class.getName();
	// Thread Pool Executors
	private ExecutorService execService;

	// Loop getting notice
	private boolean runLoop = true;

	private BaseMessage message;
	
	private boolean isFirst = true;
	
	@Override
	public IBinder onBind(Intent intent) {
		return super.onBind(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		execService = Executors.newSingleThreadExecutor();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if(intent == null ||intent.getAction()==null){
			debugMsg("on start and action is null");
			runLoop = false;
			stopSelf();
			return;
			
		}
		if (intent.getAction().equals(NAME + BaseService.ACTION_START)) {
			isFirst = true;
			startService();
		}else if(intent.getAction().equals(NAME + BaseService.ACTION_STOP)) {
			runLoop = false;
			stopSelf();
		}
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}

	public void startService() {

		execService.execute(new Runnable() {
			@Override
			public void run() {
				SharedPreferences sp = AppUtil
						.getSharedPreferences(getApplicationContext());
				HashMap<String, String> urlParams = new HashMap<String, String>();
				urlParams.put("stuid", sp.getString("stuid", null));
				urlParams.put("pass", sp.getString("pass", null));
				while (runLoop) {
					if (BaseAuth.isLogin()){
						BaseService.stop(getApplicationContext(),
								AutoLoginService.class);
						return;  // 不加这句下面的语句会继续执行
					}
					try {
						// get notice
						doTaskAsync(C.task.login, C.api.login, urlParams);
						// sleep 30 seconds
						Thread.sleep(30 * 1000L);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void onTaskComplete(int taskId, String result) {
		
		try {
			message = AppUtil.getMessage(result);
		} catch (Exception e1) {
			e1.printStackTrace();
			toastE(C.err.server);
			return;
		}
		User user = null;
		try {
			user = (User) message.getResult("User");
			if (user.getName() != null) {
				BaseAuth.setUser(user);
				BaseAuth.setLogin(true);
				debugMsg("连接成功");
				// login fail
			} else {
				BaseAuth.setUser(user); // set sid
				BaseAuth.setLogin(false);
//				toastE(C.err.auth);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			toastE(C.err.auth);
		}

	}

	@Override
	public void onNetworkError() {
		BaseService.stop(getApplicationContext(),
				AutoLoginService.class);
		if(isFirst){
			//toastE(C.err.network);
			isFirst = false;
		}
	}
	

}
