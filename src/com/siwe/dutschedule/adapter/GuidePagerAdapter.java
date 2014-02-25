package com.siwe.dutschedule.adapter;

import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.ui.UiLogin;
import com.siwe.dutschedule.util.AppUtil;

/**
 * 
 * @{# ViewPagerAdapter.java Create on 2013-5-2 下午11:03:39
 * 
 *     class desc: 引导页面适配
 * 
 */
public class GuidePagerAdapter extends PagerAdapter {

	// 界面列表
	private List<View> views;
	private BaseUi activity;

	public GuidePagerAdapter(List<View> views, BaseUi activity) {
		this.views = views;
		this.activity = activity;
	}

	// �?��arg1位置的界�?
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	// 获得当前界面�?
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	// 初始化arg1位置的界�?
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1), 0);
		if (arg1 == views.size() -1) {
			Button start = (Button) arg0
					.findViewById(R.id.b_start);
			start.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 设置已经引导
					setGuided();
					activity.forward(UiLogin.class);
					activity.doFinish();
				}

			});
		}
		return views.get(arg1);
	}


	/**
	 * 
	 * method desc：设置已经引导过了，下次启动不用再次引导
	 */
	private void setGuided() {
		SharedPreferences preferences = AppUtil.getSharedPreferences(activity);
		Editor editor = preferences.edit();
		// 存入数据
		editor.putBoolean("isFirst", false);
		// 提交修改
		editor.commit();
	}

	// 判断是否由对象生成界�?
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

}
