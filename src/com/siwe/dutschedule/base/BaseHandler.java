package com.siwe.dutschedule.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.siwe.dutschedule.util.AppUtil;

public class BaseHandler extends Handler {

	protected BaseUi ui;

	public BaseHandler(BaseUi ui) {
		this.ui = ui;
	}

	public BaseHandler(Looper looper) {
		super(looper);
	}

	@Override
	public void handleMessage(Message msg) {
		int taskId;
		String result;
		switch (msg.what) {
		case BaseTask.TASK_COMPLETE:
			ui.hideLoadBar();
			taskId = msg.getData().getInt("task");
			result = msg.getData().getString("data");

			if (result != null) { 
				try {
					BaseMessage mess = AppUtil.getMessage(result);
					ui.onTaskComplete(taskId, mess);
				} catch (Exception e) {
					ui.toastE(C.err.jsonFormat);
					e.printStackTrace();
				}
				
			} else if (!AppUtil.isEmptyInt(taskId)) {
				ui.toastE(C.err.server);
				//ui.onTaskComplete(taskId,null);
			} else {
				ui.toastE(C.err.server);
			}
			break;
		case BaseTask.NETWORK_ERROR:
			ui.hideLoadBar();
			taskId = msg.getData().getInt("task");
			ui.onNetworkError(taskId);
			break;
		case BaseTask.SHOW_LOADBAR:
			ui.showLoadBar();
			break;
		case BaseTask.HIDE_LOADBAR:
			ui.hideLoadBar();
			break;
		case BaseTask.SHOW_TOAST:
			ui.hideLoadBar();
			result = msg.getData().getString("data");
			ui.toast(result);
			break;
		case BaseTask.DB_READ_COMPLETE:
			taskId = msg.getData().getInt("task");
			ui.hideLoadBar();
			ui.onDbReadComplete(taskId);
			break;
		}
	}

}