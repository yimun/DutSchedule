package com.siwe.dutschedule.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.adapter.SchedulePagerAdapter;
import com.siwe.dutschedule.base.BaseMessage;
import com.siwe.dutschedule.base.BaseTask;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.base.C;
import com.siwe.dutschedule.model.Schedule;
import com.siwe.dutschedule.sqlite.ScheduleSqlite;
import com.siwe.dutschedule.util.AppUtil;
import com.siwe.dutschedule.util.TimeUtil;
import com.siwe.dutschedule.view.PopupManger;

public class UiSchedule extends BaseUi {

	// ViewPager
	private ViewPager mPager;// 页卡内容
	private ArrayList<View> scrollViews; // Tab页面列表

	// top button
	private Button[] btn_top = new Button[7];

	// Cursor
	private ImageView cursor;
	private int bmpW; // cursor origin width
	private int offset = 0; // 偏移量
	private int currIndex = 0; // 当前游标位置
	private int today;

	// data
	private ArrayList<Schedule> datalist = new ArrayList<Schedule>();
	private TextView tv_weekthis;
	private int currentWeek;
	private int tempWeek;

	private PopupManger popupManger;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_schedule);
		tempWeek = currentWeek = TimeUtil.getWeekOfTerm();
		today = TimeUtil.getDayOfWeek();
		this.initActionBar();
		this.initTopBtn();
		this.initCursor();

	}

	@Override
	public void onResume() {
		super.onResume();
		this.doDbTask();// init data
	}

	@Override
	public void onDbReadComplete(int taskId) {

		this.refreshViewPager(currentWeek);
		
		//如果是首次，则回到默认为当天课程
	}

	protected void doDbTask() {
		this.showLoadBar();
		sqlite = new ScheduleSqlite(this);
		datalist.clear();
		datalist.addAll((ArrayList<Schedule>) sqlite.query(null, null, null));
		sendMessage(BaseTask.DB_READ_COMPLETE, C.task.db_schedule);
	}

	private void doTaskRefresh() {
		SharedPreferences mShared = AppUtil.getSharedPreferences(this);
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("stuid", mShared.getString("stuid", null));
		urlParams.put("pass", mShared.getString("pass", null));
		try {
			this.doTaskAsync(C.task.schedule, C.api.schedule, urlParams);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		switch (taskId) {
		case C.task.schedule:
			if (!message.isSuccess()) {
				toastE("读取失败");
				return;
			}
			toastS("读取成功");
			try {
				datalist.clear();
				datalist.addAll((ArrayList<Schedule>) message
						.getResultList("Schedule"));
				// 写库
				sqlite = new ScheduleSqlite(this);
				((ScheduleSqlite) sqlite).updateAll(datalist);

				this.refreshViewPager(currentWeek);
				updateWidget();
			} catch (Exception e) {
				e.printStackTrace();
				toastE(C.err.server);
			}
			break;
		}
	}

	// ////////////////////
	void initActionBar() {
		tv_weekthis = (TextView) findViewById(R.id.weektext);
		findViewById(R.id.LEFT_MENU).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				forward(UiHome.class);
				doFinish();
			}
		});
		findViewById(R.id.REFRESH).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				doTaskRefresh();
			}
		});
		findViewById(R.id.THISWEEK).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				initPopup();
				popupManger.show(tv_weekthis);
			}
		});
	}

	// //////////////////
	private void initTopBtn() {

		int[] topIds = new int[] { R.id.text1, R.id.text2, R.id.text3,
				R.id.text4, R.id.text5, R.id.text6, R.id.text7 };
		for (int i = 0; i < 7; i++) {
			btn_top[i] = (Button) findViewById(topIds[i]);
			btn_top[i].setOnClickListener(new MyTopListener(i));
		}
	}

	private class MyTopListener implements OnClickListener {
		int position;

		public MyTopListener(int i) {
			this.position = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(position, true); // smooth move true
		}
	}

	// ///////////////////////
	private void initCursor() {

		cursor = (ImageView) findViewById(R.id.cursor);
		// get cursor width
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor)
				.getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels; // 获取分辨率宽度
		offset = (screenW / 7 - bmpW) / 2; // 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);

	}

	private void scrollCursor(int from, int to) {
		int one = offset * 2 + bmpW;
		Animation animation = new TranslateAnimation(one * from, one * to, 0, 0);
		animation.setFillAfter(true);
		animation.setDuration(300);
		cursor.startAnimation(animation);
		this.currIndex = to;
		System.out.println("scrollCursor:" + from + "——>" + to);
	}

	// //////////////////////
	private void refreshViewPager(int week) {
		if (datalist.size() == 0)
			toastE(C.err.emptydata);
		if (week < 1)
			this.tv_weekthis.setText("假期");
		else
			this.tv_weekthis.setText("第" + week + "周");

		mPager = (ViewPager) findViewById(R.id.pager);
		scrollViews = new ArrayList<View>();
		for (int i = 0; i < 7; i++) {
			scrollViews.add(this.getLayout(R.layout.pager_schedule));
		}
		mPager.setAdapter(new SchedulePagerAdapter(this, scrollViews, datalist,
				week));
		mPager.setOnPageChangeListener(new MyChangeListener());
		mPager.setCurrentItem(today, true);
		scrollCursor(currIndex,today);
		
	}

	private class MyChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			scrollCursor(currIndex, arg0);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}

	private void initPopup() {
		popupManger = new PopupManger(this);
		popupManger.setAdapter(new ArrayAdapter(this,
				R.layout.item_list_popup_menu, new String[] { "上一周", "当前周",
						"下一周" }));
		popupManger.setItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				popupManger.dismiss();
				switch (position) {
				case 0:
					refreshViewPager(--tempWeek);
					break;
				case 1:
					refreshViewPager(currentWeek);
					tempWeek = currentWeek;
					break;
				case 2:
					refreshViewPager(++tempWeek);
					break;
				}
			}
		});
		popupManger.initPopup();
	}

}
