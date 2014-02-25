package com.siwe.dutschedule.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseDialog;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.model.Bbs;
import com.siwe.dutschedule.model.Schedule;
import com.siwe.dutschedule.sqlite.BbsSqlite;
import com.siwe.dutschedule.sqlite.ScheduleSqlite;

public class UiClassDetail extends BaseUi {

	private ActionBar actionBar;
	private Schedule schedule;
	private Button bt_save;
	private Button bt_focus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_classdetail);
		schedule = (Schedule) getIntent().getExtras().getParcelable("schedule");
		this.initActionBar();
		bt_save = (Button) findViewById(R.id.save);
		bt_focus = (Button) findViewById(R.id.focus);

		if (schedule == null) {
			schedule = new Schedule();
			int index = getIntent().getExtras().getInt("index");
			int weekday = index / 10 + 1;
			int seque = 2 * (index % 10);
			schedule.setWeekday(String.valueOf(weekday));
			schedule.setSeque(String.valueOf(seque));
			bt_save.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getData();
					new ScheduleSqlite(UiClassDetail.this).insert(schedule);
					new BaseDialog(UiClassDetail.this, "修改成功").show();
					updateWidget();
				}
			});
			return;

		} else {
			this.initView();
			bt_save.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getData();
					new ScheduleSqlite(UiClassDetail.this).updateOne(schedule);
					new BaseDialog(UiClassDetail.this, "修改成功").show();
					updateWidget();
				}
			});

			bt_focus.setVisibility(View.VISIBLE);
			bt_focus.setOnClickListener(new OnClickListener() {

				private BaseDialog dialog;

				@Override
				public void onClick(View v) {
					BbsSqlite sqlite = new BbsSqlite(getContext());
					ArrayList<Bbs> list = (ArrayList<Bbs>) sqlite.query(null,
							"name=?", new String[] { schedule.getName() });
					Bundle bd = new Bundle();
					if (list.isEmpty()) {
						bd.putString("title", "大工助手");
						bd.putString("message", "您暂未关注该课程论坛，是否添加关注？");
						bd.putBoolean("showCancel", true);
						dialog = new BaseDialog(UiClassDetail.this, bd);
						dialog.setOnConfirmListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								Bundle bd = new Bundle();
								bd.putString("name", schedule.getName());
								forward(UiBbsSearch.class, bd);// TODO
							}
						});
						dialog.show();
					} else {
						Bbs bbs = list.get(0);
						bd.putParcelable("bbs", bbs);
						forward(UiChat.class, bd);
					}
				}

			});
		}
	}

	private void initView() {
		((EditText) findViewById(R.id.detail1)).setText(schedule.getName());
		((EditText) findViewById(R.id.detail3)).setText(schedule.getWeeks());
		((EditText) findViewById(R.id.detail4)).setText(schedule.getPosition());
		((EditText) findViewById(R.id.detail5)).setText(schedule.getCredit());
		((EditText) findViewById(R.id.detail6)).setText(schedule.getType());
		((EditText) findViewById(R.id.detail7)).setText(schedule.getTeacher());
	}

	private void getData() {
		schedule.setName(((EditText) findViewById(R.id.detail1)).getText()
				.toString());
		schedule.setWeeks(((EditText) findViewById(R.id.detail3)).getText()
				.toString());
		schedule.setPosition(((EditText) findViewById(R.id.detail4)).getText()
				.toString());
		schedule.setCredit(((EditText) findViewById(R.id.detail5)).getText()
				.toString());
		schedule.setType(((EditText) findViewById(R.id.detail6)).getText()
				.toString());
		schedule.setTeacher(((EditText) findViewById(R.id.detail7)).getText()
				.toString());
	}

	void initActionBar() {
		actionBar = new ActionBar();
		actionBar.setTitle("课程详细");
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		findViewById(R.id.TITLE).requestFocus();
	}

}
