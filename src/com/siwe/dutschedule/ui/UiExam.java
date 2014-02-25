package com.siwe.dutschedule.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseMessage;
import com.siwe.dutschedule.base.BaseTask;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.base.C;
import com.siwe.dutschedule.model.Exam;
import com.siwe.dutschedule.sqlite.ExamSqlite;
import com.siwe.dutschedule.util.AppUtil;
import com.siwe.dutschedule.view.PopupManger;

public class UiExam extends BaseUi {

	// widget
	private ActionBar actionBar;
	private ListView listView;

	// data
	private ArrayList<Exam> datalist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_exam);
		this.initActionBar();
		this.getWidget();
	}

	private void getWidget() {

		listView = (ListView) findViewById(R.id.lv);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		this.doDbTask();
	}

	void initActionBar() {
		actionBar = new ActionBar();
		actionBar.setTitle("考试信息");
		actionBar.bt_message.setVisibility(View.GONE);
		actionBar.bt_more.setVisibility(View.GONE);
		actionBar.bt_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				doFinish();
			}
		});
		actionBar.bt_refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				doTaskRefresh();
			}
		});
	}

	public void doDbTask() {
		this.showLoadBar();
		sqlite = new ExamSqlite(this);
		datalist = (ArrayList<Exam>) sqlite.query(null, null, null);
		sendMessage(BaseTask.DB_READ_COMPLETE, C.task.db_exam);
	}

	@Override
	public void onDbReadComplete(int taskId) {
		this.initViewData();
	}

	protected void doTaskRefresh() {
		SharedPreferences mShared = AppUtil.getSharedPreferences(this);
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("stuid", mShared.getString("stuid", null));
		urlParams.put("pass", mShared.getString("pass", null));
		try {
			this.doTaskAsync(C.task.exam, C.api.exam, urlParams);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		switch (taskId) {
		case C.task.exam:
			if (!message.isSuccess()) {
				toastE("读取失败");
				return;
			}
			toastS("读取成功");
			try {
				datalist = (ArrayList<Exam>) message.getResultList("Exam");
				// 写库
				sqlite = new ExamSqlite(this);
				((ExamSqlite) sqlite).updateAll(datalist);

				this.initViewData();
			} catch (Exception e) {
				e.printStackTrace();
				toastE(C.err.server);
			}
			break;
		}
	}

	private void initViewData() {
		if (datalist.size() == 0) {
			toastE(C.err.emptydata);
			return;
		}
		listView.setAdapter(new MyListAdapter());
	}

	class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return datalist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return datalist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			Exam item = datalist.get(arg0);
			View view = getLayout(R.layout.item_list_exam);
			((TextView) view.findViewById(R.id.type)).setText(item.getType());
			((TextView) view.findViewById(R.id.score_name)).setText(item
					.getName());
			((TextView) view.findViewById(R.id.time)).setText(item.getTime());
			((TextView) view.findViewById(R.id.position)).setText(item
					.getPosition());
			return view;
		}

	}

}
