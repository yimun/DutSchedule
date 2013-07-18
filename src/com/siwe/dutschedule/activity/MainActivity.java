package com.siwe.dutschedule.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.android.pushservice.richmedia.MediaListActivity;
import com.siwe.dutschedule.R;
import com.siwe.dutschedule.setting_edit.ClearAll;
import com.siwe.dutschedule.video.Videolist;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity implements OnClickListener {

	ImageButton ib1, ib2, ib3, ib4, ib5, ib6;
	Intent in;
	private ClearAll clearAll = new ClearAll();
	private MySQLiteOpenHelper myOpenHelper;
	private SQLiteDatabase mysql;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.viewbox);
		ib1 = (ImageButton) findViewById(R.id.imageButton1);
		ib2 = (ImageButton) findViewById(R.id.imageButton2);
		ib3 = (ImageButton) findViewById(R.id.imageButton3);
		ib4 = (ImageButton) findViewById(R.id.imageButton4);
		ib5 = (ImageButton) findViewById(R.id.imageButton5);
		ib6 = (ImageButton) findViewById(R.id.imageButton6);
		ib1.setOnClickListener(this);
		ib2.setOnClickListener(this);
		ib3.setOnClickListener(this);
		ib4.setOnClickListener(this);
		ib5.setOnClickListener(this);
		ib6.setOnClickListener(this);

		// 友盟自动检测更新
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		com.umeng.common.Log.LOG = true;
		UmengUpdateAgent.update(this);
		// 反馈回复提醒
		UMFeedbackService.enableNewReplyNotification(this,
				NotificationType.AlertDialog);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainview_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.item1: // 课程编辑
			startActivityForResult(new Intent(this, ClassEdit.class), 0);
			break;
		case R.id.item2: // 重新登录获取
			startActivity(new Intent(MainActivity.this, LoginActivity.class));
			break;
		case R.id.item3: // 清除缓存
			new AlertDialog.Builder(this)
					.setCancelable(false)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("清除缓存")
					.setMessage("您确认要清除所有课表和成绩信息吗?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									myOpenHelper = new MySQLiteOpenHelper(
											MainActivity.this);
									mysql = myOpenHelper.getReadableDatabase();
									clearAll.clear(mysql);

									new AlertDialog.Builder(MainActivity.this)
											.setCancelable(false)
											.setIcon(R.drawable.ic_launcher)
											.setTitle("清除成功")
											.setMessage("缓存信息已删除！")
											.setPositiveButton(
													"确定",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															dialog.cancel();
														}
													}).show();
									mysql.close();

								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).show();
			break;
		case R.id.item4: // 提交反馈
			UMFeedbackService.openUmengFeedbackSDK(this);
			break;
		case R.id.item5: // 关于
			startActivity(new Intent(MainActivity.this, AboutActivity.class));
			break;
		case R.id.item6:
			MainActivity.this.finish();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		// TODO 自动生成的方法存根
		SharedPreferences sharedPrefrences;
		switch (v.getId()) {
		case R.id.imageButton1:
			in = new Intent(MainActivity.this, ClassActivity.class);
			startActivity(in);
			break;
		case R.id.imageButton2:
			in = new Intent(MainActivity.this, JidianActivity.class);
			startActivity(in);
			break;
		case R.id.imageButton3:
			in = new Intent(MainActivity.this, KaoActivity.class);
			startActivity(in);
			break;
		case R.id.imageButton4:
			in = new Intent(MainActivity.this, NoticeActivity.class);
			startActivity(in);
			break;
		case R.id.imageButton5:
			Intent sendIntent = new Intent();
			sendIntent.setClass(this, MediaListActivity.class);
			sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(sendIntent);
			break;
		case R.id.imageButton6:
			in = new Intent(MainActivity.this, Videolist.class);
			startActivity(in);
			break;
		default:
			break;
		}
	}

	boolean isExit;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			System.exit(0);
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			isExit = false;
		}

	};

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
