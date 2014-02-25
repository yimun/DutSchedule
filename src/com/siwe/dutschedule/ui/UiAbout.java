package com.siwe.dutschedule.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseUi;

public class UiAbout extends BaseUi {
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_about);
		this.initActionBar();
	}

	void initActionBar() {
		actionBar = new ActionBar();
		actionBar.setTitle("关于");
		actionBar.bt_message.setVisibility(View.GONE);
		actionBar.bt_more.setVisibility(View.GONE);
		actionBar.bt_refresh.setVisibility(View.GONE);
		actionBar.bt_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				doFinish();

			}
		});
	}

}
