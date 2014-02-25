package com.siwe.dutschedule.base;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.siwe.dutschedule.util.AppUtil;
import com.siwe.dutschedule.util.ToastUtil;

public class BaseFragment extends Fragment {

	protected BaseUi baseUi;
	protected BaseFragmentHandler handler;
	protected BaseTaskPool taskPool;
	protected View mView;

	public BaseFragment() {

	}

	public BaseFragment(Context context) {
		this.baseUi = (BaseUi) context;
		this.handler = new BaseFragmentHandler(this);
		this.taskPool = new BaseTaskPool(baseUi);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		debugMemory("onActivityCreated");
	}

	@Override
	public void onResume() {
		super.onResume();
		// debug memory
		debugMemory("onResume");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		debugMemory("onDestroy");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		debugMemory("onDestroyView");
	}

	public void doTaskAsync(int taskId, int delayTime) {
		this.showLoadBar();
		taskPool.addTask(taskId, new BaseTask() {
			@Override
			public void onComplete() {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), null);
			}

			@Override
			public void onError(String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
			}
		}, delayTime);
	}

	public void doTaskAsync(int taskId, String taskUrl) {
		this.showLoadBar();
		taskPool.addTask(taskId, taskUrl, new BaseTask() {
			@Override
			public void onComplete(String httpResult) {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResult);
			}

			@Override
			public void onError(String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
			}
		}, 0);
	}

	public void doTaskAsync(int taskId, String taskUrl,
			HashMap<String, String> taskArgs) {
		this.showLoadBar();
		taskPool.addTask(taskId, taskUrl, taskArgs, new BaseTask() {
			@Override
			public void onComplete(String httpResult) {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResult);
			}

			@Override
			public void onError(String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
			}
		}, 0);
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

	public void onTaskComplete(int taskId, BaseMessage message) {

	}

	public void onTaskComplete(int taskId) {

	}

	public void onNetworkError(int taskId) {
		toastE(C.err.network);
	}

	public void toastS(String msg) {
		ToastUtil.doShowSToast(baseUi, msg);
	}

	public void toastE(String msg) {
		ToastUtil.doShowEToast(baseUi, msg);
	}

	public void hideLoadBar() {
		baseUi.hideLoadBar();
	}

	public void showLoadBar() {
		baseUi.showLoadBar();
	}

	public void debugMemory(String tag) {
		if (C.DEBUG_MODE) {
			Log.w(this.getClass().getSimpleName(),
					tag + ":" + AppUtil.getUsedMemory());
		}
	}

	public void onDbReadComplete(int taskId) {
		// TODO Auto-generated method stub

	}

}
