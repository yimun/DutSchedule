package com.siwe.dutschedule.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.adapter.ScorePagerAdapter;
import com.siwe.dutschedule.base.BaseAuth;
import com.siwe.dutschedule.base.BaseDialog;
import com.siwe.dutschedule.base.BaseMessage;
import com.siwe.dutschedule.base.BaseTask;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.base.C;
import com.siwe.dutschedule.model.Score;
import com.siwe.dutschedule.sqlite.ScoreallSqlite;
import com.siwe.dutschedule.sqlite.ScorethisSqlite;
import com.siwe.dutschedule.util.AppUtil;
import com.siwe.dutschedule.util.ScoreAnylize;
import com.siwe.dutschedule.view.PopupManger;

/**
 * @author linwei
 * 
 */
public class UiScore extends BaseUi {// widget

	private ActionBar actionBar;
	private ViewPager mPager;
	private ArrayList<View> listViews;
	private TextView[] bt_selector = new TextView[2];
	private ImageView cursor;

	// data
	private ArrayList<Score> listThis;
	private ArrayList<Score> listAll;

	private int screenW;
	private int currIndex = 0;
	private BaseDialog dialog;
	private PopupManger popupManger;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_score);
		this.initActionBar();
		this.initTopBtn();
		this.initCursor();
		this.initPopup();
	}

	@Override
	public void onResume() {
		super.onResume();
		popupManger.dismiss();
		this.doDbTask();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		listThis.clear();
		listAll.clear();
		listViews.clear();
	}

	private void initCursor() {
		cursor = (ImageView) findViewById(R.id.score_cursor);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenW = dm.widthPixels;
	}

	private void scrollCursor(int from, int to) {
		int one = screenW / 2;
		Animation animation = new TranslateAnimation(one * from, one * to, 0, 0);
		animation.setFillAfter(true);
		animation.setDuration(200);
		cursor.startAnimation(animation);
		this.currIndex = to;
		System.out.println("scrollCursor:" + from + "——>" + to);
	}

	private void initTopBtn() {

		bt_selector[0] = (TextView) findViewById(R.id.scorethis);
		bt_selector[1] = (TextView) findViewById(R.id.scoreall);
		bt_selector[0].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mPager.setCurrentItem(0, true);
			}
		});
		bt_selector[1].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mPager.setCurrentItem(1, true);
			}
		});
	}

	void initActionBar() {
		actionBar = new ActionBar();
		actionBar.setTitle("我的成绩");
		actionBar.bt_message.setVisibility(View.GONE);
		actionBar.bt_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onCreateOptionsMenu(null);
			}
		});
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

	private void initPopup() {
		popupManger = new PopupManger(this);
		popupManger.setAdapter(new ArrayAdapter(this,
				R.layout.item_list_popup_menu, new String[] { "平均分计算", "绩点计算",
						"新成绩提醒" }));
		popupManger.setItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				popupManger.dismiss();
				if (position == 2) { // 新成绩提醒选项
					dealWithDoInform();
				}
				if (listAll.isEmpty()) {
					toastE("没有成绩数据");
					return;
				}
				ScoreAnylize anylizer = new ScoreAnylize(listAll);
				Bundle bd = new Bundle();
				switch (position) {
				case 0:
					bd.putString("title", "平均分信息");
					bd.putString("message",
							"必修科目平均分: " + anylizer.getAverNeed()
									+ "\n所有科目平均分: " + anylizer.getAverAll());
					new BaseDialog((BaseUi) getContext(), bd).show();
					break;
				case 1:
					bd.putString("title", "绩点信息");
					bd.putString("message",
							"必修科目绩点: " + anylizer.getJidianNeed()
									+ "\n所有科目绩点: " + anylizer.getJidianAll());
					new BaseDialog((BaseUi) getContext(), bd).show();
					break;
				}
			}
		});
		popupManger.initPopup();
	}

	private void dealWithDoInform() {
		Bundle bd = new Bundle();
		if (!BaseAuth.isLogin()) {
			toastE("未连接");
			return;
		}
		if (BaseAuth.getUser().getDoinform().equals("1")) {
			bd.putString("title", "大工助手");
			bd.putString("message", "您已开通新成绩提醒功能，是否关闭？");
			bd.putBoolean("showCancel", true);
			dialog = new BaseDialog((BaseUi) getContext(), bd);
			dialog.setOnConfirmListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					doTaskAsync(C.task.doinform, C.api.doinform);
				}
			});
			dialog.show();
		} else {
			bd.putString("title", "大工助手");
			bd.putString("message", "您尚未开通新成绩提醒功能，是否开通？");
			bd.putBoolean("showCancel", true);
			dialog = new BaseDialog((BaseUi) getContext(), bd);
			dialog.setOnConfirmListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					doTaskAsync(C.task.doinform, C.api.doinform);
				}
			});
			dialog.show();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if (!popupManger.isShowing()) {
			popupManger.show(actionBar.bt_more);
		} else {
			popupManger.dismiss();
		}
		return false;
	}

	public void doDbTask() {
		this.showLoadBar();
		sqlite = new ScorethisSqlite(this);
		listThis = (ArrayList<Score>) sqlite.query(null, null, null);
		sqlite = new ScoreallSqlite(this);
		listAll = (ArrayList<Score>) sqlite.query(null, null, null);
		sendMessage(BaseTask.DB_READ_COMPLETE, C.task.db_scorethis);
	}

	@Override
	public void onDbReadComplete(int taskId) {
		switch (taskId) {
		case C.task.db_scorethis:
			break;
		case C.task.db_scoreall:
			break;
		}
		this.initPagerViewData();
	}

	protected void doTaskRefresh() {
		SharedPreferences mShared = AppUtil.getSharedPreferences(this);
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("stuid", mShared.getString("stuid", null));
		urlParams.put("pass", mShared.getString("pass", null));
		switch (currIndex) {
		case 0: // get score this term
			try {
				this.doTaskAsync(C.task.scorethis, C.api.scorethis, urlParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 1:// get all score
			try {
				this.doTaskAsync(C.task.scoreall, C.api.scoreall, urlParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}

	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		if (!message.isSuccess()) {
			toastE("获取失败");
			return;
		}
		switch (taskId) {
		case C.task.scorethis:
			try {
				toastS("刷新本学期成绩成功");
				listThis = (ArrayList<Score>) message.getResultList("Score");
				// 写库
				sqlite = new ScorethisSqlite(this);
				((ScorethisSqlite) sqlite).updateAll(listThis);
				this.initPagerViewData();
			} catch (Exception e) {
				e.printStackTrace();
				toastE(C.err.server);
			}
			break;

		case C.task.scoreall:
			try {
				toastS("刷新所有成绩成功");
				listAll = (ArrayList<Score>) message.getResultList("Score");
				// 写库
				sqlite = new ScoreallSqlite(this);
				((ScoreallSqlite) sqlite).updateAll(listAll);
				this.initPagerViewData();
			} catch (Exception e) {
				e.printStackTrace();
				toastE(C.err.server);
			}
			break;
		case C.task.doinform:
			if (message.getMessage().equalsIgnoreCase("NotInTime")) {
				toast("不在出分阶段内，服务暂时关闭");
				return;
			}
			if (BaseAuth.getUser().getDoinform().equalsIgnoreCase("1")) {
				BaseAuth.getUser().setDoinform("0");
				new BaseDialog(this, "已关闭新成绩提醒功能！").show();
			} else {
				BaseAuth.getUser().setDoinform("1");
				new BaseDialog(this, "已开启新成绩提醒功能！").show();
			}
			break;
		}
		this.initPagerViewData();
	}

	private void initPagerViewData() {
		mPager = (ViewPager) findViewById(R.id.viewpager);
		if (listThis.isEmpty() && listAll.isEmpty())
			toastE(C.err.emptydata);
		listViews = new ArrayList<View>();
		for (int i = 0; i < 2; i++) {
			listViews.add(this.getLayout(R.layout.pager_score));
		}
		mPager.setAdapter(new ScorePagerAdapter(this, listViews, listThis,
				listAll));
		mPager.setOnPageChangeListener(new MyChangeListener());
		mPager.setCurrentItem(currIndex, true);

	}

	private class MyChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			bt_selector[currIndex].setTextColor(getResources().getColor(
					R.color.global_gray));
			bt_selector[arg0].setTextColor(getResources()
					.getColor(R.color.text));

			scrollCursor(currIndex, arg0);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}
}
