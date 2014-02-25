package com.siwe.dutschedule.ui;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.adapter.BbsListAdapter;
import com.siwe.dutschedule.base.BaseFragment;
import com.siwe.dutschedule.base.BaseMessage;
import com.siwe.dutschedule.base.BaseTask;
import com.siwe.dutschedule.base.C;
import com.siwe.dutschedule.model.Bbs;
import com.siwe.dutschedule.sqlite.BbsSqlite;

public class FragBbs extends BaseFragment {

	private ListView listView;
	private ArrayList<Bbs> bbslist = new ArrayList<Bbs>();
	private BbsSqlite sqlite = null;
	private BbsListAdapter bbsAdapter = null;
	private RelativeLayout bt_add;

	public FragBbs() {

	}

	public FragBbs(Context context) {
		super(context);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listView = (ListView) mView.findViewById(R.id.bbslistview);
		bt_add = (RelativeLayout) baseUi.findViewById(R.id.ADD);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Bbs bbs = bbslist.get(position);
				Bundle bundle = new Bundle();
				bundle.putParcelable("bbs", bbs);
				baseUi.forward(UiChat.class, bundle);
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.ui_frag_bbs, container, false);
		return mView;
	}
	
	@Override
	public void onDestroyView(){
		super.onDestroyView();
		bt_add.setVisibility(View.GONE); // bug
	}

	@Override
	public void onResume() {
		super.onResume();
		doDBTask();
		bt_add.setVisibility(View.VISIBLE);
		bt_add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				baseUi.forward(UiBbsSearch.class);
			}
		});

	}
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		// TODO Auto-generated method stub
		super.onTaskComplete(taskId, message);
	}

	@Override
	public void onDbReadComplete(int taskId) {
		super.onDbReadComplete(taskId);
		insertData();
	}

	private void insertData() {
		if(bbslist.isEmpty())
			toastE("暂无论坛请添加关注");
		bbsAdapter = new BbsListAdapter(baseUi, bbslist);
		listView.setAdapter(bbsAdapter);
		
	}

	private void doDBTask() {
		if (sqlite == null)
			sqlite = new BbsSqlite(this.baseUi);
		bbslist.clear();
		bbslist = sqlite.queryByTime();
		sendMessage(BaseTask.DB_READ_COMPLETE, C.task.db_exam, null);
	}

}
