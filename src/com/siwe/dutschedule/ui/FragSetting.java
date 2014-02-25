package com.siwe.dutschedule.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseDialog;
import com.siwe.dutschedule.base.BaseFragment;
import com.siwe.dutschedule.sqlite.BbsSqlite;
import com.siwe.dutschedule.sqlite.CommentSqlite;
import com.siwe.dutschedule.sqlite.ExamSqlite;
import com.siwe.dutschedule.sqlite.ScheduleSqlite;
import com.siwe.dutschedule.sqlite.ScoreallSqlite;
import com.siwe.dutschedule.sqlite.ScorethisSqlite;
import com.siwe.dutschedule.util.AppUtil;
import com.umeng.fb.UMFeedbackService;

public class FragSetting extends BaseFragment implements OnClickListener {
	private View view;

	private Button user_information;
	private Button about;
	private Button contact;
	private Button logout;
	private BaseDialog dialog;

	private Button feedback;

	public FragSetting() {

	}

	public FragSetting(Context context) {
		super(context);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getWidgetId();
		setClickEvent();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.b_feedback:
			UMFeedbackService.setGoBackButtonVisible();
			UMFeedbackService.openUmengFeedbackSDK(baseUi);
			break;
		case R.id.b_about:
			baseUi.overlay(UiAbout.class);
			break;
		case R.id.b_user_information:
			baseUi.overlay(UiUserInfo.class);
			break;
		case R.id.b_logout:
			Bundle bd = new Bundle();
			bd.putString("title", "确认退出");
			bd.putString("message", "退出登录会删除所有用户的个人数据，是否退出？");
			bd.putBoolean("showCancel", true);
			dialog = new BaseDialog(baseUi, bd);
			dialog.setOnConfirmListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					SharedPreferences sp = AppUtil.getSharedPreferences(baseUi);
					sp.edit().putBoolean("isSaved", false).commit();
					clearDb(baseUi);
					baseUi.forward(UiLogin.class);
					baseUi.doFinish();
				}
			});
			dialog.show();
			break;
		case R.id.b_contact:
			new BaseDialog(baseUi,
					" 您可以通过以下方式联系我们:\n\n1.设置界面的“反馈问题”\n2.E-Mail: yimulinwei@gmail.com\n3.QQ: 755213779")
					.show();
			break;
		}
	}

	/*
	 * 清空数据库
	 */
	private void clearDb(Context con) {
		new BbsSqlite(con).delete(null, null);
		new CommentSqlite(con).delete(null, null);
		new ExamSqlite(con).delete(null, null);
		new ScheduleSqlite(con).delete(null, null);
		new ScorethisSqlite(con).delete(null, null);
		new ScoreallSqlite(con).delete(null, null);
	}

	public void getWidgetId() {
		contact = (Button) view.findViewById(R.id.b_contact);
		user_information = (Button) view.findViewById(R.id.b_user_information);
		about = (Button) view.findViewById(R.id.b_about);
		logout = (Button) view.findViewById(R.id.b_logout);
		feedback = (Button) view.findViewById(R.id.b_feedback);
	}

	public void setClickEvent() {
		logout.setOnClickListener(this);
		contact.setOnClickListener(this);
		user_information.setOnClickListener(this);
		about.setOnClickListener(this);
		feedback.setOnClickListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.view = inflater
				.inflate(R.layout.ui_frag_setting, container, false);
		return view;
	}

}
