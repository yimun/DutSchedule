package com.siwe.dutschedule.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.IBinder;

import com.siwe.dutschedule.base.BaseService;
import com.siwe.dutschedule.base.C;

public class NoticeService extends BaseService {

	private static final int ID = 1000;
	private static final String NAME = NoticeService.class.getName();
	
	// Notification manager to displaying arrived push notifications 
	private NotificationManager	notiManager;
	
	// Thread Pool Executors
	private ExecutorService execService;
	
	// Loop getting notice
	private boolean runLoop = true;
	
	@Override
	public IBinder onBind(Intent intent) {
		return super.onBind(intent);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		notiManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		execService = Executors.newSingleThreadExecutor();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if (intent.getAction().equals(NAME + BaseService.ACTION_START)) {
			startService();
		}
	}
	
	@Override
	public void onDestroy() {
		runLoop = false;
	}
	
	public void startService () {
		execService.execute(new Runnable(){
			@Override
			public void run() {
				while (runLoop) {
					try {
						// get notice
						//doTaskAsync(C.task.noticeList, C.api.noticeList);
						// sleep 30 seconds
						Thread.sleep(30 * 1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}