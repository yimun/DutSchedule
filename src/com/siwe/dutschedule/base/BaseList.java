package com.siwe.dutschedule.base;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseList extends BaseAdapter {
	
	protected BaseUi ui;
	protected ArrayList<? extends BaseModel> datalist;
	
	public BaseList(BaseUi ui,ArrayList<? extends BaseModel> datalist){
		this.ui = ui;
		this.datalist = datalist;
	}
	
	@Override
	public int getCount() {
		return datalist.size();
	}

	@Override
	public Object getItem(int position) {
		return datalist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
	
	abstract class ViewHolder{};

}
