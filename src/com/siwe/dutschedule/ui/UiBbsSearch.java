/**
 * 
 */
package com.siwe.dutschedule.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseDialog;
import com.siwe.dutschedule.base.BaseMessage;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.base.C;
import com.siwe.dutschedule.model.Bbs;
import com.siwe.dutschedule.sqlite.BbsSqlite;

/**
 * @author linwei
 *
 */
public class UiBbsSearch extends BaseUi {

	private EditText edt_search;
	private ListView lv;
	private ActionBar actionBar;
	private ArrayList<Bbs> datalist = new ArrayList<Bbs>();
	private RelativeLayout bt_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_bbssearch);
		getWidgetId();
		initActionBar();
		Bundle bd  = getIntent().getExtras();
		if(bd!=null){
			edt_search.setText(bd.getString("name"));
			doSearchTask();
		}
	}

	private void initActionBar() {
		actionBar = new ActionBar();
		actionBar.setTitle("搜索课程论坛");
		actionBar.bt_message.setVisibility(View.GONE);
		actionBar.bt_more.setVisibility(View.GONE);
		actionBar.bt_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				doFinish();
			}
		});
		actionBar.bt_refresh.setVisibility(View.GONE);
		
	}

	private void doSearchTask(){
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("name", edt_search.getText().toString());
		try {
			this.doTaskAsync(C.task.bbssearch, C.api.bbssearch, urlParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void getWidgetId() {
		edt_search = (EditText)findViewById(R.id.editText1);
		lv = (ListView)findViewById(R.id.lv);
		bt_search = (RelativeLayout)findViewById(R.id.search);
		bt_search.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				doSearchTask();
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener(){

			private BaseDialog dialog;
			private Bbs item;
			private ArrayList<Bbs> tempList = new ArrayList<Bbs>();

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				item = datalist.get(arg2);
				Bundle bd = new Bundle();
				bd.putString("title", "大工助手");
				bd.putString("message", "是否添加“"+item.getName()+"”？");
				bd.putBoolean("showCancel", true);
				dialog = new BaseDialog(UiBbsSearch.this, bd);
				dialog.setOnConfirmListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						BbsSqlite sqlite = new BbsSqlite(getContext());
				
						tempList.clear();
						tempList.add(item);
						sqlite.insertThisTerm(tempList);
						new BaseDialog((BaseUi)getContext(),"关注“"+item.getName()+"”论坛成功！").show();
					}
				});
				dialog.show();
			}
			
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		switch (taskId) {
		case C.task.bbssearch:
			if (!message.isSuccess()) {
				toastE("搜索无结果");
				return;
			}
			try {
				datalist.clear();
				datalist = (ArrayList<Bbs>) message.getResultList("Bbs");
				lv.setAdapter(new MyAdapter());
			} catch (Exception e) {
				e.printStackTrace();
				toastE(C.err.server);
			}
			break;
		}
	}

	@Override
	public void onDbReadComplete(int taskId) {
		// TODO Auto-generated method stub
		super.onDbReadComplete(taskId);
	}
	
	class MyAdapter extends BaseAdapter {

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
			Bbs item = datalist.get(arg0);
			View view = getLayout(R.layout.item_list_bbssearch);
			((TextView) view.findViewById(R.id.bbs_main_name)).setText(item.getName());
			return view;
		}

	}
	

}
