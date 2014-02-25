package com.siwe.dutschedule.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseList;
import com.siwe.dutschedule.base.BaseModel;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.model.Bbs;

public class BbsListAdapter extends BaseList {
		
	public BbsListAdapter(BaseUi ui, ArrayList<? extends BaseModel> datalist) {
		super(ui, datalist);
	}

	public static class ViewHolder {
		private TextView mName;
		private TextView mUnread;
		private TextView mLastTime;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getCount() == 0) {
			return null;
		}
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = ui.getLayout(R.layout.item_list_bbs);
			holder = new ViewHolder();
			holder.mName = (TextView) convertView.findViewById(R.id.bbs_main_name);
			holder.mUnread = (TextView) convertView.findViewById(R.id.bbs_main_count);
			holder.mLastTime = (TextView)convertView.findViewById(R.id.lasttime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Bbs bbs = (Bbs) datalist.get(position);
		holder.mName.setText(bbs.getName());
		holder.mLastTime.setText(bbs.getFormatTime());
		if(bbs.isAllRead())
			holder.mUnread.setVisibility(View.GONE);
		else{
			holder.mUnread.setVisibility(View.VISIBLE);
			holder.mUnread.setText(bbs.getUnread());
		}
		return convertView;
	}

}
