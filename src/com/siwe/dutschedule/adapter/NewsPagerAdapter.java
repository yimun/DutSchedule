package com.siwe.dutschedule.adapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseList;
import com.siwe.dutschedule.base.BaseModel;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.model.News;
import com.siwe.dutschedule.ui.UiNews;
import com.siwe.dutschedule.ui.UiWebView;

public class NewsPagerAdapter extends PagerAdapter {

	private BaseUi baseUi;
	private ArrayList<ArrayList<News>> listNews;
	private ArrayList<View> listViews;
	private int index = 0;
	private News item;

	public NewsPagerAdapter(BaseUi baseUi, ArrayList<View> listViews,
			ArrayList<ArrayList<News>> listNews) {
		this.listNews = listNews;
		this.baseUi = baseUi;
		this.listViews = listViews;
		this.initData();
	}

	private void initData() {
		for (index = 0;index < 3;index++) {
			ArrayList<News> news = listNews.get(index);
			if (news == null)
				return;
			ListView lv = (ListView) listViews.get(index).findViewById(R.id.lv);
			lv.setAdapter(new MyAdapter(baseUi, news));
			
			lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					News mitem = listNews.get(((UiNews)baseUi).currIndex).get(arg2);
					Bundle bd = new Bundle();
					bd.putString("title", mitem.getTitle());
					bd.putString("url", mitem.getUrl());
					baseUi.forward(UiWebView.class, bd);
				}
				
			});
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
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

	class MyAdapter extends BaseList {

		public MyAdapter(BaseUi ui, ArrayList<? extends BaseModel> datalist) {
			super(ui, datalist);
		}

		private class ViewHolder {
			TextView title;
			TextView time;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = ui.getLayout(R.layout.item_list_news);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.textView1);
				holder.time = (TextView) convertView
						.findViewById(R.id.textView2);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			item = (News) datalist.get(position);
			holder.title.setText(item.getTitle());
			holder.time.setText(item.getUptime());
			return convertView;
		}

	}
}
