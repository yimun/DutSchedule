package com.siwe.dutschedule.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.adapter.NewsPagerAdapter;
import com.siwe.dutschedule.base.BaseMessage;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.base.C;
import com.siwe.dutschedule.model.News;

/**
 * @author linwei
 * 
 */
public class UiNews extends BaseUi {// widget

	private ActionBar actionBar;
	private ViewPager mPager;
	private ArrayList<View> listViews = new ArrayList<View>();
	private TextView[] bt_selector = new TextView[3];
	private ImageView cursor;

	// data
	private ArrayList<ArrayList<News>> listNews = new ArrayList<ArrayList<News>>(
			); // 容量为3

	private int screenW;
	private int one;
	public int currIndex = 0;
	private int taskIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_news);
		this.initActionBar();
		this.initTopBtn();
		this.initCursor();
		this.initPagerViewData();
		doTaskRefresh();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		listNews.clear();
		listViews.clear();
	}

	private void initCursor() {
		cursor = (ImageView) findViewById(R.id.score_cursor);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenW = dm.widthPixels;
		one = screenW / 3;
	}

	private void scrollCursor(int from, int to) {
		
		Animation animation = new TranslateAnimation(one * from, one * to, 0, 0);
		animation.setFillAfter(true);
		animation.setDuration(200);
		cursor.startAnimation(animation);
		this.currIndex = to;
	}

	private void initTopBtn() {

		bt_selector[0] = (TextView) findViewById(R.id.jiaowu);
		bt_selector[1] = (TextView) findViewById(R.id.tuanwei);
		bt_selector[2] = (TextView) findViewById(R.id.chuangxin);
		
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
		bt_selector[2].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mPager.setCurrentItem(2, true);
			}
		});
	}

	void initActionBar() {
		actionBar = new ActionBar();
		actionBar.setTitle("校园公告");
		actionBar.bt_message.setVisibility(View.GONE);
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
		
		mPager = (ViewPager) findViewById(R.id.viewpager);
		for (int i = 0; i < 3; i++) {
			listViews.add(this.getLayout(R.layout.pager_news));
			listNews.add(new ArrayList<News>());
		}
	}

	protected void doTaskRefresh() {
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", String.valueOf(currIndex));
		taskIndex = currIndex;
		try {
			this.doTaskAsync(C.task.news, C.api.news, urlParams);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		if (!message.isSuccess()) {
			toastE("刷新失败");
			return;
		}
		try {
			listNews.get(taskIndex).clear();
			listNews.add(taskIndex,
					(ArrayList<News>) message.getResultList("News"));
			// toastS("刷新成功");
			this.initPagerViewData();
		} catch (Exception e) {
			e.printStackTrace();
			toastE(C.err.server);
		}
	}

	private void initPagerViewData() {
	
		mPager.setAdapter(new NewsPagerAdapter(this, listViews, listNews));
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
			if(listNews.get(arg0).isEmpty()) {
				doTaskRefresh();
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}
}
