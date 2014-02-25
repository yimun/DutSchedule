package com.siwe.dutschedule.ui;

import io.vov.vitamio.LibsChecker;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.base.BaseUi;
import com.siwe.dutschedule.util.HttpUtil;
import com.siwe.dutschedule.util.VideoList;

public class UiVideo extends BaseUi {

	ListView lv;
	public final String PATH = "channallist";
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_exam);
		initActionBar();
		lv = (ListView) findViewById(R.id.lv);
		lv.setAdapter(new ArrayAdapter(this, R.layout.item_list_video,
				R.id.tvtv, VideoList.channelName));
		lv.setOnItemClickListener(new ChannalClick());
		// 检测视频插件是否安装
		try {
			if (!LibsChecker.checkVitamioLibs(this))
				return;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			new AlertDialog.Builder(this).setMessage("安装插件失败！").create().show();
		}

	}
	
	void initActionBar() {
		actionBar = new ActionBar();
		actionBar.setTitle("大工电视墙");
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

	class ChannalClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent in = new Intent(UiVideo.this.getApplicationContext(),
					UiDisplay.class);
			in.putExtra(PATH, VideoList.channelUrl[arg2]);
			startActivity(in);
		}
	}

	public void onResume() {
		super.onResume();
		if (!HttpUtil.isWifiConnected(this)) {
			/*Bundle bd = new Bundle();
			bd.putString("title", "未链接Wifi");
			bd.putString("message", "大工电视墙依托于校园网，请连接至DLUT并登陆后重试！");
			BaseDialog dialog = new BaseDialog(this, bd);
			dialog.setOnConfirmListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					doFinish();
				}
			});
			dialog.show();*/
			new AlertDialog.Builder(this)
			.setCancelable(false)
			.setIcon(R.drawable.ic_launcher)
			.setTitle("未链接Wifi")
			.setMessage("    大工电视墙依托于校园网，请连接至DLUT并登录后重试！")
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.cancel();
							doFinish();
						}
					}).show();
		}

	}

}
