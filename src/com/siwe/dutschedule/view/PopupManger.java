package com.siwe.dutschedule.view;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseUi;

public class PopupManger {

	public OnItemClickListener getItemClickListener() {
		return itemClickListener;
	}

	public void setItemClickListener(OnItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}

	private BaseUi baseUi;
	private PopupWindow popupWindow;
	private View popupView;
	private ListView listView;
	private ListAdapter adapter;
	private OnItemClickListener itemClickListener;

	public ListAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(ListAdapter adapter) {
		this.adapter = adapter;
	}

	public PopupManger(BaseUi baseUi) {
		this.baseUi = baseUi;

	}

	public void initPopup() {
		popupView = baseUi.getLayout(R.layout.global_popup);
		popupView.setFocusableInTouchMode(true);
		listView = (ListView) popupView.findViewById(R.id.menulist);
		if (adapter != null)
			listView.setAdapter(adapter);
		if (itemClickListener != null)
			listView.setOnItemClickListener(itemClickListener);

		popupView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_MENU
						|| event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_BACK) {
					if (popupWindow.isShowing()) {
						popupWindow.dismiss();
					}
					return true;
				}
				return false;
			}
		});
		popupWindow = new PopupWindow(popupView,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());// 设置背景图片，不能在布局中设置，要通过代码来设置
		popupWindow.setAnimationStyle(R.style.menuAnimation);
		popupWindow.setOutsideTouchable(true);
	}

	public void show(View v) {
		if (!popupWindow.isShowing()) {
			popupWindow.showAtLocation(v, Gravity.TOP,
					LayoutParams.WRAP_CONTENT, 90);
		}
	}

	public boolean isShowing() {
		return popupWindow.isShowing();
	}

	public void dismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			try {
				popupWindow.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
