package com.siwe.dutschedule.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseList;
import com.siwe.dutschedule.base.BaseModel;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.model.Score;

public class ScorePagerAdapter extends PagerAdapter {
	private BaseUi ui;
	private ArrayList<View> listViews;
	private ArrayList<Score> listThis;
	private ArrayList<Score> listAll;

	public ScorePagerAdapter(BaseUi ui, ArrayList<View> listViews,
			ArrayList<Score> listData1, ArrayList<Score> listData2) {
		this.ui = ui;
		this.listViews = listViews;
		this.listThis = listData1;
		this.listAll = listData2;
		this.setData();
	}

	private void setData() {
		// TODO Auto-generated method stub
		ListView lvthis,lvall;
		if(!listThis.isEmpty()){
			lvthis = (ListView)listViews.get(0).findViewById(R.id.listview);
			lvthis.setAdapter(new MyAdapter(ui,listThis));
		}
		if(!listAll.isEmpty()){
			lvall = (ListView)listViews.get(1).findViewById(R.id.listview);
			lvall.setAdapter(new MyAdapter(ui,listAll));
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
	
	class MyAdapter extends BaseList{
		
		public MyAdapter(BaseUi ui, ArrayList<? extends BaseModel> datalist) {
			super(ui, datalist);
		}

		private class ViewHolder{
			TextView name;
			TextView type;
			TextView credit;
			TextView score;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = ui.getLayout(R.layout.item_list_score);
				holder = new ViewHolder();
				holder.name = (TextView)convertView.findViewById(R.id.score_name);
				holder.type = (TextView)convertView.findViewById(R.id.score_type);
				holder.credit = (TextView)convertView.findViewById(R.id.score_credit);
				holder.score = (TextView)convertView.findViewById(R.id.score_score);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			Score item = (Score) datalist.get(position);
			holder.name.setText(item.getName());
			holder.type.setText(item.getType());
			holder.credit.setText(item.getCredit());
			holder.score.setText(item.getScore());
			return convertView;
		}
		
	}
}
