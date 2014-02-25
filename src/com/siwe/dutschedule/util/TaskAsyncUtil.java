package com.siwe.dutschedule.util;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;


import com.siwe.dutschedule.base.BaseHandler;
import com.siwe.dutschedule.base.BaseMessage;
import com.siwe.dutschedule.base.BaseTask;
import com.siwe.dutschedule.base.BaseTaskPool;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.base.C;

public class TaskAsyncUtil {

	protected BaseUi baseUi;
	protected BaseHandler handler;
	protected BaseTaskPool taskPool;
	
	public TaskAsyncUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.baseUi = (BaseUi) context;
		this.handler = new BaseHandler(baseUi);
		this.taskPool = new BaseTaskPool(baseUi);
	}
	
	public void doTaskAsync (int taskId, int delayTime) {
		taskPool.addTask(taskId, new BaseTask(){
			@Override
			public void onComplete () {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), null);
			}
			@Override
			public void onError (String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
			}
		}, delayTime);
	}
	
	public void doTaskAsync (int taskId, String taskUrl) {
//		baseUi.showLoadBar();
		taskPool.addTask(taskId, taskUrl, new BaseTask(){
			@Override
			public void onComplete (String httpResult) {
				try{
//					baseUi.hideLoadBar();
					
					if (httpResult != null) {
						onTaskComplete(this.getId(), AppUtil.getMessage(httpResult));
					} else if (!AppUtil.isEmptyInt(this.getId())) {
						onTaskComplete(this.getId());
					} else {
						toast(C.err.server);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					toast(e.getMessage());
				}	
			}
			@Override
			public void onError (String error) {
//				baseUi.hideLoadBar();
				onNetworkError(this.getId());
			}
		}, 0);
	}
	
	public void doTaskAsync (int taskId, String taskUrl, HashMap<String, String> taskArgs) {
		baseUi.showLoadBar();
		taskPool.addTask(taskId, taskUrl, taskArgs, new BaseTask(){
			@Override
			public void onComplete (String httpResult) {
				try{
					baseUi.hideLoadBar();
					if (httpResult != null) {
						onTaskComplete(this.getId(), AppUtil.getMessage(httpResult));
					} else if (!AppUtil.isEmptyInt(this.getId())) {
						onTaskComplete(this.getId());
					} else {
						toast(C.err.server);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					toast(e.getMessage());
				}	
			}
			@Override
			public void onError (String error) {
				baseUi.hideLoadBar();
				onNetworkError(this.getId());
			}
		}, 0);
	}
	
	public void sendMessage (int what, int taskId, String data) {
		Bundle b = new Bundle();
		b.putInt("task", taskId);
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
	}
	
	public void onTaskComplete(int taskId, BaseMessage message){

	}
	
	public void onTaskComplete(int taskId){

	}
	
	public void onNetworkError (int taskId) {
		toast(C.err.network);
	}
	
	public void toast (String msg) {
		Toast.makeText(baseUi, msg, Toast.LENGTH_SHORT).show();
	}
	
//	public void handleMessage(Message msg) {
//		try {
//			int taskId;
//			String result;
//			switch (msg.what) {
//				case BaseTask.TASK_COMPLETE:
//					ui.hideLoadBar();
//					taskId = msg.getData().getInt("task");
//					result = msg.getData().getString("data");
//					if (result != null) {
//						ui.onTaskComplete(taskId, AppUtil.getMessage(result));
//					} else if (!AppUtil.isEmptyInt(taskId)) {
//						ui.onTaskComplete(taskId);
//					} else {
//						ui.toast(C.err.message);
//					}
//					break;
//				case BaseTask.NETWORK_ERROR:
//					ui.hideLoadBar();
//					taskId = msg.getData().getInt("task");
//					ui.onNetworkError(taskId);
//					break;
//				case BaseTask.SHOW_LOADBAR:
//					ui.showLoadBar();
//					break;
//				case BaseTask.HIDE_LOADBAR:
//					ui.hideLoadBar();
//					break;
//				case BaseTask.SHOW_TOAST:
//					ui.hideLoadBar();
//					result = msg.getData().getString("data");
//					ui.toast(result);
//					break;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			ui.toast(e.getMessage());
//		}
//	}
}
