package com.siwe.dutschedule.video;

import io.vov.vitamio.LibsChecker;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siwe.dutschedule.R;
import com.siwe.dutschedule.activity.backGestureListener;
import com.siwe.dutschedule.setting_edit.Utils;

public class Videolist extends Activity {

	ListView lv;
	public final String PATH = "channallist";
	GestureDetector detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list_view);
		detector = new GestureDetector(this, new backGestureListener(this));
		lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(new mAdapter());
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

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return detector.onTouchEvent(me);
	}

	// 分发手势，将listview等控件获取触摸动作的顺序延后，先执行onTouchEvent()
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		this.onTouchEvent(event);
		return super.dispatchTouchEvent(event);
	}

	class ChannalClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent in = new Intent(Videolist.this.getApplicationContext(),
					DisplayActivity.class);
			in.putExtra(PATH, Utils.channelUrl[arg2]);
			startActivity(in);
		}
	}

	class mAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Utils.channelName.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RelativeLayout menu_view = (RelativeLayout) layoutInflater.inflate(
					R.layout.tv_item, null);
			TextView tv = (TextView) menu_view.findViewById(R.id.tvtv);
			tv.setText(Utils.channelName[position]);
			return menu_view;
		}

	}

	public void bt_back(View v) {
		this.finish();
	}

	public void onResume() {
		super.onResume();
		if (!isWifiConnected(this)) { // 如果wifi未连接
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
									Videolist.this.finish();
								}
							}).show();
		}

	}

	public static boolean isWifiConnected(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetworkInfo.isConnected()) {
			return true;
		}
		return false;
	}

}
