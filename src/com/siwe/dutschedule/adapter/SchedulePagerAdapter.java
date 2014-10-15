package com.siwe.dutschedule.adapter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.model.Schedule;
import com.siwe.dutschedule.ui.UiClassDetail;
import com.siwe.dutschedule.util.TimeUtil;

public class SchedulePagerAdapter extends PagerAdapter {

	private BaseUi ui;
	private ArrayList<View> listViews;
	private ArrayList<Schedule> listData;
	private SparseArray<Schedule> map = new SparseArray<Schedule>();
	private int weekToShow;

	int[] claIds = { R.id.cla1, R.id.cla2, R.id.cla3, R.id.cla4, R.id.cla5 };
	int[] claNameIds = { R.id.cla1_name, R.id.cla2_name, R.id.cla3_name,
			R.id.cla4_name, R.id.cla5_name };
	int[] claDetailIds = { R.id.cla1_teach_posi, R.id.cla2_teach_posi,
			R.id.cla3_teach_posi, R.id.cla4_teach_posi, R.id.cla5_teach_posi, };

	public SchedulePagerAdapter(BaseUi ui, ArrayList<View> views,
			ArrayList<Schedule> data,int weekToShow) {
		this.ui = ui;
		this.listViews = views;
		this.listData = data;
		this.weekToShow = weekToShow;
		this.setData();
		this.setAllEvent(); // 设置课程详细跳转
	}

	private void setData() {
		int day, no;
		TextView tv_name, tv_detail;
		if (listData == null)
			return;
		map.clear();
		for (Schedule item : listData) {
			day = Integer.valueOf(item.getWeekday()) - 1;
			no = (Integer.valueOf(item.getSeque())) / 2;
			if (!TimeUtil.judgeIsTime(item,weekToShow)) { // 不再上课周内
				continue;
			}
			// 记录下所有对象
			int key = day * 10 + no;
			map.put(key, item);
			tv_name = (TextView) listViews.get(day)
					.findViewById(claNameIds[no]);
			tv_detail = (TextView) listViews.get(day).findViewById(
					claDetailIds[no]);

			tv_name.setText(item.getName());
			tv_detail.setText("地点:" + item.getPosition() + "\n上课周:"
					+ item.getWeeks());
		}
	}

	

	private void setAllEvent() {

		Button temp;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 5; j++) {
				temp = (Button) listViews.get(i).findViewById(claIds[j]);
				temp.setOnClickListener(new ClaListener(i * 10 + j));

			}
		}
	}

	private class ClaListener implements OnClickListener {
		private int index;

		public ClaListener(int i) {
			this.index = i;
		}

		@Override
		public void onClick(View arg0) {
			Schedule item = map.get(index);
			Bundle bd = new Bundle();
			if (item == null) {
				bd.putInt("index", index);
			} else
				bd.putParcelable("schedule", item);
			ui.forward(UiClassDetail.class, bd);
		}

	}

	@Override
	public int getCount() {
		return this.listViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO why return false dont show view?
		return (arg0 == arg1);
	}

	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(listViews.get(arg1));
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {

		((ViewPager) arg0).addView(listViews.get(arg1), 0);
		return listViews.get(arg1);
	}

}
