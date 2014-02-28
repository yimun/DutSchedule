package com.siwe.dutschedule.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.adapter.ChatListAdapter;
import com.siwe.dutschedule.base.BaseDialog;
import com.siwe.dutschedule.base.BaseMessage;
import com.siwe.dutschedule.base.BaseTask;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.base.C;
import com.siwe.dutschedule.model.Bbs;
import com.siwe.dutschedule.model.Comment;
import com.siwe.dutschedule.sqlite.BbsSqlite;
import com.siwe.dutschedule.sqlite.CommentSqlite;
import com.siwe.dutschedule.util.AppUtil;
import com.siwe.dutschedule.util.TimeUtil;
import com.siwe.dutschedule.view.ChatListView;
import com.siwe.dutschedule.view.PopupManger;

public class UiChat extends BaseUi implements OnClickListener,
		ChatListView.OnRefreshListener {

	private TextView tv_title;
	private LinearLayout bt_leftmenu;
	private LinearLayout bt_rightmenu;

	private ChatListView listView;
	private EditText edt_content;
	private LinearLayout ll_tip;
	private ImageView bt_send;
	private PopupManger popupManger;

	private ArrayList<Comment> datalist = new ArrayList<Comment>();
	private ArrayList<Comment> templist = new ArrayList<Comment>();
	private ChatListAdapter listAdapter;
	private Bbs modelBbs;
	private int list_count_curr = 0;   // 用于保存刷新前的数据量
	private Comment delcomment;

	private static final int TO_REFRESH = 1;
	private static final long REFRESH_DELAY = 20000;
	private static boolean REFRESH_LOOP = true;

	private Handler schedule = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TO_REFRESH:
				doUnreadRefresh(C.task.scheduleunread);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		modelBbs = (Bbs) getIntent().getExtras().getParcelable("bbs");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_chat);
		this.initPopup();
		this.getWidgetId();
		this.initActionBar();
		this.initBottom();
		this.initListview();
		doFirstRefresh();
		this.clearUnread();
		schedule.sendEmptyMessageDelayed(TO_REFRESH, REFRESH_DELAY);
	}

	private void clearUnread() {

		BbsSqlite bbsSqlite = new BbsSqlite(this);
		bbsSqlite.clearUnread(modelBbs);
	}

	private void getWidgetId() {
		tv_title = (TextView) findViewById(R.id.TITLE);
		bt_leftmenu = (LinearLayout) findViewById(R.id.LEFT_MENU);
		bt_rightmenu = (LinearLayout) findViewById(R.id.RIGHT_MENU);

		listView = (ChatListView) findViewById(R.id.listView1);

		edt_content = (EditText) findViewById(R.id.editText1);
		ll_tip = (LinearLayout) findViewById(R.id.tip);
		bt_send = (ImageView) findViewById(R.id.send);

	}

	private void initActionBar() {
		tv_title.setText(modelBbs.getName());
		bt_leftmenu.setOnClickListener(this);
		bt_rightmenu.setOnClickListener(this);
	}

	private void initBottom() {
		edt_content.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasfocus) {
				if (hasfocus)
					ll_tip.setVisibility(View.GONE);
				else
					ll_tip.setVisibility(View.VISIBLE);
			}
		});

		bt_send.setOnClickListener(this);
	}

	private void initListview() {

		listView.setOnRefreshListener(this);
		listAdapter = new ChatListAdapter(this, datalist);
		listView.setAdapter(listAdapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener(){

			
			private BaseDialog dialog;

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String userid = AppUtil.getSharedPreferences(getContext()).getString("id",
						" ");
				delcomment = listAdapter.getItem(arg2 - 1); // onClick方法下标都从1开始啊
				if (!delcomment.getUserid().equalsIgnoreCase(userid)) {
					return false;
				}else{
					debugMemory("comment del content"+delcomment.getContent());
					Bundle bd = new Bundle();
					bd.putString("title", "大工助手");
					bd.putString("message", "是否删除该条评论？");
					bd.putBoolean("showCancel", true);
					dialog = new BaseDialog((BaseUi) getContext(), bd);
					dialog.setOnConfirmListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							HashMap<String, String> urlParams = new HashMap<String, String>();
							urlParams.put("commentid", delcomment.getId());
							doTaskAsync(C.task.delcomment, C.api.delcomment, urlParams);
						}
					});
					dialog.show();
				}
				
				return false;
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		REFRESH_LOOP = true;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * get first data
	 */
	void doFirstRefresh() {
		if (modelBbs.getUnreadInt() == 0) { // 不存在未读消息，执行本地任务
			if (sqlite == null)
				sqlite = new CommentSqlite(this);
			Comment earlierComment = new Comment();
			earlierComment.setCourseid(modelBbs.getId());
			earlierComment.setUptime(TimeUtil.getCurrent());
			datalist.addAll(((CommentSqlite) sqlite)
					.getBeforeList(earlierComment));
			sendMessage(BaseTask.DB_READ_COMPLETE, C.task.db_getlist);
		} else { // 远程刷新
			HashMap<String, String> urlParams = new HashMap<String, String>();
			urlParams.put("courseid", modelBbs.getId());
			urlParams.put("earliertime", TimeUtil.getCurrent());
			doTaskAsync(C.task.getlist, C.api.getlist, urlParams);
		}
	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		listView.endRefresh();
		switch (taskId) {
		case C.task.getlist: // //// //
			if (!message.isSuccess()) {
				toastE("到头了");
				return;
			}
			try {
				// 尾部插入
				datalist.addAll((ArrayList<Comment>) message
						.getResultList("Comment"));
				onDbReadComplete(C.task.db_getlist);
			} catch (Exception e) {
				e.printStackTrace();
				toastE(C.err.server);
			}
			break;
		case C.task.comment:// //////
			bt_send.setClickable(true);
			if (!message.isSuccess()) {
				toastE("发布失败");
				return;
			}
			edt_content.setText("");
			doUnreadRefresh(C.task.unreadlist);
			break;
		case C.task.scheduleunread: // ///////
			if (REFRESH_LOOP)
				schedule.sendEmptyMessageDelayed(TO_REFRESH, REFRESH_DELAY);
		case C.task.unreadlist: // //////////
			if (!message.isSuccess()) {
				// toastE("读取失败");
				return;
			}
			try {
				templist.clear();
				templist = (ArrayList<Comment>) message
						.getResultList("Comment");
				if(taskId == C.task.scheduleunread) // 屏蔽掉定时刷新里面返回自己的评论
					filterMine();
				// 头部插入
				datalist.addAll(0, templist);
				listAdapter.notifyDataSetChanged();
				listView.setSelection(datalist.size() - 1);
			} catch (Exception e) {
				e.printStackTrace();
				toastE(C.err.server);
			}
			break;
		case C.task.delcomment:
			if (!message.isSuccess()) {
				toastE("删除失败");
				return;
			}
			toastS("删除成功");
			datalist.remove(delcomment);
			if(sqlite==null)
				sqlite = new CommentSqlite(this);
			sqlite.delete("id=?", new String[]{delcomment.getId()});
			listAdapter.notifyDataSetChanged();
			break;
		}
	}
	
	@Override
	public void hideLoadBar() {
		super.hideLoadBar();
		bt_send.setClickable(true);
	}

	private void filterMine() {
		if (templist.isEmpty()) {
			return;
		}
		String userid = AppUtil.getSharedPreferences(getContext()).getString("id", "");
		for (int i = 0; i < templist.size(); i++) {
			if(templist.get(i).getUserid().equals(userid))
				templist.remove(i);
		}

	}

	/**
	 * 当前未读消息的刷新
	 */
	private void doUnreadRefresh(int taskId) {
		String latertime = null;
		if (datalist.isEmpty())
			latertime = modelBbs.getLastupdate();
		else
			latertime = datalist.get(0).getUptime();
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("courseid", modelBbs.getId());
		urlParams.put("latertime", latertime);
		this.doTaskAsync(taskId, C.api.unreadlist, urlParams);
	}

	@Override
	public void onDbReadComplete(int taskId) {
		super.onDbReadComplete(taskId);
		listView.endRefresh();
		listAdapter.notifyDataSetChanged();
		
		// 滑动更新当前项目
		if(datalist.isEmpty())
			return;
		if (list_count_curr < 1)
			listView.setSelection(datalist.size() - 1);
		else
			listView.setSelection(datalist.size() - list_count_curr + 1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.LEFT_MENU:
			this.doFinish();
			break;
		case R.id.RIGHT_MENU:
			this.onCreateOptionsMenu(null);
			break;
		case R.id.send:
			String content = edt_content.getText().toString();
			if (isStringEmpty(content)) {
				toastE("内容不能为空");
				return;
			}
			hideKeyBoard(edt_content);
			v.setClickable(false);
			tv_title.requestFocus();
			HashMap<String, String> urlParams = new HashMap<String, String>();
			urlParams.put("courseid", modelBbs.getId());
			urlParams.put("content", content);
			this.doTaskAsync(C.task.comment, C.api.comment, urlParams);
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if (!popupManger.isShowing()) {
			popupManger.show(findViewById(R.id.MORE));
		} else {
			popupManger.dismiss();
		}
		return false;
	}

	
	// 此方法为下拉刷新的回调
	@Override
	public void onRefresh() {

		debugMemory("onRefresh method");
		list_count_curr = datalist.size(); // 记录下上次记录条数
		Comment earlierComment = null;
		if(list_count_curr == 0){
			earlierComment = new Comment();
			earlierComment.setUptime(TimeUtil.getCurrent());
			earlierComment.setId("-1");
		}else{
			earlierComment = datalist.get(list_count_curr - 1); // 最上方的评论
		}
		
		if (sqlite == null)
			sqlite = new CommentSqlite(this);
		if (((CommentSqlite) sqlite).existsAndHasBefore(earlierComment)) {
			debugMemory("onRefresh existsAndHasBefore");
			datalist.addAll(((CommentSqlite) sqlite)
					.getBeforeList(earlierComment));
			sendMessage(BaseTask.DB_READ_COMPLETE, C.task.db_getlist);
		} else {
			debugMemory("onRefresh doRemotetask");
			HashMap<String, String> urlParams = new HashMap<String, String>();
			urlParams.put("courseid", modelBbs.getId());
			urlParams.put("earliertime", earlierComment.getUptime());
			this.doTaskAsync(C.task.getlist, C.api.getlist, urlParams);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && edt_content.isFocused()) {
			tv_title.requestFocus();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		REFRESH_LOOP = false;
		// 执行保存数据库工作
		if (datalist.isEmpty())
			return;
		if (sqlite == null)
			sqlite = new CommentSqlite(this);
		((CommentSqlite) sqlite).saveAll(datalist);
		BbsSqlite bbsSqlite = new BbsSqlite(this);
		modelBbs.setLastupdate(datalist.get(0).getUptime());
		bbsSqlite.updateTime(modelBbs);
	}

	private void initPopup() {
		popupManger = new PopupManger(this);
		popupManger.setAdapter(new ArrayAdapter(this, R.layout.item_list_popup_menu,
				new String[] { "查看历史消息", "取消关注" }));
		popupManger.setItemClickListener(new OnItemClickListener() {

			private BaseDialog dialog;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				popupManger.dismiss();

				switch (position) {
				case 0: // 查看历史消息
					if(!datalist.isEmpty()) {
						listView.setSelection(0);
					}
					onRefresh();
					break;
				case 1:// 取消关注
					Bundle bd = new Bundle();
					bd.putString("title", "大工助手");
					bd.putString("message", "确定取消关注“" + modelBbs.getName()
							+ "”？");
					bd.putBoolean("showCancel", true);
					dialog = new BaseDialog((BaseUi) getContext(), bd);
					dialog.setOnConfirmListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							BbsSqlite bbsSqlite = new BbsSqlite(getContext());
							bbsSqlite.delete("id=?",
									new String[] { modelBbs.getId() });
							toastS("成功取消关注");
							doFinish();
						}
					});
					dialog.show();
					break;
				}
			}
		});
		popupManger.initPopup();
	}

	public void hideKeyBoard(View v) {

		InputMethodManager imm = (InputMethodManager) (getContext())
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

	}

}
