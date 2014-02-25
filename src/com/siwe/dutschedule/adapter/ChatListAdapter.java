package com.siwe.dutschedule.adapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseList;
import com.siwe.dutschedule.base.BaseModel;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.model.Comment;
import com.siwe.dutschedule.ui.UiUserInfo;
import com.siwe.dutschedule.util.AppUtil;

public class ChatListAdapter extends BaseList {

	public ChatListAdapter(BaseUi ui, ArrayList<? extends BaseModel> datalist) {
		super(ui, datalist);
	}

	public static class ViewHolder {
		private TextView chatname;
		private TextView chatcontent;
		private TextView chattime;
	}

	
	@Override
	public Comment getItem(int position) {
		return (Comment) datalist.get(getCount() - 1 - position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getCount() == 0) {
			return null;
		}
		Comment comment = getItem(position);

		ViewHolder holder = null;
		// if (convertView == null) {
		String userid = AppUtil.getSharedPreferences(ui).getString("id", null);
		if (comment.getUserid().equalsIgnoreCase(userid))
			convertView = ui.getLayout(R.layout.item_list_chat_to);
		else
			convertView = ui.getLayout(R.layout.item_list_chat_from);
		holder = new ViewHolder();
		holder.chatname = (TextView) convertView.findViewById(R.id.chatname);
		holder.chatcontent = (TextView) convertView
				.findViewById(R.id.chatcontent);
		holder.chattime = (TextView) convertView.findViewById(R.id.chattime);
		convertView.setTag(holder);

		holder.chatname.setText(comment.getUsername());
		holder.chatname.setOnClickListener(new MyClickListener(position));
		holder.chattime.setText(comment.getFormatTime());
		holder.chatcontent.setText(comment.getContent());

		return convertView;
	}

	private class MyClickListener implements OnClickListener {
		private int position;

		public MyClickListener(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			String userid = AppUtil.getSharedPreferences(ui).getString("id",
					" ");
			Comment comment = getItem(position);
			if (comment.getUserid().equalsIgnoreCase(userid)) {
				ui.overlay(UiUserInfo.class);
				return;
			}
			Bundle bd = new Bundle();
			ui.debugMemory("click user name:" + comment.getUsername());
			bd.putString("userid", comment.getUserid());
			ui.overlay(UiUserInfo.class, bd);

		}
	}


}
