package com.siwe.dutschedule.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.adapter.MenuUnreadListAdapter;
import com.siwe.dutschedule.base.BaseAuth;
import com.siwe.dutschedule.base.BaseMessage;
import com.siwe.dutschedule.base.BaseService;
import com.siwe.dutschedule.base.BaseTask;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.base.C;
import com.siwe.dutschedule.model.Bbs;
import com.siwe.dutschedule.service.AutoLoginService;
import com.siwe.dutschedule.sqlite.BbsSqlite;
import com.siwe.dutschedule.util.AppUtil;
import com.siwe.dutschedule.view.BidirSlidingLayout;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;

/**
 * @author linwei
 * 
 */
public class UiHome extends BaseUi {

	// /
	// /左侧控件
	// /
	private ImageView userImage;
	private TextView userName;
	private List<View> myLeftLinear;
	private Button bt_exit;
	// 左侧点击后特效
	private List<TextView> myColorText;

	// /
	// /中间控件
	// /
	private ActionBar actionBar;
	private LinearLayout frag_content;

	// /
	// /右侧控件
	// /
	private ListView listViewRight;
	public ArrayList<Bbs> listBbs = new ArrayList<Bbs>();
	private ArrayList<Bbs> listUnread = new ArrayList<Bbs>();
	private MenuUnreadListAdapter myAdapter = null;

	// ////////////////////////////////////////////////////////////////////////////////////

	private int currentSelectIndex = 0;

	public BidirSlidingLayout bidirSldingLayout;

	private boolean isexit = false;
	private boolean hastask = false;

	Timer texit = new Timer();
	TimerTask task = new TimerTask() {
		public void run() {
			isexit = false;
			hastask = true;
		}
	};

	private static int currentTapMenu = 0;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_home_center);
		getWidgetId();
		initActionBar();
		initLeftMenu();
		initContent();
		initRightMenu();
		homeClick();
	}

	private void initUmeng() {
		// 友盟自动检测更新
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		com.umeng.common.Log.LOG = true;
		UmengUpdateAgent.update(this);
		// 反馈回复提醒
		UMFeedbackService.enableNewReplyNotification(
				this, NotificationType.NotificationBar);
	}

	@Override
	protected void onResume() {
		super.onResume();
		doTaskGetBbsUnread();
		initUmeng();
		if(!BaseAuth.isLogin())
			BaseService.start(this, AutoLoginService.class);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BaseService.stop(this, AutoLoginService.class);
	}

	void getWidgetId() {
		actionBar = new ActionBar();

		userImage = (ImageView) findViewById(R.id.img_user);
		userName = (TextView) findViewById(R.id.s_tv_user_name);
		myLeftLinear = new ArrayList<View>();
		myLeftLinear.add((View) findViewById(R.id.s_linear_survey_center));
		myLeftLinear.add((View) findViewById(R.id.s_linear_friends));
		myLeftLinear.add((View) findViewById(R.id.s_linear_setting));
		myColorText = new ArrayList<TextView>();
		myColorText.add((TextView) findViewById(R.id.tv_survey_color));
		myColorText.add((TextView) findViewById(R.id.tv_friends_color));
		myColorText.add((TextView) findViewById(R.id.tv_setting_color));
		bt_exit = (Button) findViewById(R.id.exit);

		frag_content = (LinearLayout) findViewById(R.id.content);
		bidirSldingLayout = (BidirSlidingLayout) findViewById(R.id.bidir_sliding_layout);

		listViewRight = (ListView) findViewById(R.id.s_lv_right);
	}

	void initActionBar() {

		actionBar.bt_refresh.setVisibility(View.GONE);
		actionBar.bt_more.setVisibility(View.GONE);

		bidirSldingLayout.setContext(this);
		bidirSldingLayout.setLeftChangeView(actionBar.ic_mleft);

		actionBar.bt_left.setOnClickListener(new myListener());
		actionBar.bt_message.setOnClickListener(new myListener());
	}

	private void initLeftMenu() {

		bt_exit.setOnClickListener(new myListener());
		SharedPreferences sp = AppUtil.getSharedPreferences(this);
		userName.setText(sp.getString("username", "未登录"));
		userImage.setOnClickListener(new myListener());
		for (int i = 0; i < this.myLeftLinear.size(); i++)
			myLeftLinear.get(i).setOnClickListener(new myListener());

	}

	private void initContent() {
		// /中间控件

		bidirSldingLayout.setScrollEvent(frag_content);
	}

	protected void doTaskGetBbsUnread() {
		try {
			if (sqlite == null)
				sqlite = new BbsSqlite(this);
			String bbsdata = ((BbsSqlite) sqlite).getBbsData();
			if (bbsdata == null) {
				debugMemory("empty bbsdata");
				return;
			}
			HashMap<String, String> urlParams = new HashMap<String, String>();
			urlParams.put("data", bbsdata);
			this.doTaskAsync(C.task.bbsunread, C.api.bbsunread, urlParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		switch (taskId) {
		case C.task.bbsunread:
			if (!message.isSuccess())
				return;
			try {
				listBbs.clear();
				listBbs = (ArrayList<Bbs>) message.getResultList("Bbs");
				if (sqlite == null)
					sqlite = new BbsSqlite(this);
				((BbsSqlite) sqlite).updateUnread(listBbs);
				doDbTask();
			} catch (Exception e) {
				e.printStackTrace();
				toastE(e.getMessage());
			}
			break;
		}

	}

	private void doDbTask() {
		if (sqlite == null)
			sqlite = new BbsSqlite(this);
		listUnread.clear();
		listUnread.addAll(((BbsSqlite) sqlite).getUnreadBbs());
		sendMessage(BaseTask.DB_READ_COMPLETE, C.task.db_getunread);
	}

	@Override
	public void onDbReadComplete(int taskId) {
		super.onDbReadComplete(taskId);
		if (listUnread.isEmpty())
			actionBar.setIsread();
		else
			actionBar.setUnread();
		myAdapter.notifyDataSetChanged();
			
	}

	public void initRightMenu() {
		
		myAdapter = new MenuUnreadListAdapter(this, listUnread);
		listViewRight.setAdapter(myAdapter);

		listViewRight.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				bidirSldingLayout.scrollToContentFromRightMenu();
				Bbs bbs = listUnread.get(position);
				Bundle bundle = new Bundle();
				bundle.putParcelable("bbs", bbs);
				forward(UiChat.class, bundle);
			}
		});
	}

	class myListener implements OnClickListener {
		public void onClick(View v) {
			int index = -1;
			switch (v.getId()) {
			case R.id.exit:
				doFinish();
				break;
			case R.id.img_user:
				forward(UiUserInfo.class);
				break;
			case R.id.s_linear_survey_center:
				index = 0;
				break;
			case R.id.s_linear_friends:
				index = 1;
				break;
			case R.id.s_linear_setting:
				index = 2;
				break;
			case R.id.LEFT_MENU:
				if (bidirSldingLayout.isLeftLayoutVisible()) {
					bidirSldingLayout.scrollToContentFromLeftMenu();
				} else {
					bidirSldingLayout.initShowLeftState();
					bidirSldingLayout.scrollToLeftMenu();
				}
				break;
			case R.id.MESSAGE:
				if (bidirSldingLayout.isRightLayoutVisible()) {
					bidirSldingLayout.scrollToContentFromRightMenu();
				} else {
					bidirSldingLayout.initShowRightState();
					bidirSldingLayout.scrollToRightMenu();
				}
				break;
			}

			if (index != currentSelectIndex && index != -1) {

				myColorText.get(currentSelectIndex).setBackgroundColor(
						Color.rgb(41, 41, 41));
				myColorText.get(index).setBackgroundColor(
						getResources().getColor(R.color.holo_blue_light));
				myLeftLinear.get(currentSelectIndex).setBackgroundColor(
						Color.rgb(41, 41, 41));
				myLeftLinear.get(index).setBackgroundColor(Color.rgb(0, 0, 0));
				currentSelectIndex = index;
				switch (index) {
				case 0:
					currentTapMenu = 0;
					homeClick();
					break;
				case 1:
					currentTapMenu = 1;
					bbsClick();
					break;
				case 2:
					currentTapMenu = 2;
					settingClick();
					break;
				}
			}
		}
	}

	// fragment change
	public void homeClick() {
		FragMain home_frag = new FragMain(UiHome.this);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_layout, home_frag).commit();
		actionBar.setTitle("大工助手");
		if (bidirSldingLayout.isLeftLayoutVisible()) {
			bidirSldingLayout.scrollToContentFromLeftMenu();
		}
	}

	public void bbsClick() {
		bidirSldingLayout.scrollToContentFromLeftMenu();
		FragBbs bbs_frag = new FragBbs(UiHome.this);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_layout, bbs_frag).commit();
		actionBar.setTitle("课程论坛");

	}

	public void settingClick() {

		bidirSldingLayout.scrollToContentFromLeftMenu();
		FragSetting setting_frag = new FragSetting(UiHome.this);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_layout, setting_frag).commit();
		actionBar.setTitle("设置");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// DialogAPI.showExit(this);
			if (!bidirSldingLayout.isLeftLayoutVisible()) {
				if (isexit == false) {
					isexit = true;
					Toast.makeText(getApplicationContext(), "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
					if (!hastask) {
						texit.schedule(task, 2000);
					}
				} else {
					finish();
					System.exit(0);
				}
				return false;
			} else {
				bidirSldingLayout.scrollToContentFromLeftMenu();
				return false;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (bidirSldingLayout.isLeftLayoutVisible()) {
				bidirSldingLayout.scrollToContentFromLeftMenu();
			} else {
				bidirSldingLayout.initShowLeftState();
				bidirSldingLayout.scrollToLeftMenu();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
